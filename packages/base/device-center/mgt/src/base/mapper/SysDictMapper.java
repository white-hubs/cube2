package com.guhecloud.cube.iot.mgt.base.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.base.model.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    @InterceptorIgnore(tenantLine = "true")
    List<SysDict> queryAllByTenantId(@Param("tenantId") Long tenantId);

    @InterceptorIgnore(tenantLine = "true")
    List<SysDict> queryAllSysDictNoTenant();
}