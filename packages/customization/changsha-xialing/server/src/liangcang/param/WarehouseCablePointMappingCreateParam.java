package com.guhecloud.cube.customization.xialing.server.liangcang.param;

import lombok.Data;

import java.util.List;

/** 批量新增电缆点位映射。 */
@Data
public class WarehouseCablePointMappingCreateParam {
    /** 电缆 ID。 */
    private Long cableId;
    /** 指定点位映射项。 */
    private List<MappingItem> mappings;

    @Data
    public static class MappingItem {
        /** 点位序号。 */
        private Integer pointNo;
        /** 映射的设备中心设备 ID。 */
        private Long deviceId;
    }
}
