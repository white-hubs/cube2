package com.guhecloud.cube.srv.gen.adapter.access.control.server.vo;

import lombok.Data;

/**
 * 门禁卡
 */
@Data
public class OrgVO {
    private String id;//用于存放第三方的ID
    private String orgCode;
    private String orgName;
    private String parentId;
    private String orgPath;//例如：默认部门/xfj的组织
}
