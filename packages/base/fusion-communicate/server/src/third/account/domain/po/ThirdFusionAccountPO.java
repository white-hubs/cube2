package com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "third_fusion_account")
public class ThirdFusionAccountPO extends BasicEnhancedTenantEntity<Long> {

    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编号
     */
    private String code;

    /**
     * 通讯号码
     */
    private String number;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号等级
     */
    private String level;

    /**
     * 账号使用状态
     */
    private String usageState;

    /**
     * 厂家编号
     */
    private String providerCode;
}
