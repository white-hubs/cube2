package com.guhecloud.kaihe.scada.server.third.vo;

import lombok.Data;

/**
 * @program: gh-scada
 * @description: 设备属性对象
 * @author: hyq
 * @create: 2020-12-15 09:42
 **/
@Data
public class IotProductPropertyVo {

    private String deviceModel;//": "xfj_002",
    private String deviceCode;//": "1299639980857389058",
    private String propertyName;//": "温度",
    private String propertyId;//": "1299639533316763649",
    private String propertyCode;//": "temp",
    private String propertyValue;//": "25",
    private String propertyType;//": "int",
    private String propertyUnit;//": "摄氏度/℃",
    private String propertyConfig;//": "[0,\"50\"]",
    private String dataWrite;//": 1

}
