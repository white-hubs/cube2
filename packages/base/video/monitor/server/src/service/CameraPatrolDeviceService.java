package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolDevice;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolDeviceModifyParam;

import java.util.List;

public interface CameraPatrolDeviceService extends IService<CameraPatrolDevice> {

    void save(Long cameraPatrolLineId, List<CameraPatrolDeviceModifyParam> params);

    List<CameraPatrolDevice> listCameraPatrolDevices(Long cameraPatrolLineId);

    void removeByCameraPatrolLineId(Long cameraPatrolLineId);
}