package com.guhecloud.cube.srv.gen.tag.tag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.cube.srv.gen.tag.tag.filter.TagBusinessCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.enumeration.ConditionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tag_business")
@DataPermission(value = {
        @DataPermissionField(permission = "CURRENT_USER", field = "dep_ids", condition = ConditionEnum.LIKE,
                             fieldFilter = TagBusinessCurrentUserDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "dep_ids", condition = ConditionEnum.LIKE),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "dep_ids", condition = ConditionEnum.LIKE)
}
)
public class TagBusiness extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private String depIds;
}
