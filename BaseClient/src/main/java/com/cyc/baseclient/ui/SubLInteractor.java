package com.cyc.baseclient.ui;

/*
 * #%L
 * File: SubLInteractor.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

//// Internal Imports
import com.cyc.base.CycAccess;
import com.cyc.base.CycConnectionException;
import java.io.IOException;
//// External Imports
import java.util.Arrays;
import java.util.List;
import com.cyc.base.CycApiException;
import com.cyc.baseclient.api.DefaultSubLWorkerSynch;
import com.cyc.baseclient.util.CycTaskInterruptedException;
import com.cyc.base.CycTimeOutException;

/** 
 * <P>SubLInteractor is designed to...
 *
 * @author baxter, Oct 8, 2008, 1:19:31 PM
 * @version $Id: SubLInteractor.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public class SubLInteractor {

  private CycAccess cycAccess;
  private DefaultSubLWorkerSynch worker;
  private Exception ex;
  private int timeoutMsecs = 0;

  //// Constructors
  /** Creates a new instance of SubLInteractor. */
  SubLInteractor(CycAccess cycAccess) {
    this.cycAccess = cycAccess;
  }

  //// Protected Area
  CycAccess getCycAccess() {
    return cycAccess;
  }

  void cancelLastCommand() {
    try {
      worker.abort();
    } catch (Exception ex) {
    }
  }

  void quit() {
    cancelLastCommand();
  }

  void setTimeoutMsecs(int msecs) {
    timeoutMsecs = msecs;
  }

  /** @return List of the Objects return values from evaluating text. */
  List submitSubL(String text) throws Exception {
    ex = null;
    Object result = null;
    try {
      worker = new DefaultSubLWorkerSynch("(multiple-value-list " + text + ")", cycAccess, timeoutMsecs);
      result = worker.getWork();
    } catch (CycConnectionException cce) {
      ex = cce;
    } catch (CycTimeOutException timeOutException) {
      ex = timeOutException;
    } catch (CycApiException cycApiException) {
      ex = cycApiException;
    } catch (CycTaskInterruptedException openCycTaskInterruptedException) {
      ex = openCycTaskInterruptedException;
    } catch (RuntimeException e) {
      ex = e;
    } catch (Exception e) {
      ex = e;
    } catch (Throwable t) {
      System.out.println(t);
    }
    if (ex == null) {
      if (result instanceof List) {
      return (List) result;
      } else {
        final Object[] results = {result};
        return Arrays.asList(results);
      }
    } else {
      throw ex;
    }
  }
}
