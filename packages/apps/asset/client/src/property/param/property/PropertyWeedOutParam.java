package com.guhecloud.cube.property.management.client.property.param.property;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyWeedOutParam {
    /**
     * 处理人id
     */
    private List weedOutUserId;
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
    private List<Long> files;
    /**
     * 资产id
     */
    List<Long> propertyIds;
}
