package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据脱敏工具
 *
 * @author bq
 * @since 2022/04/27 17:30
 */
public class DataDesensitizedUtils {

    /**
     * 功能描述：姓名脱敏
     * 脱敏规则：只显示第一个汉字,比如李某某置换为李**, 李某置换为李*
     * @param fullName 完整的姓名
     * @return
     */
    public static String desensitizedName(String fullName) {
        if (StringUtils.isNotBlank(fullName)) {
            String name = StringUtils.left(fullName, 1);
            return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
        }
        return fullName;
    }

    /**
     * 功能描述：手机号脱敏
     * 脱敏规则：保留前三后四, 比如15085375241置换为150****5241
     * @param phoneNumber 手机号
     * @return
     */
    public static String desensitizedPhoneNumber(String phoneNumber) {
        if (StringUtils.isNotBlank(phoneNumber)) {
            phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }
        return phoneNumber;
    }

    /**
     * 功能描述：身份证号脱敏
     * 脱敏规则：保留前六后三, 适用于15位和18位身份证号
     * @param idNumber 身份证号
     * @return
     */
    public static String desensitizedIdNumber(String idNumber) {
        if (idNumber.length() == 15 || idNumber.length() == 18) {
            if (StringUtils.isNotBlank(idNumber)) {
                return StringUtils.left(idNumber, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idNumber, 3), StringUtils.length(idNumber), "*"), "******"));
            }
        }
        return idNumber;
    }

    /**
     * 功能描述：地址脱敏
     * 脱敏规则：从第4位开始隐藏,隐藏8位
     *         因地址位数是不确定的,所以结尾长度为总长度减去 前面保留长度和隐藏长度之和 address.length()-11
     * @param address 具体地址
     * @return
     */
    public static String desensitizedAddress(String address) {
        if (StringUtils.isNotBlank(address)) {
            return StringUtils.left(address, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(address, address.length() - 11), StringUtils.length(address), "*"), "***"));
        }
        return address;
    }

}
