package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.KnowledgeType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KnowledgeTypeMapper extends BaseMapper<KnowledgeType> {
    List<KnowledgeType> getBySequences(@Param("sequences") List<String> sequences);
}
