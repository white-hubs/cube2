package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAskLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayBalanceMutationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalancePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;

import java.util.List;

/**
 * 假期余额
 */
public interface AttHolidayBalanceRepository {
    String occupyBalance(AttAskLeaveFromDTO param);

    void rollbackOccupyBalance(AttAskLeaveFromDTO param);

    void deductOccupyBalance(AttAskLeaveFromDTO param);

    AttHolidayBalancePO createIfAbsent(Long staffId, Long holidayRuleId, AttHolidayRulePO rule);

    void createIfAbsentBatch(List<AttHolidayBalanceMutationDTO> mutations);

    void replaceAvailableBalance(AttHolidayBalanceMutationDTO mutation);

    void incrementAvailableBalance(AttHolidayBalanceMutationDTO mutation);

    void applyBalanceMutations(List<AttHolidayBalanceMutationDTO> mutations);

    void addOperationLog(Long id, String content);
}
