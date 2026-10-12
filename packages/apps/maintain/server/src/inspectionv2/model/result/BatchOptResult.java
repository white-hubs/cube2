package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import lombok.Data;

import java.util.List;

@Data
public class BatchOptResult {
    private long success;
    private long fail;

    private List<String> errorMsg;
}
