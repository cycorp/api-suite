package com.cyc.query;

/*
 * #%L
 * File: ProofValidationMode.java
 * Project: Core API Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
 * How carefully to check proofs for semantic well-formedness.
 *
 * @author baxter
 */
public enum ProofValidationMode implements InferenceParameterValue {

  /**
   * Full (most expensive). The intermediate proofs that are used in inference
   * are checked for all kinds of semantic well-formedness.
   */
  ALL,
  /**
   * Only arg-type. The intermediate proofs that are used in inference are
   * checked only for arg-type constraints.
   */
  ARG_TYPE,
  /**
   * Only HL validations. The intermediate proofs that are used in inference are
   * checked only for HL validations.
   */
  MINIMAL,
  /**
   * None (least expensive). No semantic well-formedness checks are performed on
   * the intermediate proofs that are used in inference.
   */
  NONE;


}
