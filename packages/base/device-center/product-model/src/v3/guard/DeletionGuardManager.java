package com.guhecloud.cube.base.iot.product.model.v3.guard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除守卫管理服务
 *
 * 负责统一管理所有实体的删除关联检查守卫
 * 使用策略模式,根据实体类型自动选择对应的守卫执行检查
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeletionGuardManager {

    /** 注入所有DeletionGuard实现 */
    private final List<DeletionGuard> guards;

    /** 守卫映射表: entityType -> DeletionGuard */
    private final Map<String, DeletionGuard> guardMap = new HashMap<>();

    /**
     * 初始化守卫映射表
     */
    @PostConstruct
    public void init() {
        for (DeletionGuard guard : guards) {
            String entityType = guard.getSupportedEntityType();
            guardMap.put(entityType, guard);
            log.info("注册删除守卫: entityType={}, guardClass={}", entityType, guard.getClass().getSimpleName());
        }
        log.info("删除守卫管理器初始化完成, 共注册 {} 个守卫", guardMap.size());
    }

    /**
     * 执行删除前关联检查
     *
     * @param entityId 实体ID
     * @param entityType 实体类型(product/property/event/method)
     * @throws EntityHasReferenceException 如果存在关联引用,抛出异常阻止删除
     */
    public void checkBeforeDelete(Long entityId, String entityType) {
        DeletionGuard guard = guardMap.get(entityType);
        if (guard == null) {
            log.warn("未找到对应的删除守卫, entityType={}, 跳过检查", entityType);
            return;
        }

        guard.checkDeletable(entityId, entityType);
    }
}
