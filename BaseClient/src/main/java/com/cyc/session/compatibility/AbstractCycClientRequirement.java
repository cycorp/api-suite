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
 * File: AbstractCycClientRequirement.java
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
import com.cyc.session.exception.UnsupportedCycOperationException;

/**
 *
 * @author nwinant
 */
abstract public class AbstractCycClientRequirement implements CycClientRequirement {
  
  final private String msg;
  
  protected AbstractCycClientRequirement(String msg) {
    this.msg = msg;
  }
  
  protected AbstractCycClientRequirement() {
    this(null);
  }

  public String getErrorMessage(CycClient client) throws UnsupportedCycOperationException, CycApiException, CycConnectionException {
    return this.msg;
  }
  
}
