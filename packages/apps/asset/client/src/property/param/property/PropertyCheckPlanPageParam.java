package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class PropertyCheckPlanPageParam extends Paging {
    /**
     * 盘点计划id
     */
    private Long id;
    /**
     * 状态
     */
    private String status;
    /**
     * 盘点任务名称
     */
    private String taskName;
    private String keyword;
    private String taskId;

    private List<Long> taskIdList;
    /**
     * 资产id(逗号拼接)
     */
    private String propertyId;

}
