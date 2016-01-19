package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: DefaultInferenceParameterDescriptions.java
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

//// Internal Imports
import com.cyc.query.InferenceParameter;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycApiException;
import com.cyc.baseclient.CycObjectFactory;
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import com.cyc.baseclient.connection.DefaultSublWorkerSynch;
import com.cyc.baseclient.connection.SublWorkerSynch;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.Fort;
import com.cyc.query.InferenceParameters;

//// External Imports
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <P>DefaultInferenceParameterDescriptions is designed to maintain default values
 for inference parameters. For each CycClient, the static factory method

   getDefaultInferenceParameterDescriptions

 will return an instance of this class populated from the attached Cyc image
 via the SubL function GET-INFERENCE-PARAMETER-INFORMATION.

 <P>Copyright (c) 2004 - 2009 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 9, 2005, 9:30 PM
 * @version $Id: DefaultInferenceParameterDescriptions.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public class DefaultInferenceParameterDescriptions extends HashMap<String, InferenceParameter>
        implements InferenceParameterDescriptions {

  //// Public Area
  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @param key
   * @param value
   * @return the value previously associated with <tt>key</tt>, null if none.
   * @throws UnsupportedOperationException.
   */
  @Override
  public InferenceParameter put(String key, InferenceParameter value) {
    throw new UnsupportedOperationException();
  }


  /**
   *
   * @param key
   * @return the value previously associated with <tt>key</tt>, null if none.
   */
  @Override
  public InferenceParameter remove(Object key) {
    if (key instanceof CycSymbolImpl) {
      return super.remove((CycSymbolImpl) key);
    } else {
      return null;
    }
  }

  /**
   *
   * @return the CycClient
   */
  public CycAccess getCycAccess() {
    return cycAccess;
  }

public String stringApiValue() {
    return null;
  }

  /**
   *
   * @param cycAccess
   * @return an instance for CycClient
   */
  public synchronized static InferenceParameterDescriptions getDefaultInferenceParameterDescriptions(CycAccess cycAccess) {
    InferenceParameterDescriptions inferenceParameterDescriptions =
            (InferenceParameterDescriptions) defaultInferenceParameterDescriptions.get(cycAccess);
    return inferenceParameterDescriptions;
  }

  /**
   *
   * @param cycAccess
   * @param timeoutMsecs
   * @throws IOException
   * @throws CycTimeOutException
   * @throws CycApiException
   * @return an instance for CycClient
   */
  public synchronized static InferenceParameterDescriptions loadInferenceParameterDescriptions(CycAccess cycAccess, long timeoutMsecs)
          throws CycConnectionException, CycTimeOutException, CycApiException {
    InferenceParameterDescriptions inferenceParameterDescriptions = getCachedInferenceParameterDescriptions(cycAccess);
    if (inferenceParameterDescriptions != null) {
      return inferenceParameterDescriptions;
    }
    inferenceParameterDescriptions = new DefaultInferenceParameterDescriptions(cycAccess, timeoutMsecs);
    cacheInferenceParameterDescriptions(cycAccess, inferenceParameterDescriptions);
    return inferenceParameterDescriptions;
  }

  public InferenceParameters getDefaultInferenceParameters() {
    DefaultInferenceParameters parameters = new DefaultInferenceParameters(cycAccess);
    Iterator<String> iterator = keySet().iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      InferenceParameter parameter = (InferenceParameter) (get(key));
      parameters.put(key, parameter.getDefaultValue());
    }
    return parameters;
  }

  @Override
  public String toString() {
    String str = "DefaultInferenceParameterDescriptions {\n";
    Iterator<String> iterator = keySet().iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      InferenceParameter parameter = (InferenceParameter) (get(key));
      str += ("  " + parameter + "\n");
    }
    str += "}";
    return str;
  }
  
  
  // Private
  
  /** Creates a new instance of DefaultInferenceParameterDescriptions. */
  private DefaultInferenceParameterDescriptions(CycAccess cycAccess, long timeoutMsecs) throws CycConnectionException, CycTimeOutException, CycApiException {
    this.cycAccess = cycAccess;
    init(cycAccess, timeoutMsecs);
  }

  private void init(final CycAccess cycAccess, long timeoutMsecs)
          throws CycConnectionException, CycTimeOutException, CycApiException {
    if (cycAccess.isOpenCyc()) {
      //doOpenCycInit();
      doInitFromKB(cycAccess, timeoutMsecs);
    } else {
      doInitFromKB(cycAccess, timeoutMsecs);
    }
  }
  
  private void doOpenCycInit() {
    for (final OpenCycInferenceParameterEnum d : OpenCycInferenceParameterEnum.values()) {
      final InferenceParameter ip = d.getInferenceParameter();
      super.put(ip.toString(), ip);
    }
  }
  
  private void doInitFromKB(final CycAccess cycAccess, long timeoutMsecs) 
          throws CycConnectionException, CycTimeOutException, CycApiException {
    String command = "(get-inference-parameter-information)";
    SublWorkerSynch worker = new DefaultSublWorkerSynch(command, cycAccess, timeoutMsecs);
    Object work = worker.getWork();

    if (!(isPossiblyEmptyCycList(work))) {
      throw new CycApiException("When calling " + worker + "\n got unexpected result " + work);
    }

    if (work instanceof CycArrayList) {
      CycArrayList result = (CycArrayList) work;

      for (Iterator iter = result.iterator(); iter.hasNext();) {
        Object obj = iter.next();
        if (!(obj instanceof CycSymbolImpl)) {
          throw new CycApiException("When calling " + worker + "\n got unexpected result " + obj + " expected CycSymbol");
        }
        CycSymbolImpl inferenceParameterClass = (CycSymbolImpl) obj;
        if (!(iter.hasNext())) {
          throw new CycApiException("When calling " + worker + "\n got unexpected result " + obj + " not enough items");
        }
        obj = iter.next();
        if (!(isPossiblyEmptyCycList(obj))) {
          throw new CycApiException("When calling " + worker + "\n got unexpected result " + obj + " expected CycList");
        }
        if (obj instanceof CycArrayList) {
          CycArrayList inferenceParameterDescriptionForClass = (CycArrayList) obj;
          parseInferenceParameterDescriptionForClass(inferenceParameterClass, inferenceParameterDescriptionForClass);
        }
      }
    }
  }
  
  private static void cacheInferenceParameterDescriptions(CycAccess cycAccess, InferenceParameterDescriptions inferenceParameterDescriptions) {
    defaultInferenceParameterDescriptions.put(cycAccess, inferenceParameterDescriptions);
  }

  private static InferenceParameterDescriptions getCachedInferenceParameterDescriptions(CycAccess cycAccess) {
    InferenceParameterDescriptions inferenceParameterDescriptions = (InferenceParameterDescriptions) defaultInferenceParameterDescriptions.get(cycAccess);
    return inferenceParameterDescriptions;
  }
  
  private boolean isPossiblyEmptyCycList(Object obj) {
    if ((obj instanceof CycArrayList) || (obj.equals(CycObjectFactory.nil))) {
      return true;
    }
    return false;
  }

  private Map<String, Object> constructNextPropertyMap(final Iterator iter) throws CycApiException {
    Object obj = iter.next();
    if (!(obj instanceof Fort)) {
      throw new CycApiException("Expected a Cyc FORT; got " + obj);
    }
    final Fort id = (Fort) obj;
    if (!iter.hasNext()) {
      throw new CycApiException("Unexpected end of parameter description");
    }
    obj = iter.next();
    if (!(obj instanceof CycArrayList)) {
      throw new CycApiException("Expected a Cyc list; got " + obj);
    }
    final CycArrayList propertyList = (CycArrayList) obj;
    try {
      final Map<String, Object> propertyMap = parsePropertyList(propertyList);
      propertyMap.put(AbstractInferenceParameter.ID_SYMBOL, id);
      return propertyMap;
    } catch (RuntimeException xcpt) {
      throw new BaseClientRuntimeException("Cannot parse description for inference parameter " + id, xcpt);
    }
  }

  private void parseInferenceParameterDescriptionForClass(CycSymbolImpl inferenceParameterClass, CycArrayList inferenceParameterDescriptionForClass)
          throws CycApiException {
    if (inferenceParameterClass.equals(BOOLEAN_INFERENCE_PARAMETER_CLASS)) {
      parseBooleanInferenceParameterDescription(inferenceParameterDescriptionForClass);
    } else if (inferenceParameterClass.equals(INTEGER_INFERENCE_PARAMETER_CLASS)) {
      parseIntegerInferenceParameterDescription(inferenceParameterDescriptionForClass);
    } else if (inferenceParameterClass.equals(FLOATING_POINT_INFERENCE_PARAMETER_CLASS)) {
      parseFloatingPointInferenceParameterDescription(inferenceParameterDescriptionForClass);
    } else if (inferenceParameterClass.equals(ENUMERATION_INFERENCE_PARAMETER_CLASS)) {
      parseEnumerationInferenceParameterDescription(inferenceParameterDescriptionForClass);
    } else if (inferenceParameterClass.equals(OTHER_INFERENCE_PARAMETER_CLASS)) {
      parseOtherInferenceParameterDescription(inferenceParameterDescriptionForClass);
    } else {
      throw new CycApiException("Got unexpected inference parameter class " + inferenceParameterClass);
    }
  }

  private void parseBooleanInferenceParameterDescription(CycArrayList inferenceParameterDescriptionForClass)
          throws CycApiException {
    for (Iterator iter = inferenceParameterDescriptionForClass.iterator(); iter.hasNext();) {
      final Map propertyMap = constructNextPropertyMap(iter);
      try {
        super.put(propertyMap.get(AbstractInferenceParameter.NAME_SYMBOL).toString(),
                new DefaultBooleanInferenceParameter(propertyMap));
      } catch (RuntimeException xcpt) {
        final Fort id = (Fort) propertyMap.get(AbstractInferenceParameter.ID_SYMBOL);
        throw new BaseClientRuntimeException("Cannot parse inference parameter description for " + id, xcpt);
      }

    }
  }

  private void parseIntegerInferenceParameterDescription(CycArrayList inferenceParameterDescriptionForClass)
          throws CycApiException {
    for (Iterator iter = inferenceParameterDescriptionForClass.iterator(); iter.hasNext();) {
      final Map propertyMap = constructNextPropertyMap(iter);
      try {
        super.put(propertyMap.get(AbstractInferenceParameter.NAME_SYMBOL).toString(),
                new DefaultIntegerInferenceParameter(propertyMap));
      } catch (RuntimeException xcpt) {
        final Fort id = (Fort) propertyMap.get(AbstractInferenceParameter.ID_SYMBOL);
        throw new BaseClientRuntimeException("Cannot parse inference parameter description for " + id, xcpt);
      }
    }
  }

  private void parseFloatingPointInferenceParameterDescription(CycArrayList inferenceParameterDescriptionForClass)
          throws CycApiException {
    for (Iterator iter = inferenceParameterDescriptionForClass.iterator(); iter.hasNext();) {
      final Map propertyMap = constructNextPropertyMap(iter);
      try {
        super.put(propertyMap.get(AbstractInferenceParameter.NAME_SYMBOL).toString(),
                new DefaultFloatingPointInferenceParameter(propertyMap));
      } catch (RuntimeException xcpt) {
        final Fort id = (Fort) propertyMap.get(AbstractInferenceParameter.ID_SYMBOL);
        throw new BaseClientRuntimeException("Cannot parse inference parameter description for " + id, xcpt);
      }

    }
  }

  private void parseEnumerationInferenceParameterDescription(CycArrayList inferenceParameterDescriptionForClass)
          throws CycApiException {
    for (Iterator iter = inferenceParameterDescriptionForClass.iterator(); iter.hasNext();) {
      final Map propertyMap = constructNextPropertyMap(iter);
      try {
        super.put(propertyMap.get(AbstractInferenceParameter.NAME_SYMBOL).toString(),
                new DefaultEnumerationInferenceParameter(propertyMap));
      } catch (RuntimeException xcpt) {
        final Fort id = (Fort) propertyMap.get(AbstractInferenceParameter.ID_SYMBOL);
        throw new BaseClientRuntimeException("Cannot parse inference parameter description for " + id, xcpt);
      }

    }
  }

  private void parseOtherInferenceParameterDescription(final CycArrayList inferenceParameterDescriptionForClass)
          throws CycApiException {
    for (Iterator iter = inferenceParameterDescriptionForClass.iterator(); iter.hasNext();) {
      final Map propertyMap = constructNextPropertyMap(iter);
      try {
        super.put(propertyMap.get(AbstractInferenceParameter.NAME_SYMBOL).toString(),
                new DefaultUntypedInferenceParameter(propertyMap));
      } catch (RuntimeException xcpt) {
        final Fort id = (Fort) propertyMap.get(AbstractInferenceParameter.ID_SYMBOL);
        throw new BaseClientRuntimeException("Cannot parse inference parameter description for " + id, xcpt);
      }
    }
  }

  static Map<String, Object> parsePropertyList(CycList propertyList)
          throws CycApiException {
    if ((propertyList == null) || (propertyList.size() == 0)) {
      return new HashMap();
    }
    if ((propertyList.size() % 2) != 0) {
      throw new CycApiException("Expected an even number of items; got " + propertyList.size()
              + "\n Items: " + propertyList);
    }
    Map result = new HashMap<String, Object>();
    for (Iterator iter = propertyList.iterator(); iter.hasNext();) {
      Object key = iter.next();
      Object value = iter.next();
      if (value.equals(INTEGER_PLUS_INFINITY)) {
        value = MAX_LONG_VALUE;
      } else if (value.equals(REAL_PLUS_INFINITY)) {
        value = MAX_DOUBLE_VALUE;
      }
      result.put(key.toString(), value);
    }
    return result;
  }
  
  //// Internal Rep
  
  final private static Map<CycAccess, InferenceParameterDescriptions> defaultInferenceParameterDescriptions = new HashMap();
  final private CycAccess cycAccess;
  private final static CycSymbolImpl BOOLEAN_INFERENCE_PARAMETER_CLASS =
          makeCycSymbol(":BOOLEAN-INFERENCE-PARAMETERS");
  private final static CycSymbolImpl INTEGER_INFERENCE_PARAMETER_CLASS =
          makeCycSymbol(":INTEGER-INFERENCE-PARAMETERS");
  private final static CycSymbolImpl FLOATING_POINT_INFERENCE_PARAMETER_CLASS =
          makeCycSymbol(":REAL-NUMBER-INFERENCE-PARAMETERS");
  private final static CycSymbolImpl ENUMERATION_INFERENCE_PARAMETER_CLASS =
          makeCycSymbol(":ENUMERATION-INFERENCE-PARAMETERS");
  private final static CycSymbolImpl OTHER_INFERENCE_PARAMETER_CLASS =
          makeCycSymbol(":OTHER-INFERENCE-PARAMETERS");
  private final static CycSymbolImpl INTEGER_PLUS_INFINITY = makeCycSymbol(":INTEGER-PLUS-INFINITY");
  private final static CycSymbolImpl REAL_PLUS_INFINITY = makeCycSymbol(":REAL-PLUS-INFINITY");
  private final static Long MAX_LONG_VALUE = Long.MAX_VALUE;
  private final static Double MAX_DOUBLE_VALUE = Double.MAX_VALUE;

  //// Main
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {
      System.out.println("Starting...");
      CycAccess cycAccess = CycAccessManager.getCurrentAccess();
      InferenceParameterDescriptions parameters = new DefaultInferenceParameterDescriptions(cycAccess, 100000);
      System.out.println("PARAMETERS: " + parameters);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.out.println("Exiting...");
      System.exit(0);
    }
  }
}
