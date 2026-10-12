package com.guhecloud.cube.base.alarm.server.analysis.service.impl;

import com.guhecloud.cube.base.alarm.server.analysis.mapper.SafetyAnalysisMapper;
import com.guhecloud.cube.base.alarm.server.analysis.param.SafetyAnalysisAlarmParam;
import com.guhecloud.cube.base.alarm.server.analysis.service.SafetyAnalysisService;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmHandlerRankCardVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmHandlerRankCardVO1;
import com.guhecloud.cube.base.alarm.server.analysis.vo.AlarmLevelRatioCardVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.DeviceStatsNumVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO1;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO2;
import com.guhecloud.cube.base.alarm.server.analysis.vo.SafetyAnalysisAlarmVO3;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.util.DateUnitUtils;
import com.guhecloud.cube.base.alarm.server.util.MathUtils;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceBaseInfoAndNumberVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceBaseInfoSafetyReportVO;
import com.guhecloud.cube.base.device.center.std.statistics.DeviceBaseStatisticsClient;
import com.guhecloud.cube.biz.emergency.client.EmergencyFileClient;
import com.guhecloud.cube.biz.emergency.client.vo.EmergencyFileSafetyReportVO;
import com.guhecloud.cube.property.management.client.property.param.property.RiskSourceDeviceQueryParam;
import com.guhecloud.cube.property.management.client.property.vo.property.RiskSourceDeviceVO;
import com.guhecloud.cube.property.management.client.risk.source.client.RiskSourceLedgerClient;
import com.guhecloud.cube.srv.gen.loose.works.client.ProjectScheduleClient;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectDataForAnalysisVO;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectDataForReportVO;
import com.guhecloud.cube.srv.gen.maintain.client.MaintainSafetyAnalysisClient;
import com.guhecloud.cube.srv.gen.maintain.vo.MaintainSafetyAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.vo.WorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.education.client.CultivateTaskClient;
import com.guhecloud.cube.srv.gen.safety.education.client.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.client.vo.SafetyEducationTaskChainVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyAnalysisServiceImpl implements SafetyAnalysisService {
    public static final String APPLICATION_CONSTANTS = "builtin.app::";

    private final SafetyAnalysisMapper safetyAnalysisMapper;
    private final DeviceBaseStatisticsClient deviceBaseStatisticsClient;
    private final RiskSourceLedgerClient riskSourceLedgerClient;
    private final ProjectScheduleClient projectScheduleClient;
    private final MaintainSafetyAnalysisClient maintainSafetyAnalysisClient;
    private final CultivateTaskClient cultivateTaskClient;
    private final EmergencyFileClient emergencyFileClient;

    @Override
    public SafetyAnalysisAlarmVO1 getAlarmNumSafetyAnalysisForApp(SafetyAnalysisAlarmParam param) {
        List<AlarmLevelRatioCardVO> alarmApplicationRatioCardVOS = safetyAnalysisMapper.selectAlarmNumGroupByApplicationCode(param);
        Long totalAlamNum;
        if (CollectionUtils.isNotEmpty(alarmApplicationRatioCardVOS)) {
            totalAlamNum = alarmApplicationRatioCardVOS.stream().map(AlarmLevelRatioCardVO::getAlarmNum).reduce(Long::sum).get();
            alarmApplicationRatioCardVOS.forEach(t -> {
                t.setRatio(MathUtils.calculateProportion(t.getAlarmNum(), totalAlamNum));
                t.setAlarmTotalNum(totalAlamNum);
            });
        } else {
            totalAlamNum = 0L;
        }
        //小散工程数
        ProjectDataForReportVO projectDataForReportVO = projectScheduleClient.projectDataForReport(param.getPeriod());

        List<String> deviceNameList = Stream.of("FIRE_FIGHTING_EQUIPMENT", "FLAMMABLE_EQUIPMENT", "TRANSPORT_DEVICES")
                .collect(Collectors.toList());
        RiskSourceDeviceVO riskSourceDeviceVO = getRiskSourceBuildData(deviceNameList);
        return SafetyAnalysisAlarmVO1.builder()
                .applicationAlarmDataList(alarmApplicationRatioCardVOS)
                .safetyAlarmNum(totalAlamNum)
                .looseWorkNum(projectDataForReportVO != null ? projectDataForReportVO.getNowTotal() : 0L)
                .riskSourceNum(riskSourceDeviceVO.getDeviceNum())
                .build();
    }

    @Override
    public SafetyAnalysisAlarmVO getAlarmNumAnalysisForApp(SafetyAnalysisAlarmParam param) {
        SafetyAnalysisAlarmVO safetyAnalysisAlarmVO = safetyAnalysisMapper.selectAlarmRecordTotal(param);

        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        param.setPeriod(previousPeriod);
        SafetyAnalysisAlarmVO ringAlarmVO = safetyAnalysisMapper.selectAlarmRecordTotal(param);

        safetyAnalysisAlarmVO.setRingAlarmNumRatio(MathUtils
                                                           .calculateMonthOnMonthRatio(safetyAnalysisAlarmVO.getAlarmNum(), ringAlarmVO
                                                                   .getAlarmNum()));
        safetyAnalysisAlarmVO.setRingNoHandlerRatio(MathUtils
                                                            .calculateMonthOnMonthRatio(safetyAnalysisAlarmVO.getNoHandlerNum(), ringAlarmVO
                                                                    .getNoHandlerNum()));
        safetyAnalysisAlarmVO.setRingHandlerNumRatio(MathUtils
                                                             .calculateMonthOnMonthRatio(safetyAnalysisAlarmVO.getHandlerNum(), ringAlarmVO
                                                                     .getHandlerNum()));
        if ("GAS_MANAGEMENT".equals(param.getApplicationCode())) {
            RiskSourceDeviceVO riskSourceDeviceVO = getRiskSourceBuildData(Collections.singletonList("FLAMMABLE_EQUIPMENT"));
            safetyAnalysisAlarmVO.setInflammableDeviceNum(riskSourceDeviceVO.getDeviceNum());
        }
        return safetyAnalysisAlarmVO;
    }

    @Override
    public List<AlarmLevelRatioCardVO> getAlarmLevelRatioCardForApp(SafetyAnalysisAlarmParam param) {
        List<AlarmLevelRatioCardVO> alarmLevelRatioCardVOS = safetyAnalysisMapper.selectAlarmNumGroupByLevelCode(param);
        if (CollectionUtils.isEmpty(alarmLevelRatioCardVOS)) {
            return new ArrayList<>();
        }
        Long totalAlamNum = alarmLevelRatioCardVOS.stream().map(AlarmLevelRatioCardVO::getAlarmNum).reduce(Long::sum).get();
        alarmLevelRatioCardVOS.forEach(t -> {
            t.setRatio(MathUtils.calculateProportion(t.getAlarmNum(), totalAlamNum));
            t.setAlarmTotalNum(totalAlamNum);
        });
        return alarmLevelRatioCardVOS;
    }

    @Override
    public List<AlarmLevelRatioCardVO> getAlarmEventRatioCardForApp(SafetyAnalysisAlarmParam param) {
        List<AlarmLevelRatioCardVO> alarmEventRatioCardVOS = safetyAnalysisMapper.selectAlarmNumGroupByEventCode(param);
        if (CollectionUtils.isEmpty(alarmEventRatioCardVOS)) {
            return new ArrayList<>();
        }
        Long totalAlamNum = alarmEventRatioCardVOS.stream().map(AlarmLevelRatioCardVO::getAlarmNum).reduce(Long::sum).get();
        alarmEventRatioCardVOS.forEach(t -> {
            t.setRatio(MathUtils.calculateProportion(t.getAlarmNum(), totalAlamNum));
            t.setAlarmTotalNum(totalAlamNum);
        });
        return alarmEventRatioCardVOS;
    }

    @Override
    public AlarmHandlerRankCardVO getAlarmHandlerRankCardForApp(SafetyAnalysisAlarmParam param) {
        AlarmHandlerRankCardVO1 applicationAlarmVO = safetyAnalysisMapper.selectAlarmRespondHandlerTimeOutNum(param);

        //按用户分组
        List<AlarmHandlerRankCardVO1> alarmHandlerRankCardVO1s = safetyAnalysisMapper.selectAlarmUserRatioRank(param);
        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        param.setPeriod(previousPeriod);
        AlarmHandlerRankCardVO1 ringApplicationAlarmVO = safetyAnalysisMapper.selectAlarmRespondHandlerTimeOutNum(param);

        AlarmHandlerRankCardVO rankCardVO = AlarmHandlerRankCardVO.builder()
                .respondedRatio(MathUtils.calculateProportion(applicationAlarmVO.getRespondedNum(), applicationAlarmVO.getAlarmNum()))
                .respondedRingRatio(MathUtils.calculateMonthOnMonthRatio(
                        applicationAlarmVO.getRespondedNum(), ringApplicationAlarmVO.getRespondedNum()))
                .handlerRatio(MathUtils.calculateProportion(applicationAlarmVO.getHandlerNum(), applicationAlarmVO.getAlarmNum()))
                .handlerRingRatio(MathUtils.calculateMonthOnMonthRatio(applicationAlarmVO.getHandlerNum(), ringApplicationAlarmVO
                        .getHandlerNum()))
                .timeOutRatio(MathUtils.calculateProportion(applicationAlarmVO.getTimeoutNum(), applicationAlarmVO.getAlarmNum()))
                .timeOutRingRatio(MathUtils.calculateMonthOnMonthRatio(applicationAlarmVO.getTimeoutNum()
                        , ringApplicationAlarmVO.getTimeoutNum()))
                .build();

        List<AlarmHandlerRankCardVO.AlarmUserRatioVO> userResponedRatioList = new ArrayList<>();
        List<AlarmHandlerRankCardVO.AlarmUserRatioVO> userHandlerRatioList = new ArrayList<>();
        List<AlarmHandlerRankCardVO.AlarmUserRatioVO> userTimeOutRatioList = new ArrayList<>();
        for (AlarmHandlerRankCardVO1 alarmHandlerRankCardVO1 : alarmHandlerRankCardVO1s) {
            AlarmHandlerRankCardVO.AlarmUserRatioVO userResponedRatioVO = new AlarmHandlerRankCardVO.AlarmUserRatioVO();
            userResponedRatioVO.setUserName(alarmHandlerRankCardVO1.getUserName());
            userResponedRatioVO.setRatio(MathUtils.calculateProportion(alarmHandlerRankCardVO1.getRespondedNum(), alarmHandlerRankCardVO1
                    .getAlarmNum()));
            userResponedRatioList.add(userResponedRatioVO);
            AlarmHandlerRankCardVO.AlarmUserRatioVO userHandlerRatioVO = new AlarmHandlerRankCardVO.AlarmUserRatioVO();
            userHandlerRatioVO.setUserName(alarmHandlerRankCardVO1.getUserName());
            userHandlerRatioVO.setRatio(MathUtils.calculateProportion(alarmHandlerRankCardVO1.getHandlerNum(), alarmHandlerRankCardVO1
                    .getAlarmNum()));
            userHandlerRatioList.add(userHandlerRatioVO);
            AlarmHandlerRankCardVO.AlarmUserRatioVO userTimeOutRatioVO = new AlarmHandlerRankCardVO.AlarmUserRatioVO();
            userTimeOutRatioVO.setUserName(alarmHandlerRankCardVO1.getUserName());
            userTimeOutRatioVO.setRatio(MathUtils.calculateProportion(alarmHandlerRankCardVO1.getTimeoutNum(), alarmHandlerRankCardVO1
                    .getAlarmNum()));
            userTimeOutRatioList.add(userTimeOutRatioVO);
        }
        rankCardVO.setUserResponedRatioRankList(userResponedRatioList.stream().sorted(Comparator
                                                                                              .comparing(AlarmHandlerRankCardVO.AlarmUserRatioVO::getRatio)
                                                                                              .reversed())
                                                        .collect(Collectors.toList()));
        rankCardVO.setUserhandlerRatioRankList(userHandlerRatioList.stream().sorted(Comparator
                                                                                            .comparing(AlarmHandlerRankCardVO.AlarmUserRatioVO::getRatio)
                                                                                            .reversed())
                                                       .collect(Collectors.toList()));
        rankCardVO.setUsertimeOutRatioRankList(userTimeOutRatioList.stream().sorted(Comparator
                                                                                            .comparing(AlarmHandlerRankCardVO.AlarmUserRatioVO::getRatio)
                                                                                            .reversed())
                                                       .collect(Collectors.toList()));
        return rankCardVO;
    }

    @Override
    public DeviceStatsNumVO getDeviceStatsNumCard(SafetyAnalysisAlarmParam param) {
        DeviceStatsNumVO result = new DeviceStatsNumVO();
        DeviceBaseInfoSafetyReportVO deviceVO = deviceBaseStatisticsClient.getDeviceNumberGroupOnStatusByTagCode(Collections
                                                                                                                         .singletonList(
                                                                                                                                 APPLICATION_CONSTANTS +
                                                                                                                                 param.getApplicationCode()), param
                                                                                                                         .getPeriod());
        DeviceBaseInfoAndNumberVO deviceNumberVO = new DeviceBaseInfoAndNumberVO();
        if (Objects.nonNull(deviceVO) && CollectionUtils.isNotEmpty(deviceVO.getDeviceBaseInfoAndNumberVOS())) {
            deviceNumberVO = deviceVO.getDeviceBaseInfoAndNumberVOS().get(0);
        } else {
            deviceNumberVO.setNormalNumber(0);
            deviceNumberVO.setOfflineNumber(0);
            deviceNumberVO.setTotalNumber(0);
        }
        result.setDeviceNum(deviceNumberVO.getTotalNumber());
        result.setNormalNumber(deviceNumberVO.getNormalNumber());
        result.setOfflineNumber(deviceNumberVO.getOfflineNumber());
        RiskSourceDeviceVO riskSourceDeviceVO = getRiskSourceBuildData(Collections.singletonList("FIRE_FIGHTING_EQUIPMENT"));
        result.setPutOnRecordNumber(riskSourceDeviceVO.getDeviceNum());
        result.setExpireNumber(riskSourceDeviceVO.getExpireNum());
        return result;
    }

    @Override
    public List<SafetyAnalysisAlarmVO2> getAlarmCorrectRatioCard(SafetyAnalysisAlarmParam param) {
        return safetyAnalysisMapper.selectAlarmEventCorrectRatio(param);
    }

    @Override
    public SafetyAnalysisAlarmVO3 getAlarmSafetyWorkStatusCard(SafetyAnalysisAlarmParam param) {
        Period period = param.getPeriod();

        //隐患处理率-巡更次数-巡检次数
        MaintainSafetyAnalysisVO maintainSafetyAnalysisVO = maintainSafetyAnalysisClient.maintainSafetyAnalysis(period, param.getType());
        //小散工程
        ProjectDataForAnalysisVO projectDataForAnalysisVO = projectScheduleClient
                .projectDataForAbnormalSafetyAnalysis(period, param.getType());
        WorkSituationVO projectWorkStatus = WorkSituationVO.builder()
                .totalNum(projectDataForAnalysisVO.getTotalNum())
                .ratio(projectDataForAnalysisVO.getRatio())
                .chainRatio(projectDataForAnalysisVO.getChainRatio())
                .build();

        //安全培训
        SafetyEducationStatisticsQuery statisticsQuery = new SafetyEducationStatisticsQuery();
        statisticsQuery.setType(param.getFilterStatus());
        statisticsQuery.setPeriod(period);
        SafetyEducationTaskChainVO safetyEducationTaskChainRatio = cultivateTaskClient.getSafetyEducationTaskChainRatio(statisticsQuery);
        WorkSituationVO educationWorkStatus = WorkSituationVO.builder()
                .totalNum(0L)
                .chainRatio(0f)
                .build();
        if (Objects.nonNull(safetyEducationTaskChainRatio)) {

            educationWorkStatus = WorkSituationVO.builder()
                    .totalNum(Long.valueOf(safetyEducationTaskChainRatio.getTotalNum()))
                    .chainRatio(safetyEducationTaskChainRatio.getChainRatio().floatValue())
                    .build();
        }
        //预案文件
        EmergencyFileSafetyReportVO emergencyFileForSafetyReport = emergencyFileClient.getEmergencyFileForSafetyReport(period);
        //风险源建档率
        List<String> deviceNameList = Stream.of("FIRE_FIGHTING_EQUIPMENT", "FLAMMABLE_EQUIPMENT", "TRANSPORT_DEVICES")
                .collect(Collectors.toList());
        RiskSourceDeviceQueryParam deviceQueryParam = new RiskSourceDeviceQueryParam();
        deviceQueryParam.setDeviceNameList(deviceNameList);
        List<RiskSourceDeviceVO> riskSourceDeviceNum = riskSourceLedgerClient.getRiskSourceDeviceNum(deviceQueryParam);
        long count = riskSourceDeviceNum.stream()
                .filter(t -> StringUtils.isNotBlank(t.getDeviceCode()) &&
                             Objects.nonNull(t.getDeviceNum()) && t.getDeviceNum() != 0L)
                .count();

        //报警任务响应率
        param.setApplicationCode(null);
        Long totalNum = safetyAnalysisMapper.selectAlarmRecordCountNum(param);
        param.setFilterStatus(AlarmStatusEnum.ALARM_UN_CONFIRMED.name());
        Long confirmedNum = safetyAnalysisMapper.selectAlarmRecordCountNum(param);
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        param.setPeriod(previousPeriod);
        Long ringTotalNum = safetyAnalysisMapper.selectAlarmRecordCountNum(param);
        WorkSituationVO alarmWorkSituationVO = WorkSituationVO.builder()
                .totalNum(confirmedNum)
                .ratio(MathUtils.calculateProportion(confirmedNum, totalNum))
                .chainRatio(MathUtils.calculateMonthOnMonthRatio(confirmedNum, ringTotalNum))
                .build();
        return SafetyAnalysisAlarmVO3.builder()
                .alarmDispose(alarmWorkSituationVO)
                .hiddenDangerDispose(maintainSafetyAnalysisVO.getHiddenDangerDispose())
                .projectError(projectWorkStatus)
                .inspectFrequency(maintainSafetyAnalysisVO.getInspectFrequency())
                .patrolFrequency(maintainSafetyAnalysisVO.getPatrolFrequency())
                .educationFrequency(educationWorkStatus)
                .riskSourcesFillDispose(WorkSituationVO.builder()
                                                .totalNum((long) deviceNameList.size())
                                                .ratio(MathUtils.calculateProportion(count, (long) deviceNameList.size()))
                                                .build())
                .emergencyFile(WorkSituationVO.builder()
                                       .totalNum(emergencyFileForSafetyReport.getTotalNum())
                                       .build())
                .build();
    }

    private RiskSourceDeviceVO getRiskSourceBuildData(List<String> deiceNameList) {
        RiskSourceDeviceQueryParam deviceQueryParam = new RiskSourceDeviceQueryParam();
        deviceQueryParam.setDeviceNameList(deiceNameList);
        List<RiskSourceDeviceVO> riskSourceDeviceNum = riskSourceLedgerClient.getRiskSourceDeviceNum(deviceQueryParam);
        RiskSourceDeviceVO riskSourceDeviceVO = new RiskSourceDeviceVO();
        if (CollectionUtils.isNotEmpty(riskSourceDeviceNum)) {
            List<Long> deviceNumList = riskSourceDeviceNum.stream().filter(t -> Objects.nonNull(t.getDeviceNum()))
                    .map(RiskSourceDeviceVO::getDeviceNum).collect(Collectors.toList());
            List<Long> expireNumList = riskSourceDeviceNum.stream().filter(t -> Objects.nonNull(t.getExpireNum()))
                    .map(RiskSourceDeviceVO::getExpireNum).collect(Collectors.toList());
            riskSourceDeviceVO.setDeviceNum(
                    CollectionUtils.isNotEmpty(deviceNumList) ? deviceNumList.stream().reduce(Long::sum).get() : 0L);
            riskSourceDeviceVO.setExpireNum(
                    CollectionUtils.isNotEmpty(expireNumList) ? expireNumList.stream().reduce(Long::sum).get() : 0L);
        } else {
            riskSourceDeviceVO.setDeviceNum(0L);
            riskSourceDeviceVO.setExpireNum(0L);
        }
        return riskSourceDeviceVO;
    }

}
