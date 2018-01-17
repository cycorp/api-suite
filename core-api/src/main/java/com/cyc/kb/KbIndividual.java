package com.cyc.kb;

/*
 * #%L
 * File: KbIndividual.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
import com.cyc.Cyc;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.KbIndividualService;
import java.util.Collection;

/**
 * The top-level interface for Cyc <code>#$Individuals</code>.
 * <code>KbIndividual</code> contrasts with {@link KbCollection}.
 *
 * @author vijay
 */
public interface KbIndividual extends KbTerm {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>KbIndividual</code> with the name <code>nameOrId</code>. This static method wraps
   * a call to {@link KbIndividualService#get(java.lang.String) }; see that method's documentation
   * for more details.
   *
   * @param nameOrId the string representation or the HLID of the #$Individual
   *
   * @return a new KbIndividual
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbIndividual get(String nameOrId) throws KbTypeException, CreateException {
    return Cyc.getKbIndividualService().get(nameOrId);
  }

  /**
   * Find or create a <code>KbIndividual</code> object named <code>nameOrId</code>. This static
   * method wraps a call to {@link KbIndividualService#findOrCreate(java.lang.String) }; see that
   * method's documentation for more details.
   *
   * @param nameOrId the string representation or the HLID of the #$Individual
   *
   * @return a new KbIndividual
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbIndividual findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return Cyc.getKbIndividualService().findOrCreate(nameOrId);
  }

  /**
   * Find or create a <code>KbIndividual</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. This static method wraps a call to
   * {@link KbIndividualService#findOrCreate(java.lang.String, java.lang.String)}; see that method's
   * documentation for more details.
   *
   * @param nameOrId         the string representation or the HLID of the #$Individual
   * @param constraintColStr the string representation of the collection that this #$Individual will
   *                         instantiate
   *
   * @return a new KbIndividual
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbIndividual findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return Cyc.getKbIndividualService().findOrCreate(nameOrId, constraintColStr);
  }

  /**
   * Find or create a <code>KbIndividual</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in <code>ctx</code>. This static method wraps a call
   * to
   * {@link KbIndividualService#findOrCreate(java.lang.String, java.lang.String, java.lang.String)};
   * see that method's documentation for more details.
   *
   * @param nameOrId         the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that this #$Individual will
   *                         instantiate
   * @param ctxStr           the context in which the resulting object must be an instance of
   *                         constraintCol
   *
   * @return a new KbIndividual
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbIndividual findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return Cyc.getKbIndividualService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }

  /**
   * Find or create a <code>KbIndividual</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. This static method wraps a call to
   * {@link KbIndividualService#findOrCreate(java.lang.String, com.cyc.kb.KbCollection)}; see that
   * method's documentation for more details.
   *
   * @param nameOrId      the string representation or the HLID of the #$Individual
   * @param constraintCol the collection that this #$Individual will instantiate
   *
   * @return a new KbIndividual
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbIndividual findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return Cyc.getKbIndividualService().findOrCreate(nameOrId, constraintCol);
  }

  /**
   * Find or create a <code>KbIndividual</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in <code>ctx</code>. This static method wraps a call
   * to
   * {@link KbIndividualService#findOrCreate(java.lang.String, com.cyc.kb.KbCollection, com.cyc.kb.Context)};
   * see that method's documentation for more details.
   *
   * @param nameOrId      the string representation or the HLID of the #$Individual
   * @param constraintCol the collection that this #$Individual will instantiate
   * @param ctx           the context in which the resulting object must be an instance of
   *                      constraintCol
   *
   * @return a new KbIndividual
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbIndividual findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return Cyc.getKbIndividualService().findOrCreate(nameOrId, constraintCol, ctx);
  }
  
  public static boolean existsAsType(String nameOrId) {
    return Cyc.getKbIndividualService().existsAsType(nameOrId);
  }
  
  public static KbStatus getStatus(String nameOrId) {
    return Cyc.getKbIndividualService().getStatus(nameOrId);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * finds the types this KbIndividual is asserted to be an instance of, from
 the default getContextService specified by {@link com.cyc.kb.DefaultContext#forQuery()}.
   * Essentially, this returns the asserted bindings for <code>?X</code> from
   * <code>(#$isa this ?X)</code>.
   *
   * @return the <code>KbCollection</code>s this <code>KbIndividual</code>
   * belongs to
   */
  public Collection<KbCollection> instanceOf();

  /* *
   * finds the types this KbIndividual is an instance of to, from a getContextService.
   * Essentially, this returns the asserted bindings for <code>?X</code> from
   * <code>(#$isa this ?X)</code>.
   *
   * @param ctxStr the string representing the getContextService
   *
   * @return the <code>KbCollection</code>s this <code>KbIndividual</code>
   * belongs to
   * /
  public Collection<KbCollection> instanceOf(String ctxStr);
  */
  
  /**
   * finds the types this KbIndividual is an instance of to, from a getContextService.
   * Essentially, this returns the asserted bindings for <code>?X</code> from
   * <code>(#$isa this ?X)</code>.
   *
   * @param ctx the getContextService of getQueryService
   *
   * @return the <code>KbCollection</code>s this <code>KbIndividual</code>
   * belongs to.
   */
  public Collection<KbCollection> instanceOf(Context ctx);
  
  /* *
   * {@inheritDoc}
   * /
  @Override
  KbIndividual rename(String name) throws InvalidNameException;
  
  /**
   * {@inheritDoc}
   * /
  @Override
  KbIndividual addQuotedIsa(KbCollection collection, Context getContextService) 
         throws KbTypeException, CreateException;
  
  /**
   * {@inheritDoc}
   * /
  @Override
  KbIndividual instantiates(KbCollection collection, Context getContextService) 
          throws KbTypeException, CreateException;
  
  /**
   * {@inheritDoc}
   * /
  @Override
  KbIndividual instantiates(String collectionStr, String contextStr) 
          throws KbTypeException, CreateException;
  
  /**
   * {@inheritDoc}
   * /
  @Override
  KbIndividual instantiates(KbCollection collection) throws KbTypeException, CreateException;
  */
}
