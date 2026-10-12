package com.guhecloud.cube.iot.mgt.base.service;


import com.guhecloud.cube.iot.mgt.base.vo.DictItemThinVo;

import java.util.Optional;

/**
 * 提供缓存服务
 */
public interface SysDictCacheService {

    void refreshDictItem();

    String queryItemValWithPath(long dictId, long itemId, String split);

    Optional<DictItemThinVo> queryItem(long dictId, long itemId, boolean forceFindInDb);

    String dictItemLabelName(long dictId, Long itemId);

}