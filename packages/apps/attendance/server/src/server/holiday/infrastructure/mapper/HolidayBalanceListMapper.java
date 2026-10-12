package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceListPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface HolidayBalanceListMapper extends BaseMapper<HolidayBalanceListPO> {

    void updateBatchNotIn(@Param("staffIds") Set<Long> staffIds, @Param("entity") HolidayBalanceListPO holidayBalanceListPO
                         );

    void updateBatchIn(@Param("staffIds") Set<Long> staffIds, @Param("entity") HolidayBalanceListPO holidayBalanceListPO);

    void updateAttendanceGroupId(@Param("staffIds") Set<Long> staffIds, @Param("attendanceGroupId") Long attendanceGroupId);

}
