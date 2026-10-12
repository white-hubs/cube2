package com.guhecloud.cube.base.dictionary.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dictionary.server.entity.DataDictionary;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.server.vo.DataDictionaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataDictionaryMapper extends BaseMapper<DataDictionary> {

    @InterceptorIgnore(tenantLine = "true")
    List<DataDictionary> getByTenant(@Param("tenantId") Long tenantId);

    @InterceptorIgnore(tenantLine = "true")
    List<DataDictionaryVO> selectListNotLimitTenant(@Param("param") DataDictionaryaramParam param,
                                                    @Param("statusNameList") List<String> statusNameList
                                                   );
}
