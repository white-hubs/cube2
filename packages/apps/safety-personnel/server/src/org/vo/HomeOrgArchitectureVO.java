package com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo;

import lombok.Data;

/**
 * 首页安全组织架构响应。
 */
@Data
public class HomeOrgArchitectureVO {

    /**
     * 首页展示根节点；无组织配置时为空，多根时取排序最靠前的根节点。
     */
    private OrgNodeTreeVO root;
}
