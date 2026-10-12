package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户留言/表单登记
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserFeedback extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 联系方式（手机号/电话）
     */
    private String contactWay;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 应用场景
     */
    private String applicationScenario;

}
