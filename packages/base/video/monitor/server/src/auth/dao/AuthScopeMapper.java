package com.guhecloud.cube.biz.vmonitor.server.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthScopePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface AuthScopeMapper extends BaseMapper<AuthScopePO> {

    List<AuthScopePO> selectAuthScopesByAuthTargetIds(@Param("targetIds") Collection<Long> authTargetIds);
}
