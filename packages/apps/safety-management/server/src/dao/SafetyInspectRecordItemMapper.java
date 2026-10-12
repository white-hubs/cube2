package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionItemStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyInspectRecordItemPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SafetyInspectRecordItemMapper extends BaseMapper<SafetyInspectRecordItemPO> {

    Integer selectInspectRecordCount(@Param("query") InspectionItemStatisticsQuery query);
}
