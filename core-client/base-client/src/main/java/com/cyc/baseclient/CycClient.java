package com.cyc.baseclient;

/*
 * #%L
 * File: CycClient.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.baseclient.kbtool.CycObjectTool;
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.conn.Worker;
import com.cyc.base.conn.CycConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.cyc.baseclient.exception.CycApiClosedConnectionException;
import com.cyc.baseclient.connection.CycConnectionImpl;
import static com.cyc.baseclient.connection.SublApiHelper.makeSubLStmt;
import com.cyc.baseclient.comm.Comm;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.GuidImpl;
import com.cyc.baseclient.inference.params.DefaultInferenceParameterDescriptions;
//import com.cyc.baseclient.util.Log;
import com.cyc.baseclient.util.PasswordManager;
import com.cyc.base.conn.LeaseManager;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.exception.CycApiException;
import com.cyc.session.CycServer;
import com.cyc.base.kbtool.InspectorTool;
import com.cyc.baseclient.kbtool.CycAssertTool;
import com.cyc.baseclient.kbtool.CycComparisonTool;
import com.cyc.baseclient.kbtool.CycKbObjectTool;
import com.cyc.baseclient.kbtool.CycInspectorTool;
import com.cyc.baseclient.kbtool.CycLookupTool;
import com.cyc.baseclient.kbtool.CycOwlTool;
import com.cyc.baseclient.kbtool.CycInferenceTool;
import com.cyc.baseclient.kbtool.CycRkfTool;
import com.cyc.baseclient.kbtool.CycUnassertTool;
import com.cyc.baseclient.nl.ParaphraserFactory;
import com.cyc.baseclient.subl.SublResourceLoader;
import com.cyc.baseclient.subl.SublSourceFile;
import com.cyc.baseclient.subl.functions.SublFunctions;
import com.cyc.nl.Paraphraser;
import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionRuntimeException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides wrappers for the Base Client.
 *
 * <p>
 Collaborates with the <tt>CycConnection</tt> class which manages the API connections.
 </p>
 *
 * @version $Id: CycClient.java 163752 2016-01-26 01:34:19Z nwinant $
 * @author Stephen L. reed <p><p><p><p><p>
 */
public class CycClient implements CycAccess {
  
  // Static methods
  
  @Deprecated
  protected static KbTransaction getCurrentTransaction() {
    return currentTransaction.get();
  }

  @Deprecated
  protected static void setCurrentTransaction(KbTransaction transaction) {
    currentTransaction.set(transaction);
  }
  
  
  // Static fields
  
  /** 
   * Value indicating that the Base Client should use one TCP socket for the entire session.
   */
  public static final int PERSISTENT_CONNECTION = 2;
  
  @Deprecated
  private static ThreadLocal<KbTransaction> currentTransaction = new ThreadLocal<KbTransaction>() {
    @Override
    protected KbTransaction initialValue() {
      return null;
    }
  };
  
  /**
   * Dictionary of CycClient instances, indexed by thread so that the application does not have to
   * keep passing around a CycClient object reference.
   */
  @Deprecated
  private static final Map<Thread, CycClient> cycAccessInstances = new HashMap<Thread, CycClient>();
  
  private static final String UTF8 = "UTF-8";

  private static final String CYC_IMAGE_ID_EXPRESSION = makeSubLStmt("cyc-image-id");
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CycClient.class);
  
  
  // Instance fields
    
  /* * 
   * The indicator that API request forms should be logged to a file api-requests.lisp in the 
   * working directory.
   * /
  final private boolean areAPIRequestsLoggedToFile = false;
  
  // TODO: should this be replaced with SLF4J?
  private FileWriter apiRequestLog = null;
  */
  
  /* *
   * Parameter indicating whether the Base Client should use one TCP socket for the entire session,
   * or if the socket is created and then closed for each api call, or if an XML SOAP service 
   * provides the message transport.
   * /
  private int persistentConnection = PERSISTENT_CONNECTION;
  */
  
  /** 
   * the Cyc server host name 
   */
  final private String hostName;
  
  /** 
   * The Cyc server host TCP port number.
   */
  final private Integer port;
  
  private Comm comm;
  
  /** 
   * The parameter that, when true, causes a trace of the messages to and from the server .
   */
  private int trace = CycConnectionImpl.API_TRACE_NONE;
  //protected int trace = CycConnection.API_TRACE_MESSAGES;
  //protected int trace = CycConnection.API_TRACE_DETAILED;

  /**
   * Reference to <tt>CycConnection</tt> object which manages the api connection to the OpenCyc
   * server.
   */
  private CycConnection cycConnection;

  /** 
   * The timestamp for the previous access to Cyc, used to re-establish too-long unused connections.
   */
  private long previousAccessedMilliseconds = System.currentTimeMillis();
  
  /**
   * The Cyc image ID used for detecting new Cyc images that cause the constants cache to be reset.
   */
  private String cycImageID;
  
  /** 
   * Indicates whether the connection is closed.
   */
  private volatile boolean isClosed = false;
  
  private boolean hasServerPatchingBeenChecked = false;
  
  private boolean reestablishClosedConnections = true;
  private Boolean isOpenCyc = null;
  private CycCommandTool converseTool;
  private CycAssertTool assertTool;
  private CycComparisonTool comparisonTool;
  private CycKbObjectTool compatibilityTool;
  private CycInferenceTool inferenceTool;
  private CycInspectorTool inspectorTool;
  private CycLookupTool lookupTool;
  private CycObjectTool objectTool;
  private CycUnassertTool unassertTool;
  private CycOwlTool owlTool;
  private CycRkfTool rkfTool;
  private CycServerInfoImpl serverInfo;
  
  
  // Constructors
  
  private CycClient(String hostName, Integer port)
          throws CycConnectionException, CycApiException {
    this.hostName = hostName;
    this.port = port;
  }
  
  /**
   * Constructs a new CycAccess object given a CycServer address and no CycSession.
   * 
   * @param server The address of the server to connect to.
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public CycClient(CycServerAddress server) throws CycConnectionException, CycApiException {
    this(server.getHostName(), server.getBasePort());
    commonInitialization(new CycConnectionImpl(server, this));
  }
  
  /**
   * Constructs a new CycAccess object given a CycSessionConfiguration.
   *
   * @param config The CycSessionConfiguration to which the CycAccess should be tied.
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public CycClient(CycSessionConfiguration config)
          throws CycConnectionException, CycApiException {
    this(config.getCycServer());
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
    this(session.getConfiguration());
  }
  
  /**
   * Constructs a new CycAccess object from a CycConnectionInterface.
   *
   * @param conn the Cyc connection object (in persistent, binary mode)
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public CycClient(CycConnection conn)
          throws CycConnectionException, CycApiException {
    this(conn.getHostName(), conn.getBasePort());
    commonInitialization(conn);
  }
  
  /**
   * Constructs a new CycAccess object given a Comm implementation and a CycSession.
   * @param comm
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  public CycClient(Comm comm)
          throws CycConnectionException, CycApiException {
    this(null, null);
    System.out.println("Cyc Access with Comm object: " + comm.toString());
    System.out.flush();
    this.comm = comm;
    CycConnection conn = new CycConnectionImpl(comm, this);
    this.comm.setCycConnection(conn);
    //if (comm instanceof SocketComm) {
      commonInitialization(conn);
    //}
  }
  
  
  // Public
  
  @Override
  public CycClientSession getCycSession() {
    // TODO: Can this method be made more robust? - nwinant, 2015-10-14
    
    //return this.session;
    final CycServerAddress server = this.getCycServer();
    try {
      final CycClientSession session = CycClientManager.get().getSession(server);
      if (session.getAccess() != this) {
        throw new SessionRuntimeException(
                "Expected to receive a " + CycClientSession.class.getSimpleName()
                        + " tied to this " + CycClient.class.getSimpleName() + " (" + this.toString() + ")"
                        + " but received one tied to " + this.toString());
      }
      return session;
    } catch (SessionException ex) {
      throw new SessionRuntimeException(ex);
    }
  }
  
  @Override
  public CycClientOptions getOptions() {
    return this.getCycSession().getOptions();
  }

  /**
   * Returns a string representation of this object.
   *
   * @return a string representation of this object
   */
  @Override
  public String toString() {
    final String name = this.getClass().getSimpleName() + "@" + this.hashCode();
    if (cycConnection == null) {
      return name + " no valid connection";
    }
    return name + " " + cycConnection.connectionInfo();
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
      return this.getCycConnection().getCycLeaseManagerMap().get(this.hostName + Integer.toString(this.port + CycConnectionImpl.CFASL_PORT_OFFSET));
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
    trace = CycConnectionImpl.API_TRACE_MESSAGES;
  }

  /**
   * Turns on the detailed diagnostic trace of socket messages.
   */
  @Override
  public void traceOnDetailed() {
    if (cycConnection != null) {
      cycConnection.traceOnDetailed();
    }

    trace = CycConnectionImpl.API_TRACE_DETAILED;
  }

  /**
   * Turns off the diagnostic trace of socket messages.
   */
  @Override
  public void traceOff() {
    cycConnection.traceOff();
    trace = CycConnectionImpl.API_TRACE_NONE;
  }

  /**
   * Gets the CycServer description of the server to which this CycAccess is connected.
   * @return a CycServer object
   */
  @Override
  public CycServerAddress getCycServer() {
    // TODO: this could be improved - nwinant, 2015-10-14
    return new CycServer(getHostName(), getBasePort());
  }
  
  /**
   * Does this CycClient access a single Cyc?  
   * 
   * @return true if this CycClient will always access the same Cyc server, and returns false if the
   * CycClient was constructed with  a {@link com.cyc.baseclient.comm.Comm} object that may result 
   * in different calls being sent to different Cyc servers.
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
  public synchronized CycConnection getCycConnection() {
    try {
      maybeReEstablishCycConnection();
    } catch (CycConnectionException ex) {
      LOGGER.warn("Couldn't re-establish Cyc connection.\n{}", ex.getLocalizedMessage());
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
    //TODO: Fix CycLeaseManager
    /*
    if (cycLeaseManager != null) {
      cycLeaseManager.interrupt();
    }
    */

    if (cycConnection != null) {
      cycConnection.close();
    }
    /*
    if (areAPIRequestsLoggedToFile) {
      try {
        apiRequestLog.close();
      } catch (IOException e) {
        LOGGER.error("Error when closing apiRequestLog: {}", e);
      }
    }
    */
    cycAccessInstances.remove(Thread.currentThread());
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
  
  /* *
   * Imports a MUC (Message Understanding Conference) formatted symbolic expression into cyc via
   * the function which parses the expression and creates assertions for the contained concepts
   * and relations between them.
   *
   * @param mucExpression the MUC (Message Understanding Conference) formatted symbolic expression
   * @param mtName the name of the microtheory in which the imported assertions will be made
   *
   * @return the number of assertions imported from the input MUC expression
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * /
  @Deprecated
  public int importMucExpression(CycList mucExpression,
          String mtName)
          throws CycConnectionException, CycApiException {
    String command = makeSubLStmt("convert-netowl-sexpr-to-cycl-assertions",
            mucExpression, mtName);
    return converse().converseInt(command);
  }
  */

  /**
   * Returns true if this KB is an OpenCyc image.
   *
   * @return true if this KB is an OpenCyc image, otherwise false
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data 
   * communication error occurs
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
  @Override
  public boolean hasValidLease() {
    //TODO: Fix CycLeaseManager
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
   * Should the connection to the Cyc server be re-established if it for some reason becomes 
   * unreachable? If true, a reconnect will be attempted, and a connection will be established with
   * whatever Cyc server is reachable to the original address, even if it is a different server 
   * process.
   * 
   * @return Whether to reestablish closed connections
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
   * Provides tools to send commands to a Cyc server in the SubL language, and receive the results 
   * expressed as Java objects.
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
  
  @Override
  public CycKbObjectTool getKbObjectTool() {
    if (compatibilityTool == null) {
      compatibilityTool = new CycKbObjectTool(this);
    }
    return compatibilityTool;
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
  public CycRkfTool getRKFTool() {
    if (rkfTool == null) {
      rkfTool = new CycRkfTool(this);
    }
    return rkfTool;
  }
  
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
   * Converses with Cyc to perform an API command. Creates a new connection for this command if the
   * connection is not persistent.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result as an object array of two objects
   * @see CycConnectionInterface#converse(Object)
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data
   * communication error occurs
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
  
  protected synchronized void initializeSession(CycSessionConfiguration config) {
    loadSublPatches(config);
  }
  
  
  // Private
  
  /** 
   * Re-establishes a stale binary CycConnection. 
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  private synchronized void reEstablishCycConnection() 
          throws CycConnectionException, CycApiException {
    LOGGER.warn("Trying to re-establish a closed Cyc connection: {}", this);
    
    //new Throwable().printStackTrace(System.err); // Uncomment this to get a stack trace.
    
    previousAccessedMilliseconds = System.currentTimeMillis();
    cycConnection.close();
    
    if (this.comm == null) {
      LOGGER.warn("Connect using host: {} and port: {}", hostName, port);
      cycConnection = new CycConnectionImpl(hostName, port, this);
    } else {
      LOGGER.warn("Connect using comm object: {}", comm.toString());
      cycConnection = new CycConnectionImpl(comm, this);
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
      LOGGER.warn("New Cyc image detected, resetting caches.");
      CycObjectFactory.resetCaches();
    }
  }
  
  /**
   * Provides common local and remote CycClient object initialization.
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  private void commonInitialization(CycConnection cycConnection)
          throws CycConnectionException, CycApiException {
    this.cycConnection = cycConnection; 
    LOGGER.debug("* * * Initializing * * *");
    //this.persistentConnection = PERSISTENT_CONNECTION;
    /*
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
    */
    cycAccessInstances.put(Thread.currentThread(), this);

    /* if (sharedCycAccessInstance == null) {
     * sharedCycAccessInstance = this;
     * } 
     */
    cycImageID = getCycImageID();
    
    try {
      DefaultInferenceParameterDescriptions.loadInferenceParameterDescriptions(this, 0);
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
   * Apply any missing SubL patches to the Cyc server.
   * 
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  private void loadSublPatches(CycSessionConfiguration config) throws BaseClientRuntimeException {
    if (!hasServerPatchingBeenChecked) {
      final String forMoreInfo = "For more information, see http://dev.cyc.com/api/server-patching/";
      final SublResourceLoader loader = new SublResourceLoader(this);
      try {
        if (!getServerInfo().isApiCompatible()) {
          String msg = "This server is not compatible with this release of the Core API Suite and cannot be patched; skipping. " + forMoreInfo;
          LOGGER.error(msg);
          throw new BaseClientRuntimeException(msg);
        } else if (config.isServerPatchingAllowed()) {
          // TODO: we may also want Cyc to be able to disallow server patching.
          LOGGER.info("Auto-loading SubL patches is enabled (" + CycSessionConfiguration.class.getSimpleName() + "#isServerPatchingAllowed() == true). " + forMoreInfo);
          loader.loadMissingResources(SublFunctions.SOURCES);
          hasServerPatchingBeenChecked = true;
        } else {
          final List<SublSourceFile> missing = loader.findMissingRequiredResources(SublFunctions.SOURCES);
          if (!missing.isEmpty()) {
            LOGGER.warn("Auto-loading SubL patches is not allowed (" + CycSessionConfiguration.class.getSimpleName() + "#isServerPatchingAllowed() == false); skipping. " + forMoreInfo);
            String numResources = (missing.size() >= 2) ? missing.size() + " required resources" : "required resource";
            String msg = "Cyc server " + getServerInfo().getCycServer() + " is missing " + numResources + ": " + missing.toString() + ". " + forMoreInfo;
            LOGGER.error(msg);
            throw new BaseClientRuntimeException(msg);
          }
        }
      } catch (BaseClientRuntimeException ex) {
        throw ex;
      } catch (Exception ex) {
        String msg = "Could not load SubL patches. " + forMoreInfo;
        LOGGER.warn(msg, ex);
        throw new BaseClientRuntimeException(msg, ex);
      }
    }
  }
  
  private void maybeReEstablishCycConnection() throws CycConnectionException, CycApiException {
    //if (!isSOAPConnection) {
//      if ((previousAccessedMilliseconds + MAX_UNACCESSED_MILLIS) < System.currentTimeMillis()) {
//        Log.current.println("Re-establishing a stale Cyc connection.");
//        reEstablishCycConnection();
//      }
//      else
      if (!((CycConnectionImpl) cycConnection).isValidBinaryConnection()) {
        LOGGER.warn("Re-establishing an invalid Cyc connection  to {}", this);
        reEstablishCycConnection();
      }
    //}
  }

  /**
   * 
   * @param command
   * @throws CycApiException if the Cyc server returns an error
   * @throws IOException if an I/O error occurs
   */
  private void maybeLogCommand(Object command) throws CycApiException, IOException {
    //if (trace > CycConnection.API_TRACE_NONE || areAPIRequestsLoggedToFile) {
    if (trace > CycConnectionImpl.API_TRACE_NONE) {
      final CycList commandCycList = (command instanceof CycList) ? (CycList) command : getObjectTool().makeCycList(
              (String) command);
      final String prettyCommandCycList = commandCycList.toPrettyCyclifiedString(
              "");
      /*
      if (areAPIRequestsLoggedToFile) {
        final String escapedCommandCycList = commandCycList.toPrettyEscapedCyclifiedString(
              "");
        apiRequestLog.write(escapedCommandCycList);
        apiRequestLog.write('\n');
      }
      */
      if (trace > CycConnectionImpl.API_TRACE_NONE) {
        LOGGER.warn("{}\n--> cyc", prettyCommandCycList);
      }
    }
  }

  private void maybeLogResponse(Object[] response) {
    if (trace > CycConnectionImpl.API_TRACE_NONE) {
      String responseString;

      if (response[1] instanceof CycArrayList) {
        responseString = ((CycArrayList) response[1]).toPrettyString("");
      } else if (response[1] instanceof Fort) {
        responseString = ((Fort) response[1]).cyclify();
      } else {
        responseString = response[1].toString();
      }
      LOGGER.warn("cyc --> {}", responseString);
    }
  }

  private Object[] doConverse(final CycConnection cycConnection,
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
    
  /* *
   * Returns a with-bookkeeping-info macro expression.
   *
   * @return a with-bookkeeping-info macro expression
   * /
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
  */
  
}
