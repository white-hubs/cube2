package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Getter
public class InspectStandardCommandModel {

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Length(max = 200)
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * true启用 false禁用
     */
    private Boolean enable;

    /**
     * 标准项
     */
    @Valid
    private List<InspectStandardItemCommandModel> standardItems;

    /**
     * 关联的类型
     */
    private List<InspectStandardTypeReferCommandModel> standardTypeRefers;

}
