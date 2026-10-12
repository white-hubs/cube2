package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossNodeMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossNode;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossNodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 线损节点
 * 节点和线路在LineLossSupplyServiceImpl类中聚合，这里仅提供基础能力
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossNodeServiceImpl extends ServiceImpl<LineLossNodeMapper, LineLossNode> implements LineLossNodeService {
}
