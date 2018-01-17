package com.cyc.nl;

/*
 * #%L
 * File: NlGenerationParams.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
import com.cyc.kb.KbPredicate;

import java.util.List;


/**
 *
 * @author baxter
 */
public interface NlGenerationParams {

  /**
   * 
   * @return true iff we should generate NL quantifiers for open variables.
   */
  Boolean getQuantifyVars();

  /**
   * 
   * @return true iff we should not say things like "for all X..."
   */
  Boolean getHideExplicitUniversals();

  /**
   * @return the max time allotted for each generation call (in milliseconds).
   */
  public Integer getMaxTime();
  
  /**
   * 
   * @return the list of agreement predicates to use
   */
  List<KbPredicate> getNlPreds();

  /**
   * 
   * @return the illocutionary force to generate
   * @see NlForce
   */
  NlForce getForce();

  /**
   * Set the illocutionary force to generate
   * @param force
   * @return 
   * @see NlForce
   */
  NlGenerationParams setForce(NlForce force);

  /**
   * Set the list of agreement predicates to use
   * @param preds
   * @return 
   */
  NlGenerationParams setNlPreds(List<? extends KbPredicate> preds);

  /**
   * Should we generate NL quantifiers for open variables?
   * @param quantifyVars
   * @return 
   */
  NlGenerationParams setQuantifyVars(boolean quantifyVars);

  /**
   * Should we say things like "for all X..."?
   * @param hideUniversals
   * @return 
   */
  NlGenerationParams setHideExplicitUniversals(boolean hideUniversals);

  /**
   * 
   * @return the microtheory from which domain-related knowledge is retrieved
   */
  Context getDomainContext();
  /**
   * 
   * @return the microtheory from which language-related knowledge is retrieved
   */
  Context getLanguageContext();

  /**
   * 
   * @return the generation mode
   * @see GenerationMode
   */
  GenerationMode getMode();

  /**
   * 
   * @return true iff we want to value precision over conciseness
   */
  boolean isPrecise();

  /**
   * Should we try to generate unambiguous paraphrases?
   * @param shouldDisambiguate
   * @return 
   */
  NlGenerationParams setDisambiguate(boolean shouldDisambiguate);

  /**
   * Set the microtheory from which language-related knowledge is retrieved.
   * @param languageContext
   * @return 
   */
  NlGenerationParams setLanguageContext(Context languageContext);

  /**
   * Set the microtheory from which domain-related knowledge is retrieved.
   * @param domainContext
   * @return 
   */
  NlGenerationParams setDomainContext(Context domainContext);

  /**
   * @param maxTime the maximum number of milliseconds to be allotted to each generation call.
   */
  public void setMaxTime(Integer maxTime);


  /**
   * Set the generation mode.
   * @param mode
   * @return 
   * @see GenerationMode
   */
  NlGenerationParams setMode(GenerationMode mode);

  /**
   * Should we value precision over conciseness?
   * @param precise
   * @return 
   */
  NlGenerationParams setPrecise(boolean precise);

  /**
   * 
   * @return true iff we should try to generate unambiguous paraphrases
   */
  public boolean shouldDisambiguate();

  /**
   * 
   * @return true iff we should generate bulleted lists when in HTML mode
   * @see GenerationMode#HTML
   */
  public Boolean getUseBulletsInHtmlMode();

  /**
   * Should we generate bulleted lists when in HTML mode
   * @param useBullets
   * @return 
   * @see GenerationMode#HTML
   */
  public NlGenerationParams setUseBulletsInHtmlMode(boolean useBullets);

    /**
   * 
   * @return true iff we should generate bulleted lists when in HTML mode
   * @see GenerationMode#HTML
   */
  public HtmlLinkType getHtmlLinkType();

  /**
   * Should we generate bulleted lists when in HTML mode
   * @param htmlLinkType
   * @return 
   * @see GenerationMode#HTML
   */
  public NlGenerationParams setHtmlLinkType(HtmlLinkType htmlLinkType);
  
  /**
   * @return true iff we should generate _____ for variables.
   */
  Boolean shouldUseBlanksForVars();

  /**
   * 
   * @param generateBlanks Should we generate _____ for variables?
   * @return 
   */
  NlGenerationParams setBlanksForVars(boolean generateBlanks);
}
