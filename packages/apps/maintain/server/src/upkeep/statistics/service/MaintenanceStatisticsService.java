package com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.util.DatePeriodUtils;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TrendDecimalStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration.MaintenaceTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.dao.MaintenanceStatisticsMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.param.MaintainTaskTotalParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.param.MaintenanceDeviceQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo.MaintainTaskNumVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo.MaintainTaskPersonNumVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo.MaintenanceDeviceStatisticsPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.srv.gen.maintain.server.common.util.StatisticsUtils.supplementDecimalTrendStatistics;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceStatisticsService {

    private final MaintenanceStatisticsMapper maintenanceStatisticsMapper;
    private final UserClient userClient;

    public Pagination<MaintenanceDeviceStatisticsPageVO> getDeviceStatisticsPage(MaintenanceDeviceQueryParam param) {
        Page<MaintenanceDeviceStatisticsPageVO> page = maintenanceStatisticsMapper.getDeviceStatisticsPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(page);
    }

    public List<MaintenanceDeviceStatisticsPageVO> getDeviceStatistics(MaintenanceDeviceQueryParam param) {
        return maintenanceStatisticsMapper.getDeviceStatistics(param);
    }

    public MaintainTaskNumVO getMaintainTaskNumRatio() {
        return MaintainTaskNumVO.builder()
                .unfinished(maintenanceStatisticsMapper.selectMaintainTaskNumRatio(MaintenaceTaskStatusEnum.BE_MAINTAINED.name()))
                .finished(maintenanceStatisticsMapper.selectMaintainTaskNumRatio(MaintenaceTaskStatusEnum.COMPLETED.name()))
                .build();
    }

    public List<MaintainTaskPersonNumVO> getPersonStatisticsRatio() {
        List<MaintainTaskNumVO> maintainTaskNumVOS1 = maintenanceStatisticsMapper.selectBeMaintainedTaskPerson();
        Map<Long, Integer> unfinishedMap = new HashMap<>();
        Set<Long> userIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(maintainTaskNumVOS1)) {
            unfinishedMap = maintainTaskNumVOS1.stream()
                    .collect(Collectors.toMap(MaintainTaskNumVO::getKeyId, MaintainTaskNumVO::getUnfinished));
            userIds.addAll(maintainTaskNumVOS1.stream().map(MaintainTaskNumVO::getKeyId).collect(Collectors.toSet()));
        }
        List<MaintainTaskNumVO> maintainTaskNumVOS = maintenanceStatisticsMapper.selectMaintainTaskPersonNumRatio(MaintenaceTaskStatusEnum.COMPLETED.name());
        Map<Long, Integer> finishedMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(maintainTaskNumVOS)) {
            finishedMap = maintainTaskNumVOS.stream()
                    .collect(Collectors.toMap(MaintainTaskNumVO::getKeyId, MaintainTaskNumVO::getFinished));
            userIds.addAll(maintainTaskNumVOS.stream().map(MaintainTaskNumVO::getKeyId).collect(Collectors.toSet()));
        }
        List<UserDTO> userDTOS = userClient.listEnableUserByIds(userIds);
        List<MaintainTaskPersonNumVO> result = new ArrayList<>();
        for (UserDTO userDTO : userDTOS) {
            Integer unfinishedNum = unfinishedMap.getOrDefault(userDTO.getId(), 0);
            Integer finishedNum = finishedMap.getOrDefault(userDTO.getId(), 0);
            result.add(MaintainTaskPersonNumVO.builder()
                               .key(userDTO.getName())
                               .ratio(MathUtils.calculateProportionPercentage(finishedNum, finishedNum + unfinishedNum).floatValue())
                               .build());
        }
        result = result.stream().sorted(Comparator.comparing(MaintainTaskPersonNumVO::getRatio)
                                                .reversed()).limit(5).collect(Collectors.toList());
        return result;
    }

    public List<MaintainTaskPersonNumVO> getDeviceExecuteStatistics() {
        List<MaintainTaskNumVO> finishedNumList = maintenanceStatisticsMapper.selectMaintainTaskDeviceNumRatio(MaintenaceTaskStatusEnum.COMPLETED.name());
        Set<Long> keyIdList = new HashSet<>();
        Map<Long, MaintainTaskNumVO> finishedMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(finishedNumList)) {
            keyIdList = finishedNumList.stream().map(MaintainTaskNumVO::getKeyId).collect(Collectors.toSet());

            finishedMap = finishedNumList.stream()
                    .collect(Collectors.toMap(MaintainTaskNumVO::getKeyId, Function.identity()));
        }

        List<MaintainTaskNumVO> unfinishedNumList = maintenanceStatisticsMapper.selectMaintainTaskDeviceNumRatio(MaintenaceTaskStatusEnum.BE_MAINTAINED.name());
        Map<Long, MaintainTaskNumVO> unfinishedMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(unfinishedNumList)) {
            keyIdList.addAll(unfinishedNumList.stream().map(MaintainTaskNumVO::getKeyId).collect(Collectors.toSet()));
            unfinishedMap = unfinishedNumList.stream()
                    .collect(Collectors.toMap(MaintainTaskNumVO::getKeyId, Function.identity()));
        }
        List<MaintainTaskPersonNumVO> result = new ArrayList<>();
        for (Long keyId : keyIdList) {
            MaintainTaskNumVO finishedNum = finishedMap.getOrDefault(keyId, new MaintainTaskNumVO());
            MaintainTaskNumVO unfinishedNum = unfinishedMap.getOrDefault(keyId, new MaintainTaskNumVO());
            result.add(MaintainTaskPersonNumVO.builder()
                               .key(StringUtils.isNoneBlank(finishedNum.getKeyName()) ? finishedNum.getKeyName()
                                                                                      : unfinishedNum.getKeyName())
                               .ratio(MathUtils.calculateProportionPercentage(finishedNum.getFinished(),
                                                                              finishedNum.getFinished() + unfinishedNum.getFinished())
                                              .floatValue())
                               .build()
                      );
        }
        result = result.stream().sorted(Comparator.comparing(MaintainTaskPersonNumVO::getRatio)
                                                .reversed()).limit(5).collect(Collectors.toList());
        return result;
    }

    public List<TrendDecimalStatisticsVO> getTrendStatistics(StatisticsQueryParam param) {
        var dateField = DatePeriodUtils.handleDateParam(param);
        List<TrendDecimalStatisticsVO> maintainTaskNumList = this.maintenanceStatisticsMapper.selectMaintainTaskExecuteTrend(param, null);

        List<TrendDecimalStatisticsVO> finishedTaskList = this.maintenanceStatisticsMapper.selectMaintainTaskExecuteTrend(param, MaintenaceTaskStatusEnum.COMPLETED.name());
        Map<String, BigDecimal> finishNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(finishedTaskList)) {
            finishNumMap = finishedTaskList.stream()
                    .collect(Collectors.toMap(TrendDecimalStatisticsVO::getTime, TrendDecimalStatisticsVO::getCount));
        }
        List<TrendDecimalStatisticsVO> trendRatioList = new ArrayList<>();
        for (TrendDecimalStatisticsVO trendDecimalStatisticsVO : maintainTaskNumList) {
            trendRatioList.add(TrendDecimalStatisticsVO.builder()
                                       .time(trendDecimalStatisticsVO.getTime())
                                       .count(MathUtils.calculateProportionPercentage(finishNumMap.getOrDefault(trendDecimalStatisticsVO.getTime(), BigDecimal.ZERO),
                                                                                      trendDecimalStatisticsVO.getCount()))
                                       .build());
        }
        List<TrendDecimalStatisticsVO> trendStatisticsVOS = supplementDecimalTrendStatistics(dateField,
                                                                                             param.getDateString(),
                                                                                             param.getViewDateString(),
                                                                                             param.getPeriod(),
                                                                                             trendRatioList);
        return trendStatisticsVOS;

    }

    public List<TrendDecimalStatisticsVO> getTimeStatistics() {
        List<TrendDecimalStatisticsVO> trendDecimalStatisticsVOS = this.maintenanceStatisticsMapper.selectMaintainTaskTimeNum();
        Map<String, BigDecimal> timeMap = new HashMap<>();
        timeMap.put("08-12", BigDecimal.ZERO);
        timeMap.put("13-18", BigDecimal.ZERO);
        timeMap.put("19-22", BigDecimal.ZERO);
        timeMap.put("23-07", BigDecimal.ZERO);
        for (TrendDecimalStatisticsVO trendDecimalStatisticsVO : trendDecimalStatisticsVOS) {
            if (Integer.parseInt(trendDecimalStatisticsVO.getTime()) > 7 && Integer.parseInt(trendDecimalStatisticsVO.getTime()) < 13) {
                BigDecimal add = timeMap.get("08-12").add(trendDecimalStatisticsVO.getCount());
                timeMap.put("08-12", add);
            } else if (Integer.parseInt(trendDecimalStatisticsVO.getTime()) > 12 &&
                       Integer.parseInt(trendDecimalStatisticsVO.getTime()) < 19) {
                BigDecimal add = timeMap.get("13-18").add(trendDecimalStatisticsVO.getCount());
                timeMap.put("13-18", add);
            } else if (Integer.parseInt(trendDecimalStatisticsVO.getTime()) > 18 &&
                       Integer.parseInt(trendDecimalStatisticsVO.getTime()) < 23) {
                BigDecimal add = timeMap.get("19-22").add(trendDecimalStatisticsVO.getCount());
                timeMap.put("19-22", add);
            } else if (Integer.parseInt(trendDecimalStatisticsVO.getTime()) > 22 &&
                       Integer.parseInt(trendDecimalStatisticsVO.getTime()) < 8) {
                BigDecimal add = timeMap.get("23-07").add(trendDecimalStatisticsVO.getCount());
                timeMap.put("23-07", add);
            }
        }
        List<TrendDecimalStatisticsVO> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : timeMap.entrySet()) {
            result.add(TrendDecimalStatisticsVO.builder()
                               .time(entry.getKey())
                               .count(entry.getValue())
                               .build());
        }
        return result;
    }

    /**
     * 获取任务总数
     *
     * @param param 条件
     *
     * @return 总数
     */
    public Long maintainTaskTotalStat(MaintainTaskTotalParam param) {
        return this.maintenanceStatisticsMapper.maintainTaskTotalStat(param);
    }
}
