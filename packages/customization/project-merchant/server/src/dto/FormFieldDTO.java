package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.FormField;
import lombok.Data;

import java.util.List;

@Data
public class FormFieldDTO extends ConvertionDTO<FormFieldDTO, FormField> {

    public FormFieldDTO() {
    }

    public FormFieldDTO(FormField formField) {
        super();
        map(formField);
    }

    public FormFieldDTO(List<FormField> list) {
        super();
        map(list);
    }

    private String id;

    private String formId;

    private String label;

    private String type;

    private String props;

    private String defaultValue;

    private Boolean required;

    private Integer sort;

}
