package com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.util.OuaaUserUtils;
import lombok.Data;

/**
 * 组织配置选人候选用户响应。
 */
@Data
public class OrgUserCandidateVO {

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名。
     */
    private String userName;

    /**
     * 手机号，可为空。
     */
    private String mobile;

    /**
     * OUAA部门名称，可为空。
     */
    private String deptName;

    /**
     * OUAA岗位名称，可为空。
     */
    private String postName;

    /**
     * 是否启用，可为空。
     */
    private Boolean enabled;

    public static OrgUserCandidateVO of(UserDTO user) {
        OrgUserCandidateVO vo = new OrgUserCandidateVO();
        vo.setUserId(user.getId());
        vo.setUserName(user.getName());
        vo.setMobile(user.getMobile());
        vo.setDeptName(OuaaUserUtils.resolveDeptName(user));
        vo.setPostName(OuaaUserUtils.resolvePostName(user));
        vo.setEnabled(user.getEnabled());
        return vo;
    }
}
