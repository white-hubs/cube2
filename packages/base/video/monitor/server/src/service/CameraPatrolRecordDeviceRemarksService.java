package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecordDeviceRemarks;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordDeviceRemarkQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordDeviceRemarksModifyParam;

import java.util.List;

public interface CameraPatrolRecordDeviceRemarksService extends IService<CameraPatrolRecordDeviceRemarks> {

    void save(List<CameraPatrolRecordDeviceRemarksModifyParam> params);

    List<CameraPatrolRecordDeviceRemarks> listByCameraPatrolRecordId(Long cameraPatrolRecordId);

    Page<CameraPatrolRecordDeviceRemarks> page(CameraPatrolRecordDeviceRemarkQueryParam param);
}
