package com.guhecloud.cube.biz.ouaa.client.dto;

import lombok.Data;

/**
 * 应用端详情
 *
 * @author appet
 * @date 2021/10/13 19:28
 */
@Data
public class TerminalDTO {

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
     * 微信ID
     */
    private String appId;

    /**
     * 微信密钥
     */
    private String appSecret;
    /**
     * 访问端备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
