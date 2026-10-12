package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 耗材盘点
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableCheckWithStatVO {

    //
    private Long id;
    //盘点单名称
    private String name;
    //状态
    private String status;
    //盘点仓库
    private Long storeId;

    private String storeName;
    //盘点开始时间
    private LocalDateTime startTime;
    //盘点结束时间
    private LocalDateTime endTime;
    //盘点负责人ID(用户ID)
    private Long checkerId;
    //盘点负责人
    private String checker;

    private String createBy;

    private LocalDateTime createTime;
    //盘点统计信息
    private ConsumableCheckDetailStatVO stat;
    /**
     * 完成实际时间
     **/
    private LocalDateTime realityEndTime;

}