package com.guhecloud.cube.biz.adapter.park.server.application.dahua.utils;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.biz.adapter.park.server.application.dahua.param.DaHuaParkingRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.application.dahua.result.DaHuaEventResult;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Utils {
    // 查找最后一个数字的位置
    public static int getLastDigitIndex(String input) {
        if (StringUtils.isNotBlank(input)) {
            for (int i = input.length() - 1; i >= 0; i--) {
                char ch = input.charAt(i);
                if (Character.isDigit(ch)) { // 判断是否是阿拉伯数字
                    return i; // 返回下标
                }
            }
        }
        return -1; // 如果没有找到，返回 -1
    }

    public static <T extends DaHuaParkingRecordParam> T requestBodyToParkingRecord(Integer tenant, String md5, int index,
                                                                                   String requestBody, Class<T> clazz
                                                                                  ) throws Exception {

        String key = md5.substring(index, index + 16);
        String resp = Utils.decrypt(requestBody, key);
        T result = JsonUtils.deserialize(resp, clazz);
        result.setTenant(tenant);
        return result;
    }

    public static String getSuccessResult() {
        DaHuaEventResult result = new DaHuaEventResult();
        result.setResCode("200");
        result.setResMsg("success");
        result.setData(JSON.parseObject("{isOpen:3}"));
        return JSON.toJSONString(result);
    }

    public static String decrypt(String encryptedText, String key) throws Exception {
        // 1. Base64解码
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

        // 2. 创建密钥规范
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

        // 3. 初始化Cipher
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // 4. 执行解密
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // 5. 返回字符串结果
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
