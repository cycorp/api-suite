package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: Pair.java
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

import com.cyc.baseclient.datatype.AbstractPair;
import java.util.StringTokenizer;

/**
 * Implements an ordered pair, two associated <code>Object</code>s.<p>
 *
 * @version $Id: Pair.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Bjorn Aldag
 */
public class Pair extends AbstractPair{

  /**
   * Constructs a new Pair object.
   */
  public Pair(){
    }

  /**
   * Constructs a new Pair object given two components.
   *
   * @param component1 the first component of the Pair
   * @param component2 the second component of the Pair
   */
  public Pair(Object component1, Object component2) {super(component1, component2);}

  /**
   * Returns a <code>Pair</code> representation of a <code>String</code>.
   *
   * @param pairString the string to be parsed into a Pair object
   * @return a <coe>Pair</code> representation of a <code>String</code>.
   * @throws DataFormatException when the given string is not parsable to a
   *         <code>Pair</code>
   */
  public static Pair parsePair(String pairString) {
    StringTokenizer components = new StringTokenizer(pairString, separators);
    return new Pair(components.nextToken(), components.nextToken());
    }
}
