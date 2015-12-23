package com.cyc.baseclient;

/*
 * #%L
 * File: KbTransaction.java
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

import com.cyc.base.cycobject.Fort;
import java.util.List;
import com.cyc.base.cycobject.ElMt;

/**
 * Defines the interface for KB Transaction, which provide support similar to that of database transactions.
 * @author daves
 */
public interface KbTransaction {

    /**
     * Begin the KBTransaction. Supported KB modification operations will be
     * recorded and available for rollback if needed.
     *
     */
    void begin();

    /**
     * Commit all of the KB modification operations that have occurred within
     * this transaction. If there are any failures, a BaseClientRuntimeException will be
     * thrown. After commit() has returned (whether normally or by throwing an
     * exception), this KBTransaction will no longer be active.
     */
    void commit();

    /**
     * Roll back any KB modifications that occurred while this KBTransaction was
     * active.  After rollback(), this KBTransaction will no longer be active.
     */
    void rollback();

    /**
     * Assign to the transaction the job of asserting sentence with the relevant
     * parameters.
     *
     * @param sentence The sentence to be asserted.
     * @param mt The Microtheory in which to make the assertion
     * @param bookkeeping If true, bookkeeping will be stored along with the assertion.
     * @param transcripting If true, the new assertion will be added to the Cyc server's transcript.
     * @param wffCheckingDisabled If true, the assertion will be made to the Cyc server without any semantic well-formedness checking.
     * @param templates A list of creationTemplates that should be used when making this assertion.
     */
    void noteForAssertion(String sentence, ElMt mt, boolean bookkeeping, boolean transcripting, boolean wffCheckingDisabled, List<Fort> templates);

    /**
     * Assign to the transaction the job of asserting sentence with the relevant
     * parameters.
     *
     * @param sentence The sentence to be unasserted
     * @param mt The microtheory from which <code>sentence</code> should be
     * unasserted
     * @param bookkeeping Should bookkeeping be made when unasserting?
     * @param transcripting Should the unassertion be added to the Cyc server
     * transcript?
     */
    void noteForUnassertion(String sentence, ElMt mt, boolean bookkeeping, boolean transcripting);

    /**
     * Note in the transaction that term has been created. Unlike
     * noteForAssertion and noteForUnassertion, noteCreation does not take over
     * the creation of the constant. Instead it merely remembers on the
     * transaction that this constant has been created inside the transaction,
     * so it can be unwound as necessary if the transaction is rolled back.
     *
     * @param term The constant that was created
     */
    void noteCreation(Fort term);
}
