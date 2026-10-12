package com.guhecloud.cube.biz.mdata.server.area.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AreaGroupMapper extends BaseMapper<AreaGroup> {

    List<AreaGroup> getBySequences(@Param("sequences") List<String> sequences);
}
