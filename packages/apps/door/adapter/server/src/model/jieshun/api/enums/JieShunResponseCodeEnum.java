package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum JieShunResponseCodeEnum {
    /**
     * SUCCESS 成功
     * SYSTEM_ERROR 系统错误
     * ILLEGAL_ARGUMENT 请求参数不正确
     * ILLEGAL_ACCESS_ID 接入渠道编码不正确
     * ILLEGAL_SIGN_TYPE 签名类型不正确
     * ILLEGAL_SIGN 签名不正确
     */
    SUCCESS("0", "成功"),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统错误"),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "请求参数不正确"),
    ILLEGAL_ACCESS_ID("ILLEGAL_ACCESS_ID", "接入渠道编码不正确"),
    ILLEGAL_SIGN_TYPE("ILLEGAL_SIGN_TYPE", "签名类型不正确"),
    ILLEGAL_SIGN("ILLEGAL_SIGN", "签名不正确");
    private final String code;
    private final String message;

    @Nullable
    public static JieShunResponseCodeEnum build(String code) {
        return Arrays.stream(values())
                .filter(item -> item.code.equals(code))
                .findFirst()
                .orElse(null);
    }

    public boolean eql(String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        JieShunResponseCodeEnum jieShunResponseCodeEnum = build(code);
        if (Objects.isNull(jieShunResponseCodeEnum)) {
            return false;
        }
        return jieShunResponseCodeEnum.equals(this);
    }
}
