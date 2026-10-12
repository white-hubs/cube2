package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberGroupRoleEnum;
import lombok.Data;

import java.util.List;

@Data
public class MemberGroupUserCommandParam {
    private Long groupId;
    private String role = MemberGroupRoleEnum.MEMBER.name();
    private List<Long> userIdList;
}
