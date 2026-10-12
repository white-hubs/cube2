package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  09:53
 * @Description:
 * @Version: 1.0
 */
@Data
public class PropertyBimConfigPageParam extends Paging {
    /**
     * 资产名称
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
     * 资产类别
     */
    private Long typeId;
    /**
     * 绑定类型
     */
    private String bindWay;
}
