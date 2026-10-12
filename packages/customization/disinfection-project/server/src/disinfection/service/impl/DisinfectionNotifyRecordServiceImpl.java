package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao.DisinfectionNotifyRecordMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionNotifyRecord;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotifySceneCodeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionNotifyRecordService;
import com.guhecloud.platform.base.util.ListUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DisinfectionNotifyRecordServiceImpl extends ServiceImpl<DisinfectionNotifyRecordMapper, DisinfectionNotifyRecord> implements DisinfectionNotifyRecordService {
    @Override
    public void batchSaveRecord(List<DisinfectionNotifyRecord> notifyRecords) {
        if (ListUtils.isEmpty(notifyRecords)) {
            return;
        }
        saveBatch(notifyRecords);
    }

    @Override
    public List<Long> getNotifiedTaskIdsBySceneCode(DisinfectionNotifySceneCodeEnum sceneCodeEnum) {

        return lambdaQuery().select(DisinfectionNotifyRecord::getBusinessId)
                .eq(Objects.nonNull(sceneCodeEnum), DisinfectionNotifyRecord::getSceneCode, sceneCodeEnum)
                .list()
                .stream()
                .map(DisinfectionNotifyRecord::getBusinessId)
                .collect(Collectors.toList());
    }

    @Override
    public List<DisinfectionNotifyRecord> listBySceneCode(DisinfectionNotifySceneCodeEnum sceneCodeEnum) {

        return lambdaQuery().eq(DisinfectionNotifyRecord::getSceneCode, sceneCodeEnum).list();
    }

    @Override
    public List<Long> getUserNotifiedTaskIdsByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return Collections.emptyList();
        }
        List<DisinfectionNotifyRecord> list = lambdaQuery().select(DisinfectionNotifyRecord::getBusinessId)
                .like(DisinfectionNotifyRecord::getNotifyUsers, userId).list();

        return ListUtils.convertItem(list, DisinfectionNotifyRecord::getBusinessId);
    }
}
