package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceObjectProjectsMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceObjectProjects;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceObjectProjectsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：维保对象维保项服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceObjectProjectsServiceImpl extends ServiceImpl<MaintenanceObjectProjectsMapper, MaintenanceObjectProjects> implements MaintenanceObjectProjectsService {

}