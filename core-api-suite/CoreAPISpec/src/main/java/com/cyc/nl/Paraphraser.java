/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.nl;

/*
 * #%L
 * File: Paraphraser.java
 * Project: Core API Specification
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

import com.cyc.kb.Context;
import java.util.List;

/**
 *
 * @author daves
 * @param <E>
 */
public interface Paraphraser<E> {
  
  /**
   * Returns a paraphrase of the specified object.
   *
   * @param object an object to paraphrase.
   * @return a paraphrase of the specified object.
   */
  public Paraphrase<? extends E> paraphrase(final E object);  

//  /**
//   * Returns a list of paraphrases of the specified objects, attempting to minimize
//   * ambiguity.
//   *
//   * @param objects
//   * @return list of paraphrases
//   */
//  public List<? extends Paraphrase<E>> paraphraseWithDisambiguation(List<E> objects);

  /**
   * Returns the NL generation parameters used by default to generate paraphrases.
   *
   * @return the NL generation parameters used by default to generate paraphrases.
   */
  public NlGenerationParams getParams();
  
  /**
   * Sets the NL generation parameters used by default to generate paraphrases.
   *
   * @param params the NL generation parameters used by default to generate paraphrases.
   */
  public void setParams(NlGenerationParams params);

  /**
   * Set the force.
   *
   * @param nlForce
   * @see NlGenerationParams#setForce(com.cyc.nl.NLForce)
   */
  public void setForce(NlForce nlForce);

  /**
   * Set the blanks-for-vars toggle.
   *
   * @param b
   * @see NlGenerationParams#setBlanksForVars(boolean)
   */
  public void setBlanksForVars(boolean b);

  public Context getDomainContext();

  public NlForce getForce();

  public Context getLanguageContext();

  public GenerationMode getMode();

  public List getNlPreds();

  public void setDomainContext(Context ctx);  
}
