/*
 * Class for exporting query results.
 */
package com.cyc.km.query.export;

/*
 * #%L
 * File: ResultsExporter.java
 * Project: Query API
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
import com.cyc.base.CycConnectionException;
import com.cyc.baseclient.export.ExportException;
import com.cyc.baseclient.export.PrintWriterExporter;
import com.cyc.query.Query;
import com.cyc.query.QueryAnswer;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * An abstract class for exporting {@link Query} results.
 * <p/>
 * As with any {@link PrintWriterExporter}, the output can be sent to a
 * {@link PrintWriter}, a {@link PrintStream}, or a {@link String}.
 * <p/>
 * The results are structured as a document that is sent to the specified
 * destination, and has sections for <ul><li>A header</li><li>Query
 * data</li><li>Query answers</li><li>A footer</li></ul>
 *
 * @author baxter
 */
public abstract class ResultsExporter extends PrintWriterExporter<Query> {

  /**
   *
   */
  public ResultsExporter() {
    super();
  }

  public ResultsExporter(PrintWriter printWriter) {
    super(printWriter);
  }

  /**
   * Construct a new exporter whose output will go to the specified stream.
   * <p/>
   * The exporter will <i>not</i> close the stream when it is done, or even when
   * its {@link #close()} method is invoked. Thus this constructor can be safely
   * used on {@link System#out} and similar streams that one usually does not
   * want to close.
   *
   */
  public ResultsExporter(PrintStream printStream) {
    super(printStream);
  }

  @Override
  final protected void doExport() throws ExportException {
    try {
      printHeader();
      printQueryData();
      printAnswers(object.getAnswers());
      printFooter();
      finishExport();
    } catch (Exception e) {
      throw new ExportException("Caught exception while exporting " + object, e);
    }
  }

  /**
   * Print any data that goes at the beginning of the export.
   *
   * @throws ExportException
   */
  protected void printHeader() throws ExportException, CycConnectionException {
  }

  /**
   * Print any data that goes at the end of the export.
   *
   * @throws ExportException
   */
  protected void printFooter() throws ExportException {
  }

  /**
   * Print data about the query itself.
   *
   * @throws ExportException
   */
  protected void printQueryData() throws ExportException {
  }

  /**
   * Export the answers of the query.
   *
   * @param answers
   * @throws ExportException
   * @throws CycConnectionException
   */
  final protected void printAnswers(List<QueryAnswer> answers) throws CycConnectionException, ExportException {
    heraldStartOfAnswers();
    for (final QueryAnswer answer : answers) {
      printAnswer(answer);
    }
    heraldEndOfAnswers();
  }

  /**
   * Print something right before printing all the answer data.
   */
  protected void heraldStartOfAnswers() {
  }

  /**
   * Print one answer
   *
   * @param answer the answer
   * @throws ExportException
   */
  protected abstract void printAnswer(QueryAnswer answer) throws CycConnectionException, ExportException;

  /**
   * Print something right after printing all the answer data.
   */
  protected void heraldEndOfAnswers() {
  }

  /**
   * After we've printed everything, finish things up.
   *
   * @throws ExportException
   */
  protected void finishExport() throws ExportException {
  }
}
