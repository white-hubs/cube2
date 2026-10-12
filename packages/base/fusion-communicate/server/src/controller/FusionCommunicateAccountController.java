package com.guhecloud.cube.biz.fusion.communicate.server.controller;

import com.guhecloud.cube.biz.fusion.communicate.server.param.UserQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.service.FusionCommunicateAccountService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.AccountQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionCommunicateAccountVO;
import com.guhecloud.cube.biz.fusion.communicate.server.vo.UserVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 通讯账号管理接口
 *
 * @author lanchun
 * @date 2024/7/29 14:09
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FusionCommunicateAccountController {

    private final FusionCommunicateAccountService fusionCommunicateAccountService;

    /**
     * 通讯账号分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/fusion-communicate-account/page")
    public Pagination<FusionCommunicateAccountVO> getThirdFusionAccountPage(@Valid AccountQueryParam query) {
        return this.fusionCommunicateAccountService.getThirdFusionAccountPage(query);
    }

    /**
     * 根据融合通讯账号获取系统账号
     *
     * @param fusionCommunicateAccount
     *
     * @return
     */
    @GetMapping("/fusion-communicate/sys-user/{fusionCommunicateAccount}")
    public UserVO getSysUserByFusionCommunicateAccount(@PathVariable("fusionCommunicateAccount") String fusionCommunicateAccount
                                                      ) {
        return this.fusionCommunicateAccountService.getSysUserByFusionCommunicateAccount(fusionCommunicateAccount);
    }

    /**
     * 根据系统用户主键获取已获得融合通讯账号的系统用户
     *
     * @param sysUserIds
     *
     * @return
     */
    @PostMapping("/fusion-communicate/sys-users")
    public List<UserVO> geSysUsersByIds(@RequestBody Set<Long> sysUserIds) {
        return this.fusionCommunicateAccountService.getOnlineUserIds(sysUserIds);
    }

    /**
     * 给系统用户分配融合通讯账号
     *
     * @param userIds
     *
     * @return
     */
    @PostMapping("/fusion-communicate/assign/sys-user")
    public List<AccountAllocationUserVO> assignFusionCommunicateAccount(@RequestBody Set<Long> userIds) {
        return this.fusionCommunicateAccountService.assignFusionCommunicateAccount(userIds);
    }

    /**
     * 当前用户分配通讯账号
     *
     * @return 通讯账号
     */
    @PostMapping("/fusion-communicate/assign/current-user")
    public AccountAllocationUserVO assignCurrentFusionCommunicateAccount() {
        List<AccountAllocationUserVO> accountAllocationUserVOS = this.fusionCommunicateAccountService
                .assignFusionCommunicateAccount(Collections.singleton(SecurityContextHolder.getPrincipal().getId()));
        if (CollectionUtils.isEmpty(accountAllocationUserVOS)) {
            return null;
        }
        return accountAllocationUserVOS.get(0);
    }

    /**
     * 释放账号
     *
     * @param userIds
     */
    @PutMapping("/fusion-communicate/disable")
    public void disableFusionAccount(@RequestBody Set<Long> userIds) {
        this.fusionCommunicateAccountService.disableFusionAccount(userIds);
    }

    /**
     * 释放当前用户账号
     */
    @PutMapping("/fusion-communicate/disable/current-user")
    public void disableCurrentFusionAccount() {
        this.fusionCommunicateAccountService.disableFusionAccount(Collections.singleton(SecurityContextHolder.getPrincipal().getId()));
    }

    /**
     * 保活账号
     *
     * @param userIds
     */
    @PutMapping("/fusion-communicate/keep-alive")
    public void keepAliveFusionAccount(@RequestBody Set<Long> userIds) {
        this.fusionCommunicateAccountService.keepAliveFusionAccount(userIds);
    }

    /**
     * 保活当前用户账号
     */
    @PutMapping("/fusion-communicate/current-user/keep-alive")
    public void keepAliveCurrentUserFusionAccount() {
        this.fusionCommunicateAccountService.keepAliveFusionAccount(Collections.singleton(SecurityContextHolder.getPrincipal().getId()));
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @GetMapping("/fusion-communicate/users")
    public List<UserVO> getUsers(@Valid UserQueryParam param) {
        return this.fusionCommunicateAccountService.getUsers(param);
    }
}