package com.cyc.kb.client.config;

/*
 * #%L
 * File: KbDefaultContext.java
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
import com.cyc.kb.Context;
import com.cyc.kb.DefaultContext;

public class KbDefaultContext implements DefaultContext {

  private Context assertionContext;
  private Context queryContext;

  public KbDefaultContext(Context assertionContext, Context queryContext) {
    this.assertionContext = assertionContext;
    this.queryContext = queryContext;
  }

  @Override
  public Context forAssertion() {
    if (assertionContext == null) {
      // FIXME: improve this message.
      throw new NullPointerException("ThreadLocal variable KBAPIConfiguration.getOptions().defaultContext is not set. "
              + "Set it by calling KBAPIConfiguration.getOptions().setDefaultContext(DefaultContext) atleast once in this thread.");
    }
    return assertionContext;
  }

  @Override
  public Context forQuery() {
    if (queryContext == null) {
      // FIXME: improve this message.
      throw new NullPointerException("ThreadLocal variable KBAPIConfiguration.getOptions().defaultContext is not set. "
              + "Set it by calling KBAPIConfiguration.getOptions().setDefaultContext(DefaultContext) atleast once in this thread.");
    }
    return queryContext;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + (this.assertionContext != null ? this.assertionContext.hashCode() : 0);
    hash = 79 * hash + (this.queryContext != null ? this.queryContext.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final KbDefaultContext other = (KbDefaultContext) obj;
    if (this.assertionContext != other.assertionContext && (this.assertionContext == null || !this.assertionContext.equals(other.assertionContext))) {
      return false;
    }
    if (this.queryContext != other.queryContext && (this.queryContext == null || !this.queryContext.equals(other.queryContext))) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":[" + assertionContext + ", " + queryContext + "]";
  }

}
