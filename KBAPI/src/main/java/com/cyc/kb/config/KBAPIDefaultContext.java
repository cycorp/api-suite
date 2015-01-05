package com.cyc.kb.config;

/*
 * #%L
 * File: KBAPIDefaultContext.java
 * Project: KB API
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

import com.cyc.kb.Context;
import com.cyc.kb.client.ContextImpl;

public class KBAPIDefaultContext implements DefaultContext {
	

	private Context assertionContext;
	private Context queryContext;

	public KBAPIDefaultContext(Context assertionContext, Context queryContext) {
		this.assertionContext = assertionContext;
		this.queryContext = queryContext;
	}

	@Override
	public Context forAssertion() {
	  if (assertionContext == null){
	    throw new NullPointerException("ThreadLocal variable KBAPIConfiguration.defaultContext is not set. "
	        + "Set it by calling KBAPIConfiguration.setDefaultContext(DefaultContext) atleast once in this thread.");
	  }
		return assertionContext;
	}

	@Override
	public Context forQuery() {
	  if (queryContext == null){
      throw new NullPointerException("ThreadLocal variable KBAPIConfiguration.defaultContext is not set. "
          + "Set it by calling KBAPIConfiguration.setDefaultContext(DefaultContext) atleast once in this thread.");
    }
		return queryContext;
	}

}
