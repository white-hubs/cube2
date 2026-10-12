package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author lrj
 * @Description 报警规则配置
 * @date 2021-12-30 15:12:36
 */
@Data
public class AlarmRulesQueryParam extends Paging {

    //告警名称
    private String alarmName;
    /**
     * 是否启用
     */
    private Boolean enabled;
    //告警级别
    private String alarmLevel;
    //报警类型
    private String alarmType;
    //告警规则名称
    private String alarmRule;
    //数值
    private String alarmValue;
    /**
     * 消息id
     */
    private Long messageId;
    /**
     * 触发级别名称
     */
    private String alarmLevelName;

    /**
     * 触发类型名称
     */
    private String alarmTypeName;

}