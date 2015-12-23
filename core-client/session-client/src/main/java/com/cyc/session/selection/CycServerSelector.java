/*
 * Copyright 2015 Cycorp, Inc.
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
package com.cyc.session.selection;

/*
 * #%L
 * File: CycServerSelector.java
 * Project: Session Client
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

import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSession;

/**
 * Selects a {@link com.cyc.session.CycSession} by the {@link com.cyc.session.CycServerAddress} of
 * the server to which it's connected.
 * 
 * @author nwinant
 */
public class CycServerSelector implements SessionSelector {
  
  private final CycServerAddress server;
  
  public CycServerSelector(CycServerAddress server) {
    if (server == null) {
      throw new NullPointerException("CycServerAddress is null");
    }
    this.server = server;
  }
  
  public CycServerAddress getServer() {
    return this.server;
  }

  @Override
  public boolean matchesSession(CycSession session) {
    return server.equals(session.getServerInfo().getCycServer());
  }
  
}
