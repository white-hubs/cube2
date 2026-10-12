package com.guhecloud.cube.biz.ouaa.server.param.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * @author lanchun
 * @date 2025/4/18 11:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserQuery extends Paging {

    /**
     * 关键字(可根据用户名、手机号模糊查询)
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 性别
     */
    private String gender;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否是管理员
     */
    private Boolean isAdmin;
}
