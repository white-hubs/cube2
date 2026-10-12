package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAskLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttLegalHolidayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 请假台账操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveLedgerCommandService extends ServiceImpl<AttAskLeaveLedgerMapper, AttAskLeaveLedgerPO> {

    private final AttHolidayRuleMapper attHolidayRuleMapper;
    private final UserClient userClient;

    /**
     * 请假申请通过后添加请假台账
     *
     * @param param 请假单
     */
    public void addAskLeaveLedger(AttAskLeaveFromDTO param) {
        if (param == null || param.getHolidayId() == null || param.getLeaveStartTime() == null || param.getLeaveEndTime() == null ||
            param.getLeaveStartTime().isAfter(param.getLeaveEndTime())) {
            return;
        }
        int dayOfHour = AttConstants.dayOfHour;
        boolean intoAbsence = false;
        String holidayType = AttLegalHolidayEnum.OTHER_LEAVE.name();
        AttHolidayRulePO holidayRulePO = attHolidayRuleMapper.selectById(param.getHolidayId());
        if (holidayRulePO != null) {
            if (holidayRulePO.getDayOfHour() != null) {
                dayOfHour = holidayRulePO.getDayOfHour();
            }
            if (StringUtils.isNotBlank(holidayRulePO.getHolidayType())) {
                holidayType = holidayRulePO.getHolidayType();
            }
            if (Boolean.TRUE.equals(holidayRulePO.getIntoAbsence())) {
                intoAbsence = true;
            }
        }
        // 相差天数
        long slipDay = DateUtils.between(param.getLeaveStartTime(), param.getLeaveEndTime(), ChronoUnit.DAYS, true) + AttConstants.oneLong;
        // 总请假时间
        long leaveNumber = AttHolidayDomain.unitAndNumToLong(param.getLeaveNumber(), AttConstants.HOUR, null);
        long dayNumber = (long) ((double) leaveNumber / slipDay);
        // 人员信息
        UserDTO userDTO = userClient.getUser(param.getApplicantId());
        LocalDateTime printDate = DateUtils.beginOfDay(param.getLeaveStartTime());
        List<AttAskLeaveLedgerPO> insertList = new ArrayList<>();
        do {
            AttAskLeaveLedgerPO leaveLedgerPO = AttAskLeaveLedgerPO.builder()
                    .id(IdWorker.getId())
                    .staffId(param.getApplicantId())
                    .leaveDate(printDate)
                    .holidayId(param.getHolidayId())
                    .leaveNumber(dayNumber)
                    .leaveFromId(param.getId())
                    .holidayCode(holidayType)
                    .dayOfHour(dayOfHour)
                    .intoAbsence(intoAbsence)
                    .build();
            insertList.add(leaveLedgerPO);
            if (userDTO != null) {
                leaveLedgerPO.setStaffName(userDTO.getName());
                UserDTO.Organization organization = userDTO.getOrganization();
                if (organization != null) {
                    leaveLedgerPO.setDeptId(organization.getId());
                    leaveLedgerPO.setDeptName(organization.getName());
                }
            }
            printDate = DateUtils.offsetDay(printDate, AttConstants.oneInt);
        } while (param.getLeaveEndTime().isAfter(printDate));

        this.saveBatch(insertList, AttConstants.bachSize);
    }
}
