package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterReadeRecordMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterReadeRecord;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterReadeRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterReadeRecordServiceImpl extends ServiceImpl<MeterReadeRecordMapper, MeterReadeRecord> implements MeterReadeRecordService {
}
