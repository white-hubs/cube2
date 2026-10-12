package com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.CommandDispatchRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.query.CommandRecordPageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommandDispatchRecordMapper extends BaseMapper<CommandDispatchRecordEntity> {

    void insertOrUpdateProgram(@Param("platform/database/entity") CommandDispatchRecordEntity entity);

    Page<CommandDispatchRecordEntity> getCommandDispatchRecordPage(Page<Object> convert, @Param("param") CommandRecordPageQuery param);
}
