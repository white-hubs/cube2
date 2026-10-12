package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectScenePointPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.BasicScenePointVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.FireinspPointVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ShDictDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wql
 * @describe 现场点
 * @date 2023/6/5 15:51
 */
@Mapper
public interface InspectScenePointMapper extends BaseMapper<InspectScenePointPO> {
    @Select("select * from basic_scene_point")
    List<BasicScenePointVO> getCnpScenePoint();

    @Select("select * from fireinsp_point")
    List<FireinspPointVO> getSnScenePoint();

    @Select("select * from dict_data")
    List<ShDictDataVO> getSnDictData();

}
