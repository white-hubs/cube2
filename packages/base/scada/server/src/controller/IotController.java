package com.guhecloud.kaihe.scada.server.controller;

import com.guhecloud.kaihe.scada.server.infrastructure.web.response.UnifiedResponseBody;
import com.guhecloud.kaihe.scada.server.third.service.IotService;
import com.guhecloud.kaihe.scada.server.third.service.IotV2Service;
import com.guhecloud.kaihe.scada.server.third.vo.IotDevicePropertyVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotDeviceVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductModelVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductPropertyVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProjectVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: gh-scada
 * @description: iot平台接口
 * @author: hyq
 * @create: 2020-12-11 11:04
 **/
@RequestMapping("/Iot")
@RestController
@UnifiedResponseBody
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IotController {

    @Autowired
    private IotService iotService;

    @Autowired
    private IotV2Service iotService2;

    /**
     * 获取项目列表
     *
     * @return
     */
    @GetMapping("/getProjectList")
    public List<IotProjectVo> getProjectList() {
        return iotService2.getProjectList();
    }

    /**
     * 获取iot产品列表
     *
     * @param projectId 项目ID
     *
     * @return
     */
    @GetMapping("/getProductList")
    public List<IotProductVo> getProductList(String projectId) {
        //使用后端配置的tenantID，前端不再使用传递的tenantID
        projectId = null;
        return iotService2.getProductList(projectId);
    }

    /**
     * 查询产品型号列表
     *
     * @param projectId 项目ID
     * @param productId 产品ID
     *
     * @return
     */
    @GetMapping("/getProductModelList")
    public List<IotProductModelVo> getProductModelList(String projectId, String productId) {
        //使用后端配置的tenantID，前端不再使用传递的tenantID
        projectId = null;
        return iotService2.getProductModelList(projectId, productId);
    }

    /**
     * 查询设备列表
     *
     * @param projectId    项目ID
     * @param productModel 产品型号
     * @param deviceName   设备名称 支持模糊查询
     *
     * @return
     */
    @GetMapping("/getDeviceList")
    public List<IotDeviceVo> getDeviceList(String projectId, String productModel, String deviceName) {
        //使用后端配置的tenantID，前端不再使用传递的tenantID
        projectId = null;
        return iotService2.getDeviceList(projectId, productModel, deviceName);
    }

    /**
     * 查询设备属性
     *
     * @param projectId   项目ID
     * @param deviceCode  设备编码（设备ID）
     * @param propertyIds 设备属性
     *
     * @return
     */
    @GetMapping("/getPropertiesByProductModel")
    public List<IotProductPropertyVo> getPropertiesByDeviceCode(String projectId, String deviceCode, String propertyIds) {
        //使用后端配置的tenantID，前端不再使用传递的tenantID
        projectId = null;
        return iotService2.getPropertiesByDeviceCode(projectId, deviceCode, propertyIds);
    }

    /**
     * 根据设备型号查询设备属性
     *
     * @param projectId
     * @param productModel
     *
     * @return
     */
    @GetMapping("/getPropertyByProductModel")
    public List<IotDevicePropertyVo> getPropertyByProductModel(String projectId, String productModel) {
        //使用后端配置的tenantID，前端不再使用传递的tenantID
        projectId = null;
        return iotService2.getPropertyByProductModel(projectId, productModel);
    }
}
