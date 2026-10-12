package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;

/**
 * @author pengjian
 * @date 2022/11/21 16:20
 * @description
 */
public interface EmployeeHolidayRepository {

    /**
     * 保存员工假期
     *
     * @param employeeHoliday 员工假期
     *
     * @return id
     */
    Long save(EmployeeHoliday employeeHoliday);

    /**
     * 根据员工id获取员工假期
     *
     * @param employeeId 员工id
     *
     * @return 员工假期
     */
    EmployeeHoliday getByEmployeeId(Long employeeId);
}
