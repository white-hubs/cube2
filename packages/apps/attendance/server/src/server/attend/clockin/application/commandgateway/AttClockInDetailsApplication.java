package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInLedgerDetailsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInDetailsParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-18  14:58
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockInDetailsApplication {
    private final AttAskLeaveLedgerMapper attAskLeaveLedgerMapper;
    private final AttClockInLedgerDetailsRepository clockInLedgerDetailsRepository;

    public String getClockInDetailsByUserId(AttClockInDetailsParam param) {
        if (ClockInEnum.NEGLECT_WORK.name().equals(param.getType()) || ClockInEnum.EARLY_CHECKED.name().equals(param.getType()) ||
            ClockInEnum.LATE_CHECKED.name().equals(param.getType())) {
            return clockInLedgerDetailsRepository.getLedgerDetailsAbnormalNumber(param).toString();
        } else {
            /* 获取请假统计 */
            List<AttAskLeaveLedgerPO> leaveLedgerPOList = attAskLeaveLedgerMapper.selectList(Wrappers.<AttAskLeaveLedgerPO>lambdaQuery()
                                                                                                     .ge(AttAskLeaveLedgerPO::getLeaveDate, param
                                                                                                             .getPeriod().getBegin())
                                                                                                     .le(AttAskLeaveLedgerPO::getLeaveDate, param
                                                                                                             .getPeriod().getEnd())
                                                                                                     .eq(AttAskLeaveLedgerPO::getStaffId, param.getUserId())
                                                                                                     .eq(AttAskLeaveLedgerPO::getHolidayCode, param.getType()));
            BigDecimal total = BigDecimal.ZERO;
            for (AttAskLeaveLedgerPO ex : leaveLedgerPOList) {
                total = total.add(new BigDecimal(AttHolidayDomain.longToUnit(ex.getLeaveNumber(), AttConstants.DAYS, ex.getDayOfHour(), false)));
            }
            return total.toString();
        }

    }
}
