package com.guhecloud.cube.deliver.project.merchant.dto.store;

import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkService;
import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 档位总分值（安全生产值+评分）
 * </p>
 *
 * @author bq
 * @since 2022-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StallScoreExcelDTO extends ConvertionDTO<StallScoreExcelDTO, StallScoreDTO> {

    public StallScoreExcelDTO() {
    }

    public StallScoreExcelDTO(StallScoreDTO dto) {
        super();
        map(dto);
    }

    public StallScoreExcelDTO(List<StallScoreDTO> dtos) {
        super();
        map(dtos);
    }

    @Excel(name = "档位名称")
    private String name;

    @Excel(name = "所属商户")
    private String companyName;

    @Excel(name = "所属人姓名")
    private String owner;

    @Excel(name = "安全总分值")
    private Integer defaultScore  = StallSafetyMarkService.STORE_DEFAULT_MARK;

    @Excel(name = "扣减分值")
    private int deductScore;

    @Excel(name = "安全生产值（分）")
    private Integer score = StallSafetyMarkService.STORE_DEFAULT_MARK;

    public void setScore(Integer score) {
        this.score = score;
        if (score != null) {
            this.deductScore = this.defaultScore - score;
        }
    }

}
