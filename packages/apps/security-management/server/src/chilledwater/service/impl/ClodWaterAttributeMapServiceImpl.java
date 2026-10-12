package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.ClodWaterAttributeMapMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ClodWaterAttributeMap;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ClodWaterAttributeMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClodWaterAttributeMapServiceImpl extends ServiceImpl<ClodWaterAttributeMapMapper, ClodWaterAttributeMap> implements ClodWaterAttributeMapService {
}
