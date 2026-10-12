package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestion;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/11
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerQuestionDTO extends ConvertionDTO<WholesalerQuestionDTO, WholesalerQuestion> {

    public WholesalerQuestionDTO() {}

    public WholesalerQuestionDTO(WholesalerQuestion question) {
        super();
        map(question);
    }

    public WholesalerQuestionDTO(List<WholesalerQuestion> list) {
        super();
        map(list);
    }

    private String id;

    private String questionnaireTempId;

    private String type;

    private Integer number;

    private String name;

    private String options;

    /**
     * 做答和做答ID都是中间表的内容
     */
    private String answer;

    private String responseId;

    private int stallCount;

    private List<WholesalerQuestionAnswerAnalysisDTO> answerAnalysisDTOS;

    private String createBy;

    private LocalDateTime createTime;

}
