package com.cyc.base.cycobject;

/*
 * #%L
 * File: InformationSource.java
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

//// Internal Imports

import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import java.net.URL;
//// Internal Imports
//// External Imports
/**
 * <P>InformationSource is designed to...
 * 
 * @author jmoszko, May 13, 2014, 5:43:38 PM
 * @version $Id: InformationSource.java 162904 2015-12-02 18:35:34Z nwinant $
 */
public interface InformationSource {

  /**
   * Returns a string suitable for citing this source.
   *
   * @return a string suitable for citing this source.
   */
  String getCitationString();

  /**
   *
   * @return the CycL term representing this source.
   */
  DenotationalTerm getCycL();

  /**
   * Returns a graphical representation of this source.
   *
   * @return a graphical representation of this source.
   */
  /*
  public Image getIcon() {
  return icon;
  }
   */
  URL getIconURL();
  
  public interface CitationGenerator {
    /**
     * Ask Cyc for an image for the citation string to use for the specified source.
     *
     * @param source
     * @param cycAccess
     * @return the citation string
     */
    public String getCitationStringForSource(DenotationalTerm source,
            CycAccess cycAccess) throws CycConnectionException;
    }
  
}
