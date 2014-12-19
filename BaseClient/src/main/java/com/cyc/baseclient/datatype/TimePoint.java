package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: TimePoint.java
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

import java.util.Date;
import com.cyc.base.cycobject.CycObject;

/**
 * Time intervals whose start and end are the same and include both.
 */
public interface TimePoint extends TimeInterval {

  /**
   * 
   * @return the Date that corresponds to this time point.
   */
  Date asDate();

  public static abstract class AbstractTimePoint extends AbstractTimeInterval implements TimePoint {

    public AbstractTimePoint() {
    }

    @Override
    public Date getEnd() {
      return asDate();
    }

    @Override
    public boolean getIncludesEnd() {
      return true;
    }

    @Override
    public boolean getIncludesStart() {
      return true;
    }

    @Override
    public Date getStart() {
      return asDate();
    }
  }

  /**
   * The class of time points that are fixed in time.
   * <p>Invoking {@link #asDate()} on one of these will always return the same date.
   */
  public static class FixedTimePoint extends AbstractTimePoint implements TimePoint {

    final Date date;

    public FixedTimePoint(final Date date) {
      this.date = date;
    }

    @Override
    public Date asDate() {
      return date;
    }

    @Override
    public CycObject toCycTerm() {
      return DateConverter.getInstance().toCycTerm(asDate());
    }
    public static final FixedTimePoint BEGINNING_OF_TIME = new FixedTimePoint(new Date(Long.MIN_VALUE)) {
      @Override
      public CycObject toCycTerm() {
        return ContinuousTimeInterval.BEGINNING_OF_TIME;
      }
    };
    public static final FixedTimePoint END_OF_TIME = new FixedTimePoint(new Date(Long.MAX_VALUE)) {
      @Override
      public CycObject toCycTerm() {
        return ContinuousTimeInterval.END_OF_TIME;
      }
    };
  }

  /**
   * The class of time points that are not fixed in time.
   * <p>Invoking {@link #asDate()} on one of these will not necessarily return the same date.
   */
  public static abstract class NonFixedTimePoint extends AbstractTimePoint implements TimePoint {

    public NonFixedTimePoint() {
    }
    public static final NonFixedTimePoint NOW = new NonFixedTimePoint() {
      @Override
      public CycObject toCycTerm() {
        return ContinuousTimeInterval.NOW;
      }

      @Override
      public Date asDate() {
        return new Date();
      }
    };
  }
}
