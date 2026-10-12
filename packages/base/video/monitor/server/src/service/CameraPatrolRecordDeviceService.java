package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecordDevice;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordDeviceModifyParam;

import java.util.List;

public interface CameraPatrolRecordDeviceService extends IService<CameraPatrolRecordDevice> {

    void save(Long cameraPatrolRecordId, List<CameraPatrolRecordDeviceModifyParam> params);

    CameraPatrolRecordDevice getOne(Long cameraPatrolRecordId, Long deviceId);

    List<CameraPatrolRecordDevice> listCameraPatrolRecordDevices(Long cameraPatrolRecordId);

    void removeByCameraPatrolRecordId(Long cameraPatrolRecordId);
}