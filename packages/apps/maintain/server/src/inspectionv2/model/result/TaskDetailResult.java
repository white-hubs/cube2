package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectLevelEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjVO;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 巡检任务详情(分页和详情)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskDetailResult extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 计划名
     */
    private String planName;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 巡检类型ID
     */
    private Long typeId;

    /**
     * 任务级别(紧急/普通
     */
    private InspectLevelEnum level;

    /**
     * 巡检类型名
     */
    private String typeName;

    /**
     * 是否稽查任务1是 0否
     */
    private Boolean isCheck;

    /**
     * 执行状态：执行中 已结束 已停用
     */
    private String status;

    /**
     * 发布人ID
     */
    private String publisherId;

    /**
     * 发布人名
     */
    private String publisherName;

    /**
     * json字符串
     */
    private String limitTimeJson;

    /**
     * 精确到分钟
     */
    private Integer limitTotalMinute;

    /**
     * 接收类型，1＝个人，2＝部门
     */
    private String receiveType;

    /**
     * 接收部门
     */
    private String receiveDeptIds;
    private String receiveDeptNames;

    /**
     * 接收岗位
     */
    private String receivePostIds;
    private String receivePostNames;

    /**
     * 接收角色
     */
    private String receiveRoleIds;
    private String receiveRoleNames;

    /**
     * 接收对象ID
     */
    private String receiveIds;
    private String receiveNames;

    /**
     * 协同人对象
     */
    private String assistIds;
    private String assistNames;

    /**
     * 有效截至时间
     */
    private LocalDateTime validEndTime;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 配置巡检目标对象数
     */
    private Integer targetNum;

    /**
     * 记录完成率
     */
    private double recordCompletionRate;

    /**
     * 任务耗时
     */
    private Long timeConsuming;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 计划对象集合
     */
    List<List<InspectTargetInfoDTO>> typeTargets;

    /**
     * 巡检对象跟踪
     */
    List<InspectionObjVO> inspectionObjs;
    /**
     * 巡检方式
     */
    private String method;

    /**
     * 异常数
     */
    private Integer errCount;
    /**
     * 异常项数量
     */
    private Integer abnormalItemCount = 0;
    /**
     * 已修复数
     */
    private Integer repairCount;
}
