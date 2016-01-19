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

package com.cyc.baseclient.subl;

/*
 * #%L
 * File: SublSourceFile.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;

/**
 * A SubL source file which can potentially be loaded into a running Cyc server by 
 * {@link com.cyc.baseclient.subl.SubLResourceLoader}.
 * 
 * @author nwinant
 */
public interface SublSourceFile {
  
  /**
   * Returns the path of the source file.
   * 
   * @return the path of the source file.
   */
  String getSourceFilePath();
  
  /**
   * Checks whether a particular Cyc server already has the contents of this source file. Be aware
   * that this may be implemented as a simple spot-check. E.g., classes which implement
   * {@link com.cyc.baseclient.subl.SubLFunction} may only check 
   * {@link SubLFunction#isBound(com.cyc.base.CycAccess) }.
   * 
   * @param cyc the Cyc server to check
   * @return whether the Cyc server already has the contents of this source file
   * @throws CycApiException
   * @throws CycConnectionException 
   */
  boolean isMissing(CycAccess cyc) throws CycApiException, CycConnectionException;
  
  /**
   * Determines whether the resource is required to be in present in a particular Cyc server.
   * 
   * @param cyc the Cyc server to check
   * @return whether the function is required to be present
   * @throws CycApiException
   * @throws CycConnectionException 
   */
  boolean isRequired(CycAccess cyc) throws CycApiException, CycConnectionException;
  
//  boolean isLoadable(CycAccess cyc) throws CycApiException, CycConnectionException;
}
