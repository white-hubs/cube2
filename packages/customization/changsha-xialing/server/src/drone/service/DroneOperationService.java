package com.guhecloud.cube.customization.xialing.server.drone.service;

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

/**
 * 无人机操作服务
 */
public interface DroneOperationService {
    Boolean setDroneControlPermission(DroneControlPermissionParam param);

    Boolean powerOn(DroneNestCommandParam param);

    Boolean flyAction(DroneFlyActionParam param);

    Boolean takeOff(DroneTakeOffParam param);

    Boolean pointFlightMore(DronePointFlightMoreParam param);

    Boolean pointFlight(DronePointFlightParam param);

    Boolean precisionReturn(DroneNestCommandParam param);

    Boolean setReturnAltitude(DroneReturnAltitudeParam param);

    Boolean setAlternatePoint(DroneAlternatePointParam param);

    Boolean cancelAlternatePointProtection(DroneNestCommandParam param);

    Boolean hover(DroneNestCommandParam param);

    Boolean targetZoom(DroneTargetZoomParam param);

    Boolean setHeading(DroneHeadingParam param);

    Boolean setGimbalAngle(DroneGimbalAngleParam param);

    Boolean setCameraZoom(DroneCameraZoomParam param);

    /**
     * 暂无三方协议，不对外使用。示例URL：http://{{ipport}}/prod-api/nest/drone/cmd/land/{sn}
     */
    Boolean land(DroneNestCommandParam param);

    /**
     * 暂无三方协议，不对外使用。示例URL：http://{{ipport}}/prod-api/nest/drone/cmd/return/cancel/{sn}
     */
    Boolean cancelReturn(DroneNestCommandParam param);

    /**
     * 暂无三方协议，不对外使用。示例URL：http://{{ipport}}/prod-api/nest/drone/cmd/boot/close/{sn}
     */
    Boolean powerOff(DroneNestCommandParam param);
}
