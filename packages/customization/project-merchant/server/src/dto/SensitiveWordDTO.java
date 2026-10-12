package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.SensitiveWord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SensitiveWordDTO extends ConvertionDTO<SensitiveWordDTO, SensitiveWord> implements Serializable {

    private static final long serialVersionUID = 1L;

    public SensitiveWordDTO() {
    }

    public SensitiveWordDTO(SensitiveWord word) {
        super();
        map(word);
    }

    public SensitiveWordDTO(List<SensitiveWord> words) {
        super();
        map(words);
    }

    private String id;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 名称
     */
    @NotNull
    @NotBlank
    @Length(max = 30, message = "敏感词最大不超过30字")
    private String name;

}
