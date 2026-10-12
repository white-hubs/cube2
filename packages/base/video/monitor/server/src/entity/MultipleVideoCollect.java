package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 多路视频数据模型
 *
 * @author lanchun
 * @date 2021/12/6 19:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultipleVideoCollect extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 行数
     */
    private Integer row;

    /**
     * 列数
     */
    private Integer col;

}