package com.cyc.baseclient.xml;

/*
 * #%L
 * File: CycJaxbMarshaller.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * A class for JAXB marshallers
 *
 * @author baxter
 */
public abstract class CycJaxbMarshaller<T> {

  protected static final Pattern XML_CHARS = Pattern.compile("[<>&]");
  protected final Marshaller marshaller;
  private URI xslUri = null;
  protected final OutputFormat outputFormat = new OutputFormat();
  /**
   * Map from namespace URIs onto the prefixes desired for them.
   */
  protected final Map<String, String> preferredPrefixes = new HashMap<String, String>();

  public CycJaxbMarshaller(final Class clazz) throws JAXBException {
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
    try {
      Document document = marshalToDocument(pojo);
      Transformer transformer = createTransformer();
      transformer.transform(new DOMSource(document), new StreamResult(destination));
    } catch (ParserConfigurationException parserConfigurationException) {
    } catch (JAXBException jAXBException) {
    } catch (DOMException dOMException) {
    } catch (TransformerFactoryConfigurationError transformerFactoryConfigurationError) {
    } catch (IllegalArgumentException illegalArgumentException) {
    } catch (TransformerException transformerException) {
    }
    destination.flush();
    destination.close();
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
      try {
        Document document = marshalToDocument(pojo);
        Transformer transformer = createTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(destination));
      } catch (ParserConfigurationException parserConfigurationException) {
      } catch (JAXBException jAXBException) {
      } catch (DOMException dOMException) {
      } catch (TransformerFactoryConfigurationError transformerFactoryConfigurationError) {
      } catch (IllegalArgumentException illegalArgumentException) {
      } catch (TransformerException transformerException) {
      }
      destination.flush();
      destination.close();
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

  protected void cdatafy(Node node) {
    if (shouldUseCDATA(node)) {
      final String textContent = node.getTextContent();
      final CDATASection cdata = node.getOwnerDocument().createCDATASection(textContent);
      node.setTextContent(null);
      node.appendChild(cdata);
    } else {
      final NodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        cdatafy(children.item(i));
      }
    }
  }

  protected Transformer createTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError, IllegalArgumentException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
    return transformer;
  }

  protected Document getNewDocument() throws ParserConfigurationException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.newDocument();
    return document;
  }

  /**
   *
   * @return The URI of the XSL stylesheet to use to render the marshalled XML
   * as HTML. <code>null</code> if none.
   */
  public URI getXslUri() {
    return xslUri;
  }

  public void setXslUri(URI uri) {
    xslUri = uri;
  }

  protected void maybeInsertStylesheet(final Document document) throws DOMException {
    final URI xsluri = getXslUri();
    if (xsluri != null) {
      final ProcessingInstruction processingInstruction
              = document.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"" + xsluri + "\"");
      Node rootElement = document.getDocumentElement();
      document.insertBefore(processingInstruction, rootElement);
    }
  }

  /** Should we use CDATA for node? **/
  protected boolean shouldUseCDATA(Node node) {
    final NodeList children = node.getChildNodes();
    return children.getLength() == 1 && children.item(0) instanceof Text && shouldUseCDATA(node.getTextContent());
  }

  /** Should we use CDATA for str? **/
  protected boolean shouldUseCDATA(String str) {
    return str != null && XML_CHARS.matcher(str).find();
  }

  protected Document marshalToDocument(T pojo) throws JAXBException, DOMException, ParserConfigurationException, IOException {
    Document document = getNewDocument();
    marshal(pojo, document);
    maybeInsertStylesheet(document);
    cdatafy(document);
    return document;
  }

}
