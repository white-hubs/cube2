package com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.util.OuaaUserUtils;
import lombok.Data;

/**
 * 安全人员池候选用户响应。
 */
@Data
public class PersonPoolUserVO {

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
     * 是否启用。
     */
    private Boolean enabled;

    public static PersonPoolUserVO of(UserDTO user) {
        PersonPoolUserVO vo = new PersonPoolUserVO();
        vo.setUserId(user.getId());
        vo.setUserName(user.getName());
        vo.setPhone(user.getMobile());
        vo.setDeptName(OuaaUserUtils.resolveDeptName(user));
        vo.setPostName(OuaaUserUtils.resolvePostName(user));
        vo.setEnabled(user.getEnabled());
        return vo;
    }
}
