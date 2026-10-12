package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分组相关接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupController {
    private final DisinfectionMemberGroupService disinfectionMemberGroupService;

    /**
     * 组织分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/bus/member/group/page")
    public Pagination<MemberGroupResult> getMemberGroupPage(MemberGroupQueryParam param) {
        return disinfectionMemberGroupService.getMemberGroupPage(param);
    }

    /**
     * 获取分组信息
     *
     * @param id 主键
     *
     * @return 组信息
     */
    @GetMapping("/bus/member/group/detail")
    public MemberGroupResult getMemberGroupById(Long id) {
        return disinfectionMemberGroupService.getMemberGroupById(id);
    }

    /**
     * 添加组
     *
     * @param param 组信息
     *
     * @return id
     */
    @PostMapping("/bus/member/group/add")
    public Long addMemberGroup(@RequestBody MemberGroupCommandParam param) {
        return disinfectionMemberGroupService.addMemberGroup(param);
    }

    /**
     * 删除组
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/bus/member/group/remove")
    public Boolean removeMemberGroup(Long id) {
        return disinfectionMemberGroupService.removeMemberGroup(id);
    }

    /**
     * 修改组信息
     *
     * @param param 组
     *
     * @return 是否成功
     */
    @PutMapping("/bus/member/group/only-edit")
    public Boolean editMemberGroup(@RequestBody MemberGroupCommandParam param) {
        return disinfectionMemberGroupService.editMemberGroup(param);
    }
}
