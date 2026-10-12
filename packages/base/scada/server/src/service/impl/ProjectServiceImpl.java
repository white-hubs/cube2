package com.guhecloud.kaihe.scada.server.service.impl;

import com.guhecloud.kaihe.scada.server.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProjectServiceImpl implements ProjectService {

    // 兼容现有版本，当projectId为默认时则路径直接为data下，如果有项目id时，则在data目录下创建项目文件夹，
    public String getProjectBasePath(Integer projectId) {
        if (Objects.equals(projectId, Integer.valueOf(DEFAULT_PROJECT_ID))) {
            return "";
        }
        return projectId.toString();
    }

}
