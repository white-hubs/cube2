package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlanLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolPlanLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolPlanLocationServiceImpl extends ServiceImpl<PatrolPlanLocationMapper, PatrolPlanLocation> implements PatrolPlanLocationService {

}
