package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.IotConfig;
import lombok.Data;

import java.util.List;

@Data
public class IotConfigDTO extends ConvertionDTO<IotConfigDTO, IotConfig> {

    public IotConfigDTO() {
    }

    public IotConfigDTO(IotConfig iotConfig) {
        super();
        map(iotConfig);
    }

    public IotConfigDTO(List<IotConfig> iotConfigs) {
        super();
        map(iotConfigs);
    }

    private String id;

    private String projectId;

    private String iotProjectId;

    private String platformSite;

    private String platformSiteWeb;

    private String username;

    private String password;

    private String remark;

}
