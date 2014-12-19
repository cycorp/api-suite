package com.cyc.baseclient.xml;

/*
 * #%L
 * File: TextUtil.java
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

import com.cyc.baseclient.datatype.StringUtils;

/**
 * Provides utility methods for XML text processing.<p>
 *
 * @version $Id: TextUtil.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stephen L. Reed
 */

public class TextUtil {

    /**
     * Performs xml special character substitution.
     *
     * @param text the text be examined for xml special characters
     * @return the text with xml special character substitution
     */
    public static String doEntityReference (String text) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '<')
                result.append("&lt;");
            else if (ch == '&')
                result.append("&amp;");
            else if (ch == '>')
                result.append("&gt;");
            else
                result.append(ch);
        }
        return result.toString();
    }

    /**
     * Transforms xml special character substitution to plain text
     *
     * @param text the text be examined for xml special characters
     * @return the text with xml special character substitution
     */
    public static String undoEntityReference (String text) {
        String result = text.replaceAll("&lt;", "<");
        result = result.replaceAll("&amp;", "&");
        result = result.replaceAll("&gt;", ">");
        return result;
    }
}
