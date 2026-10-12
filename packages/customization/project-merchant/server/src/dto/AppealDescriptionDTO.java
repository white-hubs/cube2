package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AppealDescription;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppealDescriptionDTO extends ConvertionDTO<AppealDescriptionDTO, AppealDescription> {

    public AppealDescriptionDTO() {
    }

    public AppealDescriptionDTO(AppealDescription appealDescription) {
        super();
        map(appealDescription);
    }

    public AppealDescriptionDTO(List<AppealDescription> list) {
        super();
        map(list);
    }

    private String id;

    private String code;

    @NotBlank
    @NotNull
    @Length(min = 1, max = 15)
    private String name;

    @NotBlank
    @NotNull
    private String type;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;


}
