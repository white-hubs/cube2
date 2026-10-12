package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 总部考试任务关联市场信息
 *
 * @author lanchun
 * @date 2024/4/9 17:13
 */
@Data
@AllArgsConstructor
public class HeadquartersExamTaskMarketVO {

    /**
     * 市场编码
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
