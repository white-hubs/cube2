package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 清理报废;
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@Data
@TableName("property_weed_out")
public class PropertyWeedOut extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 处理人id
     */
    private Long weedOutUserId;
    /**
     * 清理时间
     */
    private LocalDateTime manageTime;
    /**
     * 清理原因
     */
    private Long causeId;
    /**
     * 是否过期
     */
    private Integer overdue;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 附件
     */
    private String files;
    /**
     * 报废单号
     */
    private String weedOutNumbers;
    private String status;
    private String processInstanceId;
}
