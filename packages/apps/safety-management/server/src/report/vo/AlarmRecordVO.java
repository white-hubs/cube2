package com.guhecloud.cube.srv.gen.safety.management.report.vo;

import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordReportVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AlarmRecordVO {
    //报警数
    private Long alarmTotalNum;
    //真实报警数
    private Long alarmActualNum;
    //已响应报警数
    private Long respondedNum;
    //已处理报警数
    private Long alarmHandler;
    //超时的报警数
    private Long timeoutNum;
    //超时的报警数
    private Long outDayHandlerNum;
    //按事件分组结果
    private Map<String, List<AlarmRecordReportVO>> alarmRecordEventMap;
    //按部门分组
    private Map<String, List<AlarmRecordReportVO>> alarmRecordDepartmentMap;
    //按用户角色分组
    private Map<String, List<AlarmRecordReportVO>> alarmRecordUserMap;

}
