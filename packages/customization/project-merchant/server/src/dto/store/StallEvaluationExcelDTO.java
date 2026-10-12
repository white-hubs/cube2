package com.guhecloud.cube.deliver.project.merchant.dto.store;

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
public class StallEvaluationExcelDTO extends ConvertionDTO<StallEvaluationExcelDTO, StallScoreDTO> {

    public StallEvaluationExcelDTO() {
    }

    public StallEvaluationExcelDTO(StallScoreDTO dto) {
        super();
        map(dto);
    }

    public StallEvaluationExcelDTO(List<StallScoreDTO> dtos) {
        super();
        map(dtos);
    }

    @Excel(name = "档位名称")
    private String name;

    @Excel(name = "所属商户")
    private String companyName;

    @Excel(name = "所属人姓名")
    private String owner;

    @Excel(name = "诚信经营值(星)")
    private float honestyScore;

    @Excel(name = "商品质量(星)")
    private float zScoreAvg = 5f;

    @Excel(name = "诚实守信(星)")
    private float sScoreAvg = 5f;

    @Excel(name = "服务态度(星)")
    private float tScoreAvg = 5f;

    @Excel(name = "环境卫生(星)")
    private float wScoreAvg = 5f;

}
