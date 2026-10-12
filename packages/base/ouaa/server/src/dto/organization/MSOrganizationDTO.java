package com.guhecloud.cube.biz.ouaa.server.dto.organization;

import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-06  16:39
 * @Description:
 * @Version: 1.0
 */
@Data
public class MSOrganizationDTO {
    private Long id;
    private String jgbm;
    private String Jgmc;
    private Long sjjg;
    private String remark;
    private String px;
    private String jgdescribe;
    private String sjjgmc;
    private Integer level;
    private List<MSOrganizationDTO> children;

    public Organization getOrganization() {
        Organization organization = new Organization();
        organization.setId(this.id);
        organization.setName(this.Jgmc);
        organization.setParentId(this.sjjg == 1 ? null : this.sjjg);
        organization.setRemark(this.remark);
        organization.setSort(0F);
        return organization;
    }
}
