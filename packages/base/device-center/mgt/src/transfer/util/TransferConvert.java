package com.guhecloud.cube.iot.mgt.transfer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.mgt.things.vo.ProductPropVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductVo;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.EventExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.HiddenMappingExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.MethodExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.ProductExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.PropertyExcelDto;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class TransferConvert {

    private TransferConvert() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将ProductVo转换为ProductExcelDto
     */
    public static ProductExcelDto convertProductVoToExcelDto(ProductVo productVo) {
        ProductExcelDto.ProductExcelDtoBuilder builder = ProductExcelDto.builder()
                .productCode(String.valueOf(productVo.getId()))
                .name(productVo.getProdName())
                .model(productVo.getProdModel())
                .brand(productVo.getProdBrandName())
                .categoryNameWithCascade(productVo.getProdCategoryNameWithCascade())
                .protocol(productVo.getProtocolName())
                .description(productVo.getRemark());

        if (productVo.getProductPropList() != null) {
            List<PropertyExcelDto> properties = productVo.getProductPropList()
                    .stream()
                    .map(prop -> PropertyExcelDto.builder()
                            .code(prop.getPropCode())
                            .name(prop.getName())
                            .dataType(prop.getDataType())
                            .precision(prop.getPrecision())
                            .unit(prop.getUnitName())
                            .dataWrite(prop.getDataWrite())
                            .dataConfig(prop.getDataConfig())
                            .remark(prop.getRemark())
                            .build())
                    .collect(Collectors.toList());
            builder.properties(properties);
        }

        if (productVo.getProductEventList() != null) {
            List<EventExcelDto> events = productVo.getProductEventList()
                    .stream()
                    .map(event -> {
                        try {
                            return EventExcelDto.builder()
                                    .code(event.getEventCode())
                                    .name(event.getName())
                                    .category(event.getCategoryName())
                                    .config(OBJECT_MAPPER.writeValueAsString(event.getEventConfig()))
                                    .remark(event.getRemark())
                                    .status(event.getStatus())
                                    .build();
                        } catch (JsonProcessingException e) {
                            log.error("事件Excel导入失败", e);
                            throw new RuntimeException("事件Excel导入失败: " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());
            builder.events(events);
        }

        if (productVo.getProductMethodList() != null) {
            List<MethodExcelDto> methods = productVo.getProductMethodList()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(method -> MethodExcelDto.builder()
                            .methodCode(method.getMethodCode())
                            .name(method.getName())
                            .methodType(method.getMethodType())
                            .description(method.getDescription())
                            .inputParams(convertMapToJson(method.getInputParams()))
                            .outputParams(convertMapToJson(method.getOutputParams()))
                            .timeoutMs(method.getTimeoutMs())
                            .retryable(method.getRetryable())
                            .maxRetries(method.getMaxRetries())
                            .status(method.getStatus())
                            .remark(method.getRemark())
                            .build())
                    .collect(Collectors.toList());
            builder.methods(methods);
        }

        // 准备隐藏映射数据
        List<HiddenMappingExcelDto> hiddenMappings = new ArrayList<>();
        String productCode = String.valueOf(productVo.getId());

        // 添加品牌映射
        if (productVo.getProdBrandItem() != null && StringUtils.isNotBlank(productVo.getProdBrandName())) {
            hiddenMappings.add(HiddenMappingExcelDto.createBrandMapping(
                    productCode,
                    productVo.getProdBrandName(),
                    productVo.getProdBrandItem().getItemValue()));
        }

        // 添加属性单位映射
        if (productVo.getProductPropList() != null) {
            for (ProductPropVo prop : productVo.getProductPropList()) {
                if (StringUtils.isNotBlank(prop.getUnitName()) && StringUtils.isNotBlank(prop.getUnitCode())) {
                    hiddenMappings.add(HiddenMappingExcelDto.createUnitMapping(
                            productCode,
                            prop.getPropCode(),
                            prop.getUnitName(),
                            prop.getUnitCode()));
                }
            }
        }

        builder.hiddenMappings(hiddenMappings);

        return builder.build();
    }

    /**
     * 将Map转换为JSON字符串
     * 处理null值，返回空JSON对象
     */
    private static String convertMapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            log.error("Map转JSON失败", e);
            return "{}";
        }
    }

}
