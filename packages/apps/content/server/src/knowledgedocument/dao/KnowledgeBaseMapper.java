package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KeyValueStringInteger;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.KnowledgeBase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 知识文档mapper
 */
@Mapper
public interface KnowledgeBaseMapper extends BaseMapper<KnowledgeBase> {

    List<KeyValueStringInteger> statKnowledgeGroupType();
}
