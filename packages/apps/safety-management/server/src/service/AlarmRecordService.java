package com.guhecloud.cube.srv.gen.safety.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmRecordPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmReportPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.AlarmRiskIndexVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyAlarmRecordPO;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

public interface AlarmRecordService extends IService<SafetyAlarmRecordPO> {

    Page<AlarmRecordPageVO> getAlarmRecordPage(AlarmRecordQuery query);

    List<AlarmRecordPageVO> getAlarmRecords(AlarmRecordQuery query);

    Page<AlarmReportPageVO> getAlarmReportPage(AlarmRecordQuery query);

    List<AlarmReportPageVO> getAlarmReports(AlarmRecordQuery query);

    List<AlarmRiskIndexVO> getRiskIndexAlarmRecord(Period period, Integer marketTenant);

}
