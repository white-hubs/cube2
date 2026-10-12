package com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo;

import com.guhecloud.cube.biz.door.client.vo.DoorDeviceVo;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.ParkingInfoVO;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/11 16:28
 * @Description: 用户权限实体
 */
@Data
public class UserPermissionVO {
    /**
     * 电话号码
     */
    private String mobile;

    /**
     * 停车权限
     */
    private List<ParkingInfoVO> parkingInfos;

    /**
     * 门禁权限
     */
    private List<DoorDeviceVo> doorDeviceList;

    /**
     * 消费权限
     */
    private Boolean consumePm;
}
