package com.guhecloud.cube.biz.vmonitor.server.param.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 设备批量删除条件
 *
 * @author lanchun
 * @date 2024/8/23 9:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceBatchDeleteParam {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 区域主键
     */
    private String areaId;

    /**
     * 设备主键集合
     */
    private List<Long> deviceIds;
}
