package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayRecordPO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastPlayRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class BroadcastPlayRecordService extends ServiceImpl<BroadcastPlayRecordMapper, BroadcastPlayRecordPO> {
}
