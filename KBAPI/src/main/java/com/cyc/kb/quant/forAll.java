package com.cyc.kb.quant;

/*
 * #%L
 * File: forAll.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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

import com.cyc.kb.KBCollection;
import com.cyc.kb.client.QuantifierImpl;
import com.cyc.kb.exception.KBApiException;

/**
 * THIS CLASS IS NOT SUPPORTED IN KB API 1.0.
 * @author vijay
 */
public class forAll {
  
  /*
  private static Quantifier core = null;
  
  public forAll () throws KBApiException {
    if (core == null){
      core = new Quantifier("forAll");
    }
  }
  */
  
  /**
   * UNDER DEVELOPMENT - DO NOT USE
   * @param c
   * @return
   * @throws KBApiException 
   */
  public static QuantifiedRestrictedVariable quantifiedInstanceOf (KBCollection c) throws KBApiException {
    return new ForAllQuantifiedInstanceRestrictedVariable (c);
  }
  /**
   * UNDER DEVELOPMENT - DO NOT USE
   * @param ul
   * @return
   * @throws KBApiException 
   */
  public static QuantifiedRestrictedVariable quantified (RestrictedVariable ul) throws KBApiException {
    return new ForAllQuantifiedRestrictedVariable (ul);
  }
}
