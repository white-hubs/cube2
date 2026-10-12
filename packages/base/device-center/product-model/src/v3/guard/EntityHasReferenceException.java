package com.guhecloud.cube.base.iot.product.model.v3.guard;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 实体存在关联引用异常
 *
 * 当尝试删除被其他实体引用的实体时抛出此异常
 * 异常信息应清晰说明:
 * 1. 被删除的实体类型和ID
 * 2. 引用来源(哪些实体引用了它)
 * 3. 建议操作(如何解除引用后再删除)
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityHasReferenceException extends RuntimeException {

    /** 实体ID */
    private final Long entityId;

    /** 实体类型 */
    private final String entityType;

    /** 引用来源描述 */
    private final String referenceSource;

    /**
     * 构造函数
     *
     * @param entityId 实体ID
     * @param entityType 实体类型
     * @param referenceSource 引用来源描述
     */
    public EntityHasReferenceException(Long entityId, String entityType, String referenceSource) {
        super(String.format(
            "%s(ID=%d) 无法删除,存在关联引用: %s。请先解除关联后再删除。",
            entityType, entityId, referenceSource
        ));
        this.entityId = entityId;
        this.entityType = entityType;
        this.referenceSource = referenceSource;
    }

    /**
     * 构造函数(带详细消息)
     *
     * @param message 详细错误消息
     * @param entityId 实体ID
     * @param entityType 实体类型
     * @param referenceSource 引用来源
     */
    public EntityHasReferenceException(String message, Long entityId, String entityType, String referenceSource) {
        super(message);
        this.entityId = entityId;
        this.entityType = entityType;
        this.referenceSource = referenceSource;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getReferenceSource() {
        return referenceSource;
    }
}
