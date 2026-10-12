package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao.ElectricityNodeMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.ElectricityNode;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.ElectricityNodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElectricityNodeServiceImpl extends ServiceImpl<ElectricityNodeMapper, ElectricityNode> implements ElectricityNodeService {
}
