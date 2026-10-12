package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyCheckTaskDataSourceParam {
    /**
     * 资产类别id
     */
    private String typeId;
    private List<String> typeIds;

    private String typeName;
    /**
     * 购入时间
     */
    private LocalDateTime purchaseDate;
    /**
     * 管理员id
     */
    private Long adminId;
    private String adminName;
    /**
     * 状态
     */
    private String status;
    /**
     * 区域id
     */
    private List<String> areaIds;
    private List<AreaDTO> area;

    /**
     * 使用部门
     */
    private String receiveDepId;
    private String receiveDepName;
}
