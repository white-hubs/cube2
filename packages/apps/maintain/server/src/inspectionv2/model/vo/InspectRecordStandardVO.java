package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class InspectRecordStandardVO {

    /**
     * 是否临时添加标准
     */
    private Boolean tempAddFlag;

    /**
     * 巡检标准ID
     */
    private Long standardId;

    /**
     * 巡检标准名称
     */
    private String standardName;

    /**
     * 巡检标准名称
     */
    private List<InspectRecordStandardItemVO> itemVOS;
}
