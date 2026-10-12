package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.apps.linkage.metadata.dto.LinkageActionDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import lombok.Data;

import java.util.List;

/**
 * @author lrj
 * @Description 报警规则配置
 * @date 2021-12-30 15:12:36
 */
@Data
public class AlarmRulesVO {

    //
    private Long id;
    /**
     * 消息id
     */
    private Long messageId;
    //告警规则名称
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
     * 绑定设备数
     */
    private Long deviceNum;
    /**
     * 设备信息
     */
    private List<MeterDevice> meterDeviceList;

    /**
     * 触发级别名称
     */
    private String alarmLevelName;

    /**
     * 触发类型名称
     */
    private String alarmTypeName;

    /**
     * 动作列表
     */
    private List<LinkageActionDTO> emergencyActionVOS;
}