package com.guhecloud.platform.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 客户端请求对象
 *
 * @author Woody
 * @date 2021/5/27
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Requester implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求uri
     */
    private String uri;

    /**
     * 客户端地址
     */
    private String host;

    /**
     * 客户端请求域名
     */
    private String domain;

    /**
     * 认证头
     */
    private String authorization;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 访问端编号
     */
    private String terminal;

    /**
     * 访问端版本号
     */
    private String version;

    /**
     * 签名信息
     */
    private String signature;

    /**
     * 数据权限
     */
    private String permission;

}
