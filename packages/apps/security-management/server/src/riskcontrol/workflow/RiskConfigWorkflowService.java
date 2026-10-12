package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentDimensionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentLevelRuleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentMethodPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentMethodParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskAssessmentOptionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskEnabledParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitTypePageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitTypeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningSignPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningSignParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentDimensionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentLevelRuleVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskAssessmentMethodVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitTypeVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.entity.RiskAssessmentRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.service.impl.RiskAssessmentRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskLevelEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model.RiskPageAssembler;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlCodeUtils;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util.RiskControlCodeUtils.CodeType;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskWarningSignVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.convert.RiskConfigConvert;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentDimensionEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentLevelRuleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentMethodEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentOptionEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskUnitTypeEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskWarningSignEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentDimensionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentLevelRuleService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentMethodService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentOptionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskUnitTypeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskWarningSignService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade.FileFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity.RiskUnitSignRelEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.service.impl.RiskUnitSignRelService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("securityManagementRiskConfigWorkflowService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskConfigWorkflowService {
    private static final Set<String> PRESET_METHOD_CODES = Set.of("LEC", "LS", "MES");
    private static final Set<String> REQUIRED_RISK_LEVELS = Set.of(
            RiskLevelEnum.MAJOR.name(), RiskLevelEnum.LARGE.name(), RiskLevelEnum.GENERAL.name(), RiskLevelEnum.LOW.name());
    private static final Pattern FORMULA_VARIABLE_PATTERN = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
    private static final Pattern METHOD_FORMULA_PATTERN = Pattern.compile("[A-Za-z0-9_+\\-*/×÷().\\s]+");
    private static final Pattern DIMENSION_CODE_PATTERN = Pattern.compile("[A-Z]{1,3}");

    private final RiskWarningSignService warningSignService;
    private final RiskUnitTypeService unitTypeService;
    private final RiskAssessmentMethodService methodService;
    private final RiskAssessmentDimensionService dimensionService;
    private final RiskAssessmentOptionService optionService;
    private final RiskAssessmentLevelRuleService levelRuleService;
    private final RiskUnitSignRelService unitSignRelService;
    private final RiskUnitService unitService;
    private final RiskAssessmentRecordService assessmentRecordService;
    private final FileFacadeService fileFacadeService;

    public Pagination<RiskWarningSignVO> warningSignPage(RiskWarningSignPageParam param) {
        IPage<RiskWarningSignEntity> page = warningSignService.page(PagingUtils.convert(param),
                Wrappers.<RiskWarningSignEntity>lambdaQuery()
                        .like(StringUtils.isNotBlank(param.getKeyword()), RiskWarningSignEntity::getSignName, param.getKeyword())
                        .eq(StringUtils.isNotBlank(param.getSignType()), RiskWarningSignEntity::getSignType, param.getSignType())
                        .eq(param.getEnabled() != null, RiskWarningSignEntity::getEnabled, param.getEnabled())
                        .orderByDesc(RiskWarningSignEntity::getId));
        Pagination<RiskWarningSignVO> pagination = RiskPageAssembler.toPagination(page, RiskConfigConvert::toWarningSignVO);
        enrichWarningSigns(pagination.getRecords());
        return pagination;
    }

    public List<RiskOptionVO> warningSignOptions(RiskWarningSignPageParam param) {
        List<RiskOptionVO> options = warningSignService.list(Wrappers.<RiskWarningSignEntity>lambdaQuery()
                        .like(StringUtils.isNotBlank(param.getKeyword()), RiskWarningSignEntity::getSignName, param.getKeyword())
                        .eq(StringUtils.isNotBlank(param.getSignType()), RiskWarningSignEntity::getSignType, param.getSignType())
                        .eq(RiskWarningSignEntity::getEnabled, true))
                .stream().map(RiskConfigConvert::toOption).toList();
        enrichWarningSignOptions(options);
        return options;
    }

    public RiskWarningSignVO warningSignDetail(Long id) {
        RiskWarningSignEntity entity = warningSignService.getById(id);
        Assert.isNotFound(entity == null, "警示标志不存在");
        return enrichWarningSign(RiskConfigConvert.toWarningSignVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskWarningSignVO createWarningSign(RiskWarningSignParam param) {
        Long id = IdWorker.getId();
        param.setCode(RiskControlCodeUtils.resolve(param.getCode(), CodeType.WARNING_SIGN, id));
        assertWarningSignCodeAvailable(param.getCode(), null);
        param.setIconFileId(fileFacadeService.validateSingle(param.getIconFileId()));
        RiskWarningSignEntity entity = RiskConfigConvert.toWarningSignEntity(param);
        entity.setId(id);
        warningSignService.save(entity);
        fileFacadeService.replaceAssociations(FileFacadeService.SCENE_RISK_WARNING_SIGN_ICON,
                entity.getId(), entity.getIconFileId() == null ? List.of() : List.of(entity.getIconFileId()));
        return enrichWarningSign(RiskConfigConvert.toWarningSignVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskWarningSignVO updateWarningSign(Long id, RiskWarningSignParam param) {
        RiskWarningSignEntity entity = warningSignService.getById(id);
        Assert.isNotFound(entity == null, "警示标志不存在");
        param.setCode(RiskControlCodeUtils.resolveForUpdate(param.getCode(), entity.getSignCode()));
        assertWarningSignCodeAvailable(param.getCode(), id);
        if (param.getEnabled() == null) {
            param.setEnabled(entity.getEnabled());
        }
        param.setIconFileId(fileFacadeService.validateSingle(param.getIconFileId()));
        RiskConfigConvert.updateWarningSignEntity(entity, param);
        warningSignService.updateById(entity);
        fileFacadeService.replaceAssociations(FileFacadeService.SCENE_RISK_WARNING_SIGN_ICON,
                entity.getId(), entity.getIconFileId() == null ? List.of() : List.of(entity.getIconFileId()));
        return enrichWarningSign(RiskConfigConvert.toWarningSignVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteWarningSign(Long id) {
        Assert.isConflict(unitSignRelService.count(Wrappers.<RiskUnitSignRelEntity>lambdaQuery()
                .eq(RiskUnitSignRelEntity::getSignId, id)) > 0, "警示标志已被风险单元引用");
        DatabaseContextHolder.softDelete(warningSignService, id);
    }

    public Pagination<RiskUnitTypeVO> unitTypePage(RiskUnitTypePageParam param) {
        IPage<RiskUnitTypeEntity> page = unitTypeService.page(PagingUtils.convert(param),
                Wrappers.<RiskUnitTypeEntity>lambdaQuery()
                        .like(StringUtils.isNotBlank(param.getKeyword()), RiskUnitTypeEntity::getTypeName, param.getKeyword())
                        .orderByDesc(RiskUnitTypeEntity::getId));
        return RiskPageAssembler.toPagination(page, RiskConfigConvert::toVO);
    }

    public List<RiskOptionVO> unitTypeOptions(RiskUnitTypePageParam param) {
        return unitTypeService.list(Wrappers.<RiskUnitTypeEntity>lambdaQuery()
                        .like(StringUtils.isNotBlank(param.getKeyword()), RiskUnitTypeEntity::getTypeName, param.getKeyword()))
                .stream().map(RiskConfigConvert::toOption).toList();
    }

    public RiskUnitTypeVO unitTypeDetail(Long id) {
        RiskUnitTypeEntity entity = unitTypeService.getById(id);
        Assert.isNotFound(entity == null, "单元类型不存在");
        return RiskConfigConvert.toVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskUnitTypeVO createUnitType(RiskUnitTypeParam param) {
        Long id = IdWorker.getId();
        param.setCode(RiskControlCodeUtils.resolve(param.getCode(), CodeType.UNIT_TYPE, id));
        assertUnitTypeCodeAvailable(param.getCode(), null);
        Assert.isConflict(unitTypeService.count(Wrappers.<RiskUnitTypeEntity>lambdaQuery()
                .eq(RiskUnitTypeEntity::getTypeName, param.getName())) > 0, "单元类型名称已存在");
        RiskUnitTypeEntity entity = RiskConfigConvert.toUnitTypeEntity(param);
        entity.setId(id);
        unitTypeService.save(entity);
        return RiskConfigConvert.toVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskUnitTypeVO updateUnitType(Long id, RiskUnitTypeParam param) {
        RiskUnitTypeEntity entity = unitTypeService.getById(id);
        Assert.isNotFound(entity == null, "单元类型不存在");
        param.setCode(RiskControlCodeUtils.resolveForUpdate(param.getCode(), entity.getTypeCode()));
        assertUnitTypeCodeAvailable(param.getCode(), id);
        RiskConfigConvert.updateUnitTypeEntity(entity, param);
        unitTypeService.updateById(entity);
        return RiskConfigConvert.toVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUnitType(Long id) {
        Assert.isConflict(unitService.count(Wrappers.<RiskUnitEntity>lambdaQuery()
                .eq(RiskUnitEntity::getUnitTypeId, id)) > 0, "单元类型已被风险单元引用");
        DatabaseContextHolder.softDelete(unitTypeService, id);
    }

    public Pagination<RiskAssessmentMethodVO> methodPage(RiskAssessmentMethodPageParam param) {
        IPage<RiskAssessmentMethodEntity> page = methodService.page(PagingUtils.convert(param),
                Wrappers.<RiskAssessmentMethodEntity>lambdaQuery()
                        .and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> wrapper
                                .like(RiskAssessmentMethodEntity::getMethodName, param.getKeyword())
                                .or()
                                .like(RiskAssessmentMethodEntity::getMethodCode, param.getKeyword()))
                        .eq(param.getEnabled() != null, RiskAssessmentMethodEntity::getEnabled, param.getEnabled())
                        .orderByDesc(RiskAssessmentMethodEntity::getPresetFlag)
                        .orderByAsc(RiskAssessmentMethodEntity::getMethodCode));
        Pagination<RiskAssessmentMethodVO> pagination = RiskPageAssembler.toPagination(page, RiskConfigConvert::toVO);
        enrichMethodChildren(pagination.getRecords());
        return pagination;
    }

    public List<RiskOptionVO> methodOptions(RiskAssessmentMethodPageParam param) {
        return methodService.list(Wrappers.<RiskAssessmentMethodEntity>lambdaQuery()
                        .and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> wrapper
                                .like(RiskAssessmentMethodEntity::getMethodName, param.getKeyword())
                                .or()
                                .like(RiskAssessmentMethodEntity::getMethodCode, param.getKeyword()))
                        .eq(RiskAssessmentMethodEntity::getEnabled, true)
                        .orderByDesc(RiskAssessmentMethodEntity::getPresetFlag)
                        .orderByAsc(RiskAssessmentMethodEntity::getMethodCode))
                .stream().map(RiskConfigConvert::toOption).toList();
    }

    public RiskAssessmentMethodVO methodDetail(Long id) {
        RiskAssessmentMethodEntity method = methodService.getById(id);
        Assert.isNotFound(method == null, "评估方法不存在");
        RiskAssessmentMethodVO vo = RiskConfigConvert.toVO(method);
        enrichMethodChildren(List.of(vo));
        return vo;
    }

    private void enrichMethodChildren(List<RiskAssessmentMethodVO> methods) {
        if (CollectionUtils.isEmpty(methods)) {
            return;
        }
        List<Long> methodIds = methods.stream().map(RiskAssessmentMethodVO::getId).filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(methodIds)) {
            return;
        }
        List<RiskAssessmentDimensionEntity> dimensions = dimensionService.list(Wrappers.<RiskAssessmentDimensionEntity>lambdaQuery()
                .in(RiskAssessmentDimensionEntity::getMethodId, methodIds)
                .orderByAsc(RiskAssessmentDimensionEntity::getSortNo));
        List<Long> dimensionIds = dimensions.stream().map(RiskAssessmentDimensionEntity::getId).toList();
        Map<Long, List<RiskAssessmentOptionEntity>> optionMap = CollectionUtils.isEmpty(dimensionIds) ? Map.of()
                : optionService.list(Wrappers.<RiskAssessmentOptionEntity>lambdaQuery()
                        .in(RiskAssessmentOptionEntity::getDimensionId, dimensionIds)
                        .orderByAsc(RiskAssessmentOptionEntity::getSortNo))
                .stream().collect(Collectors.groupingBy(RiskAssessmentOptionEntity::getDimensionId));
        Map<Long, List<RiskAssessmentDimensionVO>> dimensionMap = dimensions.stream().collect(Collectors.groupingBy(
                RiskAssessmentDimensionEntity::getMethodId,
                Collectors.mapping(dimension -> {
                    RiskAssessmentDimensionVO dimensionVO = RiskConfigConvert.toVO(dimension);
                    dimensionVO.setOptions(optionMap.getOrDefault(dimension.getId(), List.of()).stream()
                            .map(RiskConfigConvert::toVO).toList());
                    return dimensionVO;
                }, Collectors.toList())));
        Map<Long, List<RiskAssessmentLevelRuleVO>> levelRuleMap = levelRuleService.list(Wrappers.<RiskAssessmentLevelRuleEntity>lambdaQuery()
                        .in(RiskAssessmentLevelRuleEntity::getMethodId, methodIds))
                .stream().collect(Collectors.groupingBy(RiskAssessmentLevelRuleEntity::getMethodId,
                        Collectors.mapping(RiskConfigConvert::toVO, Collectors.toList())));
        methods.forEach(method -> {
            method.setDimensions(dimensionMap.getOrDefault(method.getId(), List.of()));
            method.setLevelRules(levelRuleMap.getOrDefault(method.getId(), List.of()));
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAssessmentMethodVO createMethod(RiskAssessmentMethodParam param) {
        Long id = IdWorker.getId();
        param.setCode(RiskControlCodeUtils.resolve(param.getCode(), CodeType.ASSESSMENT_METHOD, id));
        validateMethodParam(param);
        assertMethodCodeAvailable(param.getCode(), null);
        RiskAssessmentMethodEntity method = RiskConfigConvert.toMethodEntity(param);
        method.setId(id);
        methodService.save(method);
        saveMethodChildren(method.getId(), param);
        return methodDetail(method.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAssessmentMethodVO updateMethod(Long id, RiskAssessmentMethodParam param) {
        RiskAssessmentMethodEntity method = methodService.getById(id);
        Assert.isNotFound(method == null, "评估方法不存在");
        param.setCode(RiskControlCodeUtils.resolveForUpdate(param.getCode(), method.getMethodCode()));
        if (Boolean.TRUE.equals(method.getPresetFlag()) || PRESET_METHOD_CODES.contains(method.getMethodCode())) {
            Assert.isConflict(!Objects.equals(method.getMethodCode(), param.getCode()), "预置评估方法编码不可修改");
            Assert.isConflict(!Objects.equals(method.getMethodName(), param.getName()), "预置评估方法名称不可修改");
            Assert.isConflict(!Objects.equals(method.getFormula(), param.getFormula()), "预置评估方法公式不可修改");
        }
        if (hasMethodChildren(param)) {
            validateMethodParam(param);
        } else {
            validateBasicMethodParam(param);
        }
        if (param.getEnabled() == null) {
            param.setEnabled(method.getEnabled());
        }
        assertMethodCodeAvailable(param.getCode(), id);
        RiskConfigConvert.updateMethodEntity(method, param);
        methodService.updateById(method);
        if (hasMethodChildren(param)) {
            softDeleteMethodChildren(id);
            saveMethodChildren(id, param);
        }
        return methodDetail(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public RiskAssessmentMethodVO patchMethod(Long id, RiskEnabledParam param) {
        RiskAssessmentMethodEntity method = methodService.getById(id);
        Assert.isNotFound(method == null, "评估方法不存在");
        Assert.isParameterError(param.getEnabled() == null, "启用状态不能为空");
        method.setEnabled(param.getEnabled());
        methodService.updateById(method);
        return RiskConfigConvert.toVO(method);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMethod(Long id) {
        RiskAssessmentMethodEntity method = methodService.getById(id);
        Assert.isNotFound(method == null, "评估方法不存在");
        Assert.isConflict(Boolean.TRUE.equals(method.getPresetFlag()) || PRESET_METHOD_CODES.contains(method.getMethodCode()),
                "预置评估方法不可删除");
        Assert.isConflict(assessmentRecordService.count(Wrappers.<RiskAssessmentRecordEntity>lambdaQuery()
                .eq(RiskAssessmentRecordEntity::getMethodId, id)) > 0, "评估方法已被评估记录引用");
        softDeleteMethodChildren(id);
        DatabaseContextHolder.softDelete(methodService, id);
    }

    private RiskWarningSignVO enrichWarningSign(RiskWarningSignVO vo) {
        enrichWarningSigns(List.of(vo));
        return vo;
    }

    private void enrichWarningSigns(List<RiskWarningSignVO> warningSigns) {
        if (CollectionUtils.isEmpty(warningSigns)) {
            return;
        }
        Set<String> fileIds = new LinkedHashSet<>();
        warningSigns.forEach(sign -> addFileId(fileIds, sign.getIconFileId()));
        Map<String, FileVO> fileMap = fileFacadeService.batchFiles(fileIds);
        warningSigns.forEach(sign -> sign.setIconFile(fileMap.get(normalizeFileId(sign.getIconFileId()))));
    }

    private void enrichWarningSignOptions(List<RiskOptionVO> options) {
        if (CollectionUtils.isEmpty(options)) {
            return;
        }
        Set<String> fileIds = new LinkedHashSet<>();
        options.forEach(option -> addFileId(fileIds, option.getIconFileId()));
        Map<String, FileVO> fileMap = fileFacadeService.batchFiles(fileIds);
        options.forEach(option -> option.setIconFile(fileMap.get(normalizeFileId(option.getIconFileId()))));
    }

    private void addFileId(Set<String> fileIds, String fileId) {
        if (StringUtils.isNotBlank(fileId)) {
            fileIds.add(normalizeFileId(fileId));
        }
    }

    private String normalizeFileId(String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return null;
        }
        try {
            return String.valueOf(Long.valueOf(fileId.trim()));
        } catch (NumberFormatException ex) {
            return fileId.trim();
        }
    }

    private void assertWarningSignCodeAvailable(String code, Long excludeId) {
        Assert.isConflict(warningSignService.count(Wrappers.<RiskWarningSignEntity>lambdaQuery()
                .eq(RiskWarningSignEntity::getSignCode, code)
                .ne(excludeId != null, RiskWarningSignEntity::getId, excludeId)) > 0, "警示标志编号已存在");
    }

    private void assertUnitTypeCodeAvailable(String code, Long excludeId) {
        Assert.isConflict(unitTypeService.count(Wrappers.<RiskUnitTypeEntity>lambdaQuery()
                .eq(RiskUnitTypeEntity::getTypeCode, code)
                .ne(excludeId != null, RiskUnitTypeEntity::getId, excludeId)) > 0, "单元类型编号已存在");
    }

    private void assertMethodCodeAvailable(String code, Long excludeId) {
        Assert.isConflict(methodService.count(Wrappers.<RiskAssessmentMethodEntity>lambdaQuery()
                .eq(RiskAssessmentMethodEntity::getMethodCode, code)
                .ne(excludeId != null, RiskAssessmentMethodEntity::getId, excludeId)) > 0, "评估方法编码已存在");
    }

    private void saveMethodChildren(Long methodId, RiskAssessmentMethodParam param) {
        List<RiskAssessmentDimensionEntity> dimensions = new ArrayList<>();
        List<RiskAssessmentOptionEntity> options = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(param.getDimensions())) {
            for (RiskAssessmentDimensionParam dimensionParam : param.getDimensions()) {
                RiskAssessmentDimensionEntity dimension = RiskConfigConvert.toDimensionEntity(methodId, dimensionParam);
                dimension.setId(IdWorker.getId());
                dimensions.add(dimension);
                if (CollectionUtils.isNotEmpty(dimensionParam.getOptions())) {
                    options.addAll(dimensionParam.getOptions().stream()
                            .map(optionParam -> RiskConfigConvert.toOptionEntity(dimension.getId(), optionParam))
                            .peek(option -> option.setId(IdWorker.getId()))
                            .toList());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(dimensions)) {
            dimensionService.saveBatch(dimensions);
        }
        if (CollectionUtils.isNotEmpty(options)) {
            optionService.saveBatch(options);
        }
        if (CollectionUtils.isNotEmpty(param.getLevelRules())) {
            List<RiskAssessmentLevelRuleEntity> rules = param.getLevelRules().stream()
                    .map(ruleParam -> RiskConfigConvert.toLevelRuleEntity(methodId, ruleParam))
                    .peek(rule -> rule.setId(IdWorker.getId()))
                    .toList();
            levelRuleService.saveBatch(rules);
        }
    }

    private boolean hasMethodChildren(RiskAssessmentMethodParam param) {
        return param.getDimensions() != null || param.getLevelRules() != null;
    }

    private void validateBasicMethodParam(RiskAssessmentMethodParam param) {
        Assert.isParameterError(StringUtils.isBlank(param.getCode()), "评估方法编码不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getName()), "评估方法名称不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getFormula()), "总分计算公式不能为空");
        Assert.isParameterError(!METHOD_FORMULA_PATTERN.matcher(param.getFormula()).matches(), "总分计算公式仅支持维度编码、数字、括号和四则运算");
    }

    private void validateMethodParam(RiskAssessmentMethodParam param) {
        validateBasicMethodParam(param);
        List<RiskAssessmentDimensionParam> dimensions = param.getDimensions() == null ? List.of() : param.getDimensions();
        Assert.isParameterError(dimensions.size() < 2, "评估方法至少配置 2 个维度");

        Set<String> dimensionCodes = new HashSet<>();
        for (RiskAssessmentDimensionParam dimension : dimensions) {
            Assert.isParameterError(StringUtils.isBlank(dimension.getCode()), "维度编码不能为空");
            Assert.isParameterError(!DIMENSION_CODE_PATTERN.matcher(dimension.getCode()).matches(), "维度编码需为 1 到 3 位大写字母");
            Assert.isParameterError(StringUtils.isBlank(dimension.getName()), "维度名称不能为空");
            Assert.isParameterError(!dimensionCodes.add(dimension.getCode()), "维度编码不能重复：" + dimension.getCode());

            List<RiskAssessmentOptionParam> options = dimension.getOptions() == null ? List.of() : dimension.getOptions();
            Assert.isParameterError(options.size() < 2, "每个评估维度至少配置 2 个选项");
            Set<BigDecimal> optionScores = new HashSet<>();
            for (RiskAssessmentOptionParam option : options) {
                Assert.isParameterError(StringUtils.isBlank(option.getName()), "维度选项名称不能为空");
                Assert.isParameterError(option.getScore() == null, "维度选项分值不能为空");
                Assert.isParameterError(!optionScores.add(option.getScore().stripTrailingZeros()),
                        "同一维度下选项分值不能重复：" + dimension.getCode());
            }
        }

        Set<String> formulaCodes = extractFormulaVariables(param.getFormula());
        Assert.isParameterError(!dimensionCodes.containsAll(formulaCodes), "公式中存在未配置的维度编码");
        Assert.isParameterError(!formulaCodes.containsAll(dimensionCodes), "配置的维度编码必须全部参与公式计算");

        List<RiskAssessmentLevelRuleParam> levelRules = param.getLevelRules() == null ? List.of() : param.getLevelRules();
        Assert.isParameterError(levelRules.size() != 4, "评估方法必须配置 4 条等级规则");
        Set<String> levelSet = levelRules.stream().map(RiskAssessmentLevelRuleParam::getRiskLevel).filter(Objects::nonNull).collect(Collectors.toSet());
        Assert.isParameterError(!Objects.equals(levelSet, REQUIRED_RISK_LEVELS), "等级规则需包含 MAJOR、LARGE、GENERAL、LOW");
        for (RiskAssessmentLevelRuleParam rule : levelRules) {
            Assert.isParameterError(StringUtils.isBlank(rule.getColor()), "等级规则颜色不能为空");
            Assert.isParameterError(rule.getMinScore() != null && rule.getMaxScore() != null
                    && rule.getMinScore().compareTo(rule.getMaxScore()) >= 0, "等级规则分值区间非法");
        }
        validateLevelRuleIntervals(levelRules);
    }

    private Set<String> extractFormulaVariables(String formula) {
        Set<String> variables = new HashSet<>();
        Matcher matcher = FORMULA_VARIABLE_PATTERN.matcher(formula);
        while (matcher.find()) {
            variables.add(matcher.group());
        }
        return variables;
    }

    private void validateLevelRuleIntervals(List<RiskAssessmentLevelRuleParam> rules) {
        for (int i = 0; i < rules.size(); i++) {
            for (int j = i + 1; j < rules.size(); j++) {
                Assert.isParameterError(isIntervalOverlap(rules.get(i), rules.get(j)), "等级规则分值区间不能重叠");
            }
        }
    }

    private boolean isIntervalOverlap(RiskAssessmentLevelRuleParam left, RiskAssessmentLevelRuleParam right) {
        BigDecimal leftMin = left.getMinScore();
        BigDecimal leftMax = left.getMaxScore();
        BigDecimal rightMin = right.getMinScore();
        BigDecimal rightMax = right.getMaxScore();
        boolean leftStartBeforeRightEnd = rightMax == null || leftMin == null || leftMin.compareTo(rightMax) < 0;
        boolean rightStartBeforeLeftEnd = leftMax == null || rightMin == null || rightMin.compareTo(leftMax) < 0;
        return leftStartBeforeRightEnd && rightStartBeforeLeftEnd;
    }

    private void softDeleteMethodChildren(Long methodId) {
        List<RiskAssessmentDimensionEntity> dimensions = dimensionService.list(Wrappers.<RiskAssessmentDimensionEntity>lambdaQuery()
                .eq(RiskAssessmentDimensionEntity::getMethodId, methodId));
        if (CollectionUtils.isNotEmpty(dimensions)) {
            List<Long> dimensionIds = dimensions.stream().map(RiskAssessmentDimensionEntity::getId).toList();
            List<RiskAssessmentOptionEntity> options = optionService.list(Wrappers.<RiskAssessmentOptionEntity>lambdaQuery()
                    .in(RiskAssessmentOptionEntity::getDimensionId, dimensionIds));
            if (CollectionUtils.isNotEmpty(options)) {
                DatabaseContextHolder.softDelete(optionService, options);
            }
            DatabaseContextHolder.softDelete(dimensionService, dimensions);
        }
        List<RiskAssessmentLevelRuleEntity> rules = levelRuleService.list(Wrappers.<RiskAssessmentLevelRuleEntity>lambdaQuery()
                .eq(RiskAssessmentLevelRuleEntity::getMethodId, methodId));
        if (CollectionUtils.isNotEmpty(rules)) {
            DatabaseContextHolder.softDelete(levelRuleService, rules);
        }
    }
}
