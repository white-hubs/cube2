package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.EventLinkageMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventLinkage;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventLinkageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventLinkageServiceImpl extends ServiceImpl<EventLinkageMapper, EventLinkage> implements EventLinkageService {
}
