package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.CnpArea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CnpAreaDataMigrationMapper extends BaseMapper<CnpArea> {

    @Select("select b.id,b.`name` from cnp_area a inner join area b on a.`name`=b.`name` where a.id=#{id}")
    CnpArea getAreaById(String id);

    @Select("select * from area where name=#{name} and parent_id=#{parentId}")
    CnpArea getAreaByParentId(@Param("name") String name, @Param("parentId") Long parentId);
}
