package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** 粮仓测量信息同步配置。 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseMeasureSyncConfigDTO {
    private Integer tenant;
    /** 温度趋势预警弹窗接收角色 id，支持数组或逗号分隔字符串。 */
    private Object roleIds;
    /** 兼容字典中以逗号分隔字符串配置角色 id。 */
    private String roleId;
}
