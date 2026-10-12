package com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全管理组织展示节点。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_org_node")
public class OrgNode extends BasicEnhancedTenantTreeEntity<Long> {

    /**
     * 节点ID。
     */
    private Long id;

    /**
     * 节点名称。
     */
    private String name;

    /**
     * 同级排序号，值越小越靠前。
     */
    private Integer sortNo;
}
