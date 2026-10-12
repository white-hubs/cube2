package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.EventMessageDelayMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventMessageDelay;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventMessageDelayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventMessageDelayServiceImpl extends ServiceImpl<EventMessageDelayMapper, EventMessageDelay> implements EventMessageDelayService {
}
