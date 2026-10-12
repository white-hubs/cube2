package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

import java.util.List;

/** 取消电缆点位映射。 */
@Data
public class WarehouseCablePointUnbindParam {
    /** 点位 ID 列表。 */
    private List<Long> pointIds;
    /** 电缆 ID；未传 pointIds 时使用 cableId + pointNos 定位。 */
    private Long cableId;
    /** 点位序号列表；未传 pointIds 时使用 cableId + pointNos 定位。 */
    private List<Integer> pointNos;
}
