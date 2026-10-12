package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAwayBusinessFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAwayBusinessLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
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
 * 出差台账
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessLedgerCommandService extends ServiceImpl<AttAwayBusinessLedgerMapper, AttAwayBusinessLedgerPO> {

    private final UserClient userClient;

    /**
     * 出差审批通过添加请假台账
     *
     * @param param 出差单
     */
    public void addAwayBusinessLedger(AttAwayBusinessFromDTO param) {
        if (param == null || param.getAwayStartTime() == null || param.getAwayEndTime() == null ||
            param.getAwayStartTime().isAfter(param.getAwayEndTime())) {
            return;
        }
        String staffName = null;
        Long deptId = null;
        String deptName = null;
        UserDTO userDTO = userClient.getUser(param.getApplicantId());
        if (userDTO != null) {
            staffName = userDTO.getName();
            UserDTO.Organization organization = userDTO.getOrganization();
            if (organization != null) {
                deptId = organization.getId();
                deptName = organization.getName();
            }
        }
        // 相差天数
        long slipDay = DateUtils.between(param.getAwayStartTime(), param.getAwayEndTime(), ChronoUnit.DAYS, true) + AttConstants.oneLong;
        // 总出差时间
        long awayNumber = AttHolidayDomain.unitAndNumToLong(param.getAwayNumber(), AttConstants.HOUR, null);
        long dayNumber = (long) ((double) awayNumber / slipDay);
        LocalDateTime printDate = DateUtils.beginOfDay(param.getAwayStartTime());
        List<AttAwayBusinessLedgerPO> insertList = new ArrayList<>();
        do {
            AttAwayBusinessLedgerPO leaveLedgerPO = AttAwayBusinessLedgerPO.builder()
                    .id(IdWorker.getId())
                    .staffId(param.getApplicantId())
                    .staffName(staffName)
                    .deptId(deptId)
                    .deptName(deptName)
                    .awayDate(printDate)
                    .awayNumber(dayNumber)
                    .awayFromId(param.getId())
                    .build();
            insertList.add(leaveLedgerPO);
            printDate = DateUtils.offsetDay(printDate, AttConstants.oneInt);
        } while (param.getAwayEndTime().isAfter(printDate));

        this.saveBatch(insertList, AttConstants.bachSize);

    }

}
