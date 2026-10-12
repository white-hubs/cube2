package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;

import java.util.List;
import java.util.Map;

public interface AttHolidayRuleRepository {

    void setHolidayTableTitle(List<AttHolidayRuleListResult> holidayTitleList, Map<Long, AttHolidayRulePO> holidayRulePOMap);

    void addHolidayRule(AttHolidayRulePO holidayRulePO);

    void editHolidayRule(AttHolidayRulePO oldRulePo, AttHolidayRulePO newRulePo, AttHolidayRulePO param);

    void removeHolidayRule(AttHolidayRulePO rulePO);
}
