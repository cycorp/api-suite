package com.cyc.base;

/*
 * #%L
 * File: CycAccessOptions.java
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

import com.cyc.base.cycobject.Fort;
import com.cyc.session.SessionOptions;

/**
 *
 * @author nwinant
 */
public interface CycAccessOptions extends SessionOptions {
    
  /**
   * Returns the value of the Cyclist.  Returns the value from {@link com.cyc.baseclient.CycClient#setCurrentCyclist(com.cyc.base.cycobject.Fort) } if one has been set.
   * Otherwise, returns the default as set with {@link com.cyc.baseclient.CycClient#setCyclist(com.cyc.base.cycobject.Fort)}.  If that also has not been set,
   * return null.
   *
   * @return the value of the default Cyclist
   */
  Fort getCyclist();

  /**
   * Returns the value of the project (KE purpose).
   *
   * @return he value of the project (KE purpose)
   */
  Fort getKePurpose();
  
  /**
   * Sets the value of the default Cyclist, whose identity will be attached via #$myCreator bookkeeping
   * assertions to new KB entities created in this session.  Setting the current Cyclist (via {@link CycClient#setCurrentCyclist(com.cyc.base.cycobject.Fort)})
   * will override the default cyclist within that thread.
   *
   * @param cyclist the cyclist term
   */
  void setCyclist(Fort cyclist);
  
  /**
   * Sets the value of the KE purpose, whose project name will be attached via #$myCreationPurpose
   * bookkeeping assertions to new KB entities created in this session.
   *
   * @param project the KE Purpose term
   */
  void setKePurpose(Fort project);

}
