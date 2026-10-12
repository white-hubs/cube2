package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/6 10:40
 * @Description: 新增巡检任务参数
 */

@Data
public class InspectTaskParam {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 巡检类型ID
     */
    private Long typeId;

    /**
     * 巡检类型名
     */
    private String typeName;

    /**
     * 执行状态：执行中 已结束 已停用
     */
    private InspectTaskStatusEnum status;

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

    /**
     * 接收岗位
     */
    private String receivePostIds;

    /**
     * 接收角色
     */
    private String receiveRoleIds;

    /**
     * 接收对象ID
     */
    private String receiveIds;

    /**
     * 生效开始时间
     */
    private LocalDateTime validBeginTime;

    /**
     * 生效结束时间
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
     * 备注
     */
    private String remark;
    /**
     * 稽查任务关联的任务id
     */
    private Long checkTaskId;
    /**
     * 子任务关联的父任务id
     */
    private Long parentId;
    /**
     * 是否需要拆解
     */
    private Boolean isSplit = false;
    /**
     * 任务级别
     */
    private String level;
    /**
     * 协同人
     */
    private String assistIds;

    /**
     * 对象ids
     */
    private String targetIds;

    /**
     * 是否需要稽查
     */
    private Boolean isCheck = false;

    /**
     * 稽查人员类型1个人，2部门
     */
    private String checkUserType;

    /**
     * 稽查部门
     */
    private String checkDeptIds;

    /**
     * 稽查岗位
     */
    private String checkPostIds;

    /**
     * 稽查角色
     */
    private String checkRoleIds;

    /**
     * 稽查人ids
     */
    private String checkUserIds;

    /**
     * 扫描方式
     */
    private String method;

    /**
     * 巡检任务关联的目标对象
     */
    @NotEmpty(message = "巡检标准和巡检项不能为空！")
    List<PlanRelationPO> taskTargetDTOList;
}
