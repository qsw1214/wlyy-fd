package com.yihu.wlyy.util;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String substring(String str, int start) {
        return StringUtils.substring(str, start);
    }

    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static String toString(Object obj) {

        if (obj == null) {
            return null;
        }

        return obj.toString();
    }

    public static String trimLeft(String value) {
        if (value == null)
            return "";
        String result = value;
        char ch[] = result.toCharArray();
        int index = -1;
        for (int i = 0; i < ch.length; i++) {
            if (Character.isWhitespace(ch[i])) {
                index = i;
            } else {
                break;
            }
        }
        if (index != -1) {
            result = result.substring(index + 1);
        }
        return result;
    }

    public static String trimRight(String value) {
        if (value == null)
            return "";
        String result = value;
        char ch[] = result.toCharArray();
        int endIndex = -1;
        for (int i = ch.length - 1; i > -1; i--) {
            if (Character.isWhitespace(ch[i])) {
                endIndex = i;
            } else {
                break;
            }
        }
        if (endIndex != -1) {
            result = result.substring(0, endIndex);
        }
        return result;
    }

    public static String fillHeadCharsLen(String strOri, int len) {
        return fillHeadCharsLen(strOri, "0", len);
    }

    public static String fillBackCharsLen(String strOri, int len) {
        return fillBackCharsLen(strOri, "0", len);
    }

    public static String fillHeadCharsLen(String strOri, String subStr, int len) {
        if (strOri == null || strOri.trim().length() == 0) {
            strOri = "";
        }
        if (subStr == null) {
            subStr = " ";
        }
        String fillStr = "";
        for (int i = 0; i < len; i++) {
            fillStr = fillStr + subStr;
        }
        subStr = fillStr + strOri;

        return (subStr.substring(subStr.length() - len, subStr.length()));
    }

    public static String fillBackCharsLen(String strOri, String subStr, int len) {
        if (strOri == null || strOri.trim().length() == 0) {
            strOri = "";
        }
        if (subStr == null) {
            subStr = " ";
        }
        String fillStr = "";
        for (int i = 0; i < len; i++) {
            fillStr = fillStr + subStr;
        }
        subStr = strOri + fillStr;

        return (subStr.substring(0, len));
    }

    public static String fillHeadChars(String strOri, int counter) {
        return fillHeadChars(strOri, "0", counter);
    }

    public static String fillBackChars(String strOri, int counter) {
        return fillBackChars(strOri, "0", counter);
    }

    public static String fillHeadChars(String strOri, String subStr, int counter) {
        if (strOri == null || strOri.trim().length() == 0) {
            strOri = "";
        }
        if (counter <= 0 || subStr == null) {
            return strOri;
        }
        String fillStr = "";
        for (int i = 0; i < counter; i++) {
            fillStr = fillStr + subStr;
        }
        return (fillStr + strOri);
    }

    public static String fillBackChars(String strOri, String subStr, int counter) {
        if (strOri == null || strOri.trim().length() == 0) {
            strOri = "";
        }
        if (counter <= 0 || subStr == null) {
            return strOri;
        }
        String fillStr = "";
        for (int i = 0; i < counter; i++) {
            fillStr = fillStr + subStr;
        }
        return (strOri + fillStr);
    }

    public static boolean isEmpty(Object strObj) {
        if (strObj == null || strObj.toString().trim().length() < 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(Object strObj) {
        return !isEmpty(strObj);
    }


    public static boolean isStrEmpty(String str) {
        if ((str == null) || (str.trim().length() < 1) || "null".endsWith(str.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    public static String getValue(String str) {
        if (str == null) {
            return "";
        }
        if (str.trim().length() <= 0)
            return "";
        str = "H" + str;
        str = str.trim();
        str = str.substring(1);
        return str;
    }

    public static boolean chkTextLen(String text, int len) {
        if (text == null || text.length() > len) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean chkTextTrimLen(String text, int len) {
        if (text == null || text.trim().length() > len) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isStrEn(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) > 127) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCharNum(char ch) {
        if (ch > 47 && ch < 58) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isStrNum(String str) {
        if (isStrEmpty(str)) {
            return true;
        }
        boolean notNum = false;
        for (int i = 0; i < str.length(); i++) {
            if (!isCharNum(str.charAt(i))) {
                notNum = true;
            }
        }
        return !notNum;
    }

    public static boolean isNum(String strSrc) throws Exception {
        for (int i = 0; i < strSrc.length(); i++) {
            if (!isCharNum(strSrc.charAt(i)))
                return false;
        }
        return true;
    }

    public static boolean isCharLetter(char ch) {
        if ((ch >= 65 && ch <= 90) && (ch >= 97 && ch <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isStrLetter(String str) {
        if (isStrEmpty(str))
            return true;
        boolean notLetter = false;
        for (int i = 0; i < str.length(); i++) {
            if (!isCharLetter(str.charAt(i))) {
                notLetter = true;
            }
        }
        return !notLetter;
    }

    public static char strToChar(String src) {
        src = src.trim();
        char result = src.charAt(0);
        return result;
    }

    public static String encodeSQL(String sql) {
        StringBuffer tempBuff = new StringBuffer();
        for (int i = 0; i < sql.length(); i++) {
            tempBuff.append(Integer.toHexString(sql.charAt(i)));
        }
        return tempBuff.toString();
    }

    public static String decodeSQL(String encoded) {
        StringBuffer tempBuff = new StringBuffer();
        for (int i = 0; i < encoded.length(); i += 2) {
            tempBuff.append((char) Integer.parseInt(
                    encoded.substring(i, i + 2), 16));
        }
        return tempBuff.toString();
    }

    public static String getAbsolutePath(String path1, String context1) {
        int i1 = path1.indexOf(context1);
        if (i1 < 0) {
            return path1;
        } else {
            return path1.substring(path1.indexOf(context1) + context1.length());
        }
    }

    public static String getSubString(String str1, int sindex, int eindex) {
        if (str1 == null) {
            return "";
        }
        if (str1.trim().length() <= 0)
            return "";
        if (str1.length() > sindex) {
            if (eindex >= 0)
                return str1.substring(sindex, eindex);
            else if (eindex < 0)
                return str1.substring(sindex);
        }
        return "";
    }

    public static String[] getValues(String[] strs, int size1) {
        String[] strs1 = new String[size1];
        for (int i = 0; i < size1; i++) {
            strs1[i] = "";
        }
        if (strs == null) {
            return strs1;
        } else {
            if (strs.length < size1) {
                for (int i = 0; i < strs.length; i++) {
                    strs1[i] = strs[i];
                }
                return strs1;
            } else {
                return strs;
            }
        }
    }

    public static String replaceStrAll(String strSource, String strFrom,
                                       String strTo) {
        String strDest = "";
        int intFromLen = strFrom.length();
        int intPos;
        while ((intPos = strSource.indexOf(strFrom)) != -1) {
            strDest = strDest + strSource.substring(0, intPos);
            strDest = strDest + strTo;
            strSource = strSource.substring(intPos + intFromLen);
        }
        strDest = strDest + strSource;
        return strDest;
    }

    public static String replaceStr(String strTarget, String strNew) {

        int iIndex = -1;
        while (true) {

            iIndex = strTarget.indexOf('\n');

            if (iIndex < 0) {
                break;
            }

            String strTemp = null;
            strTemp = strTarget.substring(0, iIndex);

            strTarget = strTemp + strNew + strTarget.substring(iIndex + 1);

        }

        return strTarget;

    }

    public static boolean includestr(String str1, String[] strarray) {
        if (strarray == null || strarray.length <= 0)
            return false;
        for (int i = 0; i < strarray.length; i++) {
            if (strarray[i] == null) {
                if (str1 == null)
                    return true;
                else
                    continue;
            }
            if (strarray[i].trim().equals(str1)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getAreaValues(String fvalue) {
        String tmpstr = fvalue;
        int i = 0;
        if (tmpstr == null)
            return null;
        if (tmpstr.trim().equals(""))
            return null;
        while (tmpstr.indexOf("\n") >= 0) {
            i++;
            tmpstr = tmpstr.substring(tmpstr.indexOf("\n") + 1);
        }
        if (tmpstr.trim().equals("")) {
            i--;
        }
        String[] fvalues = new String[i + 1];
        tmpstr = fvalue;
        i = 0;
        while (tmpstr.indexOf("\n") >= 0) {
            fvalues[i] = tmpstr.substring(0, tmpstr.indexOf("\n"));
            if (fvalues[i].indexOf("\r") >= 0)
                fvalues[i] = fvalues[i].substring(0, fvalues[i].indexOf("\r"));
            i++;
            tmpstr = tmpstr.substring(tmpstr.indexOf("\n") + 1);
        }
        if (!tmpstr.trim().equals(""))
            fvalues[i] = tmpstr;
        return fvalues;
    }

    public static String getrealAreaValues(String fvalue) {
        String tmpstr = fvalue;
        String returnstr = "";
        if (tmpstr == null)
            return null;
        if (tmpstr.trim().equals(""))
            return "";
        while (tmpstr.indexOf("|") > 0) {
            returnstr += tmpstr.substring(0, tmpstr.indexOf("|")) + "\n";
            tmpstr = tmpstr.substring(tmpstr.indexOf("|") + 1);
        }
        return returnstr;
    }

    public static int countChar(String strInput, char chr) {
        int iCount = 0;
        char chrTmp = ' ';

        if (strInput.trim().length() == 0)
            return 0;
        for (int i = 0; i < strInput.length(); i++) {
            chrTmp = strInput.charAt(i);
            if (chrTmp == chr) {
                iCount++;
            }
        }
        return iCount;
    }

    public static String strArrayToStr(String[] strs) {
        return strArrayToStr(strs, null);
    }

    public static void printStrs(String[] strs) {
        for (int i = 0; i < strs.length; i++) {
            System.out.println(strs[i]);
        }
    }

    public static void printDualStr(String[][] dualStr) {
        for (int i = 0; i < dualStr.length; i++) {
            for (int j = 0; j < dualStr[i].length; j++) {
                System.out.print(dualStr[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String[][] rowToColumn(String[][] dualStr) {
        String[][] returnDualStr = null;
        if (dualStr != null) {
            returnDualStr = new String[dualStr[0].length][dualStr.length];
            for (int i = 0; i < dualStr.length; i++)
                for (int j = 0; j < dualStr[0].length; j++)
                    returnDualStr[j][i] = dualStr[i][j];
        }
        return returnDualStr;
    }

    public static String latinString(String inStr) {
        String res = inStr;
        if (null == res)
            return null;
        res = replaceStrAll(res, "\"", "\\\"");
        res = replaceStrAll(res, "'", "\\'");
        return res;
    }

    public static String replaceWhiteSpace(String strTarget, String strNew) {

        int iIndex = -1;
        while (true) {
            char cRep = 32;
            iIndex = strTarget.indexOf(cRep);

            if (iIndex < 0) {
                break;
            }

            String strTemp = null;
            strTemp = strTarget.substring(0, iIndex);

            strTarget = strTemp + strNew + strTarget.substring(iIndex + 1);

        }

        return strTarget;

    }

    public static String double2str(double amount, int length) {
        String strAmt = Double.toString(amount);

        int pos = strAmt.indexOf('.');

        if (pos != -1 && strAmt.length() > length + pos + 1)
            strAmt = strAmt.substring(0, pos + length + 1);

        return strAmt;
    }

    public static String[] doSplit(String str, char chr) {
        int iCount = 0;
        char chrTmp = ' ';
        // ?????????????????
        for (int i = 0; i < str.length(); i++) {
            chrTmp = str.charAt(i);
            if (chrTmp == chr) {
                iCount++;
            }
        }
        String[] strArray = new String[iCount];
        for (int i = 0; i < iCount; i++) {
            int iPos = str.indexOf(chr);
            if (iPos == 0) {
                strArray[i] = "";
            } else {
                strArray[i] = str.substring(0, iPos);
            }
            str = str.substring(iPos + 1); // ??iPos+1??????,str??????????
        }
        return strArray;
    }

    public static String[] strSplit(String src, String splitchar) {
        int resultSize = 0;
        int len = src.length();
        int idx = 0;
        String strTemp = "";
        for (int i = 0; i < len; i++) {
            if (src.substring(i, i + 1).equals(splitchar)) {
                resultSize++;

            }
        }
        if ((len > 1) & (!src.substring(len - 1, len).equals(splitchar))) {
            resultSize++;
        }
        String result[] = new String[resultSize];
        for (int i = 0; i < len; i++) {
            if (src.substring(i, i + 1).equals(splitchar)) {
                result[idx] = strTemp;
                idx++;
                strTemp = "";
            } else {
                strTemp = String.valueOf(strTemp)
                        + String.valueOf(src.charAt(i));
            }
        }

        if (!strTemp.equals("")) {
            result[idx] = strTemp;
        }
        return result;
    }

    public static String[] split(String strToSplit, String strSeparator,
                                 int iLimit) {
        ArrayList tmpList = new ArrayList();
        int iFromIndex = 0;
        int iCurIndex = strToSplit.length();
        String strUnitInfo = "";
        int iCurCounts = 0;
        while ((iCurIndex != -1) && (iFromIndex < strToSplit.length())
                && (iCurCounts < iLimit)) {
            iCurIndex = strToSplit.indexOf(strSeparator, iFromIndex);
            if (iCurIndex == -1) {
                strUnitInfo = strToSplit.substring(iFromIndex, strToSplit
                        .length());
            } else {
                strUnitInfo = strToSplit.substring(iFromIndex, iCurIndex);
                iFromIndex = iCurIndex + 1;
            }
            tmpList.add(strUnitInfo);
            iCurCounts++;
        }
        int iCounts = tmpList.size();
        String tmpArray[] = new String[iCounts];
        for (int i = 0; i < iCounts; i++) {
            tmpArray[i] = (String) tmpList.get(i);
        }
        return tmpArray;
    }

    public static String strIntercept(String src, int len) {
        if (src == null) {
            return "";
        }
        if (src.length() > len) {
            src = String.valueOf(String.valueOf(src.substring(0, len))).concat(
                    "...");
        }
        return src;
    }

    public static String strtochn(String str_in) {
        try {
            String temp_p = str_in;
            if (temp_p == null) {
                temp_p = "";
            }
            String temp = "";
            if (!temp_p.equals("")) {
                byte[] byte1 = temp_p.getBytes("ISO8859_1");
                temp = new String(byte1);
            }
            return temp;
        } catch (Exception e) {
        }
        return "null";
    }

    public static String ISO2GBK(String strvalue) {
        try {
            if (strvalue == null)
                return null;
            else {
                strvalue = new String(strvalue.getBytes("ISO8859_1"), "GBK");
                return strvalue;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ***************************************************** ??
     * ????????????????????????????? ????????param str ??????????????? ????????? ?? ???return String
     * ????????????? ??????? ******************************************************
     */
    public static String cnCodeTrans(String str) {
        String s = "";
        try {
            s = new String(str.getBytes("GB2312"), "8859_1");
        } catch (UnsupportedEncodingException a) {
            System.out.print("chinese thansform exception");
        }
        return s;
    }

    /**
     * ***************************************************** ??
     * ???????????????????????STaaaa????ST****** ????????param strSource???????????????param
     * strRule???? ????????? ?? ???return false:???????????true:????????? ???????
     * ******************************************************
     */
    public static boolean judgeMatch(String strSource, String strRule) {
        int i = 0;
        // ???????????
        if ((null == strSource) || (strSource.length() == 0))
            return false;
        // ???????????
        if ((null == strRule) || (strRule.length() == 0))
            return false;
        // ??????????
        if (strSource.length() > strRule.length())
            return false;
        // ??????????
        for (i = 0; i < strRule.length(); i++) {
            // ?????????
            if (strSource.length() < i + 1) {
                break;
            }
            if ((strRule.charAt(i) != '*')
                    && (strSource.charAt(i) != strRule.charAt(i))) {
                return false;
            }
        }
        // ??????????????????????????????'*'?????????
        for (; i < strRule.length(); i++) {
            if (strRule.charAt(i) != '*')
                return false;
        }
        return true;
    }

    public static String column2Property(String column) {
        column = column.toLowerCase();
        int i = column.indexOf("_");
        while (i != -1) {
            if (i != column.length() - 1) {
                char temp = column.charAt(i + 1);
                String strTemp = String.valueOf(temp);
                column = column.replaceFirst("_" + strTemp, strTemp
                        .toUpperCase());
                i = column.indexOf("_");
            } else {
                break;
            }
        }
        return column;
    }

    public static String strArrayToStr(String[] strs, String separator) {
        StringBuffer returnstr = new StringBuffer("");

        if (strs == null)
            return "";
        if (separator == null)
            separator = "";

        for (int i = 0; i < strs.length; i++) {
            returnstr.append(strs[i]);
            if (i < strs.length - 1)
                returnstr.append(separator);
        }
        return returnstr.toString();
    }

    public static String objectArrayToStr(Object[] objects, String separator) {
        StringBuffer returnstr = new StringBuffer("");

        if (objects == null)
            return "";
        if (separator == null)
            separator = "";

        for (int i = 0; i < objects.length; i++) {
            returnstr.append(String.valueOf(objects[i]));
            if (i < objects.length - 1)
                returnstr.append(separator);
        }
        return returnstr.toString();
    }

    public static String listToStr(List element, String separator) {

        StringBuffer returnstr = new StringBuffer("");

        if (element == null)
            return "";
        if (separator == null)
            separator = "";

        Iterator it = element.iterator();

        while (it.hasNext()) {
            returnstr.append(String.valueOf(it.next()));
            if (it.hasNext())
                returnstr.append(separator);
        }

        return returnstr.toString();
    }

    public static String[] listToStrArray(List element) {

        if (element == null || element.size() == 0)
            return null;

        Iterator it = element.iterator();
        String[] strArray = new String[element.size()];
        int i = 0;

        while (it.hasNext()) {
            strArray[i] = String.valueOf(it.next());
            i++;
        }
        return strArray;
    }

    public static List strToList(String str, String separator) {

        if (str == null || str.equals(""))
            return null;
        if (separator == null)
            separator = "";

        String[] strArr = str.split(separator);
        int size = strArr.length;
        List list = new ArrayList();

        for (int i = 0; i < size; i++) {
            list.add(strArr[i]);
        }
        return list;
    }

    public static StringBuffer populate(StringBuffer bf, String value,
                                        boolean isNotLast) {
        if (value == null) {
            return bf;
        }
        // ????????????????????I??????????????????????????????SQL???????????
        System.out.println(value.replaceAll("'", "''"));
        bf.append("'").append(value.replaceAll("'", "''")).append("'");
        if (isNotLast)
            bf.append(",");
        return bf;
    }

    public static boolean isExist(String str, String substr, String sepatator) {
        if (str == null || str.trim().equals(""))
            return false;
        if (substr == null || substr.trim().equals(""))
            return false;
        String[] strArr = str.split(sepatator);
        int size = strArr.length;
        for (int i = 0; i < size; i++) {
            if (strArr[i].equals(substr))
                return true;
        }
        return false;
    }

    public static boolean isExist(String str, String substr) {
        return isExist(str, substr, ",");
    }

    public static String leftInclude(String str) {
        if (str == null || str.equals(""))
            return str;
        return str + "%";
    }

    public static String rightInclude(String str) {
        if (str == null || str.equals(""))
            return str;
        return "%" + str;
    }

    public static String include(String str) {
        if (str == null || str.equals(""))
            return str;
        return "%" + str + "%";
    }

    public static String nvl(Object source, Object target) {
        return source != null ? String.valueOf(source)
                : (target != null ? String.valueOf(target) : null);
    }

    public static String clob2Str(Object clobValue) throws SQLException {
        return ((Clob) clobValue).getSubString(1, (int) ((Clob) clobValue)
                .length());
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String hideFlowStr(String str, int maxLen) {

        if (str == null || maxLen == 0) {
            return "";
        }

        if (str.length() > maxLen) {
            str = str.substring(0, maxLen) + "...";
        }

        return str;
    }

    public static String escapeLuceneSpecialCharacters(String str) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeLuceneSpecialCharacterString(writer, str);

            return writer.toString();
        } catch (IOException ex) {
            //LogService.getLogger(StringUtil.class).error(ex.getMessage());

            return null;
        }
    }

    private static void escapeLuceneSpecialCharacterString(Writer out,
                                                           String str) throws IOException {
        if (str == null) {
            return;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            /*
             * Reference link:
			 * http://lucene.apache.org/java/docs/queryparsersyntax.html Lucene
			 * supports escaping special characters that are part of the query
			 * syntax. The current list special characters are: + - && || ! ( ) { } [ ] ^ " ~ * ? : \
			 * To escape these character use the \ before the character.
			 */

            switch (ch) {
                case '+':
                case '-':
                case '!':
                case '(':
                case ')':
                case '{':
                case '}':
                case '[':
                case ']':
                case '^':
                case '\"':
                case '~':
                case '*':
                case '?':
                case ':':
                case '\\':
                    out.write('\\');
                    out.write(ch);
                    break;
                case '&':
                case '|':
                    // check if it is '&&' or '||' ~ check if the next char is '&'
                    // or '|'
                    if ((i + 1) < sz && str.charAt(i + 1) == ch) {
                        out.write('\\');
                        out.write(ch);
                        i++;
                    }
                    out.write(ch);
                    break;
                default:
                    out.write(ch);
                    break;
            }
        }
    }

    public static String getFirstLetter(String chinese) {
        final int[] li_SecPosValue = {1601, 1637, 1833, 2078, 2274, 2302,
                2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
                4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};
        final String[] lc_FirstLetter = {"A", "B", "C", "D", "E", "F", "G",
                "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "W", "X", "Y", "Z"};
        if (chinese == null || chinese.trim().length() == 0) {
            return "";
        }

        chinese = conversionStr(chinese, "GB2312", "ISO8859-1");
        if (chinese.length() > 1) { // ????????????
            int li_SectorCode = (int) chinese.charAt(0); // ????????
            int li_PositionCode = (int) chinese.charAt(1); // ????????
            li_SectorCode = li_SectorCode - 160;
            li_PositionCode = li_PositionCode - 160;
            int li_SecPosCode = li_SectorCode * 100 + li_PositionCode;// ??????????
            if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
                for (int i = 0; i < 23; i++) {
                    if (li_SecPosCode >= li_SecPosValue[i]
                            && li_SecPosCode < li_SecPosValue[i + 1]) {
                        chinese = lc_FirstLetter[i];
                        break;
                    }
                }
            } else {
                chinese = conversionStr(chinese, "ISO8859-1", "GB2312");
                chinese = chinese.substring(0, 1);
            }
        }
        return chinese;
    }

    private static String conversionStr(String str, String charsetName,
                                        String toCharsetName) {
        try {
            str = new String(str.getBytes(charsetName), toCharsetName);
        } catch (UnsupportedEncodingException ex) {

        }
        return str;
    }

    public static String checkStr(String html) {
        try {
            html = html.replaceAll("\r", "");
            html = html.replaceAll("\n", "");
            html = html.replaceAll("\"", "'");
            html = html.replaceAll("\t", " ");
            Pattern p_script;
            Matcher m_script;
            Pattern p_href;
            Matcher m_href;
            Pattern p_a;
            Matcher m_a;
            Pattern p_on;
            Matcher m_on;

            Pattern p_iframe;
            Matcher m_iframe;
            Pattern p_frameset;
            Matcher m_frameset;

            Pattern p_img;
            Matcher m_img;

            Pattern p_p1;
            Matcher m_p1;
            Pattern p_p2;
            Matcher m_p2;

            String stript_str = "<script[\\s\\S]+</script *>";
            String href_str = " href *= *[\\s\\S]*script *:";
            String on_str = " on[\\s\\S]*=";
            String iframe_str = "<iframe[\\s\\S]+</iframe *>";
            String frameset_str = "<frameset[\\s\\S]+</frameset *>";
            String a_str = "<a ([^>])*>.*?</a([^>])*>";
            //String img_str = "\\<img[^\\>]+\\>";
            String p_str1 = "</p>";
            String p_str2 = "<p>";
            p_script = Pattern.compile(stript_str, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(html);
            html = m_script.replaceAll(""); // ????script???

            p_href = Pattern.compile(href_str, Pattern.CASE_INSENSITIVE);
            m_href = p_href.matcher(html);
            html = m_href.replaceAll(""); // ????script???

            p_a = Pattern.compile(a_str, Pattern.CASE_INSENSITIVE);
            m_a = p_a.matcher(html);
            html = m_a.replaceAll(""); // ????script???

            p_on = Pattern.compile(on_str, Pattern.CASE_INSENSITIVE);
            m_on = p_on.matcher(html);
            html = m_on.replaceAll(""); // ????script???

            p_iframe = Pattern.compile(iframe_str, Pattern.CASE_INSENSITIVE);
            m_iframe = p_iframe.matcher(html);
            html = m_iframe.replaceAll(""); // ????script???

            p_frameset = Pattern
                    .compile(frameset_str, Pattern.CASE_INSENSITIVE);
            m_frameset = p_frameset.matcher(html);
            html = m_frameset.replaceAll(""); // ????script???

            // p_img = Pattern.compile(img_str, Pattern.CASE_INSENSITIVE);
            // m_img = p_img.matcher(html);
            // html = m_img.replaceAll(""); // ????script???

            p_p1 = Pattern.compile(p_str1, Pattern.CASE_INSENSITIVE);
            m_p1 = p_p1.matcher(html);
            html = m_p1.replaceAll(""); // ????script???

            p_p2 = Pattern.compile(p_str2, Pattern.CASE_INSENSITIVE);
            m_p2 = p_p2.matcher(html);
            html = m_p2.replaceAll(""); // ????script???
        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return html;
    }

    public static String substr(String value, int maxlength, String postfix) {

        int k = 0;
        int l = 0;
        for (int i = 0; i < value.length() && maxlength > l * 2 + k; i++) {
            if (value.charAt(i) > '\200') {
                l++;
            } else {
                k++;
            }
        }
        if (l + k >= value.length()) {
            return value;
        } else if (maxlength >= l * 2 + k && l + k > 0) {
            value = value.substring(0, l + k);
        } else if (l + k > 0) {
            value = value.substring(0, (l + k) - 1);
        } else {
            return value;
        }
        if (!StringUtils.isEmpty(postfix)) {
            value += postfix;
        }
        return value;
    }


    public String nullToSpace(String Content) {
        if (Content == null) {
            Content = "";
        }
        return Content;
    }

    public String GBK2ISO(String strvalue) throws Exception {
        try {
            if (strvalue == null)
                return null;
            else {
                strvalue = new String(strvalue.getBytes("GBK"), "ISO8859_1");
                return strvalue;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static   String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}
