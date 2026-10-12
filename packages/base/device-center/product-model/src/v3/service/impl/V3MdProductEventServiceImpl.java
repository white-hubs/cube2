package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.iot.product.model.v3.enums.DictEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.EntityTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.OperationTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.guard.DeletionGuardManager;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductEventMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductEvent;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDictItem;
import com.guhecloud.cube.base.iot.product.model.v3.param.EventSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3DictItemService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductEventService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductModelVersionService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3SystemDictCacheService;
import com.guhecloud.cube.base.iot.product.model.v3.util.EntityBuilder;
import com.guhecloud.cube.base.iot.product.model.v3.util.EventConfigConverter;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductEventVO;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 事件管理Service实现 v3版本
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3MdProductEventServiceImpl extends ServiceImpl<V3MdProductEventMapper, MdProductEvent> implements V3MdProductEventService {

    private final V3MdProductEventMapper eventMapper;
    private final V3SystemDictCacheService systemDictCacheService;
    private final V3ProductModelVersionService versionService;
    private final DeletionGuardManager guardManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveEvent(EventSaveParam param) {
        log.info("开始创建事件, prodId={}, eventCode={}", param.getProdId(), param.getEventCode());

        // 1. 校验event_code在产品下的唯一性
        MdProductEvent existingEvent = getByProdIdAndEventCode(param.getProdId(), param.getEventCode());
        if (existingEvent != null) {
            throw new InvalidArgumentException("事件编码在该产品下已存在: " + param.getEventCode());
        }

        // 2. 参数校验
        validateEvent(param);

        // 3. 构建事件实体
        MdProductEvent event = EntityBuilder.buildEvent(param);

        // 4. 保存事件
        save(event);

        log.info("事件创建成功, eventId={}, eventCode={}", event.getId(), param.getEventCode());

        // 5. 触发版本快照生成
        try {
            versionService.createSnapshot(param.getProdId(), OperationTypeEnum.UPDATE.getCode(), "创建事件: " + param.getName());
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", param.getProdId(), e);
        }

        return event.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEvent(Long eventId, EventSaveParam param) {
        log.info("开始更新事件, eventId={}", eventId);

        // 1. 检查事件是否存在
        MdProductEvent event = getById(eventId);
        if (event == null) {
            throw new InvalidArgumentException("事件不存在");
        }

        // 2. 如果修改了eventCode,需要校验唯一性
        if (!event.getEventCode().equals(param.getEventCode())) {
            MdProductEvent existingEvent = getByProdIdAndEventCode(param.getProdId(), param.getEventCode());
            if (existingEvent != null && !existingEvent.getId().equals(eventId)) {
                throw new InvalidArgumentException("事件编码在该产品下已存在: " + param.getEventCode());
            }
        }

        // 3. 参数校验
        validateEvent(param);

        // 4. 更新事件信息
        EntityBuilder.updateEvent(event, param);

        // 5. 保存更新
        updateById(event);

        log.info("事件更新成功, eventId={}", eventId);

        // 6. 触发版本快照生成
        try {
            versionService.createSnapshot(param.getProdId(), OperationTypeEnum.UPDATE.getCode(), "更新事件: " + param.getName());
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", param.getProdId(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEvent(Long eventId) {
        log.info("开始删除事件, eventId={}", eventId);

        // 1. 检查事件是否存在
        MdProductEvent event = getById(eventId);
        if (event == null) {
            throw new InvalidArgumentException("事件不存在");
        }

        Long prodId = event.getProdId();

        // 2. 删除前关联检查(检查告警规则、场景联动等引用)
        guardManager.checkBeforeDelete(eventId, EntityTypeEnum.EVENT.getCode());

        // 3. 软删除
        DatabaseContextHolder.softDelete(this, event.getId());

        log.info("事件删除成功, eventId={}", eventId);

        // 4. 触发版本快照生成
        try {
            versionService.createSnapshot(prodId, OperationTypeEnum.UPDATE.getCode(), "删除事件");
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", prodId, e);
        }
    }

    @Override
    public List<ProductEventVO> listByProdId(Long prodId) {
        log.info("查询产品事件列表, prodId={}", prodId);
        List<ProductEventVO> events = eventMapper.selectByProdIdNoCategoryName(prodId);
        if (events == null || events.isEmpty()) {
            return events;
        }

        for (ProductEventVO vo : events) {
            if (vo == null || vo.getEventCategoryId() == null) {
                continue;
            }

            vo.setEventCategoryName(systemDictCacheService.getDictItemLabel(vo.getEventCategoryId()));
        }
        return events;
    }

    @Override
    public ProductEventVO getEventDetail(Long eventId) {
        log.info("查询事件详情, eventId={}", eventId);

        MdProductEvent event = getById(eventId);
        if (event == null) {
            throw new InvalidArgumentException("事件不存在");
        }

        // 转换为VO
        return EntityBuilder.buildEventVO(event, vo -> {
            // 设置事件类别名称(事件类别是无租户的系统字典)
            if (event.getCategoryId() != null) {
                vo.setEventCategoryName(systemDictCacheService.getDictItemLabel(event.getCategoryId()));
            }
        });
    }

    private MdProductEvent getByProdIdAndEventCode(Long prodId, String eventCode) {
        return eventMapper.selectByProdIdAndCode(prodId, eventCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Long prodId, List<EventSaveParam> params) {
        log.info("批量创建事件, prodId={}, count={}", prodId, params.size());

        for (EventSaveParam param : params) {
            // 确保prodId一致
            param.setProdId(prodId);
            saveEvent(param);
        }

        log.info("批量创建事件完成, prodId={}, count={}", prodId, params.size());
    }

    /**
     * 参数校验
     */
    private void validateEvent(EventSaveParam param) {
        // 校验EventConfig配置
        if (param.getConfig() != null) {
            EventConfigConverter.validate(param.getConfig());
        }
    }

    @Override
    public List<DictItemVO> listEventCategories() {
        log.info("查询事件类别列表");

        List<SysDictItem> items = systemDictCacheService.getDictItems(
            DictEnum.PRODUCT_EVENT_CATEGORY.getId()
        );

        // 转换为VO
        return items.stream()
                .map(V3DictItemService::toVO)
                .collect(Collectors.toList());
    }
}
