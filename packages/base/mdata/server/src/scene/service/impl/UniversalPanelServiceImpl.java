package com.guhecloud.cube.biz.mdata.server.scene.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.scene.dao.UniversalPanelMapper;
import com.guhecloud.cube.biz.mdata.server.scene.entity.UniversalPanel;
import com.guhecloud.cube.biz.mdata.server.scene.service.UniversalPanelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UniversalPanelServiceImpl extends ServiceImpl<UniversalPanelMapper, UniversalPanel> implements UniversalPanelService {
}
