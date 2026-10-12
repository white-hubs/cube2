package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.iot.product.model.v3.enums.DataTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.EntityTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.OperationTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.guard.DeletionGuardManager;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductPropMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductProp;
import com.guhecloud.cube.base.iot.product.model.v3.param.PropSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductPropService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductModelVersionService;
import com.guhecloud.cube.base.iot.product.model.v3.util.EntityBuilder;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductPropVO;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 属性管理Service实现 v3版本
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3MdProductPropServiceImpl extends ServiceImpl<V3MdProductPropMapper, MdProductProp> implements V3MdProductPropService {

    private final V3MdProductPropMapper propMapper;
    private final V3ProductModelVersionService versionService;
    private final ObjectMapper objectMapper;
    private final DeletionGuardManager guardManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveProp(PropSaveParam param) {
        log.info("开始创建属性, prodId={}, propCode={}", param.getProdId(), param.getPropCode());

        // 1. 校验prop_code在产品下的唯一性
        MdProductProp existingProp = getByProdIdAndPropCode(param.getProdId(), param.getPropCode());
        if (existingProp != null) {
            throw new InvalidArgumentException("属性编码在该产品下已存在: " + param.getPropCode());
        }

        // 2. 参数校验
        validateProp(param);

        // 3. 构建属性实体
        MdProductProp prop = EntityBuilder.buildProp(param);

        // 4. 保存属性
        save(prop);

        log.info("属性创建成功, propId={}, propCode={}", prop.getId(), param.getPropCode());

        // 7. 触发版本快照生成
        try {
            versionService.createSnapshot(param.getProdId(), OperationTypeEnum.UPDATE.getCode(), "创建属性: " + param.getName());
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", param.getProdId(), e);
            throw new ServiceException("创建属性快照失败", e);
        }

        return prop.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProp(Long propId, PropSaveParam param) {
        log.info("开始更新属性, propId={}", propId);

        // 1. 检查属性是否存在
        MdProductProp prop = getById(propId);
        if (prop == null) {
            throw new InvalidArgumentException("属性不存在");
        }

        // 2. 如果修改了propCode,需要校验唯一性
        if (!prop.getPropCode().equals(param.getPropCode())) {
            MdProductProp existingProp = getByProdIdAndPropCode(param.getProdId(), param.getPropCode());
            if (existingProp != null && !existingProp.getId().equals(propId)) {
                throw new InvalidArgumentException("属性编码在该产品下已存在: " + param.getPropCode());
            }
        }

        // 3. 参数校验
        validateProp(param);

        // 4. 更新属性信息
        EntityBuilder.updateProp(prop, param);

        // 5. 保存更新
        updateById(prop);

        log.info("属性更新成功, propId={}", propId);

        // 6. 触发版本快照生成
        try {
            versionService.createSnapshot(param.getProdId(), OperationTypeEnum.UPDATE.getCode(), "更新属性: " + param.getName());
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", param.getProdId(), e);
            throw new ServiceException("更新属性快照失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProp(Long propId) {
        log.info("开始删除属性, propId={}", propId);

        // 1. 检查属性是否存在
        MdProductProp prop = getById(propId);
        if (prop == null) {
            throw new InvalidArgumentException("属性不存在");
        }

        Long prodId = prop.getProdId();

        // 2. 删除前关联检查(检查告警规则、数据流转规则等引用)
        guardManager.checkBeforeDelete(propId, EntityTypeEnum.PROPERTY.getCode());

        // 3. 软删除
        DatabaseContextHolder.softDelete(this, prop.getId());

        log.info("属性删除成功, propId={}", propId);

        // 4. 触发版本快照生成
        try {
            versionService.createSnapshot(prodId, OperationTypeEnum.UPDATE.getCode(), "删除属性");
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", prodId, e);
            throw new ServiceException("删除属性快照失败", e);
        }
    }

    @Override
    public List<ProductPropVO> listByProdId(Long prodId) {
        log.info("查询产品属性列表, prodId={}", prodId);
        return propMapper.selectByProdId(prodId);
    }

    @Override
    public ProductPropVO getPropDetail(Long propId) {
        log.info("查询属性详情, propId={}", propId);

        MdProductProp prop = getById(propId);
        if (prop == null) {
            throw new InvalidArgumentException("属性不存在");
        }

        // 转换为VO
        return EntityBuilder.buildPropVO(prop);
    }

    @Override
    public MdProductProp getByProdIdAndPropCode(Long prodId, String propCode) {
        return propMapper.selectByProdIdAndCode(prodId, propCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Long prodId, List<PropSaveParam> params) {
        log.info("批量创建属性, prodId={}, count={}", prodId, params.size());

        for (PropSaveParam param : params) {
            // 确保prodId一致
            param.setProdId(prodId);
            saveProp(param);
        }

        log.info("批量创建属性完成, prodId={}, count={}", prodId, params.size());
    }

    /**
     * 参数校验
     */
    private void validateProp(PropSaveParam param) {
        // 1. 校验数据类型是否合法
        String dataType = param.getDataType();
        if (!isValidDataType(dataType)) {
            throw new InvalidArgumentException("不支持的数据类型: " + dataType);
        }

        // 2. 如果是数值类型,校验dataConfig格式
        if (isNumericType(dataType) && StringUtils.hasText(param.getDataConfig())) {
            validateJsonFormat(param.getDataConfig());
        }

        // 3. 如果是枚举类型,dataConfig必须提供
        if (DataTypeEnum.ENUM.getCode().equals(dataType) && !StringUtils.hasText(param.getDataConfig())) {
            throw new InvalidArgumentException("枚举类型必须提供数据配置");
        }
    }

    /**
     * 校验数据类型是否合法
     */
    private boolean isValidDataType(String dataType) {
        return DataTypeEnum.INT.getCode().equals(dataType)
                || DataTypeEnum.LONG.getCode().equals(dataType)
                || DataTypeEnum.FLOAT.getCode().equals(dataType)
                || DataTypeEnum.DOUBLE.getCode().equals(dataType)
                || DataTypeEnum.STRING.getCode().equals(dataType)
                || DataTypeEnum.BOOL.getCode().equals(dataType)
                || DataTypeEnum.DATE.getCode().equals(dataType)
                || DataTypeEnum.ENUM.getCode().equals(dataType)
                || DataTypeEnum.STRUCT.getCode().equals(dataType)
                || DataTypeEnum.ARRAY.getCode().equals(dataType);
    }

    /**
     * 判断是否为数值类型
     */
    private boolean isNumericType(String dataType) {
        return DataTypeEnum.INT.getCode().equals(dataType)
                || DataTypeEnum.LONG.getCode().equals(dataType)
                || DataTypeEnum.FLOAT.getCode().equals(dataType)
                || DataTypeEnum.DOUBLE.getCode().equals(dataType);
    }

    /**
     * 校验JSON格式
     */
    private void validateJsonFormat(String jsonStr) {
        if (!StringUtils.hasText(jsonStr)) {
            return;
        }
        try {
            objectMapper.readTree(jsonStr);
        } catch (Exception e) {
            throw new InvalidArgumentException("dataConfig" + "格式不正确,必须是有效的JSON字符串", e);
        }
    }
}
