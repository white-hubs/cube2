package com.guhecloud.cube.iot.mgt.transfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.mgt.base.mapper.IotSysUnitMapper;
import com.guhecloud.cube.iot.mgt.base.mapper.SysDictItemMapper;
import com.guhecloud.cube.iot.mgt.base.model.IotSysUnitEntity;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.base.param.CommonDictItemModifyParam;
import com.guhecloud.cube.iot.mgt.base.service.impl.CommonDictItemService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.base.vo.IotSysUnitVo;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.core.data.lang.AccProtocolEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductEventMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductPropMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProduct;
import com.guhecloud.cube.iot.mgt.things.model.MdProductEvent;
import com.guhecloud.cube.iot.mgt.things.model.MdProductProp;
import com.guhecloud.cube.iot.mgt.things.server.MdProductMethodService;
import com.guhecloud.cube.iot.mgt.things.vo.EventConfig;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodBatchVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodMgtVo;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.EventExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.HiddenMappingExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.MethodExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.ProductExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.excel.PropertyExcelDto;
import com.guhecloud.cube.iot.mgt.transfer.dto.result.ImportResult;
import com.guhecloud.cube.iot.mgt.transfer.service.ProductImportService;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 产品导入服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImportServiceImpl implements ProductImportService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final CommonDictItemService commonDictItemService;
    private final IotSysUnitMapper iotSysUnitMapper;
    private final SysDictItemMapper sysDictItemMapper;
    private final MdProductMapper mdProductMapper;
    private final MdProductPropMapper mdProductPropMapper;
    private final MdProductEventMapper mdProductEventMapper;
    private final MdProductMethodService mdProductMethodService;
    private final PlatformEvnCom platformEvnCom;

    @Override
    @Transactional
    public ImportResult saveProductExcelData(ProductExcelDto excelDto) {
        try {
            ImportResult result = ImportResult.successAll("成功");

            // 1. 检查产品是否已存在（幂等性处理）,通过产品编码唯一标识
            MdProduct existingProduct = findExistingProduct(excelDto.getProductCode());

            Long productId;
            if (existingProduct != null) {
                log.info("产品已存在，更新产品信息: {}", excelDto.getProductCode());
                productId = existingProduct.getId();
                updateExistingProduct(existingProduct, excelDto);
            } else {
                log.info("创建新产品: {}", excelDto.getProductCode());
                productId = createNewProduct(excelDto);
            }

            // 2. 处理产品属性
            if (excelDto.getProperties() != null && !excelDto.getProperties().isEmpty()) {
                saveOrUpdateProductProperties(productId, excelDto.getProperties(), excelDto);
            }

            // 3. 处理产品事件
            if (excelDto.getEvents() != null && !excelDto.getEvents().isEmpty()) {
                saveOrUpdateProductEvents(productId, excelDto.getEvents());
            }

            // 4. 处理产品方法
            if (excelDto.getMethods() != null && !excelDto.getMethods().isEmpty()) {
                saveOrUpdateProductMethods(productId, excelDto.getMethods());
            }

            log.info("产品数据保存完成，产品编码: {}, 产品ID: {}", excelDto.getProductCode(), productId);

            return result;
        } catch (Exception e) {
            log.error("保存产品数据失败，产品编码: {}", excelDto.getProductCode(), e);
            return ImportResult.failureAll("保存数据失败: " + e.getMessage());
        }
    }

    /**
     * 查找已存在的产品（通过产品编码或产品名称）
     */
    private MdProduct findExistingProduct(String productCode) {
        LambdaQueryWrapper<MdProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MdProduct::getProdNumber, productCode);

        return mdProductMapper.selectOne(queryWrapper);
    }

    /**
     * 更新已存在的产品
     */
    private void updateExistingProduct(MdProduct existingProduct, ProductExcelDto excelDto) {
        boolean hasChange = false;

        // 更新产品名称
        if (StringUtils.isNotBlank(excelDto.getName()) && !excelDto.getName().equals(existingProduct.getProdName())) {
            existingProduct.setProdName(excelDto.getName());
            hasChange = true;
        }

        // 更新产品型号
        if (StringUtils.isNotBlank(excelDto.getModel()) && !excelDto.getModel().equals(existingProduct.getProdModel())) {
            existingProduct.setProdModel(excelDto.getModel());
            hasChange = true;
        }

        // 更新产品描述
        if (StringUtils.isNotBlank(excelDto.getDescription()) && !excelDto.getDescription().equals(existingProduct.getRemark())) {
            existingProduct.setRemark(excelDto.getDescription());
            hasChange = true;
        }

        // 处理品类（如果指定了品类名称）
        if (StringUtils.isNotBlank(excelDto.getCategoryNameWithCascade())) {
            Long categoryId = findOrCreateProductCategory(excelDto.getCategoryNameWithCascade());
            if (categoryId != null && !categoryId.equals(existingProduct.getProdCategoryId())) {
                existingProduct.setProdCategoryId(categoryId);
                hasChange = true;
            }
        }

        // 处理品牌（优先使用brandCode匹配）
        if (StringUtils.isNotBlank(excelDto.getBrand())) {
            Long brandId = findOrCreateProductBrand(excelDto);
            if (brandId != null && !brandId.equals(existingProduct.getProdBrandId())) {
                existingProduct.setProdBrandId(brandId);
                hasChange = true;
            }
        }

        // 处理协议
        if (StringUtils.isNotBlank(excelDto.getProtocol())) {
            existingProduct.setProtocolId(AccProtocolEnum.GH_IOT_PROTOCOL.id);
            existingProduct.setAccessTypeId(findOrCreateAccessType());
        }

        if (hasChange) {
            mdProductMapper.updateById(existingProduct);
            log.info("更新产品信息完成: {}", existingProduct.getProdNumber());
        }
    }

    /**
     * 创建新产品
     */
    private Long createNewProduct(ProductExcelDto excelDto) {
        MdProduct product = new MdProduct();

        // 生成产品ID
        product.setId(IdWorker.getId());

        // 设置基本信息
        product.setProdNumber(excelDto.getProductCode());
        product.setProdName(excelDto.getName());
        product.setProdModel(excelDto.getModel());
        product.setRemark(excelDto.getDescription());

        // 设置默认值
        product.setIsCustom(1); // 默认为自定义
        product.setNodeType(1L); // 默认为直连设备
        product.setStatus(1); // 默认启用

        // 处理品类
        if (StringUtils.isNotBlank(excelDto.getCategoryNameWithCascade())) {
            Long categoryId = findOrCreateProductCategory(excelDto.getCategoryNameWithCascade());
            product.setProdCategoryId(categoryId);
        }

        // 处理品牌（优先使用brandCode匹配）
        if (StringUtils.isNotBlank(excelDto.getBrand())) {
            Long brandId = findOrCreateProductBrand(excelDto);
            product.setProdBrandId(brandId);
        }

        // 处理协议
        if (StringUtils.isNotBlank(excelDto.getProtocol())) {
            product.setProtocolId(AccProtocolEnum.GH_IOT_PROTOCOL.id);
            product.setAccessTypeId(findOrCreateAccessType());
        }

        mdProductMapper.insert(product);
        log.info("创建新产品完成: {}, ID: {}", product.getProdNumber(), product.getId());

        return product.getId();
    }

    private Long findOrCreateAccessType() {
        List<SysDictItem> ids = sysDictItemMapper.selectList(new LambdaQueryWrapper<SysDictItem>()
                                                                     .eq(SysDictItem::getDictId, InnerDicEnum.direct_node_acc.id)
                                                                     .select(SysDictItem::getId));

        //连接方式不再重要，这里返回一个即可
        if (Objects.isNull(ids) || ids.isEmpty()) {
            //不重要了，创建一个

            SysDictItem item = new SysDictItem();
            item.setId(IdWorker.getId());
            item.setDictId(InnerDicEnum.direct_node_acc.id);
            item.setItemLabel("任意");
            item.setItemValue("任意");
            item.setItemLevel(platformEvnCom.hierarchicalNumbering.get1LevelId(1));
            item.setItemSort(1);
            item.setStatus(0); // 正常状态
            item.setRemark("导入时自动创建的接入方式");
            sysDictItemMapper.insert(item);

            return item.getId();
        }

        return ids.get(0).getId();
    }

    /**
     * 查找或创建产品品类
     */
    private Long findOrCreateProductCategory(String categoryNameWithCascade) {
        if (!StringUtils.isNotBlank(categoryNameWithCascade)) {
            return null;
        }

        try {
            // 首先尝试根据级联名称查找现有品类
            List<DictItemVo> categories = commonDictItemService.queryListByDictId(InnerDicEnum.prod_category.id);

            // 构建级联标签来匹配
            for (DictItemVo category : categories) {
                String currentPath = buildCategoryPath(category, categories);
                if (categoryNameWithCascade.equals(currentPath)) {
                    return category.getId();
                }
            }

            // 如果没有找到，创建新的品类
            String[] categoryLevels = categoryNameWithCascade.split("-");
            Long parentId = null;
            Long categoryId = null;

            String currentPath = "";
            for (int i = 0; i < categoryLevels.length; i++) {
                String levelName = categoryLevels[i].trim();
                if (i > 0) {
                    currentPath += "-";
                }
                currentPath += levelName;

                // 查找当前级别是否存在
                boolean found = false;
                for (DictItemVo category : categories) {
                    String existingPath = buildCategoryPath(category, categories);
                    if (currentPath.equals(existingPath)) {
                        categoryId = category.getId();
                        parentId = categoryId;
                        found = true;
                        break;
                    }
                }

                // 如果当前级别不存在，创建它
                if (!found) {
                    CommonDictItemModifyParam param = new CommonDictItemModifyParam();
                    param.setItemLabel(levelName);
                    param.setItemValue(levelName);
                    param.setParentId(parentId);
                    param.setSort(999);
                    param.setRemark("导入时自动创建的品类");

                    categoryId = commonDictItemService.saveDictItem(param, InnerDicEnum.prod_category.id);
                    parentId = categoryId;

                    log.info("创建产品品类: {}, ID: {}", levelName, categoryId);

                    // 重新获取更新后的品类列表
                    categories = commonDictItemService.queryListByDictId(InnerDicEnum.prod_category.id);
                }
            }

            return categoryId;

        } catch (Exception e) {
            log.error("处理产品品类失败: {}", categoryNameWithCascade, e);
            return null;
        }
    }

    /**
     * 构建品类的级联路径
     */
    private String buildCategoryPath(DictItemVo category, List<DictItemVo> allCategories) {
        List<String> pathParts = new ArrayList<>();
        DictItemVo current = category;

        // 向上遍历到根节点
        while (current != null) {
            pathParts.add(0, current.getItemLabel()); // 在列表开头插入

            // 查找父节点
            DictItemVo parent = null;
            if (current.getItemParentId() != null && current.getItemParentId() != 0) {
                for (DictItemVo cat : allCategories) {
                    if (cat.getId().equals(current.getItemParentId())) {
                        parent = cat;
                        break;
                    }
                }
            }
            current = parent;
        }

        return String.join("-", pathParts);
    }

    /**
     * 查找或创建产品品牌（优先使用隐藏映射中的brandCode匹配）
     */
    private Long findOrCreateProductBrand(ProductExcelDto excelDto) {
        String brandName = excelDto.getBrand();
        String brandCode = getBrandCodeFromHiddenMappings(excelDto);

        // 如果品牌名称和编码都为空，返回空
        if (!StringUtils.isNotBlank(brandName) && !StringUtils.isNotBlank(brandCode)) {
            return null;
        }

        try {
            // 获取现有品牌列表
            List<DictItemVo> brands = commonDictItemService.queryListByDictId(InnerDicEnum.prod_brand.id);

            // 优先使用brandCode进行精确匹配
            if (StringUtils.isNotBlank(brandCode)) {
                for (DictItemVo brand : brands) {
                    if (brandCode.equals(brand.getItemValue())) {
                        return brand.getId();
                    }
                }
            }

            // brandCode未找到，使用brandName匹配
            if (StringUtils.isNotBlank(brandName)) {
                for (DictItemVo brand : brands) {
                    if (brandName.equals(brand.getItemLabel())) {
                        return brand.getId();
                    }
                }
            }

            // 都未找到，创建新品牌
            String labelToUse = StringUtils.isNotBlank(brandName) ? brandName : brandCode;
            String valueToUse = StringUtils.isNotBlank(brandCode) ? brandCode : brandName;

            CommonDictItemModifyParam param = new CommonDictItemModifyParam();
            param.setItemLabel(labelToUse);
            param.setItemValue(valueToUse);
            param.setParentId(null);
            param.setSort(999);
            param.setRemark("导入时自动创建的品牌");

            Long brandId = commonDictItemService.saveDictItem(param, InnerDicEnum.prod_brand.id);
            log.info("创建产品品牌: {} (code: {}), ID: {}", labelToUse, valueToUse, brandId);

            return brandId;

        } catch (Exception e) {
            log.error("处理产品品牌失败: {} (code: {})", brandName, brandCode, e);
            return null;
        }
    }

    /**
     * 从隐藏映射中获取品牌编码
     */
    private String getBrandCodeFromHiddenMappings(ProductExcelDto excelDto) {
        if (excelDto.getHiddenMappings() == null || excelDto.getHiddenMappings().isEmpty()) {
            return null;
        }

        return excelDto.getHiddenMappings().stream()
                .filter(mapping -> HiddenMappingExcelDto.MappingType.BRAND.equals(mapping.getMappingType()))
                .filter(mapping -> excelDto.getBrand() != null && excelDto.getBrand().equals(mapping.getDisplayName()))
                .map(HiddenMappingExcelDto::getInternalCode)
                .findFirst()
                .orElse(null);
    }

    /**
     * 从隐藏映射中获取单位编码
     */
    private String getUnitCodeFromHiddenMappings(ProductExcelDto excelDto, String unitName) {
        if (excelDto.getHiddenMappings() == null || excelDto.getHiddenMappings().isEmpty()) {
            return unitName; // 如果没有隐藏映射，使用单位名称作为编码（向后兼容）
        }

        return excelDto.getHiddenMappings().stream()
                .filter(mapping -> HiddenMappingExcelDto.MappingType.UNIT.equals(mapping.getMappingType()))
                .filter(mapping -> unitName != null && unitName.equals(mapping.getDisplayName()))
                .map(HiddenMappingExcelDto::getInternalCode)
                .findFirst()
                .orElse(unitName); // 如果找不到映射，使用单位名称作为编码（向后兼容）
    }

    /**
     * 保存或更新产品属性
     */
    private void saveOrUpdateProductProperties(Long productId, List<PropertyExcelDto> properties, ProductExcelDto excelDto) {
        for (PropertyExcelDto propDto : properties) {
            // 检查属性是否已存在
            LambdaQueryWrapper<MdProductProp> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MdProductProp::getProdId, productId)
                    .eq(MdProductProp::getPropCode, propDto.getCode());

            MdProductProp existingProp = mdProductPropMapper.selectOne(queryWrapper);

            if (existingProp != null) {
                // 更新现有属性
                updateExistingProperty(existingProp, propDto, excelDto);
            } else {
                // 创建新属性
                createNewProperty(productId, propDto, excelDto);
            }
        }
    }

    /**
     * 更新现有属性
     */
    private void updateExistingProperty(MdProductProp existingProp, PropertyExcelDto propDto, ProductExcelDto excelDto) {
        boolean hasChange = false;

        if (StringUtils.isNotBlank(propDto.getName()) && !propDto.getName().equals(existingProp.getName())) {
            existingProp.setName(propDto.getName());
            hasChange = true;
        }

        if (StringUtils.isNotBlank(propDto.getDataType()) && !propDto.getDataType().equals(existingProp.getDataType())) {
            existingProp.setDataType(propDto.getDataType());
            hasChange = true;
        }

        if (StringUtils.isNotBlank(propDto.getUnit())) {
            String unitCode = getUnitCodeFromHiddenMappings(excelDto, propDto.getUnit());
            Long unitId = findOrCreateUnit(unitCode);
            if (unitId != null && !unitId.equals(existingProp.getUnitId())) {
                existingProp.setUnitId(unitId);
                existingProp.setUnitCode(unitCode);
                hasChange = true;
            }
        }

        if (StringUtils.isNotBlank(propDto.getRemark()) && !propDto.getRemark().equals(existingProp.getRemark())) {
            existingProp.setRemark(propDto.getRemark());
            hasChange = true;
        }

        if (StringUtils.isNotBlank(propDto.getDataConfig()) && !propDto.getDataConfig().equals(existingProp.getDataConfig())) {
            existingProp.setDataConfig(propDto.getDataConfig());
            hasChange = true;
        }

        if (hasChange) {
            mdProductPropMapper.updateById(existingProp);
            log.info("更新产品属性: {}", propDto.getCode());
        }
    }

    /**
     * 创建新属性
     */
    private void createNewProperty(Long productId, PropertyExcelDto propDto, ProductExcelDto excelDto) {
        MdProductProp prop = new MdProductProp();

        prop.setId(IdWorker.getId());
        prop.setProdId(productId);
        prop.setPropCode(propDto.getCode());
        prop.setName(propDto.getName());
        prop.setDataType(propDto.getDataType());
        prop.setRemark(propDto.getRemark());

        // 处理单位
        if (StringUtils.isNotBlank(propDto.getUnit())) {
            String unitCode = getUnitCodeFromHiddenMappings(excelDto, propDto.getUnit());
            Long unitId = findOrCreateUnit(unitCode);
            prop.setUnitId(unitId);
            prop.setUnitCode(unitCode);
        }

        // 设置读写类型
        if (propDto.getDataWrite() != null) {
            prop.setDataWrite(propDto.getDataWrite());
        } else {
            prop.setDataWrite(0); // 默认只读
        }

        if (propDto.getPrecision() != null) {
            prop.setPrecision(propDto.getPrecision());
        }

        if (StringUtils.isNotBlank(propDto.getDataConfig())) {
            prop.setDataConfig(propDto.getDataConfig());
        }

        mdProductPropMapper.insert(prop);
        log.info("创建新属性: {}", propDto.getCode());
    }

    /**
     * 查找或创建单位
     *
     * @param unitCode 单位编码（如果有隐藏映射）或单位名称（向后兼容）
     *
     * @return 单位ID
     */
    private Long findOrCreateUnit(String unitCode) {
        if (!StringUtils.isNotBlank(unitCode)) {
            return null;
        }

        try {
            // 首先查找现有单位
            IotSysUnitEntity existingUnit = iotSysUnitMapper.queryByUnitCode(unitCode);
            if (existingUnit != null) {
                return existingUnit.getId();
            }

            // 如果没有找到，创建新单位
            IotSysUnitVo unitVo = new IotSysUnitVo();
            unitVo.setUnitCode(unitCode);
            unitVo.setUnitName(unitCode); // 单位名称默认与编码相同
            unitVo.setUnitType(1); // 默认类型
            unitVo.setUnitStatus(0); // 正常状态
            unitVo.setRemark("导入时自动创建的单位");

            commonDictItemService.saveUnit(unitVo);

            // 重新查询获取ID
            IotSysUnitEntity newUnit = iotSysUnitMapper.queryByUnitCode(unitCode);
            log.info("创建新单位: {}, ID: {}", unitCode, newUnit.getId());

            return newUnit.getId();

        } catch (Exception e) {
            log.error("处理单位失败: {}", unitCode, e);
            return null;
        }
    }

    /**
     * 保存或更新产品事件
     */
    private void saveOrUpdateProductEvents(Long productId, List<EventExcelDto> events) throws JsonProcessingException {
        for (EventExcelDto eventDto : events) {
            // 检查事件是否已存在
            LambdaQueryWrapper<MdProductEvent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MdProductEvent::getProdId, productId)
                    .eq(MdProductEvent::getEventCode, eventDto.getCode());

            MdProductEvent existingEvent = mdProductEventMapper.selectOne(queryWrapper);

            if (existingEvent != null) {
                // 更新现有事件
                updateExistingEvent(existingEvent, eventDto);
            } else {
                // 创建新事件
                createNewEvent(productId, eventDto);
            }
        }
    }

    /**
     * 更新现有事件
     */
    private void updateExistingEvent(MdProductEvent existingEvent, EventExcelDto eventDto) {
        boolean hasChange = false;

        if (StringUtils.isNotBlank(eventDto.getName()) && !eventDto.getName().equals(existingEvent.getName())) {
            existingEvent.setName(eventDto.getName());
            hasChange = true;
        }

        if (StringUtils.isNotBlank(eventDto.getRemark()) && !eventDto.getRemark().equals(existingEvent.getRemark())) {
            existingEvent.setRemark(eventDto.getRemark());
            hasChange = true;
        }

        if (hasChange) {
            mdProductEventMapper.updateById(existingEvent);
            log.info("更新产品事件: {}", eventDto.getCode());
        }
    }

    /**
     * 创建新事件
     */
    private void createNewEvent(Long productId, EventExcelDto eventDto) throws JsonProcessingException {
        MdProductEvent event = new MdProductEvent();

        event.setId(IdWorker.getId());
        event.setProdId(productId);
        event.setEventCode(eventDto.getCode());
        event.setName(eventDto.getName());
        event.setRemark(eventDto.getRemark());
        event.setStatus(1); // 默认启用
        event.setConfig(StringUtils.isEmpty(eventDto.getConfig()) ? EventConfig.buildNatureEventConfig()
                : OBJECT_MAPPER.readValue(eventDto.getConfig(), EventConfig.class));
        //TODO: 基于属性的事件如何处理？

        List<SysDictItem> categories = sysDictItemMapper.selectList(new LambdaQueryWrapper<SysDictItem>()
                                                                            .eq(SysDictItem::getDictId, InnerDicEnum.event_type.id)
                                                                            .eq(SysDictItem::getItemValue, eventDto.getCategory()));

        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            event.setCategoryId(categories.get(0).getId());
        } else {
            SysDictItem sysDictItem = new SysDictItem();
            sysDictItem.setId(IdWorker.getId());
            sysDictItem.setDictId(InnerDicEnum.event_type.id);
            sysDictItem.setItemValue(eventDto.getCategory());
            sysDictItem.setItemLabel(eventDto.getCategory());
            sysDictItem.setStatus(1);
            sysDictItem.setItemParentId(0L);
            sysDictItem.setItemLevel(platformEvnCom.hierarchicalNumbering.get1LevelId(1));
            sysDictItemMapper.insert(sysDictItem);

            event.setCategoryId(sysDictItem.getId());
        }

        mdProductEventMapper.insert(event);
        log.info("创建新事件: {}", eventDto.getCode());
    }

    /**
     * 保存或更新产品方法（覆盖模式）
     */
    private void saveOrUpdateProductMethods(Long productId, List<MethodExcelDto> methods) {
        try {
            List<ProductMethodMgtVo> methodVos = methods.stream()
                    .map(dto -> convertToMethodVo(dto, productId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (methodVos.isEmpty()) {
                log.warn("产品方法列表为空，跳过保存: productId={}", productId);
                return;
            }

            ProductMethodBatchVo batchVo = new ProductMethodBatchVo();
            batchVo.setProdId(productId);
            batchVo.setMethods(methodVos);

            mdProductMethodService.saveOrUpdateProdMethods(batchVo);
            log.info("产品方法保存完成，productId={}, 方法数量: {}", productId, methodVos.size());
        } catch (Exception e) {
            log.error("保存产品方法失败，productId={}", productId, e);
            throw new RuntimeException("保存产品方法失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将 MethodExcelDto 转换为 ProductMethodMgtVo
     */
    private ProductMethodMgtVo convertToMethodVo(MethodExcelDto dto, Long productId) {
        try {
            ProductMethodMgtVo vo = new ProductMethodMgtVo();
            vo.setProdId(productId);
            vo.setMethodCode(dto.getMethodCode());
            vo.setName(dto.getName());
            vo.setMethodType(dto.getMethodType());
            vo.setDescription(dto.getDescription());
            vo.setTimeoutMs(dto.getTimeoutMs() != null ? dto.getTimeoutMs() : 10000);
            vo.setRetryable(dto.getRetryable() != null ? dto.getRetryable() : false);
            vo.setMaxRetries(dto.getMaxRetries() != null ? dto.getMaxRetries() : 0);
            vo.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
            vo.setRemark(dto.getRemark());

            vo.setInputParams(parseParamsJson(dto.getInputParams(), dto.getMethodCode(), "输入参数"));
            vo.setOutputParams(parseParamsJson(dto.getOutputParams(), dto.getMethodCode(), "输出参数"));

            return vo;
        } catch (Exception e) {
            log.error("转换方法DTO失败: methodCode={}", dto.getMethodCode(), e);
            return null;
        }
    }

    private Map<String, Object> parseParamsJson(String json, String methodCode, String fieldName) {
        if (!StringUtils.isNotBlank(json)) {
            return new HashMap<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.warn("解析{}JSON失败，methodCode={}, 使用空对象", fieldName, methodCode);
            return new HashMap<>();
        }
    }

}
