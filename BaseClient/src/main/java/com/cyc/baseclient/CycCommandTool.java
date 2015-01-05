package com.cyc.baseclient;

/*
 * #%L
 * File: CycCommandTool.java
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

import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.CommandTool;
import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import java.io.IOException;
import java.util.List;

/**
 * Methods for sending commands to a Cyc server in the SubL language, and receiving
 * the results expressed as Java objects.
 * 
 * @author nwinant
 */
public class CycCommandTool implements CommandTool {
  
  protected CycCommandTool(CycAccess client) {
    this.client = client;
  }
  
  
  // Public
  
  /**
   * Converses with Cyc to perform an API command whose result is returned as an object.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result of processing the API command
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public Object converseObject(Object command)
          throws CycConnectionException, CycApiException {
    Object[] response = {null, null};
    response = converse(command);

    if (response[0].equals(Boolean.TRUE)) {
      return response[1];
    } else {
      throw new ConverseException(command, this.getCyc(), response);
    }
  }

  /**
   * Converses with Cyc to perform an API command whose result is returned as a list. The symbol
   * nil is returned as the empty list.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result of processing the API command
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycArrayList converseList(Object command)
          throws CycConnectionException, CycApiException {
    Object[] response = {null, null};
    response = converse(command);

    if (response[0].equals(Boolean.TRUE)) {
      if (response[1].equals(CycObjectFactory.nil)) {
        return new CycArrayList();
      } else {
        if (response[1] instanceof CycArrayList) {
          return (CycArrayList) response[1];
        }
      }
    }
    throw new ConverseException(command, this.getCyc(), response);
  }

  /**
   * Converses with Cyc to perform an API command whose result is returned as a Cyc sentence.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result of processing the API command
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public FormulaSentence converseSentence(Object command)
          throws CycConnectionException, CycApiException {
    return new CycFormulaSentence(converseList(command));
  }

  /**
   * Converses with Cyc to perform an API command whose result is returned as a CycObject.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result of processing the API command
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycObject converseCycObject(Object command)
          throws CycConnectionException, CycApiException {
    Object[] response = {null, null};
    response = converse(command);

    if (response[0].equals(Boolean.TRUE)) {
      if (response[1].equals(CycObjectFactory.nil)) {
        return new CycArrayList();
      } else {
        return (CycObject) response[1];
      }
    } else {
      throw new ConverseException(command, this.getCyc(), response);
    }
  }

  /**
   * Converses with Cyc to perform an API command whose result is returned as a String.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result of processing the API command
   *
   * @throws IOException if a data communication error occurs
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws CycApiException if the api request results in a cyc server error
   * @throws BaseClientRuntimeException if the return from Cyc is not a string
   */
  @Override
  public String converseString(Object command)
          throws CycConnectionException, CycApiException {
    Object[] response = {null, null};
    response = converse(command);

    if (response[0].equals(Boolean.TRUE)) {
      if (!(response[1] instanceof String)) {
        throw new BaseClientRuntimeException("Expected String but received (" + response[1].getClass()
                + ") " + response[1] + "\n in response to command " + command);
      }

      return (String) response[1];
    } else {
      throw new ConverseException(command, this.getCyc(), response);
    }
  }
  
  /**
   * Converses with Cyc to perform an API command whose result is returned as a boolean.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result of processing the API command
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean converseBoolean(Object command)
          throws CycConnectionException, CycApiException {
    Object[] response = {null, null};
    response = converse(command);

    if (response[0].equals(Boolean.TRUE)) {
      if (response[1].toString().equals("T")) {
        return true;
      } else {
        return false;
      }
    } else {
      throw new ConverseException(command, this.getCyc(), response);
    }
  }

  /**
   * Converses with Cyc to perform an API command whose result is returned as an int.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result of processing the API command
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public int converseInt(Object command)
          throws CycConnectionException, CycApiException {
    Object[] response = {null, null};
    response = converse(command);

    if (response[0].equals(Boolean.TRUE)) {
      return Integer.valueOf(response[1].toString());
    } else {
      throw new ConverseException(command, this.getCyc(), response);
    }
  }

  /**
   * Converses with Cyc to perform an API command whose result is void.
   *
   * @param command the command string or CycArrayList
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void converseVoid(final Object command)
          throws CycConnectionException, CycApiException {
    Object[] response = {null, null};
    Object voidCommand = ensureCommandReturnsSerializable(command);
    response = converse(voidCommand);

    if (response[0].equals(Boolean.FALSE)) {
      throw new ConverseException(command, this.getCyc(), response);
    }
  }
  
  private Object ensureCommandReturnsSerializable(Object cmd) {
    if (cmd instanceof String) {
      cmd = "(progn " + (String)cmd + " nil)";
    } else if (cmd instanceof CycArrayList) {
      cmd = CycArrayList.list("progn", cmd, "nil" );
    }
    return cmd;
  }
  
  /**
   * Converses with Cyc to perform an API command. Creates a new connection for this command if
   * the connection is not persistent.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result as an object array of two objects
   * @see com.cyc.base.conn.CycConnectionInterface#converse(java.lang.Object) 
   * @throws CycApiException if the api request results in a cyc server error
   */
  // @note Used by BOAPI.
  @Override
  public Object[] converseRaw(Object command)
          throws CycConnectionException, CycApiException {
    return converse(command);
  }
  
  /**
   * Wraps the given api command string with the binding environment for bookkeeping assertions.
   *
   * @param command the given command string
   *
   * @return the given api command string with the binding environment for bookkeeping assertions
   */
  @Override
  public String wrapBookkeeping(String command) {
    final String projectName = getProjectName();
    final String cyclistName = getCyclistName();

    String wrappedCommand = "(with-bookkeeping-info (new-bookkeeping-info " + cyclistName
            + " (the-date) " + projectName + " (the-second))\n"
            + wrapCyclistAndPurpose(command, cyclistName, projectName)
            + "\n)";

    return wrappedCommand;
  }

  @Override
  public String wrapCyclistAndPurpose(String command) {
    final String projectName = getProjectName();
    final String cyclistName = getCyclistName();
    return wrapCyclistAndPurpose(command, cyclistName, projectName);
  }

  @Override
  public String wrapCyclistAndPurpose(String command, String cyclistName,
          String projectName) {
    return "(clet ((*require-case-insensitive-name-uniqueness* nil)\n"
            + "       (*the-cyclist* " + cyclistName + ")\n"
            + "       (*ke-purpose* " + projectName + "))\n"
            + "    " + command
            + "\n)";
  }

  @Override
  public String wrapForwardInferenceRulesTemplates(String command,
          List<Fort> templates) {
    if ((templates == null) || templates.isEmpty()) {
      return command;
    }
    StringBuilder loadRules = new StringBuilder("(union");
    for (Fort template : templates) {
      loadRules.append(" (creation-template-allowable-rules ").append(
              template.stringApiValue()).append(")");
    }
    loadRules.append(")");
    command = wrapDynamicBinding(command, "*forward-inference-allowed-rules*",
            loadRules.toString());
    return command;
  }

  @Override
  public String wrapDisableWffChecking(String command) {
    command = wrapDynamicBinding(command, "*assume-forward-deduction-is-wf?*",
            "t");
    command = wrapDynamicBinding(command, "*assume-assert-sentence-is-wf?*", "t");
    command = "(with-strict-wff " + command + ")";
    return command;
  }

  @Override
  public String wrapDynamicBinding(String command, String symbolName,
          String apiValue) {
    return "(clet ((" + symbolName + " " + apiValue + "))\n"
            + "  " + command + "\n)";
  }
    
  
  // Protected

  /** 
   * Converses with Cyc to perform an API command. Creates a new connection for this command if
   * the connection is not persistent.
   *
   * @param command the command string or CycArrayList
   *
   * @return the result as an object array of two objects
   * @see com.cyc.base.conn.CycConnectionInterface#converse(java.lang.Object) 
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  protected Object[] converse(Object command)
          throws CycConnectionException, CycApiException {
    return getCycClient().converse(command);
  }
  
  protected CycAccess getCyc() {
    return this.client;
  }
  
  
  // Private
    
  private CycClient getCycClient() {
    return CycClientManager.getClientManager().fromCycAccess(getCyc());
  }
  
  private String getProjectName() {
    // SessionOptions#getKePurposeName() does not return the API value, so use SessionOptions#getKePurpose()#stringApiValue()
    return (getCycClient().getOptions().getKePurpose() == null) ? "nil" : getCycClient().getOptions().getKePurpose().stringApiValue();
  }
  
  private String getCyclistName() {
    // SessionOptions#getCyclistName() does not return the API value, so use SessionOptions#getCyclist()#stringApiValue()
    return (getCycClient().getOptions().getCyclist() == null) ? "nil" : getCycClient().getOptions().getCyclist().stringApiValue();
  }
  
  
  // Internal
  
  static public class ConverseException extends CycApiException {

    private ConverseException(final Object command, CycAccess cyc, final Object[] response) {
      super(response[1].toString() + "\nrequest " + (cyc.hasStaticCycServer() ? "to " + (cyc.getHostName() + ":" + cyc.getBasePort()) : "") + " : " + makeRequestString(command));
      if (response[1] instanceof CycApiException) {
        this.initCause((CycApiException) response[1]);
      }
    }

    private static String makeRequestString(final Object command) {
      if (command instanceof CycArrayList) {
        return ((CycArrayList) command).cyclify();
      } else {
        return command.toString();
      }

    }
  }
  
  final private CycAccess client;
}
