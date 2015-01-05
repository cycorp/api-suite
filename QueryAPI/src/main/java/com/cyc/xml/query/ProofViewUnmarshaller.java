package com.cyc.xml.query;

/*
 * #%L
 * File: ProofViewUnmarshaller.java
 * Project: Query API
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
import javax.xml.validation.SchemaFactory;

//import org.opencyc.xml.Constants;
import com.cyc.baseclient.xml.Constants;
import com.cyc.baseclient.xml.cycml.CycMLDecoder;
import org.xml.sax.SAXParseException;

/**
 * A class to read in proof views encoded in XML.
 *
 * @author baxter
 */
public class ProofViewUnmarshaller {

  public static final String XSD_URI = Constants.XSD_HOME + "/proofview.xsd";
  private boolean shouldValidate = true;
  private final javax.xml.bind.Unmarshaller unmarshaller;

  public ProofViewUnmarshaller() throws JAXBException {
    JAXBContext jaxbContext = getJaxbContext();
    this.unmarshaller = jaxbContext.createUnmarshaller();
    if (shouldValidate) {
      try {
        unmarshaller.setSchema(SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
                new URL(XSD_URI)));
      } catch (SAXParseException e) {
        // Means file wasn't found.
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public ProofView unmarshalProofview(final InputStream stream) throws JAXBException {
    synchronized (unmarshaller) {
      return (ProofView) unmarshaller.unmarshal(stream);
    }
  }

  public ProofViewEntry unmarshalEntry(final InputStream stream) throws JAXBException {
    synchronized (unmarshaller) {
      return (ProofViewEntry) unmarshaller.unmarshal(stream);
    }
  }

  static JAXBContext getJaxbContext() throws JAXBException {
    final String packages = CycMLDecoder.class.getPackage().getName() + ":"
            + ProofViewUnmarshaller.class.getPackage().getName();
    final JAXBContext jaxbContext = JAXBContext.newInstance(packages);
    return jaxbContext;
  }
}
