package com.cyc.baseclient;

/*
 * #%L
 * File: CycServerInfoImplTest.java
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
import com.cyc.base.exception.CycConnectionException;
import com.cyc.session.CycServerReleaseType;
import static com.cyc.session.CycServerReleaseType.ENTERPRISECYC;
import static com.cyc.session.CycServerReleaseType.MAINT;
import static com.cyc.session.CycServerReleaseType.OPENCYC;
import static com.cyc.session.CycServerReleaseType.OTHER;
import static com.cyc.session.CycServerReleaseType.RESEARCHCYC;
import com.cyc.session.exception.SessionException;
import java.net.MalformedURLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;


/**
 *
 * @author nwinant
 */
public class CycServerInfoImplTest {
  
  private CycServerInfoImpl serverInfo;
  private CycAccess access;
  
  @BeforeClass
  public static void setUpClass() throws MalformedURLException, Exception {
    //TestUtils.ensureTestEnvironmentInitialized();
  }
  
  @Before
  public void setUp() throws SessionException {
    serverInfo = CycClientManager.getCurrentClient().getServerInfo();
    access = CycClientManager.getCurrentAccess();
  }
  
  
  // Tests
  
  @Test
  public void testIsAPICompatible() {
    boolean errorFree = false;
    try {
      System.out.println("CycRevisionString:      " + serverInfo.getCycRevisionString());
      System.out.println("CycMajorRevisionNumber: " + serverInfo.getCycMajorRevisionNumber());
      System.out.println("CycMinorRevisionNumber: " + serverInfo.getCycMinorRevisionNumber());
      System.out.println("CycKBVersionString:     " + serverInfo.getCycKbVersionString());
      System.out.println("CycKBVersionString:     " + serverInfo.getCycKbVersionString());
      assertTrue(serverInfo.isApiCompatible());
      errorFree = true;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    assertTrue(errorFree);
  }
  
  @Test
  public void testIsOpenCyc() throws SessionException, CycConnectionException {
    assertEquals(access.isOpenCyc(), serverInfo.isOpenCyc());
  }
  
  @Test
  public void testGetSystemTypeString() throws SessionException, CycConnectionException {
    final String type = serverInfo.getSystemReleaseTypeString();
    if (access.isOpenCyc()) {
      assertEquals("OpenCyc", type);
    } else {
      assertNotEquals("OpenCyc", type);
    }
  }
  
  @Test
  public void getSystemType() throws SessionException, CycConnectionException {
    final String typeString = serverInfo.getSystemReleaseTypeString();
    final CycServerReleaseType type = serverInfo.getSystemReleaseType();
    System.out.println(typeString + " -> " + type);
    
    if ("OpenCyc".equals(typeString)) {
      assertEquals(typeString, type.getName());
      assertEquals(OPENCYC, type);
      
    } else if ("ResearchCyc".equals(typeString)) {
      assertEquals(typeString, type.getName());
      assertEquals(RESEARCHCYC, type);
      
    } else if ("ECyc".equals(typeString)) {
      assertEquals(typeString, type.getAbbreviation());
      assertEquals(ENTERPRISECYC, type);
      
    } else if ("Cyc-Maint".equals(typeString)) {
      assertEquals(typeString, type.getName());
      assertEquals(MAINT, type);
      
    } else {
      assertEquals(OTHER, type);
      assertEquals(null, type.getName());
      assertEquals(null, type.getAbbreviation());
    }
  }
  
}
