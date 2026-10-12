package com.guhecloud.cube.srv.gen.maintain.server.electron.param.task;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-14  16:33
 * @Description:
 * @Version: 1.0
 */
@Data
public class LocationTaskParam {
    /**
     * 蓝牙编码
     */
    @NotNull(message = "蓝牙编码不能为空")
    private List<String> bluetoothCodes;
}
