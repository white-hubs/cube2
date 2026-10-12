package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 车场监控信息返回参数
 *
 * @author lanchun
 * @date 2022/10/13 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingMonitorVO {

    private Long id;

    /**
     * 车场编号
     */
    private String code;

    /**
     * 车场名称
     */
    private String name;

    /**
     * 模型区域
     */
    private String modelArea;

    /**
     * 车位总数
     */
    private Integer totalNum;

    /**
     * 车位使用总数
     */
    private Integer totalInNum;

    /**
     * 剩余总数
     */
    private Integer totalSpaceNum;

    /**
     * 设备集合
     */
    private List<DeviceBaseArchivesInfoDTO> devices;

}
