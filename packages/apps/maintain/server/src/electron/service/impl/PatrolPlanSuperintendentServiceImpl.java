package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanSuperintendentMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlanSuperintendent;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolPlanSuperintendentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 计划人员服务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolPlanSuperintendentServiceImpl extends ServiceImpl<PatrolPlanSuperintendentMapper, PatrolPlanSuperintendent> implements PatrolPlanSuperintendentService {
}
