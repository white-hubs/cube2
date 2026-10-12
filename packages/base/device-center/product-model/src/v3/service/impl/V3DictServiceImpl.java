package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3DictItemMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3DictMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDict;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDictItem;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3SysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 字典管理Service实现(增强版)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3DictServiceImpl extends ServiceImpl<V3DictMapper, SysDict> implements V3SysDictService {

    private final V3DictItemMapper dictItemMapper;

    @Override
    public SysDictItem getItemByCode(String dictCode, String itemCode) {
        log.info("根据字典编码和项编码查询字典项, dictCode={}, itemCode={}", dictCode, itemCode);

        SysDictItem item = dictItemMapper.selectByDictCodeAndItemCode(dictCode, itemCode);

        if (item == null) {
            log.warn("字典项不存在, dictCode={}, itemCode={}", dictCode, itemCode);
        }

        return item;
    }

    @Override
    // 忽略租户
    public String getItemCodeById(Long itemId, boolean ignoreTenant) {
        if (itemId == null) {
            return null;
        }

        SysDictItem item;
        if (ignoreTenant) {
            item = dictItemMapper.noTenantSelectById(itemId);
        } else {
            item = dictItemMapper.selectById(itemId);
        }

        if (item == null) {
            log.warn("字典项不存在, itemId={}", itemId);
            return null;
        }

        return item.getItemCode();
    }

}
