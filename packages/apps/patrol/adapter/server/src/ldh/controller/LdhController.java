package com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.controller;

import com.guhecloud.cube.srv.gen.adapter.patrol.server.ldh.service.impl.LDHPatrolServiceImpl;
import com.guhecloud.subsystem.requester.sdk.param.patrol.PatrolParam;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolAlarmVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolDeviceVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolLineVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPlanVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPointVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRouteVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 兰德华电子巡更系统对接
 */
@Slf4j
@RestController
@RequestMapping("/ldh")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LdhController {
    private final LDHPatrolServiceImpl patrolService;

    /**
     * 巡更计划
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-plan")
    public List<PatrolPlanVO> getPatrolPlan(@RequestBody PatrolParam param) {
        //与项目经理讨论后 暂时不查对面的巡检计划，巡检计划改为查询考核表
        return this.patrolService.getPatrolPlanEvaluation(param);
    }

    /**
     * 巡更线路
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-line")
    public List<PatrolLineVO> getPatrolLine(@RequestBody PatrolParam param) {
        return this.patrolService.getPatrolLine(param);
    }

    /**
     * 巡检点
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-point")
    public List<PatrolPointVO> getPatrolPoint(@RequestBody PatrolParam param) {
        return this.patrolService.getPatrolPoint(param);
    }

    /**
     * 巡更线路与巡更点的关联关系
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-route")
    public List<PatrolRouteVO> getPatrolRoute(@RequestBody PatrolParam param) {
        return this.patrolService.getPatrolRoute(param);
    }

    /**
     * 巡更报警
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-alarm")
    public List<PatrolAlarmVO> getPatrolAlarm(@RequestBody PatrolParam param) {
        return this.patrolService.getPatrolAlarm(param);
    }

    /**
     * 巡更设备
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-device")
    public List<PatrolDeviceVO> getPatrolDevice(@RequestBody PatrolParam param) {
        return this.patrolService.getPatrolDevice(param);
    }

    /**
     * 巡更记录
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-record")
    public List<PatrolRecordVO> getPatrolRecord(@RequestBody PatrolParam param) {
        return this.patrolService.getPatrolRecord(param);
    }
}
