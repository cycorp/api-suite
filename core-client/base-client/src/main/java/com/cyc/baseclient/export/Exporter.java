package com.cyc.baseclient.export;

import com.cyc.baseclient.exception.ExportException;

/*
 * #%L
 * File: Exporter.java
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

/**
 * Interface for objects that export objects of type E.
 * @param <T> The type of object that this exporter exports.
 * @author baxter
 */
public interface Exporter<T> {
  void export(T object) throws ExportException;
}
