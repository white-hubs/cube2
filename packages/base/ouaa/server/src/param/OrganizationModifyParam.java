package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 组织机构更新参数
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
public class OrganizationModifyParam {
    private Long id;
    /**
     * 上级主键
     */
    private Long parentId;

    /**
     * 组织机构名称
     */
    @NotEmpty(message = "组织机构名称不能为空")
    @Size(max = 100, message = "组织机构名称长度不能大于100")
    private String name;

    /**
     * 组织机构备注
     */
    @Size(max = 200, message = "组织机构备注长度不能大于200")
    private String remark;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled = true;
    /**
     * 排序数字
     */
    private Float sort;
}
