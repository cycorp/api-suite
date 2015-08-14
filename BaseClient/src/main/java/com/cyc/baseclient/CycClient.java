package com.cyc.baseclient;

/*
 * #%L
 * File: CycClient.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

//// External Imports
import com.cyc.base.BaseClientRuntimeException;
import com.cyc.baseclient.kbtool.CycObjectTool;
import com.cyc.base.CycAccess;
import com.cyc.base.CycConnectionException;
import com.cyc.base.conn.Worker;
import com.cyc.base.conn.CycConnectionInterface;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import com.cyc.baseclient.api.CfaslInputStream;
import com.cyc.baseclient.api.CfaslOutputStream;
import com.cyc.baseclient.api.CycApiClosedConnectionException;
import com.cyc.baseclient.api.CycConnection;
import static com.cyc.baseclient.api.SubLAPIHelper.makeSubLStmt;
import com.cyc.baseclient.comm.Comm;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.GuidImpl;
import com.cyc.baseclient.inference.params.DefaultInferenceParameterDescriptions;
import com.cyc.baseclient.util.Log;
import com.cyc.baseclient.util.PasswordManager;
import com.cyc.base.conn.LeaseManager;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.CycApiException;
import com.cyc.session.CycServer;
import com.cyc.base.kbtool.InspectorTool;
import com.cyc.baseclient.kbtool.CycAssertTool;
import com.cyc.baseclient.kbtool.CycComparisonTool;
import com.cyc.baseclient.kbtool.CycInspectorTool;
import com.cyc.baseclient.kbtool.CycLookupTool;
import com.cyc.baseclient.kbtool.CycOwlTool;
import com.cyc.baseclient.kbtool.CycInferenceTool;
import com.cyc.baseclient.kbtool.CycRKFTool;
import com.cyc.baseclient.kbtool.CycUnassertTool;
import com.cyc.baseclient.subl.SubLResourceLoader;
import com.cyc.baseclient.subl.SubLSourceFile;
import com.cyc.baseclient.subl.functions.SubLFunctions;
import com.cyc.session.CycServerAddress;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides wrappers for the Base Client.
 *
 * <p>
 Collaborates with the <tt>CycConnection</tt> class which manages the api connections.
 </p>
 *
 * @version $Id: CycClient.java 159949 2015-07-27 19:12:45Z nwinant $
 * @author Stephen L. reed <p><p><p><p><p>
 */
public class CycClient implements CycAccess {
  
  // Static methods
  
  public static byte[] getCycInitializationRequest(UUID uuid) {
    CycArrayList request = new CycArrayList();
    request.add(new CycSymbolImpl("INITIALIZE-JAVA-API-PASSIVE-SOCKET"));
    request.add(uuid.toString());
    ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
    CfaslOutputStream cfo = new CfaslOutputStream(baos);
    try {
      cfo.writeObject(request);
      cfo.flush();
    } catch (IOException ioe) {} // ignore, should never happen
    return baos.toByteArray();
  }
  
  public static void handleCycInitializationRequestResponse(InputStream is) throws IOException {
    CfaslInputStream inboundStream = new CfaslInputStream(is);
    // read and ignore the status
    inboundStream.resetIsInvalidObject();
    Object status = inboundStream.readObject();
    // read and ignore the response
    inboundStream.resetIsInvalidObject();
    Object response = inboundStream.readObject();
  }
  
  @Deprecated
  protected static KBTransaction getCurrentTransaction() {
    return currentTransaction.get();
  }

  @Deprecated
  protected static void setCurrentTransaction(KBTransaction transaction) {
    currentTransaction.set(transaction);
  }
  
  private static boolean isPossibleExternalIDString(final Object obj) {
    return is64BitString(obj);
  }
  
  private static boolean is64BitString(final Object obj) {
    if (obj instanceof String) {
      return is64Bit((String) obj);
    } else {
      return false;
    }
  }

  private static boolean is64Bit(final String string) {
    final StringCharacterIterator i = new StringCharacterIterator(string);
    for (char c = i.first(); c != CharacterIterator.DONE; c = i.next()) {
      if (!is64Bit(c)) {
        return false;
      }
    }
    return true;
  }

  private static boolean is64Bit(final char c) {
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || //0-51
            (c >= '0' && c <= '9') || //52-61
            (c == '+') || (c == '/') || //62-63 original Base64 standard
            (c == '-') || (c == '_'); //62-63 modified Base64 for URL
  }
  
  
  // Static fields
  
  /** 
   * Value indicating that the Base Client should use one TCP socket for the entire session.
   */
  public static final int PERSISTENT_CONNECTION = 2;
  
  /** 
   * Value indicating that the Base Client should use one TCP socket for the entire session. 
   */
  private static final int XML_SOAP_CONNECTION = 3;

  /** 
   * Default value for isLegacyMode is no compatibility with older versions of the Base Client.
   */
  private static final boolean DEFAULT_IS_LEGACY_MODE = false;
  
  /** 
   * The maximum time that the CycClient connection is allowed to be unused before establishing 
 a fresh connection (ten hours)
   */
  private static final long MAX_UNACCESSED_MILLIS = 36000000;
  
  /** 
   * The Cyc server OK response code
   */
  private static final int OK_RESPONSE_CODE = 200;
    
  @Deprecated
  private static ThreadLocal<KBTransaction> currentTransaction = new ThreadLocal<KBTransaction>() {
    @Override
    protected KBTransaction initialValue() {
      return null;
    }
  };
  
  @Deprecated
  private static ThreadLocal<CycClientOptions> currentOptions = new ThreadLocal<CycClientOptions>() {
    @Override
    protected CycClientOptions initialValue() {
      return null;
    }
  };
    
  /**
   * Dictionary of CycClient instances, indexed by thread so that the application does not have to
 keep passing around a CycClient object reference.
   */
  @Deprecated
  private static final Map<Thread, CycClient> cycAccessInstances = new HashMap<Thread, CycClient>();
  
  /**
   * Shared CycClient instance when thread synchronization is entirely handled by the application.
   * Use of the CycClient.current() method returns this reference if the lookup by process thread
 fails.
   */
  //public static CycClient sharedCycAccessInstance = null;
  
  private static final String UTF8 = "UTF-8";

  private static final String CYC_IMAGE_ID_EXPRESSION = makeSubLStmt("cyc-image-id");
    
  private static final Logger LOGGER = LoggerFactory.getLogger(CycClient.class);
  
  
  // Instance fields
  
  final private CycClientSession session;
    
  /** 
   * The indicator that this CycClient object is using a SOAP connection to cunicate with Cyc.
   */
  final private boolean isSOAPConnection = false;
  
  /** 
   * The indicator that API request forms should be logged to a file api-requests.lisp in the 
 working directory.
   */
  final private boolean areAPIRequestsLoggedToFile = false;
  
  // TODO: should this be replaced with SLF4J?
  private FileWriter apiRequestLog = null;
  
  /**
   * Parameter indicating whether the Base Client should use one TCP socket for the entire session,
 or if the socket is created and then closed for each api call, or if an XML SOAP service 
 provides the message transport.
   */
  private int persistentConnection = PERSISTENT_CONNECTION;
  
  /** 
   * the Cyc server host name 
   */
  private String hostName;
  
  /** 
   * The Cyc server host TCP port number.
   */
  private int port;
  
  private Comm comm;
  
  /** 
   * The parameter that, when true, causes a trace of the messages to and from the server .
   */
  private int trace = CycConnection.API_TRACE_NONE;
  //protected int trace = CycConnection.API_TRACE_MESSAGES;
  //protected int trace = CycConnection.API_TRACE_DETAILED;

  /**
   * Reference to <tt>CycConnection</tt> object which manages the api connection to the OpenCyc
 server.
   */
  private CycConnectionInterface cycConnection;

  /** 
   * The timestamp for the previous access to Cyc, used to re-establish too-long unused connections.
   */
  private long previousAccessedMilliseconds = System.currentTimeMillis();
  
  /**
   * The Cyc image ID used for detecting new Cyc images that cause the constants cache to be reset.
   */
  private String cycImageID;
  
  /* * the Cyc lease manager that acquires Cyc api service leases */
  //Tag: Fix CycLeaseManager
  //private CycLeaseManager cycLeaseManager;
  
  /** 
   * Indicates whether the connection is closed.
   */
  private volatile boolean isClosed = false;
  
  private boolean reestablishClosedConnections = true;
  private Boolean isOpenCyc = null;
  private CycCommandTool converseTool;
  private CycAssertTool assertTool;
  private CycComparisonTool comparisonTool;
  private CycInferenceTool inferenceTool;
  private CycInspectorTool inspectorTool;
  private CycLookupTool lookupTool;
  private CycObjectTool objectTool;
  private CycUnassertTool unassertTool;
  private CycOwlTool owlTool;
  private CycRKFTool rkfTool;
  private CycServerInfoImpl serverInfo;
  
  
  // Constructors
  
  /**
   * Constructs a new CycAccess object from a CycConnectionInterface.
   *
   * @param conn the Cyc connection object (in persistent, binary mode)
   * @param session
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public CycClient(CycConnectionInterface conn, CycClientSession session)
          throws CycConnectionException, CycApiException {
    this.hostName = conn.getHostName();
    this.port = conn.getBasePort();
    this.session = session;
    this.cycConnection = conn;
    commonInitialization();
  }
  
  /**
   * Constructs a new CycAccess object given a CycSession.
   *
   * @param session The CycSession to which the CycAccess should be tied.
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public CycClient(CycClientSession session)
          throws CycConnectionException, CycApiException {
    final CycServerAddress server = session.getConfiguration().getCycServer();
    this.hostName = server.getHostName();
    this.port = server.getBasePort();
    this.session = session;
    this.cycConnection = new CycConnection(server, this);
    commonInitialization();
  }
  
  /**
   * Constructs a new CycAccess object given a CycServer address and no CycSession.
   * 
   * @param server The address of the server to connect to.
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public CycClient(CycServer server) throws CycConnectionException, CycApiException {
    this.hostName = server.getHostName();
    this.port = server.getBasePort();
    this.session = null;
    this.cycConnection = new CycConnection(server, this);
    commonInitialization();
  }
  
  /**
   * Constructs a new CycAccess object given a Comm implementation and a CycSession.
   * @param comm
   * @param session
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  public CycClient(Comm comm, CycClientSession session)
          throws CycConnectionException, CycApiException {
    System.out.println("Cyc Access with Comm object: " + comm.toString());
    System.out.flush();
    this.session = session;
    this.comm = comm;
    this.cycConnection = new CycConnection(comm, this);
    this.comm.setCycConnection(cycConnection);
    //if (comm instanceof SocketComm) {
      commonInitialization();
    //}
  }
  
  public CycClient(CycConnectionInterface conn)
          throws CycConnectionException, CycApiException {
    this(conn, null);
  }
  
  public CycClient(Comm c)
          throws CycConnectionException, CycApiException {
    this(c, null);
  }
  
  
  // Public
  
  @Override
  public CycClientSession getCycSession() {
    return this.session;
  }
  
  @Override
  public CycClientOptions getOptions() {
    if (this.getCycSession() != null) {
      return this.getCycSession().getOptions();
    }
    
    // If no CycSession is available, fall back on a threadlocal variable.
    if (currentOptions.get() == null) {
      currentOptions.set(new CycClientOptions(this));
    }
    return currentOptions.get();
  }

  /**
   * Returns a string representation of this object.
   *
   * @return a string representation of this object
   */
  @Override
  public String toString() {
    if (cycConnection == null) {
      return "CycAccess: no valid connection";
    }
    return cycConnection.connectionInfo();
  }

  /**
   * Returns the Cyc api services lease manager.
   *
   * @return the Cyc api services lease manager
   */
  @Deprecated
  @Override
  public LeaseManager getCycLeaseManager() {
    if (this.comm == null) {
      return this.getCycConnection().getCycLeaseManagerMap().get(this.hostName + Integer.toString(this.port + CycConnection.CFASL_PORT_OFFSET));
    } else {
      throw new CycApiException("Please access CycLeaseManager via CycConnection when Comm object is specified");
    }
  }
  
  /**
   * Turns on the diagnostic trace of socket messages.
   */
  @Override
  public void traceOn() {
    cycConnection.traceOn();
    trace = CycConnection.API_TRACE_MESSAGES;
  }

  /**
   * Turns on the detailed diagnostic trace of socket messages.
   */
  @Override
  public void traceOnDetailed() {
    if (cycConnection != null) {
      cycConnection.traceOnDetailed();
    }

    trace = CycConnection.API_TRACE_DETAILED;
  }

  /**
   * Turns off the diagnostic trace of socket messages.
   */
  @Override
  public void traceOff() {
    cycConnection.traceOff();
    trace = CycConnection.API_TRACE_NONE;
  }

  /**
   * Gets the CycServer description of the server to which this CycAccess is connected.
   * @return a CycServer object
   */
  @Override
  public CycServer getCycServer() {
    return new CycServer(getHostName(), getBasePort());
  }
  
  /**
   * Does this CycClient access a single Cyc?  
   * @return true if this CycClient will always access the same Cyc server, and returns false if the
 CycClient was constructed with  a {@link com.cyc.baseclient.comm.Comm} object that may result 
 in different calls being sent to different Cyc servers.
   */
  @Override
  public boolean hasStaticCycServer() {
      return (cycConnection.connectedToStaticCyc());
  }
  
  /**
   * gets the hostname of the connection
   * 
   * @deprecated, use getCycServer().getHostName()
   * @return the hostname of the connection
   */
  @Deprecated
  @Override
  public String getHostName() {
    return cycConnection.getHostName();
  }

  /**
   * gets the baseport of the connection
   * 
   * @deprecated, use getCycServer().getBasePort()
   * @return the baseport of the connection
   */
  @Deprecated
  @Override
  public int getBasePort() {
    return cycConnection.getBasePort();
  }

  /**
   * @deprecated, use getCycServer().getHttpPort()
   * @return the http of server the connection is connected to.
   */
  @Deprecated
  @Override
  public int getHttpPort() {
    return cycConnection.getHttpPort();
  }

  /**
   * Returns the CycConnection object.
   *
   * @return the CycConnection object
   */
  @Override
  public synchronized CycConnectionInterface getCycConnection() {
    try {
      maybeReEstablishCycConnection();
    } catch (CycConnectionException ex) {
      Log.current.println(
              "Couldn't re-establish Cyc connection.\n" + ex.getLocalizedMessage());
    }
    return cycConnection;
  }

  /** Returns whether the connection is closed
   *
   * @return whether the connection is closed
   */
  @Override
  public boolean isClosed() {
    return isClosed;
  }

  /**
   * Closes the CycConnection object. Modified by APB to be able to handle multiple calls to
 close() safely.
   */
  @Override
  public synchronized void close() {
    if (isClosed) {
      LOGGER.debug("Attempting to close {} {}, but is already closed.", this.getClass().getSimpleName(), this);
      return;
    }
    LOGGER.debug("Attempting to close {} {}", this.getClass().getSimpleName(), this);

    isClosed = true;
  //Tag: Fix CycLeaseManager
    /*
    if (cycLeaseManager != null) {
      cycLeaseManager.interrupt();
    }
    */

    if (cycConnection != null) {
      cycConnection.close();
    }
    if (areAPIRequestsLoggedToFile) {
      try {
        apiRequestLog.close();
      } catch (IOException e) {
        LOGGER.error("Error when closing apiRequestLog: {}", e);
      }
    }
    cycAccessInstances.remove(Thread.currentThread());
    // make sure it's not ever used again for setting as the 'current' CycClient.
    /*
    for (Map.Entry<String, CycClient> entry : currentCycAccesses.entrySet()) {
      if (entry.getValue().equals(this)) {
        currentCycAccesses.remove(entry.getKey());
      }
    }
    */
    /* if (sharedCycAccessInstance == null || sharedCycAccessInstance.equals(this)) {
     * final Iterator iter = cycAccessInstances.values().iterator();
     * if (iter.hasNext())
     * sharedCycAccessInstance = (CycClient) iter.next();
     * else
     * sharedCycAccessInstance = null;
     * } */
    LOGGER.debug("Closed {} {}", this.getClass().getSimpleName(), this);
  }
  
  /** 
   * Try to enhance <code>urlString</code> to log <code>cyclist</code> in and redirect 
   * to the page  it would otherwise go to directly.
   * 
   * @param urlString
   * @param cyclist
   * @param applicationTerm
   * @return 
   */
  @Override
  public String maybeAddLoginRedirect(final String urlString,
          final Fort cyclist,
          final DenotationalTerm applicationTerm) {
    // sample urlString: cg?CB-CF&236134
    final int questionMarkPos = urlString.indexOf('?');
    if ((cyclist instanceof CycConstantImpl) && (questionMarkPos >= 0)) {
      final String cgiProgram = urlString.substring(0, questionMarkPos);
      final String originalQueryString = urlString.substring(questionMarkPos + 1);
      final StringBuilder stringBuilder = new StringBuilder(cgiProgram);
      stringBuilder.append("?cb-login-handler");
      stringBuilder.append("&new_login_name=").append(
              ((CycConstantImpl) cyclist).getName());
      maybeAddPassword(cyclist, applicationTerm, stringBuilder);
      stringBuilder.append("&redirect-handler=").append(originalQueryString);
      return stringBuilder.toString();
    } else {
      return urlString;
    }
  }

  /**
   * Sets the print-readable-narts feature on.
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void setReadableNarts()
          throws CycConnectionException, CycApiException {
    converse().converseVoid("(csetq *print-readable-narts t)");
  }

  /**
   * Adds #$ to string for all CycConstants mentioned in the string that don't already have them.
   *
   * @param str the String that will have #$'s added to it.
   *
   * @return a copy of str with #$'s added where appropriate.
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public String cyclifyString(String str)
          throws CycConnectionException, CycApiException {
    final String command =
            makeSubLStmt("cyclify-string", str);
    final String answer = converse().converseString(command);
    return answer;
  }
  
  /**
   * Imports a MUC (Message Understanding Conference) formatted symbolic expression into cyc via
 the function which parses the expression and creates assertions for the contained concepts
 and relations between them.
   *
   * @param mucExpression the MUC (Message Understanding Conference) formatted symbolic expression
   * @param mtName the name of the microtheory in which the imported assertions will be made
   *
   * @return the number of assertions imported from the input MUC expression
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Deprecated
  public int importMucExpression(CycList mucExpression,
          String mtName)
          throws CycConnectionException, CycApiException {
    String command = makeSubLStmt("convert-netowl-sexpr-to-cycl-assertions",
            mucExpression, mtName);
    return converse().converseInt(command);
  }

  /**
   * Returns true if this KB is an OpenCyc image.
   *
   * @return true if this KB is an OpenCyc image, otherwise false
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   */
  @Override
  public synchronized boolean isOpenCyc() throws CycConnectionException {
    if (isOpenCyc == null) {
      try {
        isOpenCyc = converse().converseBoolean("(cyc-opencyc-feature)");
      } catch (CycApiException e) {
        isOpenCyc = false;
      }
    }

    return isOpenCyc;
  }
  
  /**
   * Returns true if this Cyc server has a Full KB.
   *
   * @return true if this Cyc has a Full KB, otherwise false
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   */
  public synchronized boolean isFullKB() throws CycConnectionException {
    final InspectorTool inspector = getInspectorTool();
    // TODO: this test could be a lot more rigorous - nwinant, 2014-08-21
    return inspector.isConstantInKB(new CycConstantImpl("#$TKBConstant", new GuidImpl("ca09c15c-2ef2-41d7-87c1-bed958a19357")));
  }
  
  /**
   * Returns the Cyc image ID.
   *
   * @return the Cyc image ID string
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public String getCycImageID()
          throws CycConnectionException, CycApiException {
    return converse().converseString(CYC_IMAGE_ID_EXPRESSION);
  }

  /**
   * Returns whether or not we have a valid lease with the Cyc server.
   *
   * @return whether or not we have a valid lease with the Cyc server
   */
  //Tag: Fix CycLeaseManager
  @Override
  public boolean hasValidLease() {
    boolean valid = false;
    for (Entry<String, LeaseManager> kv : cycConnection.getCycLeaseManagerMap().entrySet()){
      if (kv.getValue().hasValidLease()){
        valid = true;
      }
    }
    if (comm != null) {
      for (Entry<InputStream, LeaseManager> kv : cycConnection.getCycLeaseManagerCommMap().entrySet()) {
        if (kv.getValue().hasValidLease()) {
          valid = true;
        }
      }  
    }
    return valid;
  }
  
  /**
   * Should the connection to the Cyc server be re-established if it for some reason becomes unreachable?
 If true, a reconnect will be attempted, and a connection will be established with whatever Cyc
 server is reachable to the original address, even if it is a different server process."
   */
  @Override
  public boolean getReestablishClosedConnections() {
    return reestablishClosedConnections;
  }

  /*
   * If true, the CycClient will try to re-establish a connection with a Cyc server at the
   * provided address. Note that this may end up connecting to a different Cyc server than
   * it was originally connected to. If it is important for your application that the CycClient only
   * ever connect to a single server, and not connect to a different server if the original one
   * becomes inaccessible, be sure to set this to false.
   * Default value is true.
   */
  @Override
  public void setReestablishClosedConnections(boolean value) {
    reestablishClosedConnections = value;
  }
  
  /**
   * Send command to a Cyc server in the SubL language, and receive the results expressed as Java objects.
   * 
   * @return CycCommandTool
   */
  @Override
  public CycCommandTool converse() {
    if (converseTool == null) {
      converseTool = new CycCommandTool(this);
    }
    return converseTool;
  }
  
  /**
   * Provides tools for asserting facts to the Cyc KB.
   * 
   * @return CycAssertTool
   */
  @Override
  public CycAssertTool getAssertTool() {
    if (assertTool == null) {
      assertTool = new CycAssertTool(this);
    }
    return assertTool;
  }

  /**
   * Provides tools for comparing different CycObjects.
   * 
   * @return CycComparisonTool
   */
  @Override
  public CycComparisonTool getComparisonTool() {
    if (comparisonTool == null) {
      comparisonTool = new CycComparisonTool(this);
    }
    return comparisonTool;
  }
  
  /**
   * Provides tools for performing inferences over the Cyc KB.
   * 
   * @return CycInferenceTool
   */
  @Override
  public CycInferenceTool getInferenceTool() {
    if (inferenceTool == null) {
      inferenceTool = new CycInferenceTool(this);
    }
    return inferenceTool;
  }

  /**
   * Provides tools for examining individual CycObjects.
   * 
   * @return CycInspectorTool
   */
  @Override
  public CycInspectorTool getInspectorTool() {
    if (inspectorTool == null) {
      inspectorTool = new CycInspectorTool(this);
    }
    return inspectorTool;
  }

  /**
   * Provides tools for looking up CycObjects in the Cyc KB.
   * 
   * @return CycLookupTool
   */
  @Override
  public CycLookupTool getLookupTool() {
    if (lookupTool == null) {
     lookupTool  = new CycLookupTool(this);
    }
    return lookupTool;
  }

  /**
   * Provides tools for creating simple CycObjects, such as constants and lists.
   * 
   * @return CycObjectTool
   */
  @Override
  public CycObjectTool getObjectTool() {
    if (objectTool == null) {
      objectTool = new CycObjectTool(this);
    }
    return objectTool;
  }

  /**
   * Provides tools for unasserting facts to the Cyc KB.
   * 
   * @return CycUnassertTool
   */
  @Override
  public CycUnassertTool getUnassertTool() {
    if (unassertTool == null) {
      unassertTool = new CycUnassertTool(this);
    }
    return unassertTool;
  }
  
  /**
   * Tools for importing OWL ontologies into the Cyc KB
   * 
   * @deprecated Will either by moved to the KnowledgeManagement API, or deleted.
   * @return set a new CycOwlTool if null and return
   */
  public CycOwlTool getOwlTool() {
    if (owlTool == null) {
      owlTool = new CycOwlTool(this);
    }
    return owlTool;
  }

  /**
   * Tools from the RKF project.
   * 
   * @deprecated Will either by moved to the KnowledgeManagement API, or deleted.
   * @return set a new CycRKFTool if null and return
   */
  public CycRKFTool getRKFTool() {
    if (rkfTool == null) {
      rkfTool = new CycRKFTool(this);
    }
    return rkfTool;
  }
  
  /* 
  @Override
  public ServerProfile getServerProfile() {
    if (cycProfile == null) {
      cycProfile = new CycServerProfile(this);
    }
    return cycProfile;
  }
  */
  
  /**
   * Provides basic information about the state and location of the current Cyc server.
   * 
   * @return CycServerInfo
   */
  @Override
  public CycServerInfoImpl getServerInfo() {
    if (serverInfo == null) {
      serverInfo = new CycServerInfoImpl(this);
    }
    return serverInfo;
  }
  
  
  // Protected
  
  /** 
   * Re-establishes a stale binary CycConnection. 
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  private synchronized void reEstablishCycConnection() 
          throws CycConnectionException, CycApiException {
    Log.current.println(
            "Trying to re-establish a closed Cyc connection to " + this);
    
    //new Throwable().printStackTrace(System.err); // Uncomment this to get a stack trace.
    
    previousAccessedMilliseconds = System.currentTimeMillis();
    cycConnection.close();
    
    if (this.comm == null) {
      Log.current.println ("Connect using host: " + hostName + " and port: " + port);
      cycConnection = new CycConnection(hostName, port, this);
    } else {
      Log.current.println ("Connect using comm object: " + comm.toString());
      cycConnection = new CycConnection(comm, this);
      comm.setCycConnection(cycConnection);
      
      // TODO: Vijay: There is a timing issue here.
      // If we do not sleep here, the next call to Cyc, which is getCycImageID()
      // enters a recursive loop of reEstablishCycConnection() and throws a 
      // stack-overflow exception.
      /*
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // Even having this makes the code throw stack-overflow
        //Thread.currentThread().interrupt();
      }
      */ 
    }

    if (!(cycImageID.equals(getCycImageID()))) {
      Log.current.println("New Cyc image detected, resetting caches.");
      CycObjectFactory.resetCaches();
    }
  }
  
  protected void verifyPossibleDenotationalTerm(CycObject cycObject) throws IllegalArgumentException {
    if (!(cycObject instanceof DenotationalTerm || cycObject instanceof CycArrayList)) {
      throw new IllegalArgumentException(
              "cycObject must be a Cyc denotational term " + cycObject.cyclify());
    }
  }
  
  /** 
   * Converses with Cyc to perform an API command. Creates a new connection for this command if the
   * connection is not persistent.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result as an object array of two objects
   * @see CycConnectionInterface#converse(Object)
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  protected Object[] converse(Object command)
          throws CycConnectionException, CycApiException {
    //Object[] response = {null, null};
    Object[] response;
    try {
      maybeLogCommand(command);
    } catch (IOException ioe) {
      throw new CycConnectionException(ioe);
    }
    response = converseWithRetrying(command);
    previousAccessedMilliseconds = System.currentTimeMillis();
    maybeLogResponse(response);
    return response;
  }

  /**
   * Send a command to Cyc, and maybe try to recover from a closed connection.
   *
   * @param command - String, CycArrayList, or Worker
   * @return the results of evaluating the command
   * @throws CycApiException if the Cyc server returns an error
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @see #getReestablishClosedConnections()
   * @see CycConnection#converse(java.lang.Object)
   * @see CycConnection#converseBinary(com.cyc.base.conn.Worker)
   */
  protected Object[] converseWithRetrying(Object command) throws CycApiException, CycConnectionException {
    Object[] response = {null, null};
    try {
      response = doConverse(getCycConnection(), command);
    } catch (CycApiClosedConnectionException ex) {
      if (getReestablishClosedConnections()) {
        reEstablishCycConnection();
        response = doConverse(cycConnection, command);
      } else {
        throw (ex);
      }
    }
    return response;
  }
  
  
  // Private
  
  /**
   * Provides common local and remote CycClient object initialization.
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  private void commonInitialization()
          throws CycConnectionException, CycApiException {
    LOGGER.debug("* * * Initializing * * *");
    this.persistentConnection = PERSISTENT_CONNECTION;
    if (Log.current == null) {
      Log.makeLog("cyc-api.log");
    }
    
    final String apiRequestLogFile = "api-requests.lisp";
    if (areAPIRequestsLoggedToFile) {
      try {
        apiRequestLog = new FileWriter(apiRequestLogFile);
      } catch (IOException ioe) {
        throw new CycConnectionException("Could not write file " + apiRequestLogFile, ioe);
      }
    }
    cycAccessInstances.put(Thread.currentThread(), this);

    /* if (sharedCycAccessInstance == null) {
     * sharedCycAccessInstance = this;
     * } 
     */
    cycImageID = getCycImageID();
    
    loadSublPatches();
    
    try {
      DefaultInferenceParameterDescriptions.loadInferenceParameterDescriptions(
              this, 0);
    } catch (Exception e) {
      LOGGER.warn("Could not load inference parameter descriptions.", e);
      Throwable curr = e;
      while (curr != null) {
        LOGGER.warn(curr.getLocalizedMessage());
        curr = curr.getCause();
      }
    }
  }
  
  /**
   * Returns a with-bookkeeping-info macro expression.
   *
   * @return a with-bookkeeping-info macro expression
   */
  private String withBookkeepingInfo() {
    String projectName = "nil";
    final Fort project = getOptions().getKePurpose();
    final Fort cyclist = getOptions().getCyclist();
    
    if (project != null) {
      projectName = project.stringApiValue();
    }

    String cyclistName = "nil";

    if (cyclist != null) {
      cyclistName = cyclist.stringApiValue();
    }

    return "(with-bookkeeping-info (new-bookkeeping-info " + cyclistName + " (the-date) "
            + projectName + "(the-second)) ";
  }
  
  private String doubleURLEncode(final String password) throws UnsupportedEncodingException {
    return urlEncode(urlEncode(password));
  }

  private String urlEncode(final String password) throws UnsupportedEncodingException {
    return URLEncoder.encode(password, UTF8);
  }

  /** Add a piece to the URL being string-built to specify cyclist's (encrypted) password */
  private void maybeAddPassword(final Fort cyclist,
          final DenotationalTerm applicationTerm,
          final StringBuilder stringBuilder) {
    if (cyclist instanceof CycConstantImpl) {
      final PasswordManager passwordManager = new PasswordManager(this);
      try {
        if (passwordManager.isPasswordRequired()) {
          final String password = passwordManager.lookupPassword(
                  (CycConstantImpl) cyclist, applicationTerm);
          if (password != null) {
            // @hack -- Cyc decodes '+' characters twice, so we encode twice:
            final String urlEncodedPassword = doubleURLEncode(password);
            stringBuilder.append("&new_login_hashed_password=").append(
                    urlEncodedPassword);
          }
        }
      } catch (IOException ex) {
        // Ignore: User may have to supply password to browser.
      } catch (CycConnectionException ex) {
        // Ignore: User may have to supply password to browser.
      }
    }
  }
  
  /**
   * Apply any missing SubL patches to the Cyc server.
   * 
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  private void loadSublPatches() throws BaseClientRuntimeException {
    final String forMoreInfo = "For more information, see http://dev.cyc.com/api/server-patching/";
    final SubLResourceLoader loader = new SubLResourceLoader(this);
    try {
      if (!getServerInfo().isAPICompatible()) {
        String msg = "This server is not compatible with this release of the Core API Suite and cannot be patched; skipping. " + forMoreInfo;
        LOGGER.error(msg);
        throw new BaseClientRuntimeException(msg);
      } else if (!getCycSession().getConfiguration().isServerPatchingAllowed()) {
        LOGGER.warn("Loading SubL patches is not allowed (CycSessionConfiguration#isServerPatchingAllowed() == false); skipping. " + forMoreInfo);
        final List<SubLSourceFile> missing = loader.findMissingRequiredResources(SubLFunctions.SOURCES);
        if (!missing.isEmpty()) {
          String numResources = (missing.size() >= 2) ? missing.size() + " required resources" : "required resource";
          String msg = "Cyc server " + getServerInfo().getCycServer() + " is missing " + numResources + ": " + missing.toString() + ". " + forMoreInfo;
          LOGGER.error(msg);
          throw new BaseClientRuntimeException(msg);
        }
      } else {
        // TODO: we may also want Cyc to be able to disallow server patching.
        
        loader.loadMissingResources(SubLFunctions.SOURCES);
      }
    } catch (BaseClientRuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      String msg = "Could not load SubL patches. " + forMoreInfo;
      LOGGER.warn(msg, ex);
      throw new BaseClientRuntimeException(msg, ex);
    }
  }
  
  private void maybeReEstablishCycConnection() throws CycConnectionException, CycApiException {
    if (!isSOAPConnection) {
//      if ((previousAccessedMilliseconds + MAX_UNACCESSED_MILLIS) < System.currentTimeMillis()) {
//        Log.current.println("Re-establishing a stale Cyc connection.");
//        reEstablishCycConnection();
//      }
//      else
      if (!((CycConnection) cycConnection).isValidBinaryConnection()) {
        Log.current.println(
                "Re-establishing an invalid Cyc connection  to " + this);
        reEstablishCycConnection();
      }
    }
  }

  /**
   * 
   * @param command
   * @throws CycApiException if the Cyc server returns an error
   * @throws IOException if an I/O error occurs
   */
  private void maybeLogCommand(Object command) throws CycApiException, IOException {
    if (trace > CycConnection.API_TRACE_NONE || areAPIRequestsLoggedToFile) {
      final CycList commandCycList = (command instanceof CycList) ? (CycList) command : getObjectTool().makeCycList(
              (String) command);
      final String prettyCommandCycList = commandCycList.toPrettyCyclifiedString(
              "");
      final String escapedCommandCycList = commandCycList.toPrettyEscapedCyclifiedString(
              "");
      if (areAPIRequestsLoggedToFile) {
        apiRequestLog.write(escapedCommandCycList);
        apiRequestLog.write('\n');
      }
      if (trace > CycConnection.API_TRACE_NONE) {
        Log.current.println(prettyCommandCycList + "\n--> cyc");
      }
    }
  }

  private void maybeLogResponse(Object[] response) {
    if (trace > CycConnection.API_TRACE_NONE) {
      String responseString;

      if (response[1] instanceof CycArrayList) {
        responseString = ((CycArrayList) response[1]).toPrettyString("");
      } else if (response[1] instanceof Fort) {
        responseString = ((Fort) response[1]).cyclify();
      } else {
        responseString = response[1].toString();
      }
      Log.current.println("cyc --> " + responseString);
    }
  }

  private Object[] doConverse(final CycConnectionInterface cycConnection,
          final Object command) throws CycConnectionException {
    if (command instanceof Worker) {
      //SubL workers talk directly to Cyc:
      cycConnection.converseBinary((Worker) command);
      return null;
    } else {
      //String or CycArrayList commands go through a few intermediaries:
      return cycConnection.converse(command);
    }
  }
}
