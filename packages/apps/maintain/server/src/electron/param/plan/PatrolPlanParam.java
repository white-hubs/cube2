package com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlan;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class PatrolPlanParam {
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 计划状态
     */
    private String status;
    /**
     * 计划生效时间
     */
    private LocalDateTime startData;
    /**
     * 计划结束时间
     */
    private LocalDateTime endData;
    /**
     * 巡更周期
     */
    private String patrolCycle;
    /**
     * 巡更日期类型（周：week；月：month）
     */
    private String patrolDataType;
    /**
     * 巡更日期
     */
    private String patrolData;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 巡更路线id
     */
    private Long routeId;
    /**
     * 是否指派当班
     */
    private Boolean onDuty;
    /**
     * 是否按照顺序执行
     */
    private Boolean inOrder;
    /**
     * 巡更点id
     */
    private List<PatrolPlanLocationParam> locationIds;
    /**
     * 巡更人员id
     */
    private List<UserDTO> userIds;
    /**
     * 巡更方式
     */
    private List<String> patrolWayList;

    /**
     * 多任务对象集合
     */
    private List<PatrolPlanTaskParam> taskList;

    /**
     * 巡更参与对象
     */
    private PatrolConditionParticipateParam conditionParticipate;

    public static PatrolPlan getPatrolPlan(PatrolPlanParam param, UserDTO user) {
        PatrolPlan patrolPlan = ConvertUtils.convert(PatrolPlan.class, param);
        patrolPlan.setConditionType(param.getConditionParticipate().getConditionType());
        patrolPlan.setExceptionUserIds(param.getConditionParticipate().getAbnormalUserList().stream()
                                               .map(u -> String.valueOf(u.getTargetId()))
                                               .collect(Collectors.joining(",")));
        patrolPlan.setPatrolWay(String.join(",", param.getPatrolWayList()));
        if (CollectionUtils.isNotEmpty(param.getUserIds())) {
            List<String> collect = new ArrayList<>();
            param.getUserIds().forEach(ex -> collect.add(String.valueOf(ex.getId())));
            patrolPlan.setUserId(String.join(",", collect));
        }
        patrolPlan.setVisibleUserId(user.getId());
        patrolPlan.setVisibleOrgId(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getId());
        patrolPlan.setDeleted(false);
        return patrolPlan;
    }
}
