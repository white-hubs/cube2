package com.guhecloud.cube.srv.gen.tag.tag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/7 14:07
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tag_user_ref")
public class TagUserRef extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long tagId;
    private Long userId;
}
