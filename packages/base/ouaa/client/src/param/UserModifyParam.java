package com.guhecloud.cube.biz.ouaa.client.param;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户更新参数
 *
 * @author Woody
 * @date 2021/7/20
 */
@Data
public class UserModifyParam {

    /**
     * 主键id
     */
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
    @NotEmpty(message = "联系电话不能为空")
    @Size(min = 11, max = 100, message = "联系电话长度不能小于11位且大于100位")
    private String mobile;

    /**
     * 用户性别
     */
    private Gender gender;

    /**
     * 用户备注
     */
    @Size(max = 200, message = "用户备注长度不能大于200")
    private String remark;

    /**
     * 登录账号
     */
    @Size(max = 100, message = "登录账号长度不能大于100")
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled = true;

    /**
     * 入职日期
     */
    private LocalDate entryDate;

    /**
     * 组织机构主键
     */
    private List<Long> organizationIds;

    /**
     * 岗位主键列表
     */
    private List<Long> positionIds;

    /**
     * 角色主键列表
     */
    private List<Long> roleIds;

    /**
     * 租户编号
     */
    private Integer tenant;
    /**
     * 职位ID
     */
    private List<Long> appointmentIds;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * client调用不发送消息
     */
    private Boolean sendMessage = true;

    /**
     * 账号来源
     */
    private String source;
}
