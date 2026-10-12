package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo;

import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.SafetyCommittee;
import lombok.Data;

/**
 * 安委会分页响应。
 */
@Data
public class CommitteeVO {

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
     * 简介。
     */
    private String introduction;

    /**
     * 联系电话。
     */
    private String contactPhone;

    /**
     * 附件数量。
     */
    private Integer attachmentCount;

    /**
     * 首个附件名称，用于列表展示。
     */
    private String firstAttachmentName;

    /**
     * 成员数量。
     */
    private Integer memberCount;

    public static CommitteeVO of(SafetyCommittee committee) {
        return of(committee, 0, null);
    }

    public static CommitteeVO of(SafetyCommittee committee, int attachmentCount, String firstAttachmentName) {
        CommitteeVO vo = new CommitteeVO();
        vo.setId(committee.getId());
        vo.setCommitteeName(committee.getCommitteeName());
        vo.setDeptId(committee.getDeptId());
        vo.setDeptName(committee.getDeptName());
        vo.setIntroduction(committee.getIntroduction());
        vo.setContactPhone(committee.getContactPhone());
        vo.setMemberCount(committee.getMemberCount());
        vo.setAttachmentCount(attachmentCount);
        vo.setFirstAttachmentName(firstAttachmentName);
        return vo;
    }
}
