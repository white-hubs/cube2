package com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo;

import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNode;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNodeMember;
import lombok.Data;

import java.util.List;

/**
 * 组织节点详情响应。
 */
@Data
public class OrgNodeDetailVO {

    /**
     * 组织节点ID。
     */
    private Long id;

    /**
     * 父级组织节点ID，根节点为空。
     */
    private Long parentId;

    /**
     * 组织节点名称。
     */
    private String name;

    /**
     * 节点层级，从1开始。
     */
    private Integer level;

    /**
     * 同级排序号。
     */
    private Integer sortNo;

    /**
     * 节点成员总数。
     */
    private Integer memberCount;

    /**
     * 节点成员列表；详情接口返回全部成员。
     */
    private List<OrgNodeMemberVO> members;

    public static OrgNodeDetailVO of(OrgNode node, List<OrgNodeMember> members) {
        OrgNodeDetailVO vo = new OrgNodeDetailVO();
        vo.setId(node.getId());
        vo.setParentId(node.getParentId());
        vo.setName(node.getName());
        vo.setLevel(node.getLevel());
        vo.setSortNo(node.getSortNo());
        vo.setMemberCount(members.size());
        vo.setMembers(members.stream().map(OrgNodeMemberVO::of).toList());
        return vo;
    }
}
