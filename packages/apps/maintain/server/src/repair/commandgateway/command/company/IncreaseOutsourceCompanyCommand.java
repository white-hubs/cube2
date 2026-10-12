package com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.company;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/5/18 15:52
 * @description
 */
@Data
public class IncreaseOutsourceCompanyCommand {
    @NotBlank(message = "公司名称不能为空")
    private String name;
    @NotEmpty(message = "委外模式不能为空")
    private Set<Long> outsourceModelIdList;
    @NotEmpty(message = "产品品类不能为空")
    private Set<String> outsourceScopeIdList;
    @NotBlank(message = "责任人不能为空")
    private String responsiblePerson;
    @NotBlank(message = "责任人手机号不能为空")
    private String responsiblePersonPhone;

    public String getOutsourceModelId() {
        return JSONArray.toJSONString(this.outsourceModelIdList);
    }

    public String getOutsourceScopeId() {
        return JSONArray.toJSONString(this.outsourceScopeIdList);
    }
}
