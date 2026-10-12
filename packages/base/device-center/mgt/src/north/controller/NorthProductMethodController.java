package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.north.server.impl.NorthProductMethodService;
import com.guhecloud.cube.iot.mgt.north.vo.MethodInvokeRequest;
import com.guhecloud.cube.iot.mgt.north.vo.MethodInvokeResponse;
import com.guhecloud.cube.iot.mgt.north.vo.MethodRawCmdContent;
import com.guhecloud.cube.iot.mgt.north.vo.ProductMethodVo;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.CommandExecStatusEnum;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 产品方法北向接口
 */
@Slf4j
@RestController
@RequestMapping("/north/product-method")
public class NorthProductMethodController {

    @Resource
    private NorthProductMethodService northProductMethodService;

    /**
     * 查询产品方法列表
     *
     * @param prodId 产品ID
     * @return 方法列表
     */
    @GetMapping("/list")
    public List<ProductMethodVo> listProductMethods(@RequestParam Long prodId) {
        return northProductMethodService.listProductMethods(prodId);
    }

    /**
     * 查询方法详情
     *
     * @param methodId 方法ID
     * @return 方法详情
     */
    @GetMapping("/{methodId}")
    public ProductMethodVo getMethodDetail(@PathVariable Long methodId) {
        return northProductMethodService.getMethodDetail(methodId);
    }

    /**
     * 调用设备方法
     *
     * @param request 调用请求
     * @return 调用响应
     */
    @PostMapping("/invoke")
    public MethodInvokeResponse invokeMethod(@RequestBody @Valid MethodInvokeRequest request) {
        return northProductMethodService.invokeMethod(request);
    }

    /**
     * 批量调用设备方法
     *
     * @param requests 调用请求列表
     * @return 调用响应列表
     */
    @PostMapping("/invoke/batch")
    public List<MethodInvokeResponse> batchInvokeMethod(@RequestBody List<@Valid MethodInvokeRequest> requests) {
        if (Objects.isNull(requests) || requests.isEmpty()) {
            return Collections.emptyList();
        }

        List<MethodInvokeResponse> result = new ArrayList<>(requests.size());
        for (MethodInvokeRequest request : requests) {
            try {
                result.add(northProductMethodService.invokeMethod(request));
            } catch (Exception e) {
                MethodInvokeResponse response = new MethodInvokeResponse();
                response.setCommandSeq(request.getCommandSeq());
                response.setStatusCode(CommandExecStatusEnum.REJECT.code);
                response.setMessage(e.getMessage());
                result.add(response);
                log.error("method invoke error", e);
            }
        }

        return result;
    }

    /**
     * 查询方法调用状态
     *
     * @param instanceId 实例ID
     * @return 调用状态详情
     */
    @GetMapping("/invoke-status")
    public MethodRawCmdContent queryInvokeStatus(@RequestParam("instanceId") String instanceId) {
        return northProductMethodService.queryInvokeStatus(instanceId);
    }
}
