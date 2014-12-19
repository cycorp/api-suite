package com.cyc.baseclient.util;

/*
 * #%L
 * File: CycWorkerEvent.java
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

import com.cyc.baseclient.ui.SwingWorker;
import com.cyc.baseclient.ui.CycWorker;
import java.util.*;

/**
 * This is an event object for CycWorker events. It is currently
 * an unmodified subclass of EventObject, but more functionality may
 * be added in the future.
 */
public class CycWorkerEvent extends EventObject {
    
    /** 
     * Creates a new instance of CycWorkerEvent.
     * @param source The CycWorker that is generating this event.
     **/
    public CycWorkerEvent(Object source) {
        super(source);
    }

}
