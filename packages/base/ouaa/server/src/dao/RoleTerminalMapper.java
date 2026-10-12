package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleTerminal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleTerminalMapper extends BaseMapper<RoleTerminal> {

    List<RoleTerminal> listByTenant(@Param("tenantId") Long tenantId);
}
