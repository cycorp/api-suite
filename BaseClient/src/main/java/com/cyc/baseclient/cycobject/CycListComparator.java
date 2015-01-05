package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycListComparator.java
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

import java.util.*;

/**
 * Implements a <tt>Comparator</tt> for the <tt>sort</tt> method of the
 * <tt>CycList</tt> class.
 *
 * @version $0.1$
 * @author Stephen L. Reed
 */
public class CycListComparator implements Comparator {

    /**
     * Constructs a new CycListComparator object.
     */
    public CycListComparator() {
    }

    /**
     * Compares two <tt>CycList</tt> elements, according to their string
     * representations.
     *
     * @param o1 an Object for comparison
     * @param o2 another Object for comparison.
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     * @exception ClassCastException - if the arguments' types prevent them from
     * being compared by this Comparator
     */
    public int compare (Object o1, Object o2) {
        String string1 = o1.toString();
        String string2 = o2.toString();
        return string1.compareTo(string2);
    }

    /**
     * Returns <tt>true</tt> if some other object is equal to this <tt>Comparator</tt>
     *
     * @param object the reference object with which to compare.
     * @return <tt>true</tt> only if the specified object is also a
     * comparator and it imposes the same ordering as this comparator.
     */
     public boolean equals (Object object) {
        return object instanceof CycListComparator;
     }
}
