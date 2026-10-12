package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/**
 * 粮仓绑定状态分页单条（records 元素）。
 */
@Data
public class RadarBoundPageVO {
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 雷达业务设备 id；未创建绑定时来自智慧仓雷达行。 */
    private Long radarDeviceId;
    /** 绑定状态：已创建/未创建。 */
    private String bindingStatus;
    /** 仓名称。 */
    private String granaryName;
    /** 类别。 */
    private String category;
    /** 等级。 */
    private String level;
    /** 最大高度（米）。 */
    private Double maxHeight;
    /** 仓容（立方米等，与三方一致）。 */
    private Double granaryVolume;
    /** 已关联业务设备数；未创建绑定时为 0。 */
    private Integer relatedDeviceCount;
    /** 已绑定产品名称，多个名称使用顿号分隔；未创建绑定时为空。 */
    private String boundProductTypes;
    /**
     * 当前登录用户是否拥有「复制」权限：仅在仓库权限配置中绑定该用户时为 true；
     * 未登录用户或非 USER 主体时为 false。
     */
    private Boolean copyAllowed;
}
