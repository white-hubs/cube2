package com.guhecloud.cube.srv.gen.property.management.server.property.dao.assembly;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly.AssemblyClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AssemblyClassifyMapper extends BaseMapper<AssemblyClassify> {
    @Select("select name from assembly_classify where type=#{type} and status='NO_DELETE'")
    List<String> getAllByType(@Param("type") String type);
}
