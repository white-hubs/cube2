package com.guhecloud.platform.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 认证主体信息
 *
 * @author Woody
 * @date 2021/6/23
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Principal implements Serializable {
    /**
     * 主体标识
     */
    private Long id;

    /**
     * 主体类型
     */
    private Type type;

    /**
     * 是否是超级管理员
     */
    private Boolean admin;

    /**
     * 用户名
     */
    private String name;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 主体类型枚举
     */
    public enum Type {
        /**
         * 用户主体
         */
        USER,

        /**
         * 客户端主体
         */
        CLIENT;
    }
}
