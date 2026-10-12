package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/**
 * 仓×雷达可选项一行（来自智慧仓分页 deviceList 中「雷达」设备及仓信息）。
 */
@Data
public class LiangcangWarehouseListItemVO {

    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 仓名。 */
    private String warehouseName;
    /** 类别。 */
    private String categoryName;
    /** 质量等级。 */
    private String qualityGrade;
    /** 仓高。 */
    private Double warehouseHeight;
    /** 总容积。 */
    private Double totalVolume;
    /** 雷达业务设备 id。 */
    private Long radarDeviceId;
    /** 设备 SN。 */
    private String sn;
    /** 设备类型（「雷达」等）。 */
    private String type;
    /** 本系统是否已建绑定，如已创建/未创建。 */
    private String status;
}
