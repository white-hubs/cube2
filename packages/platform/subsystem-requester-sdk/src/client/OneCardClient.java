package com.guhecloud.subsystem.requester.sdk.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.onecard.AmountChangeParamQuery;
import com.guhecloud.subsystem.requester.sdk.param.onecard.CardParamQuery;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.AccountDetailsVO;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.AccountVo;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.AmountChangeRecordVo;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.CanteenOrderListVO;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.CardVo;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.ConsumeRecordOutVO;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.CopiesChangeRecordVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = SubsystemTypeConstants.ONECARD_SERVER, configuration = DynamicConfiguration.class)
public interface OneCardClient {
    /**
     * @Description 消费变更明细
     * @Author lrj
     */
    @PostMapping("/amount-change")
    Page<AmountChangeRecordVo> getAmountChange(@RequestBody AmountChangeParamQuery queryParam);

    /**
     * @Description 份变更明细
     * @Author lrj
     */
    @PostMapping("/copies-change")
    Page<CopiesChangeRecordVo> getCopiesChange(@RequestBody AmountChangeParamQuery queryParam);

    /**
     * @Description 账户状态
     * @Author lrj
     */
    @PostMapping("/account-status-info")
    AccountVo getAccountStatusInfo(@RequestBody AmountChangeParamQuery queryParam);

    /**
     * @Description 卡列表
     * @Author lrj
     */
    @PostMapping("/card/cardIssueList")
    Page<CardVo> getCards(@RequestBody CardParamQuery queryParam);

    /**
     * 批量获取账户信息
     */
    @GetMapping("/account-status-info-batch")
    Page<AccountDetailsVO> getAccountStatusInfoBatch(@SpringQueryMap AmountChangeParamQuery queryParam);

    /**
     * 获取消费记录
     */
    @GetMapping("/get-consume-record")
    Page<ConsumeRecordOutVO> getConsumeRecord(@SpringQueryMap AmountChangeParamQuery queryParam);

    /**
     * 食堂订单列表
     */
    @PostMapping("/canteen-order-list")
    Pagination<CanteenOrderListVO> getCanteenOrderList(@RequestBody AmountChangeParamQuery queryParam);
}
