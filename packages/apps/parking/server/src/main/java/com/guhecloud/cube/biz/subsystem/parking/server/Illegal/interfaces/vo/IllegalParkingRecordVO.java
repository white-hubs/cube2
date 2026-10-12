package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 09:49
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IllegalParkingRecordVO extends ProcessInstanceDTO {

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
