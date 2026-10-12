package com.guhecloud.cube.base.flow.work.order.statistics.vo;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-20  15:39
 * @Description:
 * @Version: 1.0
 */
@Data
public class WordOrderStatisticsVO extends Paging {
    /**
     * 应用code
     */
    private String applicationCode;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 用户名称
     */
    private String keyword;
    /**
     * 部门id
     */
    private Long organizationId;
    /**
     * 用户id集合
     */
    private List<String> userIds;

    /**
     * 业务集合
     */
    private String businessTypes;

    /**
     * 业务集合
     */
    private List<String> businessTypeList;

    /**
     * 工单分组ID
     */
    private Long formGroupId;
    /**
     * 开始时间-结束时间
     */
    private Period period;
}
