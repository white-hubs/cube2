package com.guhecloud.cube.property.management.client.risk.source.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  13:54
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceBimConfigParam {
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
     * 风险源类别
     */
    private String typeId;
    private String typeCode;
    /**
     * 风险源名称
     */
    private String keyword;

}
