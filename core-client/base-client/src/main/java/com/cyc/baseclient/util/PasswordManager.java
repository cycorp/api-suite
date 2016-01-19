package com.cyc.baseclient.util;

/*
 * #%L
 * File: PasswordManager.java
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
import com.cyc.baseclient.exception.CycTaskInterruptedException;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycTimeOutException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//// Internal Imports
import com.cyc.base.exception.CycApiException;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import static com.cyc.baseclient.cycobject.DefaultCycObject.stringApiValue;
import com.cyc.baseclient.cycobject.GuidImpl;

/** 
 * <P>PasswordManager is designed to handle password encryption, authentication ,etc.
 *
 * @author baxter, Jul 27, 2009, 3:10:59 PM
 * @version $Id: PasswordManager.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public class PasswordManager {

  //// Constructors
  /** Creates a new instance of PasswordManager. */
  public PasswordManager(final CycAccess cycAccess) {
    this.cycAccess = cycAccess;
  }

  //// Public Area
  /** Verify that a user/password combination for an application is valid.
   *
   * @param user
   * @param applicationTerm
   * @param unencryptedPassword
   * @return true iff Cyc verifies the authenticity of the login credentials.
   */
  public boolean areLoginCredentialsValid(final CycConstantImpl user, final DenotationalTerm applicationTerm,
          final char[] unencryptedPassword) {
    boolean isValid = false;
    final String encryptedPassword = encryptPassword(unencryptedPassword, user);
    // @note Authentication sets the cyclist to Guest on failure:
    final String command = "(PROGN" +
            "(AUTHENTICATE-THE-CYCLIST " + stringApiValue(user.getName()) +
            " " + stringApiValue(encryptedPassword) + " " + applicationTerm.stringApiValue() + ")" +
            "(CNOT (THE-CYCLIST-IS-GUEST?)))";
    try {
      isValid = CommUtils.convertResponseToBoolean(CommUtils.performApiCommand(command, cycAccess));
      if (isValid) {
        noteValidPassword(user, applicationTerm, encryptedPassword);
      }
    } catch (CycConnectionException ex) {
      Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
    } catch (CycTimeOutException ex) {
      Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
    } catch (CycApiException ex) {
      Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
    } catch (CycTaskInterruptedException ex) {
      Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
    }
    return isValid;
  }

  /** Does the Cyc Image require authentication before allowing a user access? */
  public Boolean isPasswordRequired() throws CycConnectionException {
    return CommUtils.convertResponseToBoolean(CommUtils.performApiCommand("(image-requires-authentication?)", cycAccess));
  }

  /** Update the password for the specified user and application.
   *
   * @param user the user whose password is to be updated
   * @param applicationTerm the application for which the user's password is to be updated.
   * @param unencryptedPassword the new password
   * @return true iff Cyc signals the password has been successfully updated.
   */
  public boolean updatePassword(final CycConstantImpl user, final DenotationalTerm applicationTerm,
          final char[] unencryptedPassword) {
    final String encryptedPassword = encryptPassword(unencryptedPassword, user);
    final String command = "(SPECIFY-AUTHENTICATION-INFO-FOR-USER " + user.stringApiValue() +
            " " + DefaultCycObject.stringApiValue(encryptedPassword) +
            " " + applicationTerm.stringApiValue() + ")";
    try {
      final boolean success = CommUtils.convertResponseToBoolean(CommUtils.performApiCommand(command, cycAccess));
      if (success) {
        noteValidPassword(user, applicationTerm, encryptedPassword);
      }
      return success;
    } catch (CycConnectionException ex) {
      Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
    } catch (CycTimeOutException ex) {
      Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
    } catch (CycApiException ex) {
      Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
    } catch (CycTaskInterruptedException ex) {
      Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  /** Encrypt a user/password combination using Cyc's standard password encryption.
   * 
   * @param unencryptedPassword
   * @param user
   * @return the encrypted password
   */
  public static String encryptPassword(final char[] unencryptedPassword, final CycConstantImpl user) {
    try {
      final MessageDigest encryptor = MessageDigest.getInstance("SHA-1");
      final String charSet = "iso-8859-1";
      encryptor.update(user.getName().getBytes(charSet));
      encryptor.update(passwordCharsToBytes(unencryptedPassword, charSet));
      return base64.encodeBytes(encryptor.digest());
    } catch (NoSuchAlgorithmException ex) {
      throw new BaseClientRuntimeException("Failed to encrypt password", ex);
    } catch (UnsupportedEncodingException ex) {
      throw new BaseClientRuntimeException("Failed to encrypt password", ex);
    }
  }

  /** Look up the (encrypted) password for user using application.
   *
   * This will only find passwords that have been entered into this application.
   *
   * @param user
   * @param applicationTerm
   * @return the *encrypted* password, or null if it is not known.
   */
  public String lookupPassword(final CycConstantImpl user, final DenotationalTerm applicationTerm) {
    return CACHE.get(new CacheKey(user, applicationTerm, cycAccess));
  }
  //// Protected Area

  //// Private Area
  /** Convert password in char[] form to byte[] form.
   *
   * @note Ideally, we'd like to do this conversion without constructing an intermediate String representation.
   * @param unencryptedPassword
   * @param charSet
   * @return byte array representation of the password
   * @throws java.io.UnsupportedEncodingException
   */
  private static byte[] passwordCharsToBytes(final char[] unencryptedPassword,
          final String charSet) throws UnsupportedEncodingException {
    final String unencryptedPasswordString = new String(unencryptedPassword);
    final byte[] passwordBytes = unencryptedPasswordString.getBytes(charSet);
    return passwordBytes;
  }

  private void noteValidPassword(final CycConstantImpl user, final DenotationalTerm applicationTerm,
          final String encryptedPassword) {
    CACHE.put(new CacheKey(user, applicationTerm, cycAccess), encryptedPassword);
  }
  //// Internal Rep
  private final CycAccess cycAccess;
  private final static Base64 base64 = new Base64();
  private final static Map<CacheKey, String> CACHE = new HashMap<CacheKey, String>();

  private class CacheKey {

    final private DenotationalTerm user;
    final private DenotationalTerm applicationTerm;
    final private CycAccess cyc;

    private CacheKey(final CycConstantImpl user, final DenotationalTerm applicationTerm,
            final CycAccess cyc) {
      this.user = user;
      this.applicationTerm = applicationTerm;
      this.cyc = cyc;
    }

    @Override
    public int hashCode() {
      return user.hashCode() + applicationTerm.hashCode() + cyc.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final CacheKey other = (CacheKey) obj;
      if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
        return false;
      }
      if (this.applicationTerm != other.applicationTerm && (this.applicationTerm == null ||
              !this.applicationTerm.equals(other.applicationTerm))) {
        return false;
      }
      if (this.cyc != other.cyc && (this.cyc == null || !this.cyc.equals(other.cyc))) {
        return false;
      }
      return true;
    }
  }

  //// Main
  /*
  //static private final CycConstantImpl testUser = new CycConstantImpl("Baxter", new GuidImpl("beeac37c-9c29-11b1-9dad-c379636f7270"));
  static private int successCount = 0;
  static private int testCount = 0;
  
  public static void main(final String[] args) {
    testCount = successCount = 0;
    checkOne("D", "c4W9SqCQVJfGGLjEehulnXTOCpM=");
    checkOne("dog", "ERaE0f2BsQ8hg2udao8mJakTiPY=");
    System.out.println(successCount + " of " + testCount + " tests passed.");
  }
  
  private static void checkOne(final String unencryptedPassword, final String encryptedPassword) {
    final String actual = encryptPassword(unencryptedPassword.toCharArray(), testUser);
    final String expected = encryptedPassword;
    if (actual.equals(expected)) {
      successCount++;
    } else {
      System.out.println("Wanted " + expected + " Got " + actual);
    }
    testCount++;
  }
  */
}
