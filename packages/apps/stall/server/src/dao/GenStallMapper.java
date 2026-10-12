package com.guhecloud.cube.srv.gen.stall.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.stall.server.entity.GenStall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface GenStallMapper extends BaseMapper<GenStall> {

    int updateBatchLeased(@Param("ids") Set<Long> ids, @Param("leased") Boolean leased, @Param("leaseType") Boolean leaseType);

    List<GenStall> listByAreas(@Param("areas") Collection<String> areas);
}
