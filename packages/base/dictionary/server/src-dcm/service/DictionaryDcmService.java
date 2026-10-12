package com.guhecloud.cube.base.dictionary.dcm.service;

import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.param.DictTypeCollectionParam;

import java.util.List;

public interface DictionaryDcmService {

    /**
     * 字典类型（映射到dictionary_item 表）的的申明式配置
     *
     * @param tenants   配置应用的租户范围
     * @param dictTypes 字典类型集合
     * @param isExists  TRUE: 确保存在(添加或更新), FALSE, 确保不存在(删除)
     */
    void ensureDictTypeExistent(List<String> tenants, List<DictTypeCollectionParam.DictTypeParam> dictTypes, boolean isExists);

    /**
     * 字典项集合(分别映射到SysDict, DataDict, CascadeDict)的申明式配置
     *
     * @param tenants                   配置应用的租户范围
     * @param dictEntryCollectionParams 传入的DCM字典项集合
     * @param isExists                  TRUE: 确保存在(添加或更新), FALSE, 确保不存在(删除)
     */
    void ensureDictEntryExistent(List<String> tenants, List<DictEntryCollectionParam> dictEntryCollectionParams, boolean isExists);

    /**
     * 获取指定租户或系统级的字典项目，如果是级联配置，前驱节点在前。
     *
     * @param tenantId 用作参考获取指定的租户下的配置. 当获取系统级的字典项目时, 指定null或空的tenantId; 当指定租户不存在时, 返回空集合.
     *
     * @return 有序排列的字典项目集合。
     */
    List<DictEntryCollectionParam> fetchRuntimeDictEntryCollections(String tenantId);

    /**
     * 获取系统级的字典类型
     *
     * @param tenantId 保留参数, 目前字典项目都是系统级的, 实际逻辑忽略掉该参数.
     *
     * @return
     */
    List<DictTypeCollectionParam.DictTypeParam> fetchRuntimeDictTypes(String tenantId);
}
