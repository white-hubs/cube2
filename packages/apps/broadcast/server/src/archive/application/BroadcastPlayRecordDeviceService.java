package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayRecordDevicePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastPlayRecordDeviceMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class BroadcastPlayRecordDeviceService extends ServiceImpl<BroadcastPlayRecordDeviceMapper, BroadcastPlayRecordDevicePO> {

    public List<BroadcastPlayRecordDevicePO> getByRecordIds(Collection<Long> recordIds) {
        if (CollectionUtils.isEmpty(recordIds)) {
            return Collections.emptyList();
        }
        return this.baseMapper.selectList(Wrappers.<BroadcastPlayRecordDevicePO>lambdaQuery()
                                                  .in(BroadcastPlayRecordDevicePO::getBroadcastPlayRecordId, recordIds)
                                         );
    }
}
