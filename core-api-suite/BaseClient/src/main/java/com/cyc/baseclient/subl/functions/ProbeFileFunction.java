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

package com.cyc.baseclient.subl.functions;

/*
 * #%L
 * File: ProbeFileFunction.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.baseclient.subl.subtypes.SubLStringSingleArgFunction;
import java.io.File;


/**
 * Represents PROBE-FILE.
 * 
 * @author nwinant
 */
public class ProbeFileFunction extends SubLStringSingleArgFunction<String> {

  // Fields
  
  public static final String FUNCTION_NAME = "probe-file";
  
  // Constructor
  
  /**
   * To access this function, call {@link SubLFunctions#PROBE_FILE }.
   */
  protected ProbeFileFunction() {
    super(FUNCTION_NAME);
  }
  
  
  // Public
  
  public File getFile(CycAccess access, String file) throws CycConnectionException, CycApiException {
    return new File(this.eval(access, file));
  }
  
  public File getServerRootDirectory(CycAccess access) throws CycConnectionException, CycApiException {
    final File dir = getFile(access, ".").getParentFile(); // Remove trailing "."
    return dir;
  }
}
