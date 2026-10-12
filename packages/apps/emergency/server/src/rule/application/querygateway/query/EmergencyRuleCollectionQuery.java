package com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 预案规则集合查询参数
 *
 * @author lanchun
 * @date 2022/9/5 14:16
 */
@Data
public class EmergencyRuleCollectionQuery extends Paging {

    /**
     * 规则名称查询
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 报警级别主键
     */
    private Long alarmLevelId;

    /**
     * 预案类型主键
     */
    private Long emergencyTypeId;

    /**
     * 事件分类编号
     */
    private String emergencyEventTypeCode;

    /**
     * 应用编号
     */
    @Size(max = 100, message = "应用编号不能大于100")
    private String applicationCode;
}
