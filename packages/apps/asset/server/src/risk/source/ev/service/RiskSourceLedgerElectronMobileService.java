package com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryParam;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.client.ScenePointClient;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.PropertyTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository.SelectSheetWriteHandler;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedger;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedgerExtendInfo;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration.RiskSourceInsuranceEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration.RiskSourceReceiveRserTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.enumeration.ElectronMobileEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.excel.RiskSourceEvExcelTemplateParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.param.RiskSourceLedgeEvStatQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceExcelTemplateParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceLedgerExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerExtendInfoService;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 风险源电动车
 *
 * @author lanchun
 * @date 2025/3/18 10:13
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceLedgerElectronMobileService {

    private final RiskSourceLedgerService riskSourceLedgerService;

    private final RiskSourceLedgerExtendInfoService riskSourceLedgerExtendInfoService;

    private final AreaClient areaClient;

    private final OrganizationClient organizationClient;

    private final ScenePointClient scenePointClient;

    private final StallClient stallClient;

    public void getTemplateExcel(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String fileNameEncode = null;
        fileNameEncode = URLEncoder.encode("电动车导入模板", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        CascadeDictionaryParam dictionaryParam = new CascadeDictionaryParam();
        dictionaryParam.setItemCode("risk-source-type");
        // 下拉框内容
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        //电动车分类
        ElectronMobileEnum[] electronMobileEnums = ElectronMobileEnum.values();
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
        if (ObjectUtils.isNotEmpty(electronMobileEnums)) {
            List<String> typeName = new ArrayList<>();
            for (ElectronMobileEnum electronMobileEnum : electronMobileEnums) {
                typeName.add(electronMobileEnum.getDesc());
            }
            dropdownData.put(2, typeName);
        }
        if (ObjectUtils.isNotEmpty(areaList)) {
            List<String> areaNameList = areaList.stream().map(AreaDTO::getName).collect(Collectors.toList());
            dropdownData.put(7, areaNameList);
        }
        if (ObjectUtils.isNotEmpty(stallDTOList)) {
            List<String> stallList = stallDTOList.stream().map(StallDTO::getName).collect(Collectors.toList());
            dropdownData.put(8, stallList);
        }
        if (ObjectUtils.isNotEmpty(scenePointVOList)) {
            List<String> stringList = scenePointVOList.stream().map(ScenePointVO::getName).collect(Collectors.toList());
            dropdownData.put(9, stringList);
        }
        if (ObjectUtils.isNotEmpty(orgList)) {
            List<String> orgLists = orgList.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
            dropdownData.put(11, orgLists);
            dropdownData.put(13, orgLists);
        }
        List<String> receiveUserTypeList = RiskSourceReceiveRserTypeEnum.getDescribeList();
        dropdownData.put(15, receiveUserTypeList);
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new SelectSheetWriteHandler(dropdownData))
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(RiskSourceEvExcelTemplateParam.class)
                    .sheet("电动车导入模板")
                    .doWrite(new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 电动车风险源导入
     *
     * @param file
     */
    @Transactional(rollbackFor = Exception.class)
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
        List<RiskSourceEvExcelTemplateParam> datas = reader.sheet(0).head(RiskSourceEvExcelTemplateParam.class).doReadSync();
        Assert.isParameterError(CollectionUtils.isEmpty(datas), "无法添加空的excel");
        Map<String, Long> orgMap = new HashMap<>();
        Map<String, Long> areaMap = new HashMap<>();
        Map<String, Long> bimCodeMap = new HashMap<>();
        Map<String, Integer> serialNumberMap = new HashMap<>();
        Map<String, RiskSourceLedger> serialNumberIdMap = new HashMap<>();
        Map<String, Long> serialNumberDataMap = new HashMap<>();
        List<RiskSourceLedger> allData = riskSourceLedgerService.list(new LambdaQueryWrapper<>());
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
        boolean bool = false;
        for (RiskSourceEvExcelTemplateParam templateParam : datas) {
            if (ObjectUtils.isNotEmpty(templateParam.getSerialNumber())) {
                Assert.isConflict(Objects.nonNull(serialNumberDataMap.get(templateParam.getSerialNumber())),
                                  templateParam.getSerialNumber() + "风险源编码重复");
                serialNumberDataMap.put(templateParam.getSerialNumber(), 1L);
                RiskSourceExcelTemplateParam template = this.riskSourceExcelParam(templateParam);
                // 填充数据
                RiskSourceLedgerExcelParam ex = riskSourceLedgerService.riskSourceExcelParam(template, templateParam.getRemarks(), orgMap, areaMap, bimCodeMap, serialNumberMap, serialNumberIdMap);
                if (ObjectUtils.isEmpty(ex.getId())) {
                    ex.setId(IdWorker.getId());
                    riskSourceLedgerList.add(ex);
                } else {
                    updateRiskSourceLedgerList.add(ex);
                }
                RiskSourceLedgerExtendInfo riskSourceLedgerExtendInfo = ConvertUtils.convert(RiskSourceLedgerExtendInfo.class, templateParam);
                if (ObjectUtils.isNotEmpty(riskSourceLedgerExtendInfo)) {
                    riskSourceLedgerExtendInfo.setLedgerId(ex.getId());
                    riskSourceLedgerExtendInfo.setReceiveUserType(RiskSourceReceiveRserTypeEnum.getNameByDescribe(riskSourceLedgerExtendInfo.getReceiveUserType()));
                    riskSourceLedgerExtendInfo.setInsurance(RiskSourceInsuranceEnum.getContentByDescribe(riskSourceLedgerExtendInfo.getInsurance()));
                    riskSourceLedgerExtendInfo.setBrand(templateParam.getBrand());
                    riskSourceLedgerExtendInfo.setElectronMobileType(Objects
                                                                             .requireNonNull(ElectronMobileEnum.getByDesc(templateParam.getElectronMobileType()))
                                                                             .name());
                    riskSourceLedgerExtendInfo.setElectronMobileColour(templateParam.getElectronMobileColour());
                    extendInfoList.add(riskSourceLedgerExtendInfo);
                }
            }
        }
        if (ObjectUtils.isNotEmpty(updateRiskSourceLedgerList)) {
            bool = this.riskSourceLedgerService.saveOrUpdateBatch(ConvertUtils.toList(RiskSourceLedger.class, updateRiskSourceLedgerList));
            if (bool) {
                Set<Long> ids = updateRiskSourceLedgerList.stream().map(RiskSourceLedgerExcelParam::getId).collect(Collectors.toSet());
                riskSourceLedgerExtendInfoService.remove(new LambdaQueryWrapper<RiskSourceLedgerExtendInfo>().in(RiskSourceLedgerExtendInfo::getLedgerId, ids));
            }
        }
        if (!riskSourceLedgerList.isEmpty()) {
            bool = this.riskSourceLedgerService.saveBatch(ConvertUtils.toList(RiskSourceLedger.class, riskSourceLedgerList));
        }
        if (bool && ObjectUtils.isNotEmpty(extendInfoList)) {
            riskSourceLedgerExtendInfoService.saveBatch(extendInfoList);
        }
        Assert.isNotAcceptable(!bool, "数据错误，导入失败");
        log.info("导入结果：{}", bool);
    }

    /**
     * 电动车风险源总数
     *
     * @param param
     *
     * @return
     */
    public Long getElectronMobileCount(@Valid RiskSourceLedgeEvStatQueryParam param) {
        return riskSourceLedgerService.count(Wrappers.<RiskSourceLedger>lambdaQuery()
                                                     .eq(RiskSourceLedger::getTypeCode, PropertyTypeEnum.ELECTRO_MOBILE.name())
                                                     .between(Objects.nonNull(param.getPeriod()), RiskSourceLedger::getCreateTime, param
                                                             .getPeriod().getBegin(), param.getPeriod()
                                                                      .getEnd()));
    }

    /**
     * 风险源导入数据填充
     */
    public RiskSourceExcelTemplateParam riskSourceExcelParam(RiskSourceEvExcelTemplateParam templateParam) {
        RiskSourceExcelTemplateParam param = new RiskSourceExcelTemplateParam();
        param.setSerialNumber(templateParam.getSerialNumber());
        param.setName(templateParam.getName());
        param.setTypeName("电动车");
        param.setNorms(templateParam.getNorms());
        param.setAreaName(templateParam.getAreaName());
        param.setStallName(templateParam.getStallName());
        param.setSitePointName(templateParam.getSitePointName());
        param.setAttribute(templateParam.getAttribute());
        param.setMaintenanceDepName(templateParam.getMaintenanceDepName());
        param.setMaintenanceUserName(templateParam.getMaintenanceUserName());
        param.setReceiveDepName(templateParam.getReceiveDepName());
        param.setReceiveUserName(templateParam.getReceiveUserName());
        param.setReceiveUserType(templateParam.getReceiveUserType());
        param.setReceiveUserPhone(templateParam.getReceiveUserPhone());
        return param;
    }
}
