package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTaskTargetItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskTargetItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskTargetItemPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTaskTargetItemCommandServiceImpl extends ServiceImpl<InspectTaskTargetItemMapper, InspectTaskTargetItemPO> implements InspectTaskTargetItemCommandService {
}
