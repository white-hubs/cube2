package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/11/21 15:25
 * @description 假期余额
 */
@Getter
@AllArgsConstructor
public class HolidayBalance {
    /**
     * 假期剩余余额
     */
    @Setter
    private Float remainingBalance;

    /**
     * 预扣的余额
     */
    @Setter
    private Float withholdingBalance;

    /**
     * 假期类型
     */
    private Holiday.HolidayType holidayType;

    /**
     * 当前假期变动记录
     */
    private List<HolidayBalanceChange> currentHolidayBalanceChanges;

    /*
     *  当前发放明细
     */
    private List<HolidayBalanceChange> currentGrantBalances;

    /*
     * 当前失效明细
     */
    private List<HolidayBalanceChange> currentExpiredBalances;

    /**
     * 当前预扣明细
     */
    private List<HolidayBalanceChange> currentOccupyBalances;

    /**
     * 当前实扣明细
     */
    private List<HolidayBalanceChange> currentReduceBalances;

    public HolidayBalance(Float remainingBalance, Float withholdingBalance, Holiday.HolidayType holidayType) {
        this.holidayType = holidayType;
        this.remainingBalance = remainingBalance;
        this.withholdingBalance = withholdingBalance;
    }

    public void addBalanceChange(HolidayBalanceChange holidayBalanceChange) {
        if (CollectionUtils.isEmpty(this.currentHolidayBalanceChanges)) {
            this.currentHolidayBalanceChanges = Collections.singletonList(holidayBalanceChange);
        } else {
            this.currentHolidayBalanceChanges.add(holidayBalanceChange);
        }
    }

    public void addGrantBalance(HolidayBalanceChange holidayBalanceChange) {
        if (CollectionUtils.isEmpty(this.currentGrantBalances)) {
            this.currentGrantBalances = Collections.singletonList(holidayBalanceChange);
        } else {
            this.currentGrantBalances.add(holidayBalanceChange);
        }
    }

    public void addExpiredBalance(HolidayBalanceChange holidayBalanceChange) {
        if (CollectionUtils.isEmpty(this.currentExpiredBalances)) {
            this.currentExpiredBalances = Collections.singletonList(holidayBalanceChange);
        } else {
            this.currentExpiredBalances.add(holidayBalanceChange);
        }
    }

    public void addOccupyBalances(HolidayBalanceChange holidayBalanceChange) {
        if (CollectionUtils.isEmpty(this.currentOccupyBalances)) {
            this.currentOccupyBalances = Collections.singletonList(holidayBalanceChange);
        } else {
            this.currentOccupyBalances.add(holidayBalanceChange);
        }
    }

    public void addReduceBalance(HolidayBalanceChange holidayBalanceChange) {
        if (CollectionUtils.isEmpty(this.currentReduceBalances)) {
            this.currentReduceBalances = Collections.singletonList(holidayBalanceChange);
        } else {
            this.currentReduceBalances.add(holidayBalanceChange);
        }
    }
}
