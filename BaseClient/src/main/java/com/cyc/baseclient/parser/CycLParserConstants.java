/* Generated By:JavaCC: Do not edit this line. CycLParserConstants.java */
package com.cyc.baseclient.parser;

/*
 * #%L
 * File: CycLParserConstants.java
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


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface CycLParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int OPEN_PAREN = 1;
  /** RegularExpression Id. */
  int CLOSE_PAREN = 2;
  /** RegularExpression Id. */
  int WS = 10;
  /** RegularExpression Id. */
  int CONSTANT_PREFIX = 11;
  /** RegularExpression Id. */
  int SIMPLE_VARIABLE_PREFIX = 12;
  /** RegularExpression Id. */
  int META_VARIABLE_PREFIX = 13;
  /** RegularExpression Id. */
  int GUID_PREFIX = 14;
  /** RegularExpression Id. */
  int TRUE_CONSTANT = 15;
  /** RegularExpression Id. */
  int FALSE_CONSTANT = 16;
  /** RegularExpression Id. */
  int AND_CONSTANT = 17;
  /** RegularExpression Id. */
  int OR_CONSTANT = 18;
  /** RegularExpression Id. */
  int XOR_CONSTANT = 19;
  /** RegularExpression Id. */
  int NOT_CONSTANT = 20;
  /** RegularExpression Id. */
  int EQUIV_CONSTANT = 21;
  /** RegularExpression Id. */
  int IMPLIES_CONSTANT = 22;
  /** RegularExpression Id. */
  int FORALL_CONSTANT = 23;
  /** RegularExpression Id. */
  int THEREEXISTS_CONSTANT = 24;
  /** RegularExpression Id. */
  int THEREEXISTEXACTLY_CONSTANT = 25;
  /** RegularExpression Id. */
  int THEREEXISTATMOST_CONSTANT = 26;
  /** RegularExpression Id. */
  int THEREEXISTATLEAST_CONSTANT = 27;
  /** RegularExpression Id. */
  int EXPANDSUBLFN_CONSTANT = 28;
  /** RegularExpression Id. */
  int SUBLQUOTEFN_CONSTANT = 29;
  /** RegularExpression Id. */
  int TRUE_GUID_CONSTANT = 30;
  /** RegularExpression Id. */
  int FALSE_GUID_CONSTANT = 31;
  /** RegularExpression Id. */
  int AND_GUID_CONSTANT = 32;
  /** RegularExpression Id. */
  int OR_GUID_CONSTANT = 33;
  /** RegularExpression Id. */
  int XOR_GUID_CONSTANT = 34;
  /** RegularExpression Id. */
  int NOT_GUID_CONSTANT = 35;
  /** RegularExpression Id. */
  int EQUIV_GUID_CONSTANT = 36;
  /** RegularExpression Id. */
  int IMPLIES_GUID_CONSTANT = 37;
  /** RegularExpression Id. */
  int FORALL_GUID_CONSTANT = 38;
  /** RegularExpression Id. */
  int THEREEXISTS_GUID_CONSTANT = 39;
  /** RegularExpression Id. */
  int THEREEXISTEXACTLY_GUID_CONSTANT = 40;
  /** RegularExpression Id. */
  int THEREEXISTATMOST_GUID_CONSTANT = 41;
  /** RegularExpression Id. */
  int THEREEXISTATLEAST_GUID_CONSTANT = 42;
  /** RegularExpression Id. */
  int EXPANDSUBLFN_GUID_CONSTANT = 43;
  /** RegularExpression Id. */
  int SUBLQUOTEFN_GUID_CONSTANT = 44;
  /** RegularExpression Id. */
  int TRUE_GUID = 45;
  /** RegularExpression Id. */
  int FALSE_GUID = 46;
  /** RegularExpression Id. */
  int AND_GUID = 47;
  /** RegularExpression Id. */
  int OR_GUID = 48;
  /** RegularExpression Id. */
  int XOR_GUID = 49;
  /** RegularExpression Id. */
  int NOT_GUID = 50;
  /** RegularExpression Id. */
  int EQUIV_GUID = 51;
  /** RegularExpression Id. */
  int IMPLIES_GUID = 52;
  /** RegularExpression Id. */
  int FORALL_GUID = 53;
  /** RegularExpression Id. */
  int THEREEXISTS_GUID = 54;
  /** RegularExpression Id. */
  int THEREEXISTEXACTLY_GUID = 55;
  /** RegularExpression Id. */
  int THEREEXISTATMOST_GUID = 56;
  /** RegularExpression Id. */
  int THEREEXISTATLEAST_GUID = 57;
  /** RegularExpression Id. */
  int EXPANDSUBLFN_GUID = 58;
  /** RegularExpression Id. */
  int SUBLQUOTEFN_GUID = 59;
  /** RegularExpression Id. */
  int INTEGER = 60;
  /** RegularExpression Id. */
  int FLOAT = 61;
  /** RegularExpression Id. */
  int DECIMAL_INT = 62;
  /** RegularExpression Id. */
  int FLOAT_ONE = 63;
  /** RegularExpression Id. */
  int FLOAT_TWO = 64;
  /** RegularExpression Id. */
  int CONSTANT_NAME1 = 65;
  /** RegularExpression Id. */
  int NON_NUMERIC_START_CHAR = 66;
  /** RegularExpression Id. */
  int NUMERIC_START_CHAR = 67;
  /** RegularExpression Id. */
  int VALID_CONSTANT_CHAR = 68;
  /** RegularExpression Id. */
  int CONSTANT_NAME2 = 69;
  /** RegularExpression Id. */
  int CONSTANT_GUID = 70;
  /** RegularExpression Id. */
  int GUID_CHAR = 71;
  /** RegularExpression Id. */
  int CONSTANT_GUID2 = 72;
  /** RegularExpression Id. */
  int SIMPLE_VARIABLE = 73;
  /** RegularExpression Id. */
  int NON_DASH_VARIABLE_CHAR = 74;
  /** RegularExpression Id. */
  int META_VARIABLE = 75;
  /** RegularExpression Id. */
  int NON_DASH_META_VARIABLE_CHAR = 76;
  /** RegularExpression Id. */
  int STRING = 77;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_LINE_COMMENT = 1;
  /** Lexical state. */
  int IN_COMMENT = 2;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"(\"",
    "\")\"",
    "\"//\"",
    "\";\"",
    "\"/*\"",
    "\"\\n\"",
    "<token of kind 7>",
    "\"*/\"",
    "<token of kind 9>",
    "<WS>",
    "\"#$\"",
    "\"?\"",
    "\":\"",
    "\"#G\"",
    "<TRUE_CONSTANT>",
    "<FALSE_CONSTANT>",
    "<AND_CONSTANT>",
    "<OR_CONSTANT>",
    "<XOR_CONSTANT>",
    "<NOT_CONSTANT>",
    "<EQUIV_CONSTANT>",
    "<IMPLIES_CONSTANT>",
    "<FORALL_CONSTANT>",
    "<THEREEXISTS_CONSTANT>",
    "<THEREEXISTEXACTLY_CONSTANT>",
    "<THEREEXISTATMOST_CONSTANT>",
    "<THEREEXISTATLEAST_CONSTANT>",
    "<EXPANDSUBLFN_CONSTANT>",
    "<SUBLQUOTEFN_CONSTANT>",
    "<TRUE_GUID_CONSTANT>",
    "<FALSE_GUID_CONSTANT>",
    "<AND_GUID_CONSTANT>",
    "<OR_GUID_CONSTANT>",
    "<XOR_GUID_CONSTANT>",
    "<NOT_GUID_CONSTANT>",
    "<EQUIV_GUID_CONSTANT>",
    "<IMPLIES_GUID_CONSTANT>",
    "<FORALL_GUID_CONSTANT>",
    "<THEREEXISTS_GUID_CONSTANT>",
    "<THEREEXISTEXACTLY_GUID_CONSTANT>",
    "<THEREEXISTATMOST_GUID_CONSTANT>",
    "<THEREEXISTATLEAST_GUID_CONSTANT>",
    "<EXPANDSUBLFN_GUID_CONSTANT>",
    "<SUBLQUOTEFN_GUID_CONSTANT>",
    "\"bd5880d9-9c29-11b1-9dad-c379636f7270\"",
    "\"bd5880d8-9c29-11b1-9dad-c379636f7270\"",
    "\"bd5880f9-9c29-11b1-9dad-c379636f7270\"",
    "\"bd5880fa-9c29-11b1-9dad-c379636f7270\"",
    "\"bde7f9f2-9c29-11b1-9dad-c379636f7270\"",
    "\"bd5880fb-9c29-11b1-9dad-c379636f7270\"",
    "\"bda887b6-9c29-11b1-9dad-c379636f7270\"",
    "\"bd5880f8-9c29-11b1-9dad-c379636f7270\"",
    "\"bd5880f7-9c29-11b1-9dad-c379636f7270\"",
    "\"bd5880f6-9c29-11b1-9dad-c379636f7270\"",
    "\"c10ae7b8-9c29-11b1-9dad-c379636f7270\"",
    "\"c10af932-9c29-11b1-9dad-c379636f7270\"",
    "\"c10af5e7-9c29-11b1-9dad-c379636f7270\"",
    "\"c0b2bc13-9c29-11b1-9dad-c379636f7270\"",
    "\"94f07021-8b0d-11d7-8701-0002b3a8515d\"",
    "<INTEGER>",
    "<FLOAT>",
    "<DECIMAL_INT>",
    "<FLOAT_ONE>",
    "<FLOAT_TWO>",
    "<CONSTANT_NAME1>",
    "<NON_NUMERIC_START_CHAR>",
    "<NUMERIC_START_CHAR>",
    "<VALID_CONSTANT_CHAR>",
    "<CONSTANT_NAME2>",
    "<CONSTANT_GUID>",
    "<GUID_CHAR>",
    "<CONSTANT_GUID2>",
    "<SIMPLE_VARIABLE>",
    "<NON_DASH_VARIABLE_CHAR>",
    "<META_VARIABLE>",
    "<NON_DASH_META_VARIABLE_CHAR>",
    "<STRING>",
    "\".\"",
  };

}
