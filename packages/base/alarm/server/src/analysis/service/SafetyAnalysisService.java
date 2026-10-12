package com.guhecloud.cube.base.alarm.server.analysis.service;

import com.guhecloud.cube.base.alarm.server.analysis.param.SafetyAnalysisAlarmParam;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmHandlerRankCardVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmLevelRatioCardVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.DeviceStatsNumVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO1;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO2;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO3;

import java.util.List;

public interface SafetyAnalysisService {

    /**
     * 安全分析-主界面应用报警数
     *
     * @param param
     *
     * @return
     */
    SafetyAnalysisAlarmVO1 getAlarmNumSafetyAnalysisForApp(SafetyAnalysisAlarmParam param);

    /**
     * 获取安全报警数、未处理数、已处理数、（环比同比）
     *
     * @param param
     *
     * @return
     */
    SafetyAnalysisAlarmVO getAlarmNumAnalysisForApp(SafetyAnalysisAlarmParam param);

    /**
     * 获取报警事件等级占比
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelRatioCardVO> getAlarmLevelRatioCardForApp(SafetyAnalysisAlarmParam param);

    /**
     * 获取事件类型占比
     *
     * @param param
     *
     * @return
     */
    List<AlarmLevelRatioCardVO> getAlarmEventRatioCardForApp(SafetyAnalysisAlarmParam param);

    /**
     * 获取报警处理率、响应率、超时率、以及角色的不同排名
     *
     * @param param
     *
     * @return
     */
    AlarmHandlerRankCardVO getAlarmHandlerRankCardForApp(SafetyAnalysisAlarmParam param);

    /**
     * 获取设备状态数
     *
     * @param param
     *
     * @return
     */
    DeviceStatsNumVO getDeviceStatsNumCard(SafetyAnalysisAlarmParam param);

    /**
     * 获取报警事件正确率
     *
     * @param param
     *
     * @return
     */
    List<SafetyAnalysisAlarmVO2> getAlarmCorrectRatioCard(SafetyAnalysisAlarmParam param);

    SafetyAnalysisAlarmVO3 getAlarmSafetyWorkStatusCard(SafetyAnalysisAlarmParam param);
}
