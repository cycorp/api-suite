package com.cyc.baseclient.subl;

/*
 * #%L
 * File: SublResourceLoaderTest.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.baseclient.subl.functions.SublFunctions;
import static com.cyc.baseclient.subl.functions.SublFunctions.CATEGORIZE_TERM_WRT_API;
import com.cyc.baseclient.testing.TestUtils;
import com.cyc.session.exception.SessionException;
import java.io.File;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class SublResourceLoaderTest {
  
  private CycAccess access;
  private SublResourceLoader loader;
  
  @Before
  public void setUp() throws SessionException {
    access = CycAccessManager.getCurrentAccess();
    loader = new SublResourceLoader(access);
  }
  
  
  // Tests
  
  @Test
  public void testGetSourceFilePath() throws Exception {
    String path = CATEGORIZE_TERM_WRT_API.getSourceFilePath();
    System.out.println("getSourceFilePath: " + path);
    assertEquals("subl" + File.separatorChar + "api-patches" + File.separatorChar + "categorize-term-wrt-api.lisp", path);
  }
  
  @Test
  public void testReadSource() throws Exception {
    String source = SublResourceLoader.readSourceFile(CATEGORIZE_TERM_WRT_API);
    System.out.println("loadSource: ");
    /*
    System.out.println("--");
    System.out.println(source);
    System.out.println("--");
    */
    assertNotNull(source);
    assertTrue(source.startsWith(";;"));
  }
  
  @Test
  public void TestFindMissingResources() throws Exception {
    assumeTestLoadingAllowed();
    List<SublSourceFile> missing = loader.findMissingResources(SublFunctions.SOURCES);
    System.out.println("Missing resources: " + missing.size());
    for (SublSourceFile resource : missing) {
      System.out.println(" - " + resource);
    }
    assertTrue(missing.isEmpty());
  }
    
  @Test
  public void testLoadResource() throws Exception {
    assumeTestLoadingAllowed();
    assertTrue(CATEGORIZE_TERM_WRT_API.isBound(access));
    loader.loadResource(CATEGORIZE_TERM_WRT_API);
    assertTrue(CATEGORIZE_TERM_WRT_API.isBound(access));
  }
  
  @Test
  public void testLoadMissingResources() throws Exception {
    assumeTestLoadingAllowed();
    List<SublSourceFile> missing = loader.loadMissingResources(SublFunctions.SOURCES);
    System.out.println("Missing resources: " + missing.size());
    for (SublSourceFile resource : missing) {
      System.out.println(" - " + resource);
    }
    assertTrue(missing.isEmpty());
  }
  
  
  // Private
  
  /**
   * Only allow tests to load SubL if there's nothing missing. (I.e., don't allow these tests to
   * potentially mess up other tests.)
   */
  private void assumeTestLoadingAllowed() throws CycApiException, CycConnectionException {
    org.junit.Assume.assumeTrue(loader.findMissingResources(SublFunctions.SOURCES).isEmpty());
    TestUtils.assumeNotObfuscated();
  }
}
