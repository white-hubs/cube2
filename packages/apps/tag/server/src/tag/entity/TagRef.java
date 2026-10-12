package com.guhecloud.cube.srv.gen.tag.tag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.tag.tag.enums.TagRefTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tag_ref")
public class TagRef extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long tagId;
    private Long refId;
    private TagRefTypeEnum refType;

}
