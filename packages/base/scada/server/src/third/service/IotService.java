package com.guhecloud.kaihe.scada.server.third.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.kaihe.scada.server.service.SysConfigService;
import com.guhecloud.kaihe.scada.server.third.vo.IotDevicePropertyVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotDeviceVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductModelVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductPropertyVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProjectVo;
import com.guhecloud.kaihe.scada.server.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: gh-scada
 * @description: iot平台第三方接口
 * @author: hyq
 * @create: 2020-12-10 17:42
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IotService {

    private final SysConfigService configService;

    /**
     * iot key
     */
    public static final String iot_app_key_path = "iot.app.key.path";

    /**
     * iot 密钥
     */
    public static final String iot_app_secret_path = "iot.app.secret.path";

    /**
     * iot基础参数信息
     */
    private static final String iot_base_url = "iot.base.url";

    /**
     * iot项目列表路径
     */
    public static final String iot_project_list_path = "iot.project.list.path";

    /**
     * iot产品列表路径
     */
    public static final String iot_product_list_path = "iot.product.list.path";

    /**
     * iot产品型号列表路径
     */
    public static final String iot_product_model_list_path = "iot.product.model.list.path";

    /**
     * iot设备列表路径
     */
    public static final String iot_device_list_path = "iot.device.list.path";

    /**
     * 查询资产设备多个属性
     */
    public static final String iot_device_property_path = "iot.device.property.path";

    /**
     * IOT平台设备型号的可控属性
     */
    private static final String iot_device_model_url = "iot.device.model.property.path";

    /**
     * 操作地址
     *
     * @param pathKey
     *
     * @return
     */
    private String getOperateUrl(String pathKey) {
        String baseUrl = configService.getConfigValueByConfigKey(iot_base_url);
        String path = configService.getConfigValueByConfigKey(pathKey);
        return baseUrl + path;
    }

    /**
     * 获取项目列表
     *
     * @return
     */
    public List<IotProjectVo> getProjectList() {
        List<IotProjectVo> list = new ArrayList<>();
        String url = getOperateUrl(iot_project_list_path);
        String appKey = configService.getConfigValueByConfigKey(iot_app_key_path);//获取iot平台appKey
        String appSecret = configService.getConfigValueByConfigKey(iot_app_secret_path);//获取iot平台appSecret
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", appKey);
        authMap.put("appSecret", appSecret);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        JSONObject result = HttpUtils.restGet(url, params, authMap);
        if (null != result) {
            if (result.getInteger("code") == 200) {
                list = JSON.parseArray(result.getString("data"), IotProjectVo.class);
                return list;
            }
        }
        return list;
    }

    /**
     * 获取产品列表
     *
     * @param projectId
     *
     * @return
     */
    public List<IotProductVo> getProductList(String projectId) {
        List<IotProductVo> list = new ArrayList<>();
        String url = getOperateUrl(iot_product_list_path);
        String appKey = configService.getConfigValueByConfigKey(iot_app_key_path);//获取iot平台appKey
        String appSecret = configService.getConfigValueByConfigKey(iot_app_secret_path);//获取iot平台appSecret
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", appKey);
        authMap.put("appSecret", appSecret);
        authMap.put("projectId", projectId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("projectId", projectId);
        JSONObject result = HttpUtils.restGet(url, params, authMap);
        if (null != result) {
            if (result.getInteger("code") == 200) {
                list = JSON.parseArray(result.getString("data"), IotProductVo.class);
                return list;
            }
        }
        return list;
    }

    /**
     * 查询设备列表
     *
     * @param productId 产品ID
     * @param projectId 项目ID
     *
     * @return
     */
    public List<IotProductModelVo> getProductModelList(String projectId, String productId) {
        List<IotProductModelVo> list = new ArrayList<>();
        String url = getOperateUrl(iot_product_model_list_path);
        String appKey = configService.getConfigValueByConfigKey(iot_app_key_path);//获取iot平台appKey
        String appSecret = configService.getConfigValueByConfigKey(iot_app_secret_path);//获取iot平台appSecret
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", appKey);
        authMap.put("appSecret", appSecret);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("productId", productId);
        params.add("projectId", projectId);
        JSONObject result = HttpUtils.restGet(url, params, authMap);
        if (null != result) {
            if (result.getInteger("code") == 200) {
                list = JSON.parseArray(result.getString("data"), IotProductModelVo.class);
                return list;
            }
        }
        return list;
    }

    /**
     * 查询设备列表
     *
     * @param projectId    项目ID
     * @param productModel 产品型号
     * @param deviceName   设备名称  支持模糊查询
     *
     * @return
     */
    public List<IotDeviceVo> getDeviceList(String projectId, String productModel, String deviceName) {
        List<IotDeviceVo> list = new ArrayList<>();
        String url = getOperateUrl(iot_device_list_path);
        String appKey = configService.getConfigValueByConfigKey(iot_app_key_path);//获取iot平台appKey
        String appSecret = configService.getConfigValueByConfigKey(iot_app_secret_path);//获取iot平台appSecret
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", appKey);
        authMap.put("appSecret", appSecret);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("projectId", projectId);
        params.add("productModel", productModel);
        params.add("deviceName", deviceName);
        JSONObject result = HttpUtils.restGet(url, params, authMap);
        if (null != result) {
            if (result.getInteger("code") == 200) {
                list = JSON.parseArray(result.getString("data"), IotDeviceVo.class);
                return list;
            }
        }
        return list;
    }

    /**
     * 根据设备型号查询设备属性以及属性当前值
     *
     * @param deviceCode  设备编码（设备ID）
     * @param propertyIds
     *
     * @return
     */
    public List<IotProductPropertyVo> getPropertiesByDeviceCode(String projectId, String deviceCode, String propertyIds) {
        List<IotProductPropertyVo> list = new ArrayList<>();
        String url = getOperateUrl(iot_device_property_path);
        String appKey = configService.getConfigValueByConfigKey(iot_app_key_path);//获取iot平台appKey
        String appSecret = configService.getConfigValueByConfigKey(iot_app_secret_path);//获取iot平台appSecret
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", appKey);
        authMap.put("appSecret", appSecret);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("projectId", projectId);
        params.add("deviceCode", deviceCode);
        params.add("propertyIds", propertyIds);
        JSONObject result = HttpUtils.restGet(url, params, authMap);
        if (null != result) {
            if (result.getInteger("code") == 200) {
                list = JSON.parseArray(result.getString("data"), IotProductPropertyVo.class);
                return list;
            }
        }
        return list;
    }

    /**
     * 根据设备型号查询设备属性
     *
     * @param projectId
     * @param productModel
     *
     * @return
     */
    public List<IotDevicePropertyVo> getPropertyByProductModel(String projectId, String productModel) {
        String url = getOperateUrl(iot_device_model_url);
        String appKey = configService.getConfigValueByConfigKey(iot_app_key_path);//获取iot平台appKey
        String appSecret = configService.getConfigValueByConfigKey(iot_app_secret_path);//获取iot平台appSecret
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", appKey);
        authMap.put("appSecret", appSecret);
        authMap.put("project", projectId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("deviceModel", productModel);
        JSONObject result = HttpUtils.restGet(url, params, authMap);
        List<IotDevicePropertyVo> deviceModelPropertyVos = new ArrayList<>();
        try {
            if (result.getInteger("code") == 200) {
                deviceModelPropertyVos = JSON.parseArray(result.getString("data"), IotDevicePropertyVo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceModelPropertyVos;
    }

}
