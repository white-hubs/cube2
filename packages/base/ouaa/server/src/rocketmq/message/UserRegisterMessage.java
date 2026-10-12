package com.guhecloud.cube.biz.ouaa.server.rocketmq.message;

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
public class UserRegisterMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "user-register";

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 移动电话
     */
    private String mobile;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 时间戳
     */
    @Builder.Default
    private final Long timestamp = System.currentTimeMillis();
}
