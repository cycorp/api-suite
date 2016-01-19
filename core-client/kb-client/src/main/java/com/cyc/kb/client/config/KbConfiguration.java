package com.cyc.kb.client.config;

/*
 * #%L
 * File: KbConfiguration.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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

import com.cyc.kb.DefaultContext;
import com.cyc.base.cycobject.Fort;
import com.cyc.baseclient.CycClientOptions;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.session.CycSessionManager;
import com.cyc.session.exception.SessionException;


/**
 * A class to hold the configuration parameters such as, transcript flag, current cyclist 
 * and current project. 
 * 
 * A cleaner configuration project is being worked on. This class will be deprecated 
 * by that in the next few months.
 * @author vijay
 * 
 * 
 */
public class KbConfiguration {

  public static CycClientOptions getOptions() {
    try {
      return (CycClientOptions) CycSessionManager.getCurrentSession().getOptions();
    } catch (SessionException ex) {
      throw new KbRuntimeException(ex);
    }
  }
  
  /* *
   * Gates whether KB operations are transcripted.
   * /
  private static ThreadLocal<Boolean> shouldTranscriptOperations =
          new ThreadLocal<Boolean>() {
    @Override
    protected Boolean initialValue() {
      return true;
    }
  };
  
  static private CycAccess getStaticAccess() {
    try {
      return CycAccessManager.getCurrentAccess();
    } catch (SessionApiException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    }
  }
  
  /* *
   * Sets an initial default context for assertions and queries.
   * 
   * Initially, the default contexts are:
   *  forAssertion: null
   *  forQuery: null
   * These are set to null values to encourage developers to set the defaults
   * explicitly.
   * 
   * If the program does not have an application specific default, the following
   * could be used.
   * 	forAssertion: #$UniversalVocabularyMt
   * 	forQuery: #$InferencePSC
   * /
  private static ThreadLocal<DefaultContext> defaultContext =
		  new ThreadLocal<DefaultContext>() {
	  @Override
	  protected DefaultContext initialValue() {
		  // return new KBAPIDefaultContext(Constants.uvMt(), Constants.inferencePSCMt());
		  return new KBAPIDefaultContext(null, null);
	  }
  };
  
  /* *
   * Sets the default context ThreadLocal
   * @param defaultCtxs
   * /
  public static void setDefaultContext(DefaultContext defaultCtxs) {
	  defaultContext.set(defaultCtxs);
  }
  */
  
  /**
   * Returns the current default contexts
   * @return the contents of the DefaultContest ThreadLocal
   */
  public static DefaultContext getDefaultContext() {
    return getOptions().getDefaultContext();
  }

  /* *
   * Declare that KB operations performed in this thread should or shouldn't be
   * transcripted by the Cyc server.
   * 
   * @param b flag to control the transcription of the operations
   * /
  public static void setShouldTranscriptOperations(final boolean b) {
    shouldTranscriptOperations.set(b);
  }
  */
  
  /**
   * Will actions in the current thread that modify the KB be transcripted by
   * the Cyc server?
   *
   * @return will KB operations from the current thread be transcripted?
   */
  public static boolean getShouldTranscriptOperations() {
    //return shouldTranscriptOperations.get();
    return getOptions().getShouldTranscriptOperations();
  }

  /**
   * The Cyclist or the program running on behalf of the cyclist generating the
   * operations in this thread. Return null if not set of if there is any exception.
   * 
   * @return the cyclist or the program running on behalf of a cyclist, that is
   * generating the operations in this thread
   */
  @SuppressWarnings("deprecation")
  public static KbIndividual getCurrentCyclist() {
    // Since the KB API does not provide any method to set Cyclist per CycAccess,
    // boldly assuming that what we return is always the thread version of Cyclist.
    try {
      //Fort cyclist = getStaticAccess().getOptions().getCyclist();
      Fort cyclist = getOptions().getCyclist();
      if (cyclist == null) {
        return null;
      }
      return KbIndividualImpl.get(cyclist);
    } catch (Exception e) {
      return null;
    }
  }
  
  /* *
   * Declare that KB operations performed in this thread were done by the user 
   * <code>cyclist</code>.
   * The user could be an individual #$Cyclist or an instance of #$ComputerUser, 
   * such as, (UserOfProgramFn CycBrowser CAEUser). Applications written on top 
   * of KB API should login an user and create an instance of UserOfProgramFn, based
   * on the application and the user.
   * This will set #$myCreator for constants and :ASSERT-INFO for assertions
   * 
   * @param cyclist the person or program creating constants and assertions
   * 
   * @return the back cyclist set
   * /  
  public static KBIndividual setCurrentCyclist(KBIndividualImpl cyclist) {
    getStaticAccess().getOptions().setCyclist((Fort)cyclist.getCore());
    return cyclist;
  }
    
  /* *
   * Declare that KB operations performed in this thread were done by the user 
   * <code>cyclistStr</code>.
   * The user could be an individual #$Cyclist or an instance of #$ComputerUser, 
   * such as, (UserOfProgramFn CycBrowser CAEUser). Applications written on top 
   * of KB API should login an user and create an instance of UserOfProgramFn, based
   * on the application and the user. 
   * This will set #$myCreator for constants and :ASSERT-INFO for assertions
   * @param cyclistStr  string representation of the cyclist creating constants
   * and assertions
   * @return the Cyclist created based on cyclistStr
   * /
  @SuppressWarnings("deprecation")
  public static KBIndividual setCurrentCyclist(String cyclistStr){
    try {
      String cyclist = getStaticAccess().cyclifyString(cyclistStr);
      getStaticAccess().getOptions().setCyclistName(cyclist);
      return KBIndividualImpl.get(cyclist);
    } catch (SessionApiException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    } catch (CycApiException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    } catch (KBTypeException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    } catch (CreateException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    }
  }

  /* *
   * Declare that the KB operations performed in this thread are related to the specified
   * project. The project should be an instance of #$Cyc-BasedProject. Applications written
   * on top of KB API should set the purpose on their own or by asking the user at login time.
   * 
   * Sets the #$myCreationPurpose for constants and :ASSERT-INFO for assertions.
   * @param project an instance of #$Cyc-BasedProject
   * @throws Exception 
   * /
  public static void setProject (KBIndividual project) throws Exception {
    getStaticAccess().getOptions().setKePurpose((Fort)project.getCore());
  }
  
  /* *
   * Get the project that the KB operations performed in this thread are related to. 
   * The project will be an instance of #$Cyc-BasedProject. Return null if unknown
   * or if there is any exception.
   * 
   * @return the project related to the current run of the program
   * /  
  @SuppressWarnings("deprecation")
  public KBIndividual getProject() {
    try {
      Fort purpose = getStaticAccess().getOptions().getKePurpose();
      if (purpose == null){
        return null;
      }
      return KBIndividualImpl.get(purpose);
    } catch (Exception e) {
      return null;
    }
  }
  */
}
