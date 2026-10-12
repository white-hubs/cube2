package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileNotice;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileNoticeModifyParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileNoticeVO;

public interface EmergencyFileNoticeService extends IService<EmergencyFileNotice> {

    Long save(EmergencyFileNoticeModifyParam param);

    EmergencyFileNoticeVO getEmergencyFileNotice();

    EmergencyFileNotice getBySceneCode(String sceneCode);

}
