package com.guhecloud.cube.base.dictionary.adapter;

import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryaramParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/25 9:40
 * @Description: 系统字典缓存适配
 */

public interface SysDictionaryAdapter {

    /**
     * 根据字典编码和字典项编码查询字典详情
     *
     * @param itemCode
     * @param code
     *
     * @return
     */
    SysDictionaryDTO getSysDictionaryByCodeAndItemCode(String itemCode, String code);

    /**
     * 根据字典项编码查询字典值
     */
    List<SysDictionaryDTO> getSysDictionaryByItem(@Valid SysDictionaryaramParam param);

    /**
     * 根据字典项编码查询字典值分页
     */
    Pagination<SysDictionaryDTO> getSysDictionaryByItemPage(@Valid SysDictionaryPageParam param);

    /**
     * 新增字典
     */
    SysDictionaryDTO addSysDictionary(@Valid SysDictionaryAddParam param);

    /**
     * 修改字典
     */
    SysDictionaryDTO updateSysDictionary(@Valid SysDictionaryParam param);

    /**
     * 删除字典
     */
    Boolean deleteSysDictionary(Long id);

}
