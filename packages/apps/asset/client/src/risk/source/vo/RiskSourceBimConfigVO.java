package com.guhecloud.cube.property.management.client.risk.source.vo;

import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  13:53
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceBimConfigVO {
    private Long id;
    private Long riskSourceId;
    /**
     * 风险源编码
     */
    private String serialNumber;
    /**
     * 风险源名称
     */
    private String name;
    /**
     * 风险源区域
     */
    private List<AreaDTO> area;
    private String areaId;

    /**
     * 模型编码
     */
    private String bimCode;
    private String typeCode;
    private String typeName;
}
