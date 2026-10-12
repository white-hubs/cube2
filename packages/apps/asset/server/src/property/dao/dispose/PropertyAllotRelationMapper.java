package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyAllotRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PropertyAllotRelationMapper extends BaseMapper<PropertyAllotRelation> {

    Integer insertBatch(@Param("id") Long id, @Param("allotNumbers") String allotNumbers, @Param("propertyIds") List<Long> propertyIds);

    List<Long> getPropertyIds(@Param("allotNumbers") String allotNumbers);
}
