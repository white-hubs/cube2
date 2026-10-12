package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 临时添加类, 替换device中的vo
 * @date 2024/1/17 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceCategoryMaintenanceVO {
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 品类code
     */
    private String categoryCode;
    /**
     * 品类树code
     */
    private String categoryPathCode;
    /**
     * 维保项
     */
    private Integer maintenanceItemNum;
}
