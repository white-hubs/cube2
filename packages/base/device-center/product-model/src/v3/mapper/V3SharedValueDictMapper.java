package com.guhecloud.cube.base.iot.product.model.v3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.SharedValueDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 共享值字典Mapper v3版本(新增功能)
 * 操作iot_shared_value_dict_v3表
 */
@Mapper
public interface V3SharedValueDictMapper extends BaseMapper<SharedValueDict> {

    /**
     * 根据字典类型查询共享值列表
     *
     * @param dictType 字典类型(unit_symbol/category_name/brand_name等)
     * @return 共享值列表
     */
    List<String> selectValuesByType(@Param("dictType") String dictType);

    /**
     * 根据字典类型和值查询记录
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 共享值字典实体
     */
    SharedValueDict selectByTypeAndValue(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 批量插入或更新共享值(增加使用次数)
     *
     * @param records 共享值记录列表
     * @return 影响行数
     */
    int batchInsertOrUpdate(@Param("records") List<SharedValueDict> records);

    /**
     * 根据字典类型搜索共享值(模糊匹配)
     *
     * @param dictType 字典类型
     * @param keyword 搜索关键词
     * @param limit 返回数量限制
     * @return 共享值列表
     */
    List<String> searchValuesByTypeAndKeyword(@Param("dictType") String dictType,
                                               @Param("keyword") String keyword,
                                               @Param("limit") Integer limit);
}
