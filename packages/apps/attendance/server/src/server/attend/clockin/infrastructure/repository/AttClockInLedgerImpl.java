package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.repository;

import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInLedgerDetailsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInLedgerRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInLedgerPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerDetailsResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInLedgerResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-26  13:56
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockInLedgerImpl extends ServiceImpl<AttClockInLedgerMapper, AttClockInLedgerPO> implements AttClockInLedgerRepository {
    private final AttClockInLedgerDetailsRepository clockInLedgerDetailsRepository;
    private final UserClient userClient;

    @Override
    public AttClockInLedgerPO getClockInLedger(Long userId, String ledgerDate) {
        // 某月的台账
        AttClockInLedgerPO ledger = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInLedgerPO>()
                                                                      .eq(AttClockInLedgerPO::getUserId, userId)
                                                                      .eq(AttClockInLedgerPO::getLedgerDate, ledgerDate)
                                                                      .last("limit 1"));
        if (ObjectUtils.isEmpty(ledger)) {
            // 如果没有该月的台账，需要先初始化台账
            AttClockInLedgerPO attClockInLedger = new AttClockInLedgerPO();
            attClockInLedger.setUserId(SecurityContextHolder.getPrincipal().getId());
            attClockInLedger.setUserName(SecurityContextHolder.getPrincipal().getName());
            attClockInLedger.setLedgerDate(ledgerDate);
            UserDTO user = userClient.getUser(attClockInLedger.getUserId());
            attClockInLedger.setOrgId(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getId());
            this.baseMapper.insert(attClockInLedger);
            clockInLedgerDetailsRepository.addLedgerDetails(attClockInLedger, ledgerDate);
            ledger = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInLedgerPO>()
                                                       .eq(AttClockInLedgerPO::getUserId, SecurityContextHolder.getPrincipal().getId())
                                                       .eq(AttClockInLedgerPO::getLedgerDate, ledgerDate));
        }
        return ledger;
    }

    @Override
    public Pagination<AttClockInLedgerResult> getClockInLedger(AttClockInLedgerPageParam param) {
        Page<AttClockInLedgerResult> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.getClockInLedger(page, param);
        Pagination<AttClockInLedgerResult> pagination = PagingUtils.convert(page, page.getRecords());
        return pagination;
    }

    @Override
    public List<AttClockInLedgerResult> getClockInLedgerList(AttClockInLedgerPageParam param) {
        return this.baseMapper.getClockInLedgerList(param);
    }

    @Override
    public List<AttClockInLedgerDetailsResult> getClockInLedgerDetails(Long id) {
        return this.baseMapper.getClockInLedgerDetails(id);
    }

    @Override
    public void addList(List<AttClockInLedgerPO> param) {
        this.saveBatch(param);
    }

    @Override
    public synchronized Boolean add(AttClockInLedgerPO param) {
        AttClockInLedgerPO ledger = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInLedgerPO>()
                                                                      .eq(AttClockInLedgerPO::getLedgerDate, param.getLedgerDate())
                                                                      .eq(AttClockInLedgerPO::getUserId, param.getUserId()));
        if (ObjectUtils.isEmpty(ledger)) {
            return this.save(param);
        } else {
            return false;
        }
    }

    @Override
    public Boolean updateOrgByUserId(Long userId, Long organizationId) {
        log.info("att:{}更新考勤打卡台账部门信息", userId);
        try {
            return super.update(new LambdaUpdateWrapper<AttClockInLedgerPO>()
                                        .set(AttClockInLedgerPO::getOrgId, organizationId)
                                        .eq(AttClockInLedgerPO::getUserId, userId)
                                        .eq(AttClockInLedgerPO::getLedgerDate, DateUtils.format(LocalDateTime.now(), "yyyy-MM")));
        } catch (Exception e) {
            log.error("att:更新考勤打卡台账部门信息异常", userId, e);
            return false;
        }
    }
}
