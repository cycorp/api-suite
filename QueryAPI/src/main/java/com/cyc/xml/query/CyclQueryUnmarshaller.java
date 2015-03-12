package com.cyc.xml.query;

/*
 * #%L
 * File: CyclQueryUnmarshaller.java
 * Project: Query API Implementation
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

import java.io.InputStream;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

//import org.opencyc.xml.Constants;
import com.cyc.baseclient.xml.Constants;

import com.cyc.baseclient.xml.cycml.CycMLDecoder;
import org.xml.sax.SAXParseException;

/**
 *
 * @author baxter
 */
public class CyclQueryUnmarshaller {

    private final javax.xml.bind.Unmarshaller unmarshaller;
    public static final String XSD_URI = Constants.XSD_HOME + "/cyclquery.xsd";

    public CyclQueryUnmarshaller() throws JAXBException {
        final String packages = CycMLDecoder.class.getPackage().getName() + ":"
                + this.getClass().getPackage().getName();
        this.unmarshaller = JAXBContext.newInstance(packages).createUnmarshaller();
        if (shouldValidate()) {
            try {
                final SchemaFactory schemaFactory = SchemaFactory.newInstance(
                        XMLConstants.W3C_XML_SCHEMA_NS_URI);
                final URL cyclQueryUrl = new URL(XSD_URI);
                final Schema schema = schemaFactory.newSchema(cyclQueryUrl);
                unmarshaller.setSchema(schema);
            } catch (SAXParseException e) {
                // Means file wasn't found.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    protected boolean shouldValidate() {
      return false;
    }

    public Object unmarshal(final InputStream stream) throws JAXBException {
        return unmarshaller.unmarshal(stream);
    }
}
