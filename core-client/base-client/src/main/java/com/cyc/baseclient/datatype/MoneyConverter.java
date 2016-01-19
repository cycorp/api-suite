package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: MoneyConverter.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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

import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Naut;
import com.cyc.baseclient.CommonConstants;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import com.cyc.baseclient.exception.CycParseException;

/**
 * <P>MoneyConverter is designed to convert java-style money amounts to their
 * corresponding CycL representations and vice versa.
 *
 * @todo Add more currencies. Currently only supports USD.
 *
 * @author nwinant, May 21, 2010, 2:03:06 PM
 * @version $Id: MoneyConverter.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public class MoneyConverter extends DataTypeConverter<Money> {

  //// Constructors
  private MoneyConverter() {
    SHARED_INSTANCE = this;
  }

  //// Public Area
  /** Returns an instance of
   * <code>MoneyConverter</code>.
   *
   * If an instance has already been created, the existing one will be returned.
   * Otherwise, a new one will be created.
   */
  private static MoneyConverter getInstance() {
    MoneyConverter moneyConverter = SHARED_INSTANCE;
    if (moneyConverter == null) {
      moneyConverter = new MoneyConverter();
    }
    return moneyConverter;
  }

  /** Try to parse
   * <code>naut</code> into a Java
   * <code>Money</code>.
   *
   *
   * @param naut A money-denoting Cyc term.
   * @param shouldReportFailure If true, prints stack trace and returns null if the parse fails.
   * @return An equivalent Money object.
   */
  public static Money parseCycMoney(final Naut naut,
          final boolean shouldReportFailure) {
    return getInstance().parse(naut, shouldReportFailure);
  }

  /** Try to parse
   * <code>naut</code> into a Java
   * <code>Money</code>.
   *
   * Prints stack trace and returns null if the parse fails.
   */
  public static Money parseCycMoney(final Naut naut) {
    return getInstance().parse(naut);
  }

  public static boolean isCycMoney(final Object object) {
    return getInstance().isOfType(object);
  }

  public static Naut toCycMoney(final Money obj) throws CycParseException {
    return getInstance().toCycTerm(obj);
  }

  //// Protected Area
  @Override
  protected Money fromCycTerm(final CycObject cycObject) throws CycParseException {
    final Naut naut;
    try {
      naut = (Naut) NautImpl.convertIfPromising(cycObject);
    } catch (ClassCastException ex) {
      throw new IllegalArgumentException();
    }
    final Currency currency = lookupCurrency(naut.getFunctor());
    if (currency == null) {
      throwParseException(naut,
              this.getClass().getName() + " does not recognize " + naut.getFunctor() + " as a currency.");
    } else if (naut.getArity() != 1) {
      throwParseException(naut,
              this.getClass().getName() + " can only parse Cyc terms with an arity of 1.");
    }

    return new Money(parseBigDecimal(naut.getArg(1), "amount"), currency);
  }

  /**
   * Convert Java object of type
   * <code>Money</code> to
   * <code>naut</code>.
   *
   * @param money
   * @return the NAUT representation of <tt>money</tt>
   * @throws CycParseException if the parse fails.
   */
  @Override
  protected Naut toCycTerm(final Money money) throws CycParseException {
    ensureCurrencyMapInitialized();
    final DenotationalTerm functor = lookupCycCurrencyTerm(money.getCurrency());
    if (functor == null) {
      throwParseException(
              "Cannot find Cyc UnitOfMoney for currency code " + money.getCurrency().getCurrencyCode());
    }
    return new NautImpl(functor, money.getQuantity());
  }

  //// Private Area
  public static DenotationalTerm lookupCycCurrencyTerm(final Currency curr) {
    ensureCurrencyMapInitialized();
    return CURRENCY_TO_CYC_CURRENCY_MAP.get(curr);
  }

  public static Currency lookupCurrency(final DenotationalTerm cycTerm) {
    ensureCurrencyMapInitialized();
    return lookupKeyByValue(CURRENCY_TO_CYC_CURRENCY_MAP, cycTerm);
  }

  private static void ensureCurrencyMapInitialized() {
    if (CURRENCY_TO_CYC_CURRENCY_MAP == null) {
      initializeCurrencyCycTermHash();
    }
  }

  /**
   * @todo: this needs to be fleshed out much, much more.
   * See http://en.wikipedia.org/wiki/ISO_4217
   */
  private static void initializeCurrencyCycTermHash() {
    CURRENCY_TO_CYC_CURRENCY_MAP = new HashMap<Currency, DenotationalTerm>();
    CURRENCY_TO_CYC_CURRENCY_MAP.put(Currency.getInstance("USD"), CommonConstants.DOLLAR_UNITED_STATES);
  }
  //// Internal Rep

  private static Map<Currency, DenotationalTerm> CURRENCY_TO_CYC_CURRENCY_MAP = null;
  private static MoneyConverter SHARED_INSTANCE = null;
  //// Main
}
