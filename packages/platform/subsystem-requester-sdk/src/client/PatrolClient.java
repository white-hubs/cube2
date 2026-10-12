package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.patrol.PatrolParam;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolAlarmVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolDeviceVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolLineVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPlanVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolPointVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.patrol.PatrolRouteVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author appet
 * @date 2023/10/18 18:18
 */
@FeignClient(name = SubsystemTypeConstants.PATROL_SERVER, configuration = DynamicConfiguration.class)
public interface PatrolClient {

    /**
     * 获取巡更计划
     *
     * @return
     */
    @PostMapping("/patrol-plan")
    List<PatrolPlanVO> getPatrolPlan(@RequestBody PatrolParam param);

    /**
     * 获取巡更线路
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-line")
    List<PatrolLineVO> getPatrolLine(@RequestBody PatrolParam param);

    /**
     * 获取巡更点
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-point")
    List<PatrolPointVO> getPatrolPoint(@RequestBody PatrolParam param);

    /**
     * 获取巡更线路和点的关联
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-route")
    List<PatrolRouteVO> getPatrolRoute(@RequestBody PatrolParam param);

    /**
     * 获取巡更发现的异常（报警）
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-alarm")
    List<PatrolAlarmVO> getPatrolAlarm(@RequestBody PatrolParam param);

    /**
     * 获取巡更设备
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-device")
    List<PatrolDeviceVO> getPatrolDevice(@RequestBody PatrolParam param);

    /**
     * 获取巡更记录
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/patrol-record")
    List<PatrolRecordVO> getPatrolRecord(@RequestBody PatrolParam param);

}
