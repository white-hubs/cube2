package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayGrantWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository.AttHolidayRuleRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDefaultHolidayRuleRepository implements AttHolidayRuleRepository {

    private final AttHolidayRuleMapper attHolidayRuleMapper;

    /**
     * 新增假期规则
     *
     * @param holidayRulePO 新增规则
     */
    @Override
    @LogRecord(success = "新增:{{#holidayRulePO.name}}", subType = "HOLIDAY_TYPE", subTypeName = "假期类型", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#holidayRulePO.id}}"
    )
    public void addHolidayRule(AttHolidayRulePO holidayRulePO) {
        attHolidayRuleMapper.insert(holidayRulePO);
    }

    /**
     * 编辑假期规则
     *
     * @param oldRulePo 旧规则
     * @param newRulePo 新规则
     */
    @Override
    @LogRecord(success = "更新:{_DIFF{#oldRulePo,#newRulePo}}", subType = "HOLIDAY_TYPE", subTypeName = "假期类型",
               operationType = "UPDATE",
               typeName = "考勤系统", type = "ATTENDANCE", bizNo = "{{#newRulePo.id}}"
    )
    public void editHolidayRule(AttHolidayRulePO oldRulePo, AttHolidayRulePO newRulePo, AttHolidayRulePO param) {

        attHolidayRuleMapper.update(null, Wrappers.<AttHolidayRulePO>lambdaUpdate()
                                            .eq(AttHolidayRulePO::getId, param.getId())
                                            .set(AttHolidayRulePO::getName, param.getName())
                                            .set(AttHolidayRulePO::getMinAskUnit, param.getMinAskUnit())
                                            .set(AttHolidayRulePO::getDayOfHour, param.getDayOfHour())
                                            .set(AttHolidayRulePO::getHolidayType, param.getHolidayType())
                                            .set(AttHolidayRulePO::getIntoAbsence, param.getIntoAbsence())
                                            .set(AttHolidayRulePO::getCalculationWay, param.getCalculationWay())
                                            .set(AttHolidayRulePO::getNotLimit, param.getNotLimit())
                                            .set(AttHolidayRulePO::getGrantWay, param.getGrantWay())
                                            .set(AttHolidayRulePO::getGrantDate, param.getGrantDate())
                                            .set(AttHolidayRulePO::getQuota, param.getQuota())
                                            .set(AttHolidayRulePO::getValidity, param.getValidity())
                                            .set(AttHolidayRulePO::getQuotaType, param.getQuotaType())
                                            .set(AttHolidayRulePO::getFixedNumber, param.getFixedNumber())
                                            .set(AttHolidayRulePO::getFixedUnit, param.getFixedUnit())
                                            .set(AttHolidayRulePO::getLongTerm, param.getLongTerm())
                                            .set(AttHolidayRulePO::getValidityNumber, param.getValidityNumber())
                                            .set(AttHolidayRulePO::getValidityUnit, param.getValidityUnit())
                                            .set(AttHolidayRulePO::getUpdateTime, LocalDateTime.now())
                                   );
    }

    /**
     * 逻辑删除
     *
     * @param rulePO 规则
     */
    @Override
    @LogRecord(success = "删除:{{#rulePO.name}}", subType = "HOLIDAY_TYPE", subTypeName = "假期类型", operationType = "DELETE",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#rulePO.id}}"
    )
    public void removeHolidayRule(AttHolidayRulePO rulePO) {
        // 删除前校验
        //        List<AttGroupPO> groupPOList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
        //                .like(AttGroupPO::getHolidayRuleIds,id)
        //        );
        //        if(groupPOList!=null&&!groupPOList.isEmpty()){
        //            StringBuffer buffer = new StringBuffer("当前规则应用于:");
        //            for (AttGroupPO groupPO : groupPOList) {
        //                buffer.append(groupPO.getName()).append(",");
        //            }
        //            throw new BusinessException(buffer+"考勤组，请先解除规则应用再删除！");
        //        }
        attHolidayRuleMapper.removeHolidayRuleById(rulePO.getId(), System.currentTimeMillis());
    }

    /**
     * 设置表头和假期map
     *
     * @param holidayTitleList 表头列表
     * @param holidayRulePOMap 假期map
     */
    @Override
    public void setHolidayTableTitle(List<AttHolidayRuleListResult> holidayTitleList, Map<Long, AttHolidayRulePO> holidayRulePOMap) {
        if (holidayTitleList == null) {
            holidayTitleList = new ArrayList<>();
        }
        if (holidayRulePOMap == null) {
            holidayRulePOMap = new HashMap<>();
        }
        List<AttHolidayRulePO> holidayRuleList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
        if (holidayRuleList != null && !holidayRuleList.isEmpty()) {
            for (AttHolidayRulePO rulePO : holidayRuleList) {
                holidayRulePOMap.put(rulePO.getId(), rulePO);
                AttHolidayRuleListResult ruleListResult = new AttHolidayRuleListResult();
                ruleListResult.setId(rulePO.getId());
                ruleListResult.setName(rulePO.getName());
                ruleListResult.setNotLimit(rulePO.getNotLimit());
                if (AttHolidayGrantWayEnum.OVERTIME_APPEND.name().equals(rulePO.getGrantWay())) {
                    ruleListResult.setUnit(AttConstants.HOUR);
                } else {
                    ruleListResult.setUnit(AttConstants.DAYS);
                }
                holidayTitleList.add(ruleListResult);
            }
        }
    }
}
