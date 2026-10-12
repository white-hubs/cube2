package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlowQueryDTO {

    private String projectId;

    private String search;

    private String category;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

}
