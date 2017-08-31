package com.cyc.kb;

/*
 * #%L
 * File: KbFactory.java
 * Project: Core API Object Factories
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
import com.cyc.core.service.CoreServicesLoader;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.KbFactoryServices;

/**
 *
 * @author nwinant
 */
public class KbFactory {

  // Initialization
  private static final KbFactoryServices SERVICE;

  static {
    SERVICE = CoreServicesLoader.getKbFactoryServices();
  }

  private KbFactory() {
  }

  protected static KbFactoryServices getInstance() {
    return SERVICE;
  }

  // General parsing methods
  /**
   * Checks whether a given ID or Cycl term exists in the KB.
   *
   * @param nameOrId
   * @return
   */
  public static boolean existsInKb(String nameOrId) {
    return getInstance().getConvenienceService().existsInKb(nameOrId);
  }

  /**
   * Returns a Object which is the best representation for a given Object (generally a String), so
   * long as it doesn't need to be created in the KB. In general, the version of this that takes a
   * String should be used by application developers. This method will also convert a few other
   * types of objects that are generally only used inside the API, and which are not part of the
   * public API. Typically, this will return KbObjects, but it is typed to return Object, because a
   * number of other classes can be used in CycL. Particularly salient examples of this include
   * Strings and Numbers. This method is useful in cases where you don't necessarily know what kind
   * of input you will receive, such as when parsing user input. It will attempt to return a
   * KbObject according to these basic rules:
   * <ol>
   * <li>If the string is an ID, return the KbTerm, Assertion, String, or Number associated with
   * that ID.</li>
   * <li>If the string appears to be a CycL variable, return a Variable.</li>
   * <li>If the string appears to be a CycL keyword, return a Symbol.</li>
   * <li>If the string appears to be a CycL sentence, return a Sentence.</li>
   * <li>If the string appears to be a denotational term, return a KbTerm.</li>
   * <li>If the string is a quoted string, return a String.</li>
   * <li>If the string is a number, return a Number.</li>
   * </ol>
   *
   * <p>
   * This differs from {@link #getKbObject(String)} in that this allows IDs for Strings and Numbers
   * to be used, in addition to IDs for KbObjects. If you have know that your ID is for a KbObject,
   * you can use {@link #getKbObject(String)} to get a tighter return type.
   *
   * <p>
   * When returning a KbTerm or Assertion, this method will always attempt to return an instance of
   * the most specific subclass possible. For example, "#$WeatherQuantity" will be returned as an
   * instance of FirstOrderCollection.
   *
   * <p>
   * This method attempts to follow a least-common-denominator principle of least surprise, but do
   * be aware that some strings may have multiple possible representations as KbObjects. For
   * example, this method will return a Variable for "?X", but that string could also be represented
   * as a KbIndividual; if you want "?X" as a KbIndividual, you could accomplish this via a call to
   * {@link com.cyc.kb.KbTermFactory#get(String)} or
   * {@link com.cyc.kb.KbIndividualFactory#get(String)}. Similarly, almost any string can be a
   * symbol, but the most common type of symbols are <em>keywords,</em> which are prefixed with ":".
   * This method would return a symbol for the string ":SYMBOL", but if you wanted a (non-keyword)
   * symbol for the (un-prefixed) string "SYMBOL", you would need to request it from
   * {@link com.cyc.kb.SymbolFactory#get(String)}.
   *
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Object getApiObject(Object cycLOrId) throws KbTypeException, CreateException {
    return getInstance().getConvenienceService().getApiObject(cycLOrId);
  }

  /**
   * Returns a Object which is the best representation for a given String, so long as it doesn't
   * need to be created in the KB. Generally, this will return KbObjects, but it is typed to return
   * Object, because a number of other classes can be used in CycL. Particularly salient examples of
   * this include Strings and Numbers. This method is useful in cases where you don't necessarily
   * know what kind of input you will receive, such as when parsing user input. It will attempt to
   * return a KbObject according to these basic rules:
   * <ol>
   * <li>If the string is an ID, return the KbTerm, Assertion, String, or Number associated with
   * that ID.</li>
   * <li>If the string appears to be a CycL variable, return a Variable.</li>
   * <li>If the string appears to be a CycL keyword, return a Symbol.</li>
   * <li>If the string appears to be a CycL sentence, return a Sentence.</li>
   * <li>If the string appears to be a denotational term, return a KbTerm.</li>
   * <li>If the string is a quoted string, return a String.</li>
   * <li>If the string is a number, return a Number.</li>
   * </ol>
   *
   * <p>
   * This differs from {@link #getKbObject(String)} in that this allows IDs for Strings and Numbers
   * to be used, in addition to IDs for KbObjects. If you have know that your ID is for a KbObject,
   * you can use {@link #getKbObject(String)} to get a tighter return type.
   *
   * <p>
   * When returning a KbTerm or Assertion, this method will always attempt to return an instance of
   * the most specific subclass possible. For example, "#$WeatherQuantity" will be returned as an
   * instance of FirstOrderCollection.
   *
   * <p>
   * This method attempts to follow a least-common-denominator principle of least surprise, but do
   * be aware that some strings may have multiple possible representations as KbObjects. For
   * example, this method will return a Variable for "?X", but that string could also be represented
   * as a KbIndividual; if you want "?X" as a KbIndividual, you could accomplish this via a call to
   * {@link com.cyc.kb.KbTermFactory#get(String)} or
   * {@link com.cyc.kb.KbIndividualFactory#get(String)}. Similarly, almost any string can be a
   * symbol, but the most common type of symbols are <em>keywords,</em> which are prefixed with ":".
   * This method would return a symbol for the string ":SYMBOL", but if you wanted a (non-keyword)
   * symbol for the (un-prefixed) string "SYMBOL", you would need to request it from
   * {@link com.cyc.kb.SymbolFactory#get(String)}.
   *
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Object getApiObject(String cycLOrId) throws KbTypeException, CreateException {
    return getInstance().getConvenienceService().getApiObject(cycLOrId);
  }

  /**
   * Returns a Object which is the best representation for a given String, so long as it doesn't
   * need to be created in the KB. Generally, this will return KbObjects, but it is typed to return
   * Object, because a number of other classes can be used in CycL. Particularly salient examples of
   * this include Strings and Numbers. This method is useful in cases where you don't necessarily
   * know what kind of input you will receive, such as when parsing user input. It will attempt to
   * return a KbObject according to these basic rules:
   * <ol>
   * <li>If the string is an ID, return the KbTerm, Assertion, String, or Number associated with
   * that ID.</li>
   * <li>If the string is starts and ends with { and }, return a Set containing the elements between
   * the brackets.
   * <li>If the string is starts and ends with [ and ], return a List containing the elements
   * between the brackets.</li>
   * <li>If the string appears to be a CycL variable, return a Variable.</li>
   * <li>If the string appears to be a CycL keyword, return a Symbol.</li>
   * <li>If the string appears to be a CycL sentence, return a Sentence.</li>
   * <li>If the string appears to be a denotational term, return a KbTerm.</li>
   * <li>If the string is a quoted string, return a String.</li>
   * <li>If the string is a number, return a Number.</li>
   * </ol>
   *
   * <p>
   * This differs from {@link #getKbObject(String)} in that this allows IDs for Strings and Numbers
   * to be used, in addition to IDs for KbObjects. If you have know that your ID is for a KbObject,
   * you can use {@link #getKbObject(String)} to get a tighter return type.
   *
   * <p>
   * When returning a KbTerm or Assertion, this method will always attempt to return an instance of
   * the most specific subclass possible. For example, "#$WeatherQuantity" will be returned as an
   * instance of FirstOrderCollection.
   *
   * <p>
   * This method attempts to follow a least-common-denominator principle of least surprise, but do
   * be aware that some strings may have multiple possible representations as KbObjects. For
   * example, this method will return a Variable for "?X", but that string could also be represented
   * as a KbIndividual; if you want "?X" as a KbIndividual, you could accomplish this via a call to
   * {@link com.cyc.kb.KbTermFactory#get(String)} or
   * {@link com.cyc.kb.KbIndividualFactory#get(String)}. Similarly, almost any string can be a
   * symbol, but the most common type of symbols are <em>keywords,</em> which are prefixed with ":".
   * This method would return a symbol for the string ":SYMBOL", but if you wanted a (non-keyword)
   * symbol for the (un-prefixed) string "SYMBOL", you would need to request it from
   * {@link com.cyc.kb.SymbolFactory#get(String)}.
   *
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Object getApiObjectDwim(String cycLOrId) throws KbTypeException, CreateException {
    if (cycLOrId.startsWith("{") && cycLOrId.endsWith("}")) {
      cycLOrId = "(TheSet " + cycLOrId.substring(1, cycLOrId.length() - 1) + ")";
    }
    if (cycLOrId.startsWith("[") && cycLOrId.endsWith("]")) {
      cycLOrId = "(TheList " + cycLOrId.substring(1, cycLOrId.length() - 1) + ")";
    }
    return getInstance().getConvenienceService().getApiObject(cycLOrId);
  }

  /**
   * Returns a KbObject which is the best representation for a given String, so long as it doesn't
   * need to be created in the KB. This method is useful in cases where you don't necessarily know
   * what kind of input you will receive, such as when parsing user input. It will attempt to return
   * a KbObject according to these basic rules:
   * <ol>
   * <li>If the string is an ID, return the KbTerm or Assertion associated with that ID.</li>
   * <li>If the string appears to be a CycL variable, return a Variable.</li>
   * <li>If the string appears to be a CycL keyword, return a Symbol.</li>
   * <li>If the string appears to be a CycL sentence, return a Sentence.</li>
   * <li>If the string appears to be a denotational term, return a KbTerm.</li>
   * </ol>
   *
   * <p>
   * This differs from {@link #getApiObject(String)} in that this will throw an exception if given
   * something that is a valid object for the API, but it not a KbObject (e.g. strings and numbers).
   * If you have no idea what kind of object the string represents, it's genenerally safer to use
   * {@link #getApiObject(String)}.
   *
   * <p>
   * When returning a KbTerm or Assertion, this method will always attempt to return an instance of
   * the most specific subclass possible. For example, "#$WeatherQuantity" will be returned as an
   * instance of FirstOrderCollection.
   *
   * <p>
   * This method attempts to follow a least-common-denominator principle of least surprise, but do
   * be aware that some strings may have multiple possible representations as KbObjects. For
   * example, this method will return a Variable for "?X", but that string could also be represented
   * as a KbIndividual; if you want "?X" as a KbIndividual, you could accomplish this via a call to
   * {@link com.cyc.kb.KbTermFactory#get(String)} or
   * {@link com.cyc.kb.KbIndividualFactory#get(String)}. Similarly, almost any string can be a
   * symbol, but the most common type of symbols are <em>keywords,</em> which are prefixed with ":".
   * This method would return a symbol for the string ":SYMBOL", but if you wanted a (non-keyword)
   * symbol for the (un-prefixed) string "SYMBOL", you would need to request it from
   * {@link com.cyc.kb.SymbolFactory#get(String)}.
   *
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbObject getKbObject(String cycLOrId) throws KbTypeException, CreateException {
    return (KbObject) getInstance().getConvenienceService().getKbObject(cycLOrId);
  }

  // Sentences, symbols, and variables:
  /**
   * Attempts to convert a CycL string into a CycFormulaSentence and thus into a KbObject, Sentence.
   * <p>
   *
   * @param sentStr the string representing a Sentence in the KB, a CycL sentence
   * @return a Sentence object
   * @throws com.cyc.kb.exception.KbTypeException
   *
   * @throws CreateException if the Sentence represented by sentStr could not be parsed.
   */
  public static Sentence getSentence(String sentStr) throws KbTypeException, CreateException {
    return getInstance().getSentenceService().get(sentStr);
  }

  /**
   * Builds a sentence based on <code>pred</code> and other <code>args</code>. Note that
   * <code>args</code> should be KbObjects,
   * {@link java.lang.String Strings}, {@link java.lang.Number Numbers}, or
   * {@link java.util.Date Dates}. This constructor also handles {@link java.util.List Lists} and
   * {@link java.util.Set Sets} (and Lists of Lits or Sets of Lists, etc.) of those supported
   * objects.
   *
   * @param pred the first argument of the formula
   * @param args the other arguments of the formula in the order they appear in the list
   * @return a Sentence object
   *
   * @throws KbTypeException is thrown if the built cycObject is not a instance of
   * CycFormulaSentence. This should never happen.
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence getSentence(Relation pred, Object... args)
          throws KbTypeException, CreateException {
    return getInstance().getSentenceService().get(pred, args);
  }

  /**
   * Builds an arbitrary sentence based on the <code>args</code> provided. Note that
   * <code>args</code> should either be KbObjects or Java classes, String, Number or Date. This
   * constructor also handles java.util.List and java.util.Set of other supported KB API or Java
   * objects. It even supports, List of List etc.
   *
   * @param args the arguments of the formula in order
   * @return a Sentence object
   *
   * @throws KbTypeException never thrown
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence getSentence(Object... args) throws KbTypeException, CreateException {
    return getInstance().getSentenceService().get(args);
  }

  /**
   * Creates an instance of #$CycLSubLSymbol represented by symStr in the underlying KB.
   *
   * @param symStr the string representing an #$CycLSubLSymbol in the KB
   * @return the Symbol representing an #$CycLSubLSymbol in the KB
   *
   * @throws KbTypeException Symbols are created on demand and are not expected to throw any
   * exception
   */
  public static Symbol getSymbol(String symStr) throws KbTypeException {
    return getInstance().getSymbolService().get(symStr);
  }

  /**
   * Creates an instance of #$CycLVariable represented by varStr in the underlying KB.
   *
   * @param varStr the string representing an #$CycLVariable in the KB
   * @return the Variable representing an #$CycLVariable in the KB
   *
   * @throws KbTypeException if the term represented by varStr is not an instance of #$CycLVariable
   * and cannot be made into one.
   *
   * Symbols are created on demand and are not expected to throw any exception
   */
  public static Variable getVariable(String varStr) throws KbTypeException {
    return getInstance().getVariableService().get(varStr);
  }

  /**
   * Clears all caches relating <code>KbObject</code>s to objects on the Cyc server. In most
   * applications, this will not be needed. However, it can be helpful in applications where the KB
   * is modified externally. For example, if a Cyc term is deleted and then recreated with the same
   * name by some external process, the KB API will still have the id information from the old
   * constant, and will retrieve the new constant. A call to <code>clearCache</code> will clear the
   * cache and allow the KB API to successfully retrieve the newly created constant.
   */
  public static void clearCache() {
    getInstance().getConvenienceService().clearCache();
  }

}
