package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.param;

import lombok.Data;

import java.util.List;

/**
 * 离线提交状态查询请求。
 */
@Data
public class InspectOfflineSubmitStatusParam {
    private List<String> clientOperationIds;
}
