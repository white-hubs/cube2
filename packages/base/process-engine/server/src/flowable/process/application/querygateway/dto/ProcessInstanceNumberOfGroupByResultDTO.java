package com.guhecloud.cube.base.flow.flowable.process.application.querygateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2023/11/13 17:54
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessInstanceNumberOfGroupByResultDTO {
    private String result;

    private Integer number;
}
