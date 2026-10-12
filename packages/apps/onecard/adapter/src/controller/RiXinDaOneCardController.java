package com.guhecloud.cube.srv.gen.adapter.onecard.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.AmountChangeParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.service.impl.RiXinDaOneCardAccountServiceImpl;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountDetailsVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.ConsumeRecordOutVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日新达消费对接接口
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/16 10:36
 */

@Slf4j
@RestController
@RequestMapping("/RiXinDa")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiXinDaOneCardController {
    private final RiXinDaOneCardAccountServiceImpl accountService;

    /**
     * 获取账户信息账户
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/account-status-info-batch")
    public Page<AccountDetailsVO> getAccountStatusInfo(AmountChangeParamQuery queryParam) {
        return accountService.getAccountStatusInfoBatch(queryParam);
    }

    /**
     * 获取消费记录
     */
    @GetMapping("/get-consume-record")
    public Page<ConsumeRecordOutVO> getConsumeRecord(AmountChangeParamQuery queryParam) {
        return accountService.getConsumeRecord(queryParam);
    }
}
