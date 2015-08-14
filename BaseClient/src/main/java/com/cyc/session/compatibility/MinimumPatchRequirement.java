/*
 * Copyright 2015 Cycorp, Inc..
 *
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
 */

package com.cyc.session.compatibility;

/*
 * #%L
 * File: MinimumPatchRequirement.java
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

import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.baseclient.CycClient;
import com.cyc.baseclient.CycClientManager;
import com.cyc.session.CycServerReleaseType;
import com.cyc.session.CycSession;
import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.exception.UnsupportedCycOperationException;

/**
 *
 * @author nwinant
 */
public class MinimumPatchRequirement extends AbstractCycSessionRequirement implements CycSessionRequirement, CycClientRequirement {
  final private CycServerReleaseType serverType;
  final private int minimumPatchNumber;
  
  public MinimumPatchRequirement(String msg, CycServerReleaseType serverType, int minimumPatchNumber) {
    super(msg);
    this.serverType = serverType;
    this.minimumPatchNumber = minimumPatchNumber;
  }
  
  public MinimumPatchRequirement(CycServerReleaseType serverType, int minimumPatchNumber) {
    this(null, serverType, minimumPatchNumber);
  }
  
  @Override
  public boolean isCompatible(CycClient client) throws CycApiException, CycConnectionException {
    try {
      
      if (!serverType.equals(client.getServerInfo().getSystemReleaseType())) {
        return true;
      }
      return client.getServerInfo().getCycMinorRevisionNumber() >= minimumPatchNumber;
      
    } catch (SessionCommunicationException ex) {
      throw new CycConnectionException(ex);
    } catch (SessionCommandException ex) {
      throw new CycApiException(ex);
    }
  }
  
  @Override
  public boolean isCompatible(CycSession session) throws SessionCommunicationException, SessionCommandException {
    try {
      return isCompatible(CycClientManager.getClientManager().fromSession(session));
    } catch (CycApiException ex) {
      throw new SessionCommandException(ex);
    } catch (CycConnectionException ex) {
      throw new SessionCommunicationException(ex);
    }
  }
  
  @Override
  public String getErrorMessage(CycSession session) throws UnsupportedCycOperationException, SessionCommunicationException, SessionCommandException {
    final String msg = super.getErrorMessage(session);
    if (msg != null) {
      return msg;
    }
    return serverType + " must be at patch level " + minimumPatchNumber + " or higher, but is at " + session.getServerInfo().getCycMinorRevisionNumber();
  }
  
}
