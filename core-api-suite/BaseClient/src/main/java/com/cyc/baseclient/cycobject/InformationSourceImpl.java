package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: InformationSourceImpl.java
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

//import java.awt.Image;
import com.cyc.base.cycobject.InformationSource;
import com.cyc.base.CycAccess;
import com.cyc.base.CycConnectionException;
import java.net.URL;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.api.SubLAPIHelper;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.CycObject;
import com.cyc.baseclient.CommonConstants;

/**
 * A class for representing sources of information denoted by Cyc FORTs.
 *
 * @author baxter
 */
public class InformationSourceImpl implements InformationSource {

  public InformationSourceImpl(final DenotationalTerm term, final URL iconURL,
          final String citationString) {
    this.term = term;
    this.iconURL = iconURL;
    this.citationString = citationString;
  }

  public InformationSourceImpl(DenotationalTerm sourceTerm,
          CycAccess cycAccess) throws CycConnectionException {
    this(sourceTerm, CycCitationGenerator.DEFAULT, cycAccess);
  }

  public InformationSourceImpl(DenotationalTerm sourceTerm,
          CitationGenerator citationGenerator,
          CycAccess cycAccess) throws CycConnectionException {
    this.term = sourceTerm;
    this.iconURL = getIconURLForSource(sourceTerm, cycAccess);
    try {
      this.citationString = citationGenerator.getCitationStringForSource(
              sourceTerm, cycAccess);
    } catch (RuntimeException ex) {
      this.citationString = null;
    }
  }

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
  
  @Override
  public URL getIconURL() {
    return iconURL;
  }

  /**
   *
   * @return the CycL term representing this source.
   */
  @Override
  public DenotationalTerm getCycL() {
    return term;
  }

  /**
   * Returns a string suitable for citing this source.
   *
   * @return a string suitable for citing this source.
   */
  @Override
  public String getCitationString() {
    return citationString;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final InformationSourceImpl other = (InformationSourceImpl) obj;
    if (this.term != other.term && (this.term == null || !this.term.equals(
            other.term))) {
      return false;
    }
    /*
    if (this.icon != other.icon && (this.icon == null || !this.icon.equals(
            other.icon))) {
      return false;
    }
    */
    if (this.iconURL != other.iconURL && (this.iconURL == null || !this.iconURL.equals(
            other.iconURL))) {
      return false;
    }
    if ((this.citationString == null) ? (other.citationString != null) : !this.citationString.equals(
            other.citationString)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 29 * hash + (this.term != null ? this.term.hashCode() : 0);
    //hash = 29 * hash + (this.icon != null ? this.icon.hashCode() : 0);
    hash = 29 * hash + (this.iconURL != null ? this.iconURL.hashCode() : 0);
    hash = 29 * hash + (this.citationString != null ? this.citationString.hashCode() : 0);
    return hash;
  }

  private URL getIconURLForSource(DenotationalTerm source,
          CycAccess cycAccess) {
    try {
      final String pathCommand = SubLAPIHelper.makeSubLStmt(
              "icon-path-for-source", source);
      final Object iconURLObj = cycAccess.converse().converseObject(pathCommand);
      if (iconURLObj instanceof String) {
        return new URL((String) iconURLObj);
      }
    } catch (Exception e) {
      System.err.println(
              "Couldn't load source icon for " + source + " from " + iconURL);
      e.printStackTrace(System.err);
    }
    return null;
  }
  
  /**
   * Ask Cyc for an image for the icon to use for the specified source.
   *
   * @param source
   * @param cycAccess
   * @return
   */
  /*
  private static Image getIconForSource(DenotationalTerm source,
          CycAccess cycAccess) {
    Image icon = null;
    String iconURL = null;
    try {
      final String pathCommand = SubLAPIHelper.makeSubLStmt(
              "icon-path-for-source", source);
      final Object iconURLObj = cycAccess.converseObject(pathCommand);
      if (iconURLObj instanceof String) {
        iconURL = (String) iconURLObj;
      }
      icon = ImageIO.read(new URL(iconURL));
    } catch (Exception e) {
      System.err.println(
              "Couldn't load source icon for " + source + " from " + iconURL);
      e.printStackTrace(System.err);
    }
    return icon;
  }
  */

  /**
   * A helper class to generate citation strings for sources.
   */
  public static class CycCitationGenerator implements CitationGenerator {

    CycObject style = ANY;
    CycObject markupLanguage = CommonConstants.HYPERTEXT_MARKUP_LANGUAGE;
    /**
     * A singleton citation generator with default settings.
     */
    public static final CitationGenerator DEFAULT = new CycCitationGenerator();

    /**
     * Make a new citation generator with the specified style and markup language.
     *
     * @param style an instance of #$PublicationStyleSpecification.
     * @param markupLanguage an instance of #$MarkupLanguage.
     */
    public CycCitationGenerator(DenotationalTerm style,
            DenotationalTerm markupLanguage) {
      this.style = style;
      this.markupLanguage = markupLanguage;
    }

    /**
     * Make a new citation generator with the specified style and default markup
     * language (HTML).
     *
     * @param style an instance of #$PublicationStyleSpecification.
     */
    public CycCitationGenerator(DenotationalTerm style) {
      this.style = style;
    }

    /**
     * Make a new citation generator with any style and default markup language
     * (HTML).
     *
     */
    public CycCitationGenerator() {
    }

    /**
     * Ask Cyc for an image for the citation string to use for the specified source.
     *
     * @param source
     * @param cycAccess
     * @return the string used to cite the source, an instance of #$ConceptualWork
     */
    @Override
    public String getCitationStringForSource(DenotationalTerm source,
            CycAccess cycAccess) throws CycConnectionException {
      final String command = SubLAPIHelper.makeSubLStmt(
              "source-citation-string", source, style, markupLanguage);
      return cycAccess.converse().converseString(command);
    }
  }
  private final DenotationalTerm term;
//  private Image icon = null;
  private URL iconURL = null;
  private String citationString = null;
  private static final CycSymbolImpl ANY = CycObjectFactory.makeCycSymbol(":ANY");
}
