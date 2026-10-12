package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import java.util.List;

@Data
public class InspectRecordListSubmitCommand {
    /**
     * App 端批次幂等标识。老客户端可以不传。
     */
    private String clientBatchId;

    /**
     * 设备稳定标识。老客户端可以不传。
     */
    private String clientId;

    private List<InspectTaskRecordSubmitCommand> commandList;
}
