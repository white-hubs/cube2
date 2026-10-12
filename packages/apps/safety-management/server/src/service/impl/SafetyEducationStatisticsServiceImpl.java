package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dao.CultivateTaskMapper;
import com.guhecloud.cube.srv.gen.safety.management.dao.ExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.CultivateTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.ExamPaperVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.ExamTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationTaskChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationTaskRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education.SafetyEducationMarketTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education.SafetyEducationTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.CultivateTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.CultivateTaskPO;
import com.guhecloud.cube.srv.gen.safety.management.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyEducationStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.management.util.CommonUtil;
import com.guhecloud.cube.srv.gen.safety.management.util.DateUnitUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyEducationStatisticsServiceImpl implements SafetyEducationStatisticsService {

    private final ExamTaskMapper examTaskMapper;

    private final CultivateTaskMapper cultivateTaskMapper;

    private final SafetyExamTaskPaperService safetyExamTaskPaperService;

    @Override

    public List<SafetyEducationThemeRatioVO> getSafetyEducationThemeRatio(SafetyEducationStatisticsQuery query) {
        List<SafetyEducationThemeRatioVO> examThemeRatios = this.examTaskMapper.selectExamThemeRatio(query);
        List<SafetyEducationThemeRatioVO> cultivateThemeRatio = this.cultivateTaskMapper.selectCultivateThemeRatio(query);
        List<SafetyEducationThemeRatioVO> ratioVOS = new ArrayList<>();
        Map<String, List<SafetyEducationThemeRatioVO>> collect = Stream.concat(examThemeRatios.stream(), cultivateThemeRatio.stream())
                .collect(Collectors.groupingBy(SafetyEducationThemeRatioVO::getId));
        for (String key : collect.keySet()) {
            List<SafetyEducationThemeRatioVO> vos = collect.get(key);
            int sum = vos.stream().mapToInt(SafetyEducationThemeRatioVO::getTotalNum).sum();
            vos.getFirst().setTotalNum(sum);
            ratioVOS.add(vos.getFirst());
        }
        ratioVOS = ratioVOS.stream().sorted(Comparator.comparingInt(SafetyEducationThemeRatioVO::getTotalNum).reversed())
                .collect(Collectors.toList());
        int totalNum = ratioVOS.stream().mapToInt(SafetyEducationThemeRatioVO::getTotalNum).sum();
        int count = 0;
        for (int i = 0; i < ratioVOS.size(); i++) {
            if (i > 2) {
                SafetyEducationThemeRatioVO vo = new SafetyEducationThemeRatioVO();
                int t = totalNum - count;
                vo.setTotalNum(t);
                vo.setName("其他");
                vo.setRatio(BigDecimal.valueOf(CommonUtil.countProportion(new BigDecimal(totalNum), new BigDecimal(t))));
                ratioVOS.set(i, vo);
                ratioVOS = ratioVOS.subList(0, ++i);
                break;
            } else {
                final SafetyEducationThemeRatioVO vo = ratioVOS.get(i);
                vo.setRatio(BigDecimal.valueOf(CommonUtil.countProportion(new BigDecimal(totalNum), new BigDecimal(vo.getTotalNum()))));
                count += vo.getTotalNum();
            }
        }
        return ratioVOS;
    }

    @Override
    public SafetyEducationTaskChainVO getSafetyEducationTaskChainRatio(SafetyEducationStatisticsQuery query) {
        //本期考试任务数量
        Integer currentExamTaskCount = this.examTaskMapper.selectExamTaskCount(query);
        //本期培训任务数量
        Integer currentCultivateTaskCount = this.cultivateTaskMapper.selectCultivateCount(query);
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(previousPeriod);
        //上期考试任务数量
        Integer lastExamTaskCount = this.examTaskMapper.selectExamTaskCount(query);
        //上期培训任务数量
        Integer lastCultivateTaskCount = this.cultivateTaskMapper.selectCultivateCount(query);
        SafetyEducationTaskChainVO safetyEducationTaskChainVO = new SafetyEducationTaskChainVO();
        safetyEducationTaskChainVO.setTotalNum(currentExamTaskCount + currentCultivateTaskCount);
        safetyEducationTaskChainVO.setChainRatio(MathUtils.calculateRingRatioPercentage(
                                                         currentExamTaskCount + currentCultivateTaskCount, lastExamTaskCount + lastCultivateTaskCount)
                                                );
        return safetyEducationTaskChainVO;
    }

    @Override
    public SafetyEducationTaskRatioVO getSafetyEducationTaskRatio(SafetyEducationStatisticsQuery query) {
        Integer currentExamTaskCount = this.examTaskMapper.selectExamTaskCount(query);
        Integer currentCultivateTaskCount = this.cultivateTaskMapper.selectCultivateCount(query);
        query.setPeriod(null);
        Integer examTaskCount = this.examTaskMapper.selectExamTaskCount(query);
        Integer cultivateTaskCount = this.cultivateTaskMapper.selectCultivateCount(query);
        int totalNum = examTaskCount + cultivateTaskCount;
        int periodTotalNum = currentCultivateTaskCount + currentExamTaskCount;
        SafetyEducationTaskRatioVO safetyEducationTaskRatioVO = new SafetyEducationTaskRatioVO();
        safetyEducationTaskRatioVO.setTotalNum(totalNum);
        safetyEducationTaskRatioVO.setPeriodTotalNum(periodTotalNum);
        safetyEducationTaskRatioVO.setRatio(0 == periodTotalNum ? BigDecimal.valueOf(0.0) : BigDecimal.valueOf(periodTotalNum)
                .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100)));
        return safetyEducationTaskRatioVO;
    }

    @Override
    public AppSafetyWorkSituationVO.WorkSituationVO getSafetyEducationSituation(BaseQuery query) {
        SafetyEducationTaskChainVO safetyEducationTaskChainRatio = this.getSafetyEducationTaskChainRatio(SafetyEducationStatisticsQuery
                                                                                                                 .builder()
                                                                                                                 .marketTenant(query.getMarketTenant())
                                                                                                                 .period(query.getPeriod())
                                                                                                                 .type(query.getType())
                                                                                                                 .build());
        return AppSafetyWorkSituationVO.WorkSituationVO.builder()
                .totalNum(safetyEducationTaskChainRatio.getTotalNum())
                .chainRatio(safetyEducationTaskChainRatio.getChainRatio())
                .build();
    }

    @Override
    public Long countCompleted(BaseStatisticsQuery query) {
        LambdaQueryChainWrapper<ExamTaskPO> examQuery = new LambdaQueryChainWrapper<>(examTaskMapper);
        long examCount = examQuery
                .eq(ExamTaskPO::getExamTaskStatus, ExamTaskStatusEnum.ENDED)
                .in(CollectionUtils.isNotEmpty(query.getMarketTenantIDs()), ExamTaskPO::getMarketTenant, query.getMarketTenantIDs())
                .between(query.getCreatedPeriod() != null,
                         ExamTaskPO::getCreateTime, ObjectUtils.ifNull(query.getCreatedPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getCreatedPeriod(), Period::getEnd))
                .count();
        LambdaQueryChainWrapper<CultivateTaskPO> cultivateQuery = new LambdaQueryChainWrapper<>(cultivateTaskMapper);
        long cultivateCount = cultivateQuery
                .eq(CultivateTaskPO::getCultivateTaskStatus, CultivateTaskStatusEnum.OVER)
                .in(CollectionUtils.isNotEmpty(query.getMarketTenantIDs()), CultivateTaskPO::getMarketTenant, query.getMarketTenantIDs())
                .between(query.getCreatedPeriod() != null,
                         CultivateTaskPO::getCreateTime, ObjectUtils.ifNull(query.getCreatedPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getCreatedPeriod(), Period::getEnd))
                .count();
        return Long.sum(examCount, cultivateCount);
    }

    @Override
    public SafetyEducationTaskStatisticsVO getSafetyEducationTaskStatistics(SafetyEducationStatisticsQuery query) {
        Integer cultivateCount = this.cultivateTaskMapper.selectCultivateCount(query);
        Integer examTaskCount = this.examTaskMapper.selectExamTaskCount(query);
        SafetyEducationTaskStatisticsVO taskStatisticsVO = new SafetyEducationTaskStatisticsVO();
        taskStatisticsVO.setCultivateTaskNum(cultivateCount);
        taskStatisticsVO.setExamTaskNum(examTaskCount);
        return taskStatisticsVO;
    }

    @Override
    public Pagination<SafetyEducationMarketTaskStatisticsVO> getMarketCultivateStatistics(SafetyEducationStatisticsQuery query) {
        Page<SafetyEducationMarketTaskStatisticsVO> statisticsVOS = this.cultivateTaskMapper.selectMarketCultivateStatistics(PagingUtils.convert(query), query);
        if (CollectionUtils.isEmpty(statisticsVOS.getRecords())) {
            return Pagination.empty();
        }
        SafetyEducationStatisticsQuery cultivateRankStatisticsQuery = getSafetyEducationStatisticsQuery(query, statisticsVOS);
        Page<SafetyEducationMarketTaskStatisticsVO> lastCultivateRankVOS = this.cultivateTaskMapper.selectMarketCultivateStatistics(PagingUtils.convert(cultivateRankStatisticsQuery), cultivateRankStatisticsQuery);
        chainRatio(statisticsVOS, lastCultivateRankVOS);
        return PagingUtils.convert(statisticsVOS);
    }

    private SafetyEducationStatisticsQuery getSafetyEducationStatisticsQuery(SafetyEducationStatisticsQuery query,
                                                                             Page<SafetyEducationMarketTaskStatisticsVO> statisticsVOS
                                                                            ) {
        List<String> tenants = statisticsVOS.getRecords().stream().map(vo -> String.valueOf(vo.getMarketTenant()))
                .collect(Collectors.toList());
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        SafetyEducationStatisticsQuery cultivateRankStatisticsQuery = ObjectUtils.copying(query, SafetyEducationStatisticsQuery.class);
        cultivateRankStatisticsQuery.setMarketTenant(null);
        cultivateRankStatisticsQuery.setMarketTenantIDs(tenants);
        cultivateRankStatisticsQuery.setPeriod(previousPeriod);
        return cultivateRankStatisticsQuery;
    }

    @Override
    public Pagination<SafetyEducationMarketTaskStatisticsVO> getMarketExamStatistics(SafetyEducationStatisticsQuery query) {
        Page<SafetyEducationMarketTaskStatisticsVO> statisticsVOS = this.examTaskMapper.selectMarketExamStatistics(PagingUtils.convert(query), query);
        if (CollectionUtils.isEmpty(statisticsVOS.getRecords())) {
            return Pagination.empty();
        }
        SafetyEducationStatisticsQuery cultivateRankStatisticsQuery = getSafetyEducationStatisticsQuery(query, statisticsVOS);
        Page<SafetyEducationMarketTaskStatisticsVO> lastCultivateRankVOS = this.examTaskMapper.selectMarketExamStatistics(PagingUtils.convert(cultivateRankStatisticsQuery), cultivateRankStatisticsQuery);
        chainRatio(statisticsVOS, lastCultivateRankVOS);
        return PagingUtils.convert(statisticsVOS);
    }

    @Override
    public Pagination<ExamTaskPageVO> getExamTaskPage(SafetyEducationStatisticsQuery query) {
        var examTaskPageVOPage = this.examTaskMapper.selectExamTaskPage(PagingUtils.convert(query), query);
        if (CollectionUtils.isEmpty(examTaskPageVOPage.getRecords())) {
            return Pagination.empty();
        }
        var ids = examTaskPageVOPage.getRecords().stream().map(ExamTaskPageVO::getId).collect(Collectors.toSet());
        var examTaskPaperMap = safetyExamTaskPaperService.getPaperByTaskIds(ids)
                .stream().collect(Collectors.groupingBy(SafetyExamTaskPaperPO::getExamTaskId));
        examTaskPageVOPage.getRecords().forEach(examTask -> {
            Optional.ofNullable(examTaskPaperMap.get(examTask.getId())).ifPresent((examTaskPaperPOS) -> {
                var examPaperVOS = examTaskPaperPOS.stream()
                        .map(examTaskPaperPO -> new ExamPaperVO(examTaskPaperPO.getExamPaperId(), examTaskPaperPO.getExamPaperName()))
                        .collect(Collectors.toList());
                examTask.setExamPapers(examPaperVOS);
            });
        });
        return PagingUtils.convert(examTaskPageVOPage);
    }

    @Override
    public Pagination<CultivateTaskPageVO> getCultivateTaskPage(SafetyEducationStatisticsQuery query) {
        Page<CultivateTaskPageVO> cultivateTaskPageVOPage = this.cultivateTaskMapper.selectCultivateTaskPage(PagingUtils.convert(query), query);
        return PagingUtils.convert(cultivateTaskPageVOPage);
    }

    private void chainRatio(Page<SafetyEducationMarketTaskStatisticsVO> statisticsVOS,
                            Page<SafetyEducationMarketTaskStatisticsVO> lastCultivateRankVOS
                           ) {
        statisticsVOS.getRecords().forEach(currentAlarmRankVO -> {
            Integer lastCultivateCount = 0;
            Optional<SafetyEducationMarketTaskStatisticsVO> lastCultivateRankOpt = lastCultivateRankVOS.getRecords().stream()
                    .filter(lastCultivateRank -> currentAlarmRankVO.getMarketTenant().equals(lastCultivateRank.getMarketTenant()))
                    .findFirst();
            if (lastCultivateRankOpt.isPresent()) {
                lastCultivateCount = lastCultivateRankOpt.get().getTotalNum();
            }
            currentAlarmRankVO.setChainRatio(MathUtils.calculateRingRatioPercentage(currentAlarmRankVO.getTotalNum(), lastCultivateCount));
        });
    }
}
