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
 * File: CompatibilityResultsImpl.java
 * Project: Session Client
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author nwinant
 */
public class CompatibilityResultsImpl implements CompatibilityResults {
  
  final private boolean result;
  final private List<String> errorMessages;
  
  public CompatibilityResultsImpl(boolean result, List<String> errorMessages) {
    this.result = result;
    this.errorMessages = errorMessages;
  }
  
  public CompatibilityResultsImpl(boolean result, String errorMessage) {
    this(result, Arrays.asList(errorMessage));
  }
  
  
  public CompatibilityResultsImpl(boolean result) {
    this(result, new ArrayList<String>());
  }

  @Override
  public boolean isCompatible() {
    return this.result;
  }

  @Override
  public List<String> getCompatibilityErrorMessages() {
    return Collections.unmodifiableList(this.errorMessages);
  }
  
  @Override
  public String getExceptionMessage() {
    if (!this.getCompatibilityErrorMessages().isEmpty()) {
      return this.getCompatibilityErrorMessages().get(0);
    }
    return null;
  }
  
  protected void addErrorMessage(String msg) {
    this.errorMessages.add(msg);
  }
  
}
