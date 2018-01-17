package com.cyc.kb;

/*
 * #%L
 * File: ArgUpdate.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
 *
 * @author daves
 */
public interface ArgUpdate {

  public ArgPosition getArgPosition();

  public ArgUpdateOperation getOperation();

  public Object getValue();

  public static enum ArgUpdateOperation {
    SET(":SET"),
    INSERT_BEFORE(":INSERT-BEFORE"),
    INSERT_AFTER(":INSERT-AFTER"),
    DELETE(":DELETE");

    private final String name;

    ArgUpdateOperation(String name) {
      this.name = name;
    }

    public String toName() {
      return name;
    }

    public static ArgUpdateOperation fromValue(String symbol) {
      for (ArgUpdateOperation op : ArgUpdateOperation.values()) {
        if (op.toName().equals(symbol)) {
          return op;
        }
      }
      return valueOf(symbol);
    }
  }
}
