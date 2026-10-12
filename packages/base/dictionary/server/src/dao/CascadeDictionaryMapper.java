package com.guhecloud.cube.base.dictionary.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dictionary.server.entity.CascadeDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CascadeDictionaryMapper extends BaseMapper<CascadeDictionary> {
    Integer getMaxLevel(@Param("itemCode") String itemCode, @Param("statusNameList") List<String> statusNameList);

    @InterceptorIgnore(tenantLine = "true")
    List<CascadeDictionary> getByTenant(@Param("tenantId") Long tenantId);
}
