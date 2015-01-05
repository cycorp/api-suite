package com.cyc.baseclient.xml;

/*
 * #%L
 * File: CycJAXBMarshaller.java
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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * A class for JAXB marshallers
 *
 * @author baxter
 */
public class CycJAXBMarshaller<T> {

  protected static final Pattern XML_CHARS = Pattern.compile("[<>&]");
  protected final Marshaller marshaller;
  protected final OutputFormat outputFormat = new OutputFormat();
  /**
   * Map from namespace URIs onto the prefixes desired for them.
   */
  protected final Map<String, String> preferredPrefixes = new HashMap<String, String>();

  public CycJAXBMarshaller(final Class clazz) throws JAXBException {
    marshaller = JAXBContext.newInstance(clazz).createMarshaller();
    marshaller.setProperty(
            "com.sun.xml.bind.namespacePrefixMapper",
            new NamespacePrefixMapper() {
      @Override
      public String getPreferredPrefix(String namespaceUri,
              String suggestion,
              boolean requirePrefix) {
        return preferredPrefixes.get(namespaceUri);
      }
    });
  }

  /**
   * Output the specified object to the specified writer.
   *
   * @param pojo
   * @param destination
   * @throws JAXBException
   */
  public void marshal(final T pojo, final Writer destination) throws JAXBException, IOException {
    synchronized (marshaller) {
      marshaller.marshal(pojo, new CDATASerializer(destination).asContentHandler());
    }
  }

  /**
   * Output the specified object to the specified DOM Node.
   *
   * @param pojo
   * @param destination
   * @throws JAXBException
   * @see javax.xml.bind.Marshaller#marshal(java.lang.Object, org.w3c.dom.Node)
   * .
   */
  public void marshal(final T pojo, final Node destination) throws JAXBException, IOException {
    synchronized (marshaller) {
      marshaller.marshal(pojo, destination);
    }
  }

  /**
   * Output the specified object to the specified stream.
   *
   * @param pojo
   * @param destination
   * @throws JAXBException
   */
  public void marshal(final T pojo, final OutputStream destination) throws JAXBException, IOException {
    synchronized (marshaller) {
      marshaller.marshal(pojo, new CDATASerializer(destination).asContentHandler());
    }
  }

  /**
   * Set output formatting (pretty printing) on or off.
   *
   * @param b -- True for formatted output, false for non-formatted.
   * @throws PropertyException
   */
  public void setFormattedOutput(final boolean b) throws PropertyException {
    throw new UnsupportedOperationException("Not implemented yet.");
  }

  /**
   * Set the default namespace.
   *
   * @param namespace
   */
  protected void setDefaultNamespace(final String namespace) {
    setPreferredPrefix(namespace, "");
  }

  /**
   * Set the preferred prefix to use for the specified namespace.
   *
   * @param namespace
   * @param prefix
   */
  protected void setPreferredPrefix(final String namespace, final String prefix) {
    preferredPrefixes.put(namespace, prefix);
  }

  private class CDATASerializer extends XMLSerializer {

    public CDATASerializer(OutputStream out) {
      super(out, outputFormat);
    }

    private CDATASerializer(Writer destination) {
      super(destination, outputFormat);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      boolean useCData = XML_CHARS.matcher(
              new String(ch, start, length)).find();
      if (useCData) {
        super.startCDATA();
      }
      super.characters(ch, start, length);
      if (useCData) {
        super.endCDATA();
      }
    }
  }
}
