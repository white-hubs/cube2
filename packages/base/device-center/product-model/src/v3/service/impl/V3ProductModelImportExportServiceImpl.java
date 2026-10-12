package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.iot.product.model.v3.constant.ModelConstants;
import com.guhecloud.cube.base.iot.product.model.v3.enums.DictEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.ExportTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.ImportModeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.dto.*;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.*;
import com.guhecloud.cube.base.iot.product.model.v3.model.*;
import com.guhecloud.cube.base.iot.product.model.v3.param.*;
import com.guhecloud.cube.base.iot.product.model.v3.event.ModelImportedEvent;
import com.guhecloud.cube.base.iot.product.model.v3.event.VersionRollbackEvent;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductEventService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductModelImportExportService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3SystemDictCacheService;
import com.guhecloud.cube.base.iot.product.model.v3.util.DictCodeAndName;
import com.guhecloud.cube.base.iot.product.model.v3.util.IdCodec;
import com.guhecloud.cube.base.iot.product.model.v3.util.ProductModelSnapshotConverter;
import com.guhecloud.cube.base.iot.product.model.v3.vo.*;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.Date;

import static com.guhecloud.cube.base.iot.product.model.v3.constant.ProductModel.CATEGORY_CODE_SPLIT;
import static com.guhecloud.cube.base.iot.product.model.v3.constant.ProductModel.CATEGORY_NAME_SPLIT;

/**
 * 物模型导入导出Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3ProductModelImportExportServiceImpl implements V3ProductModelImportExportService {

    private final V3MdProductMapper productMapper;
    private final V3MdProductPropMapper propMapper;
    private final V3MdProductEventMapper eventMapper;
    private final V3MdProductEventService productEventService;
    private final V3MdProductMethodMapper methodMapper;
    private final V3DictItemMapper sysDictItemMapper;
    private final V3SystemDictCacheService systemDictCacheService;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public String exportProductModel(ProductModelExportParam param) {
        log.info("开始导出物模型, prodId={}, exportType={}", param.getProdId(), param.getExportType());

        // 1. 查询产品信息
        MdProduct product = productMapper.selectById(param.getProdId());
        if (product == null) {
            throw new InvalidArgumentException("产品不存在: " + param.getProdId());
        }

        // 2. 构建导出数据(使用标准DTO)
        ProductModelSnapshotDTO.ProductModelSnapshotDTOBuilder builder = ProductModelSnapshotDTO.builder();

        builder.exportTime(new Date());
        builder.exportVersion(ModelConstants.ImportExport.EXPORT_VERSION);

        String exportType = param.getExportType();
        boolean includeSystemFields = Boolean.TRUE.equals(param.getIncludeSystemFields());

        // 根据导出类型决定包含哪些数据
        if (exportType == null || ExportTypeEnum.ALL.getCode().equals(exportType)) {
            // 查询品类信息
            DictCodeAndName categoryInfo = null;
            if (product.getProdCategoryId() != null) {
                categoryInfo = sysDictItemMapper.selectDictCodeAndNameById(product.getProdCategoryId());
            }

            // 查询品牌信息
            DictCodeAndName brandInfo = null;
            if (product.getProdBrandId() != null) {
                brandInfo = sysDictItemMapper.selectDictCodeAndNameById(product.getProdBrandId());
            }

            // 查询协议信息(协议是无租户的系统字典)
            DictCodeAndName protocolInfo = null;
            if (product.getProtocolId() != null) {
                protocolInfo = sysDictItemMapper.noTenantSelectDictCodeAndNameById(product.getProtocolId());
            }

            builder.product(ProductModelSnapshotConverter.toProductSnapshotDTO(
                product, categoryInfo, brandInfo, protocolInfo, includeSystemFields));
        }

        if (exportType == null || ExportTypeEnum.ALL.getCode().equals(exportType) || ExportTypeEnum.PROPERTIES_ONLY.getCode().equals(exportType)) {
            List<ProductPropVO> props = propMapper.selectByProdId(param.getProdId());
            builder.properties(ProductModelSnapshotConverter.toPropertySnapshotDTOList(props, includeSystemFields));
        }

        if (exportType == null || ExportTypeEnum.ALL.getCode().equals(exportType) || ExportTypeEnum.EVENTS_ONLY.getCode().equals(exportType)) {
            List<ProductEventVO> events = productEventService.listByProdId(param.getProdId());
            builder.events(ProductModelSnapshotConverter.toEventSnapshotDTOList(events, includeSystemFields));
        }

        if (exportType == null || ExportTypeEnum.ALL.getCode().equals(exportType) || ExportTypeEnum.METHODS_ONLY.getCode().equals(exportType)) {
            List<ProductMethodVO> methods = methodMapper.selectByProdId(param.getProdId());
            builder.methods(ProductModelSnapshotConverter.toMethodSnapshotDTOList(methods, includeSystemFields));
        }

        ProductModelSnapshotDTO exportData = builder.build();

        // 3. 序列化为JSON
        try {
            if (Boolean.TRUE.equals(param.getPrettyFormat())) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(exportData);
            } else {
                return objectMapper.writeValueAsString(exportData);
            }
        } catch (Exception e) {
            log.error("序列化物模型失败", e);
            throw new InvalidArgumentException("导出物模型失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importProductModel(ProductModelImportParam param) {
        log.info("开始导入物模型, importMode={}", param.getImportMode());

        // 1. 先执行验证
        Map<String, Object> validateResult = validateImport(param);
        if (Boolean.FALSE.equals(validateResult.get("valid"))) {
            throw new InvalidArgumentException("物模型验证失败: " + validateResult.get("errors"));
        }

        // 2. 如果仅验证,直接返回
        if (Boolean.TRUE.equals(param.getValidateOnly())) {
            return validateResult;
        }

        // 3. 解析导入内容(使用标准DTO)
        ProductModelSnapshotDTO modelData = param.getModelJson();

        // 4. 导入产品信息
        ProductSnapshotDTO productData = modelData.getProduct();
        Long prodId = importProductData(productData, param);

        // 5. 根据导入模式处理
        if (ImportModeEnum.REPLACE.getCode().equals(param.getImportMode())) {
            // 替换模式:删除所有现有数据
            deleteAllModelData(prodId);
        }

        // 6. 导入属性、事件、方法
        int propsCount = importPropsData(prodId, modelData.getProperties(), param);
        int eventsCount = importEventsData(prodId, modelData.getEvents(), param);
        int methodsCount = importMethodsData(prodId, modelData.getMethods(), param);

        // 7. 发布模型导入事件(触发版本快照创建)
        if (Boolean.TRUE.equals(param.getCreateSnapshot())) {
            eventPublisher.publishEvent(ModelImportedEvent.builder()
                    .prodId(prodId)
                    .timestamp(new Date())
                    .changeDesc("导入物模型")
                    .build());
        }

        // 8. 返回导入结果
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("prodId", prodId);
        result.put("propertiesImported", propsCount);
        result.put("eventsImported", eventsCount);
        result.put("methodsImported", methodsCount);

        log.info("物模型导入完成, prodId={}, props={}, events={}, methods={}",
                prodId, propsCount, eventsCount, methodsCount);

        return result;
    }

    @Override
    public Map<String, Object> validateImport(ProductModelImportParam param) {
        log.info("开始验证物模型");

        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        try {
            // 1. 解析JSON (使用标准DTO)
            ProductModelSnapshotDTO modelData = param.getModelJson();
            // 2. 验证产品信息
            ProductSnapshotDTO productData = modelData.getProduct();
            if (productData == null) {
                errors.add("缺少product节点");
            } else {
                validateProductData(productData, errors);
            }

            // 3. 验证属性
            List<PropertySnapshotDTO> properties = modelData.getProperties();
            if (properties != null) {
                for (int i = 0; i < properties.size(); i++) {
                    validatePropData(properties.get(i), i, errors);
                }
            }

            // 4. 验证事件
            List<EventSnapshotDTO> events = modelData.getEvents();
            if (events != null) {
                for (int i = 0; i < events.size(); i++) {
                    validateEventData(events.get(i), i, errors);
                }
            }

            // 5. 验证方法
            List<MethodSnapshotDTO> methods = modelData.getMethods();
            if (methods != null) {
                for (int i = 0; i < methods.size(); i++) {
                    validateMethodData(methods.get(i), i, errors);
                }
            }

            result.put("valid", errors.isEmpty());
            result.put("errors", errors);

        } catch (Exception e) {
            log.error("验证物模型失败", e);
            errors.add("JSON格式错误: " + e.getMessage());
            result.put("valid", false);
            result.put("errors", errors);
        }

        return result;
    }

    /**
     * 导入产品数据
     */
    private Long importProductData(ProductSnapshotDTO productData, ProductModelImportParam param) {
        if (productData == null) {
            throw new RuntimeException("产品数据不能为空");
        }

        String prodNumber = productData.getProdNumber();

        // 通过prodNumber查找产品
        MdProduct existingProduct = productMapper.selectOne(
            new LambdaQueryWrapper<MdProduct>()
                .eq(MdProduct::getProdNumber, prodNumber)
        );

        MdProduct product = ProductModelSnapshotConverter.toProductEntity(productData);

        // 处理品类：如果有品类名称链，则确保品类存在（级联创建）
        if (StringUtils.hasText(productData.getProdCategoryNameChain())) {
            Long categoryId = ensureCategoryExists(productData.getProdCategoryNameChain());
            product.setProdCategoryId(categoryId);
        }

        // 处理品牌：如果有品牌名称，则确保品牌存在
        if (StringUtils.hasText(productData.getProdBrandName())) {
            Long brandId = ensureBrandExists(productData.getProdBrandName());
            product.setProdBrandId(brandId);
        }

        // 处理协议：如果有协议编码，则确保协议存在
        if (StringUtils.hasText(productData.getProtocolCode())) {
            Long protocolId = ensureProtocolExists(productData.getProtocolCode());
            product.setProtocolId(protocolId);
        }

        if (existingProduct != null) {
            // 更新现有产品
            product.setId(existingProduct.getId());
            productMapper.updateById(product);
            return existingProduct.getId();
        } else {
            // 创建新产品
            productMapper.insert(product);
            return product.getId();
        }
    }

    /**
     * 导入属性数据
     */
    private int importPropsData(Long prodId, List<PropertySnapshotDTO> properties, ProductModelImportParam param) {
        if (properties == null || properties.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (PropertySnapshotDTO propData : properties) {
            String propCode = propData.getPropCode();

            // 检查是否已存在
            MdProductProp existing = propMapper.selectByProdIdAndCode(prodId, propCode);

            MdProductProp prop = ProductModelSnapshotConverter.toPropertyEntity(propData, prodId);

            if (existing != null) {
                if (Boolean.TRUE.equals(param.getSkipDuplicate())) {
                    continue;
                }
                // 更新
                prop.setId(existing.getId());
                propMapper.updateById(prop);
            } else {
                // 新增
                propMapper.insert(prop);
            }
            count++;
        }

        return count;
    }

    /**
     * 导入事件数据
     */
    private int importEventsData(Long prodId, List<EventSnapshotDTO> events, ProductModelImportParam param) {
        if (events == null || events.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (EventSnapshotDTO eventData : events) {
            String eventCode = eventData.getEventCode();

            MdProductEvent existing = eventMapper.selectByProdIdAndCode(prodId, eventCode);

            MdProductEvent event = ProductModelSnapshotConverter.toEventEntity(eventData, prodId);

            if (existing != null) {
                if (Boolean.TRUE.equals(param.getSkipDuplicate())) {
                    continue;
                }
                event.setId(existing.getId());
                eventMapper.updateById(event);
            } else {
                eventMapper.insert(event);
            }
            count++;
        }

        return count;
    }

    /**
     * 导入方法数据
     */
    private int importMethodsData(Long prodId, List<MethodSnapshotDTO> methods, ProductModelImportParam param) {
        if (methods == null || methods.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (MethodSnapshotDTO methodData : methods) {
            String methodCode = methodData.getMethodCode();

            MdProductMethod existing = methodMapper.selectByProdIdAndCode(prodId, methodCode);

            MdProductMethod method = ProductModelSnapshotConverter.toMethodEntity(methodData, prodId);

            if (existing != null) {
                if (Boolean.TRUE.equals(param.getSkipDuplicate())) {
                    continue;
                }
                method.setId(existing.getId());
                methodMapper.updateById(method);
            } else {
                methodMapper.insert(method);
            }
            count++;
        }

        return count;
    }

    /**
     * 删除所有物模型数据(替换模式)
     */
    private void deleteAllModelData(Long prodId) {
        propMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MdProductProp>()
                .eq(MdProductProp::getProdId, prodId));
        eventMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MdProductEvent>()
                .eq(MdProductEvent::getProdId, prodId));
        methodMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MdProductMethod>()
                .eq(MdProductMethod::getProdId, prodId));
    }

    /**
     * 验证产品数据
     */
    private void validateProductData(ProductSnapshotDTO data, List<String> errors) {
        if (!StringUtils.hasText(data.getProdNumber())) {
            errors.add("产品编号(prod_number)不能为空");
        }
        if (!StringUtils.hasText(data.getProdName())) {
            errors.add("产品名称(prod_name)不能为空");
        }
    }

    /**
     * 验证属性数据
     */
    private void validatePropData(PropertySnapshotDTO data, int index, List<String> errors) {
        if (!StringUtils.hasText(data.getPropCode())) {
            errors.add("属性[" + index + "]的编码(prop_code)不能为空");
        }
        if (!StringUtils.hasText(data.getName())) {
            errors.add("属性[" + index + "]的名称(name)不能为空");
        }
        if (!StringUtils.hasText(data.getDataType())) {
            errors.add("属性[" + index + "]的数据类型(data_type)不能为空");
        }
    }

    /**
     * 验证事件数据
     */
    private void validateEventData(EventSnapshotDTO data, int index, List<String> errors) {
        if (!StringUtils.hasText(data.getEventCode())) {
            errors.add("事件[" + index + "]的编码(event_code)不能为空");
        }
        if (!StringUtils.hasText(data.getName())) {
            errors.add("事件[" + index + "]的名称(name)不能为空");
        }
    }

    /**
     * 验证方法数据
     */
    private void validateMethodData(MethodSnapshotDTO data, int index, List<String> errors) {
        if (!StringUtils.hasText(data.getMethodCode())) {
            errors.add("方法[" + index + "]的编码(method_code)不能为空");
        }
        if (!StringUtils.hasText(data.getName())) {
            errors.add("方法[" + index + "]的名称(name)不能为空");
        }
    }

    /**
     * 监听版本回滚事件,执行数据恢复
     *
     * <p>使用@TransactionalEventListener(phase = BEFORE_COMMIT)确保在同一事务中执行</p>
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onVersionRollback(VersionRollbackEvent event) {
        log.info("监听到版本回滚事件, 开始恢复数据: prodId={}, targetVersionId={}",
                event.getProdId(), event.getTargetVersionId());

        // 使用importProductModel方法恢复数据
        try {
            ProductModelImportParam importParam = new ProductModelImportParam();
            importParam.setModelJson(objectMapper.readValue(event.getSnapshotData(), ProductModelSnapshotDTO.class));
            importParam.setImportMode(ImportModeEnum.REPLACE.getCode()); // 使用替换模式
            importParam.setValidateOnly(false);
            importParam.setSkipDuplicate(false);
            importParam.setCreateSnapshot(false); // 不在导入时创建快照,回滚后统一创建

            importProductModel(importParam);
            log.info("版本回滚数据恢复成功, prodId={}", event.getProdId());
        } catch (Exception e) {
            log.error("版本回滚数据恢复失败, prodId={}, targetVersionId={}",
                    event.getProdId(), event.getTargetVersionId(), e);
            throw new InvalidArgumentException("版本回滚数据恢复失败: " + e.getMessage(), e);
        }
    }

    /**
     * 确保品类存在（支持级联创建）
     *
     * @param categoryNameChain 品类名称链，格式如：一级品类-二级品类-三级品类
     * @return 最终品类的ID
     */
    private Long ensureCategoryExists(String categoryNameChain) {
        if (!StringUtils.hasText(categoryNameChain)) {
            return null;
        }

        // 按斜杠分割品类名称
        String[] categoryNames = categoryNameChain.split(CATEGORY_NAME_SPLIT);
        Long parentId = null;
        String levelCode = "";

        // 级联创建或查找品类
        for (String categoryName : categoryNames) {
            categoryName = categoryName.trim();
            if (!StringUtils.hasText(categoryName)) {
                continue;
            }

            // 生成品类编码（使用名称拼音或简化处理）
            String itemCode = IdCodec.generateShortCode();

            // 更新 levelCode
            if (StringUtils.hasText(levelCode)) {
                levelCode = levelCode + CATEGORY_CODE_SPLIT + itemCode;
            } else {
                levelCode = itemCode;
            }

            // 查找是否已存在
            SysDictItem existingItem = sysDictItemMapper.selectOne(
                new LambdaQueryWrapper<SysDictItem>()
                    .eq(SysDictItem::getDictId, DictEnum.PRODUCT_CATEGORY.getId())
                    .eq(SysDictItem::getItemCode, itemCode)
                    .eq(parentId != null, SysDictItem::getItemParentId, parentId)
                    .isNull(parentId == null, SysDictItem::getItemParentId)
            );

            if (existingItem != null) {
                parentId = existingItem.getId();
            } else {
                // 创建新品类
                SysDictItem newItem = new SysDictItem();
                newItem.setDictId(DictEnum.PRODUCT_CATEGORY.getId());
                newItem.setItemCode(itemCode);
                newItem.setItemLabel(categoryName);
                newItem.setItemValue(categoryName);
                newItem.setItemParentId(parentId);
                newItem.setItemLevelCode(levelCode);
                newItem.setItemLevelLabel(buildLevelLabel(parentId, categoryName));
                newItem.setStatus(0); // 正常状态
                newItem.setItemSort(0);

                sysDictItemMapper.insert(newItem);
                parentId = newItem.getId();

                log.info("创建品类字典项: code={}, label={}, parentId={}", itemCode, categoryName, parentId);
            }
        }

        return parentId;
    }

    /**
     * 确保品牌存在
     *
     * @param brandName 品牌名称
     * @return 品牌ID
     */
    private Long ensureBrandExists(String brandName) {
        if (!StringUtils.hasText(brandName)) {
            return null;
        }

        // 生成品牌编码（使用名称作为编码）
        String itemCode = IdCodec.generateShortCode();

        // 查找是否已存在
        SysDictItem existingItem = sysDictItemMapper.selectOne(
            new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getDictId, DictEnum.PRODUCT_BRAND.getId())
                .eq(SysDictItem::getItemCode, itemCode)
        );

        if (existingItem != null) {
            return existingItem.getId();
        }

        // 创建新品牌
        SysDictItem newItem = new SysDictItem();
        newItem.setDictId(DictEnum.PRODUCT_BRAND.getId());
        newItem.setItemCode(itemCode);
        newItem.setItemLabel(brandName);
        newItem.setItemValue(brandName);
        newItem.setItemLevelCode(itemCode);
        newItem.setItemLevelLabel(brandName);
        newItem.setStatus(0); // 正常状态
        newItem.setItemSort(0);

        sysDictItemMapper.insert(newItem);

        log.info("创建品牌字典项: code={}, label={}", itemCode, brandName);

        return newItem.getId();
    }

    /**
     * 确保协议存在(协议是无租户的系统字典)
     *
     * @param protocolCode 协议编码
     * @return 协议ID
     */
    private Long ensureProtocolExists(String protocolCode) {
        if (!StringUtils.hasText(protocolCode)) {
            return null;
        }

        SysDictItem existingItem = systemDictCacheService.getDictItemByCode(
            DictEnum.PROTOCOL.getId(), protocolCode);

        if (existingItem != null) {
            return existingItem.getId();
        }

        throw new InvalidArgumentException("协议不存在: " + protocolCode);
    }

    /**
     * 构建层级标签
     *
     * @param parentId 父级ID
     * @param currentName 当前名称
     * @return 层级标签，格式如：父级名称/当前名称
     */
    private String buildLevelLabel(Long parentId, String currentName) {
        if (parentId == null) {
            return currentName;
        }

        // 查询父级信息
        SysDictItem parentItem = sysDictItemMapper.selectById(parentId);
        if (parentItem != null && StringUtils.hasText(parentItem.getItemLevelLabel())) {
            return parentItem.getItemLevelLabel() + CATEGORY_NAME_SPLIT+ currentName;
        }

        return currentName;
    }
}
