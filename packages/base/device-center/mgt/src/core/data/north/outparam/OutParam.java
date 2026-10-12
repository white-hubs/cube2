package com.guhecloud.cube.iot.mgt.core.data.north.outparam;

import com.guhecloud.cube.iot.mgt.core.data.msg.Qos;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
public abstract class OutParam {
    //租户
    protected String tenant;

    public Qos qos() {
        return Qos.QOS0;
    }

    /**
     * 生成唯一标识
     *
     * @return 唯一标识(tenant.Md5值)，有租户id加上32位Hash组成
     */
    public String identity() {
        return tenant + "." + md5Hex(identityDo());
    }

    /**
     * 本地MD5实现，替代DigestUtil.md5Hex
     *
     * @param input 输入字符串
     *
     * @return MD5哈希的十六进制字符串
     */
    private String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不可用", e);
        }
    }

    /**
     * 返回用于计算唯一标识的字符串
     *
     * @return 用于计算唯一标识的字符串
     */
    protected abstract String identityDo();

}