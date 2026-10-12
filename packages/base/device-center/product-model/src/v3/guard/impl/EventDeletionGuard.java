package com.guhecloud.cube.base.iot.product.model.v3.guard.impl;

import com.guhecloud.cube.base.iot.product.model.v3.enums.EntityTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.guard.DeletionGuard;
import com.guhecloud.cube.base.iot.product.model.v3.guard.EntityHasReferenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 事件删除关联检查守卫
 *
 * 检查项:
 * 1. 是否被告警规则引用
 * 2. 是否被场景联动引用
 *
 * 注意: 当前简化实现,待集成相关模块后补充具体检查逻辑
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventDeletionGuard implements DeletionGuard {

    @Override
    public void checkDeletable(Long entityId, String entityType) {
        log.info("开始检查事件删除关联, eventId={}", entityId);

        // TODO: [等待外部模块] 检查是否被告警规则引用
        //   依赖: 告警模块 Feign 接口
        //   追踪: 待告警模块接口就绪后实施
        //   预期接口: AlarmRuleClient.findRulesByEventId(Long eventId)
        //   说明: 需要调用告警模块检查该事件是否被告警规则引用,如果有则禁止删除
        // 实现示例: checkAlarmRuleReferences(entityId);

        // TODO: [等待外部模块] 检查是否被场景联动引用
        //   依赖: 场景联动模块 Feign 接口
        //   追踪: 待场景联动模块接口就绪后实施
        //   预期接口: SceneLinkageClient.findScenesByEventId(Long eventId)
        //   说明: 需要调用场景联动模块检查该事件是否被场景引用,如果有则禁止删除
        // 实现示例: checkSceneLinkageReferences(entityId);

        log.info("事件删除关联检查通过, eventId={}", entityId);
    }

    @Override
    public String getSupportedEntityType() {
        return EntityTypeEnum.EVENT.getCode();
    }
}
