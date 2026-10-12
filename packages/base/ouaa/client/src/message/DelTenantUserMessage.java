package com.guhecloud.cube.biz.ouaa.client.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册消息
 *
 * @author Woody
 * @date 2021/9/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelTenantUserMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "tenant-user-del";

    /**
     * 主键
     */
    private Long id;

}
