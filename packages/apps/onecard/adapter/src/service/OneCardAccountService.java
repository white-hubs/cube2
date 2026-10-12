package com.guhecloud.cube.srv.gen.adapter.onecard.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.AmountChangeParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.param.CardParamQuery;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountDetailsVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AccountOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.AmountChangeRecordOutVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CanteenOrderListVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CardVo;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.ConsumeRecordOutVO;
import com.guhecloud.cube.srv.gen.adapter.onecard.server.vo.CopiesChangeRecordOutVo;
import com.guhecloud.platform.base.model.Pagination;

public interface OneCardAccountService {
    Page<AmountChangeRecordOutVo> getAmountChange(AmountChangeParamQuery queryParam);

    default Pagination<CanteenOrderListVO> getCanteenOrderList(AmountChangeParamQuery queryParam) {
        return null;
    }

    Page<CopiesChangeRecordOutVo> getCopiesChange(AmountChangeParamQuery queryParam);

    AccountOutVo getAccountStatusInfo(AmountChangeParamQuery queryParam);

    Page<CardVo> getCards(CardParamQuery queryParam);

    /**
     * 批量获取账户信息
     */
    Page<AccountDetailsVO> getAccountStatusInfoBatch(AmountChangeParamQuery queryParam);

    /**
     * 获取消费记录
     */
    Page<ConsumeRecordOutVO> getConsumeRecord(AmountChangeParamQuery queryParam);

}
