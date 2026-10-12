package com.guhecloud.cube.base.iot.product.model.v3.guard.impl;

import com.guhecloud.cube.base.iot.product.model.v3.enums.EntityTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.guard.DeletionGuard;
import com.guhecloud.cube.base.iot.product.model.v3.guard.EntityHasReferenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 属性删除关联检查守卫
 *
 * 检查项:
 * 1. 是否被告警规则引用
 * 2. 是否被数据流转规则引用
 * 3. 是否被场景联动引用
 *
 * 注意: 当前简化实现,待集成告警、流转、联动模块后补充具体检查逻辑
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PropertyDeletionGuard implements DeletionGuard {

    @Override
    public void checkDeletable(Long entityId, String entityType) {
        log.info("开始检查属性删除关联, propId={}", entityId);

        // TODO: [等待外部模块] 检查是否被告警规则引用
        //   依赖: 告警模块 Feign 接口
        //   追踪: 待告警模块接口就绪后实施
        //   预期接口: AlarmRuleClient.findRulesByPropertyId(Long propId)
        //   说明: 需要调用告警模块检查该属性是否被告警规则引用,如果有则禁止删除
        // 实现示例: checkAlarmRuleReferences(entityId);

        // TODO: [等待外部模块] 检查是否被数据流转规则引用
        //   依赖: 数据流转模块 Feign 接口
        //   追踪: 待数据流转模块接口就绪后实施
        //   预期接口: DataForwardingClient.findRulesByPropertyId(Long propId)
        //   说明: 需要调用数据流转模块检查该属性是否被流转规则引用,如果有则禁止删除
        // 实现示例: checkDataForwardingReferences(entityId);

        // TODO: [等待外部模块] 检查是否被场景联动引用
        //   依赖: 场景联动模块 Feign 接口
        //   追踪: 待场景联动模块接口就绪后实施
        //   预期接口: SceneLinkageClient.findScenesByPropertyId(Long propId)
        //   说明: 需要调用场景联动模块检查该属性是否被场景引用,如果有则禁止删除
        // 实现示例: checkSceneLinkageReferences(entityId);

        log.info("属性删除关联检查通过, propId={}", entityId);
    }

    @Override
    public String getSupportedEntityType() {
        return EntityTypeEnum.PROPERTY.getCode();
    }

    /**
     * 检查告警规则引用(占位方法)
     *
     * @param propId 属性ID
     * @throws EntityHasReferenceException 如果存在引用
     */
    private void checkAlarmRuleReferences(Long propId) {
        // 示例实现:
        // AlarmRuleClient alarmRuleClient = ...; // Feign客户端
        // List<AlarmRuleDTO> rules = alarmRuleClient.findRulesByPropertyId(propId);
        // if (!rules.isEmpty()) {
        //     String ruleNames = rules.stream().map(AlarmRuleDTO::getRuleName).collect(Collectors.joining(", "));
        //     throw new EntityHasReferenceException(
        //         propId,
        //         EntityTypeEnum.PROPERTY.getCode(),
        //         String.format("被告警规则引用: %s", ruleNames)
        //     );
        // }
    }
}
