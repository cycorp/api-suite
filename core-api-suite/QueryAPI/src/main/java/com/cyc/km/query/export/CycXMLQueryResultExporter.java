/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.km.query.export;

/*
 * #%L
 * File: CycXMLQueryResultExporter.java
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
import com.cyc.base.CycAccessManager;
import com.cyc.query.InferenceIdentifier;
import com.cyc.baseclient.api.SubLAPIHelper;
import com.cyc.baseclient.export.ExportException;
import com.cyc.baseclient.export.PrintStreamExporter;
import com.cyc.query.QueryImpl;

import java.io.PrintStream;

/**
 * Exports the results of a query in XML to the specified string builder.
 * Delegates the XML generation to Cyc.
 */
public class CycXMLQueryResultExporter extends PrintStreamExporter<QueryImpl> {

  public CycXMLQueryResultExporter() {
    super();
  }

  public CycXMLQueryResultExporter(PrintStream printStream) {
    super(printStream);
  }

  @Override
  protected void doExport() throws ExportException {
    final QueryImpl query = object;
    try {
      final InferenceIdentifier inferenceIdentifier = query.getInferenceIdentifier();
      final String command = SubLAPIHelper.makeSubLStmt("get-suggested-query-results-in-xml",
              inferenceIdentifier.getProblemStoreID(),
              inferenceIdentifier.getInferenceID());
      append(CycAccessManager.getCurrentAccess().converse().converseString(command));
    } catch (Exception ex) {
      throw new ExportException("Got exception exporting results for " + query,
              ex);
    }
  }

}
