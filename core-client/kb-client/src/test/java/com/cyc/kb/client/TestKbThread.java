/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cyc.kb.client;

/*
 * #%L
 * File: TestKbThread.java
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

import com.cyc.kb.exception.KbException;


/**
 *
 * @author vijay
 */
public class TestKbThread implements Runnable {

  private String threadName;
  
  public TestKbThread(String name) {
    this.threadName = name;
  }
  
  @Override
  public void run() {
    for (int i=0; i<100; i++) {
      String testConsant = "ThreadTestConstant_" + threadName + "_" + i;
      try {
        KbIndividualImpl.findOrCreate(testConsant);
        System.out.println("Thread: " + this.threadName + " created: " + testConsant);
      } catch (KbException ex) {
        System.out.println("Something went wrong: " + ex.getMessage());
      } 
    }
    
  }
}
