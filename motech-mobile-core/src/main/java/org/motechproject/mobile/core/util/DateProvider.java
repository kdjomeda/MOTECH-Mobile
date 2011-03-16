/**
 * MOTECH PLATFORM OPENSOURCE LICENSE AGREEMENT
 *
 * Copyright (c) 2010-11 The Trustees of Columbia University in the City of
 * New York and Grameen Foundation USA.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Grameen Foundation USA, Columbia University, or
 * their respective contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY GRAMEEN FOUNDATION USA, COLUMBIA UNIVERSITY
 * AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL GRAMEEN FOUNDATION
 * USA, COLUMBIA UNIVERSITY OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.motechproject.mobile.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Provides methods to manipulate Date instances.It's a helper class that provides various
 * method to get date in different formating patterns and convert date object from and to string
 * 
 *  Date : Oct 16, 2009
 * @author joseph Djomeda (joseph@dreamoval.com)
 */
public class DateProvider {
    private static Logger logger = Logger.getLogger(DateProvider.class);
    static DateFormat df;
    static Date date;

    /**
     * Converts a string representation of a Date object and parses it based on the formating pattern specified.
     * @param stringDate Date to parse
     * @return A Date Object with the new formating.
     */
    public static Date convertToDateTime(String stringDate) {
        df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = df.parse(stringDate);
        } catch (ParseException e) {
            logger.debug("Error occured while converting to datetime",e);
        }
        return date;
    }

   /**
    * Provides new Date based on a certain formating pattern
    * @return new Date with the specified formating.
    */
    public static Date getNowDateTime() {
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = df.parse("");
        } catch (ParseException e) {
            logger.debug("Error occured while getting current datetime",e);
        }
        return date;

    }

    /**
     * Provides a String representation of new Date based on certain formating pattern
     * @return String representation of new Date
     */
     public static String getNowStringDateTime() {
         
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String stringdate;
            stringdate = df.format(new Date());
            return stringdate;
        } catch (Exception e) {
             logger.debug("Error occured while getting cuurent datetime in string",e);
            return "";
        }
      

    }

}
