package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceDeadlinePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * 假期余额有效期
 */
@Mapper
public interface AttHolidayBalanceDeadlineMapper extends BaseMapper<AttHolidayBalanceDeadlinePO> {

    /**
     * 根据id增量修改余额
     *
     * @param balance    余额
     * @param updateTime 修改时间
     * @param id         主键
     *
     * @return 修改数量
     */
    @Update("update att_holiday_balance_deadline set balance = balance + #{balance}, update_time = #{updateTime} where id = #{id}")
    Integer incrementalUpdateBalance(@Param("balance") Long balance, @Param("updateTime") LocalDateTime updateTime, @Param("id") Long id);
}
