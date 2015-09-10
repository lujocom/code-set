package com.channelsoft.codeset.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * <dl>
 * <dt>CodeSet</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 北京青牛风科技有限公司</dd>
 * <dd>CreateDate: 2015年09月10日</dd>
 * </dl>
 *
 * @author LuoHui
 */
public class StringUtils {

    /**
     * 匹配字符串格式，是否是数字一个某个间隔符所组成的字符串
     * @param targetStr 匹配目标字符串
     * @param regx 字符间隔符号
     * @return 如果是返回true 否则返回false
     */
    public static boolean isMatchFormateFromString(String targetStr, char regx) {
        CharMatcher idsCharMatcher = CharMatcher.DIGIT.or(CharMatcher.is(regx));
        return idsCharMatcher.matchesAllOf(targetStr);
    }

    /**
     * 将字符串以分割符分割成Integer数组, 一般字符必须经过匹配字符串格式之后，<br/>
     * 才能使用此函数，字符串格式一般为数字+分割符
     * @param targetStr 目标字符串
     * @param splitChar 分割符
     * @return 分割后的Integer数组
     */
    public static Integer[] splitStrToIntArray(String targetStr, char splitChar) {

        if(Strings.isNullOrEmpty(targetStr)){
            return null;
        }

        List<String> resultList = Lists.newArrayList(Splitter.on(splitChar).
                omitEmptyStrings().trimResults().split(targetStr));

        Integer[] integerArr = new Integer[resultList.size()];
        for(int index = 0; index < integerArr.length; index ++){
            integerArr[index] = Integer.parseInt(resultList.get(index));
        }

        return integerArr;
    }

    /**
     * 将字符串按照分隔符分割成一个字符数组
     * @param targetStr 目标字符串
     * @param splitChar 分割符
     * @return 字符数组
     */
    public static String[] splitStrToArray(String targetStr, char splitChar) {

        if(Strings.isNullOrEmpty(targetStr)){
            return null;
        }

        List<String> resultList = Lists.newArrayList(Splitter.on(splitChar).
                omitEmptyStrings().trimResults().split(targetStr));

        return resultList.toArray(new String[resultList.size()]);

    }

    /**
     * 将字符串数组找出某个字符串中以匹配符结尾的字符串
     * @param fromArray 目标字符串数组
     * @param regx 匹配符
     * @return 匹配字符串
     */
    public static String getStrFromStrArray(String[] fromArray, String regx){

        for (String element : fromArray){
            if(!Strings.isNullOrEmpty(element) && element.endsWith(regx)){
                return element;
            }
        }
        return null;
    }



}
