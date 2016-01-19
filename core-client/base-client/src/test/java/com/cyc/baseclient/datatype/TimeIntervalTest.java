/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: TimeIntervalTest.java
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

import com.cyc.baseclient.datatype.ContinuousTimeInterval;
import com.cyc.baseclient.datatype.TimePoint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import static org.junit.Assert.*;
import org.junit.*;
import static com.cyc.baseclient.datatype.ContinuousTimeInterval.ALWAYS;
import static com.cyc.baseclient.datatype.TimePoint.NonFixedTimePoint.NOW;

/**
 *
 * @author baxter
 */
public class TimeIntervalTest {

  public TimeIntervalTest() {
  }
  static private final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
  static private Date JULY_11_1943 = null;
  static private Date JULY_31_1943 = null;
  static private Date AUGUST_11_1943 = null;
  static private ContinuousTimeInterval oneMonthInclusive = null;
  static private ContinuousTimeInterval oneMonthExclusive = null;
  static private ContinuousTimeInterval july_11_1943_toNow = null;
  static private Date ONE_YEAR_HENCE = null;

  @BeforeClass
  public static void setUpClass() throws Exception {
    JULY_11_1943 = df.parse("19430711");
    JULY_31_1943 = df.parse("19430731");
    AUGUST_11_1943 = df.parse("19430811");
    oneMonthInclusive = new ContinuousTimeInterval(JULY_11_1943, AUGUST_11_1943, true,
            true);
    oneMonthExclusive = new ContinuousTimeInterval(JULY_11_1943, AUGUST_11_1943, false,
            false);
    july_11_1943_toNow = new ContinuousTimeInterval(new TimePoint.FixedTimePoint(JULY_11_1943),
            NOW);
    {
      final Calendar tempCal = Calendar.getInstance();
      tempCal.add(Calendar.YEAR, 1);
      ONE_YEAR_HENCE = tempCal.getTime();
    }
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of endsAfter method, of class TimeInterval.
   */
  @Test
  public void testEndsAfter() {
    assertTrue(oneMonthInclusive.endsAfter(JULY_11_1943));
    assertTrue(july_11_1943_toNow.endsAfter(JULY_11_1943));
    assertFalse(oneMonthInclusive.endsAfter(AUGUST_11_1943));
    assertTrue(NOW.endsAfter(JULY_11_1943));
    assertTrue(ALWAYS.endsAfter(JULY_11_1943));
    final Date oldNow = NOW.getEnd();
    new java.util.Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        assertTrue(NOW.endsAfter(oldNow));
      }
    }, 1);
    assertFalse(NOW.endsAfter(ONE_YEAR_HENCE));
  }

  /**
   * Test of endsBefore method, of class TimeInterval.
   */
  @Test
  public void testEndsBefore() {
    assertFalse(oneMonthInclusive.endsBefore(JULY_11_1943));
    assertFalse(july_11_1943_toNow.endsBefore(JULY_11_1943));
    assertFalse(oneMonthInclusive.endsBefore(AUGUST_11_1943));
    assertTrue(oneMonthExclusive.endsBefore(AUGUST_11_1943));
    assertFalse(ALWAYS.endsBefore(JULY_11_1943));
    assertFalse(NOW.endsBefore(JULY_11_1943));
    final Date oldNow = NOW.getEnd();
    new java.util.Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        assertFalse(NOW.endsBefore(oldNow));
      }
    }, 1);
    assertTrue(NOW.endsBefore(ONE_YEAR_HENCE));
  }

  /**
   * Test of endsOn method, of class TimeInterval.
   */
  @Test
  public void testEndsOn() {
    assertTrue(oneMonthInclusive.endsOn(AUGUST_11_1943));
    assertTrue(july_11_1943_toNow.endsOn(new Date()));
    assertFalse(oneMonthExclusive.endsOn(AUGUST_11_1943));
  }

  /**
   * Test of startsBefore method, of class TimeInterval.
   */
  @Test
  public void testStartsBefore() {
    assertFalse(oneMonthInclusive.startsBefore(JULY_11_1943));
    assertTrue(oneMonthInclusive.startsBefore(AUGUST_11_1943));
    assertTrue(july_11_1943_toNow.startsBefore(AUGUST_11_1943));
    assertTrue(oneMonthExclusive.startsBefore(AUGUST_11_1943));
    assertFalse(NOW.startsBefore(JULY_11_1943));
    assertTrue(NOW.startsBefore(ONE_YEAR_HENCE));
  }

  /**
   * Test of startsAfter method, of class TimeInterval.
   */
  @Test
  public void testStartsAfter() {
    assertFalse(oneMonthInclusive.startsAfter(oneMonthInclusive.getStart()));
    assertFalse(oneMonthInclusive.startsAfter(AUGUST_11_1943));
    assertTrue(oneMonthExclusive.startsAfter(oneMonthExclusive.getStart()));
    assertTrue(NOW.startsAfter(JULY_11_1943));
    final Date oldNow = NOW.getEnd();
    new java.util.Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        assertTrue(NOW.startsAfter(oldNow));
      }
    }, 1);
    assertFalse(NOW.startsAfter(ONE_YEAR_HENCE));
  }

  /**
   * Test of startsOn method, of class TimeInterval.
   */
  @Test
  public void testStartsOn() {
    assertTrue(oneMonthInclusive.startsOn(oneMonthInclusive.getStart()));
    assertFalse(oneMonthExclusive.startsOn(oneMonthExclusive.getStart()));
    final Date oldNow = NOW.getStart();
    new java.util.Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        assertFalse(NOW.startsOn(oldNow));
      }
    }, 1);
    assertFalse(NOW.startsOn(ONE_YEAR_HENCE));
  }

  /**
   * Test of subsumes method, of class TimeInterval.
   */
  @Test
  public void testSubsumes_Date() {
    assertTrue(oneMonthInclusive.subsumes(JULY_31_1943));
    assertTrue(july_11_1943_toNow.subsumes(JULY_31_1943));
    assertTrue(oneMonthInclusive.subsumes(oneMonthInclusive.getStart()));
    assertTrue(oneMonthInclusive.subsumes(oneMonthInclusive.getEnd()));
    assertTrue(oneMonthExclusive.subsumes(JULY_31_1943));
    assertFalse(oneMonthExclusive.subsumes(oneMonthExclusive.getStart()));
    assertFalse(oneMonthExclusive.subsumes(oneMonthExclusive.getEnd()));
    assertFalse(oneMonthExclusive.subsumes(ONE_YEAR_HENCE));
  }

  /**
   * Test of endsAfterEndingOf method, of class TimeInterval.
   */
  @Test
  public void testEndsAfterEndingOf() {
    assertTrue(oneMonthInclusive.endsAfterEndingOf(oneMonthExclusive));
    assertTrue(july_11_1943_toNow.endsAfterEndingOf(oneMonthExclusive));
    assertTrue(NOW.endsAfterEndingOf(oneMonthInclusive));
    assertFalse(oneMonthExclusive.endsAfterEndingOf(oneMonthExclusive));
    assertFalse(july_11_1943_toNow.endsAfterEndingOf(july_11_1943_toNow));
    assertFalse(oneMonthExclusive.endsAfterEndingOf(NOW));
  }

  /**
   * Test of endsAfterStartingOf method, of class TimeInterval.
   */
  @Test
  public void testEndsAfterStartingOf() {
    assertTrue(oneMonthInclusive.endsAfterStartingOf(oneMonthExclusive));
    assertTrue(NOW.endsAfterStartingOf(oneMonthInclusive));
    assertTrue(oneMonthExclusive.endsAfterStartingOf(oneMonthExclusive));
    assertTrue(july_11_1943_toNow.endsAfterStartingOf(july_11_1943_toNow));
    assertFalse(oneMonthExclusive.endsAfterStartingOf(NOW));
  }

  /**
   * Test of endsBeforeEndingOf method, of class TimeInterval.
   */
  @Test
  public void testEndsBeforeEndingOf() {
    System.out.println("endsBeforeEndingOf");
    assertTrue(oneMonthExclusive.endsBeforeEndingOf(oneMonthInclusive));
    assertTrue(oneMonthExclusive.endsBeforeEndingOf(NOW));
    assertFalse(NOW.endsBeforeEndingOf(NOW));
    assertFalse(NOW.endsBeforeEndingOf(oneMonthInclusive));
    assertFalse(NOW.endsBeforeEndingOf(july_11_1943_toNow));
    assertFalse(july_11_1943_toNow.endsBeforeEndingOf(NOW));
  }

  /**
   * Test of endsBeforeStartingOf method, of class TimeInterval.
   */
  @Test
  public void testEndsBeforeStartingOf() {
    System.out.println("endsBeforeStartingOf");
    assertTrue(oneMonthExclusive.endsBeforeStartingOf(NOW));
  }

  /**
   * Test of coterminatesWith method, of class TimeInterval.
   */
  @Test
  public void testCoterminatesWith() {
    System.out.println("coterminatesWith");
    assertFalse(oneMonthExclusive.coterminatesWith(oneMonthInclusive));
    assertTrue(oneMonthExclusive.coterminatesWith(oneMonthExclusive));
    assertTrue(july_11_1943_toNow.coterminatesWith(NOW));
    assertTrue(oneMonthInclusive.coterminatesWith(oneMonthInclusive));
    final ContinuousTimeInterval oldNow = new ContinuousTimeInterval(NOW.getStart(), NOW.getEnd(),
            NOW.getIncludesStart(), NOW.getIncludesEnd());
    new java.util.Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        assertFalse(NOW.coterminatesWith(oldNow));
      }
    }, 1);
  }

  /**
   * Test of endsDuring method, of class TimeInterval.
   */
  @Test
  public void testEndsDuring() {
    System.out.println("endsDuring");
    assertTrue(NOW.endsDuring(new ContinuousTimeInterval(JULY_11_1943, ONE_YEAR_HENCE)));
    assertTrue(oneMonthInclusive.endsDuring(oneMonthInclusive));
    assertTrue(oneMonthExclusive.endsDuring(oneMonthExclusive));
    assertTrue(NOW.endsDuring(july_11_1943_toNow));
  }

  /**
   * Test of startsBeforeStartingOf method, of class TimeInterval.
   */
  @Test
  public void testStartsBeforeStartingOf() {
    System.out.println("startsBeforeStartingOf");
    assertTrue(oneMonthInclusive.startsBeforeStartingOf(oneMonthExclusive));
    assertFalse(NOW.startsBeforeStartingOf(oneMonthInclusive));
    assertFalse(oneMonthExclusive.startsBeforeStartingOf(oneMonthExclusive));
    assertTrue(oneMonthExclusive.startsBeforeStartingOf(NOW));
  }

  /**
   * Test of startsBeforeEndingOf method, of class TimeInterval.
   */
  @Test
  public void testStartsBeforeEndingOf() {
    System.out.println("startsBeforeEndingOf");
    assertTrue(oneMonthInclusive.startsBeforeEndingOf(oneMonthExclusive));
    assertFalse(NOW.startsBeforeEndingOf(oneMonthInclusive));
    assertTrue(oneMonthExclusive.startsBeforeEndingOf(oneMonthExclusive));
    assertTrue(oneMonthExclusive.startsBeforeEndingOf(NOW));
  }

  /**
   * Test of startsAfterStartingOf method, of class TimeInterval.
   */
  @Test
  public void testStartsAfterStartingOf() {
    System.out.println("startsAfterStartingOf");
    assertTrue(oneMonthInclusive.startsBeforeStartingOf(oneMonthExclusive));
    assertTrue(oneMonthExclusive.startsBeforeStartingOf(NOW));
    assertFalse(NOW.startsBeforeStartingOf(oneMonthInclusive));
  }

  /**
   * Test of startsAfterEndingOf method, of class TimeInterval.
   */
  @Test
  public void testStartsAfterEndingOf() {
    System.out.println("startsAfterEndingOf");
    assertFalse(oneMonthInclusive.startsAfterEndingOf(oneMonthExclusive));
    assertTrue(NOW.startsAfterEndingOf(oneMonthInclusive));
    assertFalse(oneMonthExclusive.startsAfterEndingOf(oneMonthExclusive));
    assertFalse(oneMonthExclusive.startsAfterEndingOf(NOW));
  }

  /**
   * Test of startsDuring method, of class TimeInterval.
   */
  @Test
  public void testStartsDuring() {
    System.out.println("startsDuring");
    assertTrue(NOW.startsDuring(new ContinuousTimeInterval(JULY_11_1943, ONE_YEAR_HENCE)));
    assertTrue(oneMonthInclusive.startsDuring(oneMonthInclusive));
    assertTrue(oneMonthExclusive.startsDuring(oneMonthExclusive));
  }

  /**
   * Test of cooriginatesWith method, of class TimeInterval.
   */
  @Test
  public void testCooriginatesWith() {
    System.out.println("cooriginatesWith");
    assertFalse(oneMonthExclusive.cooriginatesWith(oneMonthInclusive));
    assertTrue(oneMonthExclusive.cooriginatesWith(oneMonthExclusive));
    assertTrue(oneMonthInclusive.cooriginatesWith(oneMonthInclusive));
    assertTrue(oneMonthInclusive.cooriginatesWith(july_11_1943_toNow));
    final ContinuousTimeInterval oldNow = new ContinuousTimeInterval(NOW.getStart(), NOW.getEnd(),
            NOW.getIncludesStart(), NOW.getIncludesEnd());
    new java.util.Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        assertFalse(NOW.cooriginatesWith(oldNow));
      }
    }, 1);
  }

  /**
   * Test of subsumes method, of class TimeInterval.
   */
  @Test
  public void testSubsumes_TimeInterval() {
    System.out.println("subsumes");
    assertFalse(oneMonthExclusive.subsumes(NOW));
    assertTrue(july_11_1943_toNow.subsumes(NOW));
    assertTrue(new ContinuousTimeInterval(JULY_11_1943, ONE_YEAR_HENCE).subsumes(NOW));
  }

  /**
   * Test of getStart method, of class TimeInterval.
   */
  @Test
  public void testGetStart() {
    System.out.println("getStart");
    assertEquals(JULY_11_1943, oneMonthExclusive.getStart());
    assertEquals(JULY_11_1943, oneMonthInclusive.getStart());
    assertEquals(oneMonthExclusive.getStart(), oneMonthInclusive.getStart());
  }

  /**
   * Test of getIncludesStart method, of class TimeInterval.
   */
  @Test
  public void testGetIncludesStart() {
    System.out.println("getIncludesStart");
    assertTrue(oneMonthInclusive.getIncludesStart());
    assertFalse(oneMonthExclusive.getIncludesStart());
  }

  /**
   * Test of getEnd method, of class TimeInterval.
   */
  @Test
  public void testGetEnd() {
    System.out.println("getEnd");
    assertEquals(AUGUST_11_1943, oneMonthExclusive.getEnd());
    assertEquals(AUGUST_11_1943, oneMonthInclusive.getEnd());
    assertEquals(oneMonthExclusive.getEnd(), oneMonthInclusive.getEnd());
  }

  /**
   * Test of getIncludesEnd method, of class TimeInterval.
   */
  @Test
  public void testGetIncludesEnd() {
    System.out.println("getIncludesEnd");
    assertTrue(oneMonthInclusive.getIncludesEnd());
    assertFalse(oneMonthExclusive.getIncludesEnd());
  }
}
