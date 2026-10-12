package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import java.util.List;

@Data
public class InspectRecordSubmitCheckParam {
    /**
     * 巡检记录ID列表
     */
    private List<Long> recordIdList;
}
