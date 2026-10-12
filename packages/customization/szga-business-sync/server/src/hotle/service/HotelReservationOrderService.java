package com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.entity.HotelReservationOrder;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.vo.HotelReservationFromVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.ProcessCompletedMessage;
import com.guhecloud.platform.base.model.Pagination;
import instance.command.SubmitInitFormCommand;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/27 17:28
 * @Description:
 */
public interface HotelReservationOrderService extends IService<HotelReservationOrder> {

    /**
     * 酒店预定
     *
     * @param submitInitFormCommand
     *
     * @return
     */
    String reserveHotelSubmit(SubmitInitFormCommand submitInitFormCommand);

    /**
     * 获取酒店审批单记录
     *
     * @param processInstancePageQuery
     *
     * @return
     */
    Pagination<HotelReservationFromVO> getApprovalRecord(ProcessInstancePageQuery processInstancePageQuery);

    /**
     * 创建酒店预定单
     *
     * @param message
     */
    void createHotelReservationOrder(ProcessCompletedMessage message);
}
