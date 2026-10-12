package com.guhecloud.cube.base.flow.flowable.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstanceNumberOfAllVO {
    private long submitCount;
    private long waitingCount;
    private long finishedCount;
}
