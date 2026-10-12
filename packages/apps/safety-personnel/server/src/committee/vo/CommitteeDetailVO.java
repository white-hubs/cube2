package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.SafetyCommittee;
import lombok.Data;

import java.util.List;

/**
 * 安委会详情响应。
 */
@Data
public class CommitteeDetailVO {

    /**
     * 安委会ID。
     */
    private Long id;

    /**
     * 安委会名称。
     */
    private String committeeName;

    /**
     * 负责单位/部门ID。
     */
    private Long deptId;

    /**
     * 负责单位/部门名称。
     */
    private String deptName;

    /**
     * 联系电话。
     */
    private String contactPhone;

    /**
     * 简介。
     */
    private String introduction;

    /**
     * 成员数量。
     */
    private Integer memberCount;

    /**
     * 成员列表。
     */
    private List<CommitteeMemberVO> members;

    /**
     * 附件列表，由文件服务返回。
     */
    private List<FileVO> attachments;

    public static CommitteeDetailVO of(SafetyCommittee committee, List<CommitteeMember> members, List<FileVO> attachments) {
        CommitteeDetailVO vo = new CommitteeDetailVO();
        vo.setId(committee.getId());
        vo.setCommitteeName(committee.getCommitteeName());
        vo.setDeptId(committee.getDeptId());
        vo.setDeptName(committee.getDeptName());
        vo.setContactPhone(committee.getContactPhone());
        vo.setIntroduction(committee.getIntroduction());
        vo.setMemberCount(committee.getMemberCount());
        vo.setMembers(members.stream().map(CommitteeMemberVO::of).toList());
        vo.setAttachments(attachments);
        return vo;
    }
}
