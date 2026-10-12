package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzy
 * @date 2022/4/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HiddenDangerType extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 隐患分类主键
     */
    private Long hiddenDangerCategoryId;

    /**
     * 隐患分类名称
     */
    private String hiddenDangerCategoryName;

}
