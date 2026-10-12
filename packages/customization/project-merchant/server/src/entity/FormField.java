package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "form_field")
public class FormField {

    private String id;

    private String formId;

    private String label;

    private String type;

    private String props;

    private String defaultValue;

    private Boolean required;

    private Integer sort;

}
