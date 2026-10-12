package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectLevelEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.InspectTaskFieldFilter;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.InspectTaskFieldParamConvert;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.annotation.DataPermissionMoreField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.platform.database.enumeration.ConditionEnum;
import com.guhecloud.platform.database.enumeration.OpConditionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 巡检任务实体
 */
@Data
@TableName("inspect_task")
@EqualsAndHashCode(callSuper = true)
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", fields = {
                @DataPermissionMoreField(field = "receive_ids", condition = ConditionEnum.LIKE),
                @DataPermissionMoreField(field = "assist_ids", condition = ConditionEnum.LIKE)
        }, opCondition = OpConditionEnum.OR
        ),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", fields = {
                @DataPermissionMoreField(field = "receive_ids", condition = ConditionEnum.LIKE),
                @DataPermissionMoreField(field = "assist_ids", condition = ConditionEnum.LIKE)
        }, opCondition = OpConditionEnum.OR
                , fieldFilter = InspectTaskFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", fields = {
                @DataPermissionMoreField(field = "receive_ids", condition = ConditionEnum.LIKE),
                @DataPermissionMoreField(field = "assist_ids", condition = ConditionEnum.LIKE)
        }, opCondition = OpConditionEnum.OR
                , fieldParamConvert = InspectTaskFieldParamConvert.class
        )
}
)
public class InspectTaskPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 任务编码
     */
    private String code;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 父类计划Id
     */
    private Long parentId;

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
     * 巡检类型名
     */
    private String typeName;

    /**
     * 级别 紧急/普通
     */
    private InspectLevelEnum level;

    /**
     * 是否需要拆解
     */
    private Boolean isSplit = false;

    /**
     * 稽查任务id(父任务id)
     */
    private Long checkTaskId;

    /**
     * 执行状态：执行中 已结束 已停用
     */
    private InspectTaskStatusEnum status;

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
     * 协同人
     */
    private String assistIds;

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
     * 对象ids
     */
    private String targetIds;

    /**
     * 备注
     */
    private String remark;
    /**
     * 巡检方式
     */
    private String method;

    /**
     * 巡检结果
     */
    private String result;

    /**
     * 过期任务待办处理标识
     */
    private Boolean expireTodo;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 离线同步版本号。用于 App 离线缓存和提交冲突判断。
     */
    private Long revision;
}
