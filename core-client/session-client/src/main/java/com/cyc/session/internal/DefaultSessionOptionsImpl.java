/*
 * Copyright 2015 Cycorp, Inc..
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

package com.cyc.session.internal;

/*
 * #%L
 * File: DefaultSessionOptionsImpl.java
 * Project: Session Client
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

import com.cyc.kb.DefaultContext;
import com.cyc.session.CycSessionConfiguration.DefaultSessionOptions;


/**
 * Provides an immutable set of session options to server as defaults for 
 * {@link com.cyc.session.CycSession#getOptions() }.
 * 
 * @author nwinant
 */
public class DefaultSessionOptionsImpl implements DefaultSessionOptions {
  
  // Fields
  
  final private String cyclist;
  final private String project;
  final private DefaultContext defaultContext;
  final private Boolean shouldTranscriptOperations;
  
  
  // Construction
  
  protected DefaultSessionOptionsImpl() {
    // TODO: these need to be derived from properties.
    this.cyclist = null;
    this.project = null;
    this.shouldTranscriptOperations = true;
    
    // TODO: DefaultContext (or the contexts it contains) should be retrieved via a KB API factory.
    this.defaultContext = null;
    /*
    this.defaultContext = new DefaultContext() {
      private void throwException() {
        throw new NullPointerException("A DefaultContext is not set for this session."
                + " Set it by calling CycSession#getOptions()#setDefaultContext(DefaultContext)"
                + " on the current session.");
      }
      @Override
      public Context forAssertion() {
        throwException();
        return null;
      }
      @Override
      public Context forQuery() {
        throwException();
        return null;
      }
    };
    */
  }
  
  
  // Public
  
  @Override
  public String getCyclistName() {
    return this.cyclist;
  }

  @Override
  public String getKePurposeName() {
    return this.project;
  }

  @Override
  public boolean getShouldTranscriptOperations() {
    return this.shouldTranscriptOperations;
  }

  @Override
  public DefaultContext getDefaultContext() {
    return this.defaultContext;
  }
  
}
