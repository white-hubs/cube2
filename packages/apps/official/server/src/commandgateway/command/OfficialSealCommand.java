package com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class OfficialSealCommand {
    @NotEmpty(message = "印章名称不能为空")
    @Size(max = 30, message = "名称太长，请小于30个字符")
    private String sealName;
    @Size(max = 256, message = "描述太长，请小于200个字符")
    private String sealDesc;
}
