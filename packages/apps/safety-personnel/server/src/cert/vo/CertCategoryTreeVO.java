package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo;

import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertCategory;
import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 证书分类树节点响应。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CertCategoryTreeVO extends Tree.Simple<Long, CertCategoryTreeVO> {

    /**
     * 证书分类名称。
     */
    private String name;

    /**
     * 同级排序号。
     */
    private Integer sortNo;

    public static CertCategoryTreeVO of(CertCategory category) {
        CertCategoryTreeVO vo = new CertCategoryTreeVO();
        vo.setId(category.getId());
        vo.setParentId(category.getParentId());
        vo.setName(category.getName());
        vo.setLevel(category.getLevel());
        vo.setSortNo(category.getSortNo());
        return vo;
    }
}
