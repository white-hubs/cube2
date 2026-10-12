package com.guhecloud.cube.property.management.client.risk.source.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  13:53
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceBimConfigPageParam extends Paging {
    /**
     * 风险源名称
     */
    private String keyword;
    /**
     * 区域id
     */
    private Long areaId;
    /**
     * 场景类型
     */
    private String sceneType;
    /**
     * 风险源类别
     */
    private String typeCode;
    /**
     * 绑定类型
     */
    private String bindWay;
}
