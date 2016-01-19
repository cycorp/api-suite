package com.cyc.kb.client.quant;

/*
 * #%L
 * File: ThereExists.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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



import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.kb.KbCollection;
import com.cyc.kb.Quantifier;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.QuantifierImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.exception.KbException;

/**
 * THIS CLASS IS NOT SUPPORTED IN KB API 1.0.
 * @author vijay
 */
public class ThereExists {
  private static Quantifier core = null;

  public ThereExists() throws KbException {
    if (core == null) {
      core = new QuantifierImpl("thereExists");
    }
  }

  public static QuantifiedRestrictedVariable quantified(KbCollection c) throws KbException {
    return new ThereExistsQuantifiedInstanceRestrictedVariable(c);
  }

  public static QuantifiedRestrictedVariable quantified(RestrictedVariable ul) throws KbException {
    return new ThereExistsQuantifiedRestrictedVariable(ul);
  }

  public static Sentence quantified(SentenceImpl s, RestrictedVariable rv) throws KbException {
    FormulaSentence cfs = ((FormulaSentence) s.getCore());
    cfs.existentiallyBind((CycVariable) rv.getVariable().getCore());
    return new SentenceImpl(cfs);
  }

}
