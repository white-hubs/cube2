package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/11/21 16:16
 * @description
 */
@Getter
public class HolidayBalanceChange {

    @Setter
    private String businessKey;

    /**
     * 余额
     */
    private Float balance;

    /**
     * 假期类型
     */
    private Holiday.HolidayType holidayType;

    @Setter
    private String remark;

    /**
     * 余额变化
     *
     * @param number 扣减的数量
     */
    public HolidayBalanceChange change(Float number, Holiday.HolidayType holidayType) {
        this.balance = number;
        this.holidayType = holidayType;
        return this;
    }
}
