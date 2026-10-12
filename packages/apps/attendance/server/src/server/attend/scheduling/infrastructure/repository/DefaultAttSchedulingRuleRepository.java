package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.aggregate.AttSchedulingRulesAggregate;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.repository.AttSchedulingRuleRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRuleClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.SchedulingRuleOperationVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultAttSchedulingRuleRepository implements AttSchedulingRuleRepository {
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final AttSchedulingRuleClassesMapper attSchedulingRuleClassesMapper;

    /**
     * 新增
     *
     * @param aggregate 规则聚合
     *
     * @return id
     */
    @Override
    public Long insertAttSchedulingRule(AttSchedulingRulesAggregate aggregate) {
        AttSchedulingRulePO rulePo = AttSchedulingRulePO.builder()
                .id(aggregate.getId())
                .name(aggregate.getName())
                .ways(aggregate.getWays())
                .legalHolidaysRest(aggregate.getLegalHolidaysRest())
                .continuousDays(aggregate.getContinuousDays())
                .intervalHour(aggregate.getIntervalHour())
                .sameShift(aggregate.getSameShift())
                .build();
        AttPoAttributeSet.initAddBasePoAttribute(rulePo);
        // 新增规则
        attSchedulingRuleMapper.insert(rulePo);
        // 添加规则的班次
        this.insertRuleClasses(aggregate);
        // 返回规则id
        return aggregate.getId();
    }

    /**
     * 操作日志
     *
     * @param id   主键
     * @param name 名称
     */
    @Override
    @LogRecord(success = "新增:{{#name}}", subType = "SCHEDULING_RULE", subTypeName = "排班规则", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#id}}"
    )
    public void addPOLog(Long id, String name) {
        log.info("新增排班规则：{},{}", id, name);
    }

    @Override
    @LogRecord(success = "更新:{_DIFF{#oldPo,#newPo}}", subType = "SCHEDULING_RULE", subTypeName = "排班规则", operationType = "UPDATE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPo.id}}"
    )
    public void addUpdateRulePOLog(SchedulingRuleOperationVO newPo, SchedulingRuleOperationVO oldPo) {
        log.info("更新排班规则新：{}。旧：{}", newPo, oldPo);
    }

    @Override
    @LogRecord(success = "删除:【{{#rulePO.name}}】", subType = "SCHEDULING_RULE", subTypeName = "排班规则", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#rulePO.id}}"
    )
    public void addDeleteRulePOLog(AttSchedulingRulePO rulePO) {
        log.info("删除排班规则：{}", rulePO);
    }

    /**
     * 修改排班规则
     *
     * @param aggregate 规则聚合
     *
     * @return id
     */
    @Override
    public Long updateAttSchedulingRule(AttSchedulingRulesAggregate aggregate) {
        AttSchedulingRulePO rulePo = AttSchedulingRulePO.builder()
                .id(aggregate.getId())
                .name(aggregate.getName())
                .ways(aggregate.getWays())
                .legalHolidaysRest(aggregate.getLegalHolidaysRest())
                .continuousDays(aggregate.getContinuousDays())
                .intervalHour(aggregate.getIntervalHour())
                .sameShift(aggregate.getSameShift())
                .build();
        AttPoAttributeSet.initUpdateBasePoAttribute(rulePo);
        // 修改规则
        attSchedulingRuleMapper.updateById(rulePo);
        // 删除规则的班次
        attSchedulingRuleClassesMapper.removeByRuleId(DateUtils.toTimestamp(LocalDateTime.now()), aggregate.getId());
        // 重新插入新的班次
        this.insertRuleClasses(aggregate);
        // 返回规则id
        return aggregate.getId();
    }

    /**
     * 删除排班规则
     *
     * @param id 规则主键
     *
     * @return id
     */
    @Override
    public Long deleteAttSchedulingRule(Long id) {
        // 删除规则
        attSchedulingRuleMapper.removeById(DateUtils.toTimestamp(LocalDateTime.now()), id);
        // 删除规则的班次
        attSchedulingRuleClassesMapper.removeByRuleId(DateUtils.toTimestamp(LocalDateTime.now()), id);
        return id;
    }

    /**
     * 插入规则的班次
     *
     * @param aggregate 聚合(包含班次)
     */
    private void insertRuleClasses(AttSchedulingRulesAggregate aggregate) {
        // 应该循环出po集合，然后批量插入
        aggregate.getRulesClassesList().forEach(classes -> {
            AttSchedulingRuleClassesPO classesPO = AttSchedulingRuleClassesPO.builder()
                    .ruleId(aggregate.getId())
                    .id(classes.getId())
                    .week(classes.getWeek())
                    .build();
            AttPoAttributeSet.initAddBasePoAttribute(classesPO);
            attSchedulingRuleClassesMapper.insert(classesPO);
        });
    }
}
