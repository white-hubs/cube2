package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayGrantPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.job.ClearUpExpiryHolidayTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface HolidayGrantMapper extends BaseMapper<HolidayGrantPO> {

    /**
     * 查询所有到期的发放记录
     * 忽略租户查询
     *
     * @return 到期的发放记录id集合
     */
    @InterceptorIgnore(tenantLine = "on")
    Set<HolidayGrantPO> selectExpiringListIgnoreTenant(@Param("cacheValues") Set<ClearUpExpiryHolidayTask.CacheValue> cacheValues);
}
