package com.guhecloud.cube.base.alarm.client.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 报警记录查询参数
 *
 * @author lanchun
 * @date 2022/6/7 10:39
 */
@Data
public class AlarmRecordQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 报警级别主键
     */
    @Min(value = 1, message = "报警级别主键不能小于1")
    private Long alarmLevelId;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 报警类型主键
     */
    @Min(value = 1, message = "报警类型主键不能小于1")
    private Long alarmTypeId;
    /**
     * 报警类型编码
     */
    private String alarmTypeCode;
    /**
     * 报警事件
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String alarmEvent;

    /**
     * 报警事件名称
     */
    private String alarmEventName;

    /**
     * 报警对象类型主键
     */
    private String alarmTargetTypeId;

    /**
     * 报警事件分类主键
     */
    private String alarmEventTypeId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 报警区域
     */
    private List<String> areaIds;

    /**
     * 报警时间范围
     */
    private Period period;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 设备主键集合
     */
    private List<Long> deviceIds;

    /**
     * 区域
     */
    private String area;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 部门主键
     */
    private Long departmentId;

    /**
     * 是否查询用户负责的报警数
     */
    private Boolean userResponsible;

    /**
     * 设备标签集合
     */
    private Set<String> deviceTags;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;
    /**
     * 租户id
     */
    private Integer tenant;

    /**
     * 导出文件名称
     */
    private String fileName;

}
