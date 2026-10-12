package com.guhecloud.cube.base.iot.product.model.v3.util;

import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductEvent;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductMethod;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductProp;
import com.guhecloud.cube.base.iot.product.model.v3.param.EventSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.MethodSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.PropSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductEventVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductMethodVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductPropVO;

import java.util.function.Consumer;

/**
 * 实体构建工具类
 * 使用 Builder 模式简化实体对象的创建
 */
public class EntityBuilder {

    /**
     * 从 EventSaveParam 构建 MdProductEvent
     */
    public static MdProductEvent buildEvent(EventSaveParam param, Consumer<MdProductEvent> customizer) {
        MdProductEvent event = new MdProductEvent();
        event.setProdId(param.getProdId());
        event.setName(param.getName());
        event.setEventCode(param.getEventCode());
        event.setCategoryId(param.getEventCategoryId());
        // 将EventConfig对象转换为JSON字符串保存到数据库
        event.setConfig(param.getConfig());
        event.setRemark(param.getRemark());
        event.setStatus(param.getStatus() != null ? param.getStatus() : 1);
        event.setSortOrder(param.getSortOrder() != null ? param.getSortOrder() : 0);

        // 应用自定义配置
        if (customizer != null) {
            customizer.accept(event);
        }

        return event;
    }

    /**
     * 从 EventSaveParam 构建 MdProductEvent(无自定义配置)
     */
    public static MdProductEvent buildEvent(EventSaveParam param) {
        return buildEvent(param, null);
    }

    /**
     * 从 EventSaveParam 更新 MdProductEvent
     */
    public static void updateEvent(MdProductEvent event, EventSaveParam param, Consumer<MdProductEvent> customizer) {
        event.setProdId(param.getProdId());
        event.setName(param.getName());
        event.setEventCode(param.getEventCode());
        event.setCategoryId(param.getEventCategoryId());
        // 将EventConfig对象转换为JSON字符串保存到数据库
        event.setConfig(param.getConfig());
        event.setRemark(param.getRemark());
        event.setStatus(param.getStatus());
        event.setSortOrder(param.getSortOrder());

        // 应用自定义配置
        if (customizer != null) {
            customizer.accept(event);
        }

    }

    /**
     * 从 EventSaveParam 更新 MdProductEvent(无自定义配置)
     */
    public static void updateEvent(MdProductEvent event, EventSaveParam param) {
        updateEvent(event, param, null);
    }

    /**
     * 从 MdProductEvent 构建 ProductEventVO
     */
    public static ProductEventVO buildEventVO(MdProductEvent event, Consumer<ProductEventVO> customizer) {
        ProductEventVO vo = new ProductEventVO();
        vo.setId(event.getId());
        vo.setProdId(event.getProdId());
        vo.setName(event.getName());
        vo.setEventCode(event.getEventCode());
        vo.setEventCategoryId(event.getCategoryId());
        // 将JSON字符串转换为EventConfig对象
        vo.setConfig(event.getConfig());
        vo.setRemark(event.getRemark());
        vo.setStatus(event.getStatus());
        vo.setSortOrder(event.getSortOrder());
        vo.setCreateTime(event.getCreateTime());

        // 应用自定义配置
        if (customizer != null) {
            customizer.accept(vo);
        }

        return vo;
    }

    /**
     * 从 MdProductEvent 构建 ProductEventVO(无自定义配置)
     */
    public static ProductEventVO buildEventVO(MdProductEvent event) {
        return buildEventVO(event, null);
    }

    /**
     * 从 MethodSaveParam 构建 MdProductMethod
     */
    public static MdProductMethod buildMethod(MethodSaveParam param) {
        MdProductMethod method = new MdProductMethod();
        method.setProdId(param.getProdId());
        method.setName(param.getName());
        method.setMethodCode(param.getMethodCode());
        method.setCallType(param.getCallType());
        method.setInputParams(param.getInputParams());
        method.setOutputParams(param.getOutputParams());
        method.setTimeoutSeconds(param.getTimeoutSeconds() != null ? param.getTimeoutSeconds() : 30);
        method.setRetryCount(param.getRetryCount() != null ? param.getRetryCount() : 0);
        method.setRetryInterval(param.getRetryInterval() != null ? param.getRetryInterval() : 5);
        method.setRemark(param.getRemark());
        method.setStatus(param.getStatus() != null ? param.getStatus() : 1);
        method.setSortOrder(param.getSortOrder() != null ? param.getSortOrder() : 0);
        return method;
    }

    /**
     * 从 MethodSaveParam 更新 MdProductMethod
     */
    public static void updateMethod(MdProductMethod method, MethodSaveParam param) {
        method.setProdId(param.getProdId());
        method.setName(param.getName());
        method.setMethodCode(param.getMethodCode());
        method.setCallType(param.getCallType());
        method.setInputParams(param.getInputParams());
        method.setOutputParams(param.getOutputParams());
        method.setTimeoutSeconds(param.getTimeoutSeconds());
        method.setRetryCount(param.getRetryCount());
        method.setRetryInterval(param.getRetryInterval());
        method.setRemark(param.getRemark());
        method.setStatus(param.getStatus());
        method.setSortOrder(param.getSortOrder());
    }

    /**
     * 从 MdProductMethod 构建 ProductMethodVO
     */
    public static ProductMethodVO buildMethodVO(MdProductMethod method) {
        ProductMethodVO vo = new ProductMethodVO();
        vo.setId(method.getId());
        vo.setProdId(method.getProdId());
        vo.setName(method.getName());
        vo.setMethodCode(method.getMethodCode());
        vo.setCallType(method.getCallType());
        vo.setInputParams(method.getInputParams());
        vo.setOutputParams(method.getOutputParams());
        vo.setTimeoutSeconds(method.getTimeoutSeconds());
        vo.setRetryCount(method.getRetryCount());
        vo.setRetryInterval(method.getRetryInterval());
        vo.setRemark(method.getRemark());
        vo.setStatus(method.getStatus());
        vo.setSortOrder(method.getSortOrder());
        vo.setCreateTime(method.getCreateTime());
        return vo;
    }

    /**
     * 从 PropSaveParam 构建 MdProductProp
     */
    public static MdProductProp buildProp(PropSaveParam param) {
        MdProductProp prop = new MdProductProp();
        prop.setProdId(param.getProdId());
        prop.setName(param.getName());
        prop.setPropCode(param.getPropCode());
        prop.setDataType(param.getDataType());
        prop.setPrecision(param.getPrecision());
        prop.setUnitSymbol(param.getUnitSymbol());
        prop.setDataWrite(param.getDataWrite());
        prop.setDataConfig(param.getDataConfig());
        prop.setRemark(param.getRemark());
        prop.setDefaultValue(param.getDefaultValue());
        prop.setSortOrder(param.getSortOrder() != null ? param.getSortOrder() : 0);
        return prop;
    }

    /**
     * 从 PropSaveParam 更新 MdProductProp
     */
    public static void updateProp(MdProductProp prop, PropSaveParam param) {
        prop.setProdId(param.getProdId());
        prop.setName(param.getName());
        prop.setPropCode(param.getPropCode());
        prop.setDataType(param.getDataType());
        prop.setPrecision(param.getPrecision());
        prop.setUnitSymbol(param.getUnitSymbol());
        prop.setDataWrite(param.getDataWrite());
        prop.setDataConfig(param.getDataConfig());
        prop.setRemark(param.getRemark());
        prop.setDefaultValue(param.getDefaultValue());
        prop.setSortOrder(param.getSortOrder());
    }

    /**
     * 从 MdProductProp 构建 ProductPropVO
     */
    public static ProductPropVO buildPropVO(MdProductProp prop) {
        ProductPropVO vo = new ProductPropVO();
        vo.setId(prop.getId());
        vo.setProdId(prop.getProdId());
        vo.setName(prop.getName());
        vo.setPropCode(prop.getPropCode());
        vo.setDataType(prop.getDataType());
        vo.setPrecision(prop.getPrecision());
        vo.setUnitSymbol(prop.getUnitSymbol());
        vo.setDataWrite(prop.getDataWrite());
        vo.setDataConfig(prop.getDataConfig());
        vo.setRemark(prop.getRemark());
        vo.setDefaultValue(prop.getDefaultValue());
        vo.setSortOrder(prop.getSortOrder());
        vo.setCreateTime(prop.getCreateTime());
        return vo;
    }
}
