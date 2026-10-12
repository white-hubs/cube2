package com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dto.OutsourceWorkOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceWorkOrderVO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceWorkOrder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

/**
 * @author wql
 * @describe 委外维修
 * @date 2024/1/8 16:01
 */
public interface OutsourceWorkOrderService {
    Pagination<OutsourceWorkOrderVO> personalPage(ProcessInstancePageQuery processInstancePageQuery);

    OutsourceWorkOrder updateOutsourceWorkOrder(OutsourceWorkOrderDTO outsourceWorkOrderDTO, String processInstanceId);

    void sendDeviceLifeEvent(OutsourceWorkOrder outsourceWorkOrder, String processInstanceId);

    Long getRepairNumberByDeviceId(Long deviceInstanceId, Period period);
}
