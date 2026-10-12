package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 平台管理用户修改
 *
 * @author lzy
 * @date 2021/10/11
 */
@Data
public class UserUpdateParam {

    /**
     * 用户主键
     */
    @NotNull(message = "用户主键不能为空")
    @Min(value = 1, message = "用户主键不能小于1")
    private Long id;

    /**
     * 用户姓名
     */
    @NotEmpty(message = "用户姓名不能为空")
    @Size(max = 100, message = "用户姓名长度不能大于100")
    private String name;

    /**
     * 联系电话
     */
    @Size(max = 100, message = "联系电话长度不能大于100")
    private String mobile;

    /**
     * 用户性别
     */
    @NotNull(message = "用户性别不能为空")
    private Gender gender;

    /**
     * 是否启用
     */
    @NotNull(message = "状态启用不能为空")
    private Boolean enabled;

    /**
     * 用户备注
     */
    @Size(max = 200, message = "用户备注长度不能大于200")
    private String remark;

    /**
     * 文件主键
     */
    private Long fileId;

}
