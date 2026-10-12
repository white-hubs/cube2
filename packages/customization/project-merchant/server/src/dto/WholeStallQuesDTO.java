package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestionnaire;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/12
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholeStallQuesDTO {

    public WholeStallQuesDTO() {}

    private String stallId;

    private WholesalerQuestionnaire questionnaire;

    //    //    private WholesalerQuesTempDTO templateDTO;

    private List<WholesalerQuestionDTO> questionDtos;

}
