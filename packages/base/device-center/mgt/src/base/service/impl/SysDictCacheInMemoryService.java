package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.iot.mgt.base.mapper.SysDictItemMapper;
import com.guhecloud.cube.iot.mgt.base.service.SysDictCacheService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemThinVo;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.comm.thread.IotNamedThreadFactory;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
@EventBusListen(order = 1)
public class SysDictCacheInMemoryService implements SysDictCacheService {

    private final ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();
    @Resource
    private SysDictItemMapper sysDictItemMapper;
    /**
     * 字典ID, 字典项ID, 字典项名称<br/>
     * 运营品台没有租户，为0
     */
    private Map<Long, Map<Long, DictItemThinVo>> dictItemMap = new HashMap<>();
    private ScheduledExecutorService scheduler;

    @Subscribe
    @AllowConcurrentEvents
    public void run(SysReadEvent event) throws Exception {
        IotNamedThreadFactory iotNamedThreadFactory = new IotNamedThreadFactory("inner-dict-cache", true);
        this.scheduler = Executors.newSingleThreadScheduledExecutor(iotNamedThreadFactory);
        scheduler.scheduleAtFixedRate(this::refreshDictItem, 30, 120, TimeUnit.SECONDS);

        refreshDictItem();
    }

    @PreDestroy
    private void destroy() {
        if (Objects.nonNull(scheduler)) {
            scheduler.shutdown();
        }
    }

    @Override
    public void refreshDictItem() {
        log.debug("refreshDictItem dictCode");

        Map<Long, Map<Long, DictItemThinVo>> dictMap = new HashMap<>();
        for (InnerDicEnum value : InnerDicEnum.values()) {
            if (!value.cacheSupport) {
                continue;
            }

            List<DictItemThinVo> dictItemThinVos = sysDictItemMapper.queryDictItemByIdThin(value.id);

            for (DictItemThinVo dictItemThinVo : dictItemThinVos) {

                Map<Long, DictItemThinVo> itemMap = dictMap.get(dictItemThinVo.getDictId());
                if (Objects.isNull(itemMap)) {
                    itemMap = new HashMap<>();
                    dictMap.put(dictItemThinVo.getDictId(), itemMap);
                }

                itemMap.put(dictItemThinVo.getId(), dictItemThinVo);
            }
        }

        writeLock.lock();
        try {
            this.dictItemMap = dictMap;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 层级查询字典值,从当前层级往上递归查询
     *
     * @param dictId 字典ID
     * @param itemId 字典项ID
     * @param split  分隔符
     *
     * @return 字典项值
     */
    @Override
    public String queryItemValWithPath(long dictId, long itemId, String split) {
        Optional<DictItemThinVo> dictItemThinVo = queryItem(dictId, itemId, true);

        //递归向上查询父item的code并进行拼接
        List<String> codeList = new ArrayList<>();
        while (dictItemThinVo.isPresent()) {
            codeList.add(dictItemThinVo.get().getItemValue());
            dictItemThinVo = queryItem(dictId, dictItemThinVo.get().getItemParentId(), true);
        }

        //翻转
        Collections.reverse(codeList);
        return String.join(split, codeList);
    }

    @Override
    public Optional<DictItemThinVo> queryItem(long dictId, long itemId, boolean forceFindInDb) {
        //如果可能加入直接重数据库查询
        Map<Long, DictItemThinVo> dictMap = dictItemMap.get(dictId);

        if (Objects.isNull(dictMap)) {
            return Optional.empty();
        }

        if (dictMap.get(itemId) == null) {
            if (!forceFindInDb) {
                return Optional.empty();
            }

            log.info("dictMap.get(itemId) is null" + dictId + "  " + itemId + ". try load from db");
            writeLock.lock();
            try {
                List<DictItemThinVo> dictItemThinVos = sysDictItemMapper.queryDictItemByDictIdAndItemIdThin(dictId, itemId);

                if (dictItemThinVos.isEmpty()) {
                    return Optional.empty();
                }

                DictItemThinVo dictItemThinVo = dictItemThinVos.get(0);
                dictMap.put(dictItemThinVo.getId(), dictItemThinVo);
            } finally {
                writeLock.unlock();
            }
        }

        return Optional.of(dictMap.get(itemId));
    }

    @Override
    public String dictItemLabelName(long dictId, Long itemId) {
        Optional<DictItemThinVo> dictItemThinVo = queryItem(dictId, itemId, true);
        return dictItemThinVo.orElseThrow(() -> new RuntimeException("字典项不存在")).getItemLabel();
    }

    private DictItemThinVo convert2Thin(DictItemVo dictItemVo) {
        DictItemThinVo dictItemThinVo = new DictItemThinVo();
        dictItemThinVo.setId(dictItemVo.getId());
        dictItemThinVo.setDictId(dictItemVo.getDictId());
        dictItemThinVo.setItemLabel(dictItemVo.getItemLabel());
        dictItemThinVo.setItemValue(dictItemVo.getItemValue());
        dictItemThinVo.setItemLevel(dictItemVo.getItemLevel());
        dictItemThinVo.setItemParentId(dictItemVo.getItemParentId());
        return dictItemThinVo;
    }

}
