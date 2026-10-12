package com.guhecloud.cube.biz.mdata.server.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.merchant.dto.AuthenticationDTO;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Authentication;
import com.guhecloud.cube.biz.mdata.server.merchant.param.AuthenticationQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthenticationMapper extends BaseMapper<Authentication> {

    Page<AuthenticationDTO> page(Page<?> page, @Param("param") AuthenticationQueryParam param);

}
