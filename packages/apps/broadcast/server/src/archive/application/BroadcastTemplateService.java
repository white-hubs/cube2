package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastTemplatePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastTemplateMapper;
import org.springframework.stereotype.Service;

@Service
public class BroadcastTemplateService extends ServiceImpl<BroadcastTemplateMapper, BroadcastTemplatePO> {
}
