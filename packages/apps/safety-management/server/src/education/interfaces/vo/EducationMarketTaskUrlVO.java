package com.guhecloud.cube.srv.gen.safety.management.education.interfaces.vo;

import lombok.Data;

/**
 * @author lanchun
 * @date 2024/4/11 9:23
 */
@Data
public class EducationMarketTaskUrlVO {

    private Long id;

    /**
     * 市场配置主键
     */
    private Long marketId;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 市场编号
     */
    private Integer marketTenant;

    /**
     * 业务类型(区分是考试or培训)
     */
    private String taskType;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 启用状态
     */
    private Boolean enabled;
}
