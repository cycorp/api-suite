package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycListParser.java
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
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.cycobject.CycList;
import static com.cyc.base.cycobject.CycConstant.HD;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.datatype.StackWithPointer;
import com.cyc.baseclient.util.MyStreamTokenizer;

/**
 * Provides a parser that reads a <tt>String</tt> representation and constructs
 * the corresponding <tt>CycArrayList</tt>. Has a weakness in that quoted strings
 * must not contain embedded newlines.
 *
 * @version $0.1$
 * @author Stephen L. Reed
 */
public class CycListParser {

  // Read/scan functions' lexical analysis variables.
  private int tok;
  private boolean endQuote = false;
  private boolean dot = false;
  private boolean dotWord = false;
  private boolean dotParen = false;
  private int parenLevel = 0;
  private StackWithPointer readStack = new StackWithPointer();
  private StackWithPointer quoteStack = new StackWithPointer();
  private String currentString = "";
  /**
   * the stream tokenizer for the current string
   */
  protected MyStreamTokenizer st;
  /**
   * Cyc api support.
   */
  protected CycAccess cycAccess;
  private static final String consMarkerSymbol = "**consMarkerSymbol**";
  private static final int STWORD = MyStreamTokenizer.TT_WORD;
  private static final int STNUMBER = MyStreamTokenizer.TT_NUMBER;
  /**
   * Verbosity indicator <tt>0</tt> indicates quiet on a range of
   * <tt>0</tt> ... <tt>10</tt>
   */
  public static int verbosity = 0;

  /**
   * Constructs a new <tt>CycArrayListParser</tt> object.
   *
   * @param cycAccess the cyc access object
   */
  public CycListParser(CycAccess cycAccess) {
    this.cycAccess = cycAccess;
  }

  /**
   * Parses a <tt>CycArrayList</tt> string representation.
   *
   * @return the corresponding <tt>CycArrayList</tt>
   * @param string the string to be parsed
   * @throws CycApiException when a Cyc API error occurs
   */
  public CycArrayList read(String string) throws CycApiException {
    currentString = string;
    st = makeStreamTokenizer(string);
    return read(st);
  }

  /**
   * Returns the unused portion of the string after a CycArrayList expression has
 been parsed.
   *
   * @return the unused portion of the string after a CycArrayList expression has
 been parsed
   */
  public String remainingString() {
    StringBuffer remainingStringBuffer = new StringBuffer();
    if (tok == st.TT_EOF) {
      return "";
    }
    st.resetSyntax();
    try {
      while (true) {
        tok = st.nextToken();
        if (tok == st.TT_EOF) {
          break;
        }
        remainingStringBuffer.append((char) tok);
      }
    } catch (IOException e) {
      throw new BaseClientRuntimeException(e.getMessage());
    }
    return remainingStringBuffer.toString();
  }

  /**
   * Returns a configured StreamTokenizer.
   *
   * @param string the string to be parsed
   * @return a configured StreamTokenizer
   */
  protected static MyStreamTokenizer makeStreamTokenizer(String string) {
    StringReader stringReader = new StringReader(string);
    MyStreamTokenizer st = new MyStreamTokenizer(stringReader);
    st.resetSyntax();
    st.ordinaryChar('(');
    st.ordinaryChar(')');
    st.ordinaryChar('\'');
    st.ordinaryChar('`');
    st.ordinaryChar('.');
    st.whitespaceChars(0, ' ');
    st.quoteChar('"');
    st.wordChars('0', '9');
    st.wordChars('a', 'z');
    st.wordChars('A', 'Z');
    st.wordChars(128 + 32, 255);
    st.wordChars('=', '=');
    st.wordChars('+', '+');
    st.wordChars('-', '-');
    st.wordChars('_', '_');
    st.wordChars('<', '<');
    st.wordChars('>', '>');
    st.wordChars('*', '*');
    st.wordChars('/', '/');
    st.wordChars('#', '#');
    st.wordChars(':', ':');
    st.wordChars('!', '!');
    st.wordChars('$', '$');
    st.wordChars('?', '?');
    st.wordChars('%', '%');
    st.wordChars('&', '&');
    st.wordChars('.', '.');
    st.slashSlashComments(false);
    st.slashStarComments(false);
    st.commentChar(';');
    st.wordChars('?', '?');
    st.wordChars('%', '%');
    st.wordChars('&', '&');
    st.eolIsSignificant(false);
    return st;
  }

  /**
   * Parses a <tt>CycArrayList</tt> string representation.
   *
   * @return the corresponding <tt>CycArrayList</tt>
   * @param st a <tt>StreamTokenizer</tt> whose source is the
   * <tt>CycArrayList</tt> string representation.
   * @throws CycApiException when a Cyc API error is detected
   */
  public CycArrayList read(MyStreamTokenizer st) throws CycApiException {
    endQuote = false;

    // Read and parse a lisp symbolic expression.
    try {
      while (true) {
        tok = st.nextToken();
        if (verbosity > 0) {
          System.out.println("sval: " + st.sval
              + "  st: " + st.toString()
              + "  tok: " + tok);
        }

        if (endQuote) {
          // Close a quoted expression by inserting a right paren.
          endQuote = false;
          st.pushBack();
          scanRightParen();
        } else if (tok == st.TT_EOF) {
          break;
        } else {
          switch (tok) {
            case STWORD:
              scanWord(st);
              break;
            case STNUMBER:
              throw new BaseClientRuntimeException("Unexpected number");
            //scanNumber(st, true);
            //break;
            case 34:	// "
              scanString(st);
              break;
            case 39:	// Quote.
              scanQuote();
              continue;
            case 96:	// Backquote.
              scanBackquote();
              continue;
            case 40:	// Left Paren
              ScanLeftParen();
              continue;
            case 41:	// Right Paren
              scanRightParen();
              break;
            case 44:	// ,
              scanComma(st);
              break;
            case 45:	// -
              scanMinus();
              break;
            default:
              throw new CycApiException("Invalid symbol: " + st.toString()
                  + " token: " + tok
                  + "\nstring: " + currentString);
          }
        }
        if ((readStack.sp > 0) && (parenLevel == 0)) {
          // Parsed a complete symbolic expression.
          Object object = readStack.pop();
          if (object.equals(CycObjectFactory.nil)) {
            return new CycArrayList(new ArrayList());
          } else if (useNewReduceDottedPairs) {
            final CycArrayList cycList = (CycArrayList) object;
            reduceDottedPairs(cycList);
            return cycList;
          } else {
            return (CycArrayList) reduceDottedPairsOld((CycArrayList) object);
          }
        }
      }
      if (readStack.sp > 0) {
        throw new BaseClientRuntimeException("Invalid expression, sval: "
            + st.sval
            + "  st: "
            + st.toString()
            + "  tok: "
            + tok
            + "\nreadStack: " + readStack.toString()
            + "\nstring: " + currentString);
      }
    } catch (IOException ioe) {
      throw new BaseClientRuntimeException(ioe);
    } catch (CycConnectionException cce) {
      throw new BaseClientRuntimeException(cce);
    }
    throw new BaseClientRuntimeException("End of stream");
  }

  /**
   * Expands 's to (quote s when reading.
   */
  private void scanQuote() {
    Integer i;
    if (verbosity > 5) {
      System.out.println("'");
    }

    if ((parenLevel > 0) && (parenLevel != readStack.sp)) {
      readStack.push(consMarkerSymbol);
    }

    readStack.push(consMarkerSymbol);
    quoteStack.push(new Integer(++parenLevel));
    readStack.push(CycObjectFactory.quote);
  }

  /**
   * Expands #'s to (function s when reading.
   */
  private void scanFunctionQuote() {
    Integer i;

    if (verbosity > 5) {
      System.out.println("#'");
    }

    if ((parenLevel > 0) && (parenLevel != readStack.sp)) {
      readStack.push(consMarkerSymbol);
    }

    readStack.push(consMarkerSymbol);
    quoteStack.push(new Integer(++parenLevel));
    readStack.push(CycObjectFactory.makeCycSymbol("function"));
  }

  /**
   * Scans a left parenthesis when reading.
   */
  private void ScanLeftParen() {
    if (verbosity > 5) {
      System.out.println("(");
    }
    // Begin a list.
    readStack.push(consMarkerSymbol);
    ++parenLevel;
  }

  /**
   * Scans a right parenthesis when reading.
   */
  private void scanRightParen() {
    CycConstantImpl cons;
    Object firstElement;
    Object remainingElements;

    if (verbosity > 5) {
      System.out.println(")");
    }

    if (parenLevel == 0) {
      throw new BaseClientRuntimeException("read: Extra right parenthesis");
    } else if ((readStack.sp == parenLevel)
        && (readStack.peek().equals(CycObjectFactory.cons))) // Have an empty list.
    {
      readStack.pop();
    }

    // Terminate the list.
    readStack.push(CycObjectFactory.nil);
    --parenLevel;

    checkQuotes();

    // Construct the list from cons cells.
    // 'a becomes (1)cons (2)quote (3)cons (4)a (5)nil
    // Transformed to (1) cons  (quote a)

    while (readStack.sp > 2) {
      remainingElements = readStack.pop();
      firstElement = readStack.pop();
      try {
        if ((readStack.peek()).equals(consMarkerSymbol)
            && (!firstElement.equals(consMarkerSymbol))
            && (!remainingElements.equals(consMarkerSymbol))) {
          readStack.pop();	// Discard cons marker atom.
          // Replace it with cons cell.
          readStack.push(CycArrayList.construct(firstElement, remainingElements));
        } else {
          // Not a cons, so restore readStack.
          readStack.push(firstElement);
          readStack.push(remainingElements);
          break;
        }
      } catch (Exception e) { // /@hack
        e.printStackTrace();
        System.err.flush();
      }
    }
  }

  /**
   * Scans a number while reading.
   *
   * @param string the input string from which to get the numerical value.
   */
  private void scanNumber(String string) {

    Double parsedNumber = null;
    try {
      parsedNumber = new Double(string);
    } catch (NumberFormatException e) {
      if (string.contains("d")) {
        parsedNumber = new Double(string.replaceFirst("d", "E"));

      }
    }
    Double doubleNumber;
    Integer integerNumber;
    Long longNumber;
    Object number = null;

    if (verbosity > 5) {
      System.out.println(string);
    }
    // Try representing the scanned number as both java double and long.
    doubleNumber = parsedNumber;
    integerNumber = new Integer(doubleNumber.intValue());
    longNumber = new Long(doubleNumber.longValue());

    if (integerNumber.doubleValue() == doubleNumber.doubleValue()) // Choose integer if no loss of accuracy.
    {
      number = integerNumber;
    } else if (longNumber.doubleValue() == doubleNumber.doubleValue()) {
      number = longNumber;
    } else {
      number = doubleNumber;
    }

    if ((parenLevel > 0) && (parenLevel != readStack.sp)) // Within a list.
    {
      readStack.push(consMarkerSymbol);
    }

    readStack.push(number);
    checkQuotes();
  }

  /**
   * Scans a minus while reading.
   */
  private void scanMinus() {
    if (verbosity > 5) {
      System.out.println("-");
    }
    CycSymbolImpl w = CycObjectFactory.makeCycSymbol("-");

    if ((parenLevel > 0) && (readStack.sp != parenLevel)) // Within a list.
    {
      readStack.push(consMarkerSymbol);
    }

    readStack.push(w);
    checkQuotes();
  }

  /**
   * Scans a backquote while reading.
   */
  private void scanBackquote() {
    if (verbosity > 5) {
      System.out.println("`");
    }
    CycSymbolImpl w = CycObjectFactory.makeCycSymbol("`");

    if ((parenLevel > 0) && (readStack.sp != parenLevel)) // Within a list.
    {
      readStack.push(consMarkerSymbol);
    }

    readStack.push(w);
    checkQuotes();
  }

  /**
   * Scans a comma while reading.
   */
  private void scanComma(MyStreamTokenizer st) throws IOException {
    CycSymbolImpl w;
    if (st.nextToken() == '@') {
      if (verbosity > 5) {
        System.out.println(",@");
      }
      w = CycObjectFactory.makeCycSymbol(",@");
    } else {
      if (verbosity > 5) {
        System.out.println(",");
      }
      w = CycObjectFactory.makeCycSymbol(",");
    }
    st.pushBack();

    if ((parenLevel > 0) && (readStack.sp != parenLevel)) // Within a list.
    {
      readStack.push(consMarkerSymbol);
    }

    readStack.push(w);
    checkQuotes();
  }

  /**
   * Scans a word while reading.
   *
   * @param the input <tt>StreamTokenizer</tt> from which to get the word value.
   */
  private void scanWord(MyStreamTokenizer st)
      throws IOException, CycConnectionException, CycApiException {
    if (verbosity > 5) {
      System.out.println(st.sval);
    }
    Object w = null;
    char firstChar = st.sval.charAt(0);
    if (st.sval.startsWith(HD)) {
      w = cycAccess.getLookupTool().getKnownConstantByName(st.sval);
    } else if (firstChar == '?') {
      w = CycObjectFactory.makeCycVariable(st.sval);
    } else if (st.sval.equals("#")) {
      int nextTok = st.nextToken();
      if (nextTok == 39) {
        scanFunctionQuote();
        return;
      } else {
        st.pushBack();
        w = CycObjectFactory.makeCycSymbol(st.sval);
      }
    } else if (((firstChar == '-') && (!st.sval.equals("-")))
        || Character.isDigit(firstChar)) {
      scanNumber(st.sval);
      return;
    } else {
      w = CycObjectFactory.makeCycSymbol(st.sval);
    }

    if ((parenLevel > 0) && (readStack.sp != parenLevel)) // Within a list.
    {
      readStack.push(consMarkerSymbol);
    }

    readStack.push(w);
    checkQuotes();
  }

  /**
   * Scans a string while reading.
   */
  private void scanString(MyStreamTokenizer st) {
    String string = new String(st.sval);
    String line1;
    String line2;
    int index;

    //Replace `~ combination with crlf since StreamTokenizer cannot
    //span multiple lines.

    while (true) {
      index = string.indexOf("`~");
      if (index == -1) {
        break;
      }

      line1 = new String(string.substring(0, index));
      line2 = new String(string.substring(index + 2));
      string = line1 + "\r\n" + line2;
    }

    if (verbosity > 5) {
      System.out.println(st.sval);
    }
    if ((parenLevel > 0) && (readStack.sp != parenLevel)) // Within a list.
    {
      readStack.push(consMarkerSymbol);
    }

    readStack.push(string);
    checkQuotes();
  }

  /**
   * Read/Scan helper routine to check for the end of quoted forms.
   */
  private void checkQuotes() {
    if ((!quoteStack.empty())
        && (((Integer) quoteStack.peek()).intValue() == parenLevel)) {
      quoteStack.pop();
      endQuote = true;
    }
  }

  /**
   * Performs a lexical analysis of the list, removing dot objects and
   * canonicalizing dotted representation.
   *
   * @param the <tt>Object</tt> under consideration.
   */
  private void reduceDottedPairs(Object s) {
    if (s instanceof CycArrayList) {
      CycArrayList cycList = (CycArrayList) s;
      for (int i = 0; i < cycList.size(); i++) {
        final Object item = cycList.get(i);
        if (item instanceof CycArrayList) {
          reduceDottedPairs(item);
        } else if (item.equals(CycObjectFactory.dot)) {
          final Object nextItem = cycList.get(i + 1);
          cycList.remove(i);
          cycList.remove(i);
          // We've consumed the dot and one more item:
          i++;
          if (nextItem instanceof CycArrayList) {
            // Canonicalize (... . (b)) to list (... b), etc.
            reduceDottedPairs(nextItem);
            cycList.addAll((CycArrayList) nextItem);
          } else {
            // Note the dotted element:
            cycList.setDottedElement(nextItem);
          }
        }
      }
    }
  }

  private Object reduceDottedPairsOld(Object s) {
    if (!(s instanceof CycArrayList)) {
      return s;
    }
    CycArrayList cycList = (CycArrayList) s;
    if (cycList.size() == 0) {
      return s;
    } else if (cycList.size() == 3
        && cycList.second().equals(CycObjectFactory.dot)) {
      Object first = reduceDottedPairsOld(cycList.first());
      Object third = reduceDottedPairsOld(cycList.third());
      if (cycList.third() instanceof CycArrayList) {
        // Replace list (a . (b)) with list (a b)
        CycArrayList reducedCycList = new CycArrayList(first);
        reducedCycList.addAll((CycArrayList) third);
        if (!((CycArrayList) third).isProperList()) {
          reducedCycList.setDottedElement(((CycArrayList) third).getDottedElement());
        }
        return reducedCycList;
      } else {
        // Mark list (a . b) as improper and remove the dot symbol.
        CycArrayList improperList = new CycArrayList(first);
        improperList.setDottedElement(third);
        return improperList;
      }
    }
    Object firstReducedDottedPair = reduceDottedPairsOld(cycList.first());
    Object restReducedDottedPair = reduceDottedPairsOld(cycList.rest());
    CycList constructedCycList = CycArrayList.construct(firstReducedDottedPair,
        restReducedDottedPair);
    return constructedCycList;
  }
  static private boolean useNewReduceDottedPairs = true;
}
