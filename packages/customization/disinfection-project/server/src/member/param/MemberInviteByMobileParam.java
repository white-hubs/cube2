package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberInviteByMobileParam {
    /**
     * 分组id
     */
    @NotNull(message = "分组不能为空")
    private Long groupId;

    /**
     * 被邀请成员手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;
}
