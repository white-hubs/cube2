package com.guhecloud.cube.srv.gen.maintain.server.electron.entity.base;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 计划/任务负责人基类
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PatrolBaseSuperintendent extends BasicTenantEntity<Long> {
    private Long id;
    /**
     * 主关联id(计划/任务ID)
     */
    private Long masterId;
    /**
     * 负责人类型 ConditionTypeEnum
     */
    private String superintendentType;
    /**
     * 负责对象的id(例如superintendentType类型为USER时是人员id,为POST时是岗位id
     */
    private Long targetId;

    public PatrolBaseSuperintendent() {}

    public PatrolBaseSuperintendent(Long id, Long masterId, String superintendentType, Long targetId, String createBy,
                                    LocalDateTime createTime, String updateBy, LocalDateTime updateTime, Integer tenant, Boolean deleted
                                   ) {
        this.id = id;
        this.masterId = masterId;
        this.superintendentType = superintendentType;
        this.targetId = targetId;
        super.setCreateBy(createBy);
        super.setCreateTime(createTime);
        super.setUpdateBy(updateBy);
        super.setUpdateTime(updateTime);
        super.setTenant(tenant);
        super.setDeleted(deleted);
    }
}