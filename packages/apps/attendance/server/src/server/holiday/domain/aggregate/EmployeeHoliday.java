package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.exception.OccupyHolidayBalanceException;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayBalance;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayBalanceChange;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/11/21 15:31
 * @description
 */
public class EmployeeHoliday {
    /**
     * 假期余额
     */
    @Getter
    private List<HolidayBalance> holidayBalances;

    /**
     * 员工
     */
    @Getter
    private Long employeeId;

    /**
     * 假期类型
     */
    @Getter
    private Set<Holiday.HolidayType> holidayTypes;

    public EmployeeHoliday(Long employeeId, List<HolidayBalance> holidayBalances, Set<Holiday.HolidayType> holidayTypes) {
        this.employeeId = employeeId;
        this.holidayBalances = holidayBalances;
        this.holidayTypes = holidayTypes;
    }

    public void appendChange(HolidayBalanceChange holidayBalanceChange) {
        long count = this.getHolidayTypes().stream().filter(holidayType -> holidayType == holidayBalanceChange.getHolidayType()).count();
        if (count <= 0) {
            throw new OccupyHolidayBalanceException("此员工无这种假期");
        }

        List<HolidayBalance> targetHolidayBalance = this.holidayBalances.stream()
                .filter(holidayBalance -> holidayBalance.getHolidayType() == holidayBalanceChange.getHolidayType())
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(targetHolidayBalance) || Objects.isNull(targetHolidayBalance.getFirst())) {
            HolidayBalance holidayBalance = new HolidayBalance(holidayBalanceChange.getBalance(), 0.0F, holidayBalanceChange.getHolidayType());
            targetHolidayBalance.add(holidayBalance);
            this.holidayBalances.add(holidayBalance);
        }
        // 只会存在一种假期类型
        targetHolidayBalance.getFirst().addBalanceChange(holidayBalanceChange);
    }

    /**
     * 预扣假期余额
     *
     * @param number      预扣余额数量
     * @param holidayType 预扣假期类型
     * @param businessKey 业务key （可选）
     * @param remark      备注 （可选）
     */
    public void occupy(@NotNull Float number,
                       @NotNull Holiday.HolidayType holidayType,
                       @Nullable String businessKey,
                       @Nullable String remark
                      ) {

        HolidayBalanceChange holidayBalanceChange = new HolidayBalanceChange();
        holidayBalanceChange.change(
                -1 * number,
                holidayType
                                   ).setBusinessKey(businessKey);
        holidayBalanceChange.setRemark("假期余额预扣减【" + number + "】");
        this.appendChange(holidayBalanceChange);

        HolidayBalance balance = getHolidayBalance(holidayType);
        if (balance.getRemainingBalance() < number) {
            throw new OccupyHolidayBalanceException("余额不足，预扣失败");
        }

        //余额扣减
        balance.setWithholdingBalance(balance.getWithholdingBalance() + number);
        balance.setRemainingBalance(balance.getRemainingBalance() - number);

        balance.addOccupyBalances(holidayBalanceChange);
    }

    /**
     * 实际扣减假期余额,未来支持按扣减编号实际扣减余额
     *
     * @param number      实际扣减数量
     * @param holidayType 假期类型
     * @param businessKey 业务key（可选）
     * @param remark      备注（可选）
     */
    public void reduce(@NotNull Float number,
                       @NotNull Holiday.HolidayType holidayType,
                       @Nullable String businessKey,
                       @Nullable String remark
                      ) {
        HolidayBalanceChange holidayBalanceChange = new HolidayBalanceChange();
        holidayBalanceChange.setRemark("假期余额实际扣减【" + number + "】");
        holidayBalanceChange.change(-1 * number, holidayType).setBusinessKey(businessKey);

        this.appendChange(holidayBalanceChange);
        HolidayBalance balance = getHolidayBalance(holidayType);
        balance.setWithholdingBalance(balance.getWithholdingBalance() - number);
        balance.addReduceBalance(holidayBalanceChange);

    }

    public HolidayBalance reduceRecord(@NotNull Float number,
                                       @NotNull Holiday.HolidayType holidayType,
                                       @Nullable String businessKey,
                                       @Nullable String remark
                                      ) {
        HolidayBalanceChange holidayBalanceChange = new HolidayBalanceChange();
        holidayBalanceChange.setRemark("假期余额实际扣减【" + number + "】");
        holidayBalanceChange.change(-1 * number, holidayType).setBusinessKey(businessKey);
        HolidayBalance balance = getHolidayBalance(holidayType);
        balance.addReduceBalance(holidayBalanceChange);
        return balance;
    }

    public void compensate(@NotNull Float number,
                           @NotNull Holiday.HolidayType holidayType
                          ) {
        HolidayBalanceChange holidayBalanceChange = new HolidayBalanceChange();
        holidayBalanceChange.setRemark("释放预扣余额【" + number + "】");
        holidayBalanceChange.change(number, holidayType);
        this.appendChange(holidayBalanceChange);
        HolidayBalance balance = getHolidayBalance(holidayType);
        balance.setRemainingBalance(balance.getRemainingBalance() + number);
        balance.setWithholdingBalance(balance.getWithholdingBalance() - number);
    }

    public void addRemainingBalance(@NotNull Float number,
                                    @NotNull Holiday.HolidayType holidayType,
                                    @Nullable String remark
                                   ) {
        HolidayBalanceChange holidayBalanceChange = new HolidayBalanceChange();
        holidayBalanceChange.setRemark("假期剩余余额增加【" + number + "】");
        this.appendChange(
                holidayBalanceChange.change(number, holidayType)
                         );
        HolidayBalance balance = getHolidayBalance(holidayType);
        balance.setRemainingBalance(balance.getRemainingBalance() + number);
        balance.addGrantBalance(holidayBalanceChange);
    }

    public void addExpireBalance(@NotNull Float number,
                                 @NotNull Holiday.HolidayType holidayType
                                ) {
        HolidayBalanceChange holidayBalanceChange = new HolidayBalanceChange();
        holidayBalanceChange.setRemark("假期剩余余额失效[" + number + "]");
        this.appendChange(
                holidayBalanceChange.change(number, holidayType)
                         );
        HolidayBalance balance = getHolidayBalance(holidayType);
        balance.setRemainingBalance(balance.getRemainingBalance() - number);
        balance.addExpiredBalance(holidayBalanceChange);
    }

    private HolidayBalance getHolidayBalance(Holiday.HolidayType holidayType) {
        List<HolidayBalance> targetHolidayBalance = this.holidayBalances.stream()
                .filter(holidayBalance -> holidayBalance.getHolidayType() == holidayType)
                .toList();
        // 只会存在一种假期类型
        return targetHolidayBalance.getFirst();
    }

}
