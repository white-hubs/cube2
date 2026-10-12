package com.guhecloud.kaihe.scada.server.third.vo;

import lombok.Data;

/**
 * @program: gh-scada
 * @description: 产品型号对象
 * @author: hyq
 * @create: 2020-12-11 15:04
 **/
@Data
public class IotProductModelVo {
    /**
     * 产品型号ID
     */
    private String code;
    /**
     * 产品型号名称
     */
    private String name;
}
