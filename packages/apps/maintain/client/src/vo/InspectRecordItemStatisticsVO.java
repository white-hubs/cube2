package com.guhecloud.cube.srv.gen.maintain.vo;

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
