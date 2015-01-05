package com.cyc.baseclient.api;

/*
 * #%L
 * File: CycLFormat.java
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

import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycList;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cyc.baseclient.cycobject.DefaultCycObject;

/**
 *
 * @author daves
 */
public class CycLFormat extends Format {

    private CycAccess cyc;
    private boolean newlines = true;
    private boolean showHashDollar = true;
    protected CycLFormat(CycAccess cyc) {
        this.cyc = cyc;
    }

    /**
     * Get a default CycLFormat instance that will use the specified CycClient.
     */
    public static CycLFormat getInstance(CycAccess cyc) {
        final CycLFormat cycF = new CycLFormat(cyc);
        return cycF;
    }

    public void setWrapLines(boolean newlines) {
        this.newlines = newlines;
    }
    
    public void setShowHashDollar(boolean showHashDollar) {
        this.showHashDollar = showHashDollar;
    }

    private CycAccess getCycAccess() {
        return cyc;
    }
    
    private String buildFormatCommand(Object obj) {
        return "(get-pretty-formatted-string " + DefaultCycObject.stringApiValue(obj) + " " 
                + DefaultCycObject.stringApiValue(showHashDollar) + " " 
                + DefaultCycObject.stringApiValue(newlines) + ")";
    }
    
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        final String command = buildFormatCommand(obj);
        try {
            toAppendTo.append(getCycAccess().converse().converseString(command));
        } catch (Exception ex) {
            throw new BaseClientRuntimeException("Exception formatting " + obj, ex);
        }
        return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        try {
            String command = "(multiple-value-list (read-from-string-ignoring-errors (cyclify-string " + DefaultCycObject.stringApiValue(source) + ")))";
            CycList ret = (CycList) getCycAccess().converse().converseCycObject(command);
            Object value = ret.first();
            Object indexOrError = ret.second();
            if (indexOrError instanceof Integer) {
                pos.setIndex((Integer) indexOrError);
                return value;
            } else {
                pos.setErrorIndex(pos.getIndex());
                return null;
            }
        } catch (CycConnectionException ex) {
            Logger.getLogger(CycLFormat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CycApiException ex) {
            Logger.getLogger(CycLFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
