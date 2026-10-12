package com.guhecloud.cube.base.iot.product.model.v3.guard.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.iot.product.model.v3.enums.EntityTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.guard.DeletionGuard;
import com.guhecloud.cube.base.iot.product.model.v3.guard.EntityHasReferenceException;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductEventMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductMethodMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductPropMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductEvent;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductMethod;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductProp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 产品删除关联检查守卫
 *
 * 检查项:
 * 1. 是否存在关联设备实例(调用设备管理模块Feign接口)
 * 2. 是否存在未删除的属性/事件/方法
 * 3. 是否被规则引擎引用(调用规则引擎Feign接口)
 *
 * 注意: 当前简化实现,仅检查属性/事件/方法。设备实例和规则引擎检查待集成相关模块后补充。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDeletionGuard implements DeletionGuard {

    private final V3MdProductPropMapper propMapper;
    private final V3MdProductEventMapper eventMapper;
    private final V3MdProductMethodMapper methodMapper;

    @Override
    public void checkDeletable(Long entityId, String entityType) {
        log.info("开始检查产品删除关联, prodId={}", entityId);

        // 1. 检查是否存在属性
        long propCount = countProperties(entityId);
        if (propCount > 0) {
            throw new EntityHasReferenceException(
                entityId,
                EntityTypeEnum.PRODUCT.getCode(),
                String.format("存在 %d 个关联属性,请先删除所有属性", propCount)
            );
        }

        // 2. 检查是否存在事件
        long eventCount = countEvents(entityId);
        if (eventCount > 0) {
            throw new EntityHasReferenceException(
                entityId,
                EntityTypeEnum.PRODUCT.getCode(),
                String.format("存在 %d 个关联事件,请先删除所有事件", eventCount)
            );
        }

        // 3. 检查是否存在方法
        long methodCount = countMethods(entityId);
        if (methodCount > 0) {
            throw new EntityHasReferenceException(
                entityId,
                EntityTypeEnum.PRODUCT.getCode(),
                String.format("存在 %d 个关联方法,请先删除所有方法", methodCount)
            );
        }

        // TODO: [等待外部模块] 检查是否存在设备实例
        //   依赖: 设备管理模块 Feign 接口
        //   追踪: 待设备管理模块接口就绪后实施
        //   预期接口: DeviceClient.countByProductId(Long productId)
        //   说明: 需要调用设备管理模块检查该产品是否有设备实例,如果有则禁止删除
        // 实现示例:
        // long deviceCount = deviceClient.countByProductId(entityId);
        // if (deviceCount > 0) {
        //     throw new EntityHasReferenceException(
        //         entityId,
        //         EntityTypeEnum.PRODUCT.getCode(),
        //         String.format("存在 %d 个关联设备实例,请先删除所有设备", deviceCount)
        //     );
        // }

        log.info("产品删除关联检查通过, prodId={}", entityId);
    }

    @Override
    public String getSupportedEntityType() {
        return EntityTypeEnum.PRODUCT.getCode();
    }

    /**
     * 统计产品的属性数量
     */
    private long countProperties(Long productId) {
        LambdaQueryWrapper<MdProductProp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdProductProp::getProdId, productId);
        return propMapper.selectCount(wrapper);
    }

    /**
     * 统计产品的事件数量
     */
    private long countEvents(Long productId) {
        LambdaQueryWrapper<MdProductEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdProductEvent::getProdId, productId);
        return eventMapper.selectCount(wrapper);
    }

    /**
     * 统计产品的方法数量
     */
    private long countMethods(Long productId) {
        LambdaQueryWrapper<MdProductMethod> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdProductMethod::getProdId, productId);
        return methodMapper.selectCount(wrapper);
    }
}
