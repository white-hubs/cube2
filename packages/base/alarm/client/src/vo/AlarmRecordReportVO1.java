package com.guhecloud.cube.base.alarm.client.vo;

import lombok.Data;

import java.util.List;

@Data
public class AlarmRecordReportVO1 {

    private List<AlarmRecordReportVO> alarmRecordReportVOS;
    private List<AlarmRecordReportVO> userAlarmRecordReportVOS;
    private List<AlarmRecordReportVO> departmentAlarmRecordReportVOS;
    private List<AlarmRecordReportVO> ringRecordReportVOS;
    private List<AlarmRecordReportVO> sameRecordReportVOS;
}
