package com.guhecloud.cube.biz.mdata.client.dto;

import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import lombok.Data;

/**
 * @author lzy
 * @date 2021/10/19
 */
@Data
public class RiskLevelDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 风险级别类别
     */
    private RiskLevelEnum type;
    /**
     * 风险级别数值
     */
    private String value;
}
