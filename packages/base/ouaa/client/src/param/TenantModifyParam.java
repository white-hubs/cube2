package com.guhecloud.cube.biz.ouaa.client.param;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.TenantCategory;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 租户更新参数
 *
 * @author Woody
 * @date 2021/7/14
 */
@Data
public class TenantModifyParam {

    /**
     * 租户名称
     */
    @NotEmpty(message = "租户名称不能为空")
    @Size(max = 100, message = "租户名称长度不能大于100")
    private String name;

    /**
     * 用户管理员主键
     */
    @NotNull(message = "管理员主键不能为空")
    @Min(value = 1, message = "管理员主键不能小于1")
    private Long userAdminId;

    /**
     * 详细地址
     */
    @NotNull(message = "详细地址不能为空")
    @Size(max = 100, message = "详细地址不能大于20")
    private String address;

    /**
     * 所在地
     */
    @NotNull(message = "所在地不能为空")
    @Size(max = 100, message = "所在地不能大于100")
    private List<String> location;

    /**
     * 楼栋数
     */
    private Integer building;

    /**
     * 占地面积
     */
    @Decimal
    private BigDecimal areaCovered;

    /**
     * 企业logo
     */
    private Long logoId;

    /**
     * 宣传图文件主键
     */
    private List<String> fileIds;

    /**
     * 租户备注
     */
    @Size(max = 200, message = "租户备注长度不能大于200")
    private String remark;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled = true;

    /**
     * 租户类型
     */
    private TenantCategory category;
}
