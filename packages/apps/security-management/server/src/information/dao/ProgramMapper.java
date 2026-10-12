package com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.ProgramEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.ProgramPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.ProgramPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProgramMapper extends BaseMapper<ProgramEntity> {

    void insertOrUpdateProgram(List<ProgramEntity> dataList);

    Page<ProgramPageVo> getProgramPage(Page<Object> convert, @Param("param") ProgramPageParam param);
}
