package com.guhecloud.cube.srv.gen.maintain.server.electron.param.task;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolConditionParticipateParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan.PatrolPlanTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatrolTaskParam {
    /**
     * 巡更任务
     */
    private String taskName;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 巡更路线
     */
    private Long routeId;
    /**
     * 巡更点
     */
    private List<PatrolLocationVO> locationIds;
    private List<Long> locationIdList;

    /**
     * 是否按照顺序巡更
     */
    private Boolean inOrder;
    /**
     * 巡更人员
     */
    private List<UserDTO> userIds;
    /**
     * 是否只指派给当班人员
     */
    private Boolean onDuty;
    /**
     * 任务开始日期
     */
    private LocalDateTime startDate;
    private String createBy;
    private Integer tenant;
    /**
     * 巡更方式
     */
    private List<String> patrolWayList;
    /**
     * 任务时段
     * 因为前端跟计划用的同一个页面所以他入参也是集合，只不过任务这里一定只有一个。(计划会有多个
     */
    private List<PatrolPlanTaskParam> taskList;
    /**
     * 巡更参与对象
     */
    private PatrolConditionParticipateParam conditionParticipate;
}
