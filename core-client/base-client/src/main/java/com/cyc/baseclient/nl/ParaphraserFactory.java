package com.cyc.baseclient.nl;

/*
 * #%L
 * File: ParaphraserFactory.java
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

import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.ElMt;
import com.cyc.kb.Context;
import com.cyc.nl.GenerationMode;
import com.cyc.nl.NlForce;
import com.cyc.nl.NlGenerationParams;
import com.cyc.nl.Paraphrase;
import com.cyc.nl.Paraphraser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface for generating structured paraphrases of terms.
 *
 * ParaphraserFactory objects provide the ability to generate natural language strings
 for CycL objects (e.g. sentences and terms).  The basic ParaphraserFactory implementation
 available from baseclient will provide very basic paraphrasing functionality.  If the NL API
 is available on the classpath, the <code>getInstance</code> methods of this class will return a paraphraser
 * that is more flexible.  The <code>BasicParaphraser</code> (returned if only the baseclient
 * is available) is compatible with all versions of Cyc (including OpenCyc).  
 * @author baxter
 * @param <E>
 */

public abstract class ParaphraserFactory<E> {

  private static final Map<String, Boolean> classAvailability = new HashMap<String, Boolean>();
    
  static public Paraphraser getInstance(final ParaphrasableType type) {
    //this should only check once to see if these other classes are available...
    //if they're not there, it needs to give back some kind of stupid paraphrase that doesn't work well...
    try {
      switch (type) {
        case QUERY:
          if (isClassAvailable("com.cyc.nl.QueryParaphraser")) {
            return (Paraphraser) Class.forName("com.cyc.nl.QueryParaphraser").newInstance(); //new QueryParaphraser
          } else {
            return new BasicParaphraser();
          }
        //new QueryParaphraser
        case FORMULA:
           if (isClassAvailable("com.cyc.nl.FormulaParaphraser")) {
             return (Paraphraser) Class.forName("com.cyc.nl.FormulaParaphraser").newInstance();
             } else {
            return new BasicParaphraser();
          }
        case KBOBJECT:
           if (isClassAvailable("com.cyc.nl.KbObjectParaphraser")) {
             return (Paraphraser)Class.forName("com.cyc.nl.KbObjectParaphraser").newInstance();
             } else {
            return new BasicParaphraser();
          }
        case DEFAULT:
           if (isClassAvailable("com.cyc.nl.DefaultParaphraser")) {
             return (Paraphraser) Class.forName("com.cyc.nl.DefaultParaphraser").newInstance();
             } else {
            return new BasicParaphraser();
          }
      }
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(ParaphraserFactory.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(ParaphraserFactory.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(ParaphraserFactory.class.getName()).log(Level.SEVERE, null, ex);
    }
    throw new UnsupportedOperationException("Unable to produce a Paraphraser for " + type);
  }
  

  private static boolean isClassAvailable(String binaryClassName) {
    if (!classAvailability.containsKey(binaryClassName)) {
      try {
        Class clazz = ParaphraserFactory.class.getClassLoader().loadClass(binaryClassName);
        classAvailability.put(binaryClassName, true);
      } catch (java.lang.ClassNotFoundException ex) {
        classAvailability.put(binaryClassName, false);
      }
    }
    return classAvailability.get(binaryClassName);
  }
  
  public enum ParaphrasableType {
    QUERY, FORMULA, KBOBJECT, DEFAULT;
  }
  
  private static class BasicParaphraser<E> implements Paraphraser {

    public Paraphrase paraphrase(Object object) {
      return new ParaphraseImpl(object.toString(), object);
    }

    public List<? extends Paraphrase<E>> paraphraseWithDisambiguation(List<E> objects)
    throws CycConnectionException {
      List<Paraphrase<E>> result = new ArrayList<Paraphrase<E>>();
      for (E obj : objects) {
        result.add(paraphrase(obj));
      }
      return result;
    }

    public void setDomainMt(ElMt mt) {
    }

    @Override
    public NlGenerationParams getParams() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParams(NlGenerationParams params) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setForce(NlForce nlForce) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setBlanksForVars(boolean b) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Context getDomainContext() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NlForce getForce() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Context getLanguageContext() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GenerationMode getMode() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getNlPreds() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDomainContext(Context ctx) {
     //don't do anything.  BasicParaphaser doesn't care about this.
    }
  
};
  
}
