package com.guhecloud.cube.srv.gen.property.management.server.property.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.param.property.AllotParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedWorkflowParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableAddRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableReduceRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.OperateType;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertySourceEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.DepositoryDetailedService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyAllotService;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyWeedOutService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处理流引擎数据
 *
 * @author : lijie
 * @date : 2023-3-3
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/property")
public class PropertyWorkflowController {
    private final DepositoryDetailedService depositoryDetailedService;
    private final PropertyAllotService allotService;
    private final PropertyWeedOutService weedOutService;
    private final ConsumableAddRecordService consumableAddRecordService;
    private final ConsumableReduceRecordService consumableReduceRecordService;

    @PostMapping("/depository/submit")
    public Long depositorySubmit(String prefix) {
        var jsonObject = JsonUtils.deserialize(prefix, new TypeReference<Map<String, Object>>() {});
        Assert.isParameterError(jsonObject == null, "入库信息有误");
        // 封装入库基本信息
        var detailedParam = JsonUtils.deserialize(prefix, DepositoryDetailedParam.class);
        Assert.isParameterError(detailedParam == null, "入库信息有误");
        // 封装资产信息
        Object dataObj = jsonObject.get("data");
        var array = JsonUtils.deserialize(dataObj, new TypeReference<List<PropertyDetailedWorkflowParam>>() {});
        if (CollectionUtils.isNotEmpty(array)) {
            List<PropertyDetailedWorkflowParam> detailedList = new ArrayList<>();
            for (var detailed : array) {
                if (ObjectUtils.isEmpty(detailed) || ObjectUtils.isEmpty(detailed.getNumber())) {
                    throw new ServiceException(PropertyStatus.PROPERTY_DATA.errorCode()
                                                       .toString(), PropertyStatus.PROPERTY_DATA.errorMessage());
                }
                detailed.setStartTime(detailedParam.getStartTime());
                detailed.setAreaId(this.getAreaIdsDepositorySubmit(detailed.getAreaIds()));
                if (ObjectUtils.isNotEmpty(detailed) && ObjectUtils.isNotEmpty(detailed.getNumber())) {
                    int k = detailed.getNumber();
                    for (int i = 0; i < k; i++) {
                        detailed.setNumber(null);
                        detailed.setAuthority(PropertySourceEnum.FAIL.name());
                        detailedList.add(detailed);
                    }
                }
            }
            detailedParam.setPropertyDetailed(detailedList);
        }
        return depositoryDetailedService.insert(detailedParam);
    }

    /**
     * 调拨
     */
    @PostMapping("/allot/unit/submit")
    public Long allotUnitSubmit(String prefix) {
        var jsonObject = JsonUtils.deserialize(prefix, new TypeReference<Map<String, Object>>() {});
        Assert.isParameterError(jsonObject == null, "调拨信息有误");
        // 封装入库基本信息
        var allot = JsonUtils.deserialize(prefix, AllotParam.class);
        Assert.isParameterError(allot == null, "调拨信息有误");
        // 封装资产信息
        var dataObj = jsonObject.get("propertyIds");
        var propertyIds = JsonUtils.deserialize(dataObj, new TypeReference<List<Long>>() {});
        allot.setPropertyIds(propertyIds);
        allot.setType(OperateType.ALLOT_UNIT.name());
        if (CollectionUtils.isNotEmpty(allot.getAreaIds())) {
            allot.setAreaId(this.getAreaIds(allot.getAreaIds()));
        }
        return allotService.insert(allot);
    }

    /**
     * 派发
     */
    @PostMapping("/allot/personal/submit")
    public Long allotPersonalSubmit(String prefix) {
        var jsonObject = JsonUtils.deserialize(prefix, new TypeReference<Map<String, Object>>() {});
        Assert.isParameterError(jsonObject == null, "派发信息有误");
        // 封装入库基本信息
        var allot = JsonUtils.deserialize(prefix, AllotParam.class);
        Assert.isParameterError(allot == null, "派发信息有误");
        // 封装资产信息
        var dataObj = jsonObject.get("propertyIds");
        var propertyIds = JsonUtils.deserialize(dataObj, new TypeReference<List<Long>>() {});
        allot.setPropertyIds(propertyIds);
        allot.setType(OperateType.ALLOT_PERSONAL.name());
        if (CollectionUtils.isNotEmpty(allot.getAreaIds())) {
            allot.setAreaId(this.getAreaIds(allot.getAreaIds()));
        }
        return allotService.insert(allot);
    }

    @PostMapping("/weedOut/submit")
    public Long weedOutSubmit(String prefix) {
        var jsonObject = JsonUtils.deserialize(prefix, new TypeReference<Map<String, Object>>() {});
        Assert.isParameterError(jsonObject == null, "报废信息有误");
        // 封装入库基本信息
        var detailedParam = JsonUtils.deserialize(prefix, PropertyWeedOutParam.class);
        Assert.isParameterError(detailedParam == null, "报废信息有误");
        // 封装资产信息
        var dataObj = jsonObject.get("propertyIds");
        var propertyIds = JsonUtils.deserialize(dataObj, new TypeReference<List<Long>>() {});
        detailedParam.setPropertyIds(propertyIds);
        return weedOutService.insert(detailedParam);
    }

    /**
     * 绑定业务、流引擎
     */
    @PostMapping("/submit/relevance")
    public void submitrelevance(String prefix) {
        log.info("业务绑定流程实例,流引擎入参：{}", prefix);
        var jsonObject = JsonUtils.deserialize(prefix, new TypeReference<Map<String, Object>>() {});
        Assert.isParameterError(jsonObject == null, "参数信息有误");
        var workflowRelevance = JsonUtils.deserialize(jsonObject, WorkflowRelevance.class);
        Assert.isParameterError(workflowRelevance == null, "参数信息有误");
        if (ObjectUtils.isNotEmpty(workflowRelevance) &&
            ObjectUtils.isNotEmpty(workflowRelevance.getProcessInstanceId())
            && ObjectUtils.isNotEmpty(workflowRelevance.getPropertyId())) {
            log.info("{}：业务绑定流程：{}", workflowRelevance.getPropertyId(), workflowRelevance.getProcessInstanceId());
            switch (workflowRelevance.getBusinessType()) {
                case "PROPERTY_DEPOSITORY":
                    depositoryDetailedService.update(workflowRelevance);
                    break;
                case "PROPERTY_ALLOT_UNIT":
                case "PROPERTY_ALLOT_PERSONAL":
                    allotService.update(workflowRelevance);
                    break;
                case "PROPERTY_WEED_OUT":
                    weedOutService.update(workflowRelevance);
                    break;
                case "CONSUMABLE_ADD":
                    consumableAddRecordService.update(workflowRelevance);
                    break;
                case "CONSUMABLE_REDUCE":
                    consumableReduceRecordService.update(workflowRelevance);
                    break;
                default:
                    log.info("业务类型不存在,{}", workflowRelevance.getBusinessType());
                    throw new ServiceException(PropertyStatus.PROPERTY_FLOW_CALLBACK_BUSINESS.errorCode()
                                                       .toString(), PropertyStatus.PROPERTY_FLOW_CALLBACK_BUSINESS.errorMessage(), workflowRelevance.getBusinessType());
            }
        } else {
            log.info("流程回调参数错误,{}", workflowRelevance.getBusinessType());
            throw new ServiceException(PropertyStatus.PROPERTY_FLOW_CALLBACK.errorCode()
                                               .toString(), PropertyStatus.PROPERTY_FLOW_CALLBACK.errorMessage(), prefix);
        }
    }

    private String getAreaIds(List<String> areaIds) {
        return String.join(",", areaIds);
    }

    private String getAreaIdsDepositorySubmit(Map<String, Object> areaMap) {
        if (MapUtils.isEmpty(areaMap) || !areaMap.containsKey("id")) {
            return null;
        }
        var ids = areaMap.get("id");
        var areaIds = JsonUtils.deserialize(ids, new TypeReference<List<String>>() {});
        if (areaIds != null) {
            return String.join(",", areaIds);
        }
        return null;
    }
}
