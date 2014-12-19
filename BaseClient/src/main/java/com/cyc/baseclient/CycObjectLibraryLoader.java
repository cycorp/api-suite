package com.cyc.baseclient;

/*
 * #%L
 * File: CycObjectLibraryLoader.java
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
import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.Formula;
import com.cyc.base.kbtool.InspectorTool;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cyc.base.annotation.CycFormula;
import com.cyc.base.annotation.CycTerm;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * This class locates and loads classes annotated as
 * {@link com.cyc.base.annotation.CycObjectLibrary}, and provides methods to 
 * access their fields. This is useful for verifying that a Cyc server's KB 
 * meets your code's requirements.
 * 
 * @author nwinant
 */
public class CycObjectLibraryLoader {

  // Constructor
  
  protected CycObjectLibraryLoader() {
    logger = LoggerFactory.getLogger(this.getClass().getName());
  }

  /**
   * Returns the singleton instance of CycObjectLibraryLoader.
   * @return singleton instance of CycObjectLibraryLoader
   */
  synchronized public static CycObjectLibraryLoader get() {
    if (ME == null) {
      ME = new CycObjectLibraryLoader();
    }
    return ME;
  }
  

  // Public
  
  /**
   * Returns all valid {@link java.lang.reflect.Field}s for a given class.
   * @param clazz
   * @return all valid {@link java.lang.reflect.Field}s for a given class
   */
  public Collection<Field> getAllFields(Class<?> clazz) {
    final Collection<Field> results = new ArrayList<Field>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if (isFieldValid(clazz, field)) {
        results.add(field);
      }
    }
    return results;
  }

  /**
   * Returns all classes annotated as 
   * {@link com.cyc.base.annotation.CycObjectLibrary}.
   * @return all classes annotated as {@link com.cyc.base.annotation.CycObjectLibrary}
   */
  public Collection<Class> getAllLibraries() {
    final Collection<Class> results = new ArrayList<Class>();
    for (Class clazz : ClassIndex.getAnnotated(CycObjectLibrary.class)) {
      results.add(clazz);
    }
    return results;
  }

  /**
   * Returns a list of the values for all valid fields on the class which
   * are instances of classFilter. 
   * Note that this method only returns public fields; 
   * private fields will not be returned.
   * 
   * @param <E>
   * @param libraryClass
   * @param classFilter
   * @return a list of the values for all valid fields on the class which
   * are instances of classFilter
   */
  public <E extends Object> Collection<E> getAllObjectsForClass(Class libraryClass, Class<E> classFilter, boolean includeOptional) {
    final Collection<E> results = new ArrayList<E>();
    final Collection<Field> fields = getAllFields(libraryClass);
    for (Field field : fields) {
      try {
        final CycFieldAnnotationParser ann = new CycFieldAnnotationParser(field);
        if (includeOptional || !ann.isOptional()) {
          final Object o = field.get(getLibraryInstance(libraryClass));
          if (classFilter.isInstance(o)) {
            results.add((E) o);
          }
        }
      } catch (Exception ex) {
        handleException(field, ex);
      }
    }
    return results;
  }
  
  public <E extends Object> Collection<E> getAllObjectsForClass(Class libraryClass, Class<E> classFilter) {
    return getAllObjectsForClass(libraryClass, classFilter, this.isIncludeOptionalFields());
  }
  
  /**
   * Returns a Collection of the values for all valid {@link CycObject} fields
   * on the class.
   * Note that this method only returns public fields; 
   * private fields will not be returned.
   * 
   * @param libraryClass
   * @return a Collection of the values for all valid {@link CycObject} fields
   */
  public Collection<CycObject> getAllCycObjectsForClass(Class libraryClass) {
    return getAllObjectsForClass(libraryClass, CycObject.class);
  }

  /**
   * Returns a Collection of the values for all valid fields which are instances of
   * {@link com.cyc.base.cycobject.CycObject} for all 
   * {@link com.cyc.base.annotation.CycObjectLibrary} classes.
   * Note that this method only returns public fields; 
   * private fields will not be returned.
   * 
   * @param <E>
   * @param classFilter
   * @return a Collection of the values for all valid fields which are instances of
   * {@link com.cyc.base.cycobject.CycObject} for all 
   * {@link com.cyc.base.annotation.CycObjectLibrary} classes
   */
  public <E extends Object> Collection<E> getAllObjects(Class<E> classFilter) {
    final Collection<E> results = new ArrayList<E>();
    for (Class lib : getAllLibraries()) {
      logger.warn("Loading library class " + lib.getName());
      results.addAll(getAllObjectsForClass(lib, classFilter));
    }
    return results;
  }

  /**
   * Returns a Collection of the values for all valid fields which are instances of
   * {@link com.cyc.base.cycobject.CycObject} for all 
   * {@link com.cyc.base.annotation.CycObjectLibrary} classes.
   * Note that this method only returns public fields; 
   * private fields will not be returned.
   * 
   * @return a Collection of the values for all valid fields which are instances of
   * {@link com.cyc.base.cycobject.CycObject} for all 
   * {@link com.cyc.base.annotation.CycObjectLibrary} classes
   */
  public Collection<CycObject> getAllCycObjects() {
    return getAllObjects(CycObject.class);
  }
  
  /**
   * Iterates over all of the valid fields on the specified class, passing
   * information about each to the supplied {@link CycLibraryFieldHandler}.
   * Useful for unit testing.
   * 
   * @param <E>
   * @param libraryClass
   * @param handler
   */
  public <E extends Object> void processAllFieldsForClass(Class<E> libraryClass, CycLibraryFieldHandler handler) {
    handler.onLibraryEvaluationBegin(libraryClass);
    Collection<Field> fields = getAllFields(libraryClass);
    for (Field field : fields) {
      try {
        processCycTerm(libraryClass, field, handler);
      } catch (Exception ex) {
        handler.onException(field, ex);
      }
    }
    handler.onLibraryEvaluationEnd(libraryClass);
  }
  
  /**
   * Iterates over a Collection of CycObjects, checks whether each is present in
   * a given KB, and returns a Collection of all the CycObjects which are 
   * <strong>not</strong> present in said KB.
   * 
   * @param <E>
   * @param objects
   * @param access
   * @return a Collection of all the CycObjects which are
   * <strong>not</strong> present in said KB
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  public <E extends CycObject> Collection<E> findMissingCycObjects(Collection<E> objects, CycAccess access)
          throws CycConnectionException, CycApiException {
    final Collection<E> results = new ArrayList<E>();
    for (E obj : objects) {
      if (!isCycObjectInKB(access, obj)) {
        results.add(obj);
      }
    }
    return results;
  }
  
  /**
   * Returns a Collection of the values for all valid fields which are instances
   * of classFilter (for all {@link com.cyc.base.annotation.CycObjectLibrary} 
   * classes) which are <strong>not</strong> present in the KB of the Cyc specified by the 
   * {@link CycAccess} object.
   * 
   * @param <E>
   * @param classFilter
   * @param access
   * @return a Collection of the values for all valid fields which are instances of classFilter (for
   * all {@link com.cyc.base.annotation.CycObjectLibrary} classes) which are <strong>not</strong>
   * present in the KB of the Cyc specified by the {@link CycAccess} object
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  public <E extends CycObject> Collection<E> findMissingCycObjects(Class<E> classFilter, CycAccess access)
          throws CycConnectionException, CycApiException {
    final Collection<E> objs = this.getAllObjects(classFilter);
    return findMissingCycObjects(objs, access);
  }
  
  /**
   * Returns a Collection of the {@link com.cyc.base.cycobject.CycObject}s for
   * all {@link com.cyc.base.annotation.CycObjectLibrary} classes which are <strong>not</strong>
   * present in the KB of the Cyc specified by the {@link CycAccess} object.
   * 
   * @param access
   * @return a Collection of the {@link com.cyc.base.cycobject.CycObject}s for
   * all {@link com.cyc.base.annotation.CycObjectLibrary} classes which are <strong>not</strong>
   * present in the KB of the Cyc specified by the {@link CycAccess} object
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  public Collection<CycObject> findMissingCycObjects(CycAccess access)
          throws CycConnectionException, CycApiException {
    return findMissingCycObjects(CycObject.class, access);
  }
  

  // Protected
  
  protected boolean isCycObjectInKB(CycAccess access, CycObject obj)
          throws CycConnectionException, CycApiException {
    final InspectorTool inspector = access.getInspectorTool();
    if (ELMt.class.isInstance(obj)) {
      return inspector.isELMtInKB((ELMt) obj);
    } else if (CycConstant.class.isInstance(obj)) {
      return inspector.isConstantInKB((CycConstant) obj);
    } else if (CycVariable.class.isInstance(obj)) {
      return true;
    } else if (Formula.class.isInstance(obj)) {
      return inspector.isFormulaWellFormed((Formula) obj, CommonConstants.EVERYTHING_PSC);
    }
    return false;
  }
  
  protected void processCycTerm(Class libraryClass, Field field, CycLibraryFieldHandler handler) throws IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
    final CycFieldAnnotationParser ann = new CycFieldAnnotationParser(field);    
    String expected = null;
    Boolean equivalent = null;
    final Object o = field.get(getLibraryInstance(libraryClass));
    final String value = getObjectCycLValue(o);
    //if (ann.isAnnotated() && !ann.isOptional()) {
    if (ann.isAnnotated()) {
      expected = ann.getCycl();
      equivalent = (expected != null) ? expected.equals(value) : (value == null);
    }
    handler.onFieldEvaluation(field, value, ann.isAnnotated(), expected, equivalent);
  }
  
  /**
   * Determines whether a field is suitable for inclusion. May be overridden
   * with more (or less) restrictive criteria.
   * 
   * @param field
   * @return true if a field is suitable for inclusion
   */
  protected boolean isFieldValid(Class libraryClass, Field field) {
    final CycFieldAnnotationParser ann = new CycFieldAnnotationParser(field);    
    final CycObjectLibrary lib = (CycObjectLibrary) libraryClass.getAnnotation(CycObjectLibrary.class);
    return Modifier.isFinal(field.getModifiers())
            && Modifier.isPublic(field.getModifiers())
            && (ann.isAnnotated() || !lib.requireFieldAnnotations());
  }
  
  protected Object getLibraryInstance(Class libraryClass) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final CycObjectLibrary lib = (CycObjectLibrary) libraryClass.getAnnotation(CycObjectLibrary.class);
    if ((lib.accessor() != null) && !lib.accessor().isEmpty()) {
      String accessorName = getSingletonAccessMethodName(libraryClass);
      try {
        final Method accessor = libraryClass.getMethod(accessorName);
        if (!Modifier.isStatic(accessor.getModifiers())) {
          throw new NoSuchMethodException();
        }
        final Object instance = accessor.invoke(null);
        if (instance == null) {
          throw new NoSuchMethodException();
        }
        if (!libraryClass.isInstance(instance)) {
          throw new NoSuchMethodException();
        }
        return instance;
      } catch (NoSuchMethodException nsme) {
        throw new NoSuchMethodException(CycObjectLibrary.class.getSimpleName() + " '" + libraryClass.getName() + "' is annotated as being a singleton, "
                + "but could not find static no-arg accessor named '" + accessorName + "' which returns a non-null instance of " + libraryClass.getSimpleName());
      }
    }
    return null;
  }
  
  protected String getObjectCycLValue(Object o) {
    if (CycObject.class.isInstance(o)) {
      return ((CycObject) o).cyclify();
    } else if (o != null) {
      return o.toString();
    }
    return null;
  }
  
  protected String getSingletonAccessMethodName(Class libraryClass) {
    final CycObjectLibrary lib = (CycObjectLibrary) libraryClass.getAnnotation(CycObjectLibrary.class);
    return lib.accessor();
  }
  
  /**
   * Handles Exceptions which occur when accessing fields.
   * This method merely logs & rethrows the exception, but may be overridden for 
   * custom behavior.
   * 
   * @param field
   * @param ex 
   */
  protected void handleException(Field field, Exception ex) {
    String msg = "Error accessing field " + field.getName();
    logger.error(msg, ex);
    throw new BaseClientRuntimeException(msg, ex);
  }
  
  protected boolean isIncludeOptionalFields() { return false; }
  
  
  // Internal
  
  /**
   * Interface for an object which handles the results of a 
   * {@link CycObjectLibraryLoader#processAllFieldsForClass(java.lang.Class, com.cyc.baseclient.CycObjectLibraryLoader.CycLibraryFieldHandler) CycObjectLibraryLoader#processAllFieldsForClass}
   */
  public interface CycLibraryFieldHandler {
    void onLibraryEvaluationBegin(Class libraryClass);
    void onFieldEvaluation(Field field, String cyclValue, boolean hasAnnotation, String expectedCycLValue, Boolean equivalent);
    void onException(Field field, Exception ex);
    void onLibraryEvaluationEnd(Class libraryClass);
  }
  
  /**
   * Wrapper for {@link CycTerm}, {@link CycFormula}, and any other related annotations a field might have.
   */
  public class CycFieldAnnotationParser {
    
    public CycFieldAnnotationParser(Field field) {
      this.field = field;
      if (field.getAnnotation(CycTerm.class) != null) {
        process(field.getAnnotation(CycTerm.class));
      } else if(field.getAnnotation(CycFormula.class) != null) {
        process(field.getAnnotation(CycFormula.class));
      }
    }
    
    final protected void process(CycTerm term) {
      this.ann = term;
      this.cycl = term.cycl();
      this.optional = term.optional();
    }
    
    final protected void process(CycFormula formula) {
      this.ann = formula;
      this.cycl = formula.cycl();
      this.optional = formula.optional();
    }
    
    public Field getField() {
      return field;
    }
    
    public Annotation getAnnotation() {
      return ann;
    }
    
    public boolean isAnnotated() {
      return getAnnotation() != null;
    }
    
    public String getCycl() {
      return cycl;
    }
    
    public boolean isOptional() {
      return optional;
    }
    
    final private Field field;
    private Annotation ann = null;
    private String cycl = null;
    private boolean optional = false;
  }
  
  
  
  
  private static CycObjectLibraryLoader ME;
  private final Logger logger;
}
