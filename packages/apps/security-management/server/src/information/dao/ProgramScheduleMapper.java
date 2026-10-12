package com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.ProgramScheduleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.ProgramSchedulePageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProgramScheduleMapper extends BaseMapper<ProgramScheduleEntity> {

    void insertOrUpdateProgramSchedule(List<ProgramScheduleEntity> dataList);

    Page<ProgramSchedulePageVo> getPlayRecordPage(Page page, @Param("param") ProgramScheduleParam param);

    Integer getCountByParam(@Param("param") ProgramScheduleParam param);
}
