package com.cyc.baseclient.subl.subtypes;

/*
 * #%L
 * File: SubLStringNoArgFunction.java
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

/**
 * A representation of a SubL function which takes no arguments and returns a String.
 * 
 * @author nwinant
 */
public class SubLStringNoArgFunction extends AbstractSubLNoArgFunction<String> {

  /**
   * Creates an instance of SubLStringNoArgFunction bound to a particular symbol, via a string 
   * representation of that symbol.
   * 
   * @param name
   */
  public SubLStringNoArgFunction(String name) {
    super(name);
  }

  /**
   * Evaluates the function and returns a String.
   * 
   * @param access
   * @return
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  @Override
  public String eval(CycAccess access) throws CycConnectionException, CycApiException {
    return access.converse().converseString(buildCommand());
  }

}
