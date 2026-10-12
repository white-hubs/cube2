package com.guhecloud.cube.biz.meattrade.server.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("meattrade_DictionaryMapper")
public interface DictionaryMapper extends BaseMapper<Dictionary> {
}
