package com.guhecloud.cube.property.management.client.property.param.property;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  09:54
 * @Description:
 * @Version: 1.0
 */
@Data
public class PropertyBimConfigParam {
    /**
     * 场景类型
     */
    private String sceneType;
    /**
     * 业务主键
     */
    private List<String> businessIds;
    /**
     * 绑定方式
     */
    private String bindWay;
    /**
     * 资产类别
     */
    private String typeId;
    /**
     * 资产名称
     */
    private String keyword;
}
