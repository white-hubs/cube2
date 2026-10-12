package com.guhecloud.cube.biz.vmonitor.server.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.query.AuthQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthGroupPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthGroupMapper extends BaseMapper<AuthGroupPO> {

    Page<AuthGroupPO> selectPage(Page<AuthGroupPO> page, @Param("param") AuthQueryParam param);
}
