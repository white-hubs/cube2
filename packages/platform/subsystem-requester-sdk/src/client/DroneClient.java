package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneAlternatePointParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneAlarmDispatchConfigSaveParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneAlarmDispatchRecordPageParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneCameraZoomParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneControlPermissionParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneFlyActionParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneGimbalAngleParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneHeadingParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneNestCommandParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DronePointFlightMoreParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DronePointFlightParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneResultQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneReturnAltitudeParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneRouteCommandParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneRouteQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTakeOffParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTargetZoomParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskCreateParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskFinishParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTaskRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneResultVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneAlarmDispatchConfigVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneAlarmDispatchRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneRouteVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneTaskRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.drone.DroneTaskVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = SubsystemTypeConstants.DRONE_SERVER, configuration = DynamicConfiguration.class)
public interface DroneClient {

    @PostMapping("/drone/routes/page")
    Pagination<DroneRouteVO> pageRoutes(@RequestBody DroneRouteQueryParam param);

    @PostMapping("/drone/routes")
    Long createRoute(@RequestBody DroneRouteCommandParam param);

    @PutMapping("/drone/routes")
    Boolean updateRoute(@RequestBody DroneRouteCommandParam param);

    @DeleteMapping("/drone/routes/{routeId}")
    Boolean deleteRoute(@PathVariable("routeId") Long routeId);

    @PostMapping("/drone/tasks/page")
    Pagination<DroneTaskVO> pageTasks(@RequestBody DroneTaskQueryParam param);

    @GetMapping("/drone/tasks/abnormal")
    List<DroneTaskVO> listAbnormalTasks();

    @PostMapping("/drone/task-records/page")
    Pagination<DroneTaskRecordVO> pageTaskRecords(@RequestBody DroneTaskRecordQueryParam param);

    @PostMapping("/drone/tasks")
    List<DroneTaskRecordVO> createTask(@RequestBody DroneTaskCreateParam param);

    @DeleteMapping("/drone/tasks/{taskId}")
    Boolean deleteTask(@PathVariable("taskId") Long taskId);

    @PostMapping("/drone/tasks/finish")
    Boolean finishTask(@RequestBody DroneTaskFinishParam param);

    @PostMapping("/drone/results/page")
    Pagination<DroneResultVO> pageResults(@RequestBody DroneResultQueryParam param);

    /**
     * 获取当前租户唯一报警派飞配置。
     */
    @GetMapping("/drone/alarm-dispatch-config/current")
    DroneAlarmDispatchConfigVO currentAlarmDispatchConfig();

    /**
     * 保存当前租户唯一报警派飞配置。
     */
    @PutMapping("/drone/alarm-dispatch-config/current")
    Boolean saveCurrentAlarmDispatchConfig(@RequestBody DroneAlarmDispatchConfigSaveParam param);

    /**
     * 分页查询报警派飞日志。
     */
    @PostMapping("/drone/alarm-dispatch-record/page")
    Pagination<DroneAlarmDispatchRecordVO> pageAlarmDispatchRecords(@RequestBody DroneAlarmDispatchRecordPageParam param);

    /**
     * 查询报警派飞日志详情。
     */
    @GetMapping("/drone/alarm-dispatch-record/{id}")
    DroneAlarmDispatchRecordVO alarmDispatchRecordDetail(@PathVariable("id") Long id);

    @PostMapping("/drone/operations/control-permission")
    Boolean setDroneControlPermission(@RequestBody DroneControlPermissionParam param);

    @PostMapping("/drone/operations/power-on")
    Boolean powerOn(@RequestBody DroneNestCommandParam param);

    @PostMapping("/drone/operations/fly-action")
    Boolean flyAction(@RequestBody DroneFlyActionParam param);

    @PostMapping("/drone/operations/take-off")
    Boolean takeOff(@RequestBody DroneTakeOffParam param);

    @PostMapping("/drone/operations/point-flight/more")
    Boolean pointFlightMore(@RequestBody DronePointFlightMoreParam param);

    @PostMapping("/drone/operations/point-flight")
    Boolean pointFlight(@RequestBody DronePointFlightParam param);

    @PostMapping("/drone/operations/return/precision")
    Boolean precisionReturn(@RequestBody DroneNestCommandParam param);

    @PostMapping("/drone/operations/return-altitude")
    Boolean setReturnAltitude(@RequestBody DroneReturnAltitudeParam param);

    @PostMapping("/drone/operations/alternate-point")
    Boolean setAlternatePoint(@RequestBody DroneAlternatePointParam param);

    @PostMapping("/drone/operations/alternate-point/cancel-protection")
    Boolean cancelAlternatePointProtection(@RequestBody DroneNestCommandParam param);

    @PostMapping("/drone/operations/hover")
    Boolean hover(@RequestBody DroneNestCommandParam param);

    @PostMapping("/drone/operations/target-zoom")
    Boolean targetZoom(@RequestBody DroneTargetZoomParam param);

    @PostMapping("/drone/operations/heading")
    Boolean setHeading(@RequestBody DroneHeadingParam param);

    @PostMapping("/drone/operations/gimbal-angle")
    Boolean setGimbalAngle(@RequestBody DroneGimbalAngleParam param);

    @PostMapping("/drone/operations/camera-zoom")
    Boolean setCameraZoom(@RequestBody DroneCameraZoomParam param);
}
