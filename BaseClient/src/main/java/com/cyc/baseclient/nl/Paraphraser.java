package com.cyc.baseclient.nl;

/*
 * #%L
 * File: Paraphraser.java
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

import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.ELMt;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface for generating structured paraphrases of terms.
 *
 * Paraphraser objects provide the ability to generate natural language strings
 * for CycL objects (e.g. sentences and terms).  The basic Paraphraser implementation
 * available from baseclient will provide very basic paraphrasing functionality.  If the NL API
 * is available on the classpath, the <code>getInstance</code> methods of this class will return a paraphraser
 * that is more flexible.  The <code>BasicParaphraser</code> (returned if only the baseclient
 * is available) is compatible with all versions of Cyc (including OpenCyc).  
 * @author baxter
 */

public abstract class Paraphraser<E> {

  private static Map<String, Boolean> classAvailability = new HashMap<String, Boolean>();
  
  /**
   * Returns a paraphrase of the specified object.
   *
   * @param object an object to paraphrase.
   * @return a paraphrase of the specified object.
   * @throws com.cyc.base.CycConnectionException
   */
 public abstract Paraphrase<? extends E> paraphrase(final E object) throws CycConnectionException;

  /**
   * Returns a list of paraphrases of the specified objects, attempting to minimize
   * ambiguity.
   *
   * @param objects
   * @return list of paraphrases
   * @throws com.cyc.base.CycConnectionException
   */
 public abstract List<? extends Paraphrase<E>> paraphraseWithDisambiguation(List<E> objects) throws CycConnectionException;

 abstract public void setDomainMt(ELMt mt);

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
           if (isClassAvailable("com.cyc.nl.KBObjectParaphraser")) {
             return (Paraphraser) Class.forName("com.cyc.nl.KBObjectParaphraser").newInstance();
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
      Logger.getLogger(Paraphraser.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(Paraphraser.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(Paraphraser.class.getName()).log(Level.SEVERE, null, ex);
    }
    throw new UnsupportedOperationException("Unable to produce a Paraphraser for " + type);
  }
  

  private static boolean isClassAvailable(String binaryClassName) {
    if (!classAvailability.containsKey(binaryClassName)) {
        URL url = Paraphraser.class.getClassLoader().getResource(binaryClassName);
        if (url instanceof URL) {
        classAvailability.put(binaryClassName, true);
        } else {
        classAvailability.put(binaryClassName, false);          
        }

    }
    return classAvailability.get(binaryClassName);
  }
  
  public enum ParaphrasableType {
    QUERY, FORMULA, KBOBJECT, DEFAULT;
  }
  
  private static class BasicParaphraser<E> extends Paraphraser<E> {

    @Override
    public Paraphrase<E> paraphrase(E object) throws CycConnectionException {
      return new Paraphrase<E>(object.toString(), object);
    }

    @Override
    public List<? extends Paraphrase<E>> paraphraseWithDisambiguation(List<E> objects)
    throws CycConnectionException {
      List<Paraphrase<E>> result = new ArrayList<Paraphrase<E>>();
      for (E obj : objects) {
        result.add(paraphrase(obj));
      }
      return result;
    }

    @Override
    public void setDomainMt(ELMt mt) {
    }
  
};
  
}
