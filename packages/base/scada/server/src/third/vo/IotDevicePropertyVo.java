package com.guhecloud.kaihe.scada.server.third.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: guheyun
 * @description: 设备型号可控属性
 * @author: hyq
 * @create: 2020-04-21 15:05
 **/
@Data
public class IotDevicePropertyVo implements Serializable {
    private static final long serialVersion = 1L;
    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 属性编码,
     */
    private String propertyCode;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 属性Id
     */
    private String propertyId;

    /**
     * 属性描述    例如 温度控制<br/>
     * remark
     */
    private String propertyDesc;

    /**
     * 支持动作  例如 setVal<br/>
     * 属性是否支持控制: 0-只读, 1-读写
     */
    private String propertyAction;

    /**
     * 动作值
     */
    private String propertyValue;

    /**
     * 属性类型<br/>
     * int
     * float
     * double
     * boolean
     * string
     * enum
     */
    private String propertyType;

    /**
     * 属性配置信息<br/>
     * 例如:[{"value":"0","label":"冬季"},{"value":"1","label":"夏季"},{"value":"2","label":"通风"},{"value":"3","label":"除湿"},{"value":"4","label":"智能"}]
     */
    private String propertyConfig;

}
