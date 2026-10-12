package com.guhecloud.cube.srv.gen.adapter.patrol.server.service;

import com.guhecloud.subsystem.requester.sdk.param.patrol.PatrolParam;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolAlarmVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolDeviceVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolLineVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPlanVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPointVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRouteVO;

import java.util.List;

/**
 * 定义电子巡更须要实现的接口
 */
public interface PatrolService {
    /**
     * 获取巡更计划
     *
     * @return
     */
    List<PatrolPlanVO> getPatrolPlan(PatrolParam param);

    /**
     * 获取巡更线路
     *
     * @param param
     *
     * @return
     */
    List<PatrolLineVO> getPatrolLine(PatrolParam param);

    /**
     * 获取巡更点
     *
     * @param param
     *
     * @return
     */
    List<PatrolPointVO> getPatrolPoint(PatrolParam param);

    /**
     * 获取巡更线路和点的关联
     *
     * @param param
     *
     * @return
     */
    List<PatrolRouteVO> getPatrolRoute(PatrolParam param);

    /**
     * 获取巡更发现的异常（报警）
     *
     * @param param
     *
     * @return
     */
    List<PatrolAlarmVO> getPatrolAlarm(PatrolParam param);

    /**
     * 获取巡更设备
     *
     * @param param
     *
     * @return
     */
    List<PatrolDeviceVO> getPatrolDevice(PatrolParam param);

    /**
     * 获取巡更记录
     *
     * @param param
     *
     * @return
     */
    List<PatrolRecordVO> getPatrolRecord(PatrolParam param);
}
