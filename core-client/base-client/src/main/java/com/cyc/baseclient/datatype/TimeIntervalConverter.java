/*
 * A converter utility for time intervals.
 */
package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: TimeIntervalConverter.java
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

import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.Naut;
import com.cyc.baseclient.datatype.ContinuousTimeInterval.TimeIntervalFunction;
import com.cyc.baseclient.exception.CycParseException;

/**
 *
 * @author baxter
 */
public class TimeIntervalConverter extends DataTypeConverter<TimeInterval> {

  //// Constructors
  /** Creates a new instance of TimeIntervalConverter. */
  private TimeIntervalConverter() {
  }

  //// Public Area
  /** Returns an instance of
   * <code>TimeIntervalConverter</code>.
   *
   * If an instance has already been created, the existing one will be returned.
   * Otherwise, a new one will be created.
   *
   * @return The singleton instance of this class.
   */
  public static TimeIntervalConverter getInstance() {
    return SHARED_INSTANCE;
  }

  public static TimeInterval parseCycInterval(CycObject cycObject) throws CycParseException {
    return getInstance().fromCycTerm(cycObject);
  }

  @Override
  protected CycObject toCycTerm(TimeInterval obj) throws CycParseException {
    return obj.toCycTerm();
  }

  @Override
  protected TimeInterval fromCycTerm(CycObject cycObject) throws CycParseException {
    if (ContinuousTimeInterval.ALWAYS.toCycTerm().equals(cycObject)) {
      return ContinuousTimeInterval.ALWAYS;
    } else if (TimePoint.NonFixedTimePoint.NOW.toCycTerm().equals(cycObject)) {
      return TimePoint.NonFixedTimePoint.NOW;
    } else {
      try {
        final Naut naut = (Naut) NautImpl.convertIfPromising(cycObject);
        for (final ContinuousTimeInterval.TimeIntervalFunction tifn : TimeIntervalFunction.values()) {
          if (naut.getFunctor().equals(tifn.cycConstant)) {
            return tifn.constructTimeInterval(naut);
          }
        }
      } catch (ClassCastException ex) {
      }
      throw new CycParseException("Couldn't convert " + cycObject);
    }
  }
  private static final TimeIntervalConverter SHARED_INSTANCE = new TimeIntervalConverter();
}
