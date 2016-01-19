/*
 * Copyright 2016 Cycorp, Inc..
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
package com.cyc.base.kbtool;

/*
 * #%L
 * File: KbObjectTool.java
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

import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.kb.Context;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbTerm;

/**
 * Provides tools for converting KbObjects to Base Client objects.
 * 
 * @author nwinant
 */
public interface KbObjectTool {
  
  CycConstant convertToConstant(KbTerm term);
  
  CycList convertToCycList(KbObject kbObject);
  
  CycObject convertToCycObject(KbObject kbObject);
  
  DenotationalTerm convertToDenotationalTerm(KbTerm term);
  
  ElMt convertToElMt(Context context);
  
  Fort convertToFort(KbTerm term);
  
  Nart convertToNart(KbTerm term);
  
  Naut convertToNaut(KbTerm term);
  
}
