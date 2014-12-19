package com.cyc.base.conn;

/*
 * #%L
 * File: LeaseManager.java
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

import java.security.InvalidParameterException;
import java.util.EventListener;
import java.util.EventObject;

/**
 *
 * @author nwinant
 */
public interface LeaseManager {
  /** the Cyc lease manager reason when the api service request resulted in a communication error condition */
  LeaseManagerReason CYC_COMMUNICATION_ERROR = new LeaseManagerReason("CYC_COMMUNICATION_ERROR");
  /** the Cyc lease manager reason when Cyc denies the api service lease request */
  LeaseManagerReason CYC_DENIES_THE_LEASE_REQUEST = new LeaseManagerReason("CYC_DENIES_THE_LEASE_REQUEST");
  /** the Cyc lease manager reason when Cyc does not respond to the api service lease request */
  LeaseManagerReason CYC_DOES_NOT_RESPOND_TO_LEASE_REQUEST = new LeaseManagerReason("CYC_DOES_NOT_RESPOND_TO_LEASE_REQUEST");
  /** the Cyc lease manager reason when Cyc has been restarted with a new image id (invalidating any cached constant ids, and
   * possibly with inconsistent knowledge state) */
  LeaseManagerReason CYC_IMAGE_ID_HAS_CHANGED = new LeaseManagerReason("CYC_IMAGE_ID_HAS_CHANGED");
  /** the Cyc lease manager reason when a lease has been successfully renewed */
  LeaseManagerReason LEASE_SUCCESSFULLY_RENEWED = new LeaseManagerReason("LEASE_SUCCESSFULLY_RENEWED");

  //// Public Area
  /** Adds a Cyc lease manager listener.
   *
   * @param cycLeaseManagerListener a listener for Cyc lease manager events
   */
  void addListener(final LeaseManagerListener cycLeaseManagerListener);

  String getCycImageId();

  /** Gets the lease duration milliseconds.
   *
   * @return the lease duration milliseconds
   */
  long getLeaseDurationMilliseconds();

  /**
   * Returns whether or not we have a valid lease with the Cyc server.
   *
   * @return whether or not we have a valid lease with the Cyc server
   */
  boolean hasValidLease();

  /** Immediately renews the current lease by interrupting the sleep of this leasing thread. */
  void immediatelyRenewLease();

  /** Returns the indicator whether a lease request is currently pending.
   *
   * @return the indicator whether a lease request is currently pending
   */
  boolean isLeaseRequestPending();

  /**
   * Removes all Cyc lease manager listeners.
   */
  void removeAllListeners();

  /** Removes a Cyc lease manager listener.
   *
   * @param cycLeaseManagerListener a listener for Cyc lease manager events
   */
  void removeListener(final LeaseManagerListener cycLeaseManagerListener);

  /** Performs periodic Cyc api service lease acquisition, and notifies listeners if the lease fails or is denied. */
  void run();

  /** Sets the lease duration milliseconds.
   *
   * @param leaseDurationMilliseconds the lease duration milliseconds
   */
  void setLeaseDurationMilliseconds(final long leaseDurationMilliseconds);
  
  public void interrupt();
  
  
  
  
  
  /** Defines the interface for Cyc API services event listeners. */
  public interface LeaseManagerListener extends EventListener {

    /** Notifies the listener of the given Cyc API services lease event.
     *
     * @param evt the the given Cyc API services lease event
     */
    void notifyCycLeaseEvent(LeaseEventObject evt);
  };
  
  
  
  
  
  
  /** 
   * Class that provides a Cyc lease event.
   */
  public interface LeaseEventObject {
    public LeaseManagerReason getReason();
  }
  
  
  /** Class to contain the Cyc lease manager event reason */
  public static class LeaseManagerReason {

    /** Creates a new CycLeaseManagerReason instance.
     *
     * @param reason the Cyc lease manager event reason
     */
    public LeaseManagerReason(final String reason) {
      //// Preconditions
      if (reason == null || reason.length() == 0) {
        throw new InvalidParameterException("reason must be a non-empty string");
      }

      this.reason = reason;
    }

    /** Gets the Cyc lease manager event reason.
     *
     * @return the Cyc lease manager event reason
     */
    public String getReason() {
      return reason;
    }
    /** the Cyc lease manager event reason */
    private final String reason;

    public boolean isGood() {
      return this == LEASE_SUCCESSFULLY_RENEWED;
    }
  };
}
