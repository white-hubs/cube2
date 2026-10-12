package com.guhecloud.platform.database.permission.entity;

import com.guhecloud.platform.database.enumeration.ConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionField {

    /**
     * 字段
     */
    private String field;

    /**
     * 条件
     */
    private ConditionEnum condition;
}
