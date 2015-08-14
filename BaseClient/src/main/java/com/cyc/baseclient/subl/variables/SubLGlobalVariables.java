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
 * File: SubLGlobalVariables.java
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

import com.cyc.baseclient.subl.SubLGlobalVariable;
import com.cyc.baseclient.subl.subtypes.BasicSubLGlobalVariable;

/**
 *
 * @author nwinant
 */
public class SubLGlobalVariables {
  
  public static final SubLGlobalVariable REQUIRE_CASE_INSENSITIVE_NAME_UNIQUENESS = 
          new BasicSubLGlobalVariable("*require-case-insensitive-name-uniqueness*");
  
  public static final SubLGlobalVariable THE_CYCLIST = new BasicSubLGlobalVariable("*the-cyclist*");
  
  public static final SubLGlobalVariable KE_PURPOSE = new BasicSubLGlobalVariable("*ke-purpose*");
  
  public static final SubLGlobalVariable IMAGE_PURPOSE = new BasicSubLGlobalVariable("*image-purpose*");
  
  public static final BasicSubLGlobalVariable SYSTEM_CODE_VERSION_STRING = new BasicSubLGlobalVariable("*system-code-version-string*");
  
  public static final BasicSubLGlobalVariable SYSTEM_CODE_RELEASE_STRING = new BasicSubLGlobalVariable("*system-code-release-string*");
  
}
