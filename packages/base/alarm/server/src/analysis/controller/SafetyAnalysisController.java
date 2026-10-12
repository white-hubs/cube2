package com.guhecloud.cube.base.alarm.server.analysis.controller;

import com.guhecloud.cube.base.alarm.server.analysis.param.SafetyAnalysisAlarmParam;
import com.guhecloud.cube.base.alarm.server.analysis.service.SafetyAnalysisService;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmHandlerRankCardVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmLevelRatioCardVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.DeviceStatsNumVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO1;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO2;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 安全分析-app
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/safety/analysis")
public class SafetyAnalysisController {

    private final SafetyAnalysisService safetyAnalysisService;

    /**
     * 安全分析-主界面
     */
    @GetMapping("/alarm/main/data")
    public SafetyAnalysisAlarmVO1 getAlarmNumSafetyAnalysisForApp(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisService.getAlarmNumSafetyAnalysisForApp(param);
    }

    /**
     * 安全分析-安全报警数据
     */
    @GetMapping("/alarm/total/num")
    public SafetyAnalysisAlarmVO getAlarmNumAnalysisForApp(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisService.getAlarmNumAnalysisForApp(param);
    }

    /**
     * 安全分析-报警等级占比
     */
    @GetMapping("/alarm/level/ratio")
    public List<AlarmLevelRatioCardVO> getAlarmLevelRatioCardForApp(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisService.getAlarmLevelRatioCardForApp(param);
    }

    /**
     * 安全分析-报警事件占比
     */
    @GetMapping("/alarm/event/ratio")
    public List<AlarmLevelRatioCardVO> getAlarmEventRatioCardForApp(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisService.getAlarmEventRatioCardForApp(param);
    }

    /**
     * 安全分析-报警处理排名
     */
    @GetMapping("/alarm/handler/rank")
    public AlarmHandlerRankCardVO getAlarmHandlerRankCardForApp(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisService.getAlarmHandlerRankCardForApp(param);
    }

    /**
     * 安全分析-消防设备建档情况
     */
    @GetMapping("/device/status/card")
    public DeviceStatsNumVO getDeviceStatsNumCard(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisService.getDeviceStatsNumCard(param);
    }

    /**
     * 安防监控界面
     * 安全分析-报警事件正确率
     * 参数传入：应用编码、时间
     */
    @GetMapping("/alarm/correct/ratio")
    public List<SafetyAnalysisAlarmVO2> getAlarmCorrectRatioCard(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisService.getAlarmCorrectRatioCard(param);
    }

    /**
     * 安全分析-安全工作情况统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm/safety/work/status")
    public SafetyAnalysisAlarmVO3 getAlarmSafetyWorkStatusCard(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisService.getAlarmSafetyWorkStatusCard(param);
    }
}
