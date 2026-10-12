package com.guhecloud.cube.srv.gen.maintain.server.retrofit.interfaces;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.commandgateway.RetrofitOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.retrofit.interfaces.vo.RetrofitOrderVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 更新改造2.0
 *
 * @author lanchun
 * @date 2024/1/8 15:33
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RetrofitOrderController {

    private final RetrofitOrderService retrofitOrderService;

    /**
     * 更新改造单分页列表
     *
     * @param processInstancePageQuery
     *
     * @return
     */
    @GetMapping("/retrofit-order/page")
    public Pagination<RetrofitOrderVO> retrofitOrderPage(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return retrofitOrderService.retrofitOrderPage(processInstancePageQuery);
    }
}
