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
 * @describe 巡更任务
 * @date 2023/5/29 10:03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("patrol_task")
public class PatrolTask extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 巡更任务名称
     */
    private String taskName;
    /**
     * 执行时间短 HH:mm:ss,HH:mm:ss
     */
    private String executeData;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 是否指派当班
     */
    private Boolean onDuty;
    /**
     * 是否按照顺序执行
     */
    private Boolean inOrder;
    /**
     * 任务执行人id
     */
    private Long userId;

    /**
     * 任务开始时间
     */
    private LocalDateTime startDate;
    /**
     * 任务结束时间
     */
    private LocalDateTime endDate;
    /**
     * 任务结果
     */
    private String result;
    /**
     * 任务归属人ids
     * (第一个人是任务实际负责人,因为任务接受后只有一个人，分发的人都是在他之后，
     * 移交也是替换的userId,位置不会改变)
     */
    private String belonger;
    /**
     * 巡更路线id
     */
    private Long routeId;
    /**
     * 巡更点数
     */
    private Integer locationNum;
    /**
     * 任务完成时间
     */
    private LocalDateTime completeDate;
    /**
     * 执行人姓名
     */
    private String userName;
    /**
     * 执行人部门id
     */
    private Long userDepId;
    /**
     * 巡更方式
     */
    private String patrolWay;
    /**
     * 确认状态
     */
    private Boolean affirmState;
    /**
     * 间隔时间的值
     */
    private Integer intervalNumber;
    /**
     * 间隔时间的单位
     */
    private Integer intervalUnit;
    /**
     * 拆分的实际执行时间段(jsonArray)
     */
    private String executePeriod;
    /**
     * 权限人员
     */
    private Long visibleUserId;
    /**
     * 权限人员
     */
    private Long visibleOrgId;

    /**
     * 协同人
     */
    private String assistIds;
    /**
     * 拆分的实际执行时间段包含日期(jsonArray)
     */
    private String executePeriodTime;
}
