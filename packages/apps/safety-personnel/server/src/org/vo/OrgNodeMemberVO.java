package com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelOuaaFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNodeMember;
import lombok.Data;

/**
 * 组织节点成员响应。
 */
@Data
public class OrgNodeMemberVO {

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名。
     */
    private String userName;

    /**
     * OUAA部门名称，可为空。
     */
    private String deptName;

    /**
     * 手机号，可为空；首页展示时为脱敏值。
     */
    private String mobile;

    /**
     * 用户照片首个文件信息，可为空；首页按此字段展示头像。
     */
    private FileVO avatar;

    public static OrgNodeMemberVO of(OrgNodeMember member) {
        return of(member, false, null, null);
    }

    /**
     * 构建节点成员展示对象。
     *
     * @param member      成员实体
     * @param maskMobile  是否脱敏手机号（首页为 true）
     * @param ouaaFacade  OUAA门面，脱敏时必填
     *
     * @return 成员 VO
     */
    public static OrgNodeMemberVO of(OrgNodeMember member, boolean maskMobile, SafetyPersonnelOuaaFacadeService ouaaFacade) {
        return of(member, maskMobile, ouaaFacade, null);
    }

    /**
     * 构建节点成员展示对象。
     *
     * @param member     成员实体
     * @param maskMobile 是否脱敏手机号（首页为 true）
     * @param ouaaFacade OUAA门面，脱敏时必填
     * @param avatar     用户头像文件信息
     *
     * @return 成员 VO
     */
    public static OrgNodeMemberVO of(OrgNodeMember member, boolean maskMobile, SafetyPersonnelOuaaFacadeService ouaaFacade, FileVO avatar) {
        OrgNodeMemberVO vo = new OrgNodeMemberVO();
        vo.setUserId(member.getUserId());
        vo.setUserName(member.getUserName());
        vo.setDeptName(member.getDeptName());
        String mobile = member.getMobile();
        if (maskMobile && ouaaFacade != null) {
            mobile = ouaaFacade.desensitizeMobile(mobile);
        }
        vo.setMobile(mobile);
        vo.setAvatar(avatar);
        return vo;
    }
}
