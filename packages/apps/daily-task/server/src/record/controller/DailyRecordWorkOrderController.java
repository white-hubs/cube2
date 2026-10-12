package com.guhecloud.cube.biz.daily.task.record.controller;

import com.guhecloud.cube.biz.daily.task.record.param.DailyLinkWorkOrderParam;
import com.guhecloud.cube.biz.daily.task.record.service.DailyRecordWorkOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 记录关联工单
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyRecordWorkOrderController {
    private final DailyRecordWorkOrderService dailyRecordWorkOrderService;

    /**
     * 添加关联工单
     *
     * @param param 关联工单信息
     *
     * @return 是否成功
     */
    @PostMapping("/daily/work-order/link")
    public Boolean linkWorkOrder(@RequestBody @Valid DailyLinkWorkOrderParam param) {
        return dailyRecordWorkOrderService.linkWorkOrder(param);
    }

}
