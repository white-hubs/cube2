package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Form;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FormDTO extends ConvertionDTO<FormDTO, Form> {

    public FormDTO() {
    }

    public FormDTO(Form form) {
        super();
        map(form);
    }

    public FormDTO(List<Form> list) {
        super();
        map(list);
    }

    private String id;

    private List<String> flowIds;

    @NotNull
    @NotBlank
    private String projectId;

    private String category;

    @NotNull
    @NotBlank
    @Length(max = 50)
    private String name;

    private String code;

    private String type;

    private Boolean enabled;

    private List<FormFieldDTO> fieldDTOS;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime createTime;

}
