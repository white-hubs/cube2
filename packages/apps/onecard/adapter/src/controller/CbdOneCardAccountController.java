package com.guhecloud.cube.srv.gen.adapter.onecard.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.AmountChangeParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.CardParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.service.impl.CbdLvZhouOneCardAccountServiceImpl;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AmountChangeRecordOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CanteenOrderListVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CardVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CopiesChangeRecordOutVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cbdOneCard")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CbdOneCardAccountController {

    private final CbdLvZhouOneCardAccountServiceImpl cbdLvZhouOneCardAccountService;

    /**
     * @ 消费变更明细
     */
    @PostMapping("/amount-change")
    public Page<AmountChangeRecordOutVo> getAmountChange(@RequestBody AmountChangeParamQuery queryParam) {

        return cbdLvZhouOneCardAccountService.getAmountChange(queryParam);
    }
    
    /**
     * @ 食堂订单列表
     */
    @PostMapping("/canteen-order-list")
    public Pagination<CanteenOrderListVO> getCanteenOrderList(@RequestBody AmountChangeParamQuery queryParam) {
        return cbdLvZhouOneCardAccountService.getCanteenOrderList(queryParam);
    }

    /**
     * @ 份变更明细
     */
    @PostMapping("/copies-change")
    public Page<CopiesChangeRecordOutVo> getCopiesChange(@RequestBody AmountChangeParamQuery queryParam) {

        return cbdLvZhouOneCardAccountService.getCopiesChange(queryParam);
    }

    /**
     * @ 账户状态
     */
    @PostMapping("/account-status-info")
    public AccountOutVo getAccountStatusInfo(@RequestBody AmountChangeParamQuery queryParam) {
        return cbdLvZhouOneCardAccountService.getAccountStatusInfo(queryParam);
    }

    /**
     * @ 卡列表
     */
    @PostMapping("/card/cardIssueList")
    public Page<CardVo> getCards(@RequestBody CardParamQuery queryParam) {
        return cbdLvZhouOneCardAccountService.getCards(queryParam);
    }

}
