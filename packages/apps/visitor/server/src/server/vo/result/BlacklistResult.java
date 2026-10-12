package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

/**
 * 黑名单
 */
@Data
public class BlacklistResult {
    private Long id;
    /**
     * 视频盒子的人员id
     */
    private Long boxUserId;
    private String phone;
    private String deviceCode;
    private String name;
    private String reason;
    private String sex;
    private String pictures; // 图片ids
}
