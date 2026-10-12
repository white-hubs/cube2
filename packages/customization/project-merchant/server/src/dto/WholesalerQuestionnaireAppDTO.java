package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestionnaire;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/13
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerQuestionnaireAppDTO extends ConvertionDTO<WholesalerQuestionnaireAppDTO, WholesalerQuestionnaire> {
    public WholesalerQuestionnaireAppDTO() {}

    public WholesalerQuestionnaireAppDTO(WholesalerQuestionnaire questionnaire) {
        super();
        map(questionnaire);
    }

    public WholesalerQuestionnaireAppDTO(List<WholesalerQuestionnaire> list) {
        super();
        map(list);
    }

    private String id;

    private String name;

    private String tempType;

    private Integer done;

    private LocalDateTime createTime;
}
