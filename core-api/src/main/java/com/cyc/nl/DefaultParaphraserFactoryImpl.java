package com.cyc.nl;

/*
 * #%L
 * File: DefaultParaphraserFactoryImpl.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
//import com.cyc.nl.ParaphraseImpl;
//import com.cyc.base.CycAccess;
//import com.cyc.base.CycAccessManager;
//import com.cyc.base.cycobject.CycObject;
//import com.cyc.base.cycobject.CycVariable;
//import com.cyc.base.cycobject.DenotationalTerm;
//import com.cyc.base.exception.CycConnectionException;
//import com.cyc.base.cycobject.ElMt;
//import com.cyc.base.exception.CycApiException;
//import com.cyc.base.exception.CycTimeOutException;
//import com.cyc.base.inference.InferenceAnswer;
//import com.cyc.baseclient.CommonConstants;
//import com.cyc.baseclient.cycobject.CycVariableImpl;
//import com.cyc.baseclient.inference.DefaultInferenceSuspendReason;
//import com.cyc.baseclient.inference.DefaultInferenceWorkerSynch;
//import com.cyc.baseclient.inference.DefaultResultSet;
//import com.cyc.baseclient.inference.ResultSetInferenceAnswer;
//import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.nl.Paraphraser.ParaphrasableType;
import com.cyc.nl.spi.ParaphraserFactory;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface for generating structured paraphrases of terms.
 *
 * DefaultParaphraserFactoryImpl objects provide the ability to generate natural language strings for CycL
 objects (e.g. sentences and terms). The basic DefaultParaphraserFactoryImpl implementation available
 from baseclient will provide very basic paraphrasing functionality. If the NL API is available on
 the classpath, the <code>getParaphraser</code> methods of this class will return a paraphraser
 * that is more flexible. The <code>BasicParaphraser</code> (returned if only the baseclient is
 * available) is compatible with all versions of Cyc (including OpenCyc).
 *
 * @author baxter
 * @param <E>
 */
class DefaultParaphraserFactoryImpl<E> implements ParaphraserFactory {
  
  //====|    Fields    |==========================================================================//
  
  private static final String NL_API_IMPL_BASE_PATH = "com.cyc.nl"; // TODO: update? - nwinant, 2016-01-25
  private static final String BASIC_PARAPHRASER_CLASS = "com.cyc.nl.BasicParaphraser";
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultParaphraserFactoryImpl.class);
  
  private static final DefaultParaphraserFactoryImpl INSTANCE = new DefaultParaphraserFactoryImpl();
  
  private final Map<String, Boolean> classAvailability = new HashMap<>();
  
  //====|    Fields    |==========================================================================//
  
  DefaultParaphraserFactoryImpl() {
  }
  
  static DefaultParaphraserFactoryImpl getInstance() {
    return INSTANCE;
  }
  
  //====|    Public methods    |==================================================================//
  
  @Override
  public Paraphraser getParaphraser(final ParaphrasableType type) {
    //this should only check once to see if these other classes are available...
    //if they're not there, it needs to give back some kind of stupid paraphrase that doesn't work well...
    try {
      switch (type) {
        case QUERY:
          return getParaphraser(NL_API_IMPL_BASE_PATH + ".QueryParaphraser");
        case KBOBJECT:
          return getParaphraser(NL_API_IMPL_BASE_PATH + ".KbObjectParaphraser");
        case DEFAULT:
          return getParaphraser(NL_API_IMPL_BASE_PATH + ".DefaultParaphraser");
      }
    } catch (ClassNotFoundException
            | InstantiationException
            | IllegalAccessException ex) {
      LOGGER.error(null, ex);
    }
    throw new UnsupportedOperationException("Unable to produce a Paraphraser for " + type);
  }
  
  @Override
  public boolean isBasicParaphraser(Paraphraser paraphraser) {
    return paraphraser.getClass().getCanonicalName().equals(BASIC_PARAPHRASER_CLASS);
  }
  
  //====|    Internal methods    |================================================================//
  
  private Paraphraser getParaphraser(String binaryClassName)
          throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    if (isClassAvailable(binaryClassName)) {
      return (Paraphraser) Class.forName(binaryClassName).newInstance();
    } else {
      return (Paraphraser) Class.forName(BASIC_PARAPHRASER_CLASS).newInstance();
    }
  }
  
  private boolean isClassAvailable(String binaryClassName) {
    if (!classAvailability.containsKey(binaryClassName)) {
      try {
        Class clazz = DefaultParaphraserFactoryImpl.class.getClassLoader().loadClass(binaryClassName);
        LOGGER.debug("Loaded external paraphraser: {}", clazz);
        classAvailability.put(binaryClassName, true);
      } catch (java.lang.ClassNotFoundException ex) {
        LOGGER.warn("Could not find " + binaryClassName + ", indicating that NL API is not on the "
                + "classpath; Natural Language generation support will be limited.");
        classAvailability.put(binaryClassName, false);
      }
    }
    return classAvailability.get(binaryClassName);
  }

}
