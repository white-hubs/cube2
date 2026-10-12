package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestionnaire;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/12
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerQuestionnaireDTO extends ConvertionDTO<WholesalerQuestionnaireDTO, WholesalerQuestionnaire> {

    public WholesalerQuestionnaireDTO() {}

    public WholesalerQuestionnaireDTO(WholesalerQuestionnaire questionnaire) {
        super();
        map(questionnaire);
    }

    public WholesalerQuestionnaireDTO(List<WholesalerQuestionnaire> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String code;

    private String name;

    private Integer processingStatus;

    private String tempId;

    private String tempType;

    private String tempName;

    private String status;

    private String description;

    /**
     * 开始时间是否是现在
     */
    private boolean beginTimeIsNow;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private String xxlTaskIds;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted = false;

    private List<String> stallIds;

    private List<WholeSpaceDTO> stallDtos;

    private Integer count;

    private Integer signedCount;

    private Integer notSignedCount;
}
