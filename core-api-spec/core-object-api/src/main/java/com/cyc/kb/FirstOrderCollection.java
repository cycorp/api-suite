package com.cyc.kb;

/*
 * #%L
 * File: FirstOrderCollection.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;

import java.util.Collection;

/**
 * The interface for {@link KbCollection}s whose members are all
 * {@link KbIndividual}s. In other words, no <code>FirstOrderCollection</code>
 * contains a {@link KbCollection}.
 *
 * @author baxter
 */
public interface FirstOrderCollection extends KbCollection {

  /**
   * {@inheritDoc}
   *
   * The only difference is that it is known that any generalization of
   * <code>FirstOrderCollection</code> is itself a
   * <code>FirstOrderCollection</code>.
   */
  @Override
  public Collection<FirstOrderCollection> getGeneralizations();

  /**
   * {@inheritDoc}
   *
   * The only difference is that it is known that any generalization of
   * <code>FirstOrderCollection</code> is itself a
   * <code>FirstOrderCollection</code>.
   */
  @Override
  public Collection<FirstOrderCollection> getGeneralizations(String ctxStr);

  /**
   * {@inheritDoc}
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  public FirstOrderCollection addGeneralization(String moreGeneralStr,
          String ctxStr) throws KbTypeException, CreateException;

  /**
   * {@inheritDoc}
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  public FirstOrderCollection addGeneralization(KbCollection moreGeneral,
          Context ctx) throws KbTypeException, CreateException;

}
