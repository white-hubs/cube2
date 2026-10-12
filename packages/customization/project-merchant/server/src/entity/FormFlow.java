package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "form_flow")
public class FormFlow {

    private String formId;

    private String flowId;

}
