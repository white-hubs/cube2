package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceProgetRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProgetRecord;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceProgetRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：维保结果记录服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceProgetRecordServiceImpl extends ServiceImpl<MaintenanceProgetRecordMapper, MaintenanceProgetRecord> implements MaintenanceProgetRecordService {

}