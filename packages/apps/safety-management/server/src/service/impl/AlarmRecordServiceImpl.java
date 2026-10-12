package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyAlarmRecordMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmRecordPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmReportPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmRiskIndexVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyAlarmRecordPO;
import com.guhecloud.cube.srv.gen.safety.management.service.AlarmRecordService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "safetyAlarmRecordService")
public class AlarmRecordServiceImpl extends ServiceImpl<SafetyAlarmRecordMapper, SafetyAlarmRecordPO> implements AlarmRecordService {

    @Override
    public Page<AlarmRecordPageVO> getAlarmRecordPage(AlarmRecordQuery query) {
        return this.baseMapper.selectAlarmRecordPage(PagingUtils.convert(query), query);
    }

    @Override
    public List<AlarmRecordPageVO> getAlarmRecords(AlarmRecordQuery query) {
        return this.baseMapper.selectAlarmRecords(query);
    }

    @Override
    public Page<AlarmReportPageVO> getAlarmReportPage(AlarmRecordQuery query) {
        return this.baseMapper.selectAlarmReportPage(PagingUtils.convert(query), query);
    }

    @Override
    public List<AlarmReportPageVO> getAlarmReports(AlarmRecordQuery query) {
        return this.baseMapper.selectAlarmReports(query);
    }

    @Override
    public List<AlarmRiskIndexVO> getRiskIndexAlarmRecord(Period period, Integer marketTenant) {
        return this.baseMapper.selectRiskIndexAlarmRecord(period, marketTenant);
    }
}
