package com.haiyu.manager.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author jk
 * @Update zhugy
 */
public class StringUtil extends StringUtils {


    /**
     * 获取字符串, null转换为空字符串
     *
     * @param src 源对象
     * @return 字符串
     */
    public static String getStr(Object src) {
        return getStr(src, -1);
    }

    public static String getTrimedStr(Object src) {
        return getTrimedStr(src, -1);
    }

    public static String getStr(Object src, String defaultValue) {
        return getStr(src, -1, defaultValue);
    }

    public static String getTrimedStr(Object src, String defaultValue) {
        return getTrimedStr(src, -1, defaultValue);
    }

    /**
     * 获取定长的字符串, null转换为空字符串
     *
     * @param src    源对象
     * @param length 字符串长度
     * @return 字符串
     */
    public static String getStr(Object src, int length) {
        return getStr(src, 0, length);
    }

    public static String getTrimedStr(Object src, int length) {
        return getTrimedStr(src, 0, length);
    }

    public static String getStr(Object src, int length, String defaultValue) {
        return getStr(src, 0, length, defaultValue);
    }

    public static String getTrimedStr(Object src, int length,
                                      String defaultValue) {
        return getTrimedStr(src, 0, length, defaultValue);
    }

    /**
     * 从start位置开始获取定长字符串, null转换为空字符串
     *
     * @param src    源对象
     * @param start  起始位置
     * @param length 长度
     * @return
     */
    public static String getStr(Object src, int start, int length) {
        return getStr(src, start, length, "", false);
    }

    public static String getTrimedStr(Object src, int start, int length) {
        return getStr(src, start, length, "", true);
    }

    public static String getStr(Object src, int start, int length,
                                String defaultValue) {
        return getStr(src, start, length, defaultValue, false);
    }

    public static String getTrimedStr(Object src, int start, int length,
                                      String defaultValue) {
        return getStr(src, start, length, defaultValue, true);
    }

    public static String getStr(Object src, int start, int length,
                                String defaultValue, boolean trim) {
        if (src == null) {
            return defaultValue;
        }

        String value = src.toString();

        if (value.length() > start) {
            if (length < 0 || value.length() < start + length) {
                value = value.substring(start);
            } else {
                value = value.substring(start, start + length);
            }
        } else {
            value = "";
        }

        return trim ? value.trim() : value;
    }

    /**
     * 从Blob中获取字符串
     *
     * @param blob Blob对象
     * @return 字符串
     */
    public static String fromBlob(Blob blob) {
        if (blob == null) {
            return "";
        }


        // StringBuffer sb = new StringBuffer();
        try {

            byte[] array = new byte[1000];
            InputStream in = blob.getBinaryStream();

            /**
             * hjs 为了修改乱码的问题
             */
            // 保存每次读取的字节列表
            Vector<byte[]> v = new Vector<byte[]>();

            // 保存每次读取的字节长度
            Vector<Integer> v1 = new Vector<Integer>();
            int len = in.read(array, 0, 1000);

            // 保存所有字节的长度
            int alllen = 0;
            while (len > 0) {
                // sb.append(new String(array, 0, len));
                alllen = alllen + len;
                v.add(array);
                v1.add(new Integer(len));
                array = new byte[1000];
                len = in.read(array, 0, 1000);
            }
            if (alllen > 0 && v.size() > 0) {
                byte[] arraytemp = new byte[alllen];
                int index = 0;
                for (int i = 0; i < v.size(); i++) {
                    int lentemp = ((Integer) v1.get(i)).intValue();
                    byte[] arraytt = (byte[]) v.get(i);
                    System.arraycopy(arraytt, 0, arraytemp, index, lentemp);
                    index = index + lentemp;
                }
                return new String(arraytemp);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param clob
     * @return 备注：将Clob类型转换成String类型
     * <p/>
     * 2012-5-24 上午10:17:23
     * @author zj
     */
    public static String fromClob(Clob clob) {
        if (clob == null) {
            return "";
        }
        String resultString = "";
        try {
            Reader reader = clob.getCharacterStream();
            BufferedReader br = new BufferedReader(reader);

            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                sb.append(s);
                s = br.readLine();
            }
            resultString = sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * 将字符串根据spilter(非正则表达式)拆分开
     *
     * @param str
     * @param spilter
     * @return
     */
    public static String[] split(String str, String spilter) {
        if (str == null) {
            return null;
        }
        if ((spilter == null) || (spilter.equals(""))
                || (str.length() < spilter.length())) {
            String[] t = {str};
            return t;
        }
        ArrayList<String> al = new ArrayList<String>();
        char[] cs = str.toCharArray();
        char[] ss = spilter.toCharArray();
        int length = spilter.length();
        int lastIndex = 0;
        for (int i = 0; i <= str.length() - length; ) {
            boolean notSuit = false;
            for (int j = 0; j < length; ++j) {
                if (cs[(i + j)] != ss[j]) {
                    notSuit = true;
                    break;
                }
            }
            if (!(notSuit)) {
                al.add(str.substring(lastIndex, i));
                i += length;
                lastIndex = i;
            } else {
                ++i;
            }
        }
        if (lastIndex <= str.length()) {
            al.add(str.substring(lastIndex, str.length()));
        }
        String[] t = new String[al.size()];
        for (int i = 0; i < al.size(); ++i) {
            t[i] = ((String) al.get(i));
        }
        return t;
    }

    @Deprecated()
    /**
     * 建议使用StringUtils.hasLength()
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String getStr(long src) {

        return String.valueOf(src);

    }

    /**
     * 判断是否是null、空字符串、空格组成,是就返回true
     *
     * @param str
     * @return
     */
    public static boolean isNullOrWhiteSpace(String str) {
        return trimAllWhitespace(getStr(str)).length()==0;
    }

    public static void main(String[] args){
        String s = null;
        System.out.println(isNullOrWhiteSpace(s) );
    }

    /**
     * @param str
     * @return
     * @author zj
     * @todo 备注： 将String类型转换成Blob类型
     * <p/>
     * 2012-5-24 上午10:06:24
     */
    public static Blob getBlobfromString(String str) {
        Blob blob = null;
        if (str != null && !"".equals(str)) {
            //byte[] b = str.getBytes();
            //blob = Hibernate.createBlob(b);
            try {
                blob = new SerialBlob(str.getBytes("UTF-8"));
            } catch (SerialException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return blob;
    }

	/*public static Blob getBlobfromByte(byte[] b) {
        return Hibernate.createBlob(b);
	}*/

    /**
     * @param str
     * @return 备注：
     * <p/>
     * 2012-5-24 上午10:08:34
     * @author zj
     */
    public static Clob getClobfromString(String str) {
        Clob clob = null;
        if (str != null && !"".equals(str)) {
            try {
                clob = new SerialClob(str.toCharArray());
            } catch (SerialException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //clob = Hibernate.createClob(str);
        }
        return clob;
    }

    /**
     * @param list
     * @JFree-add-2012-7-5
     * @Remark : 将List[5765,5688]通过默认的分隔符号pCode=","转化为："5765,5688"
     */
    public static String getStrFromList(List<?> list) {
        String str = "";
        if (list == null || list.size() == 0) {
            return str;
        }
        String pCode = ",";
        str = getStrFromList(list, pCode);
        return str;
    }

    /**
     * @param list
     * @param pCode
     * @JFree-add-2012-7-5
     * @Remark : 将List[5765,5688]通过使用分隔符号pCode=","转化为："5765,5688"
     */
    public static String getStrFromList(List<?> list, String pCode) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            String s = StringUtil.getStr(list.get(i));
            if (i > 0) {
                str += pCode;
            }
            str += s;
        }
        return str;
    }

    /**
     * Describle(描述)：字符串大小写转化
     * <p/>
     * 方法名称：getLowerStr
     * <p/>
     * 所在类名：StringUtil
     * <p/>
     * 返回类型：String
     * <p/>
     * 创建人 ：zj
     * <p/>
     * Operate Time:2012-7-18 上午10:33:55
     *
     * @param str
     * @return
     */
    public static String getLowerStr(String str) {
        if (!StringUtil.isEmpty(str)) {
            return str.toLowerCase();
        }
        return "";
    }

    public static String getUpperStr(String str) {
        if (!StringUtil.isEmpty(str)) {
            return str.toUpperCase();
        }
        return "";
    }


    /**
     * add by dkw 20071107 截取宽度固定字符串，若字符串宽度大于参数设置，则按照参数截取加上"..."
     *
     * @param string     待截取字符串
     * @param startIndex 截取开始位置
     * @param width      宽度（一个汉字宽度为宽度单位）
     * @return 截取后的字符串
     */
    public static String getWidthFixedStr(String string, int startIndex,
                                          int width, boolean htmlStr) {
        if (string == null || string.equals(""))
            return "";
        if (startIndex < 0)
            return "";
        if (width < 0)
            return "";

        char baseChar = ' '; // char = 32
        char topChar = '~'; // char = 126
        char c;
        double tempWidth = 0.0;
        int charIndex = 0;
        string = string.substring(startIndex);
        StringBuffer sb = new StringBuffer();
        String resultStr;

        string = string.replaceAll("&lt;", "<");
        string = string.replaceAll("&gt;", ">");
        string = string.replaceAll("&nbsp;", " ");
        string = string.replaceAll("&quot;", "\"");

        while (charIndex <= string.length() - 1 && tempWidth <= width - 1) {
            c = string.charAt(charIndex);
            if (c >= baseChar && c <= topChar)
                tempWidth += 0.5;
            else
                tempWidth += 1; // 汉字加1
            sb.append(c);
            charIndex++;
        }
        resultStr = sb.toString();
        if (resultStr.length() < string.length()) {
            sb.deleteCharAt(resultStr.length() - 1);
            sb.append("...");
            resultStr = sb.toString();
        }
        if (htmlStr) {
            resultStr = resultStr.replaceAll("<", "&lt;");
            resultStr = resultStr.replaceAll(">", "&gt;");
            resultStr = resultStr.replaceAll(" ", "&nbsp;");
            resultStr = resultStr.replaceAll("\"", "&quot;");
        }
        return resultStr;
    }


    public static String removeIfLast(String string, String n) {
        String re = string;

        if (string != null && string.endsWith(n)) {
            re = string.substring(0, string.length() - n.length());
        }
        return re;

    }

    public static String removeIfFirst(String string, String n) {
        String re = string;

        if (string != null && string.startsWith(n)) {
            re = string.substring(n.length(), string.length());
        }
        return re;

    }

    /**
     * 将1,2,3,4||,1,2,3,4,之类的字符串转化为'1','2','3','4'
     */

    public static String addQuots(String ids) {

        if (StringUtil.isEmpty(ids)) {

            return "''";
        }

        String result = "";
        result = removeIfFirst(ids, ",");
        result = removeIfLast(result, ",");
        result = result.replaceAll(",", "','");
        result = "'" + result + "'";

        return result;
    }

    /**
     * 由传入的字符换，按照指定的分割符，组装成List
     *
     * @param str       待处理的字符串
     * @param splitChar 分割的符号，大多数情况下为','
     * @param elementType     只能为number.class的子类
     * @return List
     * @author zhugy
     */
    public static <T extends Number> List<T> makeNumberListFromString(String str, String splitChar, Class<T> elementType) {
        if (!StringUtils.hasLength(str)) {
            return null;
        }


        List<T> list = new ArrayList<T>();

        String[] array = delimitedListToStringArray(str, splitChar);


        for (String element : array) {

            list.add(NumberUtils.parseNumber(element, elementType));

        }
        return list;


    }

    /**
     * 由传入的字符换，按照指定的分割符，组装成List
     * @param str
     * @param splitChar
     * @param elementType
     * @return
     */
    public static List<String> makeListFromString(String str, String splitChar, Class<String> elementType) {
        if (!StringUtils.hasLength(str)) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] array = delimitedListToStringArray(str, splitChar);
        for (String element : array) {
            list.add((String) element);
        }
        return list;
    }

    /**
     * 从content中找出所有匹配patternStr规则的字符串，存入list中
     *
     * @param patternStr  正则表达式
     * @param content
     * @return
     */
    public static List<String> getMatcherList(String patternStr, String content) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 从content中找出所有匹配patternStr规则的字符串，存入StringBuilder中
     * @param patternStr  正则表达式
     * @param content
     * @return
     */
    public static String getMatcherString(String patternStr, String content) {
        StringBuilder str = new StringBuilder();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            str.append(matcher.group()).append(",");
        }
        return str.length()!=0?str.substring(0,str.length()-1):"";
    }

    /**
     * 从content中找出所有匹配patternStr规则的字符串，存入list中
     *
     * @param patternStr  正则表达式
     * @param content
     * @param flag        表达式匹配标识：  Pattern的类变量
     * @return
     */
    public static List<String> getMatcherList(String patternStr, String content, int flag) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(patternStr, flag);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 将传过来的字符串中的undefined或者null转成空字符串
     *
     * @param source
     * @return
     */
    public static String convertUndefineToNull(String source) {
        if (StringUtil.isEmpty(source)) {
            return source;
        }

        return source.replace("undefined", "null");


    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String s) {
        Pattern numberPattern = Pattern.compile("[0-9]*");

        Matcher matcher = numberPattern.matcher(s);


        return matcher.matches();
    }


    public static String connectArray(String[] stringArray) {

        if (stringArray == null || stringArray.length == 0) {

            return null;
        }
        if (stringArray.length == 1 && StringUtil.isEmpty(stringArray[0])) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(stringArray[i]);
            if (i == stringArray.length - 1) {
                return b.toString();
            }
            b.append(",");
        }

    }

    public static String connectList(List list) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(list.get(i));
            if (i == list.size() - 1) {
                return b.toString();
            }
            b.append(",");
        }
    }

    /**
     * 将Set转为逗号隔开的字符串
     * @param set
     * @return
     */
    public static String connectSet(Set<String> set) {
        if (CollectionUtils.isEmpty(set)) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            b.append(iterator.next()).append(",");
        }
        return b.substring(0,b.length()-1);
    }


    public static boolean containsInStringArray(String src, String delimiter, String target) {
        if (StringUtil.isEmpty(src) || StringUtil.isEmpty(delimiter) || StringUtil.isEmpty(target)) {
            return false;
        }
        String[] stringArray = delimitedListToStringArray(src, delimiter);
        for (String srcString : stringArray) {

            if (target.equals(srcString)) {

                return true;
            }
        }

        return false;
    }

    /**
     * 规范字符串中的分隔字符
     * 注意：规范后的字符串中将不包含空格
     * 例如：分隔符为“,”则
     * “,张三 , , 李四,王五,,”==〉“张三,李四,王五”
     *
     * @param strForSplit 待规范的字符串
     * @param splitChr    分隔符，不能是空格，长度应为1
     * @return
     */
    public static String ruleSplit(String strForSplit, String splitChr) {
        if (strForSplit == null || strForSplit.length() == 0
                || splitChr == null || splitChr.equals(" ") || (splitChr.length() != 1)) {
            return strForSplit;
        }
        String strSrc = strForSplit.replace(" ", "").replace(splitChr, " ").trim();
        while (strSrc.indexOf("  ") != -1) {
            strSrc = strSrc.replace("  ", " ");
        }
        return strSrc.replace(" ", splitChr);
    }

    /**
     * 去掉重复的字符串（如：将“addtime, a as type,addtime ” 转为 “addtime, a as type”）
     * <param name="sourceValues">形如：addtime, a as type,addtime</param>
     *
     * @param sourceValues
     * @return
     */
    public static String removeDuplicateString(String sourceValues) {
        StringBuilder sbResult = new StringBuilder();
        List<String> uniqueValue = new ArrayList<String>();
        String[] vals = sourceValues.split(",");
        for (String val : vals) {
            if (uniqueValue.contains(val) == false) {
                uniqueValue.add(val);
                sbResult.append(sbResult.length() != 0 ? "," + val : val);
            }
        }
        return getStr(sbResult);
    }
    /// <summary>
    /// 在字符串srcS前补指定的字符chrAdd直到长为len
    /// </summary>
    /// <param name="source">待补长的字符串（也可以是数字）</param>
    /// <param name="len">补长后的长度</param>
    /// <param name="chrAdd">用以补在其前的字符</param>
    /// <returns>补长后的字符串</returns>
    public static String addCharBefore(String sourceValues,int len,String chrAdd){
        if (StringUtil.isNullOrWhiteSpace(sourceValues))
        {
            return sourceValues;
        }
        while (sourceValues.length() < len)
            sourceValues=chrAdd+sourceValues;
        return sourceValues;
    }
    /// <summary>
    /// 在字符串srcS后补指定的字符chrAdd直到长为len
    /// </summary>
    /// <param name="source">待补长的字符串（也可以是数字）</param>
    /// <param name="len">补长后的长度</param>
    /// <param name="chrAdd">用以补在其后的字符</param>
    /// <returns>补长后的字符串</returns>
    public static String addCharEnd(String sourceValues,int len,String chrAdd){
        if (StringUtil.isNullOrWhiteSpace(sourceValues))
        {
            return sourceValues;
        }

        while (sourceValues.length() < len)
            sourceValues=sourceValues+chrAdd;
        return sourceValues;
    }
}
