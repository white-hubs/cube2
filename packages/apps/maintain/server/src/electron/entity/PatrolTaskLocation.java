package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 巡更任务巡检点
 * @date 2023/5/29 10:57
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("patrol_task_location")
public class PatrolTaskLocation extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 巡更点id
     */
    private Long locationId;
    /**
     * 巡更点顺序
     */
    private Long orderNum;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 类型(计划,任务)
     */
    private String addType;
    /**
     * 巡更结果
     */
    private String result;
    /**
     * 实际巡更顺序
     */
    private Long patrolOrder;
    /**
     * 是否上报隐患
     */
    private Boolean hiddenNum;
    /**
     * 巡更点负责人
     */
    private Long superintendentUserId;
    /**
     * 巡更人(实际执行时的提交人员）
     */
    private Long userId;
    /**
     * 隐患单id
     */
    private String hiddenId;
    /**
     * 巡更时间
     */
    private LocalDateTime patrolDate;
    /**
     * 任务开始时间
     */
    private LocalDateTime startDate;
    /**
     * 任务结束时间
     */
    private LocalDateTime endDate;
    /**
     * 说明
     */
    private String remark;
    /**
     * 异常类型
     */
    private String exceptionType;
    /**
     * 隐患是否处理完成
     */
    private Boolean hiddenIsDone;
    /**
     * 所属任务时间段
     */
    private String executePeriod;
}
