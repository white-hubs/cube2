package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.dto;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 联动物联设备新增配置
 *
 * @author lanchun
 * @date 2022/6/21 17:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LinkageIotDeviceProDTO {

    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 设备产品品类名称
     */
    private String deviceProdCategoryName;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;

    /**
     * 设备主键
     */
    private Long id;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 执行方式
     */
    private ExecuteModeEnum executeMode;

    /**
     * 属性值
     */
    private String propValue;
}
