package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import java.time.LocalDateTime;
import java.util.Random;

public class BizCodeUtils {

    /**
     * 根据业务标识+年月日时分秒+4位随机数得到编码
     * @param prefix 编码前缀
     * @return
     */
    public static synchronized String generate(String prefix) {
        String random = String.format("%04d", new Random().nextInt(9999));
        return prefix + DateUtils.getDate(LocalDateTime.now(),"yyyyMMddHHmmss") + "_" + random;
    }

    /**
     * 根据业务标识+年月日时分秒得到编码
     * @param prefix 编码前缀
     * @return
     */
    public static synchronized String generateNoRandom(String prefix) {
//        String random = String.format("%04d", new Random().nextInt(9999));
        return prefix + DateUtils.getDate(LocalDateTime.now(),"yyyyMMddHHmmss");
    }

}
