package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.FireinspItemEvaluation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 巡检项评价配置
 * </p>
 *
 * @author zwb
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FireinspItemEvaluationDTO extends ConvertionDTO<FireinspItemEvaluationDTO, FireinspItemEvaluation> {

    public FireinspItemEvaluationDTO() {
    }

    public FireinspItemEvaluationDTO(FireinspItemEvaluation evaluation) {
        super();
        map(evaluation);
    }

    public FireinspItemEvaluationDTO(List<FireinspItemEvaluation> evaluations) {
        super();
        map(evaluations);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */

    private String projectId;

    /**
     * 编号
     */

    private String code;

    /**
     * 标准ID
     */
    private String standardId;

    /**
     * 标准名
     */
    private String standardName;

    /**
     * 项ID
     */
    private String itemId;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 项结果编号
     */
    private String itemResultCode;

    /**
     * 项结果名
     */
    private String itemResultName;

    /**
     * 项结果扣分
     */
    @Min(value = 1, message = "扣减分值不能小于1")
    @Max(value = 99, message = "扣减分值不能大于99")
    private Integer itemResultMark;

    /**
     * 启停状态
     */
    private Boolean enabled;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人昵称
     */
    private String createByNickname;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
