package com.guhecloud.cube.biz.linkage.server.facade.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventAdditionalPicFileDTO {

    private String id;

    private String name;

    private String suffix;

    private String path;

    private String size;

    private Integer refCount;

    private String type;

    private String businessKey;

    private String tag;

    private String accessPrefix;
}
