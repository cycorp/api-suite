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

package com.cyc.baseclient.subl.variables;

/*
 * #%L
 * File: SublGlobalVariables.java
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

import com.cyc.baseclient.subl.SublGlobalVariable;
import com.cyc.baseclient.subl.subtypes.BasicSublGlobalVariable;

/**
 *
 * @author nwinant
 */
public class SublGlobalVariables {
  
  public static final SublGlobalVariable REQUIRE_CASE_INSENSITIVE_NAME_UNIQUENESS = 
          new BasicSublGlobalVariable("*require-case-insensitive-name-uniqueness*");
  
  public static final SublGlobalVariable THE_CYCLIST = new BasicSublGlobalVariable("*the-cyclist*");
  
  public static final SublGlobalVariable KE_PURPOSE = new BasicSublGlobalVariable("*ke-purpose*");
  
}
