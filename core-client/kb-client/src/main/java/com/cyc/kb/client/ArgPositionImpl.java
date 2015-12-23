package com.cyc.kb.client;

import com.cyc.kb.ArgPosition;

/*
 * #%L
 * File: ArgPositionImpl.java
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

/**
 *
 * @author baxter
 */
public class ArgPositionImpl extends com.cyc.baseclient.cycobject.ArgPositionImpl implements com.cyc.kb.ArgPosition {

  ArgPositionImpl(ArgPosition pos) {
    super(pos.getPath());
  }

  @Override
  public ArgPosition extend(ArgPosition otherArgPos) {
    return (ArgPosition)super.extend((ArgPositionImpl) otherArgPos);
  }

  @Override
  public ArgPosition toParent() {
    return super.toParent();
  }

}
