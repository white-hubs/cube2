package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.MethodInvokeRequestDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.MethodInvokeResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.MethodRawCmdContentDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductMethodDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

/**
 * 产品方法北向接口客户端
 */
@FeignClient(name = CLIENT_NAME)
public interface IotProductMethodClient {

    /**
     * 查询产品方法列表
     *
     * @param prodId 产品ID
     * @return 方法列表
     */
    @GetMapping("/north/product-method/list")
    List<ProductMethodDTO> listProductMethods(@RequestParam("prodId") Long prodId);

    /**
     * 调用设备方法
     *
     * @param request 调用请求
     * @return 调用响应
     */
    @PostMapping("/north/product-method/invoke")
    MethodInvokeResponseDTO invokeMethod(@RequestBody MethodInvokeRequestDTO request);

    /**
     * 批量调用设备方法
     *
     * @param requests 调用请求列表
     * @return 调用响应列表
     */
    @PostMapping("/north/product-method/invoke/batch")
    List<MethodInvokeResponseDTO> batchInvokeMethod(@RequestBody List<MethodInvokeRequestDTO> requests);

    /**
     * 查询方法调用状态
     *
     * @param instanceId 实例ID
     * @return 调用状态详情
     */
    @GetMapping("/north/product-method/invoke-status")
    MethodRawCmdContentDTO queryInvokeStatus(@RequestParam("instanceId") String instanceId);
}
