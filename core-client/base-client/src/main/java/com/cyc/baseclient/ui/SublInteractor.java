package com.cyc.baseclient.ui;

/*
 * #%L
 * File: SublInteractor.java
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

//// Internal Imports
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import java.io.IOException;
//// External Imports
import java.util.Arrays;
import java.util.List;
import com.cyc.base.exception.CycApiException;
import com.cyc.baseclient.connection.DefaultSublWorkerSynch;
import com.cyc.baseclient.exception.CycTaskInterruptedException;
import com.cyc.base.exception.CycTimeOutException;

/** 
 * <P>SubLInteractor is designed to...
 *
 * @author baxter, Oct 8, 2008, 1:19:31 PM
 * @version $Id: SublInteractor.java 162904 2015-12-02 18:35:34Z nwinant $
 */
public class SublInteractor {

  private CycAccess cycAccess;
  private DefaultSublWorkerSynch worker;
  private Exception ex;
  private int timeoutMsecs = 0;

  //// Constructors
  /** Creates a new instance of SubLInteractor. */
  SublInteractor(CycAccess cycAccess) {
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
      worker = new DefaultSublWorkerSynch("(multiple-value-list " + text + ")", cycAccess, timeoutMsecs);
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
