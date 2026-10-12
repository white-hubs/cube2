package com.guhecloud.cube.biz.mdata.server.purchaser.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzy
 * @date 2021/12/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Purchaser extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
