package com.guhecloud.cube.biz.mdata.server.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {

    List<GoodsCategory> getBySequences(@Param("sequences") List<String> sequences);

    List<GoodsCategory> getThirdLevel(@Param("sequences") List<String> sequences, @Param("level") Integer level);

    List<GoodsCategory> getThirdLevelByIds(@Param("ids") List<Long> sequences, @Param("level") Integer level);

    List<String> getIdsByParentIds(@Param("list") List<String> list);

    List<String> getNameByIds(@Param("list") List<String> list);

}
