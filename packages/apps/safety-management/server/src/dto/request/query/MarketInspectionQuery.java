package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 巡检各市场任务排序
 * @date 2023/9/20 16:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketInspectionQuery extends BaseQuery {
    /**
     * 任务状态
     */
    private String taskStatus;
    /**
     * 时间类型
     */
    private String type;
    /**
     * 是否异常
     */
    private Boolean abnormal;
    /**
     * 异常项查询true表示异常项不为空
     */
    private Boolean errorTagBool;
}
