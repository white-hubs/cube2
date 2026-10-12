package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @describe 巡检各市场任务排序
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketInspectionRecordQuery extends BaseQuery {
    /**
     * 查询临时任务(taskId为空)
     */
    private Boolean tempBool;
    /**
     * 巡检结果
     */
    private String result;
    /**
     * 巡检对象名称
     */
    private String targetName;

    /**
     * 状态
     */
    private String status;
    /**
     * 查询异常项
     */
    private Boolean errorTagBool;
    /**
     * 异常项集合
     */
    private List<String> errorTagList;
    /**
     * 逗号隔开的tags
     */
    private String errorTags;
    /**
     * 对象id
     */
    private Long targetId;
}
