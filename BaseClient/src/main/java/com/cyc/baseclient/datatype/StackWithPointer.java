package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: StackWithPointer.java
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

import java.util.Stack;

/**
 * Extends the <tt>Stack</tt> class to provide a stack pointer.
 *
 * @version $0.1$
 * @author Stephen L. Reed
 */
public class StackWithPointer extends Stack {

    /**
     * Stack pointer.
     */
    public int sp = 0;

    /**
     * Constructs a new empty <tt>StackWithPointer</tt> object.
     */
    public StackWithPointer() {
    }

    /**
     * Pushes the argument onto the stack.
     *
     * @param item object to be pushed onto the <tt>Stack</tt>
     * @return Object that was pushed onto the <tt>Stack</tt>
     */
    public Object push ( Object item ) {
        sp++;
        return super.push(item);
    }

    /**
     * Returns the top of the stack, setting the new top of stack item.
     *
     * @return <tt>Object</tt> that was on the top of the <tt>Stack</tt>
     */
    public Object pop() {
        --sp;
        return super.pop();
    }
}
