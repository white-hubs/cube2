package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import lombok.Data;

@Data
public class InspectRecordItemStatisticsVO {

    /**
     * 项id
     */
    private Long itemId;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 总数
     */
    private Integer totalNumber;
}
