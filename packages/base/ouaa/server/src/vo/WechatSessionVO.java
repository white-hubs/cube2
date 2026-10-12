package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatSessionVO {

    /**
     * 时间戳
     */
    private final Long timestamp = System.currentTimeMillis();

    /**
     * 会话标识
     */
    private String key;

    /**
     * 会话过期时间（秒）
     */
    private Integer expiration;

}
