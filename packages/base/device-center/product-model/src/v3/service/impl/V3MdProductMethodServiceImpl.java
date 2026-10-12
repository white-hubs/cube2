package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.iot.product.model.v3.enums.EntityTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.MethodTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.OperationTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.guard.DeletionGuardManager;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductMethodMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductMethod;
import com.guhecloud.cube.base.iot.product.model.v3.model.params.Params;
import com.guhecloud.cube.base.iot.product.model.v3.model.params.ParamSpec;
import com.guhecloud.cube.base.iot.product.model.v3.param.MethodSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductMethodService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductModelVersionService;
import com.guhecloud.cube.base.iot.product.model.v3.util.EntityBuilder;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductMethodVO;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 方法管理Service实现 v3版本(新增功能)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3MdProductMethodServiceImpl extends ServiceImpl<V3MdProductMethodMapper, MdProductMethod> implements V3MdProductMethodService {

    private final V3MdProductMethodMapper methodMapper;
    private final V3ProductModelVersionService versionService;
    private final ObjectMapper objectMapper;
    private final DeletionGuardManager guardManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveMethod(MethodSaveParam param) {
        log.info("开始创建方法, prodId={}, methodCode={}", param.getProdId(), param.getMethodCode());

        // 1. 校验method_code在产品下的唯一性
        MdProductMethod existingMethod = getByProdIdAndMethodCode(param.getProdId(), param.getMethodCode());
        if (existingMethod != null) {
            throw new InvalidArgumentException("方法编码在该产品下已存在: " + param.getMethodCode());
        }

        // 2. 参数校验
        validateMethod(param);

        // 3. 构建方法实体
        MdProductMethod method = EntityBuilder.buildMethod(param);

        // 4. 保存方法
        save(method);

        log.info("方法创建成功, methodId={}, methodCode={}", method.getId(), param.getMethodCode());

        // 6. 触发版本快照生成
        try {
            versionService.createSnapshot(param.getProdId(), OperationTypeEnum.UPDATE.getCode(), "创建方法: " + param.getName());
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", param.getProdId(), e);
        }

        return method.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMethod(Long methodId, MethodSaveParam param) {
        log.info("开始更新方法, methodId={}", methodId);

        // 1. 检查方法是否存在
        MdProductMethod method = getById(methodId);
        if (method == null) {
            throw new InvalidArgumentException("方法不存在");
        }

        // 2. 如果修改了methodCode,需要校验唯一性
        if (!method.getMethodCode().equals(param.getMethodCode())) {
            MdProductMethod existingMethod = getByProdIdAndMethodCode(param.getProdId(), param.getMethodCode());
            if (existingMethod != null && !existingMethod.getId().equals(methodId)) {
                throw new InvalidArgumentException("方法编码在该产品下已存在: " + param.getMethodCode());
            }
        }

        // 3. 参数校验
        validateMethod(param);

        // 4. 更新方法信息
        EntityBuilder.updateMethod(method, param);

        // 5. 保存更新
        updateById(method);

        log.info("方法更新成功, methodId={}", methodId);

        // 6. 触发版本快照生成
        try {
            versionService.createSnapshot(param.getProdId(), OperationTypeEnum.UPDATE.getCode(), "更新方法: " + param.getName());
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", param.getProdId(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMethod(Long methodId) {
        log.info("开始删除方法, methodId={}", methodId);

        // 1. 检查方法是否存在
        MdProductMethod method = getById(methodId);
        if (method == null) {
            throw new RuntimeException("方法不存在");
        }

        Long prodId = method.getProdId();

        // 2. 删除前关联检查(检查场景联动、定时任务等引用)
        guardManager.checkBeforeDelete(methodId, EntityTypeEnum.METHOD.getCode());

        // 3. 软删除
        DatabaseContextHolder.softDelete(this, method.getId());

        log.info("方法删除成功, methodId={}", methodId);

        // 4. 触发版本快照生成
        try {
            versionService.createSnapshot(prodId, OperationTypeEnum.UPDATE.getCode(), "删除方法");
        } catch (Exception e) {
            log.error("生成版本快照失败, prodId={}", prodId, e);
        }
    }

    @Override
    public List<ProductMethodVO> listByProdId(Long prodId) {
        log.info("查询产品方法列表, prodId={}", prodId);
        return methodMapper.selectByProdId(prodId);
    }

    @Override
    public ProductMethodVO getMethodDetail(Long methodId) {
        log.info("查询方法详情, methodId={}", methodId);

        MdProductMethod method = getById(methodId);
        if (method == null) {
            throw new InvalidArgumentException("方法不存在");
        }

        // 转换为VO
        return EntityBuilder.buildMethodVO(method);
    }

    @Override
    public MdProductMethod getByProdIdAndMethodCode(Long prodId, String methodCode) {
        return methodMapper.selectByProdIdAndCode(prodId, methodCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Long prodId, List<MethodSaveParam> params) {
        log.info("批量创建方法, prodId={}, count={}", prodId, params.size());

        for (MethodSaveParam param : params) {
            // 确保prodId一致
            param.setProdId(prodId);
            saveMethod(param);
        }

        log.info("批量创建方法完成, prodId={}, count={}", prodId, params.size());
    }

    /**
     * 参数校验
     */
    private void validateMethod(MethodSaveParam param) {
        // 1. 校验调用类型是否合法
        String callType = param.getCallType();
        if (!isValidCallType(callType)) {
            throw new InvalidArgumentException("不支持的调用类型: " + callType);
        }

        // 2. 校验输入参数
        if (param.getInputParams() != null) {
            validateMethodParams(param.getInputParams(), "输入参数");
        }

        // 3. 校验输出参数
        if (param.getOutputParams() != null) {
            validateMethodParams(param.getOutputParams(), "输出参数");
        }

        // 4. 如果是同步调用,超时时间必须设置
        if (MethodTypeEnum.SYNC.getCode().equals(callType) && param.getTimeoutSeconds() == null) {
            throw new InvalidArgumentException("同步调用必须设置超时时间");
        }
    }

    /**
     * 校验调用类型是否合法
     */
    private boolean isValidCallType(String callType) {
        return MethodTypeEnum.SYNC.getCode().equals(callType)
                || MethodTypeEnum.ASYNC.getCode().equals(callType);
    }

    /**
     * 校验方法参数对象
     */
    private void validateMethodParams(Params methodParams, String paramType) {
        if (methodParams == null || methodParams.getParams() == null) {
            return;
        }

        List<ParamSpec> params = methodParams.getParams();
        for (int i = 0; i < params.size(); i++) {
            ParamSpec param = params.get(i);

            // 校验参数名称
            if (!StringUtils.hasText(param.getName())) {
                throw new InvalidArgumentException(String.format("%s[%d]的参数名称不能为空", paramType, i));
            }

            // 校验参数编码
            if (!StringUtils.hasText(param.getParamCode())) {
                throw new InvalidArgumentException(String.format("%s[%d]的参数编码不能为空", paramType, i));
            }

            // 校验数据类型
            if (!StringUtils.hasText(param.getDataType())) {
                throw new InvalidArgumentException(String.format("%s[%d]的数据类型不能为空", paramType, i));
            }
        }
    }
}
