package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/** 平台账号密码登录信息。 */
@Data
public class WarehouseLoginInfoVO {
    /** 平台登录 access_token。 */
    private String token;
    /** refresh_token。 */
    private String refreshToken;
    /** token 类型。 */
    private String tokenType;
    /** token 有效期，单位秒。 */
    private Long expiresIn;
    /** 登录指定租户。 */
    private Integer tenant;
    /** 平台登录响应中的租户列表。 */
    private List<Object> tenants;
    /** 平台登录原始响应，便于前端按需读取扩展字段。 */
    private Map<String, Object> raw;
}
