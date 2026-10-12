package com.guhecloud.cube.deliver.project.merchant.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class SensitiveWordInit {

    private String ENCODING = "UTF-8";    //字符编码
    @SuppressWarnings("rawtypes")
    public static HashMap sensitiveWordMap;

    public SensitiveWordInit() {
        super();
    }

    /**
     * 初始化词库
     *
     * @param datas 敏感词集合
     * @return
     */
    public static HashMap init(List<String> datas) {
        addSensitiveWord(datas);
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = {
     * isEnd = 0
     * 国 = {<br>
     * isEnd = 1
     * 人 = {isEnd = 0
     * 民 = {isEnd = 1}
     * }
     * 男  = {
     * isEnd = 0
     * 人 = {
     * isEnd = 1
     * }
     * }
     * }
     * }
     * 五 = {
     * isEnd = 0
     * 星 = {
     * isEnd = 0
     * 红 = {
     * isEnd = 0
     * 旗 = {
     * isEnd = 1
     * }
     * }
     * }
     * }
     * {退={党={isEnd=1}, isEnd=0}, 老={isEnd=0, 虎={机={上={分={器={isEnd=1}, isEnd=0}, isEnd=0}, isEnd=1}, isEnd=0}}
     */
    private static void addSensitiveWord(List<String> datas) {
        sensitiveWordMap = new HashMap(datas.size());
        Iterator<String> iterator = datas.iterator();
        Map<String, Object> now = null;
        Map now2 = null;
        while (iterator.hasNext()) {
            now2 = sensitiveWordMap;
            String word = iterator.next().trim(); //敏感词
            for (int i = 0; i < word.length(); i++) {
                char key_word = word.charAt(i);
                Object obj = now2.get(key_word);
                if (obj != null) { //存在
                    now2 = (Map) obj;
                    now2.put("hasNext", "1");
                } else { //不存在
                    now = new HashMap<String, Object>();
                    now.put("isEnd", "0");
                    now.put("hasNext", "1");
                    now2.put(key_word, now);
                    now2 = now;
                }
                if (i == word.length() - 1) {
                    now2.put("isEnd", "1");
                    now.put("hasNext", "0");
                }
            }
        }
    }

    /**
     * 获取内容中的敏感词
     *
     * @param text 内容
     * @return
     */
    public static List<String> getSensitiveWord(String text) {
        List<String> words = new ArrayList<String>();
        Map now = sensitiveWordMap;
        int count = 0;  //初始化敏感词长度
        int start = 0; //标志敏感词开始的下标
        boolean hasNext = false;  // 敏感词组是否有下一个
        for (int i = 0; i < text.length(); i++) {
            char key = text.charAt(i);
            now = (Map) now.get(key);
            if (now != null) { //存在
                count++;
                if (count == 1) {
                    start = i;
                }
                if ("1".equals(now.get("isEnd"))) { //敏感词结束
                    if ("0".equals(now.get("hasNext")) || i == text.length()-1) {
                        now = sensitiveWordMap; //重新获取敏感词库
                        words.add(text.substring(start, start + count)); //取出敏感词，添加到集合
                        count = 0; //初始化敏感词长度
                        hasNext = false;
                    } else {
                        hasNext = true;
                    }
                }
            } else { //不存在
                if (hasNext) {
                    words.add(text.substring(start, start + count));
                }
                now = sensitiveWordMap;//重新获取敏感词库
                count = 0;
                hasNext = false;
            }
        }
        return words;
    }

    /**
     * 暂时未使用
     * 从文件中读取敏感词库中的内容，将内容添加到set集合中
     *
     * @return
     * @throws Exception
     * @author
     * @date
     * @version 1.0
     */
    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
        File file = new File("F:\\SPF\\项目\\敏感词过滤\\敏感词汇\\敏感词库\\敏感词2\\SensitiveWord.txt");    //读取文件
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);
        try {
            if (file.isFile() && file.exists()) {      //文件流是否存在
                set = new HashSet<String>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while ((txt = bufferedReader.readLine()) != null) {    //读取文件，将文件内容放入到set中
                    set.add(txt);
                }
            } else {         //不存在抛出异常信息
                throw new Exception("敏感词库文件不存在");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            read.close();     //关闭文件流
        }
        return set;
    }

}
