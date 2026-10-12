package com.guhecloud.cube.base.flow.form.interfaces.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDataParam {
    //表单定义的ID
    @NotNull(message = "")
    private Long formId;
    //表单Json字符串
    @NotEmpty
    private String formData;
}
