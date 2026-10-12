package com.guhecloud.cube.biz.work.center.client.param;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 批量任务统计参数
 */
@Data
public class WorkHomePageBatchParam {

    /**
     * 类型
     */
    private String type;

    /**
     * 业务类型集合
     */
    private Set<String> businessTypes;

    /**
     * 日期
     */
    private String period;

    /**
     * 分组id与人员id集合的映射
     */
    private Map<Long, List<Long>> userIdMap;
}
