package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/11/11 13:08
 * @description
 */
@Data
public class TaskInfoWithBusinessTypeQuery extends Paging {
    private String businessType;
    private String account;
    private Period submitTime;
}
