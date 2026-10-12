package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-09-10  09:45
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
public class MedicalNormRecord {
    /**
     * 指标编码
     */
    private String normCode;
    private String normName;
    /**
     * 指标值
     */
    private String value;
    /**指标状态*/
    private String status;
    /**
     * 单位
     */
    private String unit;
}
