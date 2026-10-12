package com.guhecloud.cube.iot.mgt.things.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.translate.MethodSpecValidator;
import com.guhecloud.cube.iot.mgt.comm.util.ParameterCheck;
import com.guhecloud.cube.iot.mgt.core.data.lang.MethodTypeEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMethodMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProductMethod;
import com.guhecloud.cube.iot.mgt.things.server.MdProductMethodService;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodBatchVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodMgtVo;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.comm.translate.MethodSpecValidator.parseParams;
import static com.guhecloud.cube.iot.mgt.comm.translate.MethodSpecValidator.toJSONString;
import static com.guhecloud.cube.iot.mgt.things.ProductUtils.incrementProductVersion;

/**
 * 产品方法服务实现
 */
@Service
public class MdProductMethodServiceImpl extends ServiceImpl<MdProductMethodMapper, MdProductMethod> implements MdProductMethodService {

    private static final int DEFAULT_TIMEOUT = 60000;
    private static final int DEFAULT_STATUS = 1;
    private static final int RETRY_DISABLED = 0;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private PlatformEvnCom platformEvnCom;

    @Resource
    private MdProductMapper mdProductMapper;

    @Resource
    private MdProductMethodMapper mdProductMethodMapper;

    @Override
    public void saveProdMethod(ProductMethodMgtVo productMethodMgtVo) {
        validateMethodVo(productMethodMgtVo);
        performCreate(productMethodMgtVo);
        incrementProductVersion(productMethodMgtVo.getProdId(), mdProductMapper);
    }

    @Override
    public void updateProdMethod(ProductMethodMgtVo productMethodMgtVo) {
        validateMethodVo(productMethodMgtVo);
        performUpdate(productMethodMgtVo);
        incrementProductVersion(productMethodMgtVo.getProdId(), mdProductMapper);
    }

    @Override
    public void deleteProdMethod(Long methodId) {
        MdProductMethod method = findById(methodId);
        if (method == null) {
            return;
        }

        DatabaseContextHolder.softDelete(this, methodId);
        incrementProductVersion(method.getProdId(), mdProductMapper);
    }

    @Override
    public ProductMethodMgtVo prodMethodDetail(Long methodId) {
        MdProductMethod method = findById(methodId);
        Assert.isNotFound(method == null, "产品方法不存在");
        return convert2ProductMethodVo(method);
    }

    @Override
    public List<ProductMethodMgtVo> listProdMethod(Long prodId) {
        List<MdProductMethod> methods = findActiveByProdId(prodId);

        return methods.stream()
                .map(this::convert2ProductMethodVo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateProdMethods(ProductMethodBatchVo batchVo) {
        Long prodId = batchVo.getProdId();
        List<ProductMethodMgtVo> methods = batchVo.getMethods();

        // 如果方法列表为空，删除所有方法
        if (methods == null || methods.isEmpty()) {
            List<MdProductMethod> existingMethods = findActiveByProdId(prodId);
            for (MdProductMethod method : existingMethods) {
                DatabaseContextHolder.softDelete(this, method.getId());
            }
            if (!existingMethods.isEmpty()) {
                incrementProductVersion(prodId, mdProductMapper);
            }
            return;
        }

        // 1. 查询现有方法
        List<MdProductMethod> existingMethods = findActiveByProdId(prodId);

        // 提取现有方法的ID集合和methodCode映射
        Set<Long> existingIds = existingMethods.stream()
                .map(MdProductMethod::getId)
                .collect(Collectors.toSet());

        Map<String, Long> existingMethodCodeMap = existingMethods.stream()
                .collect(Collectors.toMap(
                        MdProductMethod::getMethodCode,
                        MdProductMethod::getId
                ));

        // 2. 第一层校验：前端传递的方法列表中methodCode不能重复
        Set<String> methodCodeSet = new HashSet<>();
        for (ProductMethodMgtVo method : methods) {
            if (method.getMethodCode() != null) {
                ParameterCheck.checkIsFalse(methodCodeSet.contains(method.getMethodCode()),"方法编码重复: " + method.getMethodCode());
                methodCodeSet.add(method.getMethodCode());
            }
        }

        // 3. 分类前端传递的方法
        List<ProductMethodMgtVo> methodsToCreate = new ArrayList<>();
        List<ProductMethodMgtVo> methodsToUpdate = new ArrayList<>();
        Set<Long> methodIdsToKeep = new HashSet<>();

        for (ProductMethodMgtVo method : methods) {
            // 设置prodId并校验参数
            method.setProdId(prodId);
            validateMethodVo(method);

            // 判断是更新还是创建
            if (method.getId() != null && existingIds.contains(method.getId())) {
                // 更新操作：ID存在且在数据库中
                methodsToUpdate.add(method);
                methodIdsToKeep.add(method.getId());
            } else if (method.getMethodCode() != null && existingMethodCodeMap.containsKey(method.getMethodCode())) {
                // 更新操作：通过methodCode匹配到现有方法
                Long existingId = existingMethodCodeMap.get(method.getMethodCode());
                method.setId(existingId);
                methodsToUpdate.add(method);
                methodIdsToKeep.add(existingId);
            } else {
                // 创建操作：既没有ID也不匹配现有methodCode
                methodsToCreate.add(method);
            }
        }

        // 4. 删除不需要的方法
        List<Long> methodIdsToDelete = existingMethods.stream()
                .map(MdProductMethod::getId)
                .filter(id -> !methodIdsToKeep.contains(id))
                .toList();

        for (Long methodId : methodIdsToDelete) {
            DatabaseContextHolder.softDelete(this, methodId);
        }

        // 5. 执行更新操作（第二层校验在performUpdate中）
        for (ProductMethodMgtVo methodVo : methodsToUpdate) {
            performUpdate(methodVo);
        }

        // 6. 执行创建操作（第二层校验在performCreate中）
        for (ProductMethodMgtVo methodVo : methodsToCreate) {
            performCreate(methodVo);
        }

        // 7. 统一递增版本号（只一次）
        if (!methodIdsToDelete.isEmpty() || !methodsToUpdate.isEmpty() || !methodsToCreate.isEmpty()) {
            incrementProductVersion(prodId, mdProductMapper);
        }
    }

    /**
     * 校验方法VO参数
     */
    private void validateMethodVo(ProductMethodMgtVo productMethodMgtVo) {
        ParameterCheck.checkIsNotNull(productMethodMgtVo, "方法信息不能为空");
        ParameterCheck.checkIsNotNull(productMethodMgtVo.getProdId(), "产品ID不能为空");
        ParameterCheck.checkIsNotNull(productMethodMgtVo.getName(), "方法名称不能为空");
        ParameterCheck.checkIsNotNull(productMethodMgtVo.getName().trim(), "方法名称不能为空");
        ParameterCheck.checkIsNotNull(productMethodMgtVo.getMethodCode(), "方法编码不能为空");
        ParameterCheck.checkIsNotNull(productMethodMgtVo.getMethodCode().trim(), "方法编码不能为空");

        String methodTypeCode = StringUtils.isBlank(productMethodMgtVo.getMethodType())
                ? MethodTypeEnum.SYNC.code
                : productMethodMgtVo.getMethodType();

        MethodTypeEnum methodTypeEnum;
        try {
            methodTypeEnum = MethodTypeEnum.fromCode(methodTypeCode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("方法类型错误");
        }

        productMethodMgtVo.setMethodType(methodTypeEnum.code);

        if (productMethodMgtVo.getTimeoutMs() == null) {
            productMethodMgtVo.setTimeoutMs(DEFAULT_TIMEOUT);
        }
        ParameterCheck.checkIsTrue(productMethodMgtVo.getTimeoutMs() > 0, "超时时间必须大于0");

        if (productMethodMgtVo.getRetryable() == null) {
            productMethodMgtVo.setRetryable(false);
        }
        if (!Boolean.TRUE.equals(productMethodMgtVo.getRetryable())) {
            productMethodMgtVo.setMaxRetries(0);
        } else if (productMethodMgtVo.getMaxRetries() == null) {
            productMethodMgtVo.setMaxRetries(0);
        }

        ParameterCheck.checkIsNotNull(productMethodMgtVo.getMaxRetries(), "最大重试次数必须大于等于0");
        ParameterCheck.checkIsTrue(productMethodMgtVo.getMaxRetries() >= 0, "最大重试次数必须大于等于0");

        if (productMethodMgtVo.getStatus() == null) {
            productMethodMgtVo.setStatus(DEFAULT_STATUS);
        }
    }

    /**
     * 执行创建操作（从saveProdMethod提取）
     * 注意：不包含版本递增，由调用方控制
     */
    private void performCreate(ProductMethodMgtVo methodVo) {
        // 检查methodCode唯一性（保持原有逻辑）
        Long size = mdProductMethodMapper.selectCount(new LambdaQueryWrapper<>(MdProductMethod.class)
                .eq(MdProductMethod::getMethodCode, methodVo.getMethodCode())
                .eq(MdProductMethod::getProdId, methodVo.getProdId())
        );
        ParameterCheck.checkIsTrue(size == null || size == 0, "方法%s已存在".formatted(methodVo.getMethodCode()));

        // 校验和规范化参数
        MethodSpecValidator.MethodParamSpec paramSpec = MethodSpecValidator.validateAndNormalize(
                methodVo.getInputParams(),
                methodVo.getOutputParams()
        );

        // 构建实体对象（复用现有的convert方法）
        MdProductMethod method = convert2ProductMethod(methodVo, paramSpec);
        method.setId(platformEvnCom.nextSnowflakeId());
        method.setDeleted(false);

        this.save(method);
    }

    /**
     * 执行更新操作（从updateProdMethod提取）
     * 注意：不包含版本递增，由调用方控制
     */
    private void performUpdate(ProductMethodMgtVo methodVo) {
        ParameterCheck.checkIsNotNull(methodVo.getId(), "方法ID不能为空");

        // 校验ID存在性（保持原有逻辑）
        MdProductMethod exist = findById(methodVo.getId());

        ParameterCheck.checkIsNotNull(exist, "产品方法不存在");

        // 检查methodCode唯一性（排除自身，保持原有逻辑）
        if (!exist.getMethodCode().equals(methodVo.getMethodCode())) {
            Long size = mdProductMethodMapper.selectCount(new LambdaQueryWrapper<>(MdProductMethod.class)
                    .eq(MdProductMethod::getMethodCode, methodVo.getMethodCode())
                    .eq(MdProductMethod::getProdId, methodVo.getProdId())
            );
            ParameterCheck.checkIsTrue(size == null || size == 0, "方法%s已存在".formatted(methodVo.getMethodCode()));
            Assert.isParameterError(size == null || size > 0, "方法%s已存在".formatted(methodVo.getMethodCode()));
        }

        // 校验和规范化参数
        MethodSpecValidator.MethodParamSpec paramSpec = MethodSpecValidator.validateAndNormalize(
                methodVo.getInputParams(),
                methodVo.getOutputParams()
        );

        // 构建实体对象（复用现有的convert方法）
        MdProductMethod method = convert2ProductMethod(methodVo, paramSpec);
        method.setId(methodVo.getId());
        method.setDeleted(false);

        this.updateById(method);
    }

    /**
     * 转换VO为实体
     */
    private MdProductMethod convert2ProductMethod(ProductMethodMgtVo productMethodMgtVo,
                                                  MethodSpecValidator.MethodParamSpec paramSpec) {
        MdProductMethod method = new MdProductMethod();
        method.setProdId(productMethodMgtVo.getProdId());
        method.setName(productMethodMgtVo.getName());
        method.setMethodCode(productMethodMgtVo.getMethodCode());
        method.setDescription(productMethodMgtVo.getDescription());
        method.setMethodType(productMethodMgtVo.getMethodType());
        method.setInputParams(toJSONString(paramSpec.inputParams(), "inputParams"));
        method.setOutputParams(toJSONString(paramSpec.outputParams(), "outputParams"));
        method.setTimeoutMs(productMethodMgtVo.getTimeoutMs());
        method.setRetryable(Boolean.TRUE.equals(productMethodMgtVo.getRetryable()) ? 1 : RETRY_DISABLED);
        method.setMaxRetries(productMethodMgtVo.getMaxRetries());
        method.setStatus(productMethodMgtVo.getStatus());
        method.setExtendConfig(serializeToJson(productMethodMgtVo.getExtendConfig()));
        method.setRemark(productMethodMgtVo.getRemark());
        return method;
    }

    /**
     * 转换实体为VO
     */
    private ProductMethodMgtVo convert2ProductMethodVo(MdProductMethod method) {
        ProductMethodMgtVo vo = new ProductMethodMgtVo();
        vo.setId(method.getId());
        vo.setProdId(method.getProdId());
        vo.setName(method.getName());
        vo.setMethodCode(method.getMethodCode());
        vo.setDescription(method.getDescription());
        vo.setMethodType(method.getMethodType());
        vo.setInputParams(parseParams(method.getInputParams(), "inputParams"));
        vo.setOutputParams(parseParams(method.getOutputParams(), "outputParams"));
        vo.setTimeoutMs(method.getTimeoutMs());
        vo.setRetryable(Objects.equals(method.getRetryable(), 1));
        vo.setMaxRetries(method.getMaxRetries());
        vo.setStatus(method.getStatus());
        vo.setExtendConfig(parseJsonToMap(method.getExtendConfig()));
        vo.setRemark(method.getRemark());
        vo.setCreateTimeFormat(DateUtils.format(method.getCreateTime()));
        return vo;
    }

    /**
     * 序列化Map为JSON字符串
     */
    private String serializeToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new ServiceException("JSON序列化失败: " + e.getMessage());
        }
    }

    /**
     * 解析JSON字符串为Map
     */
    private Map<String, Object> parseJsonToMap(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    protected MdProductMethod findById(Long methodId) {
        return this.getById(methodId);
    }

    protected List<MdProductMethod> findActiveByProdId(Long prodId) {
        return this.list(new LambdaQueryWrapper<MdProductMethod>()
                                 .eq(MdProductMethod::getProdId, prodId)
                                 .eq(MdProductMethod::getDeleted, false)
                                 .orderByDesc(MdProductMethod::getCreateTime));
    }
}
