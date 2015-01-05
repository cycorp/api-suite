package com.cyc.baseclient.inference;

/*
 * #%L
 * File: DefaultInferenceStatus.java
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

//// Internal Imports
import com.cyc.base.inference.InferenceStatus;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.base.cycobject.CycSymbol;

//// External Imports
import java.util.*;

/**
 * <P>DefaultInferenceStatus is designed to...

 <P>Copyright (c) 2004 - 2006 Cycorp, Inc. All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal, tbrussea
 * @date July 27, 2005, 12:23 PM
 * @version $Id: DefaultInferenceStatus.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public final class DefaultInferenceStatus extends CycSymbolImpl implements InferenceStatus {

    private final boolean indicatesDone;

    private DefaultInferenceStatus(String name) {
        this(name, false);
    }

    private DefaultInferenceStatus(String name, boolean indicatesDone) {
        super(name);
        this.indicatesDone = indicatesDone;
    }

    public static DefaultInferenceStatus findInferenceStatus(CycSymbol symbol) {
        return (DefaultInferenceStatus) inferenceStatuses.get(symbol);
    }

    /**
     * Does this status indicate that the inference is done? It may be
     * continuable, but no further work will be performed on it until
     * instructed.
     *
     * @return true iff this status indicates that the inference is done.
     */
  @Override
    public boolean indicatesDone() {
        return this.indicatesDone;
    }
    // should probably try to gracefully find a way to get these from the KB. (*inference-statuses*)
    public final static DefaultInferenceStatus NOT_STARTED = new DefaultInferenceStatus(":NOT-STARTED");
    public final static DefaultInferenceStatus STARTED = new DefaultInferenceStatus(":STARTED");
    public final static DefaultInferenceStatus NEW = new DefaultInferenceStatus(":NEW");
    public final static DefaultInferenceStatus PREPARED = new DefaultInferenceStatus(":PREPARED");
    public final static DefaultInferenceStatus READY = new DefaultInferenceStatus(":READY");
    public final static DefaultInferenceStatus RUNNING = new DefaultInferenceStatus(":RUNNING");
    public final static DefaultInferenceStatus SUSPENDED = new DefaultInferenceStatus(":SUSPENDED", true);
    public final static DefaultInferenceStatus DEAD = new DefaultInferenceStatus(":DEAD", true);
    public final static DefaultInferenceStatus TAUTOLOGY = new DefaultInferenceStatus(":TAUTOLOGY", true);
    public final static DefaultInferenceStatus CONTRADICTION = new DefaultInferenceStatus(":CONTRADICTION", true);
    public final static DefaultInferenceStatus ILL_FORMED = new DefaultInferenceStatus(":ILL-FORMED", true);
    public final static DefaultInferenceStatus FORMATTING = new DefaultInferenceStatus(":FORMATTING");
    private static HashMap inferenceStatuses = new HashMap();

    static {
        inferenceStatuses.put(NOT_STARTED, NOT_STARTED);
        inferenceStatuses.put(STARTED, STARTED);
        inferenceStatuses.put(NEW, NEW);
        inferenceStatuses.put(PREPARED, PREPARED);
        inferenceStatuses.put(READY, READY);
        inferenceStatuses.put(RUNNING, RUNNING);
        inferenceStatuses.put(SUSPENDED, SUSPENDED);
        inferenceStatuses.put(DEAD, DEAD);
        inferenceStatuses.put(TAUTOLOGY, TAUTOLOGY);
        inferenceStatuses.put(CONTRADICTION, CONTRADICTION);
        inferenceStatuses.put(ILL_FORMED, ILL_FORMED);
        inferenceStatuses.put(FORMATTING, FORMATTING);
    }
}
