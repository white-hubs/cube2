package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/13
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerQuestionnaireQueryDTO extends BaseQueryDTO {

    private String name;

    private String status;

    private Integer processingStatus;

    private String type;

    private String createBy;

    private Boolean write;

}
