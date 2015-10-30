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
 * File: CycClientRequirementList.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwinant
 */
public class CycClientRequirementList extends ArrayList<CycClientRequirement> implements List<CycClientRequirement>, CycClientRequirement {
  
  // Fields
  
  final private static Logger LOGGER = LoggerFactory.getLogger(CycClientRequirementList.class);
  
  
  // Static
  
  public static CycClientRequirementList create() {
    return new CycClientRequirementList();
  }
  
  public static CycClientRequirementList fromList(CycClientRequirement... compatibilityTests) {
    final CycClientRequirementList suite = new CycClientRequirementList();
    suite.addAll(Arrays.asList(compatibilityTests));
    return suite;
  }
  
  
  // Basic test methods
  
  @Override
  public CompatibilityResults checkCompatibility(CycClient client) throws CycApiException, CycConnectionException {
    final List<String> errorMessages = new ArrayList<String>();
    boolean compatible = true;
    for (CycClientRequirement requirement : this) {
      //LOGGER.info("Checking {}", requirement.getClass());
      final CompatibilityResults reqResult = requirement.checkCompatibility(client);
      if (!reqResult.isCompatible()) {
        compatible = false;
        errorMessages.addAll(reqResult.getCompatibilityErrorMessages());
        for (String errMsg : reqResult.getCompatibilityErrorMessages()) {
          LOGGER.error("{}: {}", requirement.getClass().getSimpleName(), errMsg);
        }
        //LOGGER.warn("{}: {}", requirement.getClass().getSimpleName(), reqResult.isCompatible());
      } else {
        LOGGER.info("{}: {}", requirement.getClass().getSimpleName(), reqResult.isCompatible());
      }
    }
    if (compatible) {
      return new CompatibilityResultsImpl(compatible);
    } else {
      return new CompatibilityResultsImpl(compatible, errorMessages);
    }
  }

}
