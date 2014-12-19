package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: Money.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

//// Internal Imports

//// External Imports
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.util.Currency;


/**
 * <P>Money is a Java representation of monetary amounts.
 *
 * @todo Add String-parsing constructor (so you can instantiate from strings like "$53.60")
 * @todo Expose BigDecimal arithmetic methods (add, subtract, divide, etc.)
 * @todo Add currency-appropriate rounding
 * @todo Improve serialization
 *
 * @author nwinant, May 24, 2010, 4:30:39 PM
 * @version $Id: Money.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public class Money implements Serializable, Comparable<Money> {

  //// Constructors

  /** Creates a new instance of Money. */
  public Money(BigDecimal quantity, Currency currency) {
    this.quantity = quantity;
    this.currency = currency;
  }

  /** Creates a new instance of Money, with the default currency. */
  public Money(BigDecimal quantity) {
    this(quantity, DEFAULT_CURRENCY);
  }


  //// Public Area
  
  /**
   * @return the quantity
   */
  public BigDecimal getQuantity() {
    return quantity;
  }

  /**
   * @return the currency
   */
  public Currency getCurrency() {
    return currency;
  }

  public boolean isSameCurrencyAs(Money m) {
    return getCurrency().equals(m.getCurrency());
  }
  
  public int compareTo(Money o) {
    if ((o == null) || (!isSameCurrencyAs(o)))
      throw new ClassCastException();
    return getQuantity().compareTo(o.getQuantity());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Money))
      return false;
    
    final Money m = (Money) obj;
    if (!isSameCurrencyAs(m))
      return false;

    return getQuantity().equals(m.getQuantity());
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + (this.quantity != null ? this.quantity.hashCode() : 0);
    hash = 71 * hash + (this.currency != null ? this.currency.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    return getQuantity() + " " + getCurrency().getSymbol();
  }

  
  //// Protected Area


  //// Private Area


  //// Internal Rep

  public static final Currency DEFAULT_CURRENCY = Currency.getInstance("USD");
  private final BigDecimal quantity;
  private final Currency currency;

  
  //// Main

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    final Logger logger = Logger.getLogger(Money.class.toString());
    logger.info("Starting");
    Money thisObj = null;
    try {
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      if (thisObj != null) {
        // Clean up resource if necessary. E.g., call close() or flush().
      }
      logger.info("Finished.");
      System.exit(0);
    }
  }

}

