package com.guhecloud.cube.biz.daily.task.record.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordWorkOrderMapper;
import com.guhecloud.cube.biz.daily.task.record.param.DailyLinkWorkOrderParam;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordWorkOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyRecordWorkOrderServiceImpl extends ServiceImpl<DailyRecordWorkOrderMapper, DailyRecordWorkOrderPO> implements DailyRecordWorkOrderService {

    /**
     * 关联工单
     *
     * @param param 关联信息
     *
     * @return 是否成功
     */
    @Override
    public Boolean linkWorkOrder(DailyLinkWorkOrderParam param) {
        var po = param.toDailyRecordWorkOrderPO();
        return this.save(po);
    }
}
