package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * 应用端详情
 *
 * @author lanchun
 * @date 2021/10/13 19:28
 */
@Data
public class TerminalVO {

    private Long id;

    /**
     * 产品主键
     */
    private Long productId;

    /**
     * 访问端编号
     */
    private String code;

    /**
     * 访问端名称
     */
    private String name;

    /**
     * 访问端备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 微信ID
     */
    private String appId;

    /**
     * 微信密钥
     */
    private String appSecret;

    /**
     * 别名配置
     */
    private String alias;
}
