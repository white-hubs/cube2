package com.guhecloud.cube.biz.ouaa.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.biz.ouaa.dcm.support.RmdpHelperSupportRepo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Deprecated
public class DcmRoleMenuDataPermission {
    /**
     * @Deprecated 新的权限设计使用
     */
    // ---- group keys --------
    @JsonIgnore
    private String prodCode;

    @JsonIgnore
    private String roleName;

    /**
     * 访问端ID
     */
    @JsonIgnore
    private String terminalCode;

    /**
     * 应用ID
     */
    @JsonIgnore
    private String appCode;

    /**
     * 菜单主键
     */
    @JsonIgnore
    private String menuCode;

    // ----- internal ----------
    /**
     * 权限编码
     */
    private String permission;

    /**
     * 权限排序号
     */
    private int sortNo = 0;

    /**
     * 自定义的业务键值，根据permission编码确定存的数据
     */
    private String businessKeys = "";

    @JsonIgnore
    public RmdpHelperSupportRepo.RoleMenuKey getRoleMenuKey() {
        return RmdpHelperSupportRepo.RoleMenuKey.builder()
                .roleName(roleName)
                .prodCode(prodCode)
                .terminalCode(terminalCode)
                .appCode(appCode)
                .menuCode(menuCode)
                .build();
    }
}
