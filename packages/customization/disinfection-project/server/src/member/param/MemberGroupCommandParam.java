package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroup;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.Data;

@Data
public class MemberGroupCommandParam {
    /**
     * 主键
     */
    private Long id;
    /**
     * 管理员id
     */
    private Long memberUserId;
    /**
     * 分组名
     */
    private String groupName;
    /**
     * 分组人数
     */
    private Integer userCount;
    /**
     * 描述
     */
    private String description;

    public MemberGroup createMemberGroup() {
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setId(id == null ? IdWorker.getId() : id);
        memberGroup.setMemberUserId(memberUserId == null ? SecurityContextHolder.getPrincipal().getId() : memberUserId);
        memberGroup.setGroupName(groupName);
        memberGroup.setUserCount(userCount == null ? 1 : userCount);
        memberGroup.setDescription(description);
        return memberGroup;
    }
}
