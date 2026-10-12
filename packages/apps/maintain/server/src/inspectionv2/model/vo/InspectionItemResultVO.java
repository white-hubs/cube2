package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 巡检项巡检结果
 * @date 2023/4/19 15:12
 */
@Data
public class InspectionItemResultVO {
    /**
     * 结果记录id
     */
    private Long id;
    /**
     * 巡检项id
     */
    private Long itemId;
    /**
     * 巡检结果
     */
    private Integer resultFlag;
}
