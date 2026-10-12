package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 培训任务关联市场信息
 *
 * @author lanchun
 * @date 2024/4/10 14:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HeadquartersCultivateTaskMarket extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 总部培训任务主键
     */
    private Long headquartersCultivateTaskId;

    /**
     * 市场编号
     */
    private String marketCode;

    /**
     * 市场租户编号
     */
    private Integer marketTenantCode;

    /**
     * 市场名称
     */
    private String marketName;
}
