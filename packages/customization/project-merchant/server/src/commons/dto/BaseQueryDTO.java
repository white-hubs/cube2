package com.guhecloud.cube.deliver.project.merchant.commons.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseQueryDTO {

    private String projectId;

    private String search;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
