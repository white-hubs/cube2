package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolPerson;
import com.guhecloud.cube.biz.vmonitor.server.param.auth.CameraPatrolAuthParam;

import java.util.List;

public interface CameraPatrolPersonService extends IService<CameraPatrolPerson> {

    void save(Long cameraPatrolLineId, List<CameraPatrolAuthParam> personModifyParams);

    List<CameraPatrolPerson> listCameraPatrolPersons(Long cameraPatrolLineId);

    void removeByCameraPatrolLineId(Long cameraPatrolLineId);
}
