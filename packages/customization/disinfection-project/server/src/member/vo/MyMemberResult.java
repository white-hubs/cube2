package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

@Data
public class MyMemberResult {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 成员角色：MemberGroupRoleEnum
     */
    private String role;
    /**
     * 头像
     */
    private FileVO avatar;
}
