package com.cyc.kb;

/*
 * #%L
 * File: TypeFact.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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
import java.util.List;

import com.cyc.kb.exception.KBApiException;

/**
 * The interface for {@link Fact}s that are implicitly quantified over the
 * instances of a {@link KBCollection}.
 *
 * @author vijay
 */
public interface TypeFact extends Fact {

  public <O> O getTypeArgument(int getPos, Class<O> retType)
          throws KBApiException;

  public List<Object> getModifiedTypeLevelArguments();

  public void setModifiedTypeLevelArguments(
          List<Object> modifiedTypeLevelArguments);

  public void addModifiedTypeLevelArguments(Object modifiedTypeLevelArgument);

}
