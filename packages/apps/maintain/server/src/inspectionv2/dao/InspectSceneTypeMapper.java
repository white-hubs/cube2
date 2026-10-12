package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectSceneTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.BasicObjectTypeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionAreaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wql
 * @describe 现场点类型
 * @date 2023/6/5 15:51
 */
@Mapper
public interface InspectSceneTypeMapper extends BaseMapper<InspectSceneTypePO> {
    @Select("select id,object_type_name from basic_object_type")
    List<BasicObjectTypeVO> getAllCnpObjectType();

    @Select("select Id,Code,Name from inspection_area where Id=#{id}")
    InspectionAreaVO getAllInspectionArea(@Param("id") Long id);

    @Select("select Id,Code,Name from sh_area where Id=#{id}")
    InspectionAreaVO getAllShInspectionArea(@Param("id") String id);
}
