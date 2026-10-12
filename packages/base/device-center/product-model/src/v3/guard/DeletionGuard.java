package com.guhecloud.cube.base.iot.product.model.v3.guard;

/**
 * 实体删除关联检查守卫接口
 *
 * 在删除实体前检查是否存在关联引用,避免产生脏数据和悬空引用
 * 设计原则:
 * 1. 防御性检查: 删除前必须通过checkDeletable()检查
 * 2. 快速失败: 发现关联立即抛异常,阻止删除
 * 3. 明确提示: 异常信息清晰说明关联详情,指导用户操作
 */
public interface DeletionGuard {

    /**
     * 检查实体是否可删除
     *
     * @param entityId 实体ID
     * @param entityType 实体类型(product/property/event/method)
     * @throws EntityHasReferenceException 如果存在关联引用,抛出异常阻止删除
     */
    void checkDeletable(Long entityId, String entityType);

    /**
     * 获取守卫支持的实体类型
     *
     * @return 实体类型标识
     */
    String getSupportedEntityType();
}
