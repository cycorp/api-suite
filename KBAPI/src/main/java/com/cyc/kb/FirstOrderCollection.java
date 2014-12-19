package com.cyc.kb;

/*
 * #%L
 * File: FirstOrderCollection.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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

import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBTypeException;

/**
 * The interface for {@link KBCollection}s whose members are all
 * {@link KBIndividual}s. In other words, no <code>FirstOrderCollection</code>
 * contains a {@link KBCollection}.
 *
 * @author baxter
 */
public interface FirstOrderCollection extends KBCollection {

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
   * @throws KBTypeException
   * @throws CreateException
   */
  @Override
  public FirstOrderCollection addGeneralization(String moreGeneralStr,
          String ctxStr) throws KBTypeException, CreateException;

  /**
   * {@inheritDoc}
   *
   * @throws KBTypeException
   * @throws CreateException
   */
  @Override
  public FirstOrderCollection addGeneralization(KBCollection moreGeneral,
          Context ctx) throws KBTypeException, CreateException;

}
