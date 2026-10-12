package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.EmployeeHolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayBalance;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayBalanceChange;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.RangeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayUserRangMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceChangePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalancePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayDeductDetailPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayGrantPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayUserRangPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.service.HolidayBalanceChangeService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.service.HolidayDeductDetailService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.service.HolidayGrantService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/11/21 16:23
 * @description
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultEmployeeHolidayRepository implements EmployeeHolidayRepository {

    private final HolidayBalanceMapper balanceMapper;
    private final HolidayBalanceChangeService balanceChangeService;
    private final HolidayUserRangMapper userRangMapper;
    private final HolidayTypeMapper holidayTypeMapper;
    private final HolidayGrantService grantService;
    private final HolidayDeductDetailService holidayDeductDetailService;

    @Override
    public Long save(EmployeeHoliday employeeHoliday) {
        // 如果存在则更新
        List<HolidayBalance> holidayBalances = employeeHoliday.getHolidayBalances();
        // 一般只有一条，这里先循环查询，后期再优化
        holidayBalances.forEach(holidayBalance -> {
            HolidayBalancePO holidayBalancePO = balanceMapper.selectOne(
                    Wrappers.<HolidayBalancePO>lambdaQuery()
                            .eq(HolidayBalancePO::getHolidayType, holidayBalance.getHolidayType())
                            .eq(HolidayBalancePO::getUserId, employeeHoliday.getEmployeeId())
                                                                       );

            if (Objects.nonNull(holidayBalancePO)) {
                // 更新
                holidayBalancePO.setOccupyBalance(holidayBalance.getWithholdingBalance());
                holidayBalancePO.setRemainBalance(holidayBalance.getRemainingBalance());

                balanceMapper.updateById(holidayBalancePO);
            } else {
                // 新增
                holidayBalancePO = HolidayBalancePO.builder()
                        .holidayType(holidayBalance.getHolidayType())
                        .id(IdWorker.getId())
                        .occupyBalance(holidayBalance.getWithholdingBalance())
                        .remainBalance(holidayBalance.getRemainingBalance())
                        .userId(employeeHoliday.getEmployeeId())
                        .build();
                balanceMapper.insert(holidayBalancePO);
            }

            // 余额变动
            List<HolidayBalanceChange> currentHolidayBalanceChanges = holidayBalance.getCurrentHolidayBalanceChanges();
            if (CollectionUtils.isNotEmpty(currentHolidayBalanceChanges)) {
                List<HolidayBalanceChangePO> holidayBalanceChangePOS = new ArrayList<>(currentHolidayBalanceChanges.size());
                currentHolidayBalanceChanges.forEach(holidayBalanceChange -> {
                    HolidayBalanceChangePO holidayBalanceChangePO = HolidayBalanceChangePO.builder()
                            .value(holidayBalanceChange.getBalance())
                            .remark(holidayBalanceChange.getRemark())
                            .userId(employeeHoliday.getEmployeeId())
                            .holidayType(holidayBalance.getHolidayType())
                            .id(IdWorker.getId())
                            .build();
                    holidayBalanceChangePOS.add(holidayBalanceChangePO);
                });

                balanceChangeService.saveBatch(holidayBalanceChangePOS);
            }

            // 发放明细
            List<HolidayBalanceChange> currentGrantBalances = holidayBalance.getCurrentGrantBalances();
            if (CollectionUtils.isNotEmpty(currentGrantBalances)) {
                List<HolidayGrantPO> grantPOS = new ArrayList<>();
                currentGrantBalances.forEach(holidayBalanceChange -> {
                    // 增加发放记录
                    HolidayGrantPO holidayGrantPO = HolidayGrantPO.builder()
                            .balance(holidayBalanceChange.getBalance())
                            .holidayType(holidayBalanceChange.getHolidayType())
                            .userId(employeeHoliday.getEmployeeId())
                            .id(IdWorker.getId())
                            .remainingBalance(holidayBalanceChange.getBalance())
                            .build();
                    grantPOS.add(holidayGrantPO);
                });
                grantService.saveBatch(grantPOS);
            }

            /*
             *扣减余额明细
             * 先扣之前发放的假期，如果不够再依次扣下一个发放的假期
             *  如果之前的不够扣，就合并记录处理
             */
            List<HolidayBalanceChange> currentReduceBalances = holidayBalance.getCurrentReduceBalances();
            if (CollectionUtils.isNotEmpty(currentReduceBalances)) {
                List<HolidayDeductDetailPO> deductDetails = new ArrayList<>();
                currentReduceBalances.forEach(currentGrantBalance -> {
                    deductDetails.add(
                            HolidayDeductDetailPO.builder()
                                    .id(IdWorker.getId())
                                    .holidayType(currentGrantBalance.getHolidayType())
                                    .userId(employeeHoliday.getEmployeeId())
                                    .value(Math.abs(currentGrantBalance.getBalance()))
                                    .build());
                });
                holidayDeductDetailService.saveBatch(deductDetails);

                double allBalance = currentReduceBalances.stream().map(HolidayBalanceChange::getBalance).mapToDouble(v -> v).sum();
                // 获取之前发放记录剩余余额大于0的记录
                List<HolidayGrantPO> holidayGrantPOS = grantService.list(
                        Wrappers.<HolidayGrantPO>lambdaQuery()
                                .eq(HolidayGrantPO::getUserId, employeeHoliday.getEmployeeId())
                                .gt(HolidayGrantPO::getRemainingBalance, 0)
                                .orderByAsc(HolidayGrantPO::getCreateTime)
                                                                        );
                for (int i = 0; i < holidayGrantPOS.size(); i++) {
                    HolidayGrantPO holidayGrantPO = holidayGrantPOS.get(i);
                    if (holidayGrantPO.getRemainingBalance() >= allBalance) {
                        holidayGrantPO.setRemainingBalance(
                                holidayGrantPO.getRemainingBalance() - Float.parseFloat(String.valueOf(allBalance)));
                        //                        grantService.updateById(holidayGrantPO);
                    } else {
                        HolidayGrantPO next = holidayGrantPOS.get(i + 1);
                        float v = holidayGrantPO.getRemainingBalance() - Float.parseFloat(String.valueOf(allBalance));
                        holidayGrantPO.setRemainingBalance(0.0f);
                        grantService.updateById(holidayGrantPO);

                        next.setRemainingBalance(next.getRemainingBalance() - Math.abs(v));
                        grantService.updateById(next);
                    }
                }
            }
        });
        return null;
    }

    @Override
    public EmployeeHoliday getByEmployeeId(Long employeeId) {

        // 查询余额
        List<HolidayBalancePO> holidayBalancePOS = balanceMapper.selectList(
                Wrappers.<HolidayBalancePO>lambdaQuery()
                        .eq(HolidayBalancePO::getUserId, employeeId)
                                                                           );

        List<HolidayBalance> holidayBalances = new ArrayList<>(holidayBalancePOS.size());
        holidayBalancePOS.forEach(holidayBalancePO -> holidayBalances.add(
                new HolidayBalance(holidayBalancePO.getRemainBalance(), holidayBalancePO.getOccupyBalance(), holidayBalancePO.getHolidayType())
                                                                         ));

        /*
          如果是所有配置就直接加入，如果是部分配置并且不包含此员工就不加入
         */
        List<HolidayTypePO> holidayTypePOS = holidayTypeMapper.selectList(
                Wrappers.emptyWrapper()
                                                                         );
        Set<Holiday.HolidayType> holidayTypes = holidayTypePOS.stream().filter(v -> v.getStaffRange() == RangeEnum.All_STAFF)
                .map(HolidayTypePO::getType)
                .collect(Collectors.toSet());
        List<HolidayTypePO> partStaffRangeHolidays = holidayTypePOS.stream().filter(v -> v.getStaffRange() == RangeEnum.PART_STAFF)
                .toList();
        if (CollectionUtils.isNotEmpty(partStaffRangeHolidays)) {
            // 查询包含此员工的假期
            List<HolidayUserRangPO> partOfExistsHolidays = userRangMapper.selectList(
                    Wrappers.<HolidayUserRangPO>lambdaQuery()
                            .in(HolidayUserRangPO::getUserId, employeeId)
                                                                                    );
            if (CollectionUtils.isNotEmpty(partOfExistsHolidays)) {
                List<HolidayTypePO> holidayTypePOList = holidayTypeMapper.selectList(
                        Wrappers.<HolidayTypePO>lambdaQuery()
                                .in(HolidayTypePO::getId, partOfExistsHolidays.stream().map(HolidayUserRangPO::getHolidayTypeId)
                                        .collect(Collectors.toSet()))
                                                                                    );
                holidayTypes.addAll(holidayTypePOList.stream().map(HolidayTypePO::getType).toList());
            }
        }

        if (CollectionUtils.isEmpty(holidayBalancePOS)) {
            holidayTypes.forEach(holidayType -> {
                HolidayBalance holidayBalance = new HolidayBalance(0.0f, 0.0f, holidayType);
                holidayBalances.add(holidayBalance);
            });
        } else {
            Set<Holiday.HolidayType> types = new HashSet<>(holidayTypes);
            holidayBalancePOS.forEach(holidayBalancePO -> {
                boolean contains = types.contains(holidayBalancePO.getHolidayType());
                if (contains) {
                    types.remove(holidayBalancePO.getHolidayType());
                }
            });

            if (CollectionUtils.isNotEmpty(types)) {
                types.forEach(holidayType -> {
                    HolidayBalance holidayBalance = new HolidayBalance(0.0f, 0.0f, holidayType);
                    holidayBalances.add(holidayBalance);
                });
            }
        }

        return new EmployeeHoliday(
                employeeId,
                holidayBalances,
                holidayTypes
        );
    }
}
