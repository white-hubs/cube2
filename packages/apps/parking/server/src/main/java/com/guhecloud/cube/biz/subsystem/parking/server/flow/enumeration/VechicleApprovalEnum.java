package com.guhecloud.cube.biz.subsystem.parking.server.flow.enumeration;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.valueobj.IllegalParkingApprovalInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto.VechicleApprovalDTO;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto.VechicleApprovalUpdateDTO;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto.VechicleDeleteDTO;
import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 10:24
 * @Description: 车辆审批枚举
 */
@Getter
public enum VechicleApprovalEnum {

    VECHICLE_INSERT("新增车辆", VechicleApprovalDTO.class),
    VECHICLE_UPDATE("变更车辆", VechicleApprovalUpdateDTO.class),
    VECHICLE_DELETE("删除车辆", VechicleDeleteDTO.class),
    ILLEGAL_PARKING("车辆违停", IllegalParkingApprovalInfo.class);

    private final String desc;

    private final Class clazz;

    VechicleApprovalEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}
