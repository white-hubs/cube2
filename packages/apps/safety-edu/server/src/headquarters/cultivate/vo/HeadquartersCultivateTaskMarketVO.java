package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo;

import lombok.Data;

/**
 * 培训任务关联市场信息
 *
 * @author lanchun
 * @date 2024/4/10 14:27
 */
@Data
public class HeadquartersCultivateTaskMarketVO {

    private Long id;

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
