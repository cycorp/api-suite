package com.cyc.kb;

/*
 * #%L
 * File: ArgPosition.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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

import java.util.List;

/**
 *
 * @author nwinant
 */
public interface ArgPosition extends com.cyc.base.cycobject.ArgPosition {

  /**
   * Destructively extend this arg position by another arg position.
   *
   * @param otherArgPos
   * @return this ArgPosition.
   */
  public ArgPosition extend(ArgPosition otherArgPos);
  
  /**
   * Destructively modify this ArgPosition to be its parent arg position.
   *
   * @return this ArgPosition.
   */
  public ArgPosition toParent();
}
