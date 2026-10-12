package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Faq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author benhuabao
 * @date 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FaqDTO extends ConvertionDTO<FaqDTO, Faq> {

    public FaqDTO() {}

    public FaqDTO(Faq faq) {
        super();
        map(faq);
    }

    public FaqDTO(List<Faq> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    @NotNull
    @NotBlank
    private String type;

    @NotNull
    @NotBlank
    private String question;

    private LocalDateTime createDay;

    @NotNull
    @NotBlank
    private String answer;

    private List<String> fileIds;

    private List<FileVO> FileVOS;

    private String createBy;

    private LocalDateTime createTime;

}
