package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DisinfectionPlanMapper extends BaseMapper<DisinfectionPlan> {
    @InterceptorIgnore(tenantLine = "on")
    List<Integer> listAllTenantCodes();
}