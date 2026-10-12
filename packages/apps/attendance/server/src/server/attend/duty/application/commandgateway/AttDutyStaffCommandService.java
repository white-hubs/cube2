package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper.AttDutyStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyStaffPO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 值班人员操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDutyStaffCommandService extends ServiceImpl<AttDutyStaffMapper, AttDutyStaffPO> {

    /**
     * 该月是否有值班人员
     *
     * @param yearMonth 年月
     *
     * @return true表示有
     */
    public boolean existDutyStaff(String yearMonth) {
        AttDutyStaffPO po = this.getOne(Wrappers.<AttDutyStaffPO>lambdaQuery().eq(AttDutyStaffPO::getDutyMonth, yearMonth)
                                                .last(AttConstants.limitOneSql));
        return po != null;
    }

    /**
     * 值班人员同步(重置)
     *
     * @param yearMonth 年月
     * @param cover     是否强行覆盖(为true才覆盖
     */
    public void dutyStaffSync(String yearMonth, Boolean cover) {
        // 当前日期
        LocalDateTime newDate = LocalDateTime.now();
        if (StringUtils.isBlank(yearMonth)) {
            // 入参为空就默认为当前月份
            yearMonth = DateUtils.format(newDate, "yyyy-MM");
        }
        if (!Boolean.TRUE.equals(cover) && this.existDutyStaff(yearMonth)) {
            log.warn("{}已有值班人员，不做覆盖", yearMonth);
            return;
        }
        // 前一个月份
        String prevMonth = DateUtils.format(DateUtils.offsetMonth(DateUtils.parseDate(
                yearMonth + "-01"), AttConstants.minusOne), "yyyy-MM");
        // 获取前一个月份的人员
        List<AttDutyStaffPO> prevList = this.list(Wrappers.<AttDutyStaffPO>lambdaQuery()
                                                          .eq(AttDutyStaffPO::getDutyMonth, prevMonth)
                                                          .groupBy(AttDutyStaffPO::getStaffId)
                                                 );
        if (prevList == null || prevList.isEmpty()) {
            log.warn("{}的前一个月没有值班人员，无需同步", yearMonth);
            return;
        }
        /* 开始同步人员 */
        String loginUser = "";
        Principal principal = SecurityContextHolder.getPrincipal();
        if (principal != null) {
            loginUser = principal.getAccount();
        }
        List<AttDutyStaffPO> insertList = new ArrayList<>();
        for (AttDutyStaffPO oldStaff : prevList) {
            AttDutyStaffPO insPo = AttDutyStaffPO.builder().build();
            BeanUtils.copyProperties(oldStaff, insPo);
            insPo.setId(IdWorker.getId());
            insPo.setStaffId(oldStaff.getStaffId());
            insPo.setStaffName(oldStaff.getStaffName());
            insPo.setDutyMonth(yearMonth);
            insPo.setTenant(oldStaff.getTenant());
            insPo.setDeleted(false);
            EntityAuditor.populateAudit(insPo);
            insertList.add(insPo);
        }
        this.remove(Wrappers.<AttDutyStaffPO>lambdaQuery().eq(AttDutyStaffPO::getDutyMonth, yearMonth));
        this.saveBatch(insertList, AttConstants.bachSize);
    }
}
