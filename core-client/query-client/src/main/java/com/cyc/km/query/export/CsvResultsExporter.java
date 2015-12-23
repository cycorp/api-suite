/*
 */
package com.cyc.km.query.export;

/*
 * #%L
 * File: CsvResultsExporter.java
 * Project: Query Client
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

import com.cyc.base.exception.CycConnectionException;
import com.cyc.baseclient.exception.ExportException;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KbException;
import com.cyc.query.Query;
import com.cyc.query.QueryAnswer;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Exports query results in Comma-Separated-Values format
 * @author baxter
 */
public class CsvResultsExporter extends ResultsExporter {

  private static final String DOUBLE_QUOTE = "\"\"";
  private static final String QUOTE = "\"";

  /**
   * Construct a new exporter whose output will go to the specified stream.
   * <p/>
   * The exporter will <i>not</i> close the stream when it is done, or even when
   * its {@link #close()} method is invoked. Thus this constructor can be safely
   * used on {@link System#out} and similar streams that one usually does not
   * want to close.
   * @param printStream
   */
  public CsvResultsExporter(PrintStream printStream) {
    super(printStream);
  }

  /**
   *
   * @return the delimiter character.
   */
  protected char getDelimiterChar() {
    return ',';
  }

  @Override
  protected void printHeader() throws ExportException, CycConnectionException {
    final RowPrinter printer = new RowPrinter() {

      @Override
      String stringForVar(Variable var) throws ExportException, CycConnectionException {
        return var.getName();
      }
    };
    printer.printRow();
  }

  @Override
  protected void finalizeState() throws ExportException {
    super.finalizeState();
    vars.clear();
  }

  @Override
  protected void initializeState(Query object) throws ExportException {
    super.initializeState(object);
    try {
      vars.addAll(object.getQueryVariables());
    } catch (KbException ex) {
      throw new ExportException(ex);
    }
  }

  @Override
  protected void printAnswer(final QueryAnswer answer) throws CycConnectionException, ExportException {
    final RowPrinter printer = new RowPrinter() {

      @Override
      String stringForVar(Variable var) throws CycConnectionException {
        return answer.getBinding(var).toString();
      }
    };
    printer.printRow();
  }

  private abstract class RowPrinter {

    private void printRow() throws CycConnectionException, ExportException {
      boolean started = false;
      for (final Variable var : vars) {
        try {
          if (started) {
            append(getDelimiterChar());
          }
          started = true;
          append(encodeString(stringForVar(var)));
        } catch (IOException ex) {
          throw new ExportException("Caught exception printing " + var, ex);
        }
      }
      println();
    }

    abstract String stringForVar(Variable var) throws CycConnectionException, ExportException;
  }

  private String encodeString(final String rawString) {
    String result = escapeQuotes(rawString);
    if (mustQuote(result)) {
      result = QUOTE + result + QUOTE;
    }
    return result;
  }

  private boolean mustQuote(String string) {
    return string.indexOf(QUOTE) >= 0
            || string.indexOf(getDelimiterChar()) >= 0;
  }

  static private String escapeQuotes(String rawString) {
    return rawString.replace(QUOTE, DOUBLE_QUOTE);
  }
  final private List<Variable> vars = new ArrayList<Variable>();
}
