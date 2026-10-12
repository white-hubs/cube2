package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 修改考试任务关联市场信息
 *
 * @author lanchun
 * @date 2024/4/9 17:17
 */
@Data
public class ChangeHeadquartersExamTaskMarketCommand {

    /**
     * 市场编码
     */
    @NotBlank(message = "市场编码不能为空")
    @Size(max = 50, message = "市场编码长度不能大于50")
    private String marketCode;

    /**
     * 市场租户编号
     */
    @NotBlank(message = "市场租户编号不能为空")
    @Length(max = 10, message = "市场租户编号长度不能大于10位")
    private Integer marketTenantCode;

    /**
     * 市场名称
     */
    @NotBlank(message = "市场名称不能为空")
    @Size(max = 50, message = "市场名称长度不能大于50")
    private String marketName;
}
