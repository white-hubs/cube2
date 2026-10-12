package com.guhecloud.cube.biz.daily.task.record.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordRepairPO;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordRepairMapper;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordRepairService;
import org.springframework.stereotype.Service;

@Service
public class DailyRecordRepairServiceImpl extends ServiceImpl<DailyRecordRepairMapper, DailyRecordRepairPO>
        implements DailyRecordRepairService {
}
