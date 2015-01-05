package com.cyc.baseclient.export;

/*
 * #%L
 * File: StatefulSynchronizedExporter.java
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

/**
 * Exporter class for which the export method is synchronized, and
 * state is maintained during the export.
 * @param <T> The type of object that this exporter exports.
 * @author baxter
 */
public abstract class StatefulSynchronizedExporter<T> implements Exporter<T> {

  /** The object being exported. * */
  protected T object;

  @Override
  public final synchronized void export(T object) throws ExportException {
    try {
      initializeState(object);
      doExport();
    } catch (Exception e) {
      throw new ExportException(e);
    } finally {
      finalizeState();
    }
  }

  /**
   * Set up state prior to export.
   *
   * @param object the object being exported
   * @throws ExportException
   */
  protected void initializeState(T object) throws ExportException {
    this.object = object;
  }

  /**
   * Perform the export proper.
   *
   * @throws ExportException
   */
  abstract protected void doExport() throws Exception;

  /**
   * Clean up state following export.
   *
   * @throws ExportException
   */
  protected void finalizeState() throws ExportException {
  }
}
