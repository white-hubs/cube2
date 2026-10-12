package com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo;

import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNode;
import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织配置树节点响应。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrgNodeTreeVO extends Tree.Simple<Long, OrgNodeTreeVO> {

    /**
     * 组织节点名称。
     */
    private String name;

    /**
     * 同级排序号。
     */
    private Integer sortNo;

    /**
     * 节点成员总数。
     */
    private Integer memberCount;

    /**
     * 是否还有更多成员；首页每节点最多返回5人。
     */
    private Boolean hasMore;

    /**
     * 节点成员摘要；首页和树接口最多返回5人。
     */
    private List<OrgNodeMemberVO> members = new ArrayList<>();

    public static OrgNodeTreeVO of(OrgNode node) {
        OrgNodeTreeVO vo = new OrgNodeTreeVO();
        vo.setId(node.getId());
        vo.setParentId(node.getParentId());
        vo.setName(node.getName());
        vo.setLevel(node.getLevel());
        vo.setSortNo(node.getSortNo());
        return vo;
    }
}
