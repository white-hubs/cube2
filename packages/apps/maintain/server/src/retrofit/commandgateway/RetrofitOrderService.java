package com.guhecloud.cube.srv.gen.maintain.server.retrofit.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.dto.RetrofitOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.interfaces.vo.RetrofitOrderVO;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.po.RetrofitOrder;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessStartedMessage;

public interface RetrofitOrderService extends IService<RetrofitOrder> {

    /**
     * 新增更新改造单
     *
     * @param message
     *
     * @return
     */
    RetrofitOrder addRetrofitOrder(ProcessStartedMessage message);

    /**
     * 更新改造单审批结果
     *
     * @param retrofitOrderDTO  更新改造单信息
     * @param processInstanceId 流程实例id
     */
    RetrofitOrder updateRetrofitOrder(RetrofitOrderDTO retrofitOrderDTO, String processInstanceId);

    /**
     * 更新改造单分页列表
     *
     * @param processInstancePageQuery 查询条件
     *
     * @return 更新改造单列表
     */
    Pagination<RetrofitOrderVO> retrofitOrderPage(ProcessInstancePageQuery processInstancePageQuery);

    void sendDeviceLifeEvent(RetrofitOrder retrofitOrder, String desc);
}
