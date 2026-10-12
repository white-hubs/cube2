package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttLeavePostFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostLedgerPO;
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
 * 离岗单操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostLedgerCommandService extends ServiceImpl<AttLeavePostLedgerMapper, AttLeavePostLedgerPO> {

    private final UserClient userClient;

    /**
     * 离岗审批通过添加请假台账
     *
     * @param param 离岗单
     */
    public void addLeavePostLedger(AttLeavePostFromDTO param) {
        if (param == null || param.getPostStartTime() == null || param.getPostEndTime() == null ||
            param.getPostStartTime().isAfter(param.getPostEndTime())) {
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
        long slipDay = DateUtils.between(param.getPostStartTime(), param.getPostEndTime(), ChronoUnit.DAYS, true) + AttConstants.oneLong;
        // 总离岗时间
        long postNumber = AttHolidayDomain.unitAndNumToLong(param.getPostNumber(), AttConstants.HOUR, null);
        long dayNumber = (long) ((double) postNumber / slipDay);
        LocalDateTime printDate = DateUtils.beginOfDay(param.getPostStartTime());
        List<AttLeavePostLedgerPO> insertList = new ArrayList<>();
        do {
            AttLeavePostLedgerPO leaveLedgerPO = AttLeavePostLedgerPO.builder()
                    .id(IdWorker.getId())
                    .staffId(param.getApplicantId())
                    .staffName(staffName)
                    .deptId(deptId)
                    .deptName(deptName)
                    .postDate(printDate)
                    .postNumber(dayNumber)
                    .postFromId(param.getId())
                    .build();
            insertList.add(leaveLedgerPO);
            printDate = DateUtils.offsetDay(printDate, AttConstants.oneInt);
        } while (param.getPostEndTime().isAfter(printDate));

        this.saveBatch(insertList, AttConstants.bachSize);

    }
}
