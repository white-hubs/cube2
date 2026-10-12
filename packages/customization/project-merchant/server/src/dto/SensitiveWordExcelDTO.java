package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.SensitiveWord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 敏感词库
 * </p>
 *
 * @author bq
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SensitiveWordExcelDTO extends ConvertionDTO<SensitiveWordExcelDTO, SensitiveWord> implements Serializable {

    private static final long serialVersionUID = 1L;

    public SensitiveWordExcelDTO() {
    }

    public SensitiveWordExcelDTO(SensitiveWord word) {
        super();
        map(word);
    }

    public SensitiveWordExcelDTO(List<SensitiveWord> words) {
        super();
        map(words);
    }

    /**
     * 排序
     */
    private Integer order;

    /**
     * 名称
     */
    @Excel(name = "敏感词", width = 25, prompt = "支持中英文数字，限制30字内")
    private String name;

}
