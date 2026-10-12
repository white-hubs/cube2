package com.guhecloud.cube.property.management.client.property.vo.property;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AllotPageVO {
    /**
     * 出库id
     */
    private Long id;
    /**
     * 出库单号
     */
    private String allotNumbers;
    /**
     * 移交部门名称
     */
    private String overDepName;
    /**
     * 接收部门名称
     */
    private String receiveDepName;
    /**
     * 接收人
     */
    private Long receiveUserId;
    /**
     * 接收人
     */
    private String receiveUserName;
    /**
     * 接收日期
     */
    private LocalDateTime receiveTime;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 处理人
     */
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
    private String createByName;
    /**
     * 状态
     */
    private String status;
    /**
     * 资产详情
     */
    private List<PropertyDetailedVO> propertyDetailed;
    private String processInstanceId;
}
