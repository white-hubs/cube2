package com.guhecloud.cube.deliver.project.merchant.entity.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

/**
 * 发送消息人员
 *
 * @author : lijie
 * @date : 2023-4-19
 */
@Data
@TableName("assembly_notice_user")
public class ProjectMerchantAssemblyNoticeUser extends BasicTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 消息配置id
     */
    private Long noticeId;
    /**
     * 通知id
     */
    private Long typeIds;
    private String type;
    /**
     * 业务类型
     */
    private String businessType;
    private Long conditionId;

}
