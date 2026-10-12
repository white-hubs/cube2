package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/**
 * 绑定详情：智慧仓信息 + 本地已绑雷达。
 */
@Data
public class RadarBindingDetailVO {
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 已绑雷达业务设备 id，未绑时可为空。 */
    private Long radarDeviceId;
    /** 仓名称。 */
    private String granaryName;
    /** 仓类别。 */
    private String category;
    /** 质量等级。 */
    private String level;
    /** 最大高度。 */
    private Double maxHeight;
    /** 仓内容积。 */
    private Double granaryVolume;
}
