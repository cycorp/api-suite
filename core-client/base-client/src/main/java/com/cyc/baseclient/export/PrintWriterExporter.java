package com.cyc.baseclient.export;

/*
 * #%L
 * File: PrintWriterExporter.java
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
import com.cyc.baseclient.exception.ExportException;
import java.io.Closeable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Class of exporters that send output to a PrintWriter.
 *
 * @param <T> the type of object to export.
 * @author baxter
 */
public abstract class PrintWriterExporter<T> extends StatefulSynchronizedExporter<T>
        implements Exporter<T>, Appendable, Closeable {

  private ThreadLocal<PrintWriter> printWriter = new ThreadLocal<PrintWriter>();

  /**
   * Make a new exporter that will send output to the specified writer.
   *
   * @param printWriter
   */
  public PrintWriterExporter(PrintWriter printWriter) {
    this();
    this.printWriter.set(printWriter);
  }

  /**
   * Make a new exporter that will send output to the specified stream.
   * <p/>
   * The exporter will <i>not</i> close the stream when it is done, or even when
   * its {@link #close()} method is invoked. Thus this constructor can be safely
   * used on {@link System#out} and similar streams that one usually does not
   * want to close.
   *
   * @param printStream
   */
  public PrintWriterExporter(final PrintStream printStream) {
    this(new PrintWriter(new FilterOutputStream(printStream) {

      @Override
      public void close() {
        printStream.flush();
      }

    }));
  }

  /**
   * Make a new exporter. Should be passed a print stream at export time.
   *
   * @see #exportToWriter(java.lang.Object, java.io.PrintWriter)
   */
  public PrintWriterExporter() {
    super();
  }

  @Override
  protected void finalizeState() throws ExportException {
    try {
      close();
    } catch (IOException ex) {
      throw new ExportException("Couldn't close print writer following export.",
              ex);
    }
  }

  /**
   * Export the specified object to the specified stream.
   *
   * @param object
   * @param writer
   * @throws ExportException
   */
  public void exportToWriter(final T object, final PrintWriter writer) throws ExportException {
    this.printWriter.set(writer);
    export(object);
  }

  /**
   * Export the specified object to a string.
   *
   * @param object
   * @return the result string.
   * @throws ExportException
   */
  public String exportToString(final T object) throws ExportException {
    try {
      StringWriter stringWriter = new StringWriter();
      final PrintWriter exportWriter = new PrintWriter(stringWriter);
      exportToWriter(object, exportWriter);
      final String resultXML = stringWriter.toString();
      exportWriter.close();
      return resultXML;
    } catch (Exception ex) {
      throw new ExportException(ex);
    }
  }

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    getPrintWriter().append(csq);
    return this;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    getPrintWriter().append(csq, start, end);
    return this;
  }

  @Override
  public Appendable append(char c) throws IOException {
    getPrintWriter().append(c);
    return this;
  }

  /**
   * Close the {@link PrintWriter}.
   *
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    if (printWriter.get() != null) {
      printWriter.get().close();
    }
  }
  //@TODO -- Add trampolines for print(f/ln), format, write methods here as needed.

  /**
   * Trampoline to {@link PrintWriter#println(java.lang.Object) }.
   *
   * @param x
   */
  public void println(Object x) {
    getPrintWriter().println(x);
  }

  /**
   * Trampoline to {@link PrintWriter#println() }.
   */
  protected void println() {
    getPrintWriter().println();
  }

  protected PrintWriter getPrintWriter() {
    if (printWriter.get() == null) {
      throw new IllegalStateException("printStream has not been initialized.");
    }
    return printWriter.get();
  }
}
