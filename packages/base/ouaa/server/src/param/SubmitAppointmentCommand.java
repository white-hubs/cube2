package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author bq
 * @date 2022/10/18
 * @description 提交职位信息命令
 */
@Data
public class SubmitAppointmentCommand {

    private Long id;

    /**
     * 名称
     */
    @Length(max = 20, message = "名称限制在20字内")
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述限制在200字内")
    private String remark;

    private Boolean enable;

}
