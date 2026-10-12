package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 15:12
 * @Description: 违停记录
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("illegal_parking_record")
public class IllegalParkingRecordPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 工单号
     */
    private String code;

    /**
     * 工单名称
     */
    private String name;
    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 车主电话
     */
    private String ownerPhone;

    /**
     * 车主姓名
     */
    private String ownerName;

    /**
     * 违停程度
     */
    private String illegalLevel;
}
