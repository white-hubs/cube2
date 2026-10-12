package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollectCamera;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectCameraModifyParam;

import java.util.List;
import java.util.Set;

public interface MultipleVideoCollectCameraService extends IService<MultipleVideoCollectCamera> {

    void save(Long multipleVideoCollectId, List<MultipleVideoCollectCameraModifyParam> params);

    void save(MultipleVideoCollectCameraModifyParam param);

    List<MultipleVideoCollectCamera> listMultipleVideoCollectCameras(Long multipleVideoCollectId);

    List<MultipleVideoCollectCamera> listUserMultipleVideosBySource(Long userId, String source);

    void removeByMultipleVideoCollectId(Long multipleVideoCollectId);

    void removeBySource(Long userId, String source, Long deviceId);

    void removeByUser(Long userId, Long deviceId);

    void removeByDeviceIds(Set<Long> deviceIds);

    void removeByUserIds(Set<Long> userIds);
}