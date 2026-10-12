package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/10
 */
@Data
public class FaultCaseDeviceVO {
    /**
     * 品类code
     */
    private String categoryCode;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 设备数量
     */
    private Long deviceNum;
    /**
     * 子品类
     */
    private List<FaultCaseDeviceVO> children;

    public FaultCaseDeviceVO() {}

    public FaultCaseDeviceVO(String categoryCode, String categoryName, Long deviceNum) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.deviceNum = deviceNum;
    }

}
