package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DisinfectionTaskMapper extends BaseMapper<DisinfectionTask> {
    @InterceptorIgnore(tenantLine = "on")
    List<Integer> listAllTenantCodes();
}