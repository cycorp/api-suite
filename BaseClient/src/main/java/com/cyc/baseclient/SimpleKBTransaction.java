package com.cyc.baseclient;

/*
 * #%L
 * File: SimpleKBTransaction.java
 * Project: Base Client
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

import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.cycobject.Fort;
import java.util.ArrayList;
import java.util.List;
import com.cyc.base.cycobject.ELMt;

/**
 * The SimpleKBTransaction class provides transactional support for certain operations
 * in the Base Client. In particular, it provides support for asserting and
 * unasserting, and creating constants. Attempting to perform an unsupported KB
 * modification operation (i.e. delete a constant) while inside a SimpleKBTransaction
 * will trigger an UnsupportedOperationException. Nested KBTransactions are not
 * supported. Any attempt to begin a SimpleKBTransaction while already in a transaction
 * will result in an UnsupportedOperationException.
 * <p>
 There is no support for setting different transaction isolation levels. Note
 that some operations may be deferred until the transaction is actually
 committed, so it is not safe to rely on an assertion being in the KB until
 after the transaction has been committed, even in the thread in which the
 assertion is made. SimpleKBTransaction differs from JDBC transactions in that some
 portions of the transaction may be available to other users before the
 transaction has been committed. In particular, constant creation happens
 immediately, and is available to all users of the KB, though those constants
 will still be removed in the event of a transaction rollback.  If the CycClient connection is interrupted
 before the commit, the assertions will be correctly rolled back, but constants created during the 
 transaction may not be removed.
 <p>
 * Because the SimpleKBTransaction has the flexibility to decide to wait to perform KB
 * operations until commit, commit operations may take a very long time for
 * large transactions. If this presents problems, smaller transactions should be
 * used.
 * <p>
 * Note further that all parameters for assertions except for the sentence and
 * microtheory (e.g. creation templates, bookkeeping, transcripting, etc.) must
 * be the same during an entire transaction. If different values are used within
 * a single transaction, an IllegalArgumentException will be thrown.
 * <p>
 * Removing and then later re-asserting the same assertion is not supported in a
 * single SimpleKBTransaction.  Attempts to do so will trigger an UnsupportedOperationException. 
 * @author daves
 */
public class SimpleKBTransaction implements KBTransaction {
    
    private Boolean bookkeeping;
    private Boolean transcripting;
    private List<Fort> templates;
    private Boolean wffCheckingDisabled;
    private boolean isTemplateInitialized = false;
    private List<AssertionData> assertInfo = new ArrayList<AssertionData>();
    private List<AssertionData> unassertInfo = new ArrayList<AssertionData>();
    private List<Fort> createdForts = new ArrayList<Fort>();
    private static final String AND = CommonConstants.AND.cyclify();
    private static final String IST = CommonConstants.IST.cyclify();

    @Override
    public void begin() {
        if (CycClient.getCurrentTransaction() != null) {
            throw new IllegalStateException("Attempt to begin a KBTransaction while another KBTransaction is active.");
        }
        CycClient.setCurrentTransaction(this);
    }
    
    @Override
    public void commit() {
        try {
            ELMt mt = null;

            try {
                //since all the literals in the assert and unassert sentence will be decontextualized, the value we use for the Mt is irrelevant.
                mt = CommonConstants.BASE_KB;
                if (assertInfo.isEmpty() && unassertInfo.isEmpty()) {
                    return; //there's nothing to do.  If we eventually start doing something with constants during the commit, we'd need to do the right thing here.
                }
                StringBuilder assertConjunction = new StringBuilder().append("(list " + AND + " ");
                for (AssertionData assertData : assertInfo) {
                    assertConjunction.append(assertData.toIstString());
                }
                assertConjunction.append(")");

                StringBuilder unassertConjunction = new StringBuilder().append("(list " + AND + " ");
                for (AssertionData unassertData : unassertInfo) {
                    unassertConjunction.append(unassertData.toIstString());
                }
                unassertConjunction.append(")");
                CycClientManager.getCurrentClient().getAssertTool().edit(unassertConjunction.toString(), assertConjunction.toString(), mt, bookkeeping, transcripting, wffCheckingDisabled, templates);
            } catch (Exception ex) {
//                Logger.getLogger(SimpleKBTransaction.class.getName()).log(Level.SEVERE, null, ex);
                throw new BaseClientRuntimeException(ex.getClass().getName() + " while trying to commit transaction: " + ex.getMessage(), ex);
            }
        } finally {
            CycClient.setCurrentTransaction(null);
        }
    }

    @Override
    public void rollback() {
        for (Fort createdFort : createdForts) {
            try {
                CycClientManager.getCurrentClient().getUnassertTool().kill(createdFort, isBookkeeping(), isTranscripting());
            } catch (Exception ex) {
                throw new BaseClientRuntimeException(ex);
            }
        }
    }

    /**
     * @return whether or not bookkeeping should be performed for KB operations that occur inside the transaction.
     */
    public boolean isBookkeeping() {
        return bookkeeping;
    }

    /**
     * @param bookkeeping should bookkeeping be performed for KB operations inside this transaction?
     */
    public void setBookkeeping(boolean bookkeeping) {
        if (this.bookkeeping != null) {
            throw new UnsupportedOperationException("Illegal attempt to change bookkeeping on a transaction from " + this.bookkeeping + " to " + bookkeeping);
        }
        this.bookkeeping = bookkeeping;
    }

    /**
     * @return whether or not bookkeeping should be performed for KB operations that occur inside the transaction.
     */
    public boolean isTranscripting() {
        return transcripting;
    }

    /**
     * @param transcripting should transcripting be performed for KB operations inside this transaction?
     */
    public void setTranscripting(boolean transcripting) {
        if (this.transcripting != null) {
            throw new UnsupportedOperationException("Illegal attempt to change transcripting on a transaction from " + this.transcripting + " to " + transcripting);
        }
        this.transcripting = transcripting;
    }

    /**
     * @return the creation-templates specified for this transaction
     */
    public List<Fort> getTemplates() {
        return new ArrayList<Fort>(templates);
    }

    
    /**
     * @return the terms created inside this transaction.
     */
    protected List<Fort> getCreatedTerms() {
        return new ArrayList<Fort>(createdForts);
    }
    /**
     * 
     * @return information about each assertion in the scope of this transaction
     */
    protected List<AssertionData> getAssertInfo() {
        return new ArrayList<AssertionData>(assertInfo);
    }
    
    /**
     * 
     * @return information about each unassert in the scope of this transaction
     */
    protected List<AssertionData> getUnassertInfo() {
        return new ArrayList<AssertionData>(assertInfo);
    }

    /**
     * Note on the transaction with creation templates should be used.
     * @param templates  the templates to set
     */
    public void setTemplates(List<Fort> templates) {
        if (isTemplateInitialized == true && !this.templates.equals(templates)) {
            throw new UnsupportedOperationException("Illegal attempt to change the assert template on a transaction from " + this.templates + " to " + templates);
        }
        this.templates = new ArrayList<Fort>(templates);
        isTemplateInitialized = true;
    }
    
    
    @Override
    public void noteForAssertion(String sentence, ELMt mt, boolean bookkeeping, boolean transcripting, boolean wffCheckingDisabled, List<Fort>templates) {
        noteBookkeeping(bookkeeping);
        noteTranscripting(transcripting);
        noteWffCheckingDisabled(wffCheckingDisabled);
        noteTemplates(templates);
        AssertionData assertData = new AssertionData(sentence, mt);
        if (unassertInfo.contains(assertData)) {
            throw new UnsupportedOperationException("Illegal attempt to assert " + assertData + " in a transaction where it's also been unasserted.");
        }
        assertInfo.add(assertData);
    }

    @Override
    public void noteForUnassertion(String sentence, ELMt mt, boolean bookkeeping, boolean transcripting) {
        noteBookkeeping(bookkeeping);
        noteTranscripting(transcripting);
        unassertInfo.add(new AssertionData(sentence, mt));
    }
    
    @Override
    public void noteCreation(Fort fort) {
        createdForts.add(fort);
    }

    private void noteBookkeeping (boolean bookkeeping) {
        if (this.bookkeeping == null) {
           this.bookkeeping = bookkeeping;
        } else if (!this.bookkeeping.equals(bookkeeping)) {
            throw new IllegalArgumentException("Illegal attempt to change the value for bookkeeping in this transaction from " + this.bookkeeping + " to " + bookkeeping + ".");
        }
    }        

    private void noteTranscripting (boolean transcripting) {
        if (this.transcripting == null) {
           this.transcripting = transcripting;
        } else if (!this.transcripting.equals(transcripting)) {
            throw new IllegalArgumentException("Illegal attempt to change the value for transcripting in this transaction from " + this.transcripting + " to " + transcripting + ".");
        }
    }        

    private void noteWffCheckingDisabled (boolean wffCheckingDisabled) {
        if (this.wffCheckingDisabled == null) {
           this.wffCheckingDisabled = wffCheckingDisabled;
        } else if (!this.wffCheckingDisabled.equals(wffCheckingDisabled)) {
            throw new IllegalArgumentException("Illegal attempt to change the value for wffCheckingDisabled in this transaction from " + this.wffCheckingDisabled + " to " + wffCheckingDisabled + ".");
        }
    }        

    private void noteTemplates(List<Fort> templates) {
        if (isTemplateInitialized == false) {
            this.templates = new ArrayList<Fort>(templates);
            isTemplateInitialized = true;        
        } else if (!this.templates.equals(templates)) {
            throw new IllegalArgumentException("Illegal attempt to change the value for templates in this transaction from " + this.templates + " to " + templates + ".");
        }
    }
    
    public class AssertionData {

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.sentence != null ? this.sentence.hashCode() : 0);
            hash = 29 * hash + (this.mt != null ? this.mt.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AssertionData other = (AssertionData) obj;
            if ((this.sentence == null) ? (other.sentence != null) : !this.sentence.equals(other.sentence)) {
                return false;
            }
            if (this.mt != other.mt && (this.mt == null || !this.mt.equals(other.mt))) {
                return false;
            }
            return true;
        }
        
        private String sentence;
        private ELMt mt;
        AssertionData(String sentence, ELMt mt) {            
            assert sentence != null && !sentence.isEmpty();
            this.sentence = sentence;
            this.mt = mt;
        }
        
        public String toIstString() {
            return "(list " + IST + " " + mt.stringApiValue() + " " + sentence + ")";
        }
    }

}
