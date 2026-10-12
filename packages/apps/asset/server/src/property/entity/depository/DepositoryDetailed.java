package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 入库基本信息;
 *
 * @author : lijie
 * @date : 2023-3-3
 */
@Data
@TableName("property_depository_detailed")
public class DepositoryDetailed extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 来源id
     */
    private Long sourceId;
    /**
     * 来源名称
     */
    private String sourceName;
    /**
     * 收货单号
     */
    private String receiptNumbers;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 入库日期
     */
    private LocalDateTime startTime;
    /**
     * 入库状态
     */
    private String status;
    /**
     * 入库单号（RK+日期）
     */
    private String depositoryNumbers;
    private String processInstanceId;
}
