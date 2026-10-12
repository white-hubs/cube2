package com.guhecloud.cube.property.management.client.property.vo.property;

import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  09:53
 * @Description:
 * @Version: 1.0
 */
@Data
public class PropertyBimConfigVO {
    private Long id;
    private Long propertyId;
    /**
     * 资产编码
     */
    private String serialNumber;
    /**
     * 资产名称
     */
    private String name;
    private String areaId;
    /**
     * 所属区域
     */
    private List<AreaDTO> area;
    /**
     * 模型编码
     */
    private String deviceNumber;
    /**
     * 类型名称
     */
    private String typeName;
}
