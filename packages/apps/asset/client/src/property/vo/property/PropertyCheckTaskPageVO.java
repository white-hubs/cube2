package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyCheckTaskPageVO {
    private Long id;
    /**
     * 状态
     */
    private String status;
    /**
     * 盘点任务名称
     */
    private String taskName;
    /**
     * 盘点资产数
     */
    private Integer checkTotalNumber;
    /**
     * 待盘点
     */
    private Integer checkWaitNumber;
    /**
     * 已盘点
     */
    private Integer checkCompleteNumber;
    /**
     * 盘赢
     */
    private Integer checkWinNumber;
    /**
     * 盘亏
     */
    private Integer checkLossNumber;
    /**
     * 预计完成时间
     */
    private LocalDateTime estimateEndTime;
    /**
     * 时间完成时间
     */
    private LocalDateTime realityEndTime;
    /**
     * 盘点负责人
     */
    private String dutyUserName;
    private Long dutyUserId;
    /**
     * 资产明细
     */
    private List<PropertyDetailedVO> propertyDetailed;
    private Long checkUserId;
    private String checkUserName;
    private String checkDepName;
    private Integer abnormalNumber = 0;

}
