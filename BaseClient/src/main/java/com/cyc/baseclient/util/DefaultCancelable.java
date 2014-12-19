package com.cyc.baseclient.util;

/*
 * #%L
 * File: DefaultCancelable.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

//// Internal Imports

//// External Imports

/**
 * The current implementation is a toy implementation that merely makes
 * it possible to write a unit test for CancelManager.
 * <P>DefaultCancelable is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date October 14, 2005, 2:29 PM
 * @version $Id: DefaultCancelable.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public class DefaultCancelable implements Cancelable {
  
  //// Constructors
  
  /** Creates a new instance of DefaultCancelable. */
 public DefaultCancelable(String name) {
   this.taskName = name;
 }
  
  //// Public Area
  
  public void cancelTask() {
    System.out.println("Task " + taskName + " canceled!");
  }
  
  public String toString() {
    return taskName;
  }
  //// Protected Area
  
  //// Private Area
  
  //// Internal Rep
  String taskName;
  
  //// Main
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
  }
  

  
}
