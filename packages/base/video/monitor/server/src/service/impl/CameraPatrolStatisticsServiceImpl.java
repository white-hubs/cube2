package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQueryService;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolLineMapper;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolPersonMapper;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolRecordDeviceMapper;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolRecordMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolPerson;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolAuthTypeEnum;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceResultEnum;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolLineStatisticsParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolTopStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.statistics.CameraPatrolTrendStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolStatisticsService;
import com.guhecloud.cube.biz.vmonitor.server.util.DateUnitUtils;
import com.guhecloud.cube.biz.vmonitor.server.util.MathUtils;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolRecordStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolPersonPointStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolPointStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim.CameraPatrolTrendStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolStatisticsServiceImpl implements CameraPatrolStatisticsService {

    private final CameraPatrolRecordMapper cameraPatrolRecordMapper;

    private final CameraPatrolLineMapper cameraPatrolLineMapper;

    private final CameraPatrolRecordDeviceMapper cameraPatrolRecordDeviceMapper;

    private final CameraPatrolPersonMapper cameraPatrolPersonMapper;

    private final UserQueryService userQueryService;

    @Override
    public CameraPatrolRecordStatisticsVO getCameraPatrolRecordAbnormalStatistics(CameraPatrolLineStatisticsQueryParam param) {
        //本期数量
        Integer abnormalNum = this.cameraPatrolRecordMapper.selectPatrolAbnormalNum(param);
        //上期数量
        param.setPeriod(DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod()));
        Integer previousAbnormalNum = this.cameraPatrolRecordMapper.selectPatrolAbnormalNum(param);
        return CameraPatrolRecordStatisticsVO.builder()
                .abnormalNum(abnormalNum)
                .chainRatio(MathUtils.calculateMonthOnMonthRatio(
                        abnormalNum.longValue(), previousAbnormalNum.longValue()))
                .build();
    }

    @Override
    public CameraPatrolStatisticsVO getCameraPatrolStatistics(CameraPatrolLineStatisticsParam param) {
        Integer patrolLineCount = this.cameraPatrolLineMapper.selectCameraPatrolLineCount(param);
        Integer patrolLineDeviceCount = this.cameraPatrolLineMapper.selectCameraPatrolLineDevices(param);
        //巡逻人员统计
        Map<CameraPatrolAuthTypeEnum, List<CameraPatrolPerson>> cameraPatrolAuthTypeEnumCameraPatrolPersonMap = this.cameraPatrolPersonMapper
                .selectCameraPatrolPersons(param.getEnabled(), param.getPeriod())
                .stream().collect(Collectors.groupingBy(CameraPatrolPerson::getAuthType));
        Set<Long> userIds = new HashSet<>();
        cameraPatrolAuthTypeEnumCameraPatrolPersonMap.forEach((authType, persons) -> {
            Set<Long> authIds = new HashSet<>();
            persons.forEach(person ->
                                    Arrays.stream(person.getAuthIds().split(",")).map(Long::parseLong).forEach(authIds::add)
                           );
            if (CameraPatrolAuthTypeEnum.ROLE == authType) {
                userIds.addAll(userQueryService.roleIds(authIds).postList().stream().map(UserDTO::getId).collect(Collectors.toSet()));
            } else if (CameraPatrolAuthTypeEnum.DEPARTMENT == authType) {
                userIds.addAll(userQueryService.organizationIds(authIds).postList().stream().map(UserDTO::getId)
                                       .collect(Collectors.toSet()));
            } else {
                userIds.addAll(authIds);
            }
        });
        return new CameraPatrolStatisticsVO(patrolLineCount, patrolLineDeviceCount, userIds.size());
    }

    @Override
    public List<CameraPatrolTrendStatisticsVO> getCameraPatrolAbnormalTrendStatistics(CameraPatrolTrendStatisticsQueryParam param) {
        param.setPatrolResult(CameraPatrolDeviceResultEnum.CAMERA_PATROL_ABNORMAL.name());
        var dateField = DateUnitUtils.handleDateParam(param);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        List<CameraPatrolTrendStatisticsVO> cameraPatrolTrendStatisticsVOS = this.cameraPatrolRecordDeviceMapper
                .selectCameraPatrolTrendStatistics(param);
        List<CameraPatrolTrendStatisticsVO> finalData = new ArrayList<>();
        for (LocalDateTime start = startDate;
             start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
            String date = DateUtils.format(start, param.getDateString());
            String viewDate = DateUtils.format(start, param.getViewDateString());
            CameraPatrolTrendStatisticsVO trendStatVO = cameraPatrolTrendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date))
                    .findAny().orElse(null);
            finalData.add(trendStatVO == null ? new CameraPatrolTrendStatisticsVO(0, viewDate)
                                              : new CameraPatrolTrendStatisticsVO(trendStatVO.getTotalNum(), viewDate));
        }
        return finalData;
    }

    @Override
    public List<CameraPatrolPointStatisticsVO> getCameraPatrolPointTop(CameraPatrolTopStatisticsQueryParam param) {
        return this.cameraPatrolRecordDeviceMapper.selectCameraPatrolPointTop(param);
    }

    @Override
    public List<CameraPatrolTrendStatisticsVO> getCameraPatrolTimeDistribution(CameraPatrolLineStatisticsQueryParam param) {
        Map<String, CameraPatrolTrendStatisticsVO> stringCameraPatrolTrendStatisticsMap = this.cameraPatrolRecordMapper
                .selectPatrolRecordHourTrend(param)
                .stream().collect(Collectors.toMap(CameraPatrolTrendStatisticsVO::getPeriod, v -> v, (o, n) -> n));
        List<CameraPatrolTrendStatisticsVO> finalData = new ArrayList<>();
        for (Integer startHour = 0; startHour <= 23; startHour++) {
            if (stringCameraPatrolTrendStatisticsMap.containsKey(startHour.toString())) {
                CameraPatrolTrendStatisticsVO cameraPatrolTrendStatisticsVO = stringCameraPatrolTrendStatisticsMap
                        .get(startHour.toString());
                finalData.add(cameraPatrolTrendStatisticsVO);
            } else {
                finalData.add(new CameraPatrolTrendStatisticsVO(0, startHour.toString()));
            }
        }
        return finalData;
    }

    @Override
    public List<CameraPatrolPersonPointStatisticsVO> getCameraPatrolPersonPointStatistics(CameraPatrolLineStatisticsQueryParam param) {
        return this.cameraPatrolRecordMapper.selectCameraPatrolPersonPointStatistics(param);
    }
}
