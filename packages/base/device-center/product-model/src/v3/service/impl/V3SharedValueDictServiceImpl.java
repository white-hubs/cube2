package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3SharedValueDictMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.SharedValueDict;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3SharedValueDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 共享值字典Service实现
 * 自动收集和提供常用值的输入提示
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3SharedValueDictServiceImpl extends ServiceImpl<V3SharedValueDictMapper, SharedValueDict> implements V3SharedValueDictService {

    private final V3SharedValueDictMapper dictMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectValue(String dictType, String dictValue, String dictLabel) {
        if (!StringUtils.hasText(dictValue)) {
            return;
        }

        log.debug("收集共享值, dictType={}, dictValue={}", dictType, dictValue);

        // 检查是否已存在
        LambdaQueryWrapper<SharedValueDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SharedValueDict::getDictType, dictType)
                .eq(SharedValueDict::getDictValue, dictValue);

        SharedValueDict existing = getOne(wrapper);

        if (existing != null) {
            // 已存在,增加使用次数
            existing.setUseCount(existing.getUseCount() + 1);
            updateById(existing);
        } else {
            // 不存在,创建新记录
            SharedValueDict newDict = new SharedValueDict();
            newDict.setDictType(dictType);
            newDict.setDictValue(dictValue);
            newDict.setDictLabel(StringUtils.hasText(dictLabel) ? dictLabel : dictValue);
            newDict.setUseCount(1);
            newDict.setStatus(1);
            newDict.setSortOrder(0);

            save(newDict);
            log.info("新增共享值, dictType={}, dictValue={}", dictType, dictValue);
        }
    }

    @Override
    public List<SharedValueDict> listByType(String dictType) {
        log.debug("查询共享值列表, dictType={}", dictType);

        LambdaQueryWrapper<SharedValueDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SharedValueDict::getDictType, dictType)
                .eq(SharedValueDict::getStatus, 1)
                .orderByDesc(SharedValueDict::getUseCount)
                .orderByAsc(SharedValueDict::getSortOrder)
                .last("LIMIT 100");

        return list(wrapper);
    }

    @Override
    public List<SharedValueDict> searchValues(String dictType, String keyword) {
        log.debug("搜索共享值, dictType={}, keyword={}", dictType, keyword);

        LambdaQueryWrapper<SharedValueDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SharedValueDict::getDictType, dictType)
                .eq(SharedValueDict::getStatus, 1);

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(SharedValueDict::getDictValue, keyword)
                    .or()
                    .like(SharedValueDict::getDictLabel, keyword));
        }

        wrapper.orderByDesc(SharedValueDict::getUseCount)
                .orderByAsc(SharedValueDict::getSortOrder)
                .last("LIMIT 50");

        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseUseCount(String dictType, String dictValue) {
        if (!StringUtils.hasText(dictValue)) {
            return;
        }

        LambdaQueryWrapper<SharedValueDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SharedValueDict::getDictType, dictType)
                .eq(SharedValueDict::getDictValue, dictValue);

        SharedValueDict dict = getOne(wrapper);

        if (dict != null) {
            dict.setUseCount(dict.getUseCount() + 1);
            updateById(dict);
        }
    }
}
