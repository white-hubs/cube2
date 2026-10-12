package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingSourceEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDockConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoVisual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingInfo {

    /**
     * 车场ID
     */
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
     * 父级车场编号
     */
    private String parentCode;

    /**
     * 车场来源
     */
    private ParkingSourceEnum source;

    /**
     * 所在区域
     */
    private String areaId;

    /**
     * 车场地址
     */
    private String address;

    /**
     * 车场备注
     */
    private String remarks;

    /**
     * 车场视角配置
     */
    private ParkingInfoVisual viewConfig;

    /**
     * 车场设备列表
     */
    private List<ParkingInfoDevice> devices;

    /**
     * 对接配置
     */
    private ParkingInfoDockConfig dockConfig;

    /**
     * 租户ID
     */
    private Integer tenant;

    /**
     * 创建车场
     */
    public static ParkingInfo create(String code, String name, String modelArea, Integer totalNum,
                                     ParkingSourceEnum source, String areaId,
                                     String address, String remarks
                                    ) {
        return ParkingInfo.builder()
                .code(code)
                .name(name)
                .modelArea(modelArea)
                .totalNum(totalNum)
                .totalInNum(0)
                .totalSpaceNum(totalNum)
                .source(source)
                .areaId(areaId)
                .address(address)
                .remarks(remarks)
                .build();
    }

    /**
     * 添加设备
     */
    public void addDevice(ParkingInfoDevice device) {
        if (devices != null) {
            devices.add(device);
        }
    }

    /**
     * 更新车位使用情况
     */
    public void updateParkingSpace(Integer inNum) {
        this.totalInNum = inNum;
        if (inNum > this.totalNum) {
            inNum = this.totalNum;
        }
        this.totalSpaceNum = this.totalNum - inNum;
    }
}
