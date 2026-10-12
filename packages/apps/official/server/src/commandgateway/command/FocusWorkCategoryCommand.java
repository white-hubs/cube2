package com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class FocusWorkCategoryCommand {
    @Size(min = 1, max = 30, message = "督办类型名称长度太长，请小于30个字符")
    private String name;
    @Size(max = 256, message = "备注信息不能太长，请小于200个字符")
    private String remark;
}
