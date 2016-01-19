package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: TimeInterval.java
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

import com.cyc.baseclient.datatype.AbstractTimeInterval;
import java.util.Date;
import com.cyc.base.cycobject.CycObject;

/**
 *
 * @author baxter
 */
public interface TimeInterval {

  /**
   * Returns the ending date of the interval.
   *
   * @return the ending date of the interval.
   */
  Date getEnd();

  /**
   * Does this time interval include its end? If so, it ends when its end ends.
   * If not, it ends immediately prior to its end.
   *
   * @return true iff this time interval includes its end.St
   */
  boolean getIncludesEnd();

  /**
   * Does this time interval include its start? If so, it begins when its start
   * begins. If not, it begins immediately thereafter.
   *
   * @return true iff this time interval includes its start.
   */
  boolean getIncludesStart();

  /**
   * Returns the starting date of the interval.
   *
   * @return the starting date of the interval.
   */
  Date getStart();

  /**
   * Do this interval and the specified interval start simultaneously?
   *
   * @param interval
   * @return true iff this interval and the specified interval start
   * simultaneously.
   */
  boolean cooriginatesWith(AbstractTimeInterval interval);

  /**
   * Do this interval and the specified interval end simultaneously?
   *
   * @param interval
   * @return true iff this interval and the specified interval end
   * simultaneously.
   */
  boolean coterminatesWith(AbstractTimeInterval interval);

  /**
   * Does this interval end after the specified date?
   *
   * @param date
   * @return true iff this interval ends after date.
   */
  boolean endsAfter(Date date);

  /**
   * Does this interval end after the specified interval ends?
   *
   * @param interval
   * @return true iff this interval ends after the specified interval ends.
   */
  boolean endsAfterEndingOf(AbstractTimeInterval interval);

  /**
   * Does this interval end after the specified interval starts?
   *
   * @param interval
   * @return true iff this interval ends after the specified interval starts.
   */
  boolean endsAfterStartingOf(AbstractTimeInterval interval);

  /**
   * Does this interval end before the specified date?
   *
   * @param date
   * @return true iff this interval ends before date.
   */
  boolean endsBefore(Date date);

  /**
   * Does this interval end before the specified interval ends?
   *
   * @param interval
   * @return true iff this interval ends before the specified interval ends.
   */
  boolean endsBeforeEndingOf(AbstractTimeInterval interval);

  /**
   * Does this interval end before the specified interval starts?
   *
   * @param interval
   * @return true iff this interval ends before the specified interval starts.
   */
  boolean endsBeforeStartingOf(AbstractTimeInterval interval);

  /**
   * Does this interval end during the specified interval?
   *
   * @param interval
   * @return true iff this interval's end is subsumed by interval.
   */
  boolean endsDuring(AbstractTimeInterval interval);

  /**
   * Does this interval end on the specified date?
   *
   * @param date
   * @return true iff this interval ends on date.
   */
  boolean endsOn(Date date);

  /**
   * Does this interval start after the specified date?
   *
   * @param date
   * @return true iff this interval starts after date.
   */
  boolean startsAfter(Date date);

  /**
   * Does this interval start after the specified interval ends?
   *
   * @param interval
   * @return true iff this interval starts after the specified interval ends.
   */
  boolean startsAfterEndingOf(AbstractTimeInterval interval);

  /**
   * Does this interval start after the specified interval starts?
   *
   * @param interval
   * @return true iff this interval starts after the specified interval starts.
   */
  boolean startsAfterStartingOf(AbstractTimeInterval interval);

  /**
   * Does this interval start before the specified date?
   *
   * @param date
   * @return true iff this interval starts before date.
   */
  boolean startsBefore(Date date);

  /**
   * Does this interval start before the specified interval ends?
   *
   * @param interval
   * @return true iff this interval starts before the specified interval ends.
   */
  boolean startsBeforeEndingOf(AbstractTimeInterval interval);

  /**
   * Does this interval start before the specified interval starts?
   *
   * @param interval
   * @return true iff this interval starts before the specified interval starts.
   */
  boolean startsBeforeStartingOf(AbstractTimeInterval interval);

  /**
   * Does this interval start during the specified interval?
   *
   * @param interval
   * @return true iff this interval's start is subsumed by interval.
   */
  boolean startsDuring(AbstractTimeInterval interval);

  /**
   * Does this interval start on the specified date?
   *
   * @param date
   * @return true iff this interval starts on date.
   */
  boolean startsOn(Date date);

  /**
   * Does this interval subsume the specified interval?
   *
   * @param interval
   * @return true iff interval starts and ends during this interval.
   */
  boolean subsumes(AbstractTimeInterval interval);

  /**
   * Does this interval subsume the specified date?
   *
   * @param date
   * @return true iff date falls within this interval.
   */
  boolean subsumes(Date date);

  /**
   * Return the CycObject corresponding to this time interval.
   *
   * @return the corresponding CycObject
   * @see com.cyc.baseclient.cycobject.DefaultCycObject
   */
  CycObject toCycTerm();
  
}
