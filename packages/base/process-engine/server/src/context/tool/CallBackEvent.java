package com.guhecloud.cube.base.flow.context.tool;

import com.guhecloud.cube.base.flow.context.config.RouteDefinition;
import com.guhecloud.cube.base.flow.context.submit.interfaces.PreSubmitData;
import com.guhecloud.cube.base.flow.context.submit.interfaces.StartAfterProcessInstanceCallBackData;
import com.guhecloud.cube.base.flow.flowable.process.application.commandgateway.command.StartProcessInstanceCommand;
import com.guhecloud.cube.base.flow.form.enumeration.FormJsonEnum;
import com.guhecloud.cube.base.flow.form.resolver.FormResultDataResolver;
import com.guhecloud.cube.base.flow.form.util.HttpClientUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author wql
 * @describe 预提交回调
 * @date 2023/5/18 18:16
 */
@Slf4j
@Component
public class CallBackEvent {

    private static final List<RouteDefinition> preRouteDefinitions;
    private static final List<RouteDefinition> afterRouteDefinitions;

    @Autowired
    private FormResultDataResolver formResultDataResolver;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    static {
        preRouteDefinitions = new ArrayList<>();
        preRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:PROPERTY_ALLOT_UNIT", "/property/allot/unit/submit", "propertyManagement"));
        preRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:PROPERTY_ALLOT_PERSONAL", "/property/allot/personal/submit", "propertyManagement"));
        preRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:PROPERTY_WEED_OUT", "/property/weedOut/submit", "propertyManagement"));
        preRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:CONSUMABLE_ADD", "/consumable/warehousing/submit", "propertyManagement"));
        preRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:CONSUMABLE_REDUCE", "/consumable/outbound/submit", "propertyManagement"));
        preRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:PROPERTY_DEPOSITORY", "/property/depository/submit", "propertyManagement"));

        afterRouteDefinitions = new ArrayList<>();
        afterRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:PROPERTY_ALLOT_UNIT", "/property/submit/relevance", "propertyManagement"));
        afterRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:PROPERTY_ALLOT_PERSONAL", "/property/submit/relevance", "propertyManagement"));
        afterRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:PROPERTY_WEED_OUT", "/property/submit/relevance", "propertyManagement"));
        afterRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:CONSUMABLE_ADD", "/property/submit/relevance", "propertyManagement"));
        afterRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:CONSUMABLE_REDUCE", "/property/submit/relevance", "propertyManagement"));
        afterRouteDefinitions.add(new RouteDefinition("PROPERTY_MANAGEMENT:PROPERTY_DEPOSITORY", "/property/submit/relevance", "propertyManagement"));
    }

    /**
     * 提交前是否执行回调
     * expression 支持的格式如 property:type1
     * 只支持查找单个
     *
     * @param businessType    业务类型
     * @param applicationCode 应用code
     */
    private Optional<RouteDefinition> lookupRouteDefinitionBefore(String businessType, String applicationCode) {
        List<String> routes = getRoutes(businessType, applicationCode);
        return preRouteDefinitions.stream().filter(routeDefinition -> routes.contains(routeDefinition.getId())).findFirst();
    }

    /**
     * @Author wql
     * @Description 提交后是否执行回调
     * @Date 14:18 2023/5/25
     **/
    private Optional<RouteDefinition> lookupRouteDefinitionAfter(String businessType, String applicationCode) {
        List<String> routes = getRoutes(businessType, applicationCode);
        return afterRouteDefinitions.stream().filter(routeDefinition -> routes.contains(routeDefinition.getId())).findFirst();
    }

    private List<String> getRoutes(String businessType, String applicationCode) {
        String expression = applicationCode + ":" + businessType;
        List<String> routes = new ArrayList<>();
        if (expression.contains(":")) {
            String[] split = expression.split(":");
            if (split[1].contains(",")) {
                String[] specifics = split[1].split(",");
                for (int i = 0; i < specifics.length; i++) {
                    routes.add(split[0] + ":" + specifics[i]);
                }
            } else {
                routes.add(expression);
            }
        } else {
            routes.add(expression);
        }
        return routes;
    }

    /**
     * @Author wql
     * @Description 表单提交前回调
     * @Date 18:29 2023/5/18
     **/
    public Object beforeCallBack(StartProcessInstanceCommand processInstanceCommand) {
        String businessType = processInstanceCommand.getBusinessId().getBusinessType();
        String applicationCode = processInstanceCommand.getAppId().getApplicationCode();
        String parsedData = processInstanceCommand.getData();
        if (!"FLOW".equals(processInstanceCommand.getType())) {
            parsedData = formResultDataResolver.parseByFormId2(processInstanceCommand.getFormDefinitionId(), parsedData);
        }
        Optional<RouteDefinition> optional = lookupRouteDefinitionBefore(businessType, applicationCode);
        if (optional.isPresent()) {
            RouteDefinition routeDefinition = optional.get();
            ServiceInstance serviceInstance = loadBalancerClient.choose(routeDefinition.getServiceId());
            String url = String.format("http://%s:%s", serviceInstance.getHost(), serviceInstance.getPort()) + routeDefinition.getPath();
            log.info("businessType:{},applicationCode:{},提交前回调请求的url:{},回调信息:{}", businessType, applicationCode, url, parsedData);
            // TODO 需要支持统一返回对象

            // 历史兼容，后续叫业务去掉
            Map<String, Object> jsonObject = "PROPERTY_MANAGEMENT".equals(applicationCode) ?
                                             HttpClientUtils.restPostWithParameter(url, parsedData, getAuthMap())
                                                                                           : HttpClientUtils.restPost(url, new PreSubmitData(parsedData), getAuthMap());
            log.info("回调结果:{}", jsonObject);
            return jsonObject.get("data");
        }
        return null;
    }

    /**
     * @Author wql
     * @Description 表单提交后回调
     * @Date 18:30 2023/5/18
     **/
    public void afterCallBack(StartAfterProcessInstanceCallBackData startAfterProcessInstanceCallBackData) {
        String businessType = startAfterProcessInstanceCallBackData.getBusinessType();
        String applicationCode = startAfterProcessInstanceCallBackData.getApplicationCode();
        log.info("businessType:{},applicationCode:{}", businessType, applicationCode);
        Optional<RouteDefinition> optional = lookupRouteDefinitionAfter(businessType, applicationCode);
        if (optional.isPresent()) {
            RouteDefinition routeDefinition = optional.get();
            ServiceInstance serviceInstance = loadBalancerClient.choose(routeDefinition.getServiceId());
            String url = String.format("http://%s:%s", serviceInstance.getHost(), serviceInstance.getPort()) + routeDefinition.getPath();
            log.info("businessType:{},applicationCode:{},提交后回调请求的url:{},回调信息:{}", businessType, applicationCode, url, JsonUtils.serialize(startAfterProcessInstanceCallBackData));
            Map<String, Object> jsonObject = "PROPERTY_MANAGEMENT".equals(startAfterProcessInstanceCallBackData.getApplicationCode()) ?
                                             HttpClientUtils.restPostWithParameter(url, JsonUtils.serialize(startAfterProcessInstanceCallBackData), getAuthMap())
                                                                                                                                      : HttpClientUtils.restPost(url, startAfterProcessInstanceCallBackData, getAuthMap());
            log.info("回调结果:{}", jsonObject);
        }
    }

    /**
     * @Author wql
     * @Description 设置请求头
     * @Date 15:28 2023/5/19
     **/
    private static Map<String, String> getAuthMap() {
        Map<String, String> authMap = new HashMap<>();
        authMap.put(FormJsonEnum.HEADER_AUTHORIZATION.getValue(), SessionContextHolder.getRequester().getAuthorization());
        authMap.put(FormJsonEnum.HEADER_TENANT.getValue(), SessionContextHolder.getRequester().getTenant().toString());
        authMap.put(FormJsonEnum.HEADER_TERMINAL.getValue(), SessionContextHolder.getRequester().getTerminal());
        return authMap;
    }

}
