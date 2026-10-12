package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.GroupPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.GroupQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.GroupUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.GroupInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.GroupUserPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消杀分组接口
 */
@RestController
@RequestMapping("/disinfection/projects/{projectId}/groups")
@RequiredArgsConstructor
@Slf4j
public class BusinessDisinfectionGroupController {
    private final BusinessDisinfectionGroupService businessGroupService;

    /**
     * 根据条件获取全部列表
     */
    @GetMapping("/list-all")
    public List<GroupInfoVO> listAll(GroupQueryParam param) {
        return businessGroupService.listAll(param);
    }

    /**
     * 新增小组
     */
    @PostMapping
    public Boolean insert(@RequestBody @Valid GroupUpsertParam groupUpsertParam) {
        return businessGroupService.insert(groupUpsertParam);

    }

    /**
     * 删除小组
     */
    @DeleteMapping("{groupId}")
    public Boolean delete(@PathVariable Long groupId) {
        return businessGroupService.delete(groupId);
    }

    /**
     * 修改小组
     */
    @PutMapping
    public Boolean batchUpdate(@RequestBody @Valid GroupUpsertParam groupUpsertParam) {
        return businessGroupService.update(groupUpsertParam);
    }

    /**
     * 修改单个小组
     */
    @PutMapping("{groupId}")
    public Boolean singleUpdate(@RequestBody @Valid GroupUpsertParam.GroupParam groupParam) {
        return businessGroupService.singleUpdate(groupParam);
    }

    /**
     * 获取分组分页信息
     */
    @GetMapping
    public Pagination<GroupUserPageVO> list(@PathVariable String projectId, GroupPageParam param) {
        return businessGroupService.pageGroup(projectId, param);
    }

    /**
     * 小组成员信息,返回用户id列表
     */
    @GetMapping("{groupId}/members")
    public List<Long> members(@PathVariable Long groupId) {
        return businessGroupService.groupMembers(groupId);
    }

    /**
     * 小组新增成员
     */
    @PostMapping("{groupId}/members")
    public Boolean addMember(@PathVariable Long groupId, Long userId) {
        return businessGroupService.addMember(groupId, userId);
    }

    /**
     * 小组删除成员
     */
    @DeleteMapping("{groupId}/members")
    public Boolean deleteMember(@PathVariable Long groupId, Long userId) {
        return businessGroupService.deleteMember(groupId, userId);
    }

    /**
     * 查询小组角色
     */
    @GetMapping("{groupId}/roles")
    public List<UserDTO.Role> groupRoles(@PathVariable Long groupId) {
        return businessGroupService.roles(groupId);
    }
}
