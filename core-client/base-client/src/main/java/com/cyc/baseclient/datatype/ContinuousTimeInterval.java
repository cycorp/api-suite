package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: ContinuousTimeInterval.java
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

import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.annotation.CycTerm;
import java.util.Date;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.Naut;
import com.cyc.baseclient.cycobject.GuidImpl;
import com.cyc.baseclient.datatype.TimePoint.FixedTimePoint;
import com.cyc.baseclient.exception.CycParseException;

/**
 * A Java representation of continuous time intervals. 
 * <p>
 * Continuous time intervals are defined in terms
 * of a start and an end, and either include or do not include each
 * endpoint.
 *
 * @author baxter
 */
@CycObjectLibrary
public class ContinuousTimeInterval extends AbstractTimeInterval {

  /**
   * Create a new time interval that includes its start and its end.
   *
   * @param start
   * @param end
   */
  public ContinuousTimeInterval(Date start, Date end) {
    this(start, end, true, true);
  }

  /**
   * Create a new time interval that includes its start and its end.
   *
   * @param start
   * @param end
   */
  public ContinuousTimeInterval(TimePoint start, TimePoint end) {
    this(start, end, true, true);
  }

  /**
   * Create a new time interval.
   *
   * @param start
   * @param end
   * @param includesStart
   * @param includesEnd
   */
  public ContinuousTimeInterval(Date start, Date end, boolean includesStart,
          boolean includesEnd) {
    this(new FixedTimePoint(start), new FixedTimePoint(end), includesStart, includesEnd);
  }

  /**
   * Create a new time interval.
   *
   * @param startPoint
   * @param endPoint
   * @param includesStart
   * @param includesEnd
   */
  public ContinuousTimeInterval(TimePoint startPoint, TimePoint endPoint, boolean includesStart,
          boolean includesEnd) {
    if (startPoint == null) {
      throw new IllegalArgumentException("Start must not be null.");
    }
    if (endPoint == null) {
      throw new IllegalArgumentException("End must not be null.");
    }
    if (FixedTimePoint.BEGINNING_OF_TIME == startPoint && !includesStart) {
      throw new IllegalArgumentException("Time interval starting at beginning of time must include its start.");
    }
    if (FixedTimePoint.END_OF_TIME == endPoint && !includesEnd) {
      throw new IllegalArgumentException("Time interval ending at end of time must include its end.");
    }
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.includesStart = includesStart;
    this.includesEnd = includesEnd;
    if (endBeforeStart()) {
      throw new IllegalArgumentException("End must not be before start.");
    }
  }
  
  private boolean endBeforeStart() {
    try {
      return getEnd().before(getStart());
    } catch (Exception e) {
      throw new BaseClientRuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "TimeInterval{" + "start=" + startPoint + ", end=" + endPoint
            + ", includesStart=" + includesStart + ", includesEnd=" + includesEnd + '}';
  }

  /**
   * Returns the starting date of the interval.
   *
   * @return the starting date of the interval.
   */
  @Override
  public Date getStart() {
    return startPoint.asDate();
  }

  /**
   * Does this time interval include its start? If so, it begins when its start
   * begins. If not, it begins immediately startsAfterEndingOf.
   *
   * @return true iff this time interval includes its start.
   */
  @Override
  public boolean getIncludesStart() {
    return includesStart;
  }

  /**
   * Returns the ending date of the interval.
   *
   * @return the ending date of the interval.
   */
  @Override
  public Date getEnd() {
    return endPoint.asDate();
  }

  /**
   * Does this time interval include its end? If so, it ends when its end ends.
   * If not, it ends immediately endsBeforeStartingOf.
   *
   * @return true iff this time interval includes its end.St
   */
  @Override
  public boolean getIncludesEnd() {
    return includesEnd;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.startPoint != null ? this.startPoint.hashCode() : 0);
    hash = 97 * hash + (this.endPoint != null ? this.endPoint.hashCode() : 0);
    hash = 97 * hash + (this.includesStart ? 1 : 0);
    hash = 97 * hash + (this.includesEnd ? 1 : 0);
    return hash;
  }

  /**
   * Return the CycObject corresponding to this time interval.
   *
   * @return the corresponding CycObject
   * @see com.cyc.base.cycobject.CycObject
   * @see com.cyc.baseclient.cycobject.DefaultCycObject
   */
  public CycObject toCycTerm() {
    //@TODO -- Handle Terminus start/ends.
    try {
      return new NautImpl(getCycFunctor(), getCycTermForStart(),
              getCycTermForEnd());
    } catch (CycParseException ex) {
      throw new BaseClientRuntimeException(ex);
    }
  }

  private CycConstantImpl getCycFunctor() {
    return TimeIntervalFunction.getCycFunctor(includesStart, includesEnd);
  }

  private CycObject getCycTermForStart() throws CycParseException {
    return startPoint.toCycTerm();
  }

  private CycObject getCycTermForEnd() throws CycParseException {
    return endPoint.toCycTerm();
  }
  /**
   * The time interval that includes every time point.
   */
  public static final ContinuousTimeInterval ALWAYS = new ContinuousTimeInterval(FixedTimePoint.BEGINNING_OF_TIME,
          FixedTimePoint.END_OF_TIME, true, true) {

    @Override
    public CycObject toCycTerm() {
      return ALWAYS_TIME_INTERVAL;
    }
  };
  private final TimePoint startPoint;
  private final TimePoint endPoint;
  private final boolean includesStart;
  private final boolean includesEnd;
  
  @CycTerm(cycl="#$StartFn")
  public static final CycConstantImpl START_FN = new CycConstantImpl("StartFn", 
          new GuidImpl("be010fc3-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$EndFn")
  public static final CycConstantImpl END_FN = new CycConstantImpl("EndFn", 
          new GuidImpl("be01123d-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Always-TimeInterval")
  public static final CycConstantImpl ALWAYS_TIME_INTERVAL = new CycConstantImpl("Always-TimeInterval",
          new GuidImpl("c0ea3419-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Now")
  public static final CycConstantImpl NOW = new CycConstantImpl("Now", new GuidImpl(
          "bd58a068-9c29-11b1-9dad-c379636f7270"));

  // TODO: annotate as @CycFormula - nwinant, 2014-08-15
  //@CycFormula(cycl="(#$StartFn #$Always-TimeInterval)")
  public static final NautImpl BEGINNING_OF_TIME = new NautImpl(
          START_FN, ALWAYS_TIME_INTERVAL);
  
  // TODO: annotate as @CycFormula - nwinant, 2014-08-15
  //@CycFormula(cycl="(#$EndFn #$Always-TimeInterval)")
  public static final Naut END_OF_TIME = new NautImpl(
          END_FN, ALWAYS_TIME_INTERVAL);
  
  
  @CycObjectLibrary
  public enum TimeIntervalFunction {

    // FIXME: these should be referenced, somehow, from CommonConstants - nwinant, 2014-08-12
    
    INCL_INCL("TimeIntervalInclusiveFn", "c08aa1d2-9c29-11b1-9dad-c379636f7270",
    true, true),
    EXCL_EXCL("TimeIntervalBetweenFn", "bd58ce20-9c29-11b1-9dad-c379636f7270",
    false, false),
    INCL_EXCL("TimeInterval-Incl-Excl-Fn",
    "4cb5b2d6-2733-41d7-802c-cf187fc10915",
    true, false),
    EXCL_INCL("TimeInterval-Excl-Incl-Fn",
    "4c68f680-2733-41d7-886f-91075f140655",
    false, true);

    private static CycConstantImpl getCycFunctor(boolean includesStart,
            boolean includesEnd) {
      for (final TimeIntervalFunction tifn : values()) {
        if (tifn.includesStart == includesStart && tifn.includesEnd == includesEnd) {
          return tifn.cycConstant;
        }
      }
      throw new AssertionError(
              "start = " + includesStart + " end = " + includesEnd);
    }
    public final boolean includesStart;
    public final boolean includesEnd;
    public final CycConstantImpl cycConstant;
    
    private TimeIntervalFunction(final String name, final String guid,
            final boolean includesStart, final boolean includesEnd) {
      this.includesStart = includesStart;
      this.includesEnd = includesEnd;
      this.cycConstant = new CycConstantImpl(name, new GuidImpl(guid));
    }
    
    public ContinuousTimeInterval constructTimeInterval(final Date start, final Date end) {
      return new ContinuousTimeInterval(start, end, includesStart, includesEnd);
    }
    
    public ContinuousTimeInterval constructTimeInterval(Naut naut) {
      if (!cycConstant.equals(naut.getFunctor())) {
        throw new IllegalArgumentException();
      }
      Naut startNaut = (Naut) naut.getArg(1);
      final boolean inFactExcludesStart = includesStart && oneSecondAfter(startNaut);
      if (inFactExcludesStart) {
        startNaut = (NautImpl) startNaut.getArg1();
      }
      final Date startDate = parseDate(startNaut);
      Naut endNaut = (Naut) naut.getArg(2);
      final boolean inFactExcludesEnd = includesEnd && oneSecondBefore(endNaut);
      if (inFactExcludesEnd) {
        endNaut = (Naut) endNaut.getArg1();
      }
      final Date endDate = parseDate(endNaut);
      return new ContinuousTimeInterval(startDate, endDate,
              (includesStart && !inFactExcludesStart),
              (includesEnd && !inFactExcludesEnd));
    }
    
    private Date parseDate(final Naut naut) {
      final Date date = DateConverter.parseCycDate(naut);
      if (date == null) {
        throw new UnsupportedOperationException("Can't parse NAUT as date " + naut);
      }
      return date;
    }
    
    @CycTerm(cycl="#$IntervalBeforeFn")
    public static final CycConstantImpl INTERVAL_BEFORE_FN
            = new CycConstantImpl("IntervalBeforeFn", new GuidImpl("bd58fa99-9c29-11b1-9dad-c379636f7270"));
    
    @CycTerm(cycl="#$IntervalAfterFn")
    public static final CycConstantImpl INTERVAL_AFTER_FN
            = new CycConstantImpl("IntervalAfterFn", new GuidImpl("bd58a0a0-9c29-11b1-9dad-c379636f7270"));
    
    @CycTerm(cycl="#$SecondsDuration")
    public static final CycConstantImpl SECONDS_DURATION
            = new CycConstantImpl("SecondsDuration", new GuidImpl("bd58ebb1-9c29-11b1-9dad-c379636f7270"));
    
    private static final Naut ONE_SECOND
            = new NautImpl(SECONDS_DURATION, 1);
    
    private boolean oneSecondBefore(final Naut naut) {
      return INTERVAL_BEFORE_FN.equals(naut.getFunctor())
              && ONE_SECOND.equals(naut.getArg2());
    }
    
    private boolean oneSecondAfter(final Naut naut) {
      return INTERVAL_AFTER_FN.equals(naut.getFunctor())
              && ONE_SECOND.equals(naut.getArg2());
    }
  }
}
