package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 假期余额变动记录
 */
@Mapper
public interface AttHolidayBalanceRecordMapper extends BaseMapper<AttHolidayBalanceRecordPO> {

    @Select("select sum(change_number) from att_holiday_balance_record ${ew.customSqlSegment} ")
    Long balanceTotal(@Param(Constants.WRAPPER) Wrapper<AttHolidayBalanceRecordPO> wrapper);
}
