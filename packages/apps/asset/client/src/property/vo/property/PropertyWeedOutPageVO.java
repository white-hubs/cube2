package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyWeedOutPageVO {
    private Long id;
    /**
     * 处理人id
     */
    private Long weedOutUserId;
    private String weedOutUserName;
    /**
     * 清理时间
     */
    private LocalDateTime manageTime;
    /**
     * 清理原因
     */
    private String cause;
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
    /**
     * 审批状态
     */
    private String status;
    /**
     * 资产详情
     */
    private List<PropertyDetailedVO> propertyDetailed;
    private String processInstanceId;

}
