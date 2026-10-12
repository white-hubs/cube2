package com.guhecloud.cube.srv.gen.business.info.sync.server.service.dto;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/24 18:21
 * @Description: 自定义数据同步job参数
 */
@Data
public class CustomTableDataSyncParam {

    /**
     * 是否移除
     */
    private Boolean remove;

    /**
     * 移除时间（天）
     */
    private Integer removeTime;
}
