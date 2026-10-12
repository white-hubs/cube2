package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.exception;

import com.guhecloud.platform.base.standard.exception.ServiceException;

/**
 * @author pengjian
 * @date 2022/11/21 15:51
 * @description 假期余额占用失败
 */
public class OccupyHolidayBalanceException extends ServiceException {
    public OccupyHolidayBalanceException(String message) {
        super(message);
    }
}
