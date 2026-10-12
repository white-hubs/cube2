package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAskLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayBalanceMutationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository.AttHolidayBalanceRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceDeadlineMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceRecordMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceDeadlinePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalancePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ApprovalBizEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 假期余额
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDefaultHolidayBalanceRepository implements AttHolidayBalanceRepository {
    private final AttHolidayBalanceMapper attHolidayBalanceMapper;
    private final AttHolidayBalanceRecordMapper attHolidayBalanceRecordMapper;
    private final AttHolidayBalanceDeadlineMapper attHolidayBalanceDeadlineMapper;

    /**
     * 预扣(占用)假期余额
     *
     * @param param 入参
     */
    @Override
    @SuppressWarnings("unchecked")
    public String occupyBalance(AttAskLeaveFromDTO param) {
        StringBuffer result = new StringBuffer();
        long occupyBalance = AttHolidayDomain.unitAndNumToLong(param.getLeaveNumber(), AttConstants.HOUR, null);
        this.incrementAvailableBalance(AttHolidayBalanceMutationDTO.builder()
                                            .staffId(param.getApplicantId())
                                            .holidayRuleId(param.getHolidayId())
                                            .availableDelta(-occupyBalance)
                                            .occupyDelta(occupyBalance)
                                            .changeNumber(-occupyBalance)
                                            .staffName(param.getApplicantName())
                                            .operator(param.getApplicantName())
                                            .remark("请假申请发起预扣余额")
                                            .deleted(true)
                                            .build());
        // 将有效期的余额置为失效
        List<AttHolidayBalanceDeadlinePO> deadlineList = attHolidayBalanceDeadlineMapper.selectList(Wrappers
                                                                                                           .<AttHolidayBalanceDeadlinePO>lambdaQuery()
                                                                                                           .eq(AttHolidayBalanceDeadlinePO::getStaffId, param.getApplicantId())
                                                                                                           .eq(AttHolidayBalanceDeadlinePO::getHolidayRuleId, param.getHolidayId())
                                                                                                           .eq(AttHolidayBalanceDeadlinePO::getNormal, true)
                                                                                                           .orderByAsc(AttHolidayBalanceDeadlinePO::getDeadline, AttHolidayBalanceDeadlinePO::getBalance)
                                                                                                  );
        if (deadlineList != null) {
            long tempNum = occupyBalance;
            List<Long> updateList = new ArrayList<>();
            for (AttHolidayBalanceDeadlinePO deadlinePO : deadlineList) {
                if (tempNum == AttConstants.zeroInt) {
                    break;
                }
                if (deadlinePO.getBalance() <= tempNum) {
                    // 如果扣减额度比当前有效记录大就直接将记录记为失效（这里只是记录，遍历完了才更新数据库）
                    updateList.add(deadlinePO.getId());
                    result.append(deadlinePO.getId()).append(",");
                } else {
                    // 如果当前记录足够扣减剩下的占用额度，就将记录拆分成失效部分和有效部分
                    long insNum = deadlinePO.getBalance() - tempNum;
                    Long id = IdWorker.getId();
                    // 修改有效的余额
                    attHolidayBalanceDeadlineMapper.update(null, Wrappers.<AttHolidayBalanceDeadlinePO>lambdaUpdate()
                                                                   .eq(AttHolidayBalanceDeadlinePO::getId, deadlinePO.getId())
                                                                   .set(AttHolidayBalanceDeadlinePO::getBalance, insNum)
                                                          );
                    // 插入一条失效的余额
                    AttHolidayBalanceDeadlinePO insertPO = AttHolidayBalanceDeadlinePO.builder()
                            .id(id)
                            .staffId(deadlinePO.getStaffId())
                            .holidayRuleId(deadlinePO.getHolidayRuleId())
                            .balance(tempNum)
                            .deadline(deadlinePO.getDeadline())
                            .parentId(deadlinePO.getId())
                            .normal(false)
                            .build();
                    insertPO.setDeleted(false);
                    attHolidayBalanceDeadlineMapper.insert(insertPO);
                    result.append(id).append(",");
                    break;
                }
                tempNum -= deadlinePO.getBalance();
            }
            if (!updateList.isEmpty()) {
                // 整个记录失效
                attHolidayBalanceDeadlineMapper.update(null, Wrappers.<AttHolidayBalanceDeadlinePO>lambdaUpdate()
                                                               .in(AttHolidayBalanceDeadlinePO::getId, updateList)
                                                               .set(AttHolidayBalanceDeadlinePO::getNormal, false)
                                                      );
            }
        }
        if (!result.isEmpty()) {
            return result.substring(AttConstants.zeroInt, result.length() - 1);
        }
        return result.toString();
    }

    /**
     * 回滚预扣余额
     *
     * @param param 入参
     */
    @Override
    public void rollbackOccupyBalance(AttAskLeaveFromDTO param) {
        long rollbackBalance = AttHolidayDomain.unitAndNumToLong(param.getLeaveNumber(), AttConstants.HOUR, null);
        if (param.getHolidayBalance() != null && param.getHolidayBalance() >= AttConstants.zeroInt) { // 余额不为-1，表示限额，需要回滚余额
            this.incrementAvailableBalance(AttHolidayBalanceMutationDTO.builder()
                                                .staffId(param.getApplicantId())
                                                .holidayRuleId(param.getHolidayId())
                                                .availableDelta(rollbackBalance)
                                                .occupyDelta(-rollbackBalance)
                                                .changeNumber(rollbackBalance)
                                                .staffName(param.getApplicantName())
                                                .operator(param.getApplicantName())
                                                .remark("请假申请拒绝/回滚预扣余额")
                                                .deleted(true)
                                                .build());
            // 启用有效期的余额
            if (StringUtils.isNotBlank(param.getDeadlineIds())) {
                List<Long> idList = Arrays.stream(param.getDeadlineIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
                // 1.单个被占用的数据直接恢复
                attHolidayBalanceDeadlineMapper.update(null, Wrappers.<AttHolidayBalanceDeadlinePO>lambdaUpdate()
                                                               .in(AttHolidayBalanceDeadlinePO::getId, idList)
                                                               .isNull(AttHolidayBalanceDeadlinePO::getParentId)
                                                               .set(AttHolidayBalanceDeadlinePO::getNormal, true)
                                                      );
                // 2.拆分出来的占用数据需要合并到原来的数据中
                List<AttHolidayBalanceDeadlinePO> cccupyDeadlineList = attHolidayBalanceDeadlineMapper.selectList(Wrappers
                                                                                                                         .<AttHolidayBalanceDeadlinePO>lambdaQuery()
                                                                                                                         .in(AttHolidayBalanceDeadlinePO::getId, idList)
                                                                                                                         .isNotNull(AttHolidayBalanceDeadlinePO::getParentId)
                                                                                                                );
                if (cccupyDeadlineList != null) {
                    Map<Long, List<AttHolidayBalanceDeadlinePO>> groupMap = cccupyDeadlineList.stream()
                            .collect(Collectors.groupingBy(AttHolidayBalanceDeadlinePO::getParentId));
                    groupMap.forEach((parentId, list) -> {
                        Long totalOccupyBalance = list.stream().mapToLong(AttHolidayBalanceDeadlinePO::getBalance).sum();
                        attHolidayBalanceDeadlineMapper.incrementalUpdateBalance(totalOccupyBalance, LocalDateTime.now(), parentId);
                    });
                }
                // 3. 将已经合并的数据删除
                attHolidayBalanceDeadlineMapper.delete(Wrappers.<AttHolidayBalanceDeadlinePO>lambdaQuery()
                                                               .isNotNull(AttHolidayBalanceDeadlinePO::getParentId)
                                                               .in(AttHolidayBalanceDeadlinePO::getId, idList)
                                                      );
            }
        }
    }

    /**
     * 扣减占用余额
     *
     * @param param 入参对象
     */
    @Override
    public void deductOccupyBalance(AttAskLeaveFromDTO param) {
        long deductBalance = AttHolidayDomain.unitAndNumToLong(param.getLeaveNumber(), AttConstants.HOUR, null);
        if (param.getHolidayBalance() != null && param.getHolidayBalance() >= AttConstants.zeroInt) { // 余额不为-1，表示限额，需要扣减余额
            this.incrementAvailableBalance(AttHolidayBalanceMutationDTO.builder()
                                                .staffId(param.getApplicantId())
                                                .holidayRuleId(param.getHolidayId())
                                                .occupyDelta(-deductBalance)
                                                .changeNumber(-deductBalance)
                                                .staffName(param.getApplicantName())
                                                .operator(param.getApplicantName())
                                                .remark("请假申请通过扣除余额")
                                                .processInstanceId(param.getProcessInstanceId())
                                                .businessType(ApprovalBizEnum.LEAVE.name())
                                                .deleted(false)
                                                .build());
            // 删除无效期限余额
            if (StringUtils.isNotBlank(param.getDeadlineIds())) {
                List<Long> idList = Arrays.stream(param.getDeadlineIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
                attHolidayBalanceDeadlineMapper.deleteByIds(idList);
            }
        }
    }

    @Override
    public AttHolidayBalancePO createIfAbsent(Long staffId, Long holidayRuleId, AttHolidayRulePO rule) {
        AttHolidayBalancePO balancePO = attHolidayBalanceMapper.selectOne(Wrappers.<AttHolidayBalancePO>lambdaQuery()
                                                                                  .eq(AttHolidayBalancePO::getStaffId, staffId)
                                                                                  .eq(AttHolidayBalancePO::getHolidayRuleId, holidayRuleId)
                                                                                  .last(" limit 1 ")
                                                                         );
        if (balancePO != null) {
            return balancePO;
        }
        LocalDateTime now = LocalDateTime.now();
        AttHolidayBalancePO insertPO = AttHolidayBalancePO.builder()
                .staffId(staffId)
                .holidayRuleId(holidayRuleId)
                .notLimit(rule != null && Boolean.TRUE.equals(rule.getNotLimit()))
                .availableBalance(AttConstants.zeroLong)
                .occupyBalance(AttConstants.zeroLong)
                .updateRemark("初始化")
                .build();
        insertPO.setTenant(SessionContextHolder.getRequester().getTenant());
        insertPO.setDeleted(false);
        insertPO.setCreateTime(now);
        attHolidayBalanceMapper.insert(insertPO);
        return insertPO;
    }

    @Override
    public void createIfAbsentBatch(List<AttHolidayBalanceMutationDTO> mutations) {
        if (mutations == null || mutations.isEmpty()) {
            return;
        }
        Map<String, AttHolidayBalancePO> balanceMap = this.selectBalanceMap(mutations);
        LocalDateTime now = LocalDateTime.now();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Set<String> insertKeys = new HashSet<>();
        List<AttHolidayBalancePO> insertList = new ArrayList<>();
        for (AttHolidayBalanceMutationDTO mutation : mutations) {
            this.checkMutation(mutation);
            String key = balanceKey(mutation.getStaffId(), mutation.getHolidayRuleId());
            if (balanceMap.containsKey(key) || insertKeys.contains(key)) {
                continue;
            }
            AttHolidayBalancePO balancePO = AttHolidayBalancePO.builder()
                    .staffId(mutation.getStaffId())
                    .holidayRuleId(mutation.getHolidayRuleId())
                    .notLimit(Boolean.TRUE.equals(mutation.getNotLimit()))
                    .availableBalance(AttConstants.zeroLong)
                    .occupyBalance(AttConstants.zeroLong)
                    .updateRemark(StringUtils.isBlank(mutation.getRemark()) ? "初始化" : mutation.getRemark())
                    .build();
            balancePO.setTenant(tenant);
            balancePO.setDeleted(false);
            balancePO.setCreateTime(now);
            insertKeys.add(key);
            insertList.add(balancePO);
        }
        insertList.forEach(attHolidayBalanceMapper::insert);
    }

    @Override
    public void replaceAvailableBalance(AttHolidayBalanceMutationDTO mutation) {
        if (mutation != null && mutation.getTargetAvailableBalance() == null) {
            throw new ServiceException("固定修改余额必须指定目标余额");
        }
        this.applyBalanceMutations(List.of(mutation));
    }

    @Override
    public void incrementAvailableBalance(AttHolidayBalanceMutationDTO mutation) {
        if (mutation != null && mutation.getAvailableDelta() == null && mutation.getOccupyDelta() == null) {
            throw new ServiceException("增量修改余额必须指定余额变更值");
        }
        this.applyBalanceMutations(List.of(mutation));
    }

    @Override
    public void applyBalanceMutations(List<AttHolidayBalanceMutationDTO> mutations) {
        if (mutations == null || mutations.isEmpty()) {
            return;
        }
        Map<String, AttHolidayBalancePO> balanceMap = this.selectBalanceMap(mutations);
        List<AttHolidayBalancePO> insertBalanceList = new ArrayList<>();
        Map<String, AttHolidayBalancePO> updateBalanceMap = new HashMap<>();
        List<AttHolidayBalanceRecordPO> recordList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        for (AttHolidayBalanceMutationDTO mutation : mutations) {
            this.checkMutation(mutation);
            String key = balanceKey(mutation.getStaffId(), mutation.getHolidayRuleId());
            AttHolidayBalancePO balancePO = balanceMap.get(key);
            Long beforeBalance = mutation.getKnownBeforeBalance();
            if (beforeBalance == null) {
                beforeBalance = balancePO == null || balancePO.getAvailableBalance() == null ? AttConstants.zeroLong
                                                                                             : balancePO.getAvailableBalance();
            }
            Long currentOccupy = balancePO == null || balancePO.getOccupyBalance() == null ? AttConstants.zeroLong : balancePO.getOccupyBalance();
            Long newAvailable = this.calcAvailableBalance(beforeBalance, mutation);
            Long newOccupy = mutation.getOccupyDelta() == null ? currentOccupy
                                                               : Math.max(currentOccupy + mutation.getOccupyDelta(), AttConstants.zeroLong);

            if (balancePO == null) {
                balancePO = AttHolidayBalancePO.builder()
                        .staffId(mutation.getStaffId())
                        .holidayRuleId(mutation.getHolidayRuleId())
                        .notLimit(Boolean.TRUE.equals(mutation.getNotLimit()))
                        .availableBalance(newAvailable)
                        .occupyBalance(newOccupy)
                        .updateRemark(mutation.getRemark())
                        .build();
                balancePO.setTenant(tenant);
                balancePO.setDeleted(false);
                balancePO.setCreateTime(now);
                insertBalanceList.add(balancePO);
                balanceMap.put(key, balancePO);
            } else {
                balancePO.setAvailableBalance(newAvailable);
                balancePO.setOccupyBalance(newOccupy);
                balancePO.setUpdateRemark(mutation.getRemark());
                balancePO.setUpdateTime(now);
                updateBalanceMap.put(key, balancePO);
            }

            if (mutation.getChangeNumber() != null) {
                recordList.add(this.buildRecord(mutation, beforeBalance, now));
            }
        }

        insertBalanceList.forEach(attHolidayBalanceMapper::insert);
        updateBalanceMap.values().forEach(attHolidayBalanceMapper::updateById);
        recordList.forEach(attHolidayBalanceRecordMapper::insert);
    }

    /**
     * 增量修改余额
     *
     * @param staffId       人员id
     * @param holidayRuleId 规则id
     * @param available     变更可用额度 负数为减（空不修改
     * @param occupy        变更占用额度 负数为减（空不修改
     * @param updateRemark  修改备注
     */
    @Synchronized
    public void incrementalUpdateBalance(Long staffId, Long holidayRuleId, Long available, Long occupy, String updateRemark) {
        this.applyBalanceMutations(List.of(AttHolidayBalanceMutationDTO.builder()
                                           .staffId(staffId)
                                           .holidayRuleId(holidayRuleId)
                                           .availableDelta(available)
                                           .occupyDelta(occupy)
                                           .remark(updateRemark)
                                           .build()));
    }

    private Map<String, AttHolidayBalancePO> selectBalanceMap(List<AttHolidayBalanceMutationDTO> mutations) {
        Set<Long> staffIds = new HashSet<>();
        Set<Long> holidayRuleIds = new HashSet<>();
        Set<String> keys = new HashSet<>();
        for (AttHolidayBalanceMutationDTO mutation : mutations) {
            if (mutation == null || mutation.getStaffId() == null || mutation.getHolidayRuleId() == null) {
                continue;
            }
            staffIds.add(mutation.getStaffId());
            holidayRuleIds.add(mutation.getHolidayRuleId());
            keys.add(balanceKey(mutation.getStaffId(), mutation.getHolidayRuleId()));
        }
        Map<String, AttHolidayBalancePO> result = new HashMap<>();
        if (staffIds.isEmpty() || holidayRuleIds.isEmpty()) {
            return result;
        }
        List<AttHolidayBalancePO> balanceList = attHolidayBalanceMapper.selectList(Wrappers.<AttHolidayBalancePO>lambdaQuery()
                                                                                           .in(AttHolidayBalancePO::getStaffId, staffIds)
                                                                                           .in(AttHolidayBalancePO::getHolidayRuleId, holidayRuleIds)
                                                                                  );
        if (balanceList == null) {
            return result;
        }
        for (AttHolidayBalancePO balancePO : balanceList) {
            String key = balanceKey(balancePO.getStaffId(), balancePO.getHolidayRuleId());
            if (keys.contains(key)) {
                result.put(key, balancePO);
            }
        }
        return result;
    }

    private void checkMutation(AttHolidayBalanceMutationDTO mutation) {
        if (mutation == null || mutation.getStaffId() == null || mutation.getHolidayRuleId() == null) {
            throw new ServiceException("假期余额变更参数缺少人员或假期规则");
        }
        if (mutation.getAvailableDelta() != null && mutation.getTargetAvailableBalance() != null) {
            throw new ServiceException("假期余额增量和固定值不能同时指定");
        }
    }

    private Long calcAvailableBalance(Long beforeBalance, AttHolidayBalanceMutationDTO mutation) {
        if (mutation.getTargetAvailableBalance() != null) {
            return Math.max(mutation.getTargetAvailableBalance(), AttConstants.zeroLong);
        }
        if (mutation.getAvailableDelta() != null) {
            return Math.max(beforeBalance + mutation.getAvailableDelta(), AttConstants.zeroLong);
        }
        return beforeBalance;
    }

    private AttHolidayBalanceRecordPO buildRecord(AttHolidayBalanceMutationDTO mutation, Long beforeBalance, LocalDateTime now) {
        AttHolidayBalanceRecordPO recordPO = AttHolidayBalanceRecordPO.builder()
                .id(IdWorker.getId())
                .staffId(mutation.getStaffId())
                .staffName(mutation.getStaffName())
                .holidayRuleId(mutation.getHolidayRuleId())
                .addOrDeduct(mutation.getChangeNumber() >= AttConstants.zeroLong)
                .changeNumber(mutation.getChangeNumber())
                .beforeBalance(beforeBalance)
                .deadlineId(mutation.getDeadlineId())
                .deadline(mutation.getDeadline())
                .effective(mutation.getEffective())
                .remark(mutation.getRemark())
                .updateName(mutation.getOperator())
                .processInstanceId(mutation.getProcessInstanceId())
                .businessType(mutation.getBusinessType())
                .build();
        recordPO.setCreateTime(now);
        if (mutation.getDeleted() != null) {
            recordPO.setDeleted(mutation.getDeleted());
        }
        if (StringUtils.isNotBlank(mutation.getCreateBy())) {
            recordPO.setCreateBy(mutation.getCreateBy());
        }
        return recordPO;
    }

    private static String balanceKey(Long staffId, Long holidayRuleId) {
        return staffId + "_" + holidayRuleId;
    }

    /**
     * 修改余额
     *
     * @param id      主键
     * @param content 内容
     */
    @Override
    @LogRecord(success = "{{#content}}"
            , subType = "HOLIDAY_BALANCE", operationType = "UPDATE", subTypeName = "假期余额", type = "ATTENDANCE", typeName = "考勤系统",
               bizNo = "{{#id}}"
    )
    public void addOperationLog(Long id, String content) {

    }
}
