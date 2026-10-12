package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 根据蓝牙编号查询巡更点参数
 * @date 2023/10/25 13:57
 */
@Data
public class PatrolLocationByBluetoothParam {
    /**
     * 蓝牙编号
     */
    private List<String> bluetoothCodes;
}
