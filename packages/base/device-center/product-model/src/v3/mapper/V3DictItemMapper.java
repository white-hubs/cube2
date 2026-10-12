package com.guhecloud.cube.base.iot.product.model.v3.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDictItem;
import com.guhecloud.cube.base.iot.product.model.v3.util.DictCodeAndName;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典项Mapper v3版本(增强版)
 * 操作iot_sys_dict_item表
 */
@Mapper
public interface V3DictItemMapper extends BaseMapper<SysDictItem> {

    /**
     * 根据字典编码和项编码查询字典项
     *
     * @param dictCode 字典编码
     * @param itemCode 字典项编码
     * @return 字典项实体
     */
    SysDictItem selectByDictCodeAndItemCode(@Param("dictCode") String dictCode, @Param("itemCode") String itemCode);

    /**
     * 根据字典编码查询所有字典项
     *
     * @param dictCode 字典编码
     * @return 字典项列表
     */
    List<SysDictItem> selectByDictCode(@Param("dictCode") String dictCode);

    /**
     * 根据字典ID查询所有字典项
     *
     * @param dictId 字典ID
     * @return 字典项列表
     */
    List<SysDictItem> selectByDictId(@Param("dictId") Long dictId);

    /**
     * 根据字典ID和父级ID查询字典项
     *
     * @param dictId 字典ID
     * @param parentId 父级ID
     * @return 字典项列表
     */
    List<SysDictItem> selectByDictIdAndParentId(@Param("dictId") Long dictId, @Param("parentId") Long parentId);

    /**
     * 根据字典项ID查询字典项的编码和名称
     *
     * @param id 字典项ID
     * @return 字典项编码和名称
     */
    DictCodeAndName selectDictCodeAndNameById(@Param("id") Long id);


    /**
     * 根据字典项ID查询字典项的编码和名称
     *
     * @param id 字典项ID
     * @return 字典项编码和名称
     */
    @InterceptorIgnore(tenantLine = "true")
    DictCodeAndName noTenantSelectDictCodeAndNameById(@Param("id") Long id);

    /**
     * 根据字典项ID查询字典项
     *
     * @param id 字典项ID
     * @return 字典项实体
     */
    @InterceptorIgnore(tenantLine = "true")
    SysDictItem noTenantSelectById(@Param("id")Long id);

    /**
     * 根据字典ID查询所有字典项(无租户)
     * 用于查询事件类别等全局字典
     *
     * @param dictId 字典ID
     * @return 字典项列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysDictItem> noTenantSelectByDictId(@Param("dictId") Long dictId);

    /**
     * 根据字典ID和项编码查询字典项(无租户)
     * 用于查询协议等全局字典
     *
     * @param dictId   字典ID
     * @param itemCode 项编码
     * @return 字典项实体
     */
    @InterceptorIgnore(tenantLine = "true")
    SysDictItem noTenantSelectByDictIdAndItemCode(@Param("dictId") Long dictId, @Param("itemCode") String itemCode);
}
