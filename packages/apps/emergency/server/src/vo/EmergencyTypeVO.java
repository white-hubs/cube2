package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.Data;

/**
 * 预案类型返回参数
 *
 * @author lanchun
 * @date 2022/9/2 14:08
 */
@Data
public class EmergencyTypeVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;
}
