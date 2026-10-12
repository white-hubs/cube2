package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device;

import lombok.Data;

/**
 * 设备品类统计返回参数
 *
 * @author lanchun
 * @date 2025/3/17 11:45
 */
@Data
public class DeviceCategoryCountVO {

    /**
     * 品类名称
     */
    private String name;

    /**
     * 品类数量
     */
    private Integer count;
}
