package com.cyc.baseclient.kbtool;

/*
 * #%L
 * File: CycOwlTool.java
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
import com.cyc.baseclient.AbstractKBTool;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.Fort;
import com.cyc.baseclient.cycobject.FortImpl;

/**
 * Tools for importing OWL ontologies into the Cyc KB
 * 
 * @deprecated Will either by moved to the KnowledgeManagement API, or deleted.
 * @author nwinant
 */
@Deprecated
public class CycOwlTool extends AbstractKBTool {
  
  public CycOwlTool(CycAccess client) {
    super(client);
  }
  
  
  // Public

  /**
   * Imports an OWL ontology.
   *
   * @param uri the URI of the ontology
   * @param prefix the prefix to append to names of new constants created for terms in the ontology
   *
   * @return the FortImpl representing the imported ontology
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  public Fort importOwlOntology(final String uri, final String prefix)
          throws CycConnectionException, CycApiException {
    final String sourceFile = uri;
    return importOwlOntology(uri, prefix, sourceFile);
  }

  /**
   * Imports an OWL ontology.
   *
   * @param uri the URI of the ontology
   * @param prefix the prefix to append to names of new constants created for terms in the ontology
   * @param sourceFile the path for the file from which to import the ontology
   *
   * @return the FortImpl representing the imported ontology
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  public Fort importOwlOntology(final String uri, final String prefix,
          final String sourceFile)
          throws CycConnectionException, CycApiException {
    final Fort ontology = null;
    return importOwlOntology(uri, prefix, sourceFile, ontology);
  }

  /**
   * Imports an OWL ontology.
   *
   * @param uri the URI of the ontology
   * @param prefix the prefix to append to names of new constants created for terms in the ontology
   * @param sourceFile the path for the file from which to import the ontology, if not from the URI
   * @param ontology the existing FortImpl representing the ontology, if we are to update an already-imported ontology
   *
   * @return the FortImpl representing the imported ontology
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  public Fort importOwlOntology(final String uri, final String prefix,
          final String sourceFile, final Fort ontology)
          throws CycConnectionException, CycApiException {
    final Fort quotedIsa = null;
    return importOwlOntology(uri, prefix, sourceFile, ontology, quotedIsa);
  }

  /**
   * Imports an OWL ontology.
   *
   * @param uri the URI of the ontology
   * @param prefix the prefix to append to names of new constants created for terms in the ontology
   * @param sourceFile the path for the file from which to import the ontology, if not from the URI
   * @param ontology the existing FortImpl representing the ontology, if we are to update an already-imported ontology
   * @param quotedIsa the collection of which newly created terms should be quoted instances. If null, Cyc will create one.
   *
   * @return the FortImpl representing the imported ontology
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  public Fort importOwlOntology(final String uri, final String prefix,
          final String sourceFile, final Fort ontology,
          final Fort quotedIsa)
          throws CycConnectionException, CycApiException {
    final Fort currentCyclist = getCyc().getOptions().getCyclist();
    return importOwlOntology(uri, prefix, sourceFile, ontology, quotedIsa,
            currentCyclist);
  }

  /**
   * Imports an OWL ontology.
   *
   * @param uri the URI of the ontology
   * @param prefix the prefix to append to names of new constants created for terms in the ontology
   * @param sourceFile the path for the file from which to import the ontology, if not from the URI
   * @param ontology the existing FortImpl representing the ontology, if we are to update an already-imported ontology
   * @param quotedIsa the collection of which newly created terms should be quoted instances. If null, Cyc will create one.
   * @param cyclist the cyclist to attribute the new terms and assertions to
   *
   * @return the FortImpl representing the imported ontology
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  public Fort importOwlOntology(final String uri, final String prefix,
          final String sourceFile, final Fort ontology,
          final Fort quotedIsa,
          final Fort cyclist)
          throws CycConnectionException, CycApiException {
    final String sourceFileString = ((sourceFile != null) && (sourceFile.length() > 0)) ? "\"" + sourceFile + "\"" : "nil";
    final String ontologyString = (ontology == null) ? "nil" : FortImpl.stringApiValue(
            ontology);
    final String quotedIsaString = (quotedIsa == null) ? "nil" : FortImpl.stringApiValue(
            quotedIsa);
    final String cyclistString = (cyclist == null) ? "nil" : FortImpl.stringApiValue(
            cyclist);
    final Object object = getConverse().converseObject(
            "(import-owl-ontology " + "\"" + uri + "\" " + "\"" + prefix + "\" " + sourceFileString + " " + ontologyString + " " + quotedIsaString + " " + cyclistString + ")");
    if (object instanceof Fort) {
      return (Fort) object;
    } else {
      return null;
    }
  }
}
