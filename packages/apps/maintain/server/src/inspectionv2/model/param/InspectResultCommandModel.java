package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Getter
public class InspectResultCommandModel {

    private String code;

    /**
     * 巡检结果名称
     */
    @NotEmpty(message = "名称不能为空")
    @Length(max = 30)
    private String name;

    /**
     * 1 启用 0禁用
     */
    private Boolean enable;

    /**
     * 巡检结果选项
     */
    @Valid
    private List<InspectResultOptionCommandModel> options;

}
