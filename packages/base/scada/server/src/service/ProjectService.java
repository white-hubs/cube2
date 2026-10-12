package com.guhecloud.kaihe.scada.server.service;

public interface ProjectService {

    public static final String DEFAULT_PROJECT_ID = "0";

    String getProjectBasePath(Integer projectId);

}
