package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafetyInspectItemVO {
    /**
     * 巡检项记录id
     */
    private Long id;
    /**
     * 原始巡检项id
     */
    private Long itemId;
    /**
     * 巡检下名称
     */
    private String itemName;
    /**
     * 异常说明
     */
    private String errorDesc;

}
