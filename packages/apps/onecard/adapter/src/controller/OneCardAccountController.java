package com.guhecloud.cube.srv.gen.adapter.onecard.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.AmountChangeParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.CardParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.service.impl.OneCardAccountServiceImpl;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AmountChangeRecordOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CardVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CopiesChangeRecordOutVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2023/2/24 14:36
 * @Author lijie
 */
@Slf4j
@RestController
@RequestMapping("/oneCard")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OneCardAccountController {

    private final OneCardAccountServiceImpl accountService;

    /**
     * @Description 消费变更明细
     * @Author lrj
     */
    @PostMapping("/amount-change")
    public Page<AmountChangeRecordOutVo> getAmountChange(@RequestBody AmountChangeParamQuery queryParam) {

        return accountService.getAmountChange(queryParam);
    }

    /**
     * @Description 份变更明细
     * @Author lrj
     */
    @PostMapping("/copies-change")
    public Page<CopiesChangeRecordOutVo> getCopiesChange(@RequestBody AmountChangeParamQuery queryParam) {

        return accountService.getCopiesChange(queryParam);
    }

    /**
     * @Description 账户状态
     * @Author lrj
     */
    @PostMapping("/account-status-info")
    public AccountOutVo getAccountStatusInfo(@RequestBody AmountChangeParamQuery queryParam) {
        return accountService.getAccountStatusInfo(queryParam);
    }

    /**
     * @Description 卡列表
     * @Author lrj
     */
    @PostMapping("/card/cardIssueList")
    public Page<CardVo> getCards(@RequestBody CardParamQuery queryParam) {
        return accountService.getCards(queryParam);
    }

}
