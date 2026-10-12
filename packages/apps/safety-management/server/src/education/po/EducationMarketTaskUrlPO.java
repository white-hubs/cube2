package com.guhecloud.cube.srv.gen.safety.management.education.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lanchun
 * @date 2024/4/11 9:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "education_market_task_url")
public class EducationMarketTaskUrlPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 市场配置主键
     */
    private Long marketId;

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
