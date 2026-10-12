package com.guhecloud.cube.biz.meattrade.server.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.InspectorEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 抽检员
 */
@Data
@TableName("meattrade_inspector")
public class Inspector extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 抽检员名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 抽检员类型
     */
    private InspectorEnum type;

    /**
     * 添加时间
     */
    private LocalDateTime addTime;

    /**
     * 备注
     */
    private String remark;

}
