package com.guhecloud.kaihe.scada.server.third.service;

import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceThinDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceQueryParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ProductPageParam;
import com.guhecloud.kaihe.scada.server.third.vo.IotDevicePropertyVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotDeviceVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductModelVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductPropertyVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProductVo;
import com.guhecloud.kaihe.scada.server.third.vo.IotProjectVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: gh-scada
 * @description: iotV2平台第三方接口
 * @author: fl
 * @create: 2023-10-19 17:42
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IotV2Service {

    private final IotProductClient iotProductClient;

    private final TenantClient tenantClient;

    private final IotDeviceClient iotDeviceClient;

    //未指定使用
    @Value("${tenantId}")
    private String tenantId;

    /**
     * 获取项目列表<br/>
     * 在2.0中没有项目概念，因此这里以租户进行隔离
     *
     * @return 项目列表(租户)
     */
    public List<IotProjectVo> getProjectList() {
        List<TenantDTO> list = tenantClient.listTenants(null);
        if (Objects.isNull(list) || list.isEmpty()) {
            return Collections.emptyList();
        }

        //convert to project info
        return list.stream()
                .map(tenantDTO -> {
                    IotProjectVo iotProjectVo = new IotProjectVo();
                    iotProjectVo.setCode(String.valueOf(tenantDTO.getCode()));
                    iotProjectVo.setName(tenantDTO.getName());
                    return iotProjectVo;
                }).collect(Collectors.toList());
    }

    /**
     * 获取产品列表
     *
     * @param tenantId 租户
     *
     * @return
     */
    public List<IotProductVo> getProductList(String tenantId) {
        ProductPageParam param = new ProductPageParam();
        param.setWithMethodSpec(false);
        param.setWithEventSpec(false);
        param.setWithPropertySpec(false);

        //bind tenant
        bindTenantIfNecessary(tenantId);

        //分页拉取page，直到拉取完毕
        int pageNumber = 1;
        int pageSize = 100;
        List<IotProductVo> list = new ArrayList<>();
        while (true) {
            param.setPageNumber(pageNumber);
            param.setPageSize(pageSize);
            Pagination<ProductDTO> pagination = iotProductClient.page(param);
            if (Objects.isNull(pagination) || Objects.isNull(pagination.getRecords()) || pagination.getRecords().isEmpty()) {
                break;
            }

            list.addAll(pagination.getRecords()
                                .stream()
                                .map(productDTO -> {
                                    IotProductVo iotProductVo = new IotProductVo();
                                    iotProductVo.setCode(productDTO.getProdId());
                                    iotProductVo.setName(productDTO.getProdModel() + "-" + productDTO.getProdName());
                                    iotProductVo.setValue(productDTO.getProdName());
                                    return iotProductVo;
                                }).toList());

            if (pagination.getRecords().size() < pageSize) {
                break;
            }

            pageNumber++;
        }

        return list;
    }

    /**
     * 获取产品型号列表
     *
     * @param tenantId  租户ID
     * @param productId 产品ID
     *
     * @return 设备列表
     */
    public List<IotProductModelVo> getProductModelList(String tenantId, String productId) {
        //bind tenant
        bindTenantIfNecessary(tenantId);

        ProductPageParam param = new ProductPageParam();
        param.setProductId(productId);
        param.setPageNumber(1);
        param.setPageSize(2);//IOT2.0产品与型号是一对一关系，因此这里只取前两个

        Pagination<ProductDTO> pagination = iotProductClient.page(param);
        if (Objects.isNull(pagination) || pagination.getTotal() == 0) {
            return Collections.emptyList();
        }

        ProductDTO productDTO = pagination.getRecords().get(0);
        IotProductModelVo iotProductModelVo = new IotProductModelVo();
        iotProductModelVo.setCode(productDTO.getProdId());//产品ID代表了型号编码
        iotProductModelVo.setName(productDTO.getProdModel());//型号名称

        return Collections.singletonList(iotProductModelVo);
    }

    /**
     * 查询设备列表
     *
     * @param tenantId   项目ID
     * @param productId  产品ID{@link IotV2Service#getProductModelList(String, String) IotProductModelVo.code}
     * @param deviceName 设备名称  支持模糊查询
     *
     * @return 设备列表
     */
    public List<IotDeviceVo> getDeviceList(String tenantId, String productId, String deviceName) {
        List<IotDeviceVo> list = new ArrayList<>();
        DeviceQueryParam deviceQueryParam = new DeviceQueryParam();
        deviceQueryParam.setProductId(productId);
        deviceQueryParam.setDeviceNameLike(deviceName);

        //bind tenant
        bindTenantIfNecessary(tenantId);

        //循环分页拉取所有设备
        int pageNumber = 1;
        int pageSize = 100;
        while (true) {
            deviceQueryParam.setPageNumber(pageNumber);
            deviceQueryParam.setPageSize(pageSize);
            Pagination<DeviceThinDTO> pagination = iotDeviceClient.page(deviceQueryParam);
            if (Objects.isNull(pagination) || Objects.isNull(pagination.getRecords()) || pagination.getRecords().isEmpty()) {
                break;
            }

            list.addAll(pagination.getRecords()
                                .stream()
                                .map(deviceThinDTO -> {
                                    IotDeviceVo iotDeviceVo = new IotDeviceVo();
                                    iotDeviceVo.setCode(deviceThinDTO.getDeviceId());
                                    iotDeviceVo.setName(deviceThinDTO.getDeviceName());
                                    iotDeviceVo.setStatus("1");//默认让它在线
                                    iotDeviceVo.setBusinessId(deviceThinDTO.getDeviceId());
                                    iotDeviceVo.setProductNumber(deviceThinDTO.getProdModel());
                                    return iotDeviceVo;
                                }).toList());

            if (pagination.getRecords().size() < pageSize) {
                break;
            }

            pageNumber++;
        }
        return list;
    }

    /**
     * 根据设备型号查询设备属性
     *
     * @param tenantId  租户ID
     * @param productId 产品ID{@link IotV2Service#getProductModelList(String, String) IotProductModelVo.code}
     *
     * @return 设备属性列表
     */
    public List<IotDevicePropertyVo> getPropertyByProductModel(String tenantId, String productId) {
        List<IotDevicePropertyVo> list = new ArrayList<>();

        //bind tenant
        bindTenantIfNecessary(tenantId);

        ProductPageParam param = new ProductPageParam();
        param.setWithPropertySpec(true);
        param.setProductId(productId);
        param.setPageNumber(1);
        param.setPageSize(1);
        Pagination<ProductDTO> pagination = iotProductClient.page(param);
        if (Objects.isNull(pagination) || Objects.isNull(pagination.getRecords()) || pagination.getRecords().isEmpty()) {
            return list;
        }

        ProductDTO productDTO = pagination.getRecords().get(0);
        if (Objects.isNull(productDTO) || Objects.isNull(productDTO.getProductPropVoList())
            || productDTO.getProductPropVoList().isEmpty()) {
            return list;
        }

        //属性转换
        list.addAll(productDTO.getProductPropVoList()
                            .stream()
                            .map(productPropVo -> {
                                IotDevicePropertyVo iotDevicePropertyVo = new IotDevicePropertyVo();
                                iotDevicePropertyVo.setDeviceModel(productDTO.getProdModel());
                                iotDevicePropertyVo.setPropertyCode(productPropVo.getPropCode());
                                iotDevicePropertyVo.setPropertyName(productPropVo.getPropName());
                                iotDevicePropertyVo.setPropertyId(productPropVo.getPropCode());
                                iotDevicePropertyVo.setPropertyDesc(productPropVo.getRemark());
                                iotDevicePropertyVo.setPropertyAction(String.valueOf(productPropVo.getDataWrite()));
                                iotDevicePropertyVo.setPropertyType(productPropVo.getDataType());
                                iotDevicePropertyVo.setPropertyConfig(String.valueOf(productPropVo.getDataConstraint()));

                                return iotDevicePropertyVo;
                            }).toList());
        return list;
    }

    /**
     * 根据设备型号查询设备属性以及属性当前值
     *
     * @param tenantId 租户ID
     * @param deviceId 设备编码（设备ID）
     * @param propCode 属性编码
     *
     * @return 设备属性列表
     */
    public List<IotProductPropertyVo> getPropertiesByDeviceCode(String tenantId, String deviceId, String propCode) {
        List<IotProductPropertyVo> list = new ArrayList<>();

        //bind tenant
        bindTenantIfNecessary(tenantId);

        DeviceDetailParam deviceDetailParam = new DeviceDetailParam();
        deviceDetailParam.setDeviceId(deviceId);
        deviceDetailParam.setWithPropertySpec(true);
        deviceDetailParam.setWithCommandSpec(false);
        deviceDetailParam.setWithEventSpec(false);

        DeviceDetailDTO detailDTO = iotDeviceClient.detail(deviceDetailParam);
        if (Objects.isNull(detailDTO) || Objects.isNull(detailDTO.getProductPropVoList())
            || detailDTO.getProductPropVoList().isEmpty()) {
            return list;
        }

        //属性转换
        List<ProductPropDTO> productPropVoList = detailDTO.getProductPropVoList();
        for (ProductPropDTO productPropDTO : productPropVoList) {

            //过滤掉不需要的属性
            if (StringUtils.isNotBlank(propCode) && (!propCode.equals(productPropDTO.getPropCode()))) {
                continue;
            }

            IotProductPropertyVo iotProductPropertyVo = new IotProductPropertyVo();
            iotProductPropertyVo.setDeviceModel(detailDTO.getDeviceNumber());
            iotProductPropertyVo.setDeviceCode(detailDTO.getDeviceId());
            iotProductPropertyVo.setPropertyId(productPropDTO.getPropCode());
            iotProductPropertyVo.setPropertyCode(productPropDTO.getPropCode());
            iotProductPropertyVo.setPropertyName(productPropDTO.getPropName());
            iotProductPropertyVo.setPropertyValue(findValue(detailDTO.getSnapshots(), productPropDTO.getPropCode()));
            iotProductPropertyVo.setPropertyType(productPropDTO.getDataType());
            iotProductPropertyVo.setPropertyUnit(productPropDTO.getUnitName());
            iotProductPropertyVo.setPropertyConfig(String.valueOf(productPropDTO.getDataConstraint()));
            iotProductPropertyVo.setDataWrite(String.valueOf(productPropDTO.getDataWrite()));
            list.add(iotProductPropertyVo);
        }

        return list;
    }

    /**
     * 根据设备型号查询设备事件
     *
     * @param tenantId 租户ID
     */
    private void bindTenantIfNecessary(String tenantId) {
        if (StringUtils.isNotBlank(tenantId)) {
            SessionContextHolder.requestTenant(Integer.valueOf(tenantId));
        } else {
            SessionContextHolder.requestTenant(Integer.valueOf(this.tenantId));
        }
    }

    /**
     * 从快照中拉取属性值
     *
     * @param snapshots 快照
     * @param key       属性编码
     *
     * @return 属性值
     */
    private String findValue(List<KeyValueWithTsDTO> snapshots, String key) {
        if (Objects.isNull(snapshots) || snapshots.isEmpty()) {
            return null;
        }

        for (KeyValueWithTsDTO snapshot : snapshots) {
            if (snapshot.getKey().equals(key) && Objects.nonNull(snapshot.getValue())) {
                return String.valueOf(snapshot.getValue());
            }
        }

        return null;
    }
}
