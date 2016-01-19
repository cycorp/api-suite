package com.cyc.kb.client;

/*
 * #%L
 * File: KbUtils.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.Fort;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbObject;
import static com.cyc.kb.client.KbObjectImpl.getStaticAccess;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of utilities related to KB API.
 *
 * @author jmoszko
 *
 */
// TODO: Split this into KBTermUtils, KBCollectionUtils etc.
// KBTermUtils will have: isTermNameAvailable
// KBCollectionUtils will have: minCols, maxCols etc..
public class KbUtils {

  private static final Logger log = LoggerFactory.getLogger(KbObjectImpl.class.getCanonicalName());

  /**
   * This method takes a Map<KBObject, Object> instance and converts it to a
   * Map<CycObject, Object> instance.
   *
   * @param mapToConvert
   * @return kboToCoMap
   */
  public static Map<CycObject, Object> convertKBObjectMapToCoObjectMap(Map<KbObject, Object> mapToConvert) {
    Map<CycObject, Object> kboToCoMap = new HashMap<CycObject, Object>();
    for (Map.Entry<KbObject, Object> i : mapToConvert.entrySet()) {
      KbObject key = i.getKey();
      Object val = i.getValue();
      Object typedVal = null;
      if (val instanceof KbObject) {
        typedVal = ((KbObject) val).getCore();
      } else {
        typedVal = val;
      }
      kboToCoMap.put(KbObjectImpl.getCore(key), typedVal);
    }
    return kboToCoMap;
  }

  private static CycList listKBObjectToCycList (Collection<? extends KbObject> cols) {
    CycList l = new CycArrayList();
    for (KbObject col : cols) {
      l.add(col.getCore());
    };
    return l;
  }


  private static <O extends KbObject> Collection<O> cycListToKBObjectList (CycList cl) {
    List<O> kboList = new ArrayList<O>();
    for (Object o : cl) {
      try {
        kboList.add(KbObjectImpl.<O>checkAndCastObject(o));
      } catch (Exception e) {
        // ignore
      }
    }
    return kboList;
  }

  /**
   * This method wraps {@link KBObjectFactory#get(java.lang.String, java.lang.Class) } and throws
   * {@link IllegalArgumentException} upon any {@link KbException} or {@link ClassCastException}.
   * 
   * @param <O>     The type of KBObject to be built
   * @param kboStr  The input string turned to a KBObject
   * @param retType Cast type of the returned object, same as <O>
   * 
   * @return find a KBObject and throw IllegalArgumentException if one is not found
   */
  public static <O extends KbObjectImpl> O getKBObjectForArgument(String kboStr, Class<O> retType) {
    try {
      return KbObjectFactory.get(kboStr, retType);
    } catch (KbException kae) {
      throw new IllegalArgumentException(kae.getMessage(), kae);
    } catch (ClassCastException cce) {
      throw new IllegalArgumentException(cce.getMessage(), cce);
    }
  }
  
  /**
   * Return a single minimally-general (most specific) collection among the input collections, 
   * <code>cols</code> in context <code>ctx</code>. A minimally-general collection among 
   * the input collection, is
   * one that does not have a proper specialization among the input collections. 
   * Ties are broken by the count of {@link KBCollection#allGeneralizations()}
   * results, which is a rough estimate of the depth of the collection in the 
   * collection hierarchy. 
   * 
   * This method wraps CreateException and KBTypeException, as a KBApiRuntimeException,
   * these ideally should never be thrown because minCol is guaranteed to return 
   * one of the input KBCollections back. The method also wraps CycApiException and
   * CycConnectionException, which are generally wrapped by KBApiRuntimeException.
   * 
   * @param cols  the input list of collections, among which, the most specific is found
   * @param ctx   the context of query
   * 
   * @return the minimally-general collection
   */
  public static KbCollection minCol(Collection<KbCollection> cols, Context ctx) {
    try {
      CycList l = KbUtils.listKBObjectToCycList(cols);
      Fort cycCol = getStaticAccess().getLookupTool().getMinCol(l, ContextImpl.asELMt(ctx));
      return KbCollectionImpl.get(cycCol);
    } catch (Exception e) {
      // This is truely exceptional. The input collections will always result
      // in a single collection being returned.
      log.error(e.getMessage());
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }
  
  /**
   * Return a single minimally-general (most specific) collection among the input collections, 
   * <code>cols</code>, without considering context, that is, in all contexts.
   * 
   * @see KBUtils#minCol(java.util.Collection, com.cyc.kb.Context) for a more detailed 
   * explanation.
   * 
   * @param cols the input list of collections, among which, the most specific is found
   * @return the minimally-general collection
   */
  public static KbCollection minCol(Collection<KbCollection> cols) {
    try {
      CycList l = KbUtils.listKBObjectToCycList(cols);
      Fort cycCol = getStaticAccess().getLookupTool().getMinCol(l);
      return KbCollectionImpl.get(cycCol);
    } catch (Exception e) {
      // This is truely exceptional. The input collections will always result
      // in a single collection being returned.
      log.error(e.getMessage());
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }
  
  /**
   * Return all the minimally-general (most specific) collections among the input collections, 
   * <code>cols</code> in the context <code>ctx</code>. A minimally-general collection among the input collection, is
   * one that does not have a proper specialization among the input collections.
   * 
   * This method wraps CreateException and KBTypeException, as a KBApiRuntimeException,
   * these ideally should never be thrown because minCol is guaranteed to return 
   * one of the input KBCollections back. The method also wraps CycApiException and
   * CycConnectionException, which are generally wrapped by KBApiRuntimeException.
   * 
   * @param cols  the input list of collections, among which, the most specific are found
   * @param ctx   the context of query
   * 
   * @return all of the minimally-general collections
   */
  public static Collection<KbCollection> minCols(Collection<KbCollection> cols, Context ctx) {
    try {
      CycList l = KbUtils.listKBObjectToCycList(cols);
      CycList cycCols = getStaticAccess().getLookupTool().getMinCols(l,  ContextImpl.asELMt(ctx));
      return KbUtils.<KbCollection>cycListToKBObjectList(cycCols);
    } catch (Exception e) {
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }
  
  /**
   * Return all the minimally-general (most specific) collections among the input collections, 
   * <code>cols</code>, without considering context, that is, in all contexts.
   * 
   * @see KBUtils#minCols(java.util.Collection, com.cyc.kb.Context) for a more detailed
   * explanation.
   * 
   * @param cols  the input list of collections, among which, the most specific are found
   * @return all of the minimally-general collections
   */
  public static Collection<KbCollection> minCols(Collection<KbCollection> cols) {
    try {
      CycList l = KbUtils.listKBObjectToCycList(cols);
      CycList cycCols = getStaticAccess().getLookupTool().getMinCols(l);
      return KbUtils.<KbCollection>cycListToKBObjectList(cycCols);
    } catch (Exception e) {
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }
  
  /**
   * Return the most general collections among the input collections, 
   * <code>cols</code> in the context <code>ctx</code>. A most general collection 
   * among the input collection, is one that does not have a proper generalization 
   * among the input collections.
   * 
   * This method wraps CreateException and KBTypeException, as a KBApiRuntimeException,
   * these ideally should never be thrown because minCol is guaranteed to return 
   * one of the input KBCollections back. The method also wraps CycApiException and
   * CycConnectionException, which are generally wrapped by KBApiRuntimeException.
   * 
   * @param cols  the input list of collections, among which, the most general are found
   * @param ctx   the context of query
   * 
   * @return all of the most general collections
   */
  public static Collection<KbCollection> maxCols(Collection<KbCollection> cols, Context ctx) {
    try {
      CycList l = KbUtils.listKBObjectToCycList(cols);
      CycList cycCols = getStaticAccess().getLookupTool().getMaxCols(l,  ContextImpl.asELMt(ctx));
      return KbUtils.<KbCollection>cycListToKBObjectList(cycCols);
    } catch (Exception e) {
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }
  
  /**
   * Return the most general collections among the input collections, 
   * <code>cols</code>, without considering context, that is, in all contexts.
   * 
   * @see KBUtils#maxCols(java.util.Collection, com.cyc.kb.Context) for a more detailed 
   * explanation.
   * 
   * @param cols  the input list of collections, among which, the most general are found
   * 
   * @return all of the most general collections
   */
  public static Collection<KbCollection> maxCols(Collection<KbCollection> cols) {
    try {
      CycList l = KbUtils.listKBObjectToCycList(cols);
      CycList cycCols = getStaticAccess().getLookupTool().getMaxCols(l);
      return KbUtils.<KbCollection>cycListToKBObjectList(cycCols);
    } catch (Exception e) {
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }
    
  /**
   * Determine whether the given string <code>termName</code> is or is not 
   * already the name of a constant in the KB.
   * 
   * @param termName  the string which may be a constant name
   * 
   * @return true if the termName string is not already a constant name
   */
  public static boolean isTermNameAvailable(String termName) {
    try {
      boolean available = getStaticAccess().getInspectorTool().isConstantNameAvailable(termName);
      log.debug("Term name: " + termName + " is available.");
      return available;
    } catch (Exception e) {
      // Don't want to return true, we actually couldn't find out for exceptional reasons
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }
}
