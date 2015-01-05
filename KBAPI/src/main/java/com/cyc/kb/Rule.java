package com.cyc.kb;

/*
 * #%L
 * File: Rule.java
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
/**
 * The interface for CycL rules (i.e. #$implies {@link Assertion}s).  
 * Refer to #$CycLRuleAssertion for more details. The rule relates two sentences, an
 * antecedent and a consequent.
 * Free variables are implicitly universally quantified, though other variables may be explicitly
 * existentially quantified.
 *
 * To create a Rule, see the
 * {@link com.cyc.kb.client.RuleImpl#findOrCreate(String)}, {@link com.cyc.kb.client.RuleImpl#get(String)}
 * and related methods.
 *
 *
 * @author vijay
 * @version $Id: Rule.java 154990 2014-11-14 22:46:51Z nwinant $
 * @since 1.0
 */
public interface Rule extends Assertion {

  /**
   * Return the first argument of the Rule assertion. This is the antecedent of the rule and the
   * first argument of #$implies operator.
   *
   * @return the first argument of #$implies.
   */
  public Sentence getAntecedent();

  /**
   * Return the second argument of the Rule assertion. This is the consequent of the rule and the
   * second argument of the #$implies operator.
   *
   * @return the second argument of #$implies.
   */
  public Sentence getConsequent();
}
