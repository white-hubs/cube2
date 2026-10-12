package com.guhecloud.cube.base.dictionary.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dictionary.server.entity.CascadeTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CascadeTreeMapper extends BaseMapper<CascadeTree> {
    Integer updateNextIds(@Param("supDictionaryId") Long supDictionaryId, @Param("dictionaryId") String dictionaryId);
}
