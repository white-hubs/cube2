package com.guhecloud.cube.srv.gen.visitor.management.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitAreaResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VisitAreaMapper extends BaseMapper<VisitAreaPO> {

    /**
     * 根据id查询设置的区域
     *
     * @param areaId 区域id
     *
     * @return 区域
     */
    @Select("select * from `visit_area` where `area_id` = #{areaId} limit 1")
    VisitAreaResult selectByAreaId(@Param("areaId") Long areaId);
}
