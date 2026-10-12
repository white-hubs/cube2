package com.guhecloud.platform.base.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public final class PinYinUtils {
    private PinYinUtils() {}

    public static String getFull(String src) {

        char[] srcChar = src.toCharArray();
        String[] srcArry = new String[srcChar.length];
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        // 设置格式
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String result = "";
        try {
            for (char c : srcChar) {
                // 判断是否为汉字字符
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    srcArry = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    result += srcArry.length > 0 ? srcArry[0] : "";
                } else {
                    result += Character.toString(c);
                }
            }
            return result;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            throw new RuntimeException("转换拼音失败", e1);
        }
    }

    public static String getHead(String str) {
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                result.append(pinyinArray[0].charAt(0));
            } else {
                result.append(word);
            }
        }
        return result.toString();
    }

}