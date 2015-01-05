package  com.cyc.baseclient.xml;

/*
 * #%L
 * File: NameNodeFilter.java
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

import  org.w3c.dom.DOMException;
import  org.w3c.dom.traversal.NodeFilter;
import  org.w3c.dom.Node;

/**
 * Provides a implementation of org.w3c.dom.traversal.NodeFilter that allows a TreeWalker
 * to select nodes of a DOM tree based on their tag name.<p>
 *
 * @version $Id: NameNodeFilter.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stefano Bertolo
 */
public class NameNodeFilter
        implements NodeFilter {
    private String tagname;

    /**
     * Creates a NameNodeFilter that will be used by a TreeWalker to navigate a
     * DOM tree obtained from an XML input document and accept only nodes that
     * in the XML document were tagged with the string that is supplied as the
     * only parameter of this creator method.
     *
     * For example, to create a NameNodeFilter that only looks at DOM nodes
     * that were tagged <MyTag>...</MyTag> in the input XML document, do
     * NameNodeFilter filter = new NameNodeFilter("MyTag");
     *
     */
    public NameNodeFilter (String tag) {
        this.tagname = tag;
    }

    /**
     * Sets the NameNodeFilter's tagname field
     *
     */
    public void setTagName (String tag) {
        this.tagname = tag;
    }

    /**
     * Gets the NameNodeFilter's tagname field
     *
     * @return the String in the NameNodeFilter's tagname field
     *
     */
    public String getTagName () {
        return  this.tagname;
    }

    /**
     * Decides whether an input node should be accepted based on its Local
     * Name.
     *
     * @param node the input node
     * @return a short, whose value depends on the appropriate static fields of
     * the org.w3c.dom.Node class.
     *
     */
    public short acceptNode (Node node) throws DOMException {
        String name = node.getLocalName();
        if (null == name) {
            throw  new DOMException(DOMException.NOT_FOUND_ERR, "DOM Node doesn't have a local name!");
        }
        if (this.tagname.equals(name)) {
            return  this.FILTER_ACCEPT;
        }
        else if (node.hasChildNodes()) {
            return  this.FILTER_SKIP;
        }
        else {
            return  this.FILTER_REJECT;
        }
    }
}



