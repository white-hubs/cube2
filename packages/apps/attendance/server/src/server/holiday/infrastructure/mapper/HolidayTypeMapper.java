package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayTypeGrantConfigDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HolidayTypeMapper extends BaseMapper<HolidayTypePO> {

    @InterceptorIgnore(tenantLine = "on")
    @Select("select ht.`id` holidayTypeId,ht.`type` holidayType,ht.`code`,hgr.`grant_time` grantTime,hgr.`quota` grantQuota,ht.`tenant` from"
            +
            " `holiday_type` ht left join `holiday_grant_rule` hgr on ht.`grant_rule_id` = hgr.`id` where hgr.`quota` is not null"
    )
    List<HolidayTypeGrantConfigDTO> selectListIgnoreTenant();

    // @InterceptorIgnore(tenantLine = "on")
    @Select("select ht.`id` holidayTypeId,ht.`type` holidayType,ht.`code`,hgr.`grant_time` grantTime,hgr.`quota` grantQuota,ht.`tenant` from"
            +
            " `holiday_type` ht left join `holiday_grant_rule` hgr on ht.`grant_rule_id` = hgr.`id` where hgr.`quota` is not null"
    )
    List<HolidayTypeGrantConfigDTO> selectListWithGrantConfig();
}
