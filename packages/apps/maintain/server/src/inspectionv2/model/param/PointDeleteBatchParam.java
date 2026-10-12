package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import java.util.List;

@Data
public class PointDeleteBatchParam {
    private List<Long> idList;
}
