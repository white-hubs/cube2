package com.guhecloud.cube.onecard.server.consumption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.onecard.server.consumption.entity.AmountChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.param.AmountRecordQueryParam;
import com.guhecloud.cube.onecard.server.consumption.vo.AmountChangeRecordVo;
import com.guhecloud.cube.onecard.server.consumption.vo.AmountChangeVo;
import com.guhecloud.platform.base.model.Pagination;

public interface AmountChangeRecordService extends IService<AmountChangeRecord> {

    Pagination<AmountChangeRecordVo> getAmountChangeRecord(AmountRecordQueryParam amountRecordQueryParam);

    AmountChangeVo getAmountClassification(AmountRecordQueryParam param);
}
