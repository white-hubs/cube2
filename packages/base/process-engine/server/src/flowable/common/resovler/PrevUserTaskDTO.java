package com.guhecloud.cube.base.flow.flowable.common.resovler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrevUserTaskDTO {

    private String taskKey;
    private String taskName;

}
