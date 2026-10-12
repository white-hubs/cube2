package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupUserCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupUserQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberInviteByMobileParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupUserService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupUserResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分组用户相关接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupUserController {
    private final DisinfectionMemberGroupUserService disinfectionMemberGroupUserService;

    /**
     * 获取分组人员
     *
     * @param param 条件
     *
     * @return 分组人员分页列表
     */
    @GetMapping("/bus/member/group-user/page")
    public Pagination<MemberGroupUserResult> groupUserPage(MemberGroupUserQueryParam param) {
        return disinfectionMemberGroupUserService.groupUserPage(param);
    }

    /**
     * 获取分组人员
     *
     * @param param 条件
     *
     * @return 分组人员列表
     */
    @GetMapping("/bus/member/group-user/list")
    public List<MemberGroupUserResult> groupUserList(MemberGroupUserQueryParam param) {
        return disinfectionMemberGroupUserService.groupUserList(param);
    }

    /**
     * 批量添加分组人员
     *
     * @param param 人员信息
     *
     * @return 是否成功
     */
    @PutMapping("/bus/member/group-user/add-batch")
    public Boolean addGroupUserBatch(@RequestBody MemberGroupUserCommandParam param) {
        return disinfectionMemberGroupUserService.addGroupUserBatch(param);
    }

    /**
     * 管理员通过手机号邀请成员
     *
     * @param param 邀请信息
     *
     * @return 是否成功
     */
    @PostMapping("/bus/member/group-user/invite-by-mobile")
    public Boolean inviteByMobile(@Valid @RequestBody MemberInviteByMobileParam param) {
        return disinfectionMemberGroupUserService.inviteByMobile(param);
    }

    /**
     * 批量移除人员消息
     *
     * @param param 人员信息
     *
     * @return 是否成功
     */
    @PutMapping("/bus/member/group-user/remove-batch")
    public Boolean removeGroupUserBatch(@RequestBody MemberGroupUserCommandParam param) {
        return disinfectionMemberGroupUserService.removeGroupUserBatch(param);
    }
}
