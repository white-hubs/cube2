package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.controller;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.AccountQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionCommunicateAccountVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.XunYiFusionCommunicateAccountProvider;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 讯一通讯账号管理接口
 *
 * @author lanchun
 * @date 2024/7/29 14:09
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class XunYiAccountController {

    private final XunYiFusionCommunicateAccountProvider xunYiFusionCommunicateAccountProvider;

    /**
     * 通讯账号分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/xun-yi/fusion-communicate/account/page")
    public Pagination<FusionCommunicateAccountVO> getXunYiThirdFusionAccountPage(@Valid AccountQueryParam query) {
        return this.xunYiFusionCommunicateAccountProvider.getThirdFusionAccountPage(query);
    }

    /**
     * 根据融合通讯账号获取系统账号
     *
     * @param fusionCommunicateAccount
     *
     * @return
     */
    @GetMapping("/xun-yi/fusion-communicate/sys-user/{fusionCommunicateAccount}")
    public Long getXunYiSysUserByFusionCommunicateAccount(
            @PathVariable("fusionCommunicateAccount") String fusionCommunicateAccount
                                                         ) {
        return this.xunYiFusionCommunicateAccountProvider.getSysUserByFusionCommunicateAccount(fusionCommunicateAccount);
    }

    /**
     * 根据系统用户主键获取已获得融合通讯账号的系统用户主键集合
     *
     * @param sysUserIds
     *
     * @return
     */
    @PostMapping("/xun-yi/fusion-communicate/sys-users")
    public List<AccountAllocationUserVO> getXunYiSysUsersByIds(@RequestBody Set<Long> sysUserIds) {
        return this.xunYiFusionCommunicateAccountProvider.getOnlineUserIds(sysUserIds);
    }

    /**
     * 给系统用户分配融合通讯账号
     *
     * @param userIds
     *
     * @return
     */
    @PostMapping("/xun-yi/fusion-communicate/assign/sys-user")
    public List<AccountAllocationUserVO> assignXunYiFusionCommunicateAccount(@RequestBody Set<Long> userIds) {
        return this.xunYiFusionCommunicateAccountProvider.assignFusionCommunicateAccount(userIds);
    }
}
