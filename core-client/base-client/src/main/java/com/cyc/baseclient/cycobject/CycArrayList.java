package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycArrayList.java
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

//// External Imports
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.NonAtomicTerm;
import com.cyc.kb.ArgPosition;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.Guid;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import com.cyc.base.cycobject.Nart;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.datatype.Span;
import com.cyc.baseclient.datatype.StringUtils;
import com.cyc.baseclient.xml.TextUtil;
import com.cyc.baseclient.xml.XmlStringWriter;
import com.cyc.baseclient.xml.XmlWriter;

/**
 * Provides the behavior and attributes of a Base Client list, typically used
 * to represent assertions in their external (EL) form.
 *
 * @version $0.1$
 * @author Stephen L. Reed
 */
public class CycArrayList<E> extends ArrayList<E> implements CycList<E>, CycObject {

  static final long serialVersionUID = 2031704553206469327L;
  /**
   * XML serialization tags.
   */
  public static final String cycListXMLTag = "list";
  public static final String integerXMLTag = "integer";
  public static final String doubleXMLTag = "double";
  public static final String stringXMLTag = "string";
  public static final String dottedElementXMLTag = "dotted-element";
  /**
   * XML serialization indentation.
   */
  public static int indentLength = 2;
  private boolean isProperList = true;
  private E dottedElement;
  public static final CycArrayList EMPTY_CYC_LIST = new UnmodifiableCycList<Object>();

  /**
   * Constructs a new empty <tt>CycList</tt> object.
   */
  public CycArrayList() {
  }

  /**
   * Constructs a new empty <tt>CycList</tt> object of the given size.
   *
   * @param size the initial size of the list
   */
  public CycArrayList(final int size) {
    super(size);
  }

  public CycArrayList(CycArrayList<? extends E> list) {
    for (int i = 0; i < list.getProperListSize(); i++) {
      this.add((E) list.get(i));
    }
    if (!list.isProperList()) {
      setDottedElement(list.getDottedElement());
    }
  }

  /**
   * Constructs a new <tt>CycList</tt> object, containing the elements of the
   * specified collection, in the order they are returned by the collection's iterator.
   *
   * @param c the collection of assumed valid Base Client objects.
   */
  public CycArrayList(final Collection<? extends E> c) {
    super(c);
  }

  /**
   * Constructs a new <tt>CycList</tt> object, containing as its first element
   * <tt>firstElement</tt>, and containing as its remaining elements the
   * contents of the <tt>Collection</tt> remaining elements.
   *
   * @param firstElement the object which becomes the head of the <tt>CycList</tt>
   * @param remainingElements a <tt>Collection</tt>, whose elements become the
   * remainder of the <tt>CycList</tt>
   */
  public CycArrayList(final E firstElement, final Collection<? extends E> remainingElements) {
    this.add(firstElement);
    addAll(remainingElements);
  }

  /**
   * Constructs a new <tt>CycList</tt> object, containing as its sole element
   * <tt>element</tt>
   *
   * @param element the object which becomes the head of the <tt>CycList</tt>
   */
  public CycArrayList(final E element) {
    this.add(element);
  }

  /**
   * Constructs a new <tt>CycList</tt> object, containing as its first element
   * <tt>element1</tt>, and <tt>element2</tt> as its second element.
   *
   * @param element1 the object which becomes the head of the <tt>CycList</tt>
   * @param element2 the object which becomes the second element of the <tt>CycList</tt>
   */
  public CycArrayList(final E element1, final E element2) {
    this.add(element1);
    this.add(element2);
  }

  /** Returns a new proper CycArrayList having the given elements as its initial elements.
   *
   * @param elements the initial elements
   * @return a new proper CycArrayList having the given elements as its initial elements
   */
  public static <T> CycList<T> makeCycList(final T... elements) {

    final CycArrayList<T> cycList = new CycArrayList<T>();
    for (final T element : elements) {
      cycList.add(element);
    }
    return cycList;
  }

  /**
   * Constructs a CycArrayList using the semantics of Lisp symbolic expressions.<br>
   * 1.  construct(a, NIL) --> (a)<br>
   * 2.  construct(a, b) --> (a . b)<br>
   *
   * @param object1 the first <tt>Object</tt> in the <tt>CycArrayList</tt>
   * @param object2 <tt>NIL</tt> or an <tt>Object</tt>
   * @return <tt>CycArrayList</tt> (object) if <tt>object2</tt> is <tt>NIL</tt>,
   * otherwise return the improper <tt>CycArrayList</tt> (object1 . object2)
   *
   * @deprecated Use addToBeginning(E) or makeDottedPair(T,T) instead.
   */
  public static <T> CycList<T> construct(final T object1, final Object object2) {
    final CycArrayList<T> cycList = new CycArrayList<T>(object1);
    if (object2.equals(CycObjectFactory.nil)) {
      return cycList;
    }
    if (object2 instanceof CycArrayList) {
      final CycArrayList cycList2 = (CycArrayList) object2;
      cycList.addAll(cycList2);
      return cycList;
    }
    cycList.setDottedElement((T) object2);
    return cycList;
  }

  /**
   * Inserts the specified element at the beginning of this list.
   * Shifts all current elements to the right (adds one to their indices).
   *
   * @param element element to be inserted
   * @return the passed-in list with <tt>element</tt> inserted.
   */
  @Override
  public CycArrayList<E> addToBeginning(final E element) {
    if (isEmpty()) {
      add(element);
    } else {
      add(0, element);
    }
    return this;
  }

  /**
   * Constructs a CycArrayList with a normal element and a dotted element.<br>
   *
   *
   * @param normalElement the normal <tt>Object</tt> in the <tt>CycArrayList</tt>
   * @param dottedElement the <tt>Object</tt> to be the dotted element.
   * @return the dotted pair <tt>CycArrayList</tt> (normalElement . dottedElement)
   */
  public static <T> CycList<T> makeDottedPair(final T normalElement, final T dottedElement) {
    if (CycObjectFactory.nil.equals(dottedElement)) {
      return new CycArrayList<T>(normalElement);
    }
    final CycArrayList<T> cycList = new CycArrayList<T>(normalElement);
    cycList.setDottedElement((T) dottedElement);
    return cycList;
  }

  /**
   * Creates and returns a copy of this <tt>CycArrayList</tt>.
   *
   * @return a clone of this instance
   */
  @Override
  public Object clone() {
    return new CycArrayList<E>(this);
  }

  /**
   * Creates and returns a deep copy of this <tt>CycArrayList</tt>.  In a deep copy,
   * directly embedded <tt>CycArrayList</tt> objects are also deep copied.  Objects
 which are not CycArrayLists are cloned.
   *
   * @return a deep copy of this <tt>CycArrayList</tt>
   */
  @Override
  public CycArrayList<E> deepCopy() {
    final CycArrayList cycList = new CycArrayList<E>();
    if (!this.isProperList()) {
      if (this.dottedElement instanceof CycArrayList) {
        cycList.setDottedElement(((CycArrayList) this.dottedElement).deepCopy());
      } else {
        cycList.setDottedElement(this.getDottedElement());
      }
    }
    for (int i = 0; i < super.size(); i++) {
      final Object element = this.get(i);
      if (element instanceof CycArrayList) {
        cycList.add(((CycArrayList) element).deepCopy());
      } else {
        cycList.add(element);
      }
    }
    return cycList;
  }

  /**
   * Gets the dotted element.
   *
   * @return the <tt>Object</tt> which forms the dotted element of this <tt>CycArrayList</tt>
   */
  @Override
  public E getDottedElement() {
    return dottedElement;
  }

  /**
   * Sets the dotted element and set the improper list attribute to <tt>true</tt>.
   * @param dottedElement
   */
  @Override
  public void setDottedElement(final E dottedElement) {
    this.dottedElement = dottedElement;
    if ((dottedElement == null) || (CycObjectFactory.nil.equals(dottedElement))) {
      this.isProperList = true;
    } else {
      this.isProperList = false;
    }
  }

  /**
   * Returns <tt>true</tt> if this is a proper list.
   *
   * @return <tt>true</tt> if this is a proper list, otherwise return <tt>false</tt>
   */
  @Override
  public boolean isProperList() {
    return isProperList;
  }

  /** Returns the CycArrayList size including the optional dotted element.  Note that this fools list iterators.
   *
   * @return the CycArrayList size including the optional dotted element
   */
  @Override
  public int size() {
    int result = super.size();
    if (!isProperList()) {
      result++;
    }
    return result;
  }

  @Override
  public int getProperListSize() {
    return super.size();
  }
  
  public static int getProperListSize(List l) {
    if (l instanceof CycList && !((CycList)l).isProperList()) {
      return l.size() - 1;
    } else {
      return l.size();
    }
  }

  /**
   * Answers true iff the CycArrayList contains valid elements.  This is a necessary, but
   * not sufficient condition for CycL well-formedness.
   * @return true if the CycArrayList is valid.
   */
  @Override
  public boolean isValid() {
    for (int i = 0; i < this.size(); i++) {
      final Object object = this.get(i);
      if (object instanceof String || object instanceof Integer || object instanceof Guid || object instanceof Float || object instanceof ByteArray || object instanceof CycConstant || object instanceof Nart) {
        continue;
      } else if (object instanceof CycArrayList) {
        if (!((CycArrayList) object).isValid()) {
          return false;
        }
      } else {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns true if formula is well-formed in the relevant mt.
   *
   * @param mt the relevant mt
   * @return true if formula is well-formed in the relevant mt, otherwise false
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @ deprecated use CycAccess.isFormulaWellFormed(this, mt);
   */
  /*
  public boolean isFormulaWellFormed(final ElMt mt)
          throws IOException, UnknownHostException, CycApiException {
    return CycAccess.getCurrent().isFormulaWellFormed(this, mt);
  }
*/
  /**
   * Returns true if formula is well-formed Non Atomic Reifable Term.
   *
   * @return true if formula is well-formed Non Atomic Reifable Term, otherwise false
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @ deprecated use CycAccess.isCycLNonAtomicReifableTerm();
   */
  /*
  public boolean isCycLNonAtomicReifableTerm()
          throws IOException, UnknownHostException, CycApiException {
    return CycAccess.getCurrent().isCycLNonAtomicReifableTerm(this);
  }
  */

  /**
   * Returns true if formula is well-formed Non Atomic Un-reifable Term.
   *
   * @return true if formula is well-formed Non Atomic Un-reifable Term, otherwise false
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @ deprecated use CycAccess.isCycLNonAtomicUnreifableTerm();
   */
  /*
  public boolean isCycLNonAtomicUnreifableTerm()
          throws IOException, UnknownHostException, CycApiException {
    return CycAccess.getCurrent().isCycLNonAtomicUnreifableTerm(this);
  }
  */

  /**
   * Creates a new <tt>CycArrayList</tt> containing the given element.
   *
   * @param element the contents of the new <tt>CycArrayList</tt>
   * @return a new <tt>CycArrayList</tt> containing the given element
   */
  public static <E> CycList<E> list(final E element) {
    final CycArrayList<E> result = new CycArrayList<E>();
    result.add(element);
    return result;
  }

  /**
   * Creates a new <tt>CycArrayList</tt> containing the given two elements.
   *
   * @param element1 the first item of the new <tt>CycArrayList</tt>
   * @param element2 the second item of the new <tt>CycArrayList</tt>
   * @return a new <tt>CycArrayList</tt> containing the given two elements
   */
  public static <E> CycList<E> list(final E element1, final E element2) {
    final CycArrayList<E> result = new CycArrayList<E>();
    result.add(element1);
    result.add(element2);
    return result;
  }

  /**
   * Creates a new <tt>CycArrayList</tt> containing the given three elements.
   *
   * @param element1 the first item of the new <tt>CycArrayList</tt>
   * @param element2 the second item of the new <tt>CycArrayList</tt>
   * @param element3 the third item of the new <tt>CycArrayList</tt>
   * @return a new <tt>CycArrayList</tt> containing the given three elements
   */
  public static <E> CycList<E> list(final E element1, final E element2, final E element3) {
    final CycArrayList<E> result = new CycArrayList<E>();
    result.add(element1);
    result.add(element2);
    result.add(element3);
    return result;
  }

  /**
   * Returns the first element of the <tt>CycArrayList</tt>.
   *
   * @return the <tt>Object</tt> which is the first element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  @Override
  public E first() {
    if (size() == 0) {
      throw new BaseClientRuntimeException("First element not available for an empty CycList");
    }
    return this.get(0);
  }

  /**
   * Returns the second element of the <tt>CycArrayList</tt>.
   *
   * @return the <tt>Object</tt> which is the second element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  @Override
  public E second() {
    if (size() < 2) {
      throw new BaseClientRuntimeException("Second element not available");
    }
    return this.get(1);
  }

  /**
   * Returns the third element of the <tt>CycArrayList</tt>.
   *
   * @return the <tt>Object</tt> which is the third element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  @Override
  public E third() {
    if (size() < 3) {
      throw new BaseClientRuntimeException("Third element not available");
    }
    return this.get(2);
  }

  /**
   * Returns the fourth element of the <tt>CycArrayList</tt>.
   *
   * @return the <tt>Object</tt> which is the fourth element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  @Override
  public E fourth() {
    if (size() < 4) {
      throw new BaseClientRuntimeException("Fourth element not available");
    }
    return this.get(3);
  }

  /**
   * Returns the last element of the <tt>CycArrayList</tt>.
   *
   * @return the <tt>Object</tt> which is the last element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  @Override
  public E last() {
    if (size() == 0) {
      throw new BaseClientRuntimeException("Last element not available");
    }
    return this.get(this.size() - 1);
  }

  /**
   * Returns a new CycArrayList formed by removing the first element, in in the case of a
 dotted pair, returns the dotted element.
   *
   * @return the CycArrayList after removing the first element, in in the case of a
 dotted pair, returns the dotted element.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  @Override
  public Object rest() {
    if (size() == 0) {
      throw new BaseClientRuntimeException("Cannot remove first element of an empty list.");
    } else if ((super.size() == 1) && (!this.isProperList)) {
      return this.getDottedElement();
    }
    final CycArrayList<E> cycList = new CycArrayList<E>(this);
    cycList.remove(0);
    return cycList;
  }

  /**
   * Appends the given elements to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param cycList the elements to add
   * @return the list after adding the given elements to the end
   */
  @Override
  public CycArrayList<E> appendElements(final CycList<? extends E> cycList) {
    addAll(cycList);
    return this;
  }

  /**
   * Appends the given element to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param object the object element to add
   * @return the list after adding the given element to the end
   */
  @Override
  public CycArrayList<E> appendElement(final E object) {
    add(object);
    return this;
  }

  /**
   * Appends the given element to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param i the integer element to add
   * @return the list after adding the given element to the end
   */
  @Override
  public CycArrayList appendElement(final int i) {
    add(i);
    return this;
  }

  /**
   * Appends the given element to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param l the long element to add
   * @return the list after adding the given element to the end
   */
  @Override
  public CycArrayList appendElement(final long l) {
    add(l);
    return this;
  }

  /**
   * Appends the given element to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param b the boolean element to add
   * @return the list after adding the given element to the end
   */
  @Override
  public CycArrayList appendElement(final boolean b) {
    add(b);
    return this;
  }

  @Override
  public boolean add(E e) {
    return super.add(e);
  }

  /**
   * Adds the given integer to this list by wrapping it with an Integer object.
   *
   * @param i the given integer to add
   */
  public void add(final int i) {
    super.add((E) Integer.valueOf(i));
  }

  /**
   * Adds the given long to this list by wrapping it with an Long object.
   *
   * @param l the given long to add
   */
  public void add(final long l) {
    super.add((E) Long.valueOf(l));
  }

  /**
   * Adds the given float to this list by wrapping it with a Float object.
   *
   * @param f the given float to add
   */
  public void add(final float f) {
    super.add((E) Float.valueOf(f));
  }

  /**
   * Adds the given double to this list by wrapping it with a Double object.
   *
   * @param d the given double to add
   */
  public void add(final double d) {
    super.add((E) Double.valueOf(d));
  }

  /**
   * Adds the given boolean to this list by wrapping it with a Boolean object.
   *
   * @param b the given boolean to add
   */
  public void add(final boolean b) {
    super.add((E) Boolean.valueOf(b));
  }

  /**
   * Adds the given element to this list if it is not already contained.
   */
  @Override
  public void addNew(final E object) {
    if (!this.contains(object)) {
      this.add(object);
    }
  }

  /**
   * Adds the given elements to this list if they are not already contained.
   */
  @Override
  public void addAllNew(final Collection<? extends E> objects) {
    for (Iterator<? extends E> iter = objects.iterator(); iter.hasNext();) {
      this.addNew((E) iter.next());
    }
  }

  @Override
  public boolean addAll(Collection<? extends E> col) {
    boolean result = super.addAll(col);
    if (col instanceof CycArrayList) {
      CycArrayList cycList = (CycArrayList) col;
      if (!cycList.isProperList()) {
        E dottedElement = (E) cycList.getDottedElement();
        if (isProperList()) {
          setDottedElement(dottedElement);
        } else {
          add(getDottedElement());
          setDottedElement(dottedElement);
        }
      }
    }
    return result;
  }
  
  @Override
  public boolean contains(Object obj) {
    if (!isProperList()) {
      if (getDottedElement().equals(obj)) {
        return true;
      }
    }
    return super.contains(obj);
  }

  /**
   * Returns true iff this list contains duplicate elements.
   *
   * @return true iff this list contains duplicate elements
   */
  @Override
  public boolean containsDuplicates() {
    if (!isProperList) {
      if (this.contains(this.dottedElement)) {
        return true;
      }
    }
    for (int i = 0; i < this.size(); i++) {
      for (int j = i + 1; j < this.size(); j++) {
        if (this.get(i).equals(this.get(j))) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Destructively delete duplicates from the list.
   * @return <code>this</code> list with the duplicates deleted.
   */
  @Override
  public CycArrayList deleteDuplicates() {
    if (this.isProperList) {
      if (this.contains(this.dottedElement)) {
        this.setDottedElement(null);
      }
    }
    for (int i = 0; i < this.size(); i++) {
      for (int j = i + 1; j < this.size(); j++) {
        if (this.get(i).equals(this.get(j))) {
          this.remove(j);
          j--;
        }
      }
    }
    return this;
  }

  /**
   * Remove duplicates from the list.  Just like #deleteDuplicates but
   * non-destructive.
   * @return A new list with the duplicates removed.
   */
  @Override
  public CycArrayList removeDuplicates() {
    final CycArrayList result = this.deepCopy();
    return result.deleteDuplicates();
  }

  /**
   * Flatten the list. Recursively iterate through tree, and return a list of
   * the atoms found.
   * @return List of atoms in <code>this</code> CycArrayList.
   */
  @Override
  public CycArrayList<E> flatten() {
    final CycArrayList<E> result = new CycArrayList<E>();
    final Iterator<E> i = this.iterator();
    while (i.hasNext()) {
      E obj = i.next();
      if (obj instanceof CycArrayList) {
        result.addAll(((CycArrayList) obj).flatten());
      } else {
        result.add(obj);
      }
    } //end while
    if (!isProperList) {
      result.add(getDottedElement());
    }
    return result;
  }

  /**
   * Returns a new <tt>CycArrayList</tt> whose elements are the reverse of
   * this <tt>CycArrayList</tt>, which is unaffected.
   *
   * @return new <tt>CycArrayList</tt> with elements reversed.
   */
  @Override
  public CycArrayList<E> reverse() {
    if (!isProperList) {
      throw new BaseClientRuntimeException(this + " is not a proper list and cannot be reversed");
    }
    final CycArrayList<E> result = new CycArrayList<E>();
    for (int i = (this.size() - 1); i >= 0; i--) {
      result.add(this.get(i));
    }
    return result;
  }

  /**
   * Returns a <tt>CycArrayList</tt> of the length N combinations of sublists from this
   * object.  This algorithm preserves the list order with the sublists.
   *
   * @param n the length of the sublist
   * @return a <tt>CycArrayList</tt> of the length N combinations of sublists from this
   * object
   */
  @Override
  public CycList<CycList<E>> combinationsOf(int n) {
    if (!isProperList) {
      throw new BaseClientRuntimeException(this + " is not a proper list");
    }
    if (this.size() == 0 || n == 0) {
      return new CycArrayList<CycList<E>>();
    }
    return combinationsOfInternal(new CycArrayList<E>(this.subList(0, n)),
            new CycArrayList<E>(this.subList(n, this.size())));
  }

  /**
   * Provides the internal implementation <tt.combinationsOf</tt> using a recursive
   * algorithm.
   *
   * @param selectedItems a window of contiguous items to be combined
   * @param availableItems the complement of the selectedItems
   * @return a <tt>CycArrayList</tt> of the combinations of sublists from the
   * selectedItems.
   */
  private static <E> CycList<CycList<E>> combinationsOfInternal(final CycList<E> selectedItems, final CycArrayList<E> availableItems) {
    final CycList<CycList<E>> result = CycArrayList.list(selectedItems);
    if (availableItems.size() == 0) {
      return result;
    }
    CycArrayList<E> combination = null;
    for (int i = 0; i < (selectedItems.size() - 1); i++) {
      for (int j = 0; j < availableItems.size(); j++) {
        final E availableItem = availableItems.get(j);
        // Remove it (making copy), shift left, append replacement.
        combination = (CycArrayList) selectedItems.clone();
        combination.remove(i + 1);
        combination.add(availableItem);
        result.add(combination);
      }
    }
    final CycArrayList newSelectedItems = (CycArrayList) selectedItems.rest();
    newSelectedItems.add(availableItems.first());
    final CycArrayList newAvailableItems = (CycArrayList) availableItems.rest();
    result.addAll(combinationsOfInternal(newSelectedItems, newAvailableItems));
    return result;
  }

  /**
   * Returns a random ordering of the <tt>CycArrayList</tt> without recursion.
   *
   * @return a random ordering of the <tt>CycArrayList</tt> without recursion
   */
  @Override
  public CycArrayList randomPermutation() {
    final Random random = new Random();
    int randomIndex = 0;
    final CycArrayList remainingList = (CycArrayList) this.clone();
    final CycArrayList permutedList = new CycArrayList();
    if (this.size() == 0) {
      return remainingList;
    }
    while (true) {
      if (remainingList.size() == 1) {
        permutedList.addAll(remainingList);
        return permutedList;
      }
      randomIndex = random.nextInt(remainingList.size() - 1);
      permutedList.add(remainingList.get(randomIndex));
      remainingList.remove(randomIndex);
    }
  }

  /**
   * Returns a new <tt>CycArrayList</tt> with every occurrence of <tt>Object</tt> oldObject
   * replaced by <tt>Object</tt> newObject.  Substitute recursively into embedded
   * <tt>CycArrayList</tt> objects.
   *
   * @return a new <tt>CycArrayList</tt> with every occurrence of <tt>Object</tt> oldObject
   * replaced by <tt>Object</tt> newObject
   */
  @Override
  public CycArrayList subst(final E newObject, final E oldObject) {
    final CycArrayList result = new CycArrayList();
    if (!isProperList) {
      result.setDottedElement((dottedElement.equals(oldObject)) ? oldObject : newObject);
    }
    for (int i = 0; i < getProperListSize(); i++) {
      final E element = this.get(i);
      if (element.equals(oldObject)) {
        result.add(newObject);
      } else if (element instanceof CycArrayList) {
        result.add(((CycArrayList) element).subst(newObject, oldObject));
      } else {
        result.add(element);
      }
    }
    return result;
  }

  /**
   * Returns a <tt>String</tt> representation of this
   * <tt>List</tt>.
   */
  @Override
  public String toString() {
    return toStringHelper(false);
  }

  /**
   * Returns a <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.  When the parameter is true, the representation is created without causing
   * additional api calls to complete the name field of constants.
   *
   * @param safe when true, the representation is created without causing
   * additional api calls to complete the name field of constants
   * @return a <tt>String</tt> representation of this <tt>CycArrayList</tt>
   */
  protected String toStringHelper(final boolean safe) {
    final StringBuffer result = new StringBuffer("(");
    for (int i = 0; i < super.size(); i++) {
      if (i > 0) {
        result.append(" ");
      }
      final E element = this.get(i);
      if (element == null) {
        result.append("null");
      } else if (element instanceof String) {
        result.append("\"" + element + "\"");
      } else if (safe) {
        try {
          // If element understands the safeToString method, then use it.
          final Method safeToString = element.getClass().getMethod("safeToString");
          result.append(safeToString.invoke(element));
        } catch (Exception e) {
          result.append(element.toString());
        }
      } else {
        result.append(element.toString());
      }
    }
    if (!isProperList) {
      result.append(" . ");
      if (dottedElement instanceof String) {
        result.append("\"");
        result.append(dottedElement);
        result.append("\"");
      } else if (safe) {
        try {
          // If dottedElement understands the safeToString method, then use it.
          final Method safeToString = dottedElement.getClass().getMethod("safeToString");
          result.append(safeToString.invoke(dottedElement));
        } catch (Exception e) {
          result.append(dottedElement.toString());
        }
      } else {
        result.append(dottedElement.toString());
      }
    }
    result.append(")");
    return result.toString();
  }

  /**
   * Returns a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycArrayList</tt>
   * @return a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   */
  @Override
  public String toPrettyString(String indent) {
    return toPrettyStringInt(indent, "  ", "\n", false, false);
  }

  /**
   * Returns a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt> with embedded strings escaped.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycArrayList</tt>
   * @return a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   */
  @Override
  public String toPrettyEscapedCyclifiedString(String indent) {
    return toPrettyStringInt(indent, "  ", "\n", true, true);
  }

  /**
   * Returns a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycArrayList</tt>
   * @return a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   */
  @Override
  public String toPrettyCyclifiedString(String indent) {
    return toPrettyStringInt(indent, "  ", "\n", true, false);
  }

  /**
   * Returns an HTML `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycArrayList</tt>
   * @return an HTML `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   */
  @Deprecated
  public String toHTMLPrettyString(final String indent) {
    // dpb -- shouldn't this be "&nbsp;&nbsp;"?
    return "<html><body>" + toPrettyStringInt(indent, "&nbsp&nbsp", "<br>", false, false) + "</body></html>";
  }

   /**
   * Returns an HTML `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt> without having to say what the indent string is (empty indent).

   * @return an HTML `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   */
  @Deprecated
  public String toHTMLPrettyString() {
    // dpb -- shouldn't this be "&nbsp;&nbsp;"?
    return "<html><body>" + toPrettyStringInt("", "&nbsp&nbsp", "<br>", false, false) + "</body></html>";
  }
  
  /**
   * Returns a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycArrayList</tt>
   * @param incrementIndent the indent string that to the <tt>String</tt>
   * representation this <tt>CycArrayList</tt>is added at each level
   * of indenting
   * @param newLineString the string added to indicate a new line
   * @param shouldCyclify indicates that the output constants should have #$ prefix
   * @param shouldEscape indicates that embedded strings should have appropriate escapes for the SubL reader
   * @return a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycArrayList</tt>.
   */
  @Override
  public String toPrettyStringInt(final String indent,
          final String incrementIndent, final String newLineString,
          final boolean shouldCyclify, final boolean shouldEscape) {
    final StringBuffer result = new StringBuffer(indent + "(");
    for (int i = 0; i < super.size(); i++) {
      Object element = this.get(i);
      if (element instanceof NonAtomicTerm) {
        element = ((NonAtomicTerm) element).toCycList();
      }
      if (element instanceof FormulaImpl) {
        element = ((FormulaImpl) element);
      }
      if (element instanceof String) {
        if (i > 0) {
          result.append(" ");
        }
        result.append('"');
        if (shouldEscape) {
          result.append(StringUtils.escapeDoubleQuotes((String) element));
        } else {
          result.append(element);
        }
        result.append('"');
      } else if (element instanceof CycArrayList) {
        result.append(newLineString + ((CycArrayList) element).toPrettyStringInt(indent + incrementIndent, incrementIndent,
                newLineString, shouldCyclify, shouldEscape));
      } else if (element instanceof FormulaImpl) {
        result.append(newLineString + ((FormulaImpl) element).toCycList().toPrettyStringInt(indent + incrementIndent, incrementIndent,
                newLineString, shouldCyclify, shouldEscape));
      } else {
        if (i > 0) {
          result.append(" ");
        }
        if (shouldCyclify) {
          if (shouldEscape) {
            result.append(DefaultCycObject.cyclify(element));
          } else {
            result.append(DefaultCycObject.cyclifyWithEscapeChars(element, false));
          }
        } else {
          result.append(element.toString());
        }
      }
    }
    if (!isProperList) {
      result.append(" . ");
      if (dottedElement instanceof String) {
        result.append("\"");
        if (shouldEscape) {
          result.append(StringUtils.escapeDoubleQuotes((String) dottedElement));
        } else {
          result.append(dottedElement);
        }
        result.append("\"");
      } else {
        result.append(this.dottedElement.toString());
      }
    }
    result.append(")");
    return result.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (!(o instanceof List)) {
      return false;
    }
    if (!isProperList()) {
      if (!(o instanceof CycArrayList)) {
        return false;
      }
      if (((CycArrayList) o).isProperList()) {
        return false;
      }
    } else {
      if (o instanceof CycArrayList) {
        if (!((CycArrayList) o).isProperList()) {
          return false;
        }
      }
    }
    ListIterator<E> e1 = listIterator();
    ListIterator e2 = ((List) o).listIterator();
    while (e1.hasNext() && e2.hasNext()) {
      E o1 = e1.next();
      Object o2 = e2.next();
      if (o1 instanceof CycArrayList) {
        if (!((CycArrayList) o1).isProperList()) {
          if (!(o2 instanceof CycArrayList)) {
            return false;
          }
          if (((CycArrayList) o2).isProperList()) {
            return false;
          }
        } else {
          if (o2 instanceof CycArrayList) {
            if (!((CycArrayList) o2).isProperList()) {
              return false;
            }
          }
        }
      }
      if (!(o1 == null ? o2 == null : o1.equals(o2))) {
        return false;
      }
    }
    if (e1.hasNext() || e2.hasNext()) {
      return false;
    }
    if  (!isProperList()) {
      if (!(o instanceof CycArrayList)) {
        return false;
      }
      CycArrayList otherList = (CycArrayList) o;
      if (otherList.isProperList()) {
        return false;
      }
      Object dottedElement1 = getDottedElement();
      Object dottedElement2 = otherList.getDottedElement();
      if (dottedElement1 == dottedElement2) {
        return true;
      }
      if ((dottedElement1 == null) || (dottedElement2 == null)) {
        return (dottedElement1 == dottedElement2);
      }
      return dottedElement1.equals(dottedElement2);
    } else {
      return (!(o instanceof CycArrayList)) || ((o instanceof CycArrayList) && ((CycArrayList) o).isProperList());
    }
  }

  @Override
  public int hashCode() {
    int code = 0;
    int argPos = 0;
    for (final E item : this) {
      code = code ^ (item.hashCode() + argPos++);
    }
    return code;
  }
  
  public int printHashCode() {
    int code = 0;
    System.out.println("==");
    for (final E item : this) {
      if (CycArrayList.class.isInstance(item)) {
        ((CycArrayList) item).printHashCode();
      } else {
        System.out.println("  - " + item.hashCode());
        System.out.println("    " + item);
      }
      code = code ^ item.hashCode();
    }
    System.out.println("==");
    return code;
  }

  /** Convert this to a Map.  This method is only valid if the list is an association list.
   * 
   * @return the Map
   */
  @Override
  public Map<Object, Object> toMap() {
    final Map<Object, Object> map = new HashMap<Object, Object>(this.size());
    try {
      for (E elt : this) {
        CycArrayList<Object> eltAsList = (CycArrayList<Object>)elt;
        map.put(eltAsList.first(), eltAsList.rest());
      }
    } catch (Exception e) {
      throw new UnsupportedOperationException("Unable to convert CycList to Map because CycList is not an association-list.", e);
    }
    return map;
  }
  
  /**
   * Create a new association list from a map.
   * @param m
   * @return the association list that corresponds to m.
   */
  public static CycList fromMap(Map m) {
    CycList l = new CycArrayList();
    for (Object i : m.entrySet()) {
      Map.Entry e = (Map.Entry)i;
        l.add(CycArrayList.makeDottedPair(e.getKey(), e.getValue()));
    }
    return l;
  }

  /** Returns true if the given object is equal to this object as EL CycL expressions
   *
   * @param o the given object
   * @return true if the given object is equal to this object as EL CycL expressions, otherwise
   * return false
   */
  @Override
  public boolean equalsAtEL(Object o) {
    Map<CycVariableImpl, CycVariableImpl> varMap = new HashMap<CycVariableImpl, CycVariableImpl>();
    return equalsAtEL(o, varMap);
  }

  private boolean equalsAtEL(Object o, Map<CycVariableImpl, CycVariableImpl> varMap) {
    if (o == this) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o instanceof NonAtomicTerm) {
      o = ((NonAtomicTerm) o).toCycList();
    }
    if (!(o instanceof List)) {
      return false;
    }
    if (!isProperList()) {
      if (!(o instanceof CycArrayList)) {
        return false;
      }
      if (((CycArrayList) o).isProperList()) {
        return false;
      }
    } else {
      if (o instanceof CycArrayList) {
        if (!((CycArrayList) o).isProperList()) {
          return false;
        }
      }
    }
    java.util.ListIterator<E> e1 = listIterator();
    java.util.ListIterator e2 = ((List) o).listIterator();
    while (e1.hasNext() && e2.hasNext()) {
      Object o1 = e1.next();
      if ((o1 != null) && (o1 instanceof NonAtomicTerm)) {
        o1 = ((NonAtomicTerm) o1).toCycList();
      }
      Object o2 = e2.next();
      if ((o2 != null) && (o2 instanceof NonAtomicTerm)) {
        o2 = ((NonAtomicTerm) o2).toCycList();
      }
      if (o1 instanceof CycArrayList) {
        if (!((CycArrayList) o1).isProperList()) {
          if (!(o2 instanceof CycArrayList)) {
            return false;
          }
          if (((CycArrayList) o2).isProperList()) {
            return false;
          }
        } else {
          if (o2 instanceof CycArrayList) {
            if (!((CycArrayList) o2).isProperList()) {
              return false;
            }
          }
        }
        if (!(o1 == null ? o2 == null : ((CycArrayList) o1).equalsAtEL(o2, varMap))) {
          return false;
        }
      } else if ((o1 instanceof Integer && o2 instanceof Long) || (o1 instanceof Long && o2 instanceof Integer)) {
        return ((Number) o1).longValue() == ((Number) o2).longValue();
      } else if ((o1 instanceof Float && o2 instanceof Double) || (o1 instanceof Double && o2 instanceof Float)) {
        return ((Number) o1).doubleValue() == ((Number) o2).doubleValue();
      } else if (o1 instanceof CycVariableImpl && o2 instanceof CycVariableImpl) {
        if (varMap.containsKey(o1) && !varMap.get(o1).equals(o2)) {
          return false;
        } else {
          varMap.put((CycVariableImpl) o1, (CycVariableImpl) o2);
        }
      } else if (o1 instanceof CycFormulaSentence && o2 instanceof CycFormulaSentence) {
        if (((CycFormulaSentence) o1).args.equalsAtEL(((CycFormulaSentence) o2).args, varMap) == false) {
          return false;
        }
      } else if (!(o1 == null ? o2 == null : o1.equals(o2))) {
        return false;
      }
    }
    return !(e1.hasNext() || e2.hasNext());
  }

  @Override
  public int compareTo(Object o) {
    if (o == this) {
      return 0;
    }
    if (o == null) {
      return 1;
    }
    if (!(o instanceof List)) {
      return 1;
    }
    if (!isProperList()) {
      if (!(o instanceof CycArrayList)) {
        return 1;
      }
      if (((CycArrayList) o).isProperList()) {
        return 1;
      }
    } else {
      if (o instanceof CycArrayList) {
        if (!((CycArrayList) o).isProperList()) {
          return -1;
        }
      }
    }
    java.util.ListIterator<E> e1 = listIterator();
    java.util.ListIterator e2 = ((List) o).listIterator();
    while (e1.hasNext() && e2.hasNext()) {
      E o1 = e1.next();
      Object o2 = e2.next();

      if (o1 == o2) {
        continue;
      }
      if (o1 == null) {
        return -1;
      }
      if (o2 == null) {
        return 1;
      }
      if (!(o1 instanceof Comparable) && !(o2 instanceof Comparable)) {
        continue;
      }
      if (!(o1 instanceof Comparable)) {
        return 1;
      }
      if (!(o2 instanceof Comparable)) {
        return -1;
      }
      Comparable co1 = (Comparable) o1;
      Comparable co2 = (Comparable) o2;


      if (co1 instanceof CycArrayList) {
        if (!((CycArrayList) co1).isProperList()) {
          if (!(co2 instanceof CycArrayList)) {
            return 1;
          }
          if (((CycArrayList) co2).isProperList()) {
            return 1;
          }
        } else {
          if (co2 instanceof CycArrayList) {
            if (!((CycArrayList) co2).isProperList()) {
              return -1;
            }
          }
        }
      }

      int ret = co1.compareTo(co2);
      if (ret != 0) {
        return ret;
      }
    }
    if (e1.hasNext()) {
      return 1;
    }
    if (e2.hasNext()) {
      return -1;
    }
    return 0;
  }

  /**
   * Returns a cyclified string representation of the Base Client <tt>CycArrayList</tt>.
   * Embedded constants are prefixed with "#$".  Embedded quote and backslash
   * chars in strings are escaped.
   *
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  @Override
  public String cyclifyWithEscapeChars() {
    return cyclifyWithEscapeChars(false);
  }

  /**
   * Returns a cyclified string representation of the Base Client <tt>CycArrayList</tt>.
   * Embedded constants are prefixed with "#$".  Embedded quote and backslash
   * chars in strings are escaped.
   *
   * @param isApi Should the list be cyclified for as an API call?
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  @Override
  public String cyclifyWithEscapeChars(boolean isApi) {
    final StringBuffer result = new StringBuffer("(");
    String cyclifiedObject = null;
    for (int i = 0; i < super.size(); i++) {
      final E object = this.get(i);
      cyclifiedObject = DefaultCycObject.cyclifyWithEscapeChars(object, isApi);
      if (i > 0) {
        result.append(" ");
      }
      result.append(cyclifiedObject);
    }
    if (!isProperList) {
      result.append(" . ");
      result.append(DefaultCycObject.cyclifyWithEscapeChars(dottedElement, isApi));
    }
    result.append(")");
    return result.toString();
  }

  /**
   * Returns a cyclified string representation of the Base Client <tt>CycArrayList</tt>.
   * Embedded constants are prefixed with "#$".
   *
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  @Override
  public String cyclify() {
    final StringBuffer result = new StringBuffer("(");
    for (int i = 0; i < super.size(); i++) {
      E object = this.get(i);
      if (object == null) {
        throw new BaseClientRuntimeException("Invalid null element after " + result + " in " + this);
      }
      if (i > 0) {
        result.append(" ");
      }
      result.append(DefaultCycObject.cyclify(object));
    }
    if (!isProperList) {
      result.append(" . ");
      result.append(DefaultCycObject.cyclify(dottedElement));
    }
    result.append(")");
    return result.toString();
  }

  public Map<ArgPosition, Span> getPrettyStringDetails() {
    Map<ArgPosition, Span> map = new HashMap<ArgPosition, Span>();
    getPrettyStringDetails(this, "", 0, new ArgPositionImpl(), map);
    Span loc = new Span(0, toPrettyString("").length());
    map.put(ArgPositionImpl.TOP, loc);
    return map;
  }

  private static int getPrettyStringDetails(final CycArrayList list, final String indent,
          int currentPos, final ArgPositionImpl argPos, final Map<ArgPosition, Span> map) {
    String str;
    ArgPosition newArgPos;
    String tab = "  ";
    str = indent + "(";
    currentPos += str.length();
    String cyclifiedObject = null;
    int tempPos;
    for (int i = 0, size = list.size(); i < size; i++) {
      if (i > 0) {
        str = " ";
        currentPos += str.length();
      }
      if ((!list.isProperList()) && ((i + 1) >= size)) {
        currentPos += 2;
      }
      Object element = list.get(i);
      if (element instanceof Nart) {
        element = ((Nart) element).toCycList();
      }
      if (element instanceof String) {
        str = "\"" + element + "\"";
        newArgPos = argPos.deepCopy();
        newArgPos.extend(i);
        Span loc = new Span(currentPos, currentPos + str.length());
        map.put(newArgPos, loc);
        currentPos += str.length();
      } else if (element instanceof CycArrayList) {
        argPos.extend(i);
        tempPos = currentPos + indent.length() + tab.length();
        currentPos = getPrettyStringDetails((CycArrayList) element,
                indent + tab, currentPos, argPos, map);
        Span loc = new Span(tempPos, currentPos);
        ArgPosition deepCopy = argPos.deepCopy();
        map.put(deepCopy, loc);
        argPos.toParent();
      } else {
        str = element.toString();
        newArgPos = argPos.deepCopy();
        newArgPos.extend(i);
        Span loc = new Span(currentPos, currentPos + str.length());
        map.put(newArgPos, loc);
        currentPos += str.length();
      }
    }
    str = ")";
    return currentPos + str.length();
  }
  /**
   * the limit on lists that are returned as one LIST expression;
   * lists longer than this are broken down into NCONC of LISTs expressions
   */
  final static public int MAX_STRING_API_VALUE_LIST_LITERAL_SIZE = 2048;
  final static private CycSymbolImpl LIST_NIL = new CycSymbolImpl("NIL");

  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   * @throws IllegalArgumentException if the total size of the list exceeds 
   * MAX_STRING_API_VALUE_LIST_LITERAL_SIZE times MAX_STRING_API_VALUE_LIST_LITERAL_SIZE in size,
   * because of the danger of causing a stack overflow in the communication
   * with the SubL interpreter
   */
  @Override
  public String stringApiValue() {
    return CycArrayList.stringApiValue(this);
  }
  
  public static String stringApiValue(List l) {
    if (l.isEmpty()) {
      return "(list)";
    }
    final int fullSlices = (l.size() / MAX_STRING_API_VALUE_LIST_LITERAL_SIZE);
    if (fullSlices > MAX_STRING_API_VALUE_LIST_LITERAL_SIZE) {
      // @todo this could be improved upon, since the 
      // (NCONC (LIST ... slice1 ... ) (LIST ... slice2 ...))
      // trick could be wrapped with another level of NCONC to handle even bigger
      // expressions, and so on and so forth, requiring only 
      // MAX_STRING_API_VALUE_LIST_LITERAL_SIZE+nesting depth stack space 
      // at any one point ...
      throw new IllegalArgumentException("Cannot currently handle LISTs longer than " + MAX_STRING_API_VALUE_LIST_LITERAL_SIZE * MAX_STRING_API_VALUE_LIST_LITERAL_SIZE);
    }
    final int tailSliceStart = fullSlices * MAX_STRING_API_VALUE_LIST_LITERAL_SIZE;
    final boolean fitsIntoOneSlice = fullSlices == 0;
    final StringBuilder result = new StringBuilder(l.size() * 20);
    final boolean properList = ((!(l instanceof CycList)) || ((CycList)l).isProperList());
    if (!fitsIntoOneSlice) {
      // we have multiple slices 
      result.append("(nconc").append(" ");
      for (int i = 0; i < fullSlices; i++) {
        int start = i * MAX_STRING_API_VALUE_LIST_LITERAL_SIZE;
        int end = start + MAX_STRING_API_VALUE_LIST_LITERAL_SIZE;
        // and full slices are ALL proper
        CycArrayList.appendSubSlice(l, result, start, end, true);
      }
    }
    // @note if fullSlices is 0, tailSliceStart will be 0 also
    appendSubSlice(l, result, tailSliceStart, CycArrayList.getProperListSize(l), properList);
    if (!fitsIntoOneSlice) {
      result.append(")");
    }
    return result.toString();
  }

  protected static StringBuilder appendSubSlice(List l, StringBuilder builder, int start, int end, boolean properList) {
    // note the asterisk, which results in a dotted list
    if (l instanceof UnmodifiableCycList) {
      throw new UnsupportedOperationException();
    }
    builder.append(properList ? "(list" : "(list*");
    for (int i = start; i < end; i++) {
      CycArrayList.appendElement(builder, l.get(i));
    }
    if (!properList) {
      ((CycArrayList)l).appendDottedElement(builder);
    }
    builder.append(")");
    return builder;
  }

  protected static void appendElement(StringBuilder builder, Object object) {
    if (object == null) {
      throw new BaseClientRuntimeException("Got unexpected null object.");
    }
    builder.append(" ");
    builder.append(DefaultCycObject.stringApiValue(object));
  }
  
  private void appendDottedElement(StringBuilder builder) {
    final E dottedObject = (E) ((dottedElement == null) ? LIST_NIL : dottedElement);
    appendElement(builder, dottedObject);    
  }

  /**
   * Returns this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value
   */
  @Override
  public Object cycListApiValue() {
    return cycListApiValue(false);
  }

  /**
   * Returns this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value.
   *
   * @param shouldQuote Should the list be SubL-quoted?
   * @return this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value
   */
  @Override
  public Object cycListApiValue(final boolean shouldQuote) {
    if (shouldQuote) {
      return makeCycList(CycObjectFactory.quote, this);
    } else {
      return this;
    }
  }

  /**
   * Returns a new CycArrayList, which is sorted in the default collating sequence.
   *
   * @return a new <tt>CycArrayList</tt>, sorted in the default collating sequence.
   */
  @Override
  public CycArrayList sort() {
    final CycArrayList sortedList = new CycArrayList(this);
    Collections.sort(sortedList, new CycListComparator());
    return sortedList;
  }

  /**
   * Returns a <tt>CycArrayListVisitor</tt> enumeration of the non-CycArrayList and non-nil elements.
   *
   * @return a <tt>CycArrayListVisitor</tt> enumeration of the non-CycArrayList and non-nil elements.
   */
  @Override
  public CycListVisitor cycListVisitor() {
    return new CycListVisitor(this);
  }

  /**
   * Returns the list of constants found in the tree
   *
   * @return the list of constants found in the tree
   */
  @Override
  public CycArrayList treeConstants() {
    final CycArrayList constants = new CycArrayList();
    final Stack stack = new Stack();
    stack.push(this);
    while (!stack.empty()) {
      final Object obj = stack.pop();
      if (obj instanceof CycConstantImpl) {
        constants.add(obj);
      } else if (obj instanceof CycAssertionImpl) {
        stack.push(((CycAssertionImpl) obj).getMt());
        pushTreeConstantElements(((CycAssertionImpl) obj).getFormula(), stack);
      } else if (obj instanceof Nart) {
        stack.push(((Nart) obj).getFunctor());
        pushTreeConstantElements(((Nart) obj).getArguments(), stack);
      } else if (obj instanceof CycArrayList) {
        pushTreeConstantElements(((CycArrayList) obj), stack);
      }
    }
    return constants;
  }

  private void pushTreeConstantElements(List list, Stack stack) {
    final Iterator iter = list.iterator();
    while (iter.hasNext()) {
      stack.push(iter.next());
    }
  }

  @Override
  public E get(int index) {
    if ((index == (size() - 1)) && (!isProperList())) {
      return getDottedElement();
    } else {
      return super.get(index);
    }
  }

  /**
   * Replaces the element at the specified position in this list with
   * the specified element.
   *
   * @param index index of element to replace.
   * @param element element to be stored at the specified position.
   * @return the element previously at the specified position.
   * @throws    IndexOutOfBoundsException if index out of range
   *		  <tt>(index &lt; 0 || index &gt;= size())</tt>.
   */
  @Override
  public E set(int index, E element) {
    if ((index == (size() - 1)) && (!isProperList())) {
      final E oldValue = getDottedElement();
      setDottedElement(element);
      return oldValue;
    } else {
      return super.set(index, element);
    }
  }

  /**
   * This behaves like the SubL function GETF, but returns null if the indicator is not present.
   */
  @Override
  public Object getf(CycSymbol indicator) {
    return getf(indicator, null);
  }

  /**
   * This behaves like the SubL function GETF
   * @param treatNilAsAbsent -- If true, return defaultResult when list contains NIL for indicator.
   */
  @Override
  public Object getf(CycSymbol indicator, Object defaultResult, boolean treatNilAsAbsent) {
    int indicatorIndex = firstEvenIndexOf(indicator);
    if (indicatorIndex == -1) { // the indicator is not present
      return defaultResult;
    } else {
      final Object value = get(indicatorIndex + 1);
      if (treatNilAsAbsent && CycObjectFactory.nil.equals(value)) {
        return defaultResult;
      } else {
        return value;
      }
    }
  }

  /**
   * This behaves like the SubL function GETF
   */
  @Override
  public Object getf(CycSymbol indicator, Object defaultResult) {
    return getf(indicator, defaultResult, false);
  }
  
  /**
   * 
   * @return true iff this CycArrayList is a property-list, which is a list with an even number of elements,
 consisting of alternating keywords and values, with no repeating keywords.
   */
  @Override
  public boolean isPlist() {
    boolean expectingKeyword = true;
    List<CycSymbolImpl> keywords = new ArrayList<CycSymbolImpl>();
    for (Object elt : this) {
        if (expectingKeyword) {
            if (elt instanceof CycSymbolImpl && ((CycSymbolImpl)elt).isKeyword() && keywords.contains(elt) == false) {
                expectingKeyword = false;
                keywords.add((CycSymbolImpl) elt);
            } else return false;
        } else { 
            expectingKeyword = true;
        }
    }
    if (expectingKeyword) {
        return true;
    } else { 
        return false;
    }
  }

  /**
   * @return a new CycArrayList representing the contents of map as a plist.
   */
  static public CycArrayList convertMapToPlist(final Map map) {
    final CycArrayList plist = new CycArrayList();
    if (map != null) {
      for (final Iterator<Map.Entry> i = map.entrySet().iterator(); i.hasNext();) {
        final Map.Entry entry = i.next();
        plist.add(entry.getKey());
        plist.add(entry.getValue());
      }
    }
    return plist;
  }

  private int firstEvenIndexOf(Object elem) {
    if (elem == null) {
      for (int i = 0; i < size(); i = i + 2) {
        if (get(i) == null) {
          return i;
        }
      }
    } else {
      for (int i = 0; i < size(); i = i + 2) {
        if (elem.equals(get(i))) {
          return i;
        }
      }
    }
    return -1;
  }

  @Override
  public Object findElementAfter(Object searchObject, Object notFound) {
    int i = 0;
    for (Object curElement : this) {
      if ((searchObject == curElement) || ((searchObject != null) && (searchObject.equals(curElement)))) {
        int index = i + 1;
        if (index >= size()) {
          throw new BaseClientRuntimeException("Search object: " + searchObject
                  + "  appears at end of list: " + this + "");
        }
        return get(index);
      }
      i++;
    }
    return notFound;
  }

  @Override
  public Object findElementAfter(Object searchObject) {
    String notFound = "notfoundstr_1230948u23jhiekdsfswkfjslkdfs";
    Object result = findElementAfter(searchObject, notFound);
    if (result == notFound) {
      throw new BaseClientRuntimeException("Search object: " + searchObject
              + "  is not found in: " + this + "");
    }
    return result;
  }

  /**
   * Returns a <tt>CycArrayList</tt> of all the indices of the given element within this CycArrayList.
   *
   * @param elem The element to search for in the list
   * @return a <tt>CycArrayList</tt> of all the indices of the given element within this CycArrayList.
   */
  @Override
  public CycArrayList allIndicesOf(Object elem) {
    CycArrayList result = new CycArrayList();
    if (elem == null) {
      for (int i = 0; i < size(); i++) {
        if (get(i) == null) {
          result.add(i);
        }
      }
    } else {
      for (int i = 0; i < size(); i++) {
        if (elem.equals(get(i))) {
          result.add(i);
        }
      }
    }
    return result;
  }

  /**
   * Returns the list of objects of the specified type found in the tree.
   *
   * @param cls What class to select from the tree
   * @return the list of objects of type <code>cls</code> found in the tree
   */
  @Override
  public CycArrayList treeGather(Class cls) {
    final CycArrayList result = new CycArrayList();
    final Stack stack = new Stack();
    stack.push(this);
    while (!stack.empty()) {
      final Object obj = stack.pop();
      if (cls.isInstance(obj)) {
        result.add(obj);
      } else if (obj instanceof CycArrayList) {
        CycArrayList l = (CycArrayList) obj;
        final Iterator iter = l.iterator();
        while (iter.hasNext()) {
          stack.push(iter.next());
        }
        if (!l.isProperList) {
          stack.push(l.getDottedElement());
        }
      }
    }
    return result;
  }

  /**
   * Returns true if the proper list tree contains the given object anywhere in the tree.
   *
   * @param object the object to be found in the tree.
   * @return true if the proper list tree contains the given object anywhere in the tree
   */
  @Override
  public boolean treeContains(Object object) {
    if (object instanceof Nart) {
      object = ((Nart) object).toCycList();
    }
    if (this.contains(object)) {
      return true;
    }
    for (int i = 0; i < this.size(); i++) {
      Object element = this.get(i);
      if (element instanceof Nart) {
        element = ((Nart) element).toCycList();
      }
      if (element.equals(object)) {
        return true;
      }
      if ((element instanceof CycArrayList) && (((CycArrayList) element).treeContains(object))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns <tt>true</tt> if the element is a member of this <tt>CycArrayList</tt> and
   * no element in <tt>CycArrayList</tt> otherElements precede it.
   *
   * @param element the element under consideration
   * @param otherElements the <tt>CycArrayList</tt> of other elements under consideration
   * @return <tt>true</tt> if the element is a member of this <tt>CycArrayList</tt> and
   * no elements in <tt>CycArrayList</tt> otherElements contained in this <tt>CycArrayList</tt>
   * precede it
   */
  @Override
  public boolean doesElementPrecedeOthers(final Object element, final CycList otherElements) {
    for (int i = 0; i < this.size(); i++) {
      if (element.equals(this.get(i))) {
        return true;
      }
      if (otherElements.contains(this.get(i))) {
        return false;
      }
    }
    return false;
  }

  /**
   * Returns the XML representation of this object.
   *
   * @return the XML representation of this object
   */
  @Deprecated
  public String toXMLString() throws IOException {
    final XmlStringWriter xmlStringWriter = new XmlStringWriter();
    toXML(xmlStringWriter, 0, false);
    return xmlStringWriter.toString();
  }

  /**
   * Prints the XML representation of the <ttt>CycArrayList</tt> to an <tt>XMLWriter</tt>
   *
   * @param xmlWriter the output XML serialization writer
   * @param indent specifies by how many spaces the XML output should be indented
   * @param relative specifies whether the indentation should be absolute --
   * indentation with respect to the beginning of a new line, relative = false
   * -- or relative to the indentation currently specified in the indent_string field
   * of the xml_writer object, relative = true.
   */
  @Deprecated
  public void toXML(final XmlWriter xmlWriter, final int indent,
          final boolean relative) throws IOException {
    final int startingIndent = xmlWriter.getIndentLength();
    xmlWriter.printXMLStartTag(cycListXMLTag, indent, relative, true);
    try {
      final Iterator iterator = this.iterator();
      Object arg;
      for (int i = 0, size = getProperListSize(); i < size; i++) {
        arg = iterator.next();
        toXML(arg, xmlWriter, indentLength, true);
      }
      if (!isProperList) {
        xmlWriter.printXMLStartTag(dottedElementXMLTag, indentLength, relative,
                true);
        toXML(dottedElement, xmlWriter, indentLength, true);
        xmlWriter.printXMLEndTag(dottedElementXMLTag, 0, true);
        xmlWriter.setIndent(-indentLength, true);
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    } finally {
      xmlWriter.printXMLEndTag(cycListXMLTag, 0, true);
    }
  }

  /**
   * Writes a CycArrayList element the the given XML output stream.
   *
   * @param object the object to be serialized as XML
   * @param xmlWriter the output XML serialization writer
   * @param indent specifies by how many spaces the XML output should be indented
   * @param relative specifies whether the indentation should be absolute --
   * indentation with respect to the beginning of a new line, relative = false
   * -- or relative to the indentation currently specified in the indent_string field
   * of the xml_writer object, relative = true.
   */
  @Deprecated
  public static void toXML(final Object object, final XmlWriter xmlWriter, final int indent, final boolean relative) throws IOException {
    final int startingIndent = xmlWriter.getIndentLength();
    if (object instanceof Integer) {
      xmlWriter.printXMLStartTag(integerXMLTag, indentLength, true, false);
      xmlWriter.print(object.toString());
      xmlWriter.printXMLEndTag(integerXMLTag);
    } else if (object instanceof String) {
      xmlWriter.printXMLStartTag(stringXMLTag, indentLength, true, false);
      xmlWriter.print(TextUtil.doEntityReference((String) object));
      xmlWriter.printXMLEndTag(stringXMLTag);
    } else if (object instanceof Double) {
      xmlWriter.printXMLStartTag(doubleXMLTag, indentLength, true, false);
      xmlWriter.print(object.toString());
      xmlWriter.printXMLEndTag(doubleXMLTag);
    } else if (object instanceof FortImpl) {
      ((FortImpl) object).toXML(xmlWriter, indentLength, true);
    } else if (object instanceof ByteArray) {
      ((ByteArray) object).toXML(xmlWriter, indentLength, true);
    } else if (object instanceof CycVariableImpl) {
      ((CycVariableImpl) object).toXML(xmlWriter, indentLength, true);
    } else if (object instanceof CycSymbolImpl) {
      ((CycSymbolImpl) object).toXML(xmlWriter, indentLength, true);
    } else if (object instanceof Guid) {
      GuidImpl.fromGuid((Guid) object).toXML(xmlWriter, indentLength, true);
    } else if (object instanceof CycArrayList) {
      ((CycArrayList) object).toXML(xmlWriter, indentLength, true);
    } else if (object instanceof FormulaImpl) {
      ((FormulaImpl) object).toXML(xmlWriter, indentLength, true);
    } else if (object instanceof CycAssertionImpl) {
      ((CycAssertionImpl) object).toXML(xmlWriter, indentLength, true);
    } else {
      throw new BaseClientRuntimeException("Invalid CycList object (" + object.getClass().getSimpleName()
              + ") " + object);
    }
    xmlWriter.setIndent(-indentLength, true);
    if (startingIndent != xmlWriter.getIndentLength()) {
      throw new BaseClientRuntimeException("Starting indent " + startingIndent + " is not equal to ending indent " + xmlWriter.getIndentLength() + " for object " + object);
    }
  }

  /**
   * Gets the value following the given keyword symbol.
   *
   * @param keyword the keyword symbol
   * @return the value following the given keyword symbol, or null if not found
   */
  @Override
  public Object getValueForKeyword(final CycSymbol keyword) {
    for (int i = 0; i < this.size() - 1; i++) {
      if (this.get(i).equals(keyword)) {
        return this.get(i + 1);
      }
    }
    return null;
  }

  /**
   * Forms a quote expression for the given object and adds it to the list.
   *
   * @param object the object to be quoted and added to this list
   */
  @Override
  public void addQuoted(final Object object) {
    this.add((E) makeCycList(CycObjectFactory.quote, object));
  }

  /**
   * Returns the object from the this CycArrayList according to the
 path specified by the given arg position.
   *
   * @param argPosition the given arg position
   * @return the object from this CycArrayList according to the
 path specified by the given (n1 n2 ...) zero-indexed path expression
   */
  @Override
  public Object getSpecifiedObject(final ArgPosition argPosition) {
    return getSpecifiedObject(new CycArrayList(argPosition.getPath()));
  }

  /**
   * Returns the object from the this CycArrayList according to the
 path specified by the given (n1 n2 ...) zero-indexed path expression.
   *
   * @param pathSpecification the given (n1 n2 ...) zero-indexed path expression
   * @return the object from this CycArrayList according to the
 path specified by the given (n1 n2 ...) zero-indexed path expression
   */
  @Override
  public Object getSpecifiedObject(final CycList pathSpecification) {
    if (pathSpecification.size() == 0) {
      return this;
    }
    Object answer = (CycArrayList) this.clone();
    CycList tempPathSpecification = pathSpecification;
    int index = 0;
    try {
      while (!tempPathSpecification.isEmpty()) {
        index = ((Integer) tempPathSpecification.first()).intValue();
        if (answer instanceof Nart) {
          if (index == 0) {
            answer = ((Nart) answer).getFunctor();
          } else {
            answer = ((Nart) answer).getArgument(index);
          }
        } else {
          answer = ((CycArrayList) answer).get(index);
        }
        tempPathSpecification = (CycArrayList) tempPathSpecification.rest();
      }
      return answer;
    } catch (Exception e) {
      throw new BaseClientRuntimeException("Can't get object specified by path expression: '" + pathSpecification + "' in forumla: '" + this + "'.  answer: " + answer + " index: " + index + "\n" + StringUtils.getStringForException(e));
    }
  }

  /**
   * Sets the object in this CycArrayList to the given value according to the
 path specified by the given ((n1 n2 ...) zero-indexed path expression.
   *
   * @param pathSpecification the (n1 n2 ...) zero-indexed path expression
   * @param value the given value
   */
  @Override
  public void setSpecifiedObject(ArgPosition pathSpecification, final Object value) {
    setSpecifiedObject(new CycArrayList(pathSpecification.getPath()), value);
  }

  /**
   * Sets the object in this CycArrayList to the given value according to the
 path specified by the given ((n1 n2 ...) zero-indexed path expression.
   *
   * @param pathSpecification the (n1 n2 ...) zero-indexed path expression
   * @param value the given value
   */
  @Override
  public void setSpecifiedObject(CycList pathSpecification, final Object value) {
    CycArrayList parentContainer = null;
    Object container = this;
    int parentIndex = -1;
    int index = ((Integer) pathSpecification.first()).intValue();
    pathSpecification = (CycArrayList) pathSpecification.rest();
    while (true) {
      if (container instanceof Nart) {
        // after the first iteration the imbedded container can be a Nart
        container = ((Nart) container).toCycList();
        parentContainer.set(parentIndex, container);
      }
      if (pathSpecification.isEmpty()) {
        break;
      }
      parentContainer = (CycArrayList) container;
      if (container instanceof CycArrayList) {
        container = ((CycArrayList) container).get(index);
      } else {
        throw new BaseClientRuntimeException("Don't know a path into: " + container);
      }
      parentIndex = index;
      index = ((Integer) pathSpecification.first()).intValue();
      pathSpecification = (CycArrayList) pathSpecification.rest();
    }
    if (container instanceof CycArrayList) {
      container = ((CycArrayList) container).set(index, value);
    } else if (container instanceof Nart) {
      if (index == 0) {
        ((Nart) container).setFunctor((FortImpl) value);
      } else {
        ((Nart) container).getArguments().set(index - 1, value);
      }
    } else {
      throw new BaseClientRuntimeException("Don't know about: " + container);
    }
  }

  @Override
  public void treeSubstitute(Object oldObject, Object newObject) {
    List<ArgPosition> locs = getArgPositionsForTerm(oldObject);
    for (ArgPosition loc : locs) {
      setSpecifiedObject(loc, newObject);
    }
  }

  /** Returns a list of arg positions that describe all the locations where
 the given term can be found in this CycArrayList. An arg position is a flat
   * list of Integers that give the nths (0 based) to get to a particular
   * sub term in a tree.
   * @param term The term to search for
   * @return The list of all arg postions where term can be found
   * class where possible.
   */
  @Override
  public List<ArgPosition> getArgPositionsForTerm(final Object term) {
    if (this.equals(term)) {
      return Collections.emptyList();
    }
    List<ArgPosition> result = new ArrayList<ArgPosition>();
    ArgPositionImpl curArgPosition = ArgPositionImpl.TOP;
    internalGetArgPositionsForTerm(term, this, curArgPosition, result);
    return result;
  }

  /** Private method used to implement getCycArgPositionForTerm() functionality.
   * @param term The term to search for
   * @param subTree The current sub part of the tree being explored
   * @param curPosPath The current arg position being explored
   * @param result Current store of arg positions found so far
   */
  private static void internalGetArgPositionsForTerm(Object term, Object subTree,
          final ArgPositionImpl curPosPath, final List<ArgPosition> result) {
    if (term instanceof Nart) {
      term = ((Nart) term).toCycList();
    }
    if (term == subTree) {
      final ArgPositionImpl newArgPos = new ArgPositionImpl(curPosPath.getPath());
      result.add(newArgPos);
      return;
    }
    if (subTree == null) {
      return;
    }
    if (subTree instanceof Nart) {
      subTree = ((Nart) subTree).toCycList();
    }
    if (subTree.equals(term)) {
      final ArgPositionImpl newArgPos = new ArgPositionImpl(curPosPath.getPath());
      result.add(newArgPos);
      return;
    }
    if ((subTree instanceof CycArrayList) && ((CycArrayList) subTree).treeContains(term)) {
      int newPos = 0;
      for (Iterator iter = ((List) subTree).iterator(); iter.hasNext(); newPos++) {
        final ArgPositionImpl newPosPath = new ArgPositionImpl(curPosPath.getPath());
        newPosPath.extend(newPos);
        internalGetArgPositionsForTerm(term, iter.next(), newPosPath, result);
      }
    }
  }

  @Override
  public List getReferencedConstants() {
    return treeConstants();
  }
  //// serializable

  private void writeObject(ObjectOutputStream stream) throws java.io.IOException {
    stream.defaultWriteObject();
    if (!isProperList) {
      stream.writeBoolean(false);
      stream.writeObject(this.dottedElement);
    } else {
      stream.writeBoolean(true);
    }
  }

  private void readObject(ObjectInputStream stream) throws java.io.IOException,
          java.lang.ClassNotFoundException {
    stream.defaultReadObject();
    isProperList = stream.readBoolean();
    if (!isProperList) {
      dottedElement = (E) stream.readObject();
    }
  }
  
  /**
   * Unmodifiable CycList. Attempts to invoke methods which would alter the underlying structure 
   * will cause a {@link java.lang.UnsupportedOperationException} to be thrown.
   * 
   * <p><strong>Note regarding Java 8:</strong> In order to maintain backwards compatibility to Java
   * 6, the following methods have not been overridden, and are still capable of modifying a
   * UnmodifiableCycList instance:
   * <ul>
   *   <li>Collection#removeIf(Predicate<? super E> filter)</li>
   *   <li>List#replaceAll(UnaryOperator<E> operator)</li>
   * </ul>
   * 
   * @param <E> 
   */
  static public class UnmodifiableCycList<E> extends CycArrayList<E> {

    public UnmodifiableCycList(CycList<? extends E> list) {
      for (int i = 0; i < list.getProperListSize(); i++) {
        super.add((E) list.get(i));
      }
      if (!list.isProperList()) {
        super.setDottedElement(list.getDottedElement());
      }
    }

    private UnmodifiableCycList() {
      super();
    }

    @Override
    public boolean add(E e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(boolean b) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(double d) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(float f) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(int i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(long l) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> col) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addAllNew(Collection<? extends E> objects) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addNew(E object) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addQuoted(Object object) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CycArrayList<E> addToBeginning(E element) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CycArrayList<E> appendElement(E object) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CycArrayList<E> appendElement(boolean b) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CycArrayList<E> appendElement(int i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CycArrayList<E> appendElement(long l) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CycArrayList<E> appendElements(CycList<? extends E> cycList) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
      throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CycArrayList removeDuplicates() {
      throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
      throw new UnsupportedOperationException();
    }

    @Override
    public E set(int index, E element) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setDottedElement(Object dottedElement) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setSpecifiedObject(CycList pathSpecification, Object value) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public CycArrayList sort() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CycArrayList subst(Object newObject, Object oldObject) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void trimToSize() {
      throw new UnsupportedOperationException();
    }
    
    // Java 8 methods
    
    public void sort(Comparator c) {
      // Omits @Override annotation to maintain backwards compatibility with < Java 8.
      // - nwinant, 2016-01-05
      throw new UnsupportedOperationException();
    }
    
    /*
    These methods take Java 8 arguments, so there's no clean way to override them in < Java 8...
    TODO: Uncomment these once Base Client has moved to Java 8. - nwinant, 2016-01-05
    
    public void replaceAll(UnaryOperator operator) {
      throw new UnsupportedOperationException();
    }
    
    public boolean removeIf(Predicate filter) {
      throw new UnsupportedOperationException();
    }
    */
  }
  
   /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        if ((index < 0) || (index > size())) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
        return new CycListItr(index);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @see #listIterator(int)
     */
    @Override
    public ListIterator<E> listIterator() {
        return new CycListItr(0);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new CycItr();
    }
    
    private void removeInt(int i) {
      remove(i);
    }
    
    private void setInt(int i, E val) {
      set(i, val);
    }
    
    private void addInt(int i, E val) {
      add(i, val);
    }

    /**
     * An optimized version of AbstractList.Itr
     */
    private class CycItr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;
        int mySize = getProperListSize();

        @Override
        public boolean hasNext() {
            return (cursor != mySize);
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size()) {
                throw new NoSuchElementException();
            }
            cursor = i + 1;
            return (E) get(lastRet = i);
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                removeInt(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private class CycListItr extends CycItr implements ListIterator<E> {
        CycListItr(int index) {
            super();
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return (cursor != 0);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return (cursor - 1);
        }

        @SuppressWarnings("unchecked")
        @Override
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0) {
                throw new NoSuchElementException();
            }
            cursor = i;
            return (E) get(lastRet = i);
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();

            try {
                setInt(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                addInt(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

  /*
  public static void main(String[] args) {
    CycArrayList list = new CycArrayList();
    list.add(new Integer(1));
    list.add(new Integer(2));
    CycArrayList list2 = new CycArrayList();
    list2.add(new Integer(1));
    list2.add(new Integer(2));
    CycArrayList list3 = new CycArrayList();
    list3.add(new Integer(1));
    list2.add(list3);
    list.add(list2);
    list.add(new Integer(3));
    list.add(new Integer(1));
    System.out.println("Got original object: " + list);
    list.treeSubstitute(new Integer(1), new Integer(2));
    System.out.println("Got transformed object; " + list);
  }
  */
}
