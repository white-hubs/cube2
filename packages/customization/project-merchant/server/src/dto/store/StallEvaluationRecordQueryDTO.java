package com.guhecloud.cube.deliver.project.merchant.dto.store;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author benhuabao
 * @date 2021/12/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StallEvaluationRecordQueryDTO extends BaseQueryDTO {

    private String stallId;

    private String condition;

    private Integer examineStatus;

    private Boolean handled;

    private Integer underStar;

}
