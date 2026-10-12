package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolPlanTaskVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatrolPlanVO {
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 生效时间
     */
    private LocalDateTime startData;
    /**
     * 失效时间
     */
    private LocalDateTime endData;
    /**
     * 巡更路线
     */
    private Long routeId;
    private Long routeName;
    /**
     * 巡更周期
     */
    private String patrolCycle;
    /**
     * 巡更日期类型（周：week；月：month）
     */
    private String patrolDataType;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 巡更日期
     */
    private String patrolData;
    /**
     * 计划状态
     */
    private String status;
    /**
     * 巡更点集合
     */
    private List<PatrolLocationVO> locationList;
    /**
     * 巡更人员集合
     */
    private List<UserDTO> userList;
    /**
     * 是否按照顺序执行
     */
    private Boolean inOrder;
    /**
     * 是否指派当班
     */
    private Boolean onDuty;
    /**
     * 巡更方式
     */
    private List<String> patrolWayList;
    /**
     * 计划的任务
     */
    private List<PatrolPlanTaskVO> taskList;

    /**
     * 巡更参与对象
     */
    private PatrolConditionParticipateVO conditionParticipate;
}
