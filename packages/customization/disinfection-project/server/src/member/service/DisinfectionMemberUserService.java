package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberUserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberUserResult;
import com.guhecloud.platform.base.model.Pagination;

public interface DisinfectionMemberUserService {

    /**
     * 获取用户列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    Pagination<MemberUserResult> getMemberUserPage(MemberUserQueryParam param);

    Boolean deleteMemberUser(Long id);
}
