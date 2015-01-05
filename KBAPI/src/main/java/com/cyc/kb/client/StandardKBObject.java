package com.cyc.kb.client;

/*
 * #%L
 * File: StandardKBObject.java
 * Project: KB API
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

import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.kbtool.InspectorTool;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBStatus;
import com.cyc.kb.KBAPIEnums.LookupType;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBApiServerSideException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeConflictException;
import com.cyc.kb.exception.KBTypeException;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract common supertype of several classes that share initialization code.
 * The class and the methods of this class are not part of the KB API.
 * 
 * @author David Baxter
 * @todo DaveS review Documentation
 */
abstract class StandardKBObject extends KBObjectImpl {

  private static final Logger log = LoggerFactory.getLogger(StandardKBObject.class.getCanonicalName());
  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   * <p>
   * 
   * @throws KBApiRuntimeException
   *           if there is a problem connecting to Cyc.
   */
  StandardKBObject() {
    super();
  }

  // We will either use HLID or cycObjString. There is no point using both.
  /*
   * StandardKBObject(String cycObjStr, String hlid) throws KBApiException {
   * super(cycObjStr, hlid); try { if (!isValidCore(core)) { String msg =
   * "The term \"" + core.toString() + "\" is not a " + getTypeString() + ".";
   * log.fine(msg); throw new KBTypeException(msg); } } catch (Exception ex) {
   * throw new KBApiException(ex); } }
   */

  /**
   * This not part of the public, supported KB API. An implementation-dependent constructor.
   * 
   * It is only called by the subclasses, when they need to build wrap an existing
   * CycObject, usually as a result of a Query.
   * 
   * 
   * @param cycObject the object to be wrapped 
   * 
   * @throws KBTypeException  if the object (which already exists) is not of the 
   * appropriate type
   */
  StandardKBObject(CycObject cycObject) throws KBTypeException {
    this();
    setCore(cycObject);
  }

  /**
   * !!!EXPERIMENTAL!!!
   * 
   * @param nameOrId
   * @param l
   * @throws KBTypeException
   * @throws CreateException
   */
  StandardKBObject(String nameOrId, List<Object> l) throws KBTypeException, CreateException {
    this();
    setCore(nameOrId);
    this.quantification = l;
  }

  /**
   * This not part of the public, supported KB API. This is called by subclasses to find or create 
   * an object of their individual types, represented by <code>nameOrId</code> in the underlying KB
   * 
   * @param nameOrId  the string representation of the candidate object or its HLID
   * 
   * @throws KBTypeException  if the object is not of the appropriate type
   * @throws CreateException  if the object can't be created for any reason
   */
  StandardKBObject(String nameOrId) throws KBTypeException, CreateException  {
    this();
    setCore(nameOrId);
  }

  /**
   * This not part of the public, supported KB API. This is called by the subclasses to finds or creates; 
   * or finds an object of their individual types, represented by <code>nameOrId</code> in 
   * the underlying KB based on input <code>lookup</code>
   * 
   * @param nameOrId  the string representation of the candidate object or its HLID
   * @param lookup  the type of lookup to perform 
   * 
   * @throws KBTypeException  if the object is not of the appropriate type
   * @throws CreateException  if the object can't be created for any reason
   */
  StandardKBObject(String nameOrId, LookupType lookup) throws KBTypeException, CreateException  {
    this();
    setCore(nameOrId, lookup);
  }


  /**
   * This not part of the public, supported KB API. Check that the candidate core
   * object is valid for this type. This is part of the bootstrapping process,
   * where we check the core before we build a KB Object. At this point, we can 
   * not use any KB API methods or {@link #getCore()}.
   * 
   * This checks the validity of only KBTerm subclasses. The core validity for 
   * Assertion and its subclasses, Sentence, Variable and
   * Symbol are handled by the individual classes
   * 
   * If a Variable is wrapped by any subclass of KBTerm, then it is
   * always considered valid core. This is to allow representing a
   * #$CycLDenotationalTerm and #$CycLVariable of a given type
   * (#$Collection) using same KBTerm subclass.
   * 
   * @param cycObject
   *          the object that is checked for type given by getTypeString()
   * 
   * @return if a cycObject is of a given type
   * 
   * @throws KBApiRuntimeException
   *           if there is a problem connecting to Cyc
   * @throws KBApiServerSideException
   *           if there is problem executing a SubL command
   * 
   * NOTE: This does not throw KBTypeException just because there is a
   *           server error.
   */
  protected boolean isValidCore(CycObject cycObject) throws KBApiRuntimeException,
      KBApiServerSideException {
    try {
      if (cycObject instanceof CycVariable) {
        return true;
      } else {
        final InspectorTool inspectorTool = getAccess().getInspectorTool();
        return inspectorTool.isa(cycObject, getTypeString());
      }
    } catch (CycApiException e) {
      throw new KBApiServerSideException(e.getMessage(), e);
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
  }

  /**
   * The method attempts to find or create a Cyc KB Object based on <code>nameOrIdOrVar</code>.
   * If the string starts with "?" then a Variable is created and assigned to the core.
   * 
   * @param nameOrIdOrVar  the string representation of the candidate object or its HLID, or
   * a variable name.
   * 
   * @throws KBTypeException  if the object is found but is not of the right type
   * @throws CreateException  if the object can't be created for any reason
   * 
   * @see #setCore(java.lang.String, com.cyc.kb.KBAPIEnums.LookupType) for more detailed comments
   */
  private void setCore(String nameOrIdOrVar) throws KBTypeException, CreateException {
    if (nameOrIdOrVar.startsWith("?")) {
      // TODO: Should we check in the cache to avoid variable name clash?
      // There is an argument to be make to not cache these. Unless used
      // in the same sentence, there is no reason why variable names should not
      // clash. 
      // The KB.Sentence might have to check if the there is a name collision. 
      setCore(new CycVariableImpl(nameOrIdOrVar));
    } else {
      setCore(nameOrIdOrVar, LookupType.FIND_OR_CREATE);
    }
  }

  /**
   * The method tries to initially find an Cyc KB object assuming the string <code>nameOrIdOrVar</code>
   * is a HLID. If unsuccessful, it attempts to {@link LookupType#FIND} or {@link LookupType#FIND_OR_CREATE}
   * based on <code>lookup</code>.
   * 
   * If the underlying object can be made into an instance of a type given by {@link #getType()}
   * then it will be made otherwise {@link KBTypeConflictException} will be thrown
   * 
   * @param nameOrIdOrVar  the string representation of the candidate object or its HLID 
   * @param lookup  find or create the candidate object
   * 
   * @throws KBTypeException  if an object is found but is not of the right type
   * @throws KBTypeConflictException if an object is found but couldn't be converted to right type.
   * This is not explicitly thrown. But documented so users can check for it. 
   * 
   * @throws CreateException  if an object can't be created for any reason
   * @throws KBObjectNotFoundException  if an object is not found when {@link LookupType#FIND} is used
   * @throws InvalidNameException if given an invalid nameOrId field and could not create such an object
   */
  private void setCore(String nameOrIdOrVar, LookupType lookup) throws KBTypeException, CreateException {
    if (nameOrIdOrVar.startsWith("?")) {
      // TODO: Should we check in the cache to avoid variable name clash?
      setCore(new CycVariableImpl(nameOrIdOrVar));
      return;
    }

    CycObject tempCore = null;
    try {
      Object o = null;
      try {
        o = DefaultCycObject.fromPossibleCompactExternalId(nameOrIdOrVar,
            getAccess());
      } catch (Exception ex) {
        // do nothing--this wasn't an hlid.
      }
      if (o instanceof CycObject) {
        tempCore = (CycObject) o;
      } else {
        String cyclifiedStr = getAccess().cyclifyString(nameOrIdOrVar);
        tempCore = null;
        try {
          tempCore = getTempCore(cyclifiedStr);

        } catch (Exception e) {
          if (lookup.equals(LookupType.FIND)) {
            String msg = "The term '" + nameOrIdOrVar + "' was not found.";
            log.trace(msg);
            throw new KBObjectNotFoundException(msg, e);
          } else if (lookup.equals(LookupType.FIND_OR_CREATE)) {
            if (cyclifiedStr.charAt(0) == '(') {
              // NART creation check if the operator (first
              // argument) is reifiable.
              // If not, it throws an exception.
              tempCore = getAccess().getObjectTool().makeCycNaut(cyclifiedStr);
            } else {
              try {
                tempCore = makeCycConstant(cyclifiedStr);
              } catch (CycApiException ex) {
                if (ex.getMessage().contains("Cannot create new constant")) {
                  throw new InvalidNameException(
                      "Unable to make a constant with the name '" + nameOrIdOrVar + "'.",
                      ex);
                } else {
                  throw ex;
                }
              }
              String factString = "(#$isa " + tempCore.stringApiValue() + " "
                  + getTypeString() + ")";
              AssertionImpl.assertSentence(factString, "#$UniversalVocabularyMt",
                  null, null);
            }
          }
        }
      }
      if (tempCore != null && isValidCore(tempCore)) {
        core = tempCore;
      } else {

        KBStatus status = KBObjectFactory.getStatus(nameOrIdOrVar, this.getClass());
        if (status == KBStatus.EXISTS_WITH_COMPATIBLE_TYPE
            && lookup == LookupType.FIND_OR_CREATE) {
          AssertionImpl.assertSentence("(#$isa " + tempCore.cyclify() + " "
              + getTypeString() + ")", "#$UniversalVocabularyMt", null, null);
          // @todo where should this really be asserted???
          core = tempCore;
        } else if (status == KBStatus.EXISTS_WITH_COMPATIBLE_TYPE) {
          throw new KBTypeException(tempCore + " is not a " + this.getClass());
        } else if (status == KBStatus.EXISTS_AS_TYPE) {
          core = tempCore;
        } else if (status == KBStatus.EXISTS_WITH_TYPE_CONFLICT) {
          throw new KBTypeConflictException("Unable to convert "
              + tempCore.cyclify() + " to a " + this.getClass());
        } else {
          String msg = "The term '" + nameOrIdOrVar + "' is not a " + getTypeString()
              + ".";
          log.trace(msg);
          throw new KBTypeException(msg);
        }
      }
    } catch (KBTypeException te) {
      throw te;
    } catch (KBObjectNotFoundException onfe) {
      throw onfe;
    } catch (InvalidNameException ine) {
      throw ine;
    } catch (Exception e) {
      throw new CreateException("Failed to create new " + getTypeString()
          + " named " + nameOrIdOrVar, e);
    }
  }

  /**
   * Creates a term and reifies it based on cyclifiedStr
   * 
   * @param cyclifiedStr  the string to be converted to a CycObject 
   * 
   * @throws KBApiRuntimeException if IOException is thrown by CycAPI
   */
  private CycObject getTempCore(String cyclifiedStr) throws KBApiRuntimeException {
    CycObject tempCore;
    try {
      try {
        tempCore = getAccess().getLookupTool().getTermByName(cyclifiedStr);
      } catch (CycApiException e) {
        tempCore = CycFormulaSentence.makeCycSentence(getAccess(), cyclifiedStr);
      }
      if (tempCore instanceof Nart) {
        ((Nart) tempCore).ensureReified(getAccess());
      }
      return tempCore;
    } catch (CycConnectionException ioe) {
      throw new KBApiRuntimeException(ioe.getMessage(), ioe);
    }
  }

  /**
   * Checks if a given cycObject is a valid core for a class type given by
   * getTypeString() and sets the core.
   * 
   * @param cycObject the object to be checked and assigned
   * 
   * @throws KBTypeException if cycObject does not satisfy the type
   */
  final void setCore(CycObject cycObject) throws KBTypeException {
    if (isValidCore(cycObject)) {
      core = cycObject;
    } else {
      String msg = "The term \"" + cycObject.toString() + "\" is not a "
          + getTypeString() + ".";
      log.trace(msg);
      throw new KBTypeException(msg);
    }
  }

  /** 
   * !!!EXPERIMENTAL METHOD!!!
   * 
   * Shallow copy of the KB Object without any validation of the core using
   * isValidCore for a given type.
   * 
   * This should only be used in copy constructors for KBObject subclasses. The copy
   * constructors are used to provide a way for higher level APIs to construct
   * subclass objects using super class objects, when appropriate.
   */
  final void setCore (KBObject kbObject){
    //if (kbObject.getClass().isAssignableFrom(this.getClass())){
      core = kbObject.getCore();
    //} else {
    //  throw new KBApiRuntimeException ("Incompatible types in copy constructor.");
    //}
  }
  
  private CycConstant makeCycConstant(String cyclifiedIndStr) {
    try {
      return getAccess().getObjectTool().makeCycConstant(cyclifiedIndStr, true,
          KBAPIConfiguration.getShouldTranscriptOperations());
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
  }
}
