package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param;

import lombok.Data;

/**
 * 培训市场
 *
 * @author lanchun
 * @date 2024/4/10 14:29
 */
@Data
public class HeadquartersCultivateTaskMarketParam {

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