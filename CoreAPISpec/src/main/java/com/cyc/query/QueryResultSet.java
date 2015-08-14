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
package com.cyc.query;

/*
 * #%L
 * File: QueryResultSet.java
 * Project: Core API Specification
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

import com.cyc.kb.KBObject;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KBApiException;
import java.util.Iterator;
import java.util.List;

  /**
   * An extension of {@link KBInferenceResultSet} that also provides an
   * {@link Iterable#iterator} method for {@link QueryAnswer} objects.
   */
  public interface QueryResultSet  { //extends KBInferenceResultSet

    public Iterator<QueryAnswer> iterator();
    
    public boolean getTruthValue();

    public InferenceIdentifier getInferenceIdentifier();
  
    public Integer getCurrentRowCount();
    
    public boolean next();

  //public Object getObject(String coll) throws KBApiException;

  public <T> T getObject(Variable columnVar, Class<T> aClass);

  public <T> T getObject(String columnLabel, Class<T> aClass);

  public <T> T getObject(int columnIndex, Class<T> aClass);    
  
  //note that you'll get a ClassCastException later, because this may not actually give you back a T
  public <T> T getKBObject(Variable columnVar, Class<T> aClass) throws IllegalArgumentException, KBApiException;

  public <T> T getKBObject(String varName, Class<T> aClass) throws IllegalArgumentException, KBApiException;

  public <T> T getKBObject(int columnIndex, Class<T> aClass) throws IllegalArgumentException, KBApiException;

  public void afterLast();

  public int findColumn(Variable columnVar);

  public int findColumn(String columnLabel);

  public List<String> getColumnNames();

  public boolean isInferenceComplete();

  public void close();

  public boolean isClosed();

  public int getRow();

  public String getString(int i);

  public String getString(String varName);

  public String getString(Variable X);

  public boolean getBoolean(int columnIndex);

  public int getInt(int columnIndex);

  public int getInt(String varName);

  public long getLong(int columnIndex);

  public long getLong(String varName);

  public float getFloat(int columnIndex);

  public float getFloat(String varName);

  public double getDouble(int columnIndex);

  public double getDouble(String varName);

  public boolean getBoolean(String var);

  public Object getDate(String now);

  public void beforeFirst();

  public boolean first();

  public boolean last();

  public boolean absolute(int i);

  public boolean relative(int i);
  
  public boolean isBeforeFirst();

  public boolean isAfterLast();

  public boolean isFirst();

  public boolean isLast();
  
  public boolean previous();
}  

