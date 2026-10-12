package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.OutsourceWorkOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceWorkOrderVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author wql
 * @describe 委外维修
 * @date 2024/1/8 14:42
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OutsourceWorkOrderController {

    private final OutsourceWorkOrderService outsourceWorkOrderService;

    /**
     * 个人委外报修工单
     *
     * @Author wql
     * @Description 个人委外报修工单
     * @Date 11:36 2023/11/6
     **/
    @GetMapping("/outsource/apply/personal")
    public Pagination<OutsourceWorkOrderVO> personalPage(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return outsourceWorkOrderService.personalPage(processInstancePageQuery);
    }

}
