package com.cyc.baseclient.connection;

/*
 * #%L
 * File: CyclopsBenchmark.java
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
import com.cyc.baseclient.CycClient;
import java.net.*;
import java.io.*;

/**
 * Loads and executes the Cyclops (Cyc Logical Operations) benchmark.<p>
 * @version $Id: CyclopsBenchmark.java 162904 2015-12-02 18:35:34Z nwinant $
 * @author Stephen L. Reed
 */
public class CyclopsBenchmark {

    /**
     * Constructs a new CyclopsBenchmark object.
     */
    public CyclopsBenchmark() {
    }

    /**
     * Path to benchmark the subl file.
     */
    public String benchmarkFilePath = "benchmarks.lisp";

    /**
     * Main method to load and execute the Cyclops benchmark.
     */
    public static void main(String[] args) {
        CyclopsBenchmark cyclopsBenchmark = new CyclopsBenchmark();
        cyclopsBenchmark.execute();
    }

    public void execute () {
        Double cyclops = null;
        try {
            CycAccess cycAccess = CycAccessManager.getCurrentAccess();
            System.out.println("Loading benchmarks.lisp");
            String script = "(load \"" + benchmarkFilePath + "\")";
            cycAccess.converse().converseVoid(script);
            script = "(benchmark-cyclops)";
            System.out.println("Running Cyclops benchmark");
            cyclops = (Double) cycAccess.converse().converseObject(script);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(cyclops + " Cyclops (Cyc Logical Operations Per Second)");
    }

}
