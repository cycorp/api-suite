package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: AbstractTimeInterval.java
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

import com.cyc.baseclient.datatype.ContinuousTimeInterval;
import java.util.Date;

/**
 * Implementation of high-level time interval methods.
 * @author baxter
 */
public abstract class AbstractTimeInterval implements TimeInterval {

  /**
   * Do this interval and the specified interval start simultaneously?
   *
   * @param interval
   * @return true iff this interval and the specified interval start
   * simultaneously.
   */
  public boolean cooriginatesWith(AbstractTimeInterval interval) {
    return !startsBeforeStartingOf(interval) && !startsAfterStartingOf(interval);
  }

  /**
   * Do this interval and the specified interval end simultaneously?
   *
   * @param interval
   * @return true iff this interval and the specified interval end
   * simultaneously.
   */
  public boolean coterminatesWith(AbstractTimeInterval interval) {
    return !endsBeforeEndingOf(interval) && !endsAfterEndingOf(interval);
  }

  /**
   * Does this interval end after the specified date?
   *
   * @param date
   * @return true iff this interval ends after date.
   */
  public boolean endsAfter(Date date) {
    return getEnd().after(date);
  }

  /**
   * Does this interval end after the specified interval ends?
   *
   * @param interval
   * @return true iff this interval ends after the specified interval ends.
   */
  public boolean endsAfterEndingOf(AbstractTimeInterval interval) {
    return endsAfter(interval.getEnd()) || (endsOn(interval.getEnd()) && !interval.getIncludesEnd());
  }

  /**
   * Does this interval end after the specified interval starts?
   *
   * @param interval
   * @return true iff this interval ends after the specified interval starts.
   */
  public boolean endsAfterStartingOf(AbstractTimeInterval interval) {
    return endsAfter(interval.getStart());
  }

  /**
   * Does this interval end before the specified date?
   *
   * @param date
   * @return true iff this interval ends before date.
   */
  public boolean endsBefore(Date date) {
    return !endsAfter(date) && !endsOn(date);
  }

  /**
   * Does this interval end before the specified interval ends?
   *
   * @param interval
   * @return true iff this interval ends before the specified interval ends.
   */
  public boolean endsBeforeEndingOf(AbstractTimeInterval interval) {
    if (this.getEnd().before(interval.getEnd())) {
      return true;
    } else if (this.getEnd().equals(interval.getEnd())) {
      return !this.getIncludesEnd() && interval.getIncludesEnd();
    } else {
      return false;
    }
  }

  /**
   * Does this interval end before the specified interval starts?
   *
   * @param interval
   * @return true iff this interval ends before the specified interval starts.
   */
  public boolean endsBeforeStartingOf(AbstractTimeInterval interval) {
    return endsBefore(interval.getStart()) || (endsOn(interval.getStart()) && !this.getIncludesEnd());
  }

  /**
   * Does this interval end during the specified interval?
   *
   * @param interval
   * @return true iff this interval's end is subsumed by interval.
   */
  public boolean endsDuring(AbstractTimeInterval interval) {
    return interval.subsumes(getEnd()) || coterminatesWith(interval);
  }

  /**
   * Does this interval end on the specified date?
   *
   * @param date
   * @return true iff this interval ends on date.
   */
  public boolean endsOn(Date date) {
    return getEnd().equals(date) && getIncludesEnd();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ContinuousTimeInterval other = (ContinuousTimeInterval) obj;
    return this.cooriginatesWith(other) && this.coterminatesWith(other);
  }

  /**
   * Does this interval start after the specified date?
   *
   * @param date
   * @return true iff this interval starts after date.
   */
  public boolean startsAfter(Date date) {
    return !startsBefore(date) && !startsOn(date);
  }

  /**
   * Does this interval start after the specified interval ends?
   *
   * @param interval
   * @return true iff this interval starts after the specified interval ends.
   */
  public boolean startsAfterEndingOf(AbstractTimeInterval interval) {
    return startsAfter(interval.getEnd()) || (startsOn(interval.getEnd()) && !interval.getIncludesEnd());
  }

  /**
   * Does this interval start after the specified interval starts?
   *
   * @param interval
   * @return true iff this interval starts after the specified interval starts.
   */
  public boolean startsAfterStartingOf(AbstractTimeInterval interval) {
    if (this.getStart().after(interval.getStart())) {
      return true;
    } else if (this.getStart().equals(interval.getStart())) {
      return !this.getIncludesStart() && interval.getIncludesStart();
    } else {
      return false;
    }
  }

  /**
   * Does this interval start before the specified date?
   *
   * @param date
   * @return true iff this interval starts before date.
   */
  public boolean startsBefore(Date date) {
    return getStart().before(date);
  }

  /**
   * Does this interval start before the specified interval ends?
   *
   * @param interval
   * @return true iff this interval starts before the specified interval ends.
   */
  public boolean startsBeforeEndingOf(AbstractTimeInterval interval) {
    return startsBefore(interval.getEnd());
  }

  /**
   * Does this interval start before the specified interval starts?
   *
   * @param interval
   * @return true iff this interval starts before the specified interval starts.
   */
  public boolean startsBeforeStartingOf(AbstractTimeInterval interval) {
    return startsBefore(interval.getStart()) || (startsOn(interval.getStart()) && !interval.getIncludesStart());
  }

  /**
   * Does this interval start during the specified interval?
   *
   * @param interval
   * @return true iff this interval's start is subsumed by interval.
   */
  public boolean startsDuring(AbstractTimeInterval interval) {
    return interval.subsumes(getStart()) || cooriginatesWith(interval);
  }

  /**
   * Does this interval start on the specified date?
   *
   * @param date
   * @return true iff this interval starts on date.
   */
  public boolean startsOn(Date date) {
    return getStart().equals(date) && getIncludesStart();
  }

  /**
   * Does this interval subsume the specified date?
   *
   * @param date
   * @return true iff date falls within this interval.
   */
  public boolean subsumes(Date date) {
    return !startsAfter(date) && !endsBefore(date);
  }

  /**
   * Does this interval subsume the specified interval?
   *
   * @param interval
   * @return true iff interval starts and ends during this interval.
   */
  public boolean subsumes(AbstractTimeInterval interval) {
    return interval.startsDuring(this) && interval.endsDuring(this);
  }
  
}
