package com.guhecloud.cube.deliver.project.merchant.entity.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

/**
 * 审批配置;
 *
 * @author : lijie
 * @date : 2023-4-18
 */
@Data
@TableName("assembly_notice_condition")
public class ProjectMerchantAssemblyNoticeCondition extends BasicTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long noticeId;
    private String name;
    private Integer value;
    private String businessType;
    private String moduleType;
    private Long msgId;
}
