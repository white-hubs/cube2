package com.guhecloud.cube.base.device.center.core.vo;

import lombok.Data;

import java.util.List;

@Data
public class BatchOptResult {
    private long success;
    private long fail;

    private List<String> errorMsg;
}
