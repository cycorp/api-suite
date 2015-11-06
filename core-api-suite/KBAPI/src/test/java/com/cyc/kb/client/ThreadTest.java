/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cyc.kb.client;

/*
 * #%L
 * File: ThreadTest.java
 * Project: KB API Implementation
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

import static com.cyc.kb.client.TestUtils.skipTest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author vijay
 */
public class ThreadTest {
  
  public ThreadTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  // Don't run this tests always!!
  //
  // TODO: should this be re-enabled? - nwinant, 2015-07-02
  // 
  @Test
  public void createKBContent() throws InterruptedException {
    skipTest(this, "createKBContent", "We don't always want to run this test.");
      //Running a single thread
    /*
    TestKBThread t = new TestKBThread("FirstThread");
    System.out.println("Start the thread.");
    t.run();
    System.out.println("We are done.");
    */
    
    // Executor
    System.out.println("Start executor.");
    ExecutorService executor = Executors.newFixedThreadPool(14);
    for (int i=0; i<14; i++) {
      TestKBThread t = new TestKBThread("Thread" + i);
      executor.submit(t);
    }
    
    executor.awaitTermination(50, TimeUnit.SECONDS);
            
    System.out.println("Executor done.");
  }
}
