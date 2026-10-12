package com.guhecloud.platform.database.permission.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DbDataFieldPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 可见范围(全部、自定义查看、当前用户部门的数据、当前用户账号创建的数据,若后续还有其它过滤条件则由业务自行定义)
     */
    private String scope;

    /**
     * 过滤字段
     */
    private List<PermissionField> fields;

    /**
     * 过滤参数
     */
    private Object filterParams;
}
