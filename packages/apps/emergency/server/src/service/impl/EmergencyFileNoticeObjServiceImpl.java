package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyFileNoticeObjMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileNoticeObj;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileNoticeObjService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileNoticeObjServiceImpl extends ServiceImpl<EmergencyFileNoticeObjMapper, EmergencyFileNoticeObj> implements EmergencyFileNoticeObjService {

    @Override
    public List<EmergencyFileNoticeObj> getEmergencyFileNoticeObjsBySceneCode(@NonNull String sceneCode) {
        return this.baseMapper.selectList(Wrappers.<EmergencyFileNoticeObj>lambdaQuery()
                                                  .eq(EmergencyFileNoticeObj::getSceneCode, sceneCode)
                                         );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBySceneCode(@NonNull String sceneCode) {
        List<EmergencyFileNoticeObj> emergencyFileNoticeObjsBySceneCode = this.getEmergencyFileNoticeObjsBySceneCode(sceneCode);
        if (!CollectionUtils.isEmpty(emergencyFileNoticeObjsBySceneCode)) {
            DatabaseContextHolder.softDelete(this, emergencyFileNoticeObjsBySceneCode);
        }
    }
}
