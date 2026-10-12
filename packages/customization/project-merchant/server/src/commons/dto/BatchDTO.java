package com.guhecloud.cube.deliver.project.merchant.commons.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class BatchDTO {

    private String projectId;

    private List<String> ids;

}
