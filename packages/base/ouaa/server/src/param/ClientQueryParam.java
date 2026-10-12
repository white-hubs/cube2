package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * 客户端查询参数
 *
 * @author lanchun
 * @date 2021/10/25 13:54
 */
@Data
public class ClientQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100个字符")
    private String keyword;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 租户
     */
    @Min(value = 1, message = "租户不能小于1")
    private Integer tenant;
}
