package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户个人修改参数
 *
 * @author lanchun
 * @date 2022/8/11 16:17
 */
@Data
public class UserPersonalUpdateParam {

    /**
     * 用户姓名
     */
    @NotEmpty(message = "用户姓名不能为空")
    @Size(max = 100, message = "用户姓名长度不能大于100")
    private String name;

    /**
     * 用户性别
     */
    @NotNull(message = "用户性别不能为空")
    private Gender gender;

    /**
     * 文件主键
     */
    private Long fileId;

    /**
     * 用户备注
     */
    @Size(max = 200, message = "用户备注长度不能大于200")
    private String remark;
}
