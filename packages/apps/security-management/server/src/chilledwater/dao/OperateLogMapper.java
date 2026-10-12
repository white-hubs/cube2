package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.OperateLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.OperateLogParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.OperateLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OperateLogMapper extends BaseMapper<OperateLog> {
    Page<OperateLogVo> getOperateHardwareLogs(Page page, @Param("param") OperateLogParam pram);
}
