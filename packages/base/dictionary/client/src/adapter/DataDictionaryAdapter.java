package com.guhecloud.cube.base.dictionary.adapter;

import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/25 9:40
 * @Description: 通用字典缓存适配
 */
public interface DataDictionaryAdapter {

    /**
     * 根据字典项编码查询字典值
     */
    List<DataDictionaryDTO> getDataDictionaryByItem(@Valid DataDictionaryaramParam param);

    /**
     * 根据字典项编码查询字典值分页
     */
    Pagination<DataDictionaryDTO> getDataDictionaryByItemPage(@Valid DataDictionaryPageParam param);

    /**
     * 根据字典项编码+字典编码查询字典值
     */
    DataDictionaryDTO getDataDictionaryByItemAndCode(@Valid DataDictionaryaramByItemAndCodeParam param);

    /**
     * 新增字典
     */
    DataDictionaryDTO addDataDictionary(@Valid DataDictionaryAddParam param);

    /**
     * 修改字典
     */
    DataDictionaryDTO updateDataDictionary(@Valid DataDictionaryParam param);

    /**
     * 删除字典
     */
    Boolean deleteDataDictionary(Long id);

}
