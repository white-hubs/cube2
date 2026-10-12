package com.guhecloud.cube.iot.mgt.north.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemThinVo;
import com.guhecloud.cube.iot.mgt.north.model.SysDictItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NorthSysDictItemMapper extends BaseMapper<SysDictItem> {

    @InterceptorIgnore(tenantLine = "true")
    List<DictItemThinVo> queryThinsByDictId(@Param("dictId") long dictId);

    @InterceptorIgnore(tenantLine = "true")
    List<DictItemThinVo> queryThinsByDictIdAndItemId(@Param("dictId") long dictId, @Param("itemId") long itemId);

}