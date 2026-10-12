package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileNoticeObj;

import java.util.List;

public interface EmergencyFileNoticeObjService extends IService<EmergencyFileNoticeObj> {

    List<EmergencyFileNoticeObj> getEmergencyFileNoticeObjsBySceneCode(String sceneCode);

    void removeBySceneCode(String sceneCode);
}