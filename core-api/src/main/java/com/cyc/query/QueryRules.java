package com.cyc.query;

/*
 * #%L
 * File: QueryRules.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
import com.cyc.kb.Rule;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionCommunicationException;
import java.util.Collection;

/**
 *
 * @author nwinant
 */
public interface QueryRules {

  Collection<Rule> getAllowedRules() throws SessionCommunicationException;

  Collection<Rule> getForbiddenRules() throws SessionCommunicationException;

  Collection<Rule> getPracticeRules() throws SessionCommunicationException;

  /**
   * Relevant only for queries loaded from the KB (instances of CycLQuerySpecification), this loads
   * all of the rules specified on the query and only allows Cyc to use those rules when running
   * this query.
   * <p>
   * This is normally performed automatically by Cyc, but some older releases may not do so.
   * 
   * @param includePracticeRules whether to include queryPracticeRules
   * 
   * @throws QueryConstructionException
   * @throws SessionCommunicationException
   */
  void useOnlySpecifiedRules(boolean includePracticeRules) 
          throws QueryConstructionException, SessionCommunicationException;

  /**
   * If any rules are specified on the inference parameters for this query, remove them and let Cyc
   * decide what rules are relevant.
   */
  void allowAllRules();

}
