package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: DefaultUntypedInferenceParameter.java
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

import com.cyc.base.cycobject.CycList;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.query.StandardInferenceMetric;
import java.util.HashSet;
import java.util.Map;

/**
 * <P>DefaultUntypedInferenceParameter is designed to...
 *
 * <P>Copyright (c) 2004 - 2009 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author baxter
 * @date June 19, 2009
 * @version $Id: DefaultUntypedInferenceParameter.java 163356 2016-01-04 20:55:47Z nwinant $
 */
class DefaultUntypedInferenceParameter extends AbstractInferenceParameter {

  public DefaultUntypedInferenceParameter(Map<String, Object> propertyMap) {
    super(propertyMap);
  }

  @Override
  public boolean isValidValue(Object potentialValue) {
    return true;
  }

 
  @Override
  public Object parameterValueCycListApiValue(Object val) {
    if (val instanceof HashSet) {
      CycList l = new CycArrayList();
      for (Object o : ((HashSet)val).toArray()) {
        if (o instanceof StandardInferenceMetric) {
          l.add(new CycSymbolImpl(o.toString()));
        } else
          l.add(o);
      }
      return l;
    }
    return val;
  }
}
