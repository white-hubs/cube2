package com.guhecloud.cube.onecard.server.consumption.controller;

import com.guhecloud.cube.onecard.server.consumption.enumeration.AccountNameEnum;
import com.guhecloud.cube.onecard.server.consumption.param.AccountInfoQueryParam;
import com.guhecloud.cube.onecard.server.consumption.service.AccountService;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountDetailsVO2;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountDetailsVo;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountNameVo;
import com.guhecloud.cube.onecard.server.consumption.vo.AccountVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 账户管理
 *
 * @Date 2023/2/24 14:36
 * @Author tangzhiqiang
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class AccountController {

    private final AccountService accountService;

    /**
     * 账户管理分页
     *
     * @Author tangzhiqiang
     */
    @GetMapping("/accountInfo")
    public Pagination<AccountVo> getAccountInfo(AccountInfoQueryParam param) {
        return accountService.getConsumptionInfo(param);
    }

    /**
     * 账户管理详情
     *
     * @Date 2023/2/28 12:33
     * @Author tangzhiqiang
     */
    @GetMapping("/account-details/{id}")
    public AccountDetailsVo getAccountDetails(@PathVariable Long id) {
        return accountService.getConsumptionDetails(id);
    }

    /**
     * 账户状态下拉框
     *
     * @Date 2023/3/1 11:44
     * @Author tangzhiqiang
     */
    @GetMapping("/account-status")
    public List<AccountNameVo> getAccountName() {
        return AccountNameEnum.list();
    }

    /**
     * 获取指定员工的账户信息
     */
    @GetMapping("/account-info/{id}")
    public AccountDetailsVO2 getAccountInfo(@PathVariable Long id) {
        return accountService.getAccountInfo(id);
    }

}
