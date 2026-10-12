package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import lombok.Data;

/**
 * 门禁卡
 */
@Data
public class OrgModel {
    private String orgIndexCode;//用于存放第三方的ID
    private String organizationCode;
    private String orgName;
    private String parentOrgIndexCode;
    private String orgPath;//例如：默认部门/xfj的组织
}
