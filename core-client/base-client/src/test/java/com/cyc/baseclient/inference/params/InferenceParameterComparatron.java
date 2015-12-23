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

package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: InferenceParameterComparatron.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.baseclient.connection.DefaultSublWorkerSynch;
import com.cyc.baseclient.connection.SublWorkerSynch;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.session.CycServer;
import com.cyc.session.exception.SessionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * This will load the inference parameters from both an OpenCyc server and a Research Cyc server,
 * compare them, and report any differences between them.
 * 
 * @author nwinant
 */
public class InferenceParameterComparatron {
  
  public static class ParamMap {
    final private Multimap<CycConstant,CycList> params = ArrayListMultimap.<CycConstant,CycList>create();
    final private Multimap<CycSymbol,CycConstant> paramClasses = ArrayListMultimap.<CycSymbol,CycConstant>create();
    
    public CycSymbol getParamClass(CycConstant paramId) {
      for (CycSymbol key : paramClasses.keySet()) {
        if (paramClasses.get(key).contains(paramId)) {
          return key;
        }
      }
      return null;
    }
    
    public Set<CycConstant> getParamIds() {
      return params.keySet();
    }
    
    public Collection<CycList> getFields(CycConstant paramId) {
      return params.get(paramId);
    }
    
    public void put(CycSymbol inferenceParameterClass, CycConstant paramId, CycSymbol fieldId, Object value) {
      final CycList valueList = CycArrayList.makeCycList(
              fieldId,
              value);
      params.put(paramId, valueList);
      paramClasses.put(inferenceParameterClass, paramId);
    }
    
    private Map<CycSymbol, Object> getValuesMap(CycConstant paramId) {
      final Map<CycSymbol, Object> result = new HashMap<CycSymbol, Object>();
      for (CycList list : params.get(paramId)) {
        result.put(
                (CycSymbol) list.get(0), 
                list.get(1));
      }
      return result;
    }
    
    public void clear() {
      params.clear();
      paramClasses.clear();
    }
  }
  
  public static class Success {
    final CycSymbol field;
    final Object expected;
    final Object actual;
    
    public Success(CycSymbol field, Object expected, Object actual) {
      this.field = field;
      this.expected = expected;
      this.actual = actual;
    }
    
    public void print(PrintStream out) {
      out.println("  " + field);
      out.println("    Expected: " + expected);
      out.println("    Actual:   " + actual);
    }
  }
  
  public static class Error {
    final String msg;
    final CycSymbol field;
    final Object expected;
    final Object actual;
    
    public Error(String msg, CycSymbol field, Object expected, Object actual) {
      this.msg = msg;
      this.field = field;
      this.expected = expected;
      this.actual = actual;
    }
    
    public Error(String msg) {
      this(msg, null, null, null);
    }
    
    public void print(PrintStream out) {
      if (field != null) {
        out.println("  " + field + ": " + msg);
        out.println("    Expected: " + expected);
        out.println("    Actual:   " + actual);
      } else {
        out.println("  " + msg);
      }
    }
  }
  
  final private CycAccess rcyc;
  final private CycAccess ocyc;
  final private long timeoutMsecs;
  final private ParamMap rcycParams = new ParamMap();
  final private ParamMap ocycParams = new ParamMap();
  final private Multimap<CycConstant,Success> successes = ArrayListMultimap.<CycConstant,Success>create();
  final private Multimap<CycConstant,Error> errors = ArrayListMultimap.<CycConstant,Error>create();

  
  // Constructors
  
  public InferenceParameterComparatron(final CycServer rcycAddress, final CycServer ocycAddress, long timeoutMsecs) throws SessionException {
    this.rcyc = CycAccessManager.get().getAccess(rcycAddress);
    this.ocyc = CycAccessManager.get().getAccess(ocycAddress);
    this.timeoutMsecs = timeoutMsecs;
  }
  
  public InferenceParameterComparatron(final String rcycAddress, final String ocycAddress, long timeoutMsecs) throws SessionException {
    this(CycServer.fromString(rcycAddress), CycServer.fromString(ocycAddress), timeoutMsecs);
  }
  
  
  // Public
    
  public void load() throws CycConnectionException, CycTimeOutException, CycApiException {
    rcycParams.clear();
    ocycParams.clear();
    lookupParamsForCyc(rcyc, rcycParams);
    lookupParamsForCyc(ocyc, ocycParams);
  }
  
  public ParamMap getResearchCycParams() {
    return this.rcycParams;
  }
  
  public ParamMap getOpenCycParams() {
    return this.ocycParams;
  }
  
  public Multimap<CycConstant,Error> getErrors() {
    return this.errors;
  }
  
  public Multimap<CycConstant,Success> getSuccesses() {
    return this.successes;
  }
  
  public void print() {
    System.out.println("Research Cyc  ================================================");
    printMap(rcycParams);
    System.out.println();
    System.out.println();
    
    System.out.println("Open Cyc  ====================================================");
    printMap(ocycParams);
    System.out.println();
    System.out.println();
  }
  
  public void compare() throws SessionException {
    System.out.println("Comparing............... ");
    successes.clear();
    errors.clear();
    //for (CycConstant rcycParamId : rcycParams.params.keySet()) {
    for (CycConstant rcycParamId : rcycParams.getParamIds()) {
      final Map<CycSymbol, Object> rcycValues = rcycParams.getValuesMap(rcycParamId);
      final Map<CycSymbol, Object> ocycValues = ocycParams.getValuesMap(rcycParamId);
      //if (ocycParams.params.get(rcycParamId).isEmpty()) {
      if (ocycParams.getFields(rcycParamId).isEmpty()) {
        recordError(rcycParamId, "Parameter is empty in OpenCyc");
      } else {
        for (CycSymbol rcycValueKey : rcycValues.keySet()) {
          final Object rcycValue = rcycValues.get(rcycValueKey);
          final Object ocycValue = ocycValues.get(rcycValueKey);
          if (!ocycValues.containsKey(rcycValueKey)) {
            recordError(
                    rcycParamId, 
                    "Missing from OCyc",
                    rcycValueKey,  
                    rcycValue,
                    null);
          } else if (!rcycValue.equals(ocycValue)) {
            recordError(
                    rcycParamId, 
                    "Wrong value in OCyc",
                    rcycValueKey,  
                    rcycValue,
                    ocycValue);
          } else {
            recordSuccess(
                    rcycParamId,
                    rcycValueKey,
                    rcycValue,
                    ocycValue);
          }
        }
      }
    }
    printResults();
  }
  
  public int printErrors() {
    int totalErrs = 0;
    System.out.println("Errors:");
    System.out.println();
    for (CycConstant paramId : getErrors().keySet()) {
      System.out.println(paramId + " (" + this.rcycParams.getParamClass(paramId) + ")");
      for (Error err : getErrors().get(paramId)) {
        err.print(System.out);
        totalErrs++;
      }
      System.out.println();
    }
    System.out.println();
    System.out.println("Total ERRORS: " + totalErrs);
    return totalErrs;
  }
  
  public int printSuccesses() {
    int totalSuccesses = 0;
    System.out.println("Successes:"); 
    System.out.println();
    for (CycConstant paramId : getSuccesses().keySet()) {
      System.out.println(paramId + " (" + this.rcycParams.getParamClass(paramId) + ")");
      for (Success success : getSuccesses().get(paramId)) {
        success.print(System.out);
        totalSuccesses++;
      }
      System.out.println();
    }
    
    System.out.println();
    System.out.println("Total SUCCESSES: " + totalSuccesses);
    return totalSuccesses;
  }
  
  public void printResults() throws SessionException {
    System.out.println("RESULTS  =====================================================================");
    
    System.out.println("Report run on " + new Date());
    System.out.println();
    System.out.println("ResearchCyc : " + this.rcyc.getServerInfo().getCycServer());
    System.out.println("  KB Version: " + this.rcyc.getServerInfo().getCycKbVersionString());
    System.out.println("  Revision  : " + this.rcyc.getServerInfo().getCycRevisionString());
    System.out.println();
    System.out.println("OpenCyc     : " + this.ocyc.getServerInfo().getCycServer());
    System.out.println("  KB Version: " + this.ocyc.getServerInfo().getCycKbVersionString());
    System.out.println("  Revision  : " + this.ocyc.getServerInfo().getCycRevisionString());
    System.out.println();
    
    //int totalSuccesses = printSuccesses();
    int totalErrs = printErrors();
    
    System.out.println();
    System.out.println("-------------------------------");
    System.out.println();
    System.out.println();
    
    //int totalErrs = printErrors();
    int totalSuccesses = printSuccesses();
    
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("Errors: " + totalErrs);
    System.out.println("Successes: " + totalSuccesses);
    System.out.println("=================================================================  END RESULTS");
  }
  
  
  // Private
  
  private void recordSuccess(CycConstant paramId, CycSymbol field, Object expected, Object actual) {
    successes.put(paramId, new Success(field, expected, actual));
  }
  
  private void recordError(CycConstant paramId, String msg) {
    errors.put(paramId, new Error(msg));
  }
  
  private void recordError(CycConstant paramId, String msg, CycSymbol field, Object expected, Object actual) {
    errors.put(paramId, new Error(msg, field, expected, actual));
  }
  
  private void printMap(ParamMap map) {
    //final List<CycConstant> keys = new ArrayList(map.params.keySet());
    final List<CycConstant> keys = new ArrayList(map.getParamIds());
    for (CycConstant key : keys) {
      //final Collection<CycList> fields = map.params.get(key);
      final Collection<CycList> fields = map.getFields(key);
      System.out.println(map.getParamClass(key) + "  " + key);
      for (CycList field : fields) {
        System.out.println("  " + field);
      }
      System.out.println();
    }
  }
  
  private void processParam(CycSymbol inferenceParameterClass, CycConstant paramId, CycList param, ParamMap map) {
    for (Iterator iter = param.iterator(); iter.hasNext();) {
      CycSymbol fieldId = (CycSymbol) iter.next();
      Object value = iter.next();
      map.put(inferenceParameterClass, paramId, fieldId, value);
    }
  }
  
  private void processParams(CycSymbol inferenceParameterClass, CycList params, ParamMap map) {
    for (Iterator iter = params.iterator(); iter.hasNext();) {
      CycConstant id = (CycConstant) iter.next();
      CycList obj = (CycList) iter.next();
      processParam(inferenceParameterClass, id, obj, map);
    }
  }
  
  private void lookupParamsForCyc(CycAccess access, ParamMap map) 
          throws CycConnectionException, CycTimeOutException, CycApiException {
    String command = "(get-inference-parameter-information)";
    SublWorkerSynch worker = new DefaultSublWorkerSynch(command, access, timeoutMsecs);
    Object work = worker.getWork();
    CycArrayList result = (CycArrayList) work;
    for (Iterator iter = result.iterator(); iter.hasNext();) {
      CycSymbol inferenceParameterClass = (CycSymbol) iter.next();
      Object obj2 = iter.next();
      processParams(inferenceParameterClass, (CycList) obj2, map);
    }
  }
  
  
  // Main
  
  // NOTE: this may be run from the test class, 
  
  public static void main(String[] args) {
    try {
      final String rcyc = "localhost:3620";
      final String ocyc = "localhost:3600";
      
      System.out.println("Here we go...");
      System.out.println();
      final InferenceParameterComparatron inferenceParamComparatron = new InferenceParameterComparatron(rcyc, ocyc, 10000);
      inferenceParamComparatron.load();
//      inferenceParamComparatron.print();
      inferenceParamComparatron.compare();
      System.out.println("... Done!");
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    } finally {
      System.exit(0);
    }
  }
}
