package com.guhecloud.cube.base.iot.product.model.v3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 字典Mapper v3版本(增强版)
 * 操作iot_sys_dict表
 */
@Mapper
public interface V3DictMapper extends BaseMapper<SysDict> {

    /**
     * 根据字典编码查询字典
     *
     * @param dictCode 字典编码
     * @return 字典实体
     */
    SysDict selectByDictCode(@Param("dictCode") String dictCode);

    /**
     * 根据字典编码查询字典(包含字典项)
     *
     * @param dictCode 字典编码
     * @return 字典实体(含字典项列表)
     */
    SysDict selectByDictCodeWithItems(@Param("dictCode") String dictCode);
}
