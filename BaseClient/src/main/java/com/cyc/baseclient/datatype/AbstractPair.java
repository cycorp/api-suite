package  com.cyc.baseclient.datatype;

/*
 * #%L
 * File: AbstractPair.java
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

import  java.util.StringTokenizer;
import  java.io.Serializable;


/**
 * Implements an ordered pair, two associated <code>Object</code>s.<p>
 *
 * @version $Id: AbstractPair.java 155703 2015-01-05 23:15:30Z nwinant $
 * @author Bjorn Aldag
 */
public abstract class AbstractPair
        implements Serializable {
    protected static final String beginChar = "(";
    protected static final String endChar = ")";
    protected static final String separatorChar = "#";
    protected static final String separators = beginChar + endChar + separatorChar;
    /**
     * The first component of the pair.
     */
    public Object component1;
    /**
     * The second component of the pair.
     */
    public Object component2;

    /**
     * Constructs an AbstractPair object.
     */
    public AbstractPair () {
    }

    /**
     * Constructs a new pair, with <code>component1</code> as its first and
     * <code>component2</code> as its second component.
     */
    public AbstractPair (Object component1, Object component2) {
        this.component1 = component1;
        this.component2 = component2;
    }

    /**
     * Compares the specified object with this <code>AbstractPair</code> for
     * equality.
     * <p>
     * Returns <code>true</code> if the given object is also a pair with the same
     * components, <code>false</code> otherwise.
     *
     * @return <code>true</code> if the given object is also a pair with the same
     * components, <code>false</code> otherwise.
     */
    public boolean equals (Object o) {
        return  (o.getClass().equals(this.getClass()) && (((((AbstractPair)o).component1 == null) &&
                (this.component1 == null)) || ((this.component1 != null) && ((AbstractPair)o).component1.equals(this.component1)))
                && (((((AbstractPair)o).component2 == null) && (this.component2 == null)) || ((this.component2
                != null) && ((AbstractPair)o).component2.equals(this.component2))));
    }

    public boolean elementsEqual () {
        return  component1.equals(component2);
    }

    /**
     * Returns a hash code value of this pair.
     *
     * @return a hash code value of this pair.
     */
    public int hashCode () {
        return  (component1 == null ? 0 : component1.hashCode()) + (component2 == null ? 1 : component2.hashCode());
    }

    /**
     * Returns a string representation of this pair.
     *
     * @return a string representation of this pair.
     */
    public String toString () {
        return  beginChar + component1 + separatorChar + component2 + endChar;
    }
}



