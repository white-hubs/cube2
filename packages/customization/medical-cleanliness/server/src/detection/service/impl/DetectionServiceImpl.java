package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.param.TenantQueryParam;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DevicePropHistoryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.dto.DetectionReportSourceDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.dto.ReportStampInfoDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Basic;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Company;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Property;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.ReportCompany;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.ReportRecord;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Standard;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.CompanyCommandParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.CompanyQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionBasicParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionReportCommandParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionReportQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.StandardTreeQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionReportCompanyMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionReportRecordMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionStandardMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionBasicService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionCompanyService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionPropertyService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionReportRecordService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionStandardService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.CompanyVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionBasicDetailVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionBasicVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionReportVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.PropertyVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.ReportRecordDetailVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.StandardTreeVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.PinYinUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetectionServiceImpl implements IDetectionService {
    private static final String FILE_CODE = "MEDICAL_CLEANLINESS";
    private final IDetectionBasicService basicService;
    private final IDetectionPropertyService propertyService;
    private final IDetectionStandardService standardService;
    private final TenantClient tenantClient;
    private final IDetectionCompanyService companyService;
    private final IDetectionReportRecordService reportRecordService;
    private final FileClient fileClient;
    private final FileOperateClient fileOperateClient;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final DetectionStandardMapper detectionStandardMapper;
    private final IotDevicePropClient iotDevicePropClient;
    private final IotProductClient iotProductClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final DetectionReportRecordMapper detectionReportRecordMapper;
    private final DetectionReportCompanyMapper detectionReportCompanyMapper;

    @Transactional
    @Override
    public void insertDetectionBasic(@NonNull DetectionBasicParam basicParam, long basicId) {

        Map<String, Long> standardIdsMap = MapUtils.listToMap(basicParam.getStandards(), DetectionBasicParam.StandardParam::getName, IdWorker::getId);
        //新增依据
        insertBasic(basicParam.getBasicName(), basicId);
        log.info("检测依据新增成功，依据id：{}", basicId);
        //新增标准
        insertStandards(basicParam.getStandards(), basicId, standardIdsMap);
        log.info("检测标准新增成功，标准idMap：{}", standardIdsMap);
        //新增属性
        insertProperties(basicParam.getStandards(), basicId, standardIdsMap);
        log.info("检测属性新增成功");
    }

    @Transactional
    @Override
    public void deleteDetectionBasic(long id) {
        //删除检测依据
        deleteBasic(id);
        log.info("删除检测依据成功");
        //删除检测标准
        deleteStandards(id);
        log.info("删除检测标准成功");
        //删除检测属性
        deleteProperties(id);
        log.info("删除检测属性成功");
    }

    @Transactional
    @Override
    public void updateDetectionBasic(long basicId, DetectionBasicParam basicParam) {
        //根据basicId查询依据
        updateBasic(basicId, basicParam);
        log.info("依据修改成功");
        updateStandard(basicId, basicParam);
        log.info("标准修改成功");
        updateProperty(basicId, basicParam);
        log.info("属性修改成功");

    }

    @Override
    public Pagination<DetectionBasicVO> detectionBasicPage(Integer pageNum, Integer pageSize, String keyword) {
        Page<Basic> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<Basic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Basic::getDeleted, BigInteger.ZERO);
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like(Basic::getName, keyword);
        }
        Page<Basic> basicPage = basicService.page(page, queryWrapper);
        Pagination<DetectionBasicVO> basicVoPagination = PagingUtils.convert(basicPage, DetectionBasicVO.class);
        //设置依据对应属性个数
        setBasicVoPropertyNum(basicVoPagination.getRecords());
        //设置依据标准信息
        setBasicVoStandardLevelNames(basicVoPagination.getRecords());
        return basicVoPagination;
    }

    private void setBasicVoStandardLevelNames(List<DetectionBasicVO> detectionBasicList) {
        if (ListUtils.isEmpty(detectionBasicList)) {
            return;
        }
        List<Long> basicIds = ListUtils.convertItem(detectionBasicList, DetectionBasicVO::getId);
        //根据依据id查询各个依据的标准
        Map<Long, List<Standard>> standardMaps = standardService.getMapByBasicIds(basicIds);

        detectionBasicList.forEach(basicVo -> {
            List<Standard> standards = standardMaps.getOrDefault(basicVo.getId(), Collections.emptyList());
            basicVo.setStandardLevelNames(standards.stream().map(Standard::getName).collect(Collectors.joining(",")));
        });
    }

    @Override
    public DetectionBasicDetailVO getDetectionBasicById(long id) {
        Basic basic = basicService.getOne(new LambdaQueryWrapper<>(Basic.class).eq(Basic::getId, id)
                                                  .eq(Basic::getDeleted, BigInteger.ZERO));
        DetectionBasicDetailVO detectionBasicVo = ObjectUtils.copying(basic, DetectionBasicDetailVO.class);
        //设置依据标准信息
        setBasicVoStandards(detectionBasicVo);
        //设置依据属性信息
        setBasicVoPropertyInfo(detectionBasicVo);
        return detectionBasicVo;
    }

    private void setBasicVoPropertyInfo(DetectionBasicDetailVO detectionBasicVo) {
        List<Property> propertyList = propertyService.list(new LambdaQueryWrapper<Property>()
                                                                   .eq(Property::getBasicId, detectionBasicVo.getId())
                                                                   .eq(Property::getDeleted, BigInteger.ZERO));
        //根据standardId进行分组
        Map<Long, List<Property>> groupPropertyByStandardId = MapUtils.listGroupToMap(propertyList, Property::getStandardId);
        List<DetectionBasicDetailVO.StandardVO> standards = detectionBasicVo.getStandards();
        standards.forEach(standardVO -> {
            List<Property> properties = groupPropertyByStandardId.getOrDefault(standardVO.getId(), Collections.emptyList());
            List<DetectionBasicDetailVO.PropertyVO> propertyVOList = ObjectUtils.copying(properties, DetectionBasicDetailVO.PropertyVO.class);
            standardVO.setProperties(propertyVOList);
        });
    }

    private void setBasicVoStandards(DetectionBasicDetailVO detectionBasicVo) {

        //查询依据标准
        List<Standard> standardList = standardService.list(new LambdaQueryWrapper<Standard>()
                                                                   .eq(Standard::getBasicId, detectionBasicVo.getId())
                                                                   .eq(Standard::getDeleted, BigInteger.ZERO));
        //依据id进行分组g
        List<DetectionBasicDetailVO.StandardVO> voList = ObjectUtils.copying(standardList, DetectionBasicDetailVO.StandardVO.class);
        detectionBasicVo.setStandards(voList);

    }

    private void setBasicVoPropertyNum(List<DetectionBasicVO> records) {
        if (ListUtils.isEmpty(records)) {
            return;
        }
        List<Long> basicIds = ListUtils.convertItem(records, DetectionBasicVO::getId);
        List<Property> propertyList = propertyService.list(new LambdaQueryWrapper<Property>().in(Property::getBasicId, basicIds)
                                                                   .eq(Property::getDeleted, BigInteger.ZERO));
        if (ListUtils.isEmpty(propertyList)) {
            return;
        }
        Map<Long, List<Property>> propertyMap = MapUtils.listGroupToMap(propertyList, Property::getBasicId);
        records.forEach(record -> {
            Long basicId = record.getId();
            record.setPropertyNum(propertyMap.getOrDefault(basicId, Collections.emptyList()).size());
        });

    }

    /**
     * 获取受检公司分页列表
     *
     * @param queryParam 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<CompanyVO> getCompanyPage(CompanyQueryParam queryParam) {
        /* 获取租户分页列表 */
        TenantQueryParam tenantQueryParam = new TenantQueryParam();
        tenantQueryParam.setCode(queryParam.getTenantId());
        tenantQueryParam.setPageNumber(queryParam.getPageNumber());
        tenantQueryParam.setPageSize(queryParam.getPageSize());
        Pagination<TenantDTO> tenantPage = tenantClient.pageTenants(tenantQueryParam);
        List<TenantDTO> tenantDTOList = tenantPage.getRecords();
        if (CollectionUtils.isEmpty(tenantDTOList)) {
            return Pagination.empty();
        }
        /* 获取公司信息 */
        Set<Integer> tenantSet = tenantDTOList.stream().map(TenantDTO::getCode).collect(Collectors.toSet());
        List<Company> companyList = companyService.lambdaQuery().in(Company::getTenantId, tenantSet).list();
        Map<Integer, Company> companyMap = new HashMap<>();
        Optional.ofNullable(companyList).orElse(new ArrayList<>()).forEach(company -> companyMap.put(company.getTenantId(), company));


        /* 组装 */
        List<CompanyVO> companyVOList = tenantDTOList.stream().map(tenantDTO -> {
            Company company = Optional.ofNullable(companyMap.get(tenantDTO.getCode())).orElse(new Company());
            CompanyVO vo = ObjectUtils.copying(company, CompanyVO.class);
            vo.setTenantId(tenantDTO.getCode());
            vo.setTenantName(tenantDTO.getName());
            LocalDateTime calibrationExpireTime = company.getCalibrationExpireTime();
            if (calibrationExpireTime != null) {
                vo.setCalibrationExpireDay((int) ChronoUnit.DAYS.between(ZonedDateTime.now().toLocalDateTime(), calibrationExpireTime));
            }
            vo.setTemplateContent(null);
            return vo;
        }).collect(Collectors.toList());
        return PagingUtils.convert(tenantPage, companyVOList);
    }

    /**
     * 检测公司详情
     *
     * @param tenant 租户
     *
     * @return 详情
     */
    @Override
    public CompanyVO getCompanyDetail(Integer tenant) {
        TenantDTO tenantDTO = tenantClient.getByCode(tenant);
        Assert.isNotFound(tenantDTO, "受检单位不存在，请检查是否被删除");

        Company company = companyService.lambdaQuery().eq(Company::getTenantId, tenant).last(" limit 1").one();
        CompanyVO vo = company != null ? ObjectUtils.copying(company, CompanyVO.class) : new CompanyVO();
        vo.setTenantId(tenant);
        vo.setTenantName(tenantDTO.getName());
        vo.setFileVO(vo.getDetectionStampFileId() != null ? fileClient.getById(vo.getDetectionStampFileId()) : null);
        LocalDateTime calibrationExpireTime = vo.getCalibrationExpireTime();
        if (calibrationExpireTime != null) {
            vo.setCalibrationExpireDay((int) ChronoUnit.DAYS.between(ZonedDateTime.now().toLocalDateTime(), calibrationExpireTime));
        }
        return vo;
    }

    /**
     * 编辑检测公司
     *
     * @param commandParam 入参
     *
     * @return 租户id
     */
    @Override
    public Integer editCompany(@RequestBody CompanyCommandParam commandParam) {
        Assert.isNotFound(commandParam.getTenantId());
        Assert.isNotFound(commandParam.getDetectionStampFileId());
        Assert.isParameterError(
                commandParam.getCalibrationExpireDay() == null || commandParam.getCalibrationExpireDay() < 1, "校准天数必须大于0");
        Company company = companyService.lambdaQuery().eq(Company::getTenantId, commandParam.getTenantId()).last(" limit 1").one();
        if (company != null) {
            // 这里要先处理文件再处理编辑信息
            if (company.getDetectionStampFileId() != null &&
                !company.getDetectionStampFileId().equals(commandParam.getDetectionStampFileId())) {
                fileClient.remove(company.getDetectionStampFileId());
                this.companyFileInduce(commandParam.getDetectionStampFileId());
            }
            // 处理编辑信息
            this.setCompanyUpdateField(company, commandParam);
        } else {
            company = new Company();
            ObjectUtils.copying(commandParam, company);
            company.setId(IdWorker.getId());
            EntityAuditor.populateAudit(company);
            company.setCalibrationExpireTime(this.dayToTime(commandParam.getCalibrationExpireDay()));
            // 处理文件
            this.companyFileInduce(commandParam.getDetectionStampFileId());
        }
        companyService.saveOrUpdate(company);

        return company.getTenantId();
    }

    /**
     * 清除检测公司信息
     *
     * @param tenantId 租户id
     *
     * @return 是否成功
     */
    @Override
    public Boolean cleanCompany(Integer tenantId) {
        if (tenantId != null) {
            companyService.remove(Wrappers.<Company>lambdaQuery().eq(Company::getTenantId, tenantId));
        }
        return true;
    }

    /**
     * 获取剩余校验天数
     *
     * @return 是否合格
     */
    @Override
    public Integer getCountdown() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Company company = companyService.lambdaQuery().eq(Company::getTenantId, tenant).last("limit 1").one();
        LocalDateTime calibrationExpireTime = Optional.ofNullable(company).orElse(new Company()).getCalibrationExpireTime();
        if (calibrationExpireTime != null) {
            return (int) ChronoUnit.DAYS.between(ZonedDateTime.now().toLocalDateTime(), calibrationExpireTime);
        }
        return 0;
    }

    /**
     * 获取标准数形集合
     *
     * @param param 产品id集合
     *
     * @return 标准数形集合
     */
    @Override
    public List<StandardTreeVO> getStandardTree(StandardTreeQueryParam param) {
        Set<String> propSet = new HashSet<>();
        if (param != null && CollectionUtils.isNotEmpty(param.getProductIdList())) {
            List<String> stringList = param.getProductIdList().stream().filter(Objects::nonNull).distinct()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            List<ProductDTO> productDTOList = iotProductClient.prodListDetail(stringList);
            Optional.ofNullable(productDTOList).orElse(new ArrayList<>()).forEach(dto -> {
                List<ProductPropDTO> productPropDTOList = dto.getProductPropVoList();
                Optional.ofNullable(productPropDTOList).orElse(new ArrayList<>()).forEach(p -> {
                    propSet.add(p.getPropCode());
                });
            });
            if (CollectionUtils.isEmpty(propSet)) {
                return Collections.emptyList();
            }
        }
        return detectionStandardMapper.getStandardTreeList(new ArrayList<>(propSet));
    }

    /**
     * 检测报告记录分页列表
     *
     * @param param 分页条件
     *
     * @return 报告记录
     */
    @Override
    public Pagination<DetectionReportVO> getReportPage(DetectionReportQueryParam param) {
        Page<ReportRecord> page = reportRecordService.page(PagingUtils.convert(param), Wrappers.<ReportRecord>lambdaQuery()
                .orderByDesc(ReportRecord::getCreateTime));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page, DetectionReportVO.class);

    }

    /**
     * 报告详情
     *
     * @param id 主键id
     *
     * @return 详情
     */
    @Override
    public ReportRecordDetailVO getReportDetail(Long id) {
        ReportRecord reportRecord = reportRecordService.getById(id);
        Assert.isNotFound(reportRecord);
        return this.getReportToVO(reportRecord);
    }

    /**
     * admin端检测报告记录分页列表
     *
     * @param param 分页条件
     *
     * @return 报告记录
     */
    @Override
    public Pagination<DetectionReportVO> getAdminReportPage(DetectionReportQueryParam param) {
        Page<DetectionReportVO> page = detectionReportRecordMapper.getAdminReportPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(page);
    }

    /**
     * admin端报告详情
     *
     * @param id 主键id
     *
     * @return 详情
     */
    @Override
    public ReportRecordDetailVO getAdminReportDetail(Long id) {
        ReportRecord reportRecord = detectionReportRecordMapper.getAdminReportDetail(id);
        return this.getReportToVO(reportRecord);
    }

    /**
     * 生成报告
     *
     * @param commandParam 报告入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReportRecordDetailVO generateReport(DetectionReportCommandParam commandParam) {
        Integer countdown = this.getCountdown();
        Assert.isNotAcceptable(countdown <= 1, "设备未校准请校准后生成报告");
        Long id = IdWorker.getId();
        List<StandardTreeVO> standardTreeList = commandParam.getList();
        Assert.isNotFound(standardTreeList);
        Set<Long> basicIdSet = new HashSet<>();
        Map<String, PropertyVO> propertyVOMap = new HashMap<>();
        /* 导出源数据使用 */
        DetectionReportSourceDTO reportSourceDTO = new DetectionReportSourceDTO();
        List<Map<String, String>> header = new ArrayList<>();
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("name", "检测点");
        Map<String, String> timeMap = new HashMap<>();
        nameMap.put("time", "检测时间");
        header.add(nameMap);
        header.add(timeMap);
        Map<String, String> propNameMap = new HashMap<>();
        List<Map<String, String>> sourceDataList = new ArrayList<>();
        // 设备采样信息集合
        List<ReportStampInfoDTO> reportStampInfoList = new ArrayList<>();
        // 所有属性code的Set
        Set<String> allPropCodeSet = new HashSet<>();
        // 检测项目
        StringBuilder propNames = new StringBuilder();
        // 设备区域
        StringBuilder deviceAreas = new StringBuilder();
        // 检测依据
        StringBuilder basics = new StringBuilder();
        // 检测依据名称
        String basicNames = "";
        // 结果评价
        StringBuilder resultAppraise = new StringBuilder("自动判定：本次实时动态检测的");

        standardTreeList.forEach(standard -> {
            basicIdSet.add(standard.getBasicId());
            if (CollectionUtils.isNotEmpty(standard.getPropertyList())) {
                standard.getPropertyList().forEach(p -> {
                    Map<String, String> map = new HashMap<>();
                    map.put(p.getProductPropertyCode(), p.getProductPropertyName());
                    header.add(map);
                    propNames.append(p.getProductPropertyName()).append("，");
                    allPropCodeSet.add(p.getProductPropertyCode());
                    propertyVOMap.put(p.getProductPropertyCode(), p);
                    propNameMap.put(p.getProductPropertyCode(), p.getProductPropertyName());
                });
            }
        });
        String propName = StringUtils.isNotBlank(propNames) ? propNames.deleteCharAt(propNames.length() - 1).toString() : "";
        reportSourceDTO.setHeader(header);
        List<Basic> basicList = basicService.lambdaQuery().eq(Basic::getDeleted, 0L).in(Basic::getId, basicIdSet).list();
        basicList.forEach(b -> basics.append(b.getName()).append(","));

        if (StringUtils.isNotBlank(basics)) {
            basicNames = basics.deleteCharAt(basics.length() - 1).toString();
        }
        resultAppraise.append(propName).append("符合").append(basicNames).append("要求");

        List<DeviceBaseDetailsDTO> deviceBaseDetailsDTOList = deviceCenterStdClient.getDetailsListByDeviceId(new HashSet<>(commandParam.getDeviceIdList()));
        Assert.isParameterError(ListUtils.isEmpty(deviceBaseDetailsDTOList), "设备不存在，请检查所选设备是否正常");
        /* 处理每个设备的读数数据 */
        deviceBaseDetailsDTOList.forEach(deviceDTO -> {
            deviceAreas.append(deviceDTO.getAreaName()).append(deviceDTO.getDeviceName()).append(",");
            this.handlerDevicesData(deviceDTO, propNameMap, sourceDataList, reportStampInfoList, propertyVOMap, allPropCodeSet,
                                    DateUtils.toTimestamp(commandParam.getPeriod().getBegin()), DateUtils.toTimestamp(commandParam
                                                                                                                              .getPeriod()
                                                                                                                              .getEnd()));
        });
        sourceDataList.sort(Comparator.comparing((Map<String, String> m) -> m.get("time"))
                                    .thenComparing(m -> m.get("name")));
        DetectionReportSourceDTO sourceDTO = new DetectionReportSourceDTO();
        sourceDTO.setHeader(header);
        sourceDTO.setData(sourceDataList);

        Integer tenant = SessionContextHolder.getRequester().getTenant();
        ReportRecord oldRecord = reportRecordService.lambdaQuery().select(ReportRecord::getSort).orderByDesc(ReportRecord::getCreateTime)
                .last("limit 1").one();
        int sort = oldRecord == null ? 1 : ObjectUtils.getOrElse(oldRecord.getSort() != null, oldRecord.getSort(), 0) + 1;
        TenantDTO tenantDTO = tenantClient.getByCode(tenant);
        String pinYinHead = PinYinUtils.getHead(ObjectUtils.getOrElse(StringUtils.isNotBlank(tenantDTO.getName()), tenantDTO.getName(), ""));

        Company company = companyService.lambdaQuery().eq(Company::getTenantId, tenant)
                .last(" limit 1").one();

        ReportRecord reportRecord = new ReportRecord();
        reportRecord.setId(id);
        reportRecord.setSort(sort);
        reportRecord.setCode("JD-" + pinYinHead.toUpperCase() + "-" + sort);
        reportRecord.setTenantName(tenantDTO.getName());
        reportRecord.setCompany(company.getDetectionCompany());
        reportRecord.setDetectionDate(commandParam.getPeriod().getEnd());
        reportRecord.setDetectionDatePeriod(
                DateUtils.format(commandParam.getPeriod().getBegin()) + "~" +
                DateUtils.format(commandParam.getPeriod().getEnd()));
        reportRecord.setArea(
                StringUtils.isNotBlank(deviceAreas) ? deviceAreas.deleteCharAt(deviceAreas.length() - 1).toString() : "");
        reportRecord.setStatement(company.getDetectionStatement());
        reportRecord.setCompanyAddress(company.getDetectionAddress());
        reportRecord.setPhone(company.getDetectionPhone());
        reportRecord.setTenantAddress(tenantDTO.getAddress());
        reportRecord.setPropertyName(propName);
        reportRecord.setBasicName(basicNames);
        reportRecord.setResultAppraise(resultAppraise.toString());
        reportRecord.setStampInfoList(JsonUtils.serialize(reportStampInfoList));
        reportRecord.setSourceList(JsonUtils.serialize(sourceDTO));
        reportRecordService.save(reportRecord);
        // 保存报表的公司信息
        ReportCompany reportCompany = new ReportCompany();
        reportCompany.setDetectionCompany(company.getDetectionCompany());
        reportCompany.setDetectionAddress(company.getDetectionAddress());
        reportCompany.setDetectionPhone(company.getDetectionPhone());
        reportCompany.setTemplateContent(company.getTemplateContent());
        reportCompany.setDetectionStatement(company.getDetectionStatement());
        reportCompany.setCalibrationExpireTime(company.getCalibrationExpireTime());
        reportCompany.setDetectionStampFileId(company.getDetectionStampFileId());
        reportCompany.setTenantId(company.getTenantId());
        reportCompany.setDetectionReportId(reportRecord.getId());
        reportRecord.setTenant(reportRecord.getTenant());
        EntityAuditor.populateCreateAudit(reportCompany, reportRecord.getCreateBy(), reportRecord.getCreatorId(), reportRecord.getCreatorType(), reportRecord.getCreateTime());
        EntityAuditor.populateUpdateAudit(reportCompany, reportRecord.getUpdateBy(), reportRecord.getUpdaterId(), reportRecord.getUpdaterType(), reportRecord.getUpdateTime());
        detectionReportCompanyMapper.insert(reportCompany);
        return this.getReportToVO(reportRecord);
    }

    private ReportRecordDetailVO getReportToVO(ReportRecord reportRecord) {
        reportRecord.setSourceList(null);
        ReportRecordDetailVO vo = ObjectUtils.copying(reportRecord, ReportRecordDetailVO.class);
        vo.setDetectionDateFmt(DateUtils.format(reportRecord.getDetectionDate(), "yyyy-MM-dd"));
        vo.setCreateTimeFmt(DateUtils.format(reportRecord.getDetectionDate(), "yyyy-MM-dd"));
        if (StringUtils.isNotBlank(reportRecord.getStampInfoList())) {
            vo.setReportStampInfoList(JsonUtils.deserialize(reportRecord.getStampInfoList(), new TypeReference<List<ReportStampInfoDTO>>() {}));
            vo.setStampInfoList(null);
        }
        return vo;
    }

    private void handlerDevicesData(DeviceBaseDetailsDTO deviceBaseDetailsDTO,
                                    Map<String, String> nameMap,
                                    List<Map<String, String>> sourceDataList,
                                    List<ReportStampInfoDTO> reportStampInfoList,
                                    Map<String, PropertyVO> propertyVOMap,
                                    Set<String> allPropCodeSet, Long startTs, Long endTs
                                   ) {
        Long deviceId = deviceBaseDetailsDTO.getId();
        // 获取设备历史记录
        DevicePropHistoryParam historyParam = new DevicePropHistoryParam();
        historyParam.setDeviceId(deviceId);
        historyParam.setPropCodes(new ArrayList<>(allPropCodeSet));
        historyParam.setStartTs(startTs);
        historyParam.setEndTs(endTs);
        List<KeyValueWithTsDTO> historyPropList = iotDevicePropClient.devicePropHistoryProp(historyParam);
        /* 剔除和校验 */
        Map<Long, Boolean> pressureMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(historyPropList)) {
            ListIterator<KeyValueWithTsDTO> iterator = historyPropList.listIterator();
            while (iterator.hasNext()) {
                KeyValueWithTsDTO next = iterator.next();
                String key = next.getKey();
                // 非数字剔除
                Object obj = next.getValue();
                double db;
                try {
                    db = Double.parseDouble(String.valueOf(obj));
                } catch (Exception e) {
                    log.error("{}({})：不是数值", deviceBaseDetailsDTO.getDeviceName(), key);
                    iterator.remove();
                    continue;
                }

                // 压差值剔除
                String name = nameMap.get(key);
                if (StringUtils.isNotBlank(name) && name.contains("压差") && db <= 3 && db >= -3) {
                    pressureMap.put(next.getTs(), true);
                    iterator.remove();
                }

                // 空调开关为0校验
                if (db == 0) {
                    PropertyVO propertyVO = propertyVOMap.get(key);
                    Assert.isParameterError(propertyVO != null &&
                                            propertyVO.getProductPropertyName().contains("空调开关"), "空调为关机状态,请重新选择");
                }
            }
        }

        Assert.isOutOfRange(CollectionUtils.isEmpty(historyPropList), "数据少于3,条请重新选择");

        // 按code分组的历史记录(处理平均值)
        Map<String, List<KeyValueWithTsDTO>> devicePropHistoryPropCodeGroup = historyPropList.stream()
                .collect(Collectors.groupingBy(KeyValueWithTsDTO::getKey));
        Map<String, Double> propAvgValueMap = new HashMap<>();
        Map<String, Double> propMaxValueMap = new HashMap<>();
        Map<String, Double> propMinValueMap = new HashMap<>();

        devicePropHistoryPropCodeGroup.forEach((k, ls) -> {
            DoubleSummaryStatistics stats = ls
                    .stream()
                    .filter(v -> v.getValue() != null)
                    .mapToDouble(v -> Double.parseDouble(v.getValue().toString()))
                    .summaryStatistics();
            Assert.isOutOfRange(ls.size() < 3, nameMap.get(k) + "数据少于3,条请重新选择");
            propAvgValueMap.put(k, stats.getAverage());
            propMaxValueMap.put(k, stats.getMax());
            propMinValueMap.put(k, stats.getMin());
        });

        ReportStampInfoDTO stampInfoDTO = new ReportStampInfoDTO();
        stampInfoDTO.setDeviceId(deviceId);
        stampInfoDTO.setDeviceName(deviceBaseDetailsDTO.getDeviceName());
        Map<String, ReportStampInfoDTO.PropCheck> propCheckMap = new HashMap<>();
        // 设备整体是否合格
        AtomicBoolean qualifiedBool = new AtomicBoolean(true);

        // 按时间分组的历史记录(处理导出的数据源)
        Map<Long, List<KeyValueWithTsDTO>> devicePropHistoryPropTimeGroup = historyPropList.stream()
                .collect(Collectors.groupingBy(KeyValueWithTsDTO::getTs));
        AtomicInteger count = new AtomicInteger();
        DecimalFormat df = new DecimalFormat("0.00");
        devicePropHistoryPropTimeGroup.forEach((t, ls) -> {
            // 不在压差剔除的数据中
            if (pressureMap.get(t) == null) {
                Map<String, String> map = new HashMap<>();
                map.put("name", deviceBaseDetailsDTO.getDeviceName());
                map.put("time", DateUtils.format(DateUtils.parse(t)));
                // 历史数据转map
                Map<String, Object> vm = ls
                        .stream()
                        .collect(Collectors.toMap(KeyValueWithTsDTO::getKey, KeyValueWithTsDTO::getValue, (o, n) -> n));
                count.getAndIncrement();
                allPropCodeSet.forEach(c -> {
                    String codeValue = "";
                    Double avg = propAvgValueMap.get(c);
                    Double max = propMaxValueMap.get(c);
                    Double min = propMinValueMap.get(c);
                    PropertyVO propertyVO = propertyVOMap.get(c);
                    if (propertyVO != null) {
                        Assert.isOutOfRange(avg == null, propertyVO.getProductPropertyName() + "数据少于3,条请重新选择");
                        /* 历史检测数据源 */
                        codeValue = vm.get(c) == null ? "" : vm.get(c) + propertyVO.getProductPropertyUnit();
                        /* 采样信息内容 */
                        ReportStampInfoDTO.PropCheck propCheck = new ReportStampInfoDTO.PropCheck();
                        propCheck.setPropCode(c);
                        propCheck.setPropName(propertyVO.getProductPropertyName() + "(" + propertyVO.getProductPropertyUnit() + ")");
                        propCheck.setValue(propAvgValueMap.get(c) == null ? "" : df.format(propAvgValueMap.get(c)));
                        if (propertyVO.getMinVal() != null && propertyVO.getMaxVal() != null) {
                            if (propertyVO.getMinVal().equals(propertyVO.getMaxVal())) {
                                propCheck.setMaxMin("" + propertyVO.getMaxVal());
                            } else {
                                propCheck.setMaxMin(propertyVO.getMinVal() + " ～ " + propertyVO.getMaxVal());
                            }
                            propCheck.setMin(propertyVO.getMinVal().toString());
                            propCheck.setMax(propertyVO.getMaxVal().toString());
                        } else if (propertyVO.getMinVal() != null) {
                            propCheck.setMaxMin("≥ " + propertyVO.getMinVal());
                            propCheck.setMin(propertyVO.getMinVal().toString());
                        } else if (propertyVO.getMaxVal() != null) {
                            propCheck.setMaxMin("≤ " + propertyVO.getMaxVal());
                            propCheck.setMax(propertyVO.getMaxVal().toString());
                        }
                        BigDecimal maxValue = new BigDecimal(max);
                        BigDecimal minValue = new BigDecimal(min);
                        boolean maxBool =
                                (Objects.isNull(propertyVO.getMinVal()) || maxValue.compareTo(propertyVO.getMinVal()) >= 0) &&
                                (Objects.isNull(propertyVO.getMaxVal()) || maxValue.compareTo(propertyVO.getMaxVal()) <= 0);
                        boolean minBool =
                                (Objects.isNull(propertyVO.getMinVal()) || minValue.compareTo(propertyVO.getMinVal()) >= 0) &&
                                (Objects.isNull(propertyVO.getMaxVal()) || minValue.compareTo(propertyVO.getMaxVal()) <= 0);
                        if (maxBool && minBool) {
                            propCheck.setResult("合格");
                        } else {
                            propCheck.setResult("不合格");
                            qualifiedBool.set(false);
                        }
                        propCheckMap.put(c, propCheck);
                        Assert.isParameterError(!(maxBool && minBool),
                                                "检测结论不合格，请重新选择。(" + propertyVO.getProductPropertyName() + ")");

                    }
                    map.put(c, codeValue);
                });
                sourceDataList.add(map);
            }
        });
        Assert.isOutOfRange(count.get() < 3, "可用的数据少于3条，请重新选择！");
        stampInfoDTO.setQualifiedBool(qualifiedBool.get());
        stampInfoDTO.setPropCheckList(new ArrayList<>(propCheckMap.values()));
        reportStampInfoList.add(stampInfoDTO);
    }

    @Override
    public void downloadExcelSourceList(long recordId) {
        //根据报告id获取报告生成记录
        String sourceList = reportRecordService.getRecordSourceListById(recordId);
        Assert.isParameterError(StringUtils.isEmpty(sourceList), String.format("报告：%s，查询到的数据源列表为空", recordId));
        try {

            DetectionReportSourceDTO source = JsonUtils.deserializeToBean(sourceList, new TypeReference<DetectionReportSourceDTO>() {});
            Assert.parameterIsNull(source, "数据源列表为空，请联系管理员！");
            exportSourceList(source);
            log.info("数据源列表导出成功！");
        } catch (JsonProcessingException e) {
            log.error("数据源json格式有错误！,错误原因:{}", e.getMessage());
            throw new ServiceException("数据源json格式有错误,请联系管理员！");
        } catch (RuntimeException | IOException e) {
            log.error("数据源导出有错，错误原因：{}", e.getMessage());
            throw new ServiceException("数据源导出有错，请联系管理员！");
        }

    }

    private void exportSourceList(@NonNull DetectionReportSourceDTO source) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter();
        List<Map<String, String>> header = source.getHeader();
        Assert.isParameterError(ListUtils.isEmpty(header), "数据源为设置表头信息，请联系管理员！");
        //设置excel表头
        header.forEach(map -> map.forEach(writer::addHeaderAlias));
        //设置数据
        List<Map<String, String>> data = source.getData();
        writer.write(data, true);
        ExcelUtils.downloadExcel(request, response, "数据源列表", writer);

    }

    private void updateProperty(long basicId, DetectionBasicParam basicParam) {
        //查询这个依据所有属性
        List<Property> propertyList = propertyService.list(new LambdaQueryWrapper<Property>().eq(Property::getBasicId, basicId)
                                                                   .eq(Property::getDeleted, BigInteger.ZERO));
        Map<Long, List<Property>> standardPropertyMap = MapUtils.listGroupToMap(propertyList, Property::getStandardId);
        List<Property> waitUpdateList = new ArrayList<>();
        List<Property> waitDeleteList = new ArrayList<>();
        List<Property> waitInsertList = new ArrayList<>();
        List<DetectionBasicParam.StandardParam> standardParams = basicParam.getStandards();
        standardParams.forEach(standardParam -> {
            //修改的属性
            List<DetectionBasicParam.PropertyParam> propertyParams = standardParam.getProperties();
            List<DetectionBasicParam.PropertyParam> updateList = propertyParams
                    .stream()
                    .filter(propertyParam -> Objects.nonNull(propertyParam.getId()))
                    .collect(Collectors.toList());
            Set<Long> updatePropertyIdSet = SetUtils.listToSet(updateList, DetectionBasicParam.PropertyParam::getId);
            Map<Long, DetectionBasicParam.PropertyParam> propertyParamMap = MapUtils.listToMap(updateList, DetectionBasicParam.PropertyParam::getId, propertyParam -> propertyParam);
            List<Property> existProperties = standardPropertyMap.getOrDefault(standardParam.getId(), new ArrayList<>());
            existProperties.forEach(property -> {
                Long id = property.getId();
                DetectionBasicParam.PropertyParam propertyParam = propertyParamMap.get(id);
                if (Objects.isNull(propertyParam)) {
                    return;
                }
                ObjectUtils.copying(propertyParam, property);
            });

            waitUpdateList.addAll(existProperties);
            //新增的属性
            List<Property> insertPropertyList = propertyParams
                    .stream()
                    .filter(propertyParam -> Objects.isNull(propertyParam.getId()))
                    .map(propertyParam -> {
                        propertyParam.setId(IdWorker.getId());
                        Property property = ObjectUtils.copying(propertyParam, Property.class);
                        property.setBasicId(basicId);
                        property.setStandardId(standardParam.getId());
                        EntityAuditor.populateAudit(property);
                        return property;
                    }).collect(Collectors.toList());
            waitInsertList.addAll(insertPropertyList);
            //统计删除的属性，用于标准存在，但是标准下的属性有变动的情况
            List<Property> delList = existProperties
                    .stream()
                    .filter(property -> {
                        Long propertyId = property.getId();
                        return !updatePropertyIdSet.contains(propertyId);
                    })
                    .collect(Collectors.toList());
            waitDeleteList.addAll(delList);

        });
        //统计删除的属性，用于那些被删除了标准但是没有被删除属性的
        //查询现在依据下的标准
        List<Standard> standards = standardService.listByBasicId(basicId);
        List<Long> standardIds = ListUtils.convertItem(standards, Standard::getId);
        //把标准已被删除的属性添加到待删除列表
        List<Property> delList = propertyList
                .stream()
                .filter(property -> {
                    Long standardId = property.getStandardId();
                    return !standardIds.contains(standardId);
                })
                .collect(Collectors.toList());
        waitDeleteList.addAll(delList);
        propertyService.updateBatchById(waitUpdateList);
        DatabaseContextHolder.softDelete(Property.class, waitDeleteList);
        propertyService.saveBatch(waitInsertList);
    }

    private void updateStandard(long basicId, DetectionBasicParam basicParam) {
        //查询依据下面的标准
        List<Standard> standards = standardService.list(new LambdaQueryWrapper<Standard>().eq(Standard::getBasicId, basicId)
                                                                .eq(Standard::getDeleted, BigInteger.ZERO));
        Set<Long> existStandardIds = SetUtils.listToSet(standards, Standard::getId);
        Assert.isParameterError(ListUtils.isEmpty(existStandardIds), "依据id：{},存在的标准为空，数据异常，请联系管理员");
        List<DetectionBasicParam.StandardParam> standardParams = basicParam.getStandards();
        //需要修改的标准
        updateStandards(standardParams, existStandardIds);
        //删除的标准
        delStandards(standardParams, existStandardIds);
        //新增的标准
        insertNewStandards(standardParams, basicId);
    }

    private void insertNewStandards(List<DetectionBasicParam.StandardParam> standardParams, long basicId) {
        List<DetectionBasicParam.StandardParam> insertStandardList = standardParams.stream()
                .filter(standardParam -> Objects.isNull(standardParam.getId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(insertStandardList)) {
            log.debug("没有要新增的标准");
            return;
        }
        Map<String, Long> standardsIdMap = insertStandardList.stream()
                .collect(Collectors.toMap(DetectionBasicParam.StandardParam::getName, IdWorker::getId));
        //设置标准的Id
        for (DetectionBasicParam.StandardParam standardParam : standardParams) {
            standardParam.setId(standardsIdMap.get(standardParam.getName()));
        }
        insertStandards(insertStandardList, basicId, standardsIdMap);
    }

    private void delStandards(List<DetectionBasicParam.StandardParam> standardParams, Set<Long> existStandardIds) {
        Set<Long> standardIds = standardParams.stream().map(DetectionBasicParam.StandardParam::getId).filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<Long> delIds = existStandardIds.stream().filter(id -> !standardIds.contains(id)).collect(Collectors.toList());
        if (ListUtils.isEmpty(delIds)) {
            log.debug("没有要删除的标准");
            return;
        }
        List<Standard> standardList = standardService.listByIds(delIds);
        if (ListUtils.isNotEmpty(standardList)) {
            //批量删除
            DatabaseContextHolder.softDelete(Standard.class, standardList);
        }
    }

    private void updateStandards(@NonNull List<DetectionBasicParam.StandardParam> standardParams, Set<Long> existStandardIds) {
        Map<Long, String> updateStandardsMap = standardParams
                .stream()
                //保留修改的标准，就是有id的实体
                .filter(standardParam -> Objects.nonNull(standardParam.getId()))
                //当前系统存在的id，才是要修改的
                .filter(standardParam -> existStandardIds.contains(standardParam.getId()))
                .collect(Collectors.toMap(DetectionBasicParam.StandardParam::getId, DetectionBasicParam.StandardParam::getName));
        //查询存在的标准
        Set<Long> standards = updateStandardsMap.keySet();
        if (ListUtils.isEmpty(standards)) {
            log.debug("没有要更新的标准");
            return;
        }
        List<Standard> standardList = standardService.listByIds(standards);
        //批量修改标准名称
        standardList.forEach(standard -> standard.setName(updateStandardsMap.get(standard.getId())));
        standardService.updateBatchById(standardList);
    }

    private void updateBasic(long basicId, DetectionBasicParam basicParam) {
        Basic basic = basicService.getById(basicId);
        if (Objects.isNull(basic)) {
            log.error("修改的依据不存在");
            return;
        }
        basic.setName(basicParam.getBasicName());
        basicService.updateById(basic);
    }

    private void deleteProperties(long id) {
        LambdaUpdateWrapper<Property> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Property::getBasicId, id);
        updateWrapper.eq(Property::getDeleted, BigInteger.ZERO);
        updateWrapper.set(Property::getDeleted, SecurityContextHolder.getPrincipal().getId());
        propertyService.update(updateWrapper);
    }

    private void deleteStandards(long id) {
        LambdaUpdateWrapper<Standard> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Standard::getDeleted, SecurityContextHolder.getPrincipal().getId());
        updateWrapper.eq(Standard::getBasicId, id);
        updateWrapper.eq(Standard::getDeleted, BigInteger.ZERO);
        standardService.update(updateWrapper);
    }

    private void deleteBasic(long id) {
        //查询删除的依据是否存在
        Basic basic = basicService.getOne(new LambdaQueryWrapper<Basic>().eq(Basic::getId, id).eq(Basic::getDeleted, BigInteger.ZERO));
        if (Objects.isNull(basic)) {
            log.info("删除的依据不存在，跳过删除");
            return;
        }
        DatabaseContextHolder.softDelete(Basic.class, basic);
    }

    private void insertProperties(List<DetectionBasicParam.StandardParam> standardParams, long basicId, Map<String, Long> standardIdsMap) {
        List<List<Property>> propertyList = ListUtils.convertItem(standardParams, standardParam -> {

            List<DetectionBasicParam.PropertyParam> propertyParams = standardParam.getProperties();
            String name = standardParam.getName();
            Long standardId = standardIdsMap.get(name);
            return ListUtils.convertItem(propertyParams, propertyParam -> {
                Property property = ObjectUtils.copying(propertyParam, Property.class);
                property.setId(IdWorker.getId());
                property.setStandardId(standardId);
                property.setBasicId(basicId);
                EntityAuditor.populateAudit(property);
                return property;
            });
        });
        List<Property> list = propertyList.stream().flatMap(List::stream).collect(Collectors.toList());
        propertyService.saveBatch(list);
    }

    private void insertStandards(List<DetectionBasicParam.StandardParam> standardParams, long basicId, Map<String, Long> standardIdsMap) {
        List<Standard> standardList = ListUtils.convertItem(standardParams, standardParam -> {
            String name = standardParam.getName();
            Standard standard = new Standard();
            standard.setName(name);
            standard.setBasicId(basicId);
            Long standardId = standardIdsMap.get(name);
            standard.setId(standardId);
            EntityAuditor.populateAudit(standard);
            return standard;
        });
        standardService.saveBatch(standardList);
    }

    private void insertBasic(@NonNull String basicName, long basicId) {
        Basic basic = new Basic();
        basic.setName(basicName);
        basic.setId(basicId);
        EntityAuditor.populateAudit(basic);
        basicService.save(basic);
    }

    private void setCompanyUpdateField(Company company, CompanyCommandParam commandParam) {
        company.setUpdateBy(SecurityContextHolder.getPrincipal().getAccount());
        company.setUpdateTime(LocalDateTime.now());
        company.setDetectionCompany(commandParam.getDetectionCompany());
        company.setDetectionAddress(commandParam.getDetectionAddress());
        company.setDetectionPhone(commandParam.getDetectionPhone());
        company.setDetectionStatement(commandParam.getDetectionStatement());
        company.setDetectionStampFileId(commandParam.getDetectionStampFileId());
        company.setCalibrationExpireTime(this.dayToTime(commandParam.getCalibrationExpireDay()));
        company.setTemplateContent(commandParam.getTemplateContent());
    }

    private LocalDateTime dayToTime(Integer day) {
        if (day != null && day > 0) {
            LocalDateTime nowTime = LocalDateTime.now();
            return nowTime.plusDays(day + 1);
        }
        return null;
    }

    private void companyFileInduce(Long fileId) {
        if (fileId != null) {
            fileOperateClient.fileInduceSingle(FILE_CODE, null, fileId);
        }
    }
}
