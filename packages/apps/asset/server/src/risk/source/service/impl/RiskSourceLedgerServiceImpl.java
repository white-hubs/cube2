package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.bracing.dto.request.AssemblyNoticeObjDTO;
import com.guhecloud.cube.base.dictionary.client.CascadeDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.CascadeDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryDetailParam;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryParam;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.property.management.client.property.enumeration.MessageBusinessTypeEnum;
import com.guhecloud.cube.property.management.client.property.param.property.RiskSourceDeviceQueryParam;
import com.guhecloud.cube.property.management.client.property.vo.SafetyManageCollectPropertyVO;
import com.guhecloud.cube.property.management.client.property.vo.property.RiskSourceDeviceVO;
import com.guhecloud.cube.property.management.client.risk.source.param.PartsDetailsParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerAllParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceIdTypeVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerExtendInfoVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceMessageVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceTypeNumVO;
import com.guhecloud.cube.srv.gen.maintain.client.ScenePointClient;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.FileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.PropertyTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertySourceEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository.SelectSheetWriteHandler;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao.RiskSourceLedgerExtendInfoMapper;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao.RiskSourceLedgerMapper;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao.RiskSourcePartsMapper;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedger;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedgerExtendInfo;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceParts;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration.RiskSourceInsuranceEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration.RiskSourceMessageEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration.RiskSourceReceiveRserTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration.RiskSourceStateEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceExcelTemplateParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceLedgerExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerService;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourcePartsService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  16:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceLedgerServiceImpl extends ServiceImpl<RiskSourceLedgerMapper, RiskSourceLedger> implements RiskSourceLedgerService {
    private final RiskSourcePartsService riskSourcePartsService;
    private final RiskSourcePartsMapper riskSourcePartsMapper;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;
    private final AreaClient areaClient;
    private final OrganizationClient organizationClient;
    private final AssemblyNoticeClient assemblyNoticeClient;
    private final CascadeDictionaryClient cascadeDictionaryClient;
    private final ScenePointClient scenePointClient;
    private final StallClient stallClient;
    private final PropertyDetailedMapper propertyDetailedMapper;
    private final UserClient userClient;
    private final RiskSourceLedgerMapper riskSourceLedgerMapper;
    private final RiskSourceLedgerExtendInfoMapper riskSourceLedgerExtendInfoMapper;
    private final RiskSourceLedgerExtendInfoServiceImpl extendInfoService;

    @Override
    public RiskSourceLedgerVO queryById(Long id) {
        RiskSourceLedger riskSourceLedger = this.baseMapper.selectById(id);
        Assert.isParameterError(Objects.isNull(riskSourceLedger), "风险源不存在");
        RiskSourceLedgerVO riskSourceLedgerVO = ConvertUtils.convert(RiskSourceLedgerVO.class, riskSourceLedger);
        RiskSourceLedgerExtendInfo extendInfo = this.riskSourceLedgerExtendInfoMapper.selectOne(new LambdaQueryWrapper<RiskSourceLedgerExtendInfo>()
                                                                                                        .eq(RiskSourceLedgerExtendInfo::getLedgerId, id));
        riskSourceLedgerVO.setExtendInfo(ConvertUtils.convert(RiskSourceLedgerExtendInfoVO.class, extendInfo));
        List<RiskSourceParts> riskSourcePartsList = riskSourcePartsMapper.selectList(new LambdaQueryWrapper<RiskSourceParts>()
                                                                                             .eq(RiskSourceParts::getRiskSourceLedgerId, id));
        riskSourceLedgerVO.setPartsDetails(ConvertUtils.toList(PartsDetailsParam.class, riskSourcePartsList));
        if (ObjectUtils.isNotEmpty(riskSourceLedger.getFile())) {
            List<FileVO> fileVOList = fileClient.getByBusinessKey(riskSourceLedger.getFile());
            riskSourceLedgerVO.setFiles(fileVOList);
        }
        if (ObjectUtils.isNotEmpty(riskSourceLedgerVO.getAreaId())) {
            if (ObjectUtils.isNotEmpty(riskSourceLedgerVO.getAreaId())) {
                String[] areaIds = riskSourceLedgerVO.getAreaId().split(",");
                Long areaId = Long.valueOf(areaIds[areaIds.length - 1]);
                riskSourceLedgerVO.setAreaName(areaClient.getAreaNamesById(areaId));
            }
        }
        if (ObjectUtils.isNotEmpty(riskSourceLedgerVO.getTypeCode())) {
            CascadeDictionaryDetailParam detailParam = new CascadeDictionaryDetailParam();
            detailParam.setCode(riskSourceLedgerVO.getTypeCode());
            CascadeDictionaryDTO cascadeDictionary = cascadeDictionaryClient.getCascadeDictionary(detailParam);
            if (ObjectUtils.isNotEmpty(cascadeDictionary)) {
                riskSourceLedgerVO.setTypeCode(
                        ObjectUtils.isNotEmpty(cascadeDictionary.getSuperiorsAllCode()) ? cascadeDictionary.getSuperiorsAllCode()
                                                                                        : cascadeDictionary.getCode());
                riskSourceLedgerVO.setTypeName(
                        ObjectUtils.isNotEmpty(cascadeDictionary.getSuperiorsAllName()) ? cascadeDictionary.getSuperiorsAllName()
                                                                                        : cascadeDictionary.getName());
            }
        }
        return riskSourceLedgerVO;
    }

    @Override
    public RiskSourceLedgerVO queryByBimCode(String bimCode) {
        RiskSourceLedger riskSourceLedger = this.baseMapper.selectOne(new LambdaQueryWrapper<RiskSourceLedger>()
                                                                              .eq(RiskSourceLedger::getBimCode, bimCode)
                                                                              .last("limit 1"));
        Assert.parameterIsNull(riskSourceLedger, "风险源不存在");
        return this.queryById(riskSourceLedger.getId());
    }

    @Override
    public List<RiskSourceIdTypeVO> getRiskSourceIdTypes(RiskSourceLedgerPageParam param) {
        var wrapper = new LambdaQueryWrapper<RiskSourceLedger>();
        wrapper.select(RiskSourceLedger::getId, RiskSourceLedger::getName, RiskSourceLedger::getTypeCode)
                .like(StringUtils.isNotBlank(param.getKeyword()), RiskSourceLedger::getName, param.getKeyword())
                .in(!CollectionUtils.isEmpty(param.getIds()), RiskSourceLedger::getId, param.getIds())
                .eq(ObjectUtils.isNotNull(param.getDeptId()), RiskSourceLedger::getReceiveDepId, param.getDeptId());
        var list = this.baseMapper.selectList(wrapper);
        return list.stream().map(po -> new RiskSourceIdTypeVO(po.getId(), po.getName(), po.getTypeCode(), null))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<RiskSourceLedgerVO> queryByPage(RiskSourceLedgerPageParam param) {
        Page<RiskSourceLedger> page = new Page<>(param.getPageNumber(), param.getPageSize());
        if (StringUtils.isNotBlank(param.getTypeCode())) {
            param.setTypeCodes(subTypeCodes(true, param.getTypeCode()));
            param.setTypeCode(null);
        }
        this.baseMapper.selectAllByPage(page, param);
        Pagination<RiskSourceLedgerVO> pagination = new Pagination<>(page.getTotal(), page.getCurrent(), ConvertUtils.toList(RiskSourceLedgerVO.class, page.getRecords()));
        pagination.getRecords().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                String[] areaIds = ex.getAreaId().split(",");
                Long areaId = Long.valueOf(areaIds[areaIds.length - 1]);
                ex.setAreaName(areaClient.getAreaNamesById(areaId));
            }
        });
        return pagination;
    }

    public Set<String> subTypeCodes(Boolean children, String typeCode) {
        if (StringUtils.isBlank(typeCode)) {
            return new HashSet<>();
        }
        Set<String> typeCodes = Sets.newHashSet(typeCode);
        if (children) {
            CascadeDictionaryDetailParam detailParam = new CascadeDictionaryDetailParam();
            detailParam.setCode(typeCode);
            CascadeDictionaryDTO cascadeDictionary = cascadeDictionaryClient.getCascadeDictionary(detailParam);
            if (ObjectUtils.isNotEmpty(cascadeDictionary)) {
                CascadeDictionaryParam dictionaryParam = new CascadeDictionaryParam();
                dictionaryParam.setSuperiorsId(cascadeDictionary.getId());
                List<CascadeDictionaryDTO> nextList = cascadeDictionaryClient.getCascadeDictionaryItem(dictionaryParam);
                while (ObjectUtils.isNotEmpty(nextList)) {
                    List<CascadeDictionaryDTO> cascadeCodes = new ArrayList<>();
                    nextList.forEach(dto -> {
                        typeCodes.add(dto.getCode());
                        if (ObjectUtils.isNotEmpty(dto.getNextList())) {
                            cascadeCodes.addAll(dto.getNextList());
                        }
                    });
                    nextList = cascadeCodes;
                }
            }
        }
        return typeCodes;
    }

    @Override
    public List<SafetyManageCollectPropertyVO> queryAll(RiskSourceLedgerAllParam param) {
        List<SafetyManageCollectPropertyVO> safetyManageCollectProperty = new ArrayList<>();
        CascadeDictionaryParam cascadeDictionaryParam = new CascadeDictionaryParam();
        cascadeDictionaryParam.setItemCode("risk-source-type");
        List<CascadeDictionaryDTO> cascadeDictionary = cascadeDictionaryClient.getCascadeDictionaryItem(cascadeDictionaryParam);
        for (CascadeDictionaryDTO ex : cascadeDictionary) {
            if (PropertyTypeEnum.FIRE_FIGHTING_EQUIPMENT.name().equals(ex.getCode())) {
                List<String> codes = new ArrayList<>();
                codes.add(ex.getCode());
                this.codeRecursion(codes, ex);
                this.getSafetyManageCollectPropertyVOList(param, codes, ex.getCode(), safetyManageCollectProperty);
            } else if (PropertyTypeEnum.FLAMMABLE_EQUIPMENT.name().equals(ex.getCode())) {
                List<String> codes = new ArrayList<>();
                codes.add(ex.getCode());
                this.codeRecursion(codes, ex);
                this.getSafetyManageCollectPropertyVOList(param, codes, ex.getCode(), safetyManageCollectProperty);
            } else if (PropertyTypeEnum.TRANSPORT_DEVICES.name().equals(ex.getCode())) {
                List<String> codes = new ArrayList<>();
                codes.add(ex.getCode());
                this.codeRecursion(codes, ex);
                this.getSafetyManageCollectPropertyVOList(param, codes, ex.getCode(), safetyManageCollectProperty);
            }
        }
        return safetyManageCollectProperty;
    }

    public List<SafetyManageCollectPropertyVO> queryAllByCreateTime() {
        List<SafetyManageCollectPropertyVO> safetyManageCollectProperty = new ArrayList<>();
        CascadeDictionaryParam cascadeDictionaryParam = new CascadeDictionaryParam();
        cascadeDictionaryParam.setItemCode("risk-source-type");
        List<CascadeDictionaryDTO> cascadeDictionary = cascadeDictionaryClient.getCascadeDictionaryItem(cascadeDictionaryParam);
        for (CascadeDictionaryDTO ex : cascadeDictionary) {
            if (PropertyTypeEnum.FIRE_FIGHTING_EQUIPMENT.name().equals(ex.getCode())) {
                List<String> codes = new ArrayList<>();
                codes.add(ex.getCode());
                this.codeRecursion(codes, ex);
                safetyManageCollectProperty.addAll(countPropertyVOList(codes, ex.getCode()));
            } else if (PropertyTypeEnum.FLAMMABLE_EQUIPMENT.name().equals(ex.getCode())) {
                List<String> codes = new ArrayList<>();
                codes.add(ex.getCode());
                this.codeRecursion(codes, ex);
                safetyManageCollectProperty.addAll(countPropertyVOList(codes, ex.getCode()));
            } else if (PropertyTypeEnum.TRANSPORT_DEVICES.name().equals(ex.getCode())) {
                List<String> codes = new ArrayList<>();
                codes.add(ex.getCode());
                this.codeRecursion(codes, ex);
                safetyManageCollectProperty.addAll(countPropertyVOList(codes, ex.getCode()));
            }
        }
        return safetyManageCollectProperty;
    }

    public List<SafetyManageCollectPropertyVO> countPropertyVOList(List<String> codes, String propertyType) {
        List<RiskSourceLedger> riskSourceLedgers = this.baseMapper.selectList(new LambdaQueryWrapper<RiskSourceLedger>()
                                                                                      .in(RiskSourceLedger::getTypeCode, codes)
                                                                                      .select(RiskSourceLedger::getId)
                                                                                      .last("limit 1"));
        List<SafetyManageCollectPropertyVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(riskSourceLedgers)) {
            riskSourceLedgers.forEach(ledger -> {
                vos.add(SafetyManageCollectPropertyVO.builder()
                                .propertyType(propertyType)
                                .id(ledger.getId())
                                .build());
            });
        }
        return vos;
    }

    @Override
    public Boolean insert(RiskSourceLedgerParam param) {
        // 产品设计缺陷，之前统计有的是统计的数量，有的是条数，现在统一为条数，所以默认为1
        param.setNumber(1);
        this.dataCheck(param.getBimCode());
        RiskSourceLedger riskSourceLedger = ConvertUtils.convert(RiskSourceLedger.class, param);
        String status = ObjectUtils.isNotEmpty(param.getUsageDate()) && DateUtils.compare(param.getUsageDate(), DateUtils.now()) < 1
                        ? RiskSourceStateEnum.EXPIRE.name() : RiskSourceStateEnum.NORMAL.name();
        riskSourceLedger.setStatus(status);
        if (ObjectUtils.isNotEmpty(param.getFiles())) {
            riskSourceLedger.setFile(IdUtils.next());
        }
        Boolean bool = this.baseMapper.insert(riskSourceLedger) > 0;
        Assert.isUnknownError(!bool, "新增失败");
        RiskSourceLedgerExtendInfo extendInfo = ConvertUtils.convert(RiskSourceLedgerExtendInfo.class, param);
        extendInfo.setLedgerId(riskSourceLedger.getId());
        bool = this.riskSourceLedgerExtendInfoMapper.insert(extendInfo) > 0;
        if (bool) {
            if (ObjectUtils.isNotEmpty(param.getFiles())) {
                FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
                fileRefCreateParam.setFileIdStr(param.getFiles());
                fileRefCreateParam.setBusinessKey(riskSourceLedger.getFile());
                fileRefCreateParam.setSceneCode(FileSceneCodeEnum.RISK_SOURCE.getCode());
                fileRefClient.refInduce(fileRefCreateParam);
            }
            bool = riskSourcePartsService.insertBatch(param.getPartsDetails(), riskSourceLedger.getId());
        }
        return bool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertBatch(List<RiskSourceLedger> param) {
        return this.saveBatch(param);
    }

    @Override
    public Boolean update(RiskSourceLedgerParam param) {
        // 产品设计缺陷，之前统计有的是统计的数量，有的是条数，现在统一为条数，所以默认为1
        param.setNumber(1);
        this.dataCheck(param.getId(), param.getBimCode());
        RiskSourceLedger riskSourceLedgerPO = this.baseMapper.selectById(param.getId());
        if (ObjectUtils.isNotEmpty(riskSourceLedgerPO.getFile())) {
            fileRefClient.removeByBusinessKey(riskSourceLedgerPO.getFile());
        }
        RiskSourceLedger riskSourceLedger = ConvertUtils.convert(RiskSourceLedger.class, param);
        if (ObjectUtils.isNotEmpty(param.getFiles())) {
            riskSourceLedger.setFile(IdUtils.next());
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setSceneCode(FileSceneCodeEnum.RISK_SOURCE.getCode());
            fileRefCreateParam.setFileIdStr(param.getFiles());
            fileRefCreateParam.setBusinessKey(riskSourceLedger.getFile());
            fileRefClient.refInduce(fileRefCreateParam);
        }
        String status = ObjectUtils.isNotEmpty(param.getUsageDate()) && DateUtils.compare(param.getUsageDate(), DateUtils.now()) < 1
                        ? RiskSourceStateEnum.EXPIRE.name() : RiskSourceStateEnum.NORMAL.name();
        riskSourceLedger.setStatus(status);
        riskSourceLedger.setDeleted(riskSourceLedgerPO.getDeleted());
        EntityAuditor.populateCreateAudit(riskSourceLedger, riskSourceLedgerPO.getCreateBy(), riskSourceLedgerPO.getCreatorId(),
                                          riskSourceLedgerPO.getCreatorType(), riskSourceLedgerPO.getCreateTime());
        Boolean bool = this.baseMapper.updateById(riskSourceLedger) > 0;
        Assert.isUnknownError(!bool, "修改失败");
        RiskSourceLedgerExtendInfo extendInfo = ConvertUtils.convert(RiskSourceLedgerExtendInfo.class, param);
        if (ObjectUtils.isNotEmpty(extendInfo)) {
            extendInfo.setLedgerId(param.getId());
            RiskSourceLedgerExtendInfo riskSourceLedgerExtendInfo = this.riskSourceLedgerExtendInfoMapper.selectOne(new LambdaQueryWrapper<>(RiskSourceLedgerExtendInfo.class)
                                                                                                                            .eq(RiskSourceLedgerExtendInfo::getLedgerId, param.getId()));
            if (ObjectUtils.isNotEmpty(riskSourceLedgerExtendInfo)) {
                extendInfo.setId(riskSourceLedgerExtendInfo.getId());
                this.riskSourceLedgerExtendInfoMapper.updateById(extendInfo);
            } else {
                this.riskSourceLedgerExtendInfoMapper.insert(extendInfo);
            }
        }
        return riskSourcePartsService.insertBatch(param.getPartsDetails(), param.getId());
    }

    @Override
    public Boolean updateStatus() {
        return this.update().set("status", RiskSourceStateEnum.EXPIRE.name())
                .eq("status", RiskSourceStateEnum.NORMAL.name())
                .le("usage_date", DateUtils.now()).update();
    }

    @Override
    public void deleteByIds(Set<Long> ids) {
        DatabaseContextHolder.softDelete(RiskSourceLedger.class, ids);
    }

    @Override
    public void getTemplateExcel(List<CascadeDictionaryDTO> type, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String fileNameEncode = null;
        fileNameEncode = URLEncoder.encode("风险源导入模板", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        CascadeDictionaryParam dictionaryParam = new CascadeDictionaryParam();
        dictionaryParam.setItemCode("risk-source-type");
        // 下拉框内容
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        // 风险源分类
        if (CollectionUtils.isEmpty(type)) {
            type = cascadeDictionaryClient.getAllCascadeDictionaryItem(dictionaryParam);
        }
        // 区域列表
        List<AreaDTO> areaList = areaClient.getAllAreaFullName();
        // 部门列表
        OrganizationQueryParam orgParam = new OrganizationQueryParam();
        orgParam.setEnabled(true);
        List<OrganizationDTO> orgList = organizationClient.getOrganizations(orgParam);
        // 现场点列表
        InspectScenePointQueryParam scenePointQueryParam = new InspectScenePointQueryParam();
        List<ScenePointVO> scenePointVOList = scenePointClient.scenePointListAll(scenePointQueryParam);
        // 档位列表
        List<StallDTO> stallDTOList = stallClient.getStallList();
        if (ObjectUtils.isNotEmpty(type)) {
            List<String> typeName = type.stream().map(CascadeDictionaryDTO::getName).collect(Collectors.toList());
            dropdownData.put(2, typeName);
        }
        if (ObjectUtils.isNotEmpty(areaList)) {
            List<String> areaNameList = areaList.stream().map(AreaDTO::getName).collect(Collectors.toList());
            dropdownData.put(4, areaNameList);
        }
        if (ObjectUtils.isNotEmpty(stallDTOList)) {
            List<String> stallList = stallDTOList.stream().map(StallDTO::getName).collect(Collectors.toList());
            dropdownData.put(5, stallList);
        }
        if (ObjectUtils.isNotEmpty(scenePointVOList)) {
            List<String> stringList = scenePointVOList.stream().map(ScenePointVO::getName).collect(Collectors.toList());
            dropdownData.put(6, stringList);
        }
        if (ObjectUtils.isNotEmpty(orgList)) {
            dropdownData.put(8, orgList.stream().map(OrganizationDTO::getName).collect(Collectors.toList()));
            dropdownData.put(10, orgList.stream().map(OrganizationDTO::getName).collect(Collectors.toList()));
        }
        List<String> insuranceList = RiskSourceInsuranceEnum.getDescribeList();
        dropdownData.put(23, insuranceList);
        List<String> receiveUserTypeList = RiskSourceReceiveRserTypeEnum.getDescribeList();
        dropdownData.put(24, receiveUserTypeList);
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new SelectSheetWriteHandler(dropdownData))
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(RiskSourceExcelTemplateParam.class)
                    .sheet("风险源导入模板")
                    .doWrite(new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importTemplate(MultipartFile file) {
        Assert.isParameterError(file.isEmpty(), "必须上传excel文件");
        // 创建Excel读取器并指定数据类型
        ExcelReaderBuilder reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            log.error("ouaa:导入用户读取excel失败", e);
            throw new ServiceException("文件读取失败");
        }
        // 读取第一个sheet的数据
        List<RiskSourceExcelTemplateParam> datas = reader.sheet(0).head(RiskSourceExcelTemplateParam.class).doReadSync();
        Assert.isParameterError(CollectionUtils.isEmpty(datas), "无法添加空的excel");
        Map<String, Long> orgMap = new HashMap<>();
        Map<String, Long> areaMap = new HashMap<>();
        Map<String, Long> bimCodeMap = new HashMap<>();
        Map<String, Integer> serialNumberMap = new HashMap<>();
        Map<String, RiskSourceLedger> serialNumberIdMap = new HashMap<>();
        Map<String, Long> serialNumberDataMap = new HashMap<>();
        List<RiskSourceLedger> allData = super.baseMapper.selectList(new LambdaQueryWrapper<>());
        allData.forEach(ex -> {
            serialNumberIdMap.put(ex.getSerialNumber(), ex);
            if (ObjectUtils.isNotEmpty(ex.getBimCode())) {
                bimCodeMap.put(ex.getBimCode(), ex.getId());
            }
            if (ObjectUtils.isNotEmpty(serialNumberMap.get(ex.getSerialNumber()))) {
                serialNumberMap.put(ex.getSerialNumber(), serialNumberMap.get(ex.getSerialNumber()) + 1);
            } else {
                serialNumberMap.put(ex.getSerialNumber(), 1);
            }
        });
        List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(new OrganizationQueryParam());
        organizationDTOList.forEach(ex -> {
            orgMap.put(ex.getName(), ex.getId());
        });
        List<AreaDTO> areaList = areaClient.getAllAreaFullName();
        areaList.forEach(ex -> {
            areaMap.put(ex.getName(), ex.getId());
        });
        // 风险源信息
        List<RiskSourceLedgerExcelParam> riskSourceLedgerList = new ArrayList<>();
        List<RiskSourceLedgerExcelParam> updateRiskSourceLedgerList = new ArrayList<>();
        List<RiskSourceLedgerExtendInfo> extendInfoList = new ArrayList<>();
        // 配件信息
        List<RiskSourceParts> partsList = new ArrayList<>();
        Boolean bool = false;
        int count = 1;
        for (RiskSourceExcelTemplateParam templateParam : datas) {
            count++;
            Assert.isParameterError(StringUtils.isEmpty(templateParam.getSerialNumber()) || StringUtils.isEmpty(templateParam.getName()) ||
                                    StringUtils.isEmpty(templateParam.getTypeName()), "第" + count + "行有空的必填项！");
            if (ObjectUtils.isNotEmpty(templateParam.getSerialNumber())) {
                Assert.isConflict(Objects.nonNull(serialNumberDataMap.get(templateParam.getSerialNumber())),
                                  templateParam.getSerialNumber() + "风险源编码重复");
                serialNumberDataMap.put(templateParam.getSerialNumber(), 1L);
                // 填充数据
                RiskSourceLedgerExcelParam ex = this.riskSourceExcelParam(templateParam, null, orgMap, areaMap, bimCodeMap, serialNumberMap, serialNumberIdMap);

                Assert.isNotFound(StringUtils.isEmpty(ex.getTypeCode()),
                                  templateParam.getTypeName() + "：风险源类型不存在");

                if (ObjectUtils.isEmpty(ex.getId())) {
                    ex.setId(IdUtils.next());
                    riskSourceLedgerList.add(ex);
                } else {
                    updateRiskSourceLedgerList.add(ex);
                }
                RiskSourceLedgerExtendInfo riskSourceLedgerExtendInfo = ConvertUtils.convert(RiskSourceLedgerExtendInfo.class, templateParam);
                if (ObjectUtils.isNotEmpty(riskSourceLedgerExtendInfo)) {
                    riskSourceLedgerExtendInfo.setLedgerId(ex.getId());
                    riskSourceLedgerExtendInfo.setReceiveUserType(RiskSourceReceiveRserTypeEnum.getNameByDescribe(riskSourceLedgerExtendInfo.getReceiveUserType()));
                    riskSourceLedgerExtendInfo.setInsurance(RiskSourceInsuranceEnum.getContentByDescribe(riskSourceLedgerExtendInfo.getInsurance()));
                    extendInfoList.add(riskSourceLedgerExtendInfo);
                }
                RiskSourceParts riskSourceParts = this.getRiskSourceParts(ex);
                if (ObjectUtils.isNotEmpty(riskSourceParts)) {
                    partsList.add(riskSourceParts);
                }
            }
        }
        if (ObjectUtils.isNotEmpty(updateRiskSourceLedgerList)) {
            bool = this.saveOrUpdateBatch(ConvertUtils.toList(RiskSourceLedger.class, updateRiskSourceLedgerList));
            if (bool) {
                Set<Long> ids = updateRiskSourceLedgerList.stream().map(RiskSourceLedgerExcelParam::getId).collect(Collectors.toSet());
                extendInfoService.remove(new LambdaQueryWrapper<RiskSourceLedgerExtendInfo>().in(RiskSourceLedgerExtendInfo::getLedgerId, ids));
                riskSourcePartsService.deleteBatch(ids);
            }
        }
        if (!riskSourceLedgerList.isEmpty()) {
            bool = this.saveBatch(ConvertUtils.toList(RiskSourceLedger.class, riskSourceLedgerList));
        }
        if (bool && ObjectUtils.isNotEmpty(extendInfoList)) {
            extendInfoService.saveBatch(extendInfoList);
        }
        if (bool && ObjectUtils.isNotEmpty(partsList)) {
            bool = riskSourcePartsService.insertBatch(partsList);
        }
        Assert.isNotAcceptable(!bool, "数据错误，导入失败");
        log.info("导入结果：{}", bool);
    }

    @Override
    public void overageJob() {
        List<RiskSourceLedger> tenantList = this.baseMapper.selectList(new LambdaQueryWrapper<RiskSourceLedger>()
                                                                               .groupBy(RiskSourceLedger::getTenant));
        tenantList.parallelStream().forEach(ex -> {
            SessionContextHolder.requestTenant(ex.getTenant());
            List<MessageSendConfig> conditions = this.getMsgConfigList();
            for (MessageSendConfig messageSendConfig : conditions) {
                try {
                    messageSendConfig.getNoticeObjList().forEach(objType -> {
                        if (ObjectUtils.isNotEmpty(messageSendConfig.getValue())) {
                            LocalDateTime messageDate = LocalDateTime.now();
                            if (RiskSourceMessageEnum.RISK_SOURCE_OVERDUE_ABOUT.name().equals(messageSendConfig.getSceneCode()) ||
                                RiskSourceMessageEnum.MANDATORY_TESTING_OVERDUE_ABOUT.name().equals(messageSendConfig.getSceneCode())) {
                                messageDate = DateUtils.offset(messageDate, ChronoUnit.DAYS, messageSendConfig.getValue());
                            }
                            Map<String, RiskSourceMessageVO> messageVOMap = this.getMessageData(messageDate, objType, messageSendConfig.getSceneCode());
                            this.sendMsg(messageSendConfig, messageVOMap);
                        }
                    });
                } catch (Exception e) {
                    log.error("风险源发送消息失败", e);
                }
            }
        });
    }

    /**
     * 获取消息通知配置
     */
    private List<MessageSendConfig> getMsgConfigList() {
        MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram.builder().businessType(MessageBusinessTypeEnum.RISK_SOURCE.name())
                .sceneCodes(RiskSourceMessageEnum.getNameList()).build();
        return assemblyNoticeClient.getSendMsgConfig(queryPram);
    }

    /**
     * 发送消息通知
     *
     * @param messageSendConfig 消息配置
     * @param messageVOMap      风险源数据
     */
    private void sendMsg(MessageSendConfig messageSendConfig, Map<String, RiskSourceMessageVO> messageVOMap) {
        for (String userId : messageVOMap.keySet()) {
            RiskSourceMessageVO riskSourceMessageVO = messageVOMap.get(userId);
            Map<String, String> map = new HashMap<>();
            map.put("riskSourceName", String.join(",", riskSourceMessageVO.getRiskSourceName()));
            map.put("riskSourceTotal", riskSourceMessageVO.getRiskSourceTotal().toString());
            map.put("number", messageSendConfig.getValue().toString());
            Set<Long> notifyUsers = new HashSet<>();
            notifyUsers.add(Long.parseLong(userId));
            MessageSendParam sendParam = MessageSendParam.builder()
                    .notifyId(messageSendConfig.getNotifyId())
                    .tempParams(map)
                    .notifyUsers(notifyUsers)
                    .build();
            MessageSendResponse sendResponse = assemblyNoticeClient.sendMsg(sendParam);
            log.info("风险源消息发送结果：{}", sendResponse.getMessage());
        }
    }

    /**
     * 风险源强检日期到期发送消息
     */
    private Map<String, RiskSourceMessageVO> getMessageData(LocalDateTime messageDate, AssemblyNoticeObjDTO objType, String sceneCode) {
        Map<String, RiskSourceMessageVO> messageVOMap = new HashMap<>();
        List<String> riskSourceName = new ArrayList<>();
        Page<RiskSourceLedger> page = new Page<>(1, 2);
        if (RiskSourceMessageEnum.MANDATORY_TESTING_OVERDUE_ABOUT.name().equals(sceneCode)) {
            page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<RiskSourceLedger>()
                    .eq(RiskSourceLedger::getTypeCode, objType.getObjType())
                    .isNotNull(RiskSourceLedger::getMandatoryTestingDate)
                    .eq(RiskSourceLedger::getMandatoryTestingDate, DateUtils.formatDate(messageDate)));
        } else {
            page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<RiskSourceLedger>()
                    .eq(RiskSourceLedger::getTypeCode, objType.getObjType())
                    .isNotNull(RiskSourceLedger::getUsageDate)
                    .eq(RiskSourceLedger::getUsageDate, DateUtils.formatDate(messageDate)));
        }
        page.getRecords().forEach(riskSourceLedger -> {
            riskSourceName.add(riskSourceLedger.getName());
        });
        if (ObjectUtils.isNotEmpty(page.getTotal()) && page.getTotal() > 0) {
            String[] userList = objType.getObjId().split(",");
            for (String user : userList) {
                RiskSourceMessageVO riskSourceMessageVO = messageVOMap.get(user);
                if (ObjectUtils.isNotEmpty(riskSourceMessageVO)) {
                    if (riskSourceMessageVO.getRiskSourceTotal() < 2) {
                        riskSourceMessageVO.getRiskSourceName().add(riskSourceName.getFirst());
                    }
                    riskSourceMessageVO.setRiskSourceTotal(riskSourceMessageVO.getRiskSourceTotal() + page.getTotal());
                } else {
                    riskSourceMessageVO = new RiskSourceMessageVO();
                    riskSourceMessageVO.setRiskSourceTotal(page.getTotal());
                    riskSourceMessageVO.setRiskSourceName(riskSourceName);
                    messageVOMap.put(user, riskSourceMessageVO);
                }
            }
        }
        return messageVOMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void propertyCopyRiskSource(List<String> typeNames) {
        List<PropertyDetailed> propertyDetaileds = propertyDetailedMapper.selectList(new LambdaQueryWrapper<PropertyDetailed>()
                                                                                             .eq(PropertyDetailed::getAuthority, PropertySourceEnum.DEPOSITORY.name())
                                                                                             .in(PropertyDetailed::getTypeName, typeNames));
        List<RiskSourceLedger> param = new ArrayList<>();
        int i = 1;
        for (PropertyDetailed ex : propertyDetaileds) {
            RiskSourceLedger riskSourceLedger = ConvertUtils.convert(RiskSourceLedger.class, ex);
            riskSourceLedger.setStatus(ObjectUtils.isNotEmpty(riskSourceLedger.getUsageDate()) &&
                                       DateUtils.compare(riskSourceLedger.getUsageDate(), LocalDateTime.now()) < 1
                                       ? RiskSourceStateEnum.EXPIRE.name()
                                       : RiskSourceStateEnum.NORMAL.name());
            PropertyTypeEnum typeEnum = PropertyTypeEnum.getTypeEnum(ex.getTypeName());
            if (ObjectUtils.isNotNull(typeEnum)) {
                CascadeDictionaryDetailParam detailParam = new CascadeDictionaryDetailParam();
                detailParam.setCode(typeEnum.name());
                CascadeDictionaryDTO cascadeDictionary = cascadeDictionaryClient.getCascadeDictionary(detailParam);
                if (ObjectUtils.isNotEmpty(cascadeDictionary)) {
                    riskSourceLedger.setTypeCode(typeEnum.name());
                    riskSourceLedger.setTypeName(
                            ObjectUtils.isNotEmpty(cascadeDictionary.getSuperiorsAllName()) ? cascadeDictionary.getSuperiorsAllName()
                                                                                            : cascadeDictionary.getName());
                }
            }
            if (ObjectUtils.isNotEmpty(ex.getReceiveUserId())) {
                UserDTO userDTO = userClient.getUser(ex.getReceiveUserId());
                if (ObjectUtils.isNotEmpty(userDTO)) {
                    riskSourceLedger.setReceiveUserName(userDTO.getName());
                }
            }
            param.add(riskSourceLedger);
            if (param.size() >= 300) {
                boolean bool = this.insertBatch(param);
                log.info("数据同步结果：{}", bool);
                i += 1;
                param = new ArrayList<>();
            }
        }
        if (!param.isEmpty()) {
            Boolean bool = this.insertBatch(param);
            log.info("{}:数据同步结果：{}", i, bool);
        }
    }

    @Override
    public List<RiskSourceDeviceVO> getRiskSourceDeviceNum(RiskSourceDeviceQueryParam param) {
        List<RiskSourceDeviceVO> result = new ArrayList<>();
        Map<String, Set<String>> parentToChild = new HashMap<>();
        if (CollectionUtils.isNotEmpty(param.getDeviceNameList())) {
            Set<String> deviceTypeCodeList = new HashSet<>();
            for (String string : param.getDeviceNameList()) {
                Set<String> strings = this.subTypeCodes(true, string);
                deviceTypeCodeList.addAll(strings);
                parentToChild.put(string, strings);
            }
            param.setDeviceNameList(new ArrayList<>(deviceTypeCodeList));
        }
        List<RiskSourceDeviceVO> riskSourceDeviceVOS = riskSourceLedgerMapper.selectRiskSourceDeviceNumByCode(param);

        if (CollectionUtils.isEmpty(riskSourceDeviceVOS)) {
            return result;
        }
        for (Map.Entry<String, Set<String>> entry : parentToChild.entrySet()) {
            List<RiskSourceDeviceVO> riskSourceDeviceVOS1 = riskSourceDeviceVOS.stream()
                    .filter(t -> entry.getValue().contains(t.getDeviceCode()))
                    .toList();
            long deviceNum = 0L;
            long expireNum = 0L;
            for (RiskSourceDeviceVO riskSourceDeviceVO : riskSourceDeviceVOS1) {
                deviceNum = deviceNum + (Objects.nonNull(riskSourceDeviceVO.getDeviceNum()) ? riskSourceDeviceVO.getDeviceNum() : 0L);
                expireNum = expireNum + (Objects.nonNull(riskSourceDeviceVO.getExpireNum()) ? riskSourceDeviceVO.getExpireNum() : 0L);
            }
            RiskSourceDeviceVO riskSourceDeviceVO = new RiskSourceDeviceVO();
            riskSourceDeviceVO.setDeviceCode(entry.getKey());
            riskSourceDeviceVO.setDeviceNum(deviceNum);
            riskSourceDeviceVO.setExpireNum(expireNum);
            result.add(riskSourceDeviceVO);
        }
        return result;
    }

    @Override
    public List<RiskSourceLedgerVO> queryRiskSourceLedgerWithLocation(String typeCodes, Integer size) {
        if (StringUtils.isEmpty(typeCodes)) {
            return new ArrayList<>();
        }
        List<String> typeCodeList = Arrays.stream(typeCodes.split(",")).collect(Collectors.toList());

        List<RiskSourceTypeNumVO> riskSourceTypeNumVOS = this.baseMapper.selectRiskSourceTypeNumLimitLocation(typeCodeList);
        if (CollectionUtils.isEmpty(riskSourceTypeNumVOS)) {
            return new ArrayList<>();
        }
        List<RiskSourceTypeNumVO> hasLocationRiskSource = riskSourceTypeNumVOS
                .stream().filter(t -> Objects.nonNull(t.getTypeNum()) && t.getTypeNum() > 0).toList();
        if (CollectionUtils.isEmpty(hasLocationRiskSource)) {
            return new ArrayList<>();
        }
        //平均每个类型多少个
        int avgNum = size / typeCodeList.size();
        //剩余填充数
        int count = size;
        Map<String, Integer> typeNumMap = new HashMap<>();
        //每个类型剩余个数
        Map<String, Integer> typeRemainMap = new HashMap<>();
        for (RiskSourceTypeNumVO riskSourceTypeNumVO : hasLocationRiskSource) {
            if (count == 0) {
                break;
            }
            typeNumMap.put(riskSourceTypeNumVO.getTypeCode(), riskSourceTypeNumVO.getTypeNum());
            //满足平均或大于
            if (riskSourceTypeNumVO.getTypeNum() >= avgNum) {
                if (riskSourceTypeNumVO.getTypeNum() > avgNum) {
                    typeRemainMap.put(riskSourceTypeNumVO.getTypeCode(), riskSourceTypeNumVO.getTypeNum() - avgNum);
                }
                count = count - avgNum;
            } else {
                count = count - riskSourceTypeNumVO.getTypeNum();
            }
        }
        if (count > 0 && MapUtils.isNotEmpty(typeRemainMap)) {
            for (Map.Entry<String, Integer> entry : typeRemainMap.entrySet()) {
                //满足size
                if (count == 0 || !typeNumMap.containsKey(entry.getKey())) {
                    break;
                }
                Integer integer = typeNumMap.get(entry.getKey());
                integer = integer + entry.getValue();
                typeNumMap.put(entry.getKey(), integer);
                count = count - entry.getValue();
            }
        }
        List<RiskSourceLedgerVO> riskSourceLedgerVOS = this.baseMapper.selectRiskSourceLedgerWithLocation(typeNumMap);
        riskSourceLedgerVOS.forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                String[] areaIds = ex.getAreaId().split(",");
                Long areaId = Long.valueOf(areaIds[areaIds.length - 1]);
                ex.setAreaName(areaClient.getAreaNamesById(areaId));
            }
        });
        return riskSourceLedgerVOS;
    }

    private void getSafetyManageCollectPropertyVOList(RiskSourceLedgerAllParam param, List<String> codes, String typeCode,
                                                      List<SafetyManageCollectPropertyVO> data
                                                     ) {
        List<RiskSourceLedger> riskSourceLedgers = this.baseMapper.getAll(codes, param.getPeriod());
        Map<Long, String> areaMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(riskSourceLedgers)) {
            List<Long> areaIds = riskSourceLedgers.stream().filter(t -> StringUtils.isNotBlank(t.getAreaId()))
                    .map(t -> {
                        String[] split = t.getAreaId().split(",");
                        return Long.valueOf(split[split.length - 1].trim());
                    }).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(areaIds)) {
                areaMap = Optional.ofNullable(areaClient.getByIds(areaIds))
                        .orElse(new ArrayList<>())
                        .stream().collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));

            }
        }
        Map<Long, String> finalAreaMap = areaMap;
        riskSourceLedgers.forEach(ex -> {
            SafetyManageCollectPropertyVO safetyManageCollect = ConvertUtils.convert(SafetyManageCollectPropertyVO.class, ex);
            safetyManageCollect.setPropertyName(ex.getName());
            safetyManageCollect.setPropertySubType(ex.getTypeCode());
            safetyManageCollect.setNum(Optional.ofNullable(ex.getNumber()).orElse(1));
            safetyManageCollect.setUsername(ex.getReceiveUserName());
            safetyManageCollect.setPutInTime(ex.getCreateTime());
            safetyManageCollect.setExpirationTime(ex.getUsageDate());
            safetyManageCollect.setPropertyType(typeCode);
            safetyManageCollect.setPropertySubType(ex.getTypeCode());
            safetyManageCollect.setPropertySubTypeName(ex.getTypeName());
            safetyManageCollect.setPropertySubTypeName(ex.getTypeName());
            safetyManageCollect.setBimCode(ex.getBimCode());
            safetyManageCollect.setAttribute(ex.getAttribute());
            safetyManageCollect.setStallName(ex.getStallName());
            safetyManageCollect.setSitePointName(ex.getSitePointName());
            safetyManageCollect.setAddress(ex.getAddress());
            // 区域
            if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                    String[] areaIds = ex.getAreaId().split(",");
                    Long areaId = Long.valueOf(areaIds[areaIds.length - 1].trim());
                    safetyManageCollect.setAreaName(finalAreaMap.get(areaId));
                }
            }
            data.add(safetyManageCollect);
        });
    }

    private void codeRecursion(List<String> codes, CascadeDictionaryDTO cascadeDictionary) {
        for (CascadeDictionaryDTO ex : cascadeDictionary.getNextList()) {
            codes.add(ex.getCode());
            this.codeRecursion(codes, ex);
        }
    }

    private RiskSourceParts getRiskSourceParts(RiskSourceLedgerExcelParam ex) {
        RiskSourceParts riskSourceParts = null;
        if (ObjectUtils.isNotEmpty(ex.getPartsCode())
            || ObjectUtils.isNotEmpty(ex.getPartsName()) || ObjectUtils.isNotEmpty(ex.getPartsExpireDate())
            || ObjectUtils.isNotEmpty(ex.getPartsRemarks())) {
            riskSourceParts = new RiskSourceParts();
            riskSourceParts.setRiskSourceLedgerId(ex.getId());
            riskSourceParts.setCode(ex.getPartsCode());
            riskSourceParts.setName(ex.getPartsName());
            riskSourceParts.setExpireDate(ex.getPartsExpireDate());
            riskSourceParts.setRemarks(ex.getPartsRemarks());
        }
        return riskSourceParts;
    }

    /**
     * 校验模型编码是否重复
     */
    private void dataCheck(String bimCode) {
        if (ObjectUtils.isNotEmpty(bimCode)) {
            RiskSourceLedger riskSourceLedger = this.baseMapper.selectOne(new LambdaQueryWrapper<RiskSourceLedger>()
                                                                                  .eq(RiskSourceLedger::getBimCode, bimCode)
                                                                                  .last("limit 1"));
            Assert.isConflict(Objects.nonNull(riskSourceLedger), bimCode + "模型编码已存在");
        }
    }

    public void dataCheck(Long id, String bimCode) {
        if (ObjectUtils.isNotEmpty(bimCode)) {
            List<RiskSourceLedger> riskSourceLedger = this.baseMapper.selectList(new LambdaQueryWrapper<RiskSourceLedger>()
                                                                                         .eq(RiskSourceLedger::getBimCode, bimCode)
                                                                                         .last("limit 1"));
            if (ObjectUtils.isNotEmpty(riskSourceLedger)) {
                Assert.isConflict(riskSourceLedger.size() > 1, "模型编码已存在");
                if (ObjectUtils.isNotEmpty(id)) {
                    Assert.isConflict(!id.equals(riskSourceLedger.getFirst().getId()), "模型编码已存在");
                }
            }
        }
    }

    /**
     * 风险源导入数据填充
     */
    public RiskSourceLedgerExcelParam riskSourceExcelParam(RiskSourceExcelTemplateParam templateParam,
                                                           String remark,
                                                           Map<String, Long> orgMap,
                                                           Map<String, Long> areaMap, Map<String, Long> bimCodeMap,
                                                           Map<String, Integer> serialNumberMap,
                                                           Map<String, RiskSourceLedger> serialNumberIdMap
                                                          ) {
        if (ObjectUtils.isNotEmpty(templateParam.getBimCode())) {
            Assert.isConflict(Objects.nonNull(bimCodeMap.get(templateParam.getBimCode())), templateParam.getBimCode() + "模型编码已存在");
        }
        Integer total = serialNumberMap.get(templateParam.getSerialNumber());
        Assert.isParameterError(ObjectUtils.isNotEmpty(total) && total > 1, templateParam.getSerialNumber() + "风险源存在多条数据");
        RiskSourceLedgerExcelParam ex = ConvertUtils.convert(RiskSourceLedgerExcelParam.class, templateParam);
        ex.setRemarks(remark);
        RiskSourceLedger riskSourceLedger = serialNumberIdMap.get(templateParam.getSerialNumber());
        if (ObjectUtils.isNotEmpty(riskSourceLedger)) {
            ex.setId(riskSourceLedger.getId());
            ex.setDeleted(riskSourceLedger.getDeleted());
            EntityAuditor.populateCreateAudit(ex, riskSourceLedger.getCreateBy(), riskSourceLedger.getCreatorId(),
                                              riskSourceLedger.getCreatorType(), riskSourceLedger.getCreateTime());
        }
        ex.setNumber(1);
        String status = ObjectUtils.isNotEmpty(ex.getUsageDate()) && DateUtils.compare(ex.getUsageDate(), LocalDateTime.now()) < 1
                        ? RiskSourceStateEnum.EXPIRE.name() : RiskSourceStateEnum.NORMAL.name();
        ex.setStatus(status);
        Long areaId = areaMap.get(ex.getAreaName());
        if (ObjectUtils.isNotEmpty(areaId)) {
            ex.setAreaId(areaId.toString());
            List<Long> ids = areaClient.getParentIds(areaId);
            ex.setAreaId(ids.stream().map(Object::toString).collect(Collectors.joining(",")));
        }
        Long maintenanceDepId = orgMap.get(ex.getMaintenanceDepName());
        if (ObjectUtils.isNotEmpty(maintenanceDepId)) {
            ex.setMaintenanceDepId(maintenanceDepId);
        } else {
            ex.setMaintenanceDepName(null);
        }
        Long receiveDepId = orgMap.get(ex.getReceiveDepName());
        if (ObjectUtils.isNotEmpty(receiveDepId)) {
            ex.setReceiveDepId(receiveDepId);
        } else {
            ex.setReceiveDepName(null);
        }
        if (ObjectUtils.isNotEmpty(ex.getStallName())) {
            StallDTO stallDTO = stallClient.getStallByName(ex.getStallName());
            ex.setStallId(Optional.ofNullable(stallDTO).orElse(new StallDTO()).getId());
        }
        if (ObjectUtils.isNotEmpty(ex.getSitePointName())) {
            ScenePointVO scenePointVO = scenePointClient.scenePointDetailByName(ex.getSitePointName());
            ex.setSitePointId(Optional.ofNullable(scenePointVO).orElse(new ScenePointVO()).getId());
        }
        if (ObjectUtils.isNotEmpty(ex.getTypeName())) {
            CascadeDictionaryDetailParam dictionaryDetailParam = new CascadeDictionaryDetailParam();
            dictionaryDetailParam.setName(ex.getTypeName());
            CascadeDictionaryDTO cascadeDictionaryDTO = cascadeDictionaryClient.getCascadeDictionary(dictionaryDetailParam);
            if (ObjectUtils.isNotEmpty(cascadeDictionaryDTO)) {
                ex.setTypeCode(cascadeDictionaryDTO.getCode());
                ex.setTypeName(
                        ObjectUtils.isNotEmpty(cascadeDictionaryDTO.getSuperiorsAllName()) ? cascadeDictionaryDTO.getSuperiorsAllName()
                                                                                           : cascadeDictionaryDTO.getName());
            } else {
                ex.setTypeName(null);
            }
        }
        return ex;
    }

    @Override
    public List<RiskSourceLedgerVO> queryRiskSourceLedgerBatch(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        List<RiskSourceLedger> riskSourceLedgerList = this.baseMapper.selectByIds(idList);
        if (CollectionUtils.isEmpty(riskSourceLedgerList)) {
            return Collections.emptyList();
        }
        Map<Long, RiskSourceLedgerExtendInfoVO> riskSourceLedgerExtendInfoVOMap =
                Optional.ofNullable(this.riskSourceLedgerExtendInfoMapper.selectList(new LambdaQueryWrapper<RiskSourceLedgerExtendInfo>()
                                                                                             .in(RiskSourceLedgerExtendInfo::getLedgerId, idList)))
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(riskSourceLedgerExtendInfo -> ConvertUtils.convert(RiskSourceLedgerExtendInfoVO.class, riskSourceLedgerExtendInfo))
                        .collect(Collectors.toMap(RiskSourceLedgerExtendInfoVO::getLedgerId, Function.identity()));
        Map<Long, List<RiskSourceParts>> riskSourcePartMap = Optional
                .ofNullable(riskSourcePartsMapper.selectList(new LambdaQueryWrapper<RiskSourceParts>()
                                                                     .in(RiskSourceParts::getRiskSourceLedgerId, idList)))
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.groupingBy(RiskSourceParts::getRiskSourceLedgerId));
        Set<Long> areaIds = new HashSet<>();
        Set<String> typeCodes = new HashSet<>();
        for (RiskSourceLedger riskSourceLedger : riskSourceLedgerList) {

            if (StringUtils.isNotBlank(riskSourceLedger.getAreaId())) {
                areaIds.addAll(StringUtils.splitAndTranslate(riskSourceLedger.getAreaId(), Long::valueOf));
            }
            if (StringUtils.isNotBlank(riskSourceLedger.getTypeCode())) {
                typeCodes.add(riskSourceLedger.getTypeCode());
            }
        }
        Map<Long, AreaDTO> areaMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(areaIds)) {
            areaMap = Optional.ofNullable(areaClient.getByIds(areaIds)).orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(AreaDTO::getId, Function.identity()));
        }
        Map<String, CascadeDictionaryDTO> dictionaryDTOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(typeCodes)) {
            CascadeDictionaryParam detailParam = new CascadeDictionaryParam();
            detailParam.setCodes(typeCodes);
            dictionaryDTOMap = Optional.ofNullable(cascadeDictionaryClient.getCascadeDictionaryItem(detailParam))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(CascadeDictionaryDTO::getCode, Function.identity()));
        }
        List<RiskSourceLedgerVO> riskSourceLedgerVOList = new ArrayList<>();
        for (RiskSourceLedger riskSourceLedger : riskSourceLedgerList) {
            RiskSourceLedgerVO riskSourceLedgerVO = ConvertUtils.convert(RiskSourceLedgerVO.class, riskSourceLedger);
            if (StringUtils.isNotBlank(riskSourceLedger.getAreaId())) {
                Map<Long, AreaDTO> finalAreaMap = areaMap;
                String areaName = StringUtils.splitAndTranslate(riskSourceLedger.getAreaId(), Long::valueOf)
                        .stream().map(t -> finalAreaMap.getOrDefault(t, new AreaDTO()).getName())
                        .collect(Collectors.joining("/"));
                riskSourceLedgerVO.setAreaName(areaName);
            }
            if (StringUtils.isNotBlank(riskSourceLedger.getTypeCode()) && dictionaryDTOMap.containsKey(riskSourceLedger.getTypeCode())) {
                CascadeDictionaryDTO cascadeDictionary = dictionaryDTOMap.get(riskSourceLedger.getTypeCode());
                riskSourceLedgerVO.setTypeCode(
                        ObjectUtils.isNotEmpty(cascadeDictionary.getSuperiorsAllCode()) ? cascadeDictionary.getSuperiorsAllCode()
                                                                                        : cascadeDictionary.getCode());
                riskSourceLedgerVO.setTypeName(
                        ObjectUtils.isNotEmpty(cascadeDictionary.getSuperiorsAllName()) ? cascadeDictionary.getSuperiorsAllName()
                                                                                        : cascadeDictionary.getName());
            }
            riskSourceLedgerVO.setExtendInfo(riskSourceLedgerExtendInfoVOMap.get(riskSourceLedger.getId()));
            if (riskSourcePartMap.containsKey(riskSourceLedger.getId())) {
                riskSourceLedgerVO.setPartsDetails(ConvertUtils.toList(PartsDetailsParam.class, riskSourcePartMap.get(riskSourceLedger.getId())));
            }
            riskSourceLedgerVOList.add(riskSourceLedgerVO);
        }
        return riskSourceLedgerVOList;

    }

    /**
     * 查询档位和现场点关联的风险源
     * 只实现了类型、状态、档位id集合、现场点集合的条件
     * 需要其他条件自己再加
     *
     * @param param 条件
     *
     * @return 集合
     */
    @Override
    public List<RiskSourceLedgerVO> riskSourceLedgerVOList(RiskSourceLedgerPageParam param) {
        List<RiskSourceLedger> list = this.baseMapper.selectList(Wrappers.<RiskSourceLedger>lambdaQuery()
                                                                         .eq(StringUtils.isNotBlank(param.getTypeCode()), RiskSourceLedger::getTypeCode, param.getTypeCode())
                                                                         .eq(StringUtils.isNotBlank(param.getStatus()), RiskSourceLedger::getStatus, param.getStatus())
                                                                         .in(CollectionUtils.isNotEmpty(param.getStallIdList()), RiskSourceLedger::getStallId, param.getStallIdList())
                                                                         .in(CollectionUtils.isNotEmpty(param.getPrintIdList()), RiskSourceLedger::getSitePointId, param.getPrintIdList())
                                                                );

        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(po -> {
                RiskSourceLedgerVO vo = ObjectUtils.copying(po, RiskSourceLedgerVO.class);
                if (StringUtils.isNotBlank(po.getAreaId())) {
                    String[] areaIds = po.getAreaId().split(",");
                    Long areaId = Long.valueOf(areaIds[areaIds.length - 1]);
                    vo.setAreaName(areaClient.getAreaNamesById(areaId));
                }
                return vo;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
