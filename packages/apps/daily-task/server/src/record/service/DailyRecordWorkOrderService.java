package com.guhecloud.cube.biz.daily.task.record.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import com.guhecloud.cube.biz.daily.task.record.param.DailyLinkWorkOrderParam;

public interface DailyRecordWorkOrderService extends IService<DailyRecordWorkOrderPO> {

    Boolean linkWorkOrder(DailyLinkWorkOrderParam param);
}
