package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;

import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/11/21 14:34
 * @description 假期仓储
 */
public interface HolidayRepository {

    /**
     * 保存假期
     *
     * @param holiday 假期
     *
     * @return 假期id
     */
    Long save(Holiday holiday);

    /**
     * 通过id获取假期
     *
     * @param holidayId 假期id
     *
     * @return 假期
     */
    Optional<Holiday> getById(Long holidayId);

    /**
     * 根据假期类型获取
     *
     * @param holidayType 假期类型
     *
     * @return 假期
     */
    Holiday getByType(Holiday.HolidayType holidayType);
}
