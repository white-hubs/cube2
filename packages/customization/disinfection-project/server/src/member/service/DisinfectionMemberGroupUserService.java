package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupUser;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupUserCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupUserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberInviteByMobileParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupUserResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface DisinfectionMemberGroupUserService extends IService<MemberGroupUser> {
    Pagination<MemberGroupUserResult> groupUserPage(MemberGroupUserQueryParam param);

    List<MemberGroupUserResult> groupUserList(MemberGroupUserQueryParam param);

    Boolean addGroupUserBatch(MemberGroupUserCommandParam param);

    Boolean inviteByMobile(MemberInviteByMobileParam param);

    Boolean removeGroupUserBatch(MemberGroupUserCommandParam param);

    Boolean editGroupUser(MemberGroupUserCommandParam param);

    List<Long> getGroupIdsByUserId(Long userId);

    List<Long> getUserIdsByGroupIds(List<Long> groupIds);
}
