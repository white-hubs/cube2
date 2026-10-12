package com.guhecloud.cube.biz.ouaa.metadata.enumeration;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/15 11:35
 * @Description:
 */

public enum OuaaFileSceneCodeEnum {

    OUAA_TENANT("OUAA::TENANT", "企业管理_租户信息"),
    OUAA_USER("OUAA::USER", "企业管理_用户信息"),
    OUAA_APP_CARD("OUAA::APP_CARD", "企业管理_应用卡片"),
    OUAA_APP_VERSION("OUAA::APP_VERSION", "企业管理_APP版本"),
    ;
    private final String code;
    private final String remarks;

    OuaaFileSceneCodeEnum(String code, String remarks) {
        this.remarks = remarks;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getRemarks() {
        return remarks;
    }
}
