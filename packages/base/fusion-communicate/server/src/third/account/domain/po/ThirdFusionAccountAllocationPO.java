package com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 第三方账号分配表
 *
 * @author lanchun
 * @date 2024/7/31 15:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "third_fusion_account_allocation")
public class ThirdFusionAccountAllocationPO extends BasicEnhancedTenantEntity<Long> {

    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 厂家编号
     */
    private String providerCode;

    /**
     * 第三方账号号码
     */
    private String thirdAccountNumber;

    /**
     * 系统用户主键
     */
    private Long userId;

    /**
     * 保活时间
     */
    private LocalDateTime keepAliveTime;
}
