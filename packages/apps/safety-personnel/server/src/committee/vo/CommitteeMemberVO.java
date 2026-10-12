package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo;

import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeMember;
import lombok.Data;

/**
 * 安委会成员响应。
 */
@Data
public class CommitteeMemberVO {

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名。
     */
    private String userName;

    /**
     * 手机号。
     */
    private String phone;

    /**
     * OUAA部门名称。
     */
    private String deptName;

    /**
     * OUAA岗位名称。
     */
    private String postName;

    /**
     * 是否在任。
     */
    private Boolean active;

    public static CommitteeMemberVO of(CommitteeMember member) {
        CommitteeMemberVO vo = new CommitteeMemberVO();
        vo.setUserId(member.getUserId());
        vo.setUserName(member.getUserName());
        vo.setPhone(member.getPhone());
        vo.setDeptName(member.getDeptName());
        vo.setPostName(member.getPostName());
        vo.setActive(member.getActive());
        return vo;
    }
}
