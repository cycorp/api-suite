package com.cyc.baseclient;

/*
 * #%L
 * File: CycObjectLibraryLoader.java
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

import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.ElMt;
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
 * This class locates and loads classes cycFieldotated as
 {@link com.cyc.base.annotation.CycObjectLibrary}, and provides methods to 
 * access their fields. This is useful for verifying that a Cyc server's KB 
 * meets your code's requirements.
 * 
 * @author nwinant
 */
public class CycObjectLibraryLoader {

  // Fields
  
  private final Logger logger;
  private boolean requireOptionalFields = false;
  final private CycAccess access;
  
  
  // Constructor
  
  public CycObjectLibraryLoader(CycAccess access) {
    this.access = access;
    this.logger = LoggerFactory.getLogger(this.getClass().getName());
  }
  

  // Public
  
  public boolean setRequireOptionalFields(boolean includeOptional) {
    return this.requireOptionalFields = includeOptional;
  }
  
  public boolean isRequiringOptionalFields() {
    return this.requireOptionalFields;
  }

  /**
   * Returns all classes cycFieldotated as 
 {@link com.cyc.base.annotation.CycObjectLibrary}.
   * @return all classes cycFieldotated as {@link com.cyc.base.annotation.CycObjectLibrary}
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
  public <E extends Object> Collection<E> getAllObjectsForClass(Class libraryClass, Class<E> classFilter) {
    final Collection<E> results = new ArrayList<E>();
    final Collection<CycLibraryField> annfields = getAllCycLibraryFields(libraryClass);
    for (CycLibraryField ann : annfields) {
      try {
        if (isFieldToBeIncluded(ann)) {
          final Object o = ann.getField().get(getLibraryInstance(libraryClass));
          if (classFilter.isInstance(o)) {
            results.add((E) o);
          }
        }
      } catch (Exception ex) {
        handleException(ann, ex);
      }
    }
    return results;
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
    final Collection<CycLibraryField> cycFields = getAllCycLibraryFields(libraryClass);
    final Collection<CycLibraryField> processedFields = new ArrayList();
    for (CycLibraryField cycField : cycFields) {
      if (this.isFieldToBeIncluded(cycField)) {
        try {
          processCycTerm(libraryClass, cycField, handler);
          processedFields.add(cycField);
        } catch (Exception ex) {
          handler.onException(cycField, ex);
        }
      }
    }
    handler.onLibraryEvaluationEnd(libraryClass, processedFields);
  }
  
  /**
   * Iterates over a Collection of CycObjects, checks whether each is present in
   * a given KB, and returns a Collection of all the CycObjects which are 
   * <strong>not</strong> present in said KB.
   * 
   * @param <E>
   * @param objects
   * @return a Collection of all the CycObjects which are
   * <strong>not</strong> present in said KB
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  public <E extends CycObject> Collection<E> findMissingCycObjects(Collection<E> objects)
          throws CycConnectionException, CycApiException {
    final Collection<E> results = new ArrayList<E>();
    for (E obj : objects) {
      if (!isCycObjectInKB(obj)) {
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
   * @return a Collection of the values for all valid fields which are instances of classFilter (for
   * all {@link com.cyc.base.annotation.CycObjectLibrary} classes) which are <strong>not</strong>
   * present in the KB of the Cyc specified by the {@link CycAccess} object
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  public <E extends CycObject> Collection<E> findMissingCycObjects(Class<E> classFilter)
          throws CycConnectionException, CycApiException {
    final Collection<E> objs = this.getAllObjects(classFilter);
    return findMissingCycObjects(objs);
  }
  
  /**
   * Returns a Collection of the {@link com.cyc.base.cycobject.CycObject}s for
   * all {@link com.cyc.base.annotation.CycObjectLibrary} classes which are <strong>not</strong>
   * present in the KB of the Cyc specified by the {@link CycAccess} object.
   * 
   * @return a Collection of the {@link com.cyc.base.cycobject.CycObject}s for
   * all {@link com.cyc.base.annotation.CycObjectLibrary} classes which are <strong>not</strong>
   * present in the KB of the Cyc specified by the {@link CycAccess} object
   * @throws CycConnectionException
   * @throws CycApiException 
   */
  public Collection<CycObject> findMissingCycObjects()
          throws CycConnectionException, CycApiException {
    return findMissingCycObjects(CycObject.class);
  }
  
  /**
   * Returns a collections of all valid {@link CycLibraryField}s for a given class.
   * 
   * @param clazz
   * @return a collection of CycLibraryFields for a given class
   */
  public Collection<CycLibraryField> getAllCycLibraryFields(Class<?> clazz) {
    final Collection<CycLibraryField> results = new ArrayList<CycLibraryField>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      CycLibraryField cycField = new CycLibraryField(field);
      if (isFieldValid(clazz, cycField)) {
        results.add(cycField);
      }
    }
    return results;
  }
  

  // Protected
  
  protected CycAccess getAccess() {
    return this.access;
  }
    
  protected String getObjectCycLValue(Object o) {
    if (CycObject.class.isInstance(o)) {
      return ((CycObject) o).cyclify();
    } else if (o != null) {
      return o.toString();
    }
    return null;
  }

    
  // Private
  
  /**
   * Determines whether a field is suitable for inclusion. May be overridden
   * with more (or less) restrictive criteria.
   * 
   * @param field
   * @return true if a field is suitable for inclusion
   */
  private boolean isFieldValid(Class libraryClass, CycLibraryField cycField) {
    final CycObjectLibrary lib = (CycObjectLibrary) libraryClass.getAnnotation(CycObjectLibrary.class);
    return Modifier.isFinal(cycField.getField().getModifiers())
            && Modifier.isPublic(cycField.getField().getModifiers())
            && (cycField.isAnnotated() || !lib.requireFieldAnnotations());
  }
  
  private boolean isFieldToBeIncluded(CycLibraryField cycField) {
    if (!isRequiringOptionalFields() && cycField.isOptional()) {
      System.out.println("Skipping field " + cycField + ": Optional.");
      return false;
    } else if (isOpenCyc() && !cycField.includedInOpenCycKB) {
      System.out.println("Skipping field " + cycField + ": Not included in OpenCyc.");
      return false;
    }
    return true;
  }
  
  private boolean isCycObjectInKB(CycObject obj)
          throws CycConnectionException, CycApiException {
    final InspectorTool inspector = getAccess().getInspectorTool();
    if (ElMt.class.isInstance(obj)) {
      return inspector.isElMtInKB((ElMt) obj);
    } else if (CycConstant.class.isInstance(obj)) {
      return inspector.isConstantInKB((CycConstant) obj);
    } else if (CycVariable.class.isInstance(obj)) {
      return true;
    } else if (Formula.class.isInstance(obj)) {
      return inspector.isFormulaWellFormed((Formula) obj, CommonConstants.EVERYTHING_PSC);
    }
    return false;
  }
  
  private void processCycTerm(Class libraryClass, CycLibraryField cycField, CycLibraryFieldHandler handler) throws IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
    Boolean equivalent = null;
    final Object o = cycField.getField().get(getLibraryInstance(libraryClass));
    final String value = getObjectCycLValue(o);
    //if (cycField.isAnnotated() && !cycField.isOptional()) {
    if (cycField.isAnnotated()) {
      final String expected = cycField.getCycl();
      equivalent = (expected != null) ? expected.equals(value) : (value == null);
    }
    handler.onFieldEvaluation(cycField, value, equivalent);
  }
  
  private Object getLibraryInstance(Class libraryClass) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
  
  private String getSingletonAccessMethodName(Class libraryClass) {
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
  private void handleException(CycLibraryField cycField, Exception ex) {
    String msg = "Error accessing field " + cycField.getField().getName();
    logger.error(msg, ex);
    throw new BaseClientRuntimeException(msg, ex);
  }
    
  protected boolean isOpenCyc() {
    try {
      return getAccess().isOpenCyc();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  // Inner classes
    
  /**
   * Interface for an object which handles the results of a 
   * {@link CycObjectLibraryLoader#processAllFieldsForClass(java.lang.Class, com.cyc.baseclient.CycObjectLibraryLoader.CycLibraryFieldHandler) CycObjectLibraryLoader#processAllFieldsForClass}
   */
  public interface CycLibraryFieldHandler {
    void onLibraryEvaluationBegin(Class libraryClass);
    void onFieldEvaluation(CycLibraryField cycField, String cyclValue, Boolean equivalent);
    void onException(CycLibraryField cycField, Exception ex);
    void onLibraryEvaluationEnd(Class libraryClass, Collection<CycLibraryField> processedFields);
  }
  
  /**
   * Wrapper for a field describing a Cyc object, along with any cycFieldotations ({@link CycTerm}, 
   * {@link CycFormula}, etc.) which it might have.
   */
  public class CycLibraryField {
    
    public CycLibraryField(Field field) {
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
      this.includedInOpenCycKB = term.includedInOpenCycKB();
    }
    
    final protected void process(CycFormula formula) {
      this.ann = formula;
      this.cycl = formula.cycl();
      this.optional = formula.optional();
      this.includedInOpenCycKB = formula.includedInOpenCycKB();
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
    
    public boolean isIncludedInOpenCycKB() {
      return includedInOpenCycKB;
    }
    
    final private Field field;
    private Annotation ann = null;
    private String cycl = null;
    private boolean optional = false;
    private boolean includedInOpenCycKB = true;
  }
}
