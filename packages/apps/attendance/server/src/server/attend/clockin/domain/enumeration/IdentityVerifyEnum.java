package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2023-10-11  19:34
 * @Description: 人脸失败状态码
 * @Version: 1.0
 */
@Getter
public enum IdentityVerifyEnum {
    /**
     * 开河平台响应码
     */
    SUCCESS(1000, "成功"),
    INNER_EXCEPTION(1501, "内部服务异常"),
    FACE_NO_DETECTED(1502, "未探测到人脸"),
    FACE_DB_COMPARE_FAILED(1503, "人脸识别失败"),
    FACE_DETECT_TIMEOUT(2000, "人脸探测超时"),
    FACE_DETECT_BUSY(2010, "人脸探测繁忙"),
    FAIL(408, "人脸探测请求超时"),
    CONFIG_FAILED(-1, "未配置人脸识别服务"),
    WNKNOWN(-2, "未知异常"),
    // 度目响应码
    OK(1, "成功"),

    ;
    private final Integer code;

    private final String remarks;

    IdentityVerifyEnum(Integer code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }

    public static String getRemarksByResult(String result) {
        String remarks = "";
        if (ObjectUtils.isEmpty(result)) {
            return remarks;
        }
        IdentityVerifyEnum[] verifyEnums = IdentityVerifyEnum.values();
        for (IdentityVerifyEnum ex : verifyEnums) {
            if (result.contains(ex.getCode().toString())) {
                remarks = ex.getRemarks();
                break;
            }
        }
        return remarks;
    }

    public static IdentityVerifyEnum getIdentityVerifyEnum(String result) {
        IdentityVerifyEnum identityVerifyEnum = WNKNOWN;
        if (ObjectUtils.isEmpty(result)) {
            return identityVerifyEnum;
        }
        IdentityVerifyEnum[] verifyEnums = IdentityVerifyEnum.values();
        for (IdentityVerifyEnum ex : verifyEnums) {
            if (result.contains(ex.getCode().toString())) {
                identityVerifyEnum = ex;
                break;
            }
        }
        return identityVerifyEnum;
    }
}
