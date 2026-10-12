package com.guhecloud.cube.customization.xialing.server.drone.controller;

import com.guhecloud.cube.customization.xialing.server.drone.service.DroneOperationService;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneAlternatePointParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneCameraZoomParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneControlPermissionParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneFlyActionParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneGimbalAngleParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneHeadingParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneNestCommandParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DronePointFlightMoreParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DronePointFlightParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneReturnAltitudeParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTakeOffParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTargetZoomParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 无人机操作控制器
 * <p>
 * 提供霞凝无人机实时控制指令接口。
 * </p>
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DroneOperationController {
    private final DroneOperationService droneOperationService;

    /**
     * 设置飞机控制权限。
     */
    @PostMapping("/drone/operations/control-permission")
    public Boolean setDroneControlPermission(@RequestBody DroneControlPermissionParam param) {
        return droneOperationService.setDroneControlPermission(param);
    }

    /**
     * 飞机开机。
     */
    @PostMapping("/drone/operations/power-on")
    public Boolean powerOn(@RequestBody DroneNestCommandParam param) {
        return droneOperationService.powerOn(param);
    }

    /**
     * 飞行操控。
     */
    @PostMapping("/drone/operations/fly-action")
    public Boolean flyAction(@RequestBody DroneFlyActionParam param) {
        return droneOperationService.flyAction(param);
    }

    /**
     * 一键起飞，scheduleId 由前端传入。
     */
    @PostMapping("/drone/operations/take-off")
    public Boolean takeOff(@RequestBody DroneTakeOffParam param) {
        return droneOperationService.takeOff(param);
    }

    /**
     * 紧急悬停。
     */
    @PostMapping("/drone/operations/hover")
    public Boolean hover(@RequestBody DroneNestCommandParam param) {
        return droneOperationService.hover(param);
    }

    /**
     * 精准返航。
     */
    @PostMapping("/drone/operations/return/precision")
    public Boolean precisionReturn(@RequestBody DroneNestCommandParam param) {
        return droneOperationService.precisionReturn(param);
    }

    /**
     * 指点飞行新接口。
     */
    @PostMapping("/drone/operations/point-flight/more")
    public Boolean pointFlightMore(@RequestBody DronePointFlightMoreParam param) {
        return droneOperationService.pointFlightMore(param);
    }

    /**
     * 指点飞行旧接口。
     */
    @PostMapping("/drone/operations/point-flight")
    public Boolean pointFlight(@RequestBody DronePointFlightParam param) {
        return droneOperationService.pointFlight(param);
    }

    /**
     * 返航高度设置。
     */
    @PostMapping("/drone/operations/return-altitude")
    public Boolean setReturnAltitude(@RequestBody DroneReturnAltitudeParam param) {
        return droneOperationService.setReturnAltitude(param);
    }

    /**
     * 备降点设置。
     */
    @PostMapping("/drone/operations/alternate-point")
    public Boolean setAlternatePoint(@RequestBody DroneAlternatePointParam param) {
        return droneOperationService.setAlternatePoint(param);
    }

    /**
     * 取消备降点保护。
     */
    @PostMapping("/drone/operations/alternate-point/cancel-protection")
    public Boolean cancelAlternatePointProtection(@RequestBody DroneNestCommandParam param) {
        return droneOperationService.cancelAlternatePointProtection(param);
    }

    /**
     * 拉框放大。
     */
    @PostMapping("/drone/operations/target-zoom")
    public Boolean targetZoom(@RequestBody DroneTargetZoomParam param) {
        return droneOperationService.targetZoom(param);
    }

    /**
     * 飞机航向角设置。
     */
    @PostMapping("/drone/operations/heading")
    public Boolean setHeading(@RequestBody DroneHeadingParam param) {
        return droneOperationService.setHeading(param);
    }

    /**
     * 云台俯仰方位角设置。
     */
    @PostMapping("/drone/operations/gimbal-angle")
    public Boolean setGimbalAngle(@RequestBody DroneGimbalAngleParam param) {
        return droneOperationService.setGimbalAngle(param);
    }

    /**
     * 相机变倍设置。
     */
    @PostMapping("/drone/operations/camera-zoom")
    public Boolean setCameraZoom(@RequestBody DroneCameraZoomParam param) {
        return droneOperationService.setCameraZoom(param);
    }
}
