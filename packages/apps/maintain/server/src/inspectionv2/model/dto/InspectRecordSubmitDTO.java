package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import lombok.Data;

import java.util.List;

@Data
public class InspectRecordSubmitDTO {
    private InspectRecordPO po;
    private List<InspectRecordItemCommandModel> commandModels;
}
