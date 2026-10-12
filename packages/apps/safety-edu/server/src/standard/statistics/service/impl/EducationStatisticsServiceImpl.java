package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.service.impl;

import com.guhecloud.cube.srv.gen.safety.education.server.common.util.CommonUtil;
import com.guhecloud.cube.srv.gen.safety.education.server.common.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.common.util.StatisticsUtils;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.dao.CultivateTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo.SafetyEducationTaskChainVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.service.EducationStatisticsService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EducationStatisticsServiceImpl implements EducationStatisticsService {

    private final ExamTaskMapper examTaskMapper;

    private final CultivateTaskMapper cultivateTaskMapper;

    @Override
    public List<SafetyEducationThemeRatioVO> getSafetyEducationThemeRatio(SafetyEducationStatisticsQuery query) {
        List<SafetyEducationThemeRatioVO> examThemeRatios = this.examTaskMapper
                .selectExamThemeRatio(SessionContextHolder.getRequester().getTenant(), query);
        List<SafetyEducationThemeRatioVO> cultivateThemeRatio = this.cultivateTaskMapper.selectCultivateThemeRatio(query);
        return getSafetyEducationThemeRatioVOS(examThemeRatios, cultivateThemeRatio);
    }

    @Override
    public List<SafetyEducationThemeRatioVO> getSafetyEducationPermissionThemeRatio(SafetyEducationStatisticsQuery query) {
        List<SafetyEducationThemeRatioVO> examThemeRatios = getExamThemePermissionRatio(query);
        List<SafetyEducationThemeRatioVO> cultivateThemeRatio = getCultivateThemePermissionRatio(query);
        return getSafetyEducationThemeRatioVOS(examThemeRatios, cultivateThemeRatio);
    }

    private List<SafetyEducationThemeRatioVO> getSafetyEducationThemeRatioVOS(List<SafetyEducationThemeRatioVO> examThemeRatios,
                                                                              List<SafetyEducationThemeRatioVO> cultivateThemeRatio
                                                                             ) {
        List<SafetyEducationThemeRatioVO> ratioVOS = new ArrayList<>();
        Map<String, List<SafetyEducationThemeRatioVO>> collect = Stream
                .concat(examThemeRatios.stream(), cultivateThemeRatio.stream())
                .collect(Collectors.groupingBy(SafetyEducationThemeRatioVO::getId));
        for (String key : collect.keySet()) {
            List<SafetyEducationThemeRatioVO> vos = collect.get(key);
            int sum = vos.stream().mapToInt(SafetyEducationThemeRatioVO::getTotalNum).sum();
            vos.get(0).setTotalNum(sum);
            ratioVOS.add(vos.get(0));
        }
        ratioVOS = ratioVOS.stream().sorted(Comparator.comparingInt(SafetyEducationThemeRatioVO::getTotalNum).reversed())
                .collect(Collectors.toList());
        int totalNum = ratioVOS.stream().mapToInt(SafetyEducationThemeRatioVO::getTotalNum).sum();
        int count = 0;
        for (int i = 0; i < ratioVOS.size(); i++) {
            if (i > 2) {
                int subList = i;
                SafetyEducationThemeRatioVO vo = new SafetyEducationThemeRatioVO();
                int t = totalNum - count;
                vo.setTotalNum(t);
                vo.setName("其他");
                vo.setRatio(BigDecimal.valueOf(CommonUtil.countProportion(new BigDecimal(totalNum), new BigDecimal(t))));
                ratioVOS.set(i, vo);
                ratioVOS = ratioVOS.subList(0, ++subList);
                break;
            } else {
                final SafetyEducationThemeRatioVO vo = ratioVOS.get(i);
                vo.setRatio(BigDecimal.valueOf(CommonUtil
                                                       .countProportion(new BigDecimal(totalNum), new BigDecimal(vo.getTotalNum()))));
                count += vo.getTotalNum();
            }
        }
        return ratioVOS;
    }

    @Override
    public SafetyEducationTaskChainVO getSafetyEducationTaskChainRatio(SafetyEducationStatisticsQuery query) {
        //本期考试任务数量
        Integer currentExamTaskCount = this.examTaskMapper
                .selectVisualExamTaskCount(SessionContextHolder.getRequester().getTenant(), query);
        //本期培训任务数量
        Integer currentCultivateTaskCount = this.cultivateTaskMapper.selectCultivateCount(query);
        //参与人数
        Long cultivateUserCount = this.cultivateTaskMapper.selectCultivateUserCount(query);
        Long examUserCount = this.examTaskMapper.selectExamUserCount(query);
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(previousPeriod);
        //上期考试任务数量
        Integer lastExamTaskCount = this.examTaskMapper
                .selectVisualExamTaskCount(SessionContextHolder.getRequester().getTenant(), query);
        //上期培训任务数量
        Integer lastCultivateTaskCount = this.cultivateTaskMapper.selectCultivateCount(query);
        //同期
        Period samePeriod = DateUnitUtils.getSamePeriod(query.getPeriod());
        query.setPeriod(samePeriod);
        //同期考试任务数量
        Integer lastSameExamTaskCount = this.examTaskMapper
                .selectVisualExamTaskCount(SessionContextHolder.getRequester().getTenant(), query);
        //同期培训任务数量
        Integer lastSameCultivateTaskCount = this.cultivateTaskMapper.selectCultivateCount(query);
        SafetyEducationTaskChainVO safetyEducationTaskChainVO = new SafetyEducationTaskChainVO();
        safetyEducationTaskChainVO.setParticipants(cultivateUserCount + examUserCount);
        safetyEducationTaskChainVO.setTotalNum(currentExamTaskCount + currentCultivateTaskCount);
        safetyEducationTaskChainVO.setChainRatio(StatisticsUtils.getChainRatio(
                                                         currentExamTaskCount + currentCultivateTaskCount, lastExamTaskCount + lastCultivateTaskCount)
                                                );
        //同比
        safetyEducationTaskChainVO.setSameRatio(StatisticsUtils
                                                        .getChainRatio(currentExamTaskCount + currentCultivateTaskCount,
                                                                       lastSameExamTaskCount + lastSameCultivateTaskCount));
        return safetyEducationTaskChainVO;
    }

    @DataPermissionSwitch(dataModel = "safetyEducation:examTaskUser")
    public List<SafetyEducationThemeRatioVO> getExamThemePermissionRatio(SafetyEducationStatisticsQuery query) {
        return this.examTaskMapper
                .selectExamThemeRatio(SessionContextHolder.getRequester().getTenant(), query);
    }

    @DataPermissionSwitch(dataModel = "safetyEducation:cultivateTaskUser")
    public List<SafetyEducationThemeRatioVO> getCultivateThemePermissionRatio(SafetyEducationStatisticsQuery query) {
        return cultivateTaskMapper.selectCultivateThemeRatio(query);
    }
}
