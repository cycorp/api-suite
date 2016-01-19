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
 * File: FboundpFunction.java
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
import com.cyc.base.cycobject.CycSymbol;
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import static com.cyc.baseclient.connection.SublApiHelper.makeNestedSubLStmt;
import static com.cyc.baseclient.connection.SublApiHelper.makeSubLStmt;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.baseclient.subl.SublFunction;

/**
 * Represents FBOUNDP, which is used to check whether a symbol is bound to a function.
 * 
 * <p>To check if a <em>variable</em> is bound, see {@link SubLFunctions#BOUNDP }.
 * 
 * <p>Because this class underlies {@link com.cyc.baseclient.subl.subtypes.BasicSubLFunction#isBound(com.cyc.base.CycAccess) }, 
 * it does not extend that class or any of its sub-classes.
 * 
 * @author nwinant
 */
public class FboundpFunction extends DefaultCycObject implements SublFunction {
  
  // Fields
  
  public static final String FUNCTION_NAME = "fboundp";
  final private CycSymbol symbol;
  
  
  // Constructor
  
  /**
   * To access this function, call {@link SubLFunctions#FBOUNDP }.
   */
  protected FboundpFunction() {
    this.symbol = makeCycSymbol(FUNCTION_NAME);
  }
  
  
  // Public
  
  public boolean eval(CycAccess access, CycSymbol symbol) throws CycConnectionException, CycApiException {
    if (symbol == null) {
      throw new CycApiException("Error building command for " + toString() + ": Argument is null.");
    }
    final String command = makeSubLStmt("boolean", makeNestedSubLStmt(this.getSymbol(), symbol));
    return access.converse().converseBoolean(command);
  }
  
  public boolean eval(CycAccess access, SublFunction function) throws CycConnectionException, CycApiException {
    return eval(access, function.getSymbol());
  }
  
  public boolean eval(CycAccess access, String symbolName) throws CycConnectionException, CycApiException {
    return eval(access, makeCycSymbol(symbolName));
  }
  
  @Override
  public CycSymbol getSymbol() {
    return this.symbol;
  }
  
  @Override
  public boolean isBound(CycAccess cyc) throws CycApiException, CycConnectionException {
    return true;  // We'll just go ahead and assume it's bound. - nwinant
  }
  
  @Override
  public boolean isRequired(CycAccess cyc) throws CycApiException, CycConnectionException {
    return true;
  }
  
  @Override
  public String toString() {
    return this.getSymbol().toString();
  }

  @Override
  public int compareTo(Object o) {
    return (o != null) ? toString().compareTo(o.toString()) : 1;
  }
  
}
