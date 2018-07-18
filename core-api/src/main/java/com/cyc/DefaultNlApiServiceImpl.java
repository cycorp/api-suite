package com.cyc;

/*
 * #%L
 * File: DefaultNlApiServiceImpl.java
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

import com.cyc.nl.Paraphraser;
import com.cyc.nl.Paraphraser.ParaphrasableType;
import com.cyc.nl.spi.NlApiService;
import com.cyc.nl.spi.ParaphraserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface for generating structured paraphrases of terms.
 *
 * DefaultNlApiServiceImpl objects provide the ability to generate natural language strings for CycL
 * objects (e.g. sentences and terms). The basic DefaultNlApiServiceImpl implementation available
 * from baseclient will provide very basic paraphrasing functionality. If the NL API is available on
 * the classpath, the <code>getParaphraser</code> methods of this class will return a paraphraser
 * that is more flexible. The <code>BasicParaphraser</code> (returned if only the baseclient is
 * available) is compatible with all versions of Cyc (including OpenCyc).
 *
 * @author baxter
 * @param <E>
 */
class DefaultNlApiServiceImpl<E> implements NlApiService, ParaphraserFactory {
  
  //====|    Fields    |==========================================================================//
  
  private static final String BASIC_PARAPHRASER_NAME = "BasicParaphraser";
    
  private static final String BASIC_PARAPHRASER_CLASS = "com.cyc.nl." + BASIC_PARAPHRASER_NAME;
  
  private static final Logger LOG = LoggerFactory.getLogger(DefaultNlApiServiceImpl.class);
  
  private final Paraphraser basicParaphraser;
  
  //====|    Fields    |==========================================================================//
  
  DefaultNlApiServiceImpl() {
    LOG.warn(
            "NL API is not on the classpath; Natural Language generation support will be limited.");
    Paraphraser paraphraser = null;
    try {
      paraphraser = (Paraphraser) Class.forName(BASIC_PARAPHRASER_CLASS).newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
      final String msg = "Error loading " + BASIC_PARAPHRASER_NAME + ": " + ex;
      if (LOG.isDebugEnabled()) {
        LOG.error(msg, ex);
      } else {
        LOG.error(msg);
      }
    }
    basicParaphraser = paraphraser;
  }
  
  //====|    NlApiService methods    |============================================================//
  
  @Override
  public ParaphraserFactory getParaphraserFactory() {
    return this;
  }
  
  //====|    ParaphraserFactory methods    |======================================================//
  
  @Override
  public Paraphraser getParaphraser(final ParaphrasableType type) {
    return basicParaphraser;
  }
  
  @Override
  public boolean isBasicParaphraser(Paraphraser paraphraser) {
    return paraphraser.getClass().getCanonicalName().equals(BASIC_PARAPHRASER_CLASS);
  }
  
}
