package com.guhecloud.cube.base.iot.product.model.v3.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.iot.product.model.v3.dto.*;
import com.guhecloud.cube.base.iot.product.model.v3.model.*;
import com.guhecloud.cube.base.iot.product.model.v3.vo.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 物模型快照转换工具类
 * 提供Entity/VO与DTO之间的双向转换
 *
 * <p>用于替代原有的Map操作,提供类型安全的数据转换</p>
 */
@Slf4j
public class ProductModelSnapshotConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ProductModelSnapshotConverter() {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    // ==================== ProductSnapshotDTO转换 ====================

    /**
     * 将MdProduct实体转换为ProductSnapshotDTO
     *
     * @param product             产品实体
     * @param categoryInfo        产品品类信息(code和名称链)
     * @param brandInfo           产品品牌信息(code和名称)
     * @param protocolInfo        协议信息(code和名称)
     * @param includeSystemFields 是否包含系统字段(创建时间、更新时间等)
     * @return ProductSnapshotDTO
     */
    public static ProductSnapshotDTO toProductSnapshotDTO(
            MdProduct product,
            DictCodeAndName categoryInfo,
            DictCodeAndName brandInfo,
            DictCodeAndName protocolInfo,
            boolean includeSystemFields) {
        if (product == null) {
            return null;
        }

            ProductSnapshotDTO.ProductSnapshotDTOBuilder builder = ProductSnapshotDTO.builder()
                .prodNumber(product.getProdNumber())
                .prodName(product.getProdName())
                .prodNamePinyin(product.getProdNamePinyin())
                .prodCategoryNameChain(categoryInfo != null ? categoryInfo.getLevelName() : null)
                .nodeType(product.getNodeType())
                .prodBrandName(brandInfo != null ? brandInfo.getName() : null)
                .prodModel(product.getProdModel())
                .protocolCode(protocolInfo != null ? protocolInfo.getCode() : null)
                .remark(product.getRemark())
                .extendConfig(product.getExtendConfig())
                .status(product.getStatus())
                .prodVersion(product.getProdVersion())
                .prodIcon(product.getProdIcon())
                .prodPic(product.getProdPic())
                .createTime(product.getCreateTime())
                .updateTime(product.getUpdateTime());

        if (includeSystemFields) {
            builder.createTime(product.getCreateTime())
                    .updateTime(product.getUpdateTime());
        }

        return builder.build();
    }

    /**
     * 将ProductSnapshotDTO转换为MdProduct实体
     * 注意: code字段需要在调用方根据业务需要转换为对应的ID后设置
     *
     * @param dto ProductSnapshotDTO
     * @return MdProduct实体
     */
    public static MdProduct toProductEntity(ProductSnapshotDTO dto) {
        if (dto == null) {
            return null;
        }

        MdProduct product = new MdProduct();
        product.setProdNumber(dto.getProdNumber());
        product.setProdName(dto.getProdName());
        product.setProdNamePinyin(dto.getProdNamePinyin());
        // 注意: prodCategoryId、prodBrandId、protocolId需要调用方根据code查询后设置
        product.setNodeType(dto.getNodeType());
        product.setProdModel(dto.getProdModel());
        product.setRemark(dto.getRemark());
        product.setExtendConfig(dto.getExtendConfig());
        product.setStatus(dto.getStatus());
        product.setProdVersion(dto.getProdVersion());
        product.setProdIcon(dto.getProdIcon());
        product.setProdPic(dto.getProdPic());

        return product;
    }

    // ==================== PropertySnapshotDTO转换 ====================

    /**
     * 将ProductPropVO转换为PropertySnapshotDTO
     *
     * @param propVO              属性VO
     * @param includeSystemFields 是否包含系统字段
     * @return PropertySnapshotDTO
     */
    public static PropertySnapshotDTO toPropertySnapshotDTO(ProductPropVO propVO, boolean includeSystemFields) {
        if (propVO == null) {
            return null;
        }

        PropertySnapshotDTO.PropertySnapshotDTOBuilder builder = PropertySnapshotDTO.builder()
                .propCode(propVO.getPropCode())
                .name(propVO.getName())
                .dataType(propVO.getDataType())
                .precision(propVO.getPrecision())
                .unitSymbol(propVO.getUnitSymbol())
                .dataWrite(propVO.getDataWrite())
                .dataConfig(propVO.getDataConfig())
                .remark(propVO.getRemark())
                .defaultValue(propVO.getDefaultValue());

        if (includeSystemFields) {
            builder.createTime(propVO.getCreateTime());
        }

        return builder.build();
    }

    /**
     * 批量转换属性列表
     */
    public static List<PropertySnapshotDTO> toPropertySnapshotDTOList(List<ProductPropVO> propVOList, boolean includeSystemFields) {
        if (propVOList == null) {
            return null;
        }
        return propVOList.stream()
                .map(vo -> toPropertySnapshotDTO(vo, includeSystemFields))
                .collect(Collectors.toList());
    }

    /**
     * 将PropertySnapshotDTO转换为MdProductProp实体
     *
     * @param dto    PropertySnapshotDTO
     * @param prodId 产品ID
     * @return MdProductProp实体
     */
    public static MdProductProp toPropertyEntity(PropertySnapshotDTO dto, Long prodId) {
        if (dto == null) {
            return null;
        }

        MdProductProp prop = new MdProductProp();
        prop.setProdId(prodId);
        prop.setPropCode(dto.getPropCode());
        prop.setName(dto.getName());
        prop.setDataType(dto.getDataType());
        prop.setPrecision(dto.getPrecision());
        prop.setUnitSymbol(dto.getUnitSymbol());
        prop.setDataWrite(dto.getDataWrite());
        prop.setDataConfig(dto.getDataConfig());
        prop.setRemark(dto.getRemark());
        prop.setDefaultValue(dto.getDefaultValue());

        return prop;
    }

    // ==================== EventSnapshotDTO转换 ====================

    /**
     * 将ProductEventVO转换为EventSnapshotDTO
     *
     * @param eventVO             事件VO
     * @param includeSystemFields 是否包含系统字段
     * @return EventSnapshotDTO
     */
    public static EventSnapshotDTO toEventSnapshotDTO(ProductEventVO eventVO, boolean includeSystemFields) throws JsonProcessingException {
        if (eventVO == null) {
            return null;
        }

        EventSnapshotDTO.EventSnapshotDTOBuilder builder = EventSnapshotDTO.builder()
                .eventCode(eventVO.getEventCode())
                .name(eventVO.getName())
                .categoryName(eventVO.getEventCategoryName())
                .config(eventVO.getConfig())
                .remark(eventVO.getRemark())
                .status(eventVO.getStatus());

        if (includeSystemFields) {
            builder.createTime(eventVO.getCreateTime());
        }

        return builder.build();
    }

    /**
     * 批量转换事件列表
     */
    public static List<EventSnapshotDTO> toEventSnapshotDTOList(List<ProductEventVO> eventVOList, boolean includeSystemFields) {
        if (eventVOList == null) {
            return null;
        }
        return eventVOList.stream()
                .map(vo -> {
                    try {
                        return toEventSnapshotDTO(vo, includeSystemFields);
                    } catch (JsonProcessingException e) {
                        log.error("EventSnapshotDTO to MdProductEvent error", e);
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    /**
     * 将EventSnapshotDTO转换为MdProductEvent实体
     *
     * @param dto    EventSnapshotDTO
     * @param prodId 产品ID
     * @return MdProductEvent实体
     */
    public static MdProductEvent toEventEntity(EventSnapshotDTO dto, Long prodId) {
        if (dto == null) {
            return null;
        }

        MdProductEvent event = new MdProductEvent();
        event.setProdId(prodId);
        event.setEventCode(dto.getEventCode());
        event.setName(dto.getName());
        event.setConfig(dto.getConfig());
        event.setRemark(dto.getRemark());
        event.setStatus(dto.getStatus());

        return event;
    }

    // ==================== MethodSnapshotDTO转换 ====================

    /**
     * 将ProductMethodVO转换为MethodSnapshotDTO
     *
     * @param methodVO            方法VO
     * @param includeSystemFields 是否包含系统字段
     * @return MethodSnapshotDTO
     */
    public static MethodSnapshotDTO toMethodSnapshotDTO(ProductMethodVO methodVO, boolean includeSystemFields) {
        if (methodVO == null) {
            return null;
        }

        MethodSnapshotDTO.MethodSnapshotDTOBuilder builder = MethodSnapshotDTO.builder()
                .methodCode(methodVO.getMethodCode())
                .name(methodVO.getName())
                .callType(methodVO.getCallType())
                .inputParams(methodVO.getInputParams())
                .outputParams(methodVO.getOutputParams())
                .timeoutSeconds(methodVO.getTimeoutSeconds())
                .retryCount(methodVO.getRetryCount())
                .retryInterval(methodVO.getRetryInterval())
                .remark(methodVO.getRemark())
                .status(methodVO.getStatus());

        if (includeSystemFields) {
            builder.createTime(methodVO.getCreateTime());
        }

        return builder.build();
    }

    /**
     * 批量转换方法列表
     */
    public static List<MethodSnapshotDTO> toMethodSnapshotDTOList(List<ProductMethodVO> methodVOList, boolean includeSystemFields) {
        if (methodVOList == null) {
            return null;
        }

        return methodVOList.stream()
                .map(vo -> toMethodSnapshotDTO(vo, includeSystemFields))
                .collect(Collectors.toList());
    }

    /**
     * 将MethodSnapshotDTO转换为MdProductMethod实体
     *
     * @param dto    MethodSnapshotDTO
     * @param prodId 产品ID
     * @return MdProductMethod实体
     */
    public static MdProductMethod toMethodEntity(MethodSnapshotDTO dto, Long prodId) {
        if (dto == null) {
            return null;
        }

        MdProductMethod method = new MdProductMethod();
        method.setProdId(prodId);
        method.setMethodCode(dto.getMethodCode());
        method.setName(dto.getName());
        method.setCallType(dto.getCallType());
        method.setInputParams(dto.getInputParams());
        method.setOutputParams(dto.getOutputParams());
        method.setTimeoutSeconds(dto.getTimeoutSeconds());
        method.setRetryCount(dto.getRetryCount());
        method.setRetryInterval(dto.getRetryInterval());
        method.setRemark(dto.getRemark());
        method.setStatus(dto.getStatus());

        return method;
    }

    // ==================== ProductModelSnapshotDTO完整转换 ====================

    /**
     * 构建完整的物模型快照DTO
     *
     * @param product             产品实体
     * @param categoryInfo        产品品类信息(code和名称链)
     * @param brandInfo           产品品牌信息(code和名称)
     * @param protocolInfo        协议信息(code和名称)
     * @param props               属性列表
     * @param events              事件列表
     * @param methods             方法列表
     * @param includeSystemFields 是否包含系统字段
     * @return ProductModelSnapshotDTO
     */
    public static ProductModelSnapshotDTO buildCompleteSnapshot(
            MdProduct product,
            DictCodeAndName categoryInfo,
            DictCodeAndName brandInfo,
            DictCodeAndName protocolInfo,
            List<ProductPropVO> props,
            List<ProductEventVO> events,
            List<ProductMethodVO> methods,
            boolean includeSystemFields) {

        return ProductModelSnapshotDTO.builder()
                .product(toProductSnapshotDTO(product, categoryInfo, brandInfo, protocolInfo, includeSystemFields))
                .properties(toPropertySnapshotDTOList(props, includeSystemFields))
                .events(toEventSnapshotDTOList(events, includeSystemFields))
                .methods(toMethodSnapshotDTOList(methods, includeSystemFields))
                .build();
    }
}
