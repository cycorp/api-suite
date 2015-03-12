package com.cyc.baseclient.xml;

/*
 * #%L
 * File: Marshaller.java
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

import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.GuidImpl;
import com.cyc.baseclient.cycobject.ByteArray;
import com.cyc.baseclient.cycobject.NartImpl;
import java.io.*;

/**
 * Provides the behavior of a CYC-ML marshaller.<p>
 *
 * @version $Id: Marshaller.java 155703 2015-01-05 23:15:30Z nwinant $
 * @author Stephen L. Reed
 */

public class Marshaller {

    /**
     * Marshalls the given object into its CYC-ML XML representation.
     *
     * @param object the object for marshalling
     * @return the CYC-ML XML representation string
     */
    public static String marshall(Object object) throws IOException {
        if (object instanceof GuidImpl)
            return ((GuidImpl) object).toXMLString();
        else if (object instanceof CycSymbolImpl)
            return ((CycSymbolImpl) object).toXMLString();
        else if (object instanceof CycVariableImpl)
            return ((CycVariableImpl) object).toXMLString();
        else if (object instanceof CycConstantImpl)
            return ((CycConstantImpl) object).toXMLString();
        else if (object instanceof NartImpl)
            return ((NartImpl) object).toXMLString();
        else if (object instanceof CycArrayList)
            return ((CycArrayList) object).toXMLString();
        else if (object instanceof String)
            return "<string>" + (String) object + "</string>\n";
        else if (object instanceof Integer)
            return "<integer>" + ((Integer) object).toString() + "</integer>\n";
        else if (object instanceof Double)
            return "<double>" + ((Double) object).toString() + "</double>\n";
        else if (object instanceof ByteArray)
            return ((ByteArray) object).toXMLString();
        else
            throw new IOException("Invalid object for marshalling " + object);
    }

}
