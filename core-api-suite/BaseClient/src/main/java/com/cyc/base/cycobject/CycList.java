package com.cyc.base.cycobject;

/*
 * #%L
 * File: CycList.java
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

import com.cyc.kb.ArgPosition;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author nwinant
 */
public interface CycList<E> extends CycObject, List<E>, Serializable {
  
  /**
   * the limit on lists that are returned as one LIST expression;
   * lists longer than this are broken down into NCONC of LISTs expressions
   */
  int MAX_STRING_API_VALUE_LIST_LITERAL_SIZE = 2048;

  boolean add(E e);

  /**
   * Adds the given elements to this list if they are not already contained.
   */
  void addAllNew(final Collection<? extends E> objects);

  /**
   * Adds the given element to this list if it is not already contained.
   */
  void addNew(final E object);

  /**
   * Forms a quote expression for the given object and adds it to the list.
   *
   * @param object the object to be quoted and added to this list
   */
  void addQuoted(final Object object);

  /**
   * Inserts the specified element at the beginning of this list.
   * Shifts all current elements to the right (adds one to their indices).
   *
   * @param element element to be inserted
   * @return the passed-in list with <tt>element</tt> inserted.
   */
  CycList<E> addToBeginning(final E element);

  /**
   * Returns a <tt>CycList</tt> of all the indices of the given element within this CycList.
   *
   * @param elem The element to search for in the list
   * @return a <tt>CycList</tt> of all the indices of the given element within this CycList.
   */
  CycList allIndicesOf(Object elem);

  /**
   * Appends the given element to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param object the object element to add
   * @return the list after adding the given element to the end
   */
  CycList<E> appendElement(final E object);

  /**
   * Appends the given element to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param i the integer element to add
   * @return the list after adding the given element to the end
   */
  CycList appendElement(final int i);

  /**
   * Appends the given element to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param l the long element to add
   * @return the list after adding the given element to the end
   */
  CycList appendElement(final long l);

  /**
   * Appends the given element to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param b the boolean element to add
   * @return the list after adding the given element to the end
   */
  CycList appendElement(final boolean b);

  /**
   * Appends the given elements to the end of the list and returns the list (useful when nesting method calls).
   *
   * @param cycList the elements to add
   * @return the list after adding the given elements to the end
   */
  CycList<E> appendElements(final CycList<? extends E> cycList);

  /**
   * Creates and returns a copy of this <tt>CycList</tt>.
   *
   * @return a clone of this instance
   */
  Object clone();

  /**
   * Returns a <tt>CycList</tt> of the length N combinations of sublists from this
   * object.  This algorithm preserves the list order with the sublists.
   *
   * @param n the length of the sublist
   * @return a <tt>CycList</tt> of the length N combinations of sublists from this
   * object
   */
  CycList<CycList<E>> combinationsOf(int n);

  /**
   * Returns true iff this list contains duplicate elements.
   *
   * @return true iff this list contains duplicate elements
   */
  boolean containsDuplicates();

  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @param shouldQuote Should the list be SubL-quoted?
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
  Object cycListApiValue(final boolean shouldQuote);

  /**
   * Returns a <tt>CycListVisitor</tt> enumeration of the non-CycList and non-nil elements.
   *
   * @return a <tt>CycListVisitor</tt> enumeration of the non-CycList and non-nil elements.
   */
  Enumeration cycListVisitor();

  /**
   * Returns a cyclified string representation of the <tt>CycList</tt>.
   * Embedded constants are prefixed with "#$".
   *
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  String cyclify();

  /**
   * Returns a cyclified string representation of the <tt>CycList</tt>.
   * Embedded constants are prefixed with "#$".  Embedded quote and backslash
   * chars in strings are escaped.
   *
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  String cyclifyWithEscapeChars();

  /**
   * Returns a cyclified string representation of the <tt>CycList</tt>.
   * Embedded constants are prefixed with "#$".  Embedded quote and backslash
   * chars in strings are escaped.
   *
   * @param isApi Should the list be cyclified for as an API call?
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  String cyclifyWithEscapeChars(boolean isApi);

  /**
   * Creates and returns a deep copy of this <tt>CycList</tt>.  In a deep copy,
   * directly embedded <tt>CycList</tt> objects are also deep copied.  Objects
   * which are not CycLists are cloned.
   *
   * @return a deep copy of this <tt>CycList</tt>
   */
  CycList<E> deepCopy();

  /**
   * Destructively delete duplicates from the list.
   * @return <code>this</code> list with the duplicates deleted.
   */
  CycList deleteDuplicates();

  /**
   * Returns <tt>true</tt> if the element is a member of this <tt>CycList</tt> and
   * no element in <tt>CycList</tt> otherElements precede it.
   *
   * @param element the element under consideration
   * @param otherElements the <tt>CycList</tt> of other elements under consideration
   * @return <tt>true</tt> if the element is a member of this <tt>CycList</tt> and
   * no elements in <tt>CycList</tt> otherElements contained in this <tt>CycList</tt>
   * precede it
   */
  boolean doesElementPrecedeOthers(final Object element, final CycList otherElements);

  /** Returns true if the given object is equal to this object as EL CycL expressions
   *
   * @param o the given object
   * @return true if the given object is equal to this object as EL CycL expressions, otherwise
   * return false
   */
  boolean equalsAtEL(Object o);

  Object findElementAfter(Object searchObject, Object notFound);

  Object findElementAfter(Object searchObject);

  /**
   * Returns the first element of the <tt>CycList</tt>.
   *
   * @return the <tt>Object</tt> which is the first element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  E first();

  /**
   * Flatten the list. Recursively iterate through tree, and return a list of
   * the atoms found.
   * @return List of atoms in <code>this</code> CycList.
   */
  CycList<E> flatten();

  /**
   * Returns the fourth element of the <tt>CycList</tt>.
   *
   * @return the <tt>Object</tt> which is the fourth element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  E fourth();

  /** Returns a list of arg positions that describe all the locations where
   * the given term can be found in this CycList. An arg position is a flat
   * list of Integers that give the nths (0 based) to get to a particular
   * sub term in a tree.
   * @param term The term to search for
   * @return The list of all arg postions where term can be found
   * class where possible.
   */
  List<ArgPosition> getArgPositionsForTerm(final Object term);

  /**
   * Gets the dotted element.
   *
   * @return the <tt>Object</tt> which forms the dotted element of this <tt>CycList</tt>
   */
  E getDottedElement();

  int getProperListSize();

  /**
   * Returns the object from the this CycList according to the
   * path specified by the given arg position.
   *
   * @param argPosition the given arg position
   * @return the object from this CycList according to the
   * path specified by the given (n1 n2 ...) zero-indexed path expression
   */
  Object getSpecifiedObject(final ArgPosition argPosition);

  /**
   * Returns the object from the this CycList according to the
   * path specified by the given (n1 n2 ...) zero-indexed path expression.
   *
   * @param pathSpecification the given (n1 n2 ...) zero-indexed path expression
   * @return the object from this CycList according to the
   * path specified by the given (n1 n2 ...) zero-indexed path expression
   */
  Object getSpecifiedObject(final CycList pathSpecification);

  /**
   * Gets the value following the given keyword symbol.
   *
   * @param keyword the keyword symbol
   * @return the value following the given keyword symbol, or null if not found
   */
  Object getValueForKeyword(final CycSymbol keyword);

  /**
   * This behaves like the SubL function GETF, but returns null if the indicator is not present.
   */
  Object getf(CycSymbol indicator);

  /**
   * This behaves like the SubL function GETF
   * @param treatNilAsAbsent -- If true, return defaultResult when list contains NIL for indicator.
   */
  Object getf(CycSymbol indicator, Object defaultResult, boolean treatNilAsAbsent);

  /**
   * This behaves like the SubL function GETF
   */
  Object getf(CycSymbol indicator, Object defaultResult);

  /**
   *
   * @return true iff this CycList is a property-list, which is a list with an even number of elements,
   * consisting of alternating keywords and values, with no repeating keywords.
   */
  boolean isPlist();

  /**
   * Returns <tt>true</tt> if this is a proper list.
   *
   * @return <tt>true</tt> if this is a proper list, otherwise return <tt>false</tt>
   */
  boolean isProperList();

  /**
   * Answers true iff the CycList contains valid elements.  This is a necessary, but
   * not sufficient condition for CycL well-formedness.
   */
  boolean isValid();

  /**
   * Returns an iterator over the elements in this list in proper sequence.
   *
   * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
   *
   * @return an iterator over the elements in this list in proper sequence
   */
  Iterator<E> iterator();

  /**
   * Returns the last element of the <tt>CycList</tt>.
   *
   * @return the <tt>Object</tt> which is the last element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  E last();

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
  ListIterator<E> listIterator(int index);

  /**
   * Returns a list iterator over the elements in this list (in proper
   * sequence).
   *
   * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
   *
   * @see #listIterator(int)
   */
  ListIterator<E> listIterator();

  /**
   * Returns a random ordering of the <tt>CycList</tt> without recursion.
   *
   * @return a random ordering of the <tt>CycList</tt> without recursion
   */
  CycList randomPermutation();

  /**
   * Remove duplicates from the list.  Just like #deleteDuplicates but
   * non-destructive.
   * @return A new list with the duplicates removed.
   */
  CycList removeDuplicates();

  /**
   * Returns a new CycList formed by removing the first element, in in the case of a
   * dotted pair, returns the dotted element.
   *
   * @return the CycList after removing the first element, in in the case of a
   * dotted pair, returns the dotted element.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  Object rest();

  /**
   * Returns a new <tt>CycList</tt> whose elements are the reverse of
   * this <tt>CycList</tt>, which is unaffected.
   *
   * @return new <tt>CycList</tt> with elements reversed.
   */
  CycList<E> reverse();

  /**
   * Returns the second element of the <tt>CycList</tt>.
   *
   * @return the <tt>Object</tt> which is the second element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  E second();

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
  E set(int index, E element);

  /**
   * Sets the dotted element and set the improper list attribute to <tt>true</tt>.
   */
  void setDottedElement(final E dottedElement);

  /**
   * Sets the object in this CycList to the given value according to the
   * path specified by the given ((n1 n2 ...) zero-indexed path expression.
   *
   * @param pathSpecification the (n1 n2 ...) zero-indexed path expression
   * @param value the given value
   */
  void setSpecifiedObject(ArgPosition pathSpecification, final Object value);

  /**
   * Sets the object in this CycList to the given value according to the
   * path specified by the given ((n1 n2 ...) zero-indexed path expression.
   *
   * @param pathSpecification the (n1 n2 ...) zero-indexed path expression
   * @param value the given value
   */
  void setSpecifiedObject(CycList pathSpecification, final Object value);

  /** Returns the CycList size including the optional dotted element.  Note that this fools list iterators.
   *
   * @return the CycList size including the optional dotted element
   */
  int size();

  /**
   * Returns a new CycList, which is sorted in the default collating sequence.
   *
   * @return a new <tt>CycList</tt>, sorted in the default collating sequence.
   */
  CycList sort();

  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   * @throws IllegalArgumentException if the total size of the list exceeds
   * MAX_STRING_API_VALUE_LIST_LITERAL_SIZE times MAX_STRING_API_VALUE_LIST_LITERAL_SIZE in size,
   * because of the danger of causing a stack overflow in the communication
   * with the SubL interpreter
   */
  String stringApiValue();

  /**
   * Returns a new <tt>CycList</tt> with every occurrance of <tt>Object</tt> oldObject
   * replaced by <tt>Object</tt> newObject.  Substitute recursively into embedded
   * <tt>CycList</tt> objects.
   *
   * @return a new <tt>CycList</tt> with every occurrance of <tt>Object</tt> oldObject
   * replaced by <tt>Object</tt> newObject
   */
  CycList subst(final E newObject, final E oldObject);

  /**
   * Returns the third element of the <tt>CycList</tt>.
   *
   * @return the <tt>Object</tt> which is the third element of the list.
   * @throws BaseClientRuntimeException if list is not long enough.
   */
  E third();

  /** Convert this to a Map.  This method is only valid if the list is an association list.
   *
   * @return the Map
   */
  Map<Object, Object> toMap();

  /**
   * Returns a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycList</tt>.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycList</tt>
   * @return a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycList</tt>.
   */
  String toPrettyCyclifiedString(String indent);

  /**
   * Returns a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycList</tt> with embedded strings escaped.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycList</tt>
   * @return a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycList</tt>.
   */
  String toPrettyEscapedCyclifiedString(String indent);

  /**
   * Returns a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycList</tt>.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycList</tt>
   * @return a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycList</tt>.
   */
  String toPrettyString(String indent);

  /**
   * Returns a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycList</tt>.
   * @param indent the indent string that is added before the
   * <tt>String</tt> representation this <tt>CycList</tt>
   * @param incrementIndent the indent string that to the <tt>String</tt>
   * representation this <tt>CycList</tt>is added at each level
   * of indenting
   * @param newLineString the string added to indicate a new line
   * @param shouldCyclify indicates that the output constants should have #$ prefix
   * @param shouldEscape indicates that embedded strings should have appropriate escapes for the SubL reader
   * @return a `pretty-printed' <tt>String</tt> representation of this
   * <tt>CycList</tt>.
   */
  String toPrettyStringInt(final String indent, final String incrementIndent, final String newLineString, final boolean shouldCyclify, final boolean shouldEscape);
  
  /**
   * Returns the list of constants found in the tree
   *
   * @return the list of constants found in the tree
   */
  CycList treeConstants();

  /**
   * Returns true if the proper list tree contains the given object anywhere in the tree.
   *
   * @param object the object to be found in the tree.
   * @return true if the proper list tree contains the given object anywhere in the tree
   */
  boolean treeContains(Object object);

  /**
   * Returns the list of objects of the specified type found in the tree.
   *
   * @param cls What class to select from the tree
   * @return the list of objects of type <code>cls</code> found in the tree
   */
  CycList treeGather(Class cls);

  void treeSubstitute(Object oldObject, Object newObject);
  
}
