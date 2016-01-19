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
 * File: ServerFunctionRequirement.java
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

import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.baseclient.CycClient;
import com.cyc.baseclient.subl.SublFunction;

/**
 *
 * @author nwinant
 */
public class ServerFunctionRequirement extends AbstractCycClientRequirement {
  
  final private SublFunction requiredFunction;
  
  public ServerFunctionRequirement(SublFunction requiredFunction) {
    super("Server is missing function " + requiredFunction);
    this.requiredFunction = requiredFunction;
  }
  
  @Override
  public CompatibilityResults checkCompatibility(CycClient client) throws CycApiException, CycConnectionException {
    if (requiredFunction.isBound(client)) {
      return new CompatibilityResultsImpl(true);
    } else {
      return new CompatibilityResultsImpl(false, getDefaultErrorMessage());
    }
  }
  
}
