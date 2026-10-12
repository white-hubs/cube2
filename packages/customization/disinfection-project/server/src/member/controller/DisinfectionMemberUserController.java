package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberUserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberUserResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消杀用户相关接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberUserController {
    private final DisinfectionMemberUserService disinfectionMemberUserService;

    /**
     * 获取用户列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/bus/member/user/page")
    public Pagination<MemberUserResult> getMemberUserPage(MemberUserQueryParam param) {
        return disinfectionMemberUserService.getMemberUserPage(param);
    }

    /**
     * 删除用户
     *
     * @param id 用户主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/bus/member/user/remove")
    public Boolean deleteMemberUser(Long id) {
        return disinfectionMemberUserService.deleteMemberUser(id);
    }
}
