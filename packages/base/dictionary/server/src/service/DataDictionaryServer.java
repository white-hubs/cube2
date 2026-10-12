package com.guhecloud.cube.base.dictionary.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.dictionary.server.entity.DataDictionary;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.server.vo.DataDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface DataDictionaryServer extends IService<DataDictionary> {
    /**
     * 查询字典详情
     */
    DataDictionaryVO getDataDictionaryById(@PathVariable("id") Long id);

    /**
     * 批量查询字典信息
     */
    List<DataDictionaryVO> getDataDictionaryByIds(List<Long> ids);

    List<DataDictionaryVO> getDataDictionaryByIdsStr(String ids);

    List<DataDictionaryVO> getDataDictionaryByItem(DataDictionaryaramParam param);

    Pagination<DataDictionaryVO> getDataDictionaryByItemPage(DataDictionaryPageParam param);

    /**
     * 根据字典项编码+字典编码查询字典值
     */
    DataDictionaryVO getDataDictionaryByItemAndCode(DataDictionaryaramByItemAndCodeParam param);

    /**
     * 新增字典
     */
    DataDictionaryVO addDataDictionary(DataDictionaryAddParam param);

    /**
     * 修改字典
     */
    DataDictionaryVO updateDataDictionary(DataDictionaryParam param);

    /**
     * 删除字典
     */
    Boolean deleteDataDictionary(Long id);

    /**
     * 获取所有指定租户下的未删除字典项.
     *
     * @param tenantId 指定租户Id
     *
     * @return 字典项列表
     */
    List<DataDictionaryVO> getByTenant(Long tenantId);

    int batchRemoveByIds(List<Long> tenants);

    void batchAddOrUpdate(List<DataDictionary> dataDictionaryParams);

    /**
     * 获取指定租户下的字典项
     *
     * @param param 查询参数
     *
     * @return 字典项列表
     */
    List<DataDictionaryVO> adminGetDataDictionaryByItem(DataDictionaryaramParam param);
}
