package com.guhecloud.cube.base.dictionary.server.service;

import com.guhecloud.cube.base.dictionary.server.entity.DictionaryItem;
import com.guhecloud.cube.base.dictionary.server.enumeration.DataBaseOperateTypeEnum;
import com.guhecloud.cube.base.dictionary.server.enumeration.DictionaryTypeEnum;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-25  09:28
 * @Description: 修改通知
 * @Version: 1.0
 */
public interface ChangeNoticeService {
    void sendChangeNotice(DictionaryItem dictionaryItem, String code, String value, DictionaryTypeEnum type);

    void sendChangeNotice(DictionaryItem dictionaryItem, String code, String value, DictionaryTypeEnum type,
                          DataBaseOperateTypeEnum operateType
                         );

    void deleteNotice(String itemCode, String code, DictionaryTypeEnum type);

    void refreshCache(DictionaryItem dictionaryItem, String code, String value, DictionaryTypeEnum type);
}
