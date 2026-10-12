package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.param;

import lombok.Data;

import java.util.Map;

/**
 * 创建离线同步会话请求。
 */
@Data
public class InspectOfflineCreateSyncSessionParam {
    private String clientId;
    private String appVersion;
    private Integer offlineSchemaVersion;
    private String baseCursor;
    private Map<String, String> knownResourceVersions;
}
