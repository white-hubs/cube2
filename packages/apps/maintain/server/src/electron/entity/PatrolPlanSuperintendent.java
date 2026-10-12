package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.base.PatrolBaseSuperintendent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 计划负责人
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TableName("patrol_plan_superintendent")
public class PatrolPlanSuperintendent extends PatrolBaseSuperintendent {

    public PatrolPlanSuperintendent() {}

    public PatrolPlanSuperintendent(Long id, Long masterId, String superintendentType, Long targetId, String createBy,
                                    LocalDateTime createTime, String updateBy, LocalDateTime updateTime, Integer tenant, Boolean deleted
                                   ) {
        super(id, masterId, superintendentType, targetId, createBy, createTime, updateBy, updateTime, tenant, deleted);
    }
}