package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingClasses;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingRulePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleClassesResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排班规则查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingRuleQueryService {

    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final AttSchedulingRuleClassesMapper attSchedulingRuleClassesMapper;

    /**
     * 分页查询排班规则
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    public Pagination<AttSchedulingRuleResult> pageSchedulingRule(AttSchedulingRulePageParam param) {
        Page<AttSchedulingRulePO> page = attSchedulingRuleMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                    .<AttSchedulingRulePO>lambdaQuery()
                                                                                    .like(StringUtils.isNotBlank(param.getName()), AttSchedulingRulePO::getName, param.getName())
                                                                                    .orderByDesc(AttSchedulingRulePO::getUpdateTime)
                                                                           );
        return new Pagination<>(page.getTotal(), page.getPages(),
                                page.getRecords().stream().map(po -> {
                                    AttSchedulingRuleResult result = new AttSchedulingRuleResult();
                                    BeanUtils.copyProperties(po, result);
                                    result.setContinuousDays(result.getContinuousDays() > 0 ? result.getContinuousDays() : null);
                                    result.setIntervalHour(result.getIntervalHour() > 0 ? result.getIntervalHour() : null);
                                    return result;
                                }).collect(Collectors.toList())
        );
    }

    /**
     * 排班规则详情
     *
     * @param id 规则id
     *
     * @return 详情
     */
    public AttSchedulingRuleDetailResult infoSchedulingRule(Long id) {
        AttSchedulingRuleDetailResult result = new AttSchedulingRuleDetailResult();
        // 查询规则
        AttSchedulingRulePO rulePO = attSchedulingRuleMapper.selectById(id);
        BeanUtils.copyProperties(rulePO, result);
        // 查询规则的班次
        List<AttSchedulingRuleClassesResult> ruleClassesResultList = attSchedulingRuleClassesMapper.selectRuleClassesByRuleId(id);

        result.setRulesClassesList(ruleClassesResultList);

        return result;
    }

    /**
     * 校验集合中的班次是否交叉
     *
     * @param ruleClassList      校验的集合
     * @param checkFirstAndFinal 是否校验最后一个和第一个
     *
     * @return 是否交叉(true交叉 ， false不交叉
     */
    public boolean checkRuleClass(List<AttSchedulingClasses> ruleClassList, Boolean checkFirstAndFinal) {
        if (ruleClassList == null || ruleClassList.isEmpty()) {
            return false;
        }
        // 星期的map
        ruleClassList.sort(Comparator.comparing(AttSchedulingClasses::getClassesRealStartTime));
        if (Boolean.TRUE.equals(checkFirstAndFinal)) {
            AttSchedulingClasses next = new AttSchedulingClasses();
            next.setClassesRealStartTime(DateUtils.offsetDay(ruleClassList.getLast().getClassesRealStartTime(), 1));
            next.setClassesRealEndTime(DateUtils.offsetDay(ruleClassList.getLast().getClassesRealEndTime(), 1));
            ruleClassList.add(next);
        }
        for (int i = 0; i < ruleClassList.size(); i++) {
            int nextI = i + 1;
            AttSchedulingClasses classesI = ruleClassList.get(i);
            if (nextI >= ruleClassList.size()) {
                break;
            }
            AttSchedulingClasses classesNextI = ruleClassList.get(nextI);
            if (!classesI.getClassesRealEndTime().isBefore(classesNextI.getClassesRealStartTime())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 无日期的班次转有日期的班次（规则班次校验,年月不准确）
     *
     * @param day       日期
     * @param classesPO 无日期的班次
     *
     * @return 有日期的班次
     */
    public AttSchedulingClasses stringClassToDateClass(int day, AttClassesPO classesPO) {
        AttSchedulingClasses result = new AttSchedulingClasses();
        result.setClassesType(classesPO.getClassesType());

        String newDayStr = String.valueOf(day).length() > 1 ? String.valueOf(day) : "0" + day;
        String newDateStr = "2023-08-" + newDayStr;

        result.setClassesRealStartTime(DateUtils.parse(newDateStr + " " + classesPO.getStartWorkTime() + ":00"));
        result.setClassesBeforeStartTime(DateUtils.parse(newDateStr + " " + classesPO.getStartClockBeforeTime() + ":00"));
        result.setClassesAfterStartTime(DateUtils.parse(newDateStr + " " + classesPO.getStartClockAfterTime() + ":00"));
        result.setClassesRealEndTime(DateUtils.parse(newDateStr + " " + classesPO.getEndWorkTime() + ":00"));
        result.setClassesBeforeEndTime(DateUtils.parse(newDateStr + " " + classesPO.getEndClockBeforeTime() + ":00"));
        result.setClassesAfterEndTime(DateUtils.parse(newDateStr + " " + classesPO.getEndClockAfterTime() + ":00"));
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(classesPO.getRestStartTime())) {
            result.setClassesRestStartTime(DateUtils.parse(newDateStr + " " + classesPO.getRestStartTime() + ":00"));
        }
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(classesPO.getRestEndTime())) {
            result.setClassesRestEndTime(DateUtils.parse(newDateStr + " " + classesPO.getRestEndTime() + ":00"));
        }

        if (Boolean.TRUE.equals(classesPO.getEndTimeIsMorrow())) {
            result.setClassesRealEndTime(DateUtils.offsetDay(result.getClassesRealEndTime(), 1));
            result.setClassesAfterEndTime(DateUtils.offsetDay(result.getClassesAfterEndTime(), 1));
        }

        if (!result.getClassesBeforeStartTime().isBefore(result.getClassesRealStartTime())) {
            result.setClassesBeforeStartTime(DateUtils.offsetDay(result.getClassesBeforeStartTime(), -1));
        }
        if (!result.getClassesAfterStartTime().isAfter(result.getClassesRealStartTime())) {
            result.setClassesAfterStartTime(DateUtils.offsetDay(result.getClassesAfterStartTime(), 1));
        }
        if (!result.getClassesBeforeEndTime().isBefore(result.getClassesRealEndTime())) {
            result.setClassesBeforeEndTime(DateUtils.offsetDay(result.getClassesBeforeEndTime(), 1));
        }
        if (!result.getClassesAfterEndTime().isAfter(result.getClassesRealEndTime())) {
            result.setClassesAfterEndTime(DateUtils.offsetDay(result.getClassesAfterEndTime(), 1));
        }

        return result;
    }
}
