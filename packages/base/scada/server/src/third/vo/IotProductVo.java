package com.guhecloud.kaihe.scada.server.third.vo;

import lombok.Data;

/**
 * @program: gh-scada
 * @description: 项目列表
 * @author: hyq
 * @create: 2020-12-11 10:50
 **/
@Data
public class IotProductVo {

    /**
     * 产品ID
     */
    private String code;
    /**
     * 产品型号名称加产品名称
     */
    private String name;
    /**
     * 产品名称
     */
    private String value;
}
