package com.guhecloud.cube.deliver.project.merchant.utils;

import java.util.*;

public class SensitiveWordUtil {

    @SuppressWarnings("rawtypes")
    private static Map sensitiveWordMap = SensitiveWordInit.sensitiveWordMap;

    private static void initSensitiveWord(List<String> datas) {
        sensitiveWordMap = SensitiveWordInit.init(datas);
    }

    /**
     * 替换敏感字字符
     * 替换字符，默认*
     *
     * @param txt
     * @author
     * @date
     * @version 1.0
     */
    public static String replaceSensitiveWord(String txt) {
        String resultTxt = txt;
        try {
            List<String> set = SensitiveWordInit.getSensitiveWord(txt);     //获取所有的敏感词
            // 敏感词排序,一定要把长的先替换
            set.sort(Comparator.comparing(String::length, Comparator.reverseOrder()));
            Iterator<String> iterator = set.iterator();
            String word = null;
            String replaceString = null;
            while (iterator.hasNext()) {
                word = iterator.next();
                replaceString = getReplaceChars("*", word.length());
                resultTxt = resultTxt.replaceAll(word, replaceString);
            }
        } catch (Exception e) {

        }
        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     * @author
     * @date
     * @version 1.0
     */
    private static String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        if (length > 6) {
            length = 6;
        }
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }
        return resultReplace;
    }


    /**
     * 添加敏感词
     *
     * @param str
     */
    private static boolean addSensitiveWord(String str) {
        if (sensitiveWordMap == null) {
            return false;
        }
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        nowMap = sensitiveWordMap;
        for (int i = 0; i < str.length(); i++) {
            char keyChar = str.charAt(i);       //转换成char型
            Object wordMap = nowMap.get(keyChar);       //获取
            if (wordMap != null) {        //如果存在该key，直接赋值
                nowMap = (Map) wordMap;
            } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                newWorMap = new HashMap<String, String>();
                newWorMap.put("isEnd", "0");     //不是最后一个
                nowMap.put(keyChar, newWorMap);
                nowMap = newWorMap;
            }
            if (i == str.length() - 1) {
                nowMap.put("isEnd", "1");    //最后一个
            }
        }
        return true;
    }

}
