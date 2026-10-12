package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.CnpAreaPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.FireinspRecordItemPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TjAreaDataMigrationMapper extends BaseMapper<CnpAreaPO> {

    @Select("select b.id,b.`name` from cnp_area a inner join area b on a.`name`=b.`name` where a.id=#{id}")
    CnpAreaPO getAreaById(String id);

    @Select("select * from area where name=#{name} and parent_id=#{parentId}")
    CnpAreaPO getAreaByParentId(@Param("name") String name, @Param("parentId") Long parentId);

    @Select("select * from tj_dept")
    List<OrganizationDTO> getAllDep();

    @Select("select * from fireinsp_record_item")
    List<FireinspRecordItemPO> getAllRecordItem();

    @Select("select * from user")
    List<UserDTO> getAllUser();
}
