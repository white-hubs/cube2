package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionNotifyRecord;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotifySceneCodeEnum;

import java.util.List;

public interface DisinfectionNotifyRecordService extends IService<DisinfectionNotifyRecord> {

    void batchSaveRecord(List<DisinfectionNotifyRecord> notifyRecords);

    List<Long> getNotifiedTaskIdsBySceneCode(DisinfectionNotifySceneCodeEnum sceneCodeEnum);

    List<DisinfectionNotifyRecord> listBySceneCode(DisinfectionNotifySceneCodeEnum sceneCodeEnum);

    List<Long> getUserNotifiedTaskIdsByUserId(Long userId);
}
