/*
 * Copyright 2015 Cycorp, Inc.
 *
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
 */
package com.cyc.kb.service;

/*
 * #%L
 * File: ConvenienceServiceImpl.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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

import com.cyc.kb.spi.ConvenienceService;
import com.cyc.kb.spi.KbFactoryServices;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbStatus;
import com.cyc.kb.client.AssertionImpl;
import com.cyc.kb.client.KbObjectFactory;
import com.cyc.kb.client.KbObjectImpl;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * @author nwinant
 */
public class ConvenienceServiceImpl implements ConvenienceService<KbObject> {
  
  final private KbFactoryServices services;
  
  public ConvenienceServiceImpl(KbFactoryServices services) {
    this.services = services;
  }
  
  
  // Public
  
  @Override
  public boolean existsInKb(String nameOrId) {
    final String string = cleanString(nameOrId);
    return !(KbStatus.DOES_NOT_EXIST.equals(KbObjectFactory.getStatus(string, KbTermImpl.class))
            && KbStatus.DOES_NOT_EXIST.equals(KbObjectFactory.getStatus(string, AssertionImpl.class)));
  }
  
  @Override
  public KbObject getKbObject(String nameOrIdOrCycL) throws CreateException, KbTypeException {
    final String string = cleanString(nameOrIdOrCycL);
    if (isVariable(string)) {
      return services.getVariableService().get(string);
    }
    if (isKeyword(string)) {
      return services.getSymbolService().get(string);
    }
    if (isSentence(string)) {
      return services.getSentenceService().get(string);
    }
    try {
      return KbObjectImpl.get(string);  // TODO: should this be replaced with a call to a service?
    } catch (KbObjectNotFoundException ex) {
      throw new KbObjectNotFoundException("Could not retrieve term for " + nameOrIdOrCycL, ex);
    }
  }
  
  
  // Private
  
  private String cleanString(String inputString) {
    if (inputString == null) {
      NullPointerException npe = new NullPointerException("String cannot be null");
      throw new KbRuntimeException(npe);
    }
    final String trimmedString = inputString.trim();
    if (trimmedString.isEmpty()) {
      throw new KbRuntimeException("String cannot be empty");
    }
    return trimmedString;
  }
  
  private boolean isVariable(String nameOrId) {
    return nameOrId.startsWith("?") 
            && CycVariableImpl.isValidVariableName(nameOrId.toUpperCase());
  }
  
  private boolean isKeyword(String string) {
    return string.startsWith(":") 
            && CycSymbolImpl.isValidSymbolName(string);
  }
  
  private String getInnerStringWithoutPrefix(String string) {
    if (!string.startsWith("(")) { 
      return null;
    }
    final String inner = string.substring(1).trim();
    return (inner.startsWith("#$")) ? inner.substring(2) : inner;
  }
  
  private boolean isSentence(String string) {
    final String inner = getInnerStringWithoutPrefix(string);
    return (inner != null) && Character.isLowerCase(inner.charAt(0));
  }
  /*
  private boolean isNonAtomicTerm(String string) {
    final String inner = getInnerStringWithoutPrefix(string);
    return (inner != null) && Character.isUpperCase(inner.charAt(0));
  }
  */

  @Override
  public void clearCache() {
    KbObjectFactory.clearKBObjectCache();
  }
}
