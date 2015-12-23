package com.cyc.kb;

/*
 * #%L
 * File: KbIndividual.java
 * Project: Core API Object Specification
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
import java.util.Collection;

/**
 * The top-level interface for Cyc <code>#$Individuals</code>.
 * <code>KbIndividual</code> contrasts with {@link KbCollection}.
 *
 * @author vijay
 */
public interface KbIndividual extends KbTerm {

  /**
   * finds the types this KbIndividual is asserted to be an instance of, from
   * the default context specified by {@link com.cyc.kb.DefaultContext#forQuery()}.
   * Essentially, this returns the asserted bindings for <code>?X</code> from
   * <code>(#$isa this ?X)</code>.
   *
   * @return the <code>KbCollection</code>s this <code>KbIndividual</code>
   * belongs to
   */
  public Collection<KbCollection> instanceOf();

  /**
   * finds the types this KbIndividual is an instance of to, from a context.
   * Essentially, this returns the asserted bindings for <code>?X</code> from
   * <code>(#$isa this ?X)</code>.
   *
   * @param ctxStr the string representing the context
   *
   * @return the <code>KbCollection</code>s this <code>KbIndividual</code>
   * belongs to
   */
  public Collection<KbCollection> instanceOf(String ctxStr);
  
  /**
   * finds the types this KbIndividual is an instance of to, from a context.
   * Essentially, this returns the asserted bindings for <code>?X</code> from
   * <code>(#$isa this ?X)</code>.
   *
   * @param ctx the context of query
   *
   * @return the <code>KbCollection</code>s this <code>KbIndividual</code>
   * belongs to.
   */
  public Collection<KbCollection> instanceOf(Context ctx);

}
