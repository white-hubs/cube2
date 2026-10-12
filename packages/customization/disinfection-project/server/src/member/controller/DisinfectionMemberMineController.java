package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MineGroupOperationParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MyMemberParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberMineService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MyMemberResult;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 我的(登陆人数据相关接口)
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberMineController {
    private final DisinfectionMemberMineService disinfectionMemberMineService;

    /**
     * 查询当前用户所有组信息
     */
    @GetMapping("/bus/member/mine/groups")
    public List<MemberGroupResult> getUserGroups(String keyword) {
        return disinfectionMemberMineService.getUserGroups(keyword);
    }

    /**
     * 获取我的成员列表
     */
    @GetMapping("/bus/member/mine/members")
    public List<MyMemberResult> getMyMembers(@Valid @ModelAttribute MyMemberParam param) {
        return disinfectionMemberMineService.getMyMembers(param);
    }

    /**
     * 组信息(没有查询当前用户所有默认组会添加)
     *
     */
    @PostMapping("/bus/member/mine/groups-add-default")
    public List<MemberGroupResult> addDefaultGroups() {
        var userId = SecurityContextHolder.getPrincipal().getId();
        var account = SecurityContextHolder.getPrincipal().getAccount();
        disinfectionMemberMineService.userDefaultGroup(userId, account);
        return disinfectionMemberMineService.getUserGroups(null);
    }

    /**
     * 获取用户选择的组(没有就用自己创建的组)
     * （没有创建的组会创建一个组，然后设置选择的组）
     *
     * @return 选择的组id
     */
    @PostMapping("/bus/member/mine/select-group-add-default")
    public MemberGroupResult userSelectGroup() {
        return disinfectionMemberMineService.userSelectGroup(null);
    }

    /**
     * 修改用户选择的组
     *
     * @return 选择的组id
     */
    @PostMapping("/bus/member/mine/select-group-set")
    public Boolean userSelectGroup(@RequestBody MineGroupOperationParam param) {
        return disinfectionMemberMineService.setUserSelectGroup(SecurityContextHolder.getPrincipal().getId(), param.getGroupId());
    }

    /**
     * 退出组
     *
     * @param param groupId 要推出的组
     *
     * @return 是否成功
     */
    @PostMapping("/bus/member/mine/exit-group")
    public Boolean exitGroup(@RequestBody MineGroupOperationParam param) {
        return disinfectionMemberMineService.exitGroup(SecurityContextHolder.getPrincipal().getId(), param.getGroupId());
    }
}
