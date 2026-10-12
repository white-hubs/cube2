package com.guhecloud.cube.base.dictionary.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.dictionary.server.entity.DictionaryItem;
import com.guhecloud.cube.base.dictionary.server.param.DictionaryItemParam;
import com.guhecloud.cube.base.dictionary.server.vo.DictionaryItemVO;

import java.util.List;

public interface DictionaryItemService extends IService<DictionaryItem> {
    DictionaryItemVO getDictionaryItemByCode(String code);

    List<DictionaryItemVO> getDictionaryItemAll();

    DictionaryItemVO addDictionaryItem(DictionaryItemParam param);

    /**
     * 批量更新dictionaryItem
     *
     * @param dictionaryItemParams 字典项数据
     *
     * @return boolean 更新结果
     */
    boolean batchAddOrUpdate(List<DictionaryItemParam> dictionaryItemParams);

    /**
     * 批量基于字典code删除
     *
     * @param codes 字典code
     *
     * @return int 删除数量
     */
    int batchRemoveByCode(List<String> codes);

    /**
     * 根据字典项刷新缓存
     *
     * @param code 字典code
     */
    void refreshDictionaryItemCache(String code);
}
