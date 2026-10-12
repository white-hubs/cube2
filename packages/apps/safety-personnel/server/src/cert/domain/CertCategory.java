package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 证书分类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_cert_category")
public class CertCategory extends BasicEnhancedTenantTreeEntity<Long> {

    /**
     * 证书分类ID。
     */
    private Long id;

    /**
     * 证书分类名称。
     */
    private String name;

    /**
     * 同级排序号。
     */
    private Integer sortNo;
}
