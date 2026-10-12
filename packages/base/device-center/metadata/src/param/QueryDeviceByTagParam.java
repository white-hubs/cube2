package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;

import java.util.List;

@Data
public class QueryDeviceByTagParam {

    /**
     * 标签编号集合
     */
    private List<String> tagCodes;

}
