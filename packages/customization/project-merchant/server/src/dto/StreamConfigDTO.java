package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.StreamConfig;
import lombok.Data;

import java.util.List;

@Data
public class StreamConfigDTO extends ConvertionDTO<StreamConfigDTO, StreamConfig> {

    public StreamConfigDTO() {
    }

    public StreamConfigDTO(StreamConfig streamConfig) {
        super();
        map(streamConfig);
    }

    public StreamConfigDTO(List<StreamConfig> streamConfigs) {
        super();
        map(streamConfigs);
    }

    private String id;

    private String projectId;

    private String name;

    private String host;

    private String ip;

    private Integer port;

    private Integer rtmpPort;

    private String lanIp;

    private Integer lanPort;

    private String username;

    private String password;

    private String remark;

    private String streamSession;

}
