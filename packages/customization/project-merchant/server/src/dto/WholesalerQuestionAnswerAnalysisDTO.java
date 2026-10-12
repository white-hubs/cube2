package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @author 2022/04/09
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerQuestionAnswerAnalysisDTO {

    private String option;

    private int count;

    private int ratio;

}
