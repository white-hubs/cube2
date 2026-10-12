package com.guhecloud.cube.srv.gen.personnel.management.server.querygateway;

import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.adapter.UserDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.dto.UserDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryParam;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.PositionClient;
import com.guhecloud.cube.biz.ouaa.client.dto.AppointmentDetailDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.PositionDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.client.param.PosistionQueryParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffExtendCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffTypeCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.interfaces.vo.ApprovalDataVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.querygateway.FlowFormDataQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.querygateway.query.FlowFormDataQueryParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.dao.StaffMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.EducationEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.GenderEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.MaritalStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.PoliticalStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.ProbationPeriodEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.StaffStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.WorkingStatus;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params.StaffNotInParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.HomePageStatisticsVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffChangeStatVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffChangeVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffDetailAttendVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffDetailVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffExcelVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffExchangeStatisticsVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffStateVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffStructureStatisticsVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffExtendPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query.StaffQueryParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.DateUnitUtils;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.ExcelBeanUtil;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.MathUtils;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.PageResponse;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.ExcelUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.DesensitizeUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.ServletUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
 * @author bq
 * @date 2022/11/08
 * @description 员工查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffQueryService extends ServiceImpl<StaffMapper, StaffPO> {

    private static final String EDU = "edu";
    private static final String POLITICAL = "political";
    private static final String STAFF_TYPE = "staffType";
    private static final String STAFF_STATUS = "staffStatus";
    private static final String PROBATION_PERIOD = "probationPeriod";
    private static final String GENDER = "gender";
    private static final String MARITAL_STATUS = "maritalStatus";

    private static final String ORGANIZATION_ID = "organizationId";
    private static final String APPOINTMENT_ID = "appointmentId";
    private static final String POSITION_IDS = "positionIds";

    private static final String EDUCATION = "education";
    private static final String POLITICAL_STATUS = "politicalStatus";
    private static final String TYPE = "type";
    private static final String STATUS = "status";

    private static final String ROTATION = "rotation";            // 轮岗
    private static final String INDUCTION = "induction";            // 入职
    private static final String REGULAR_WORKER = "regularWorker";    // 转正
    private static final String LEAVE_OFFICE = "leaveOffice";        // 离职

    private final StaffMapper staffMapper;
    private final OrganizationClient organizationClient;
    private final FlowFormDataQueryService flowFormDataQueryService;
    private final PositionClient positionClient;
    private final AppointmentClient appointmentClient;
    private final DataDictionaryAdapter dataDictionaryAdapter;
    private final UserDictionaryAdapter userDictionaryAdapter;
    private final StaffTypeCommandService staffTypeCommandService;
    private final StaffExtendCommandService staffExtendCommandService;
    private final AreaClient areaClient;

    /**
     * 获取员工，排除id（考勤管理使用）
     * 注意字段不全，需要的自己加xml的查询字段
     * param.notInIds:需要排除的id集合
     *
     * @param param 入参对象
     *
     * @return 员工集合
     */
    public Pagination<StaffPO> getStaffPageNotIn(StaffNotInParam param) {
        Page<StaffPO> page = staffMapper.getStaffPageNotIn(new Page(param.getCurrent(), param.getSize()), param);
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    /**
     * 根据员工id集合查询员工
     * 考勤使用字段不全
     *
     * @param ids id集合
     *
     * @return 员工集合
     */
    public List<StaffPO> getStaffByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return staffMapper.selectList(Wrappers.<StaffPO>lambdaQuery().select(StaffPO::getId, StaffPO::getName, StaffPO::getJobNumber)
                                              .in(StaffPO::getId, ids));
    }

    public StaffDetailVO getStaffVOById(Long staffId) {
        StaffPO staffPO = staffMapper.selectById(staffId);
        if (staffPO == null) {
            return null;
        }
        StaffDetailVO detailVO = ObjectUtils.copying(staffPO, StaffDetailVO.class);
        detailVO.setDateOfBirth(null);
        return detailVO;
    }

    public StaffDetailVO get(Long id) {
        StaffPO staffPO = this.getById(id);
        if (Objects.isNull(staffPO)) {
            staffPO = this.lambdaQuery().eq(StaffPO::getUserId, id).one();
        }
        StaffDetailVO detailVO = ObjectUtils.copying(staffPO, StaffDetailVO.class);
        if (Objects.isNull(detailVO)) {
            return null;
        }
        this.extend(Collections.singletonList(detailVO), true);
        detailVO.setTypeName(staffTypeCommandService.valueOfType(detailVO.getType()));
        // 扩展职位名称
        List<String> ids = null;
        if (ObjectUtils.isNotEmpty(staffPO.getAppointmentId())) {
            ids = Arrays.stream(staffPO.getAppointmentId().split(",")).collect(Collectors.toList());
        }
        detailVO.setAppointmentIds(ObjectUtils.isNotEmpty(ids) ? ids : new ArrayList<>());
        return detailVO;
    }

    public StaffDetailVO getByMobile(String mobile) {
        StaffPO staffPO = this.lambdaQuery().eq(StaffPO::getMobile, mobile).one();
        if (Objects.isNull(staffPO)) {
            return null;
        }
        StaffDetailVO detailVO = ObjectUtils.copying(staffPO, StaffDetailVO.class);
        this.extend(Collections.singletonList(detailVO), false);
        detailVO.setTypeName(staffTypeCommandService.valueOfType(detailVO.getType()));

        return detailVO;
    }

    public List<StaffDetailVO> getList(StaffQueryParam staffQueryParam) {
        List<StaffDetailVO> detailVOS = staffMapper.getList(staffQueryParam);
        this.extend(detailVOS, false);
        return detailVOS;
    }

    public List<StaffDetailAttendVO> getListByAttend(StaffQueryParam staffQueryParam) {
        List<StaffDetailVO> detailVOS = staffMapper.getList(staffQueryParam);

        if (CollectionUtils.isEmpty(detailVOS)) {
            return Collections.emptyList();
        }
        this.extend(detailVOS, false);
        return ConvertUtils.toList(StaffDetailAttendVO.class, detailVOS);
    }

    /**
     * 获取员工数量
     *
     * @return
     */
    public long getNum() {
        //TODO 过滤掉离职的
        return this.getStaffNum(null, "", null);
    }

    public Page<StaffDetailVO> getPage(StaffQueryParam staffQueryParam) {
        Page<StaffDetailVO> page = staffMapper.getPage(PagingUtils.convert(staffQueryParam), staffQueryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return page;
        }
        this.extend(page.getRecords(), true);
        return page;
    }

    /**
     * 批量导出
     *
     * @param param    查询参数
     * @param request
     * @param response
     *
     * @throws Exception
     */
    public void exportData(StaffQueryParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<StaffDetailVO> staffVOS = staffMapper.getList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(staffVOS), "未查询到员工，无法导出");
        this.extend(staffVOS, false);
        List<StaffExcelVO> excelVOS = ObjectUtils.copying(staffVOS, StaffExcelVO.class);
        // 获取excel表格数据
        ExcelBeanUtil<StaffExcelVO> excelBeanUtil = new ExcelBeanUtil<>(StaffExcelVO.class);
        List<Map<String, String>> excelDatas = excelBeanUtil.getExcelDatas(excelVOS);
        ExcelWriter writer = ExcelUtils.getWriter(excelDatas, false, false, "");
        ExcelUtils.downloadExcel(request, response, "人员花名册", writer);
    }

    /**
     * 下载模板
     *
     * @param request
     * @param response
     */
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, String>> excelHeader = new ArrayList<>();
        ExcelBeanUtil<StaffExcelVO> excelBeanUtil = new ExcelBeanUtil<>(StaffExcelVO.class);
        Map<String, String> fieldNames = excelBeanUtil.getFieldNames();
        excelHeader.add(fieldNames);
        ExcelWriter writer = ExcelUtils.getWriter(excelHeader, true, true, "花名册导入模板");
        int i = 0;
        // 配置下拉选项
        for (Map.Entry<String, String> entry : fieldNames.entrySet()) {
            if ("genderName".equals(entry.getKey())) {
                // 性别
                writer.addSelect(new CellRangeAddressList(2, 500, i, i), GenderEnum.getList().stream().map(m -> m.get("name"))
                        .collect(Collectors.joining(",")));
            }
            if ("postNames".equals(entry.getKey())) {
                // 岗位
                String postNames = positionClient.list(PosistionQueryParam.builder().enabled(true).build()).stream()
                        .map(PositionDTO::getName).collect(Collectors.joining(","));
                writer.addSelect(new CellRangeAddressList(2, 500, i, i), postNames);
            }
            if ("typeName".equals(entry.getKey())) {
                // 员工类型
                writer.addSelect(new CellRangeAddressList(2, 500, i, i), staffTypeCommandService.getStaffTypeMap().stream()
                        .map(m -> m.get("name")).collect(Collectors.joining(",")));
            }
            if ("statusName".equals(entry.getKey())) {
                // 员工状态
                writer.addSelect(new CellRangeAddressList(2, 500, i, i), StaffStatusEnum.getList().stream().map(m -> m.get("name"))
                        .collect(Collectors.joining(",")));
            }
            if ("areaNameList".equals(entry.getKey())) {
                setDropdownToExcel(writer, areaClient.listAreas().stream().map(AreaDTO::getName)
                        .toArray(String[]::new), 2, 500, i, i, entry.getKey());
            }
            if ("educationName".equals(entry.getKey())) {
                // 学历
                writer.addSelect(new CellRangeAddressList(2, 500, i, i), EducationEnum.getList().stream().map(m -> m.get("name"))
                        .collect(Collectors.joining(",")));
            }
            if ("organizationName".equals(entry.getKey())) {
                // 部门
                OrganizationQueryParam organizationQueryParam = new OrganizationQueryParam();
                String[] array = organizationClient.getOrganizations(organizationQueryParam).stream()
                        .filter(t -> Boolean.TRUE.equals(t.getEnabled()))
                        .map(OrganizationDTO::getName)
                        .toArray(String[]::new);
                setDropdownToExcel(writer, array, 2, 500, i, i, entry.getKey());
            }
            if ("probationPeriodName".equals(entry.getKey())) {
                // 试用期
                writer.addSelect(new CellRangeAddressList(2, 500, i, i), ProbationPeriodEnum.getList().stream().map(m -> m.get("name"))
                        .collect(Collectors.joining(",")));
            }
            i = i + 1;
        }
        ExcelUtils.downloadExcel(request, response, "人员花名册模板", writer);
    }

    private void setDropdownToExcel(ExcelWriter writer, String[] dataArray, int firstRow, int lastRow, int firstCol, int lastCol,
                                    String sheetName
                                   ) {
        if (dataArray.length == 0) {
            return;
        }
        // 创建名称，可被其他单元格引用
        Workbook workbook = writer.getWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        Cell cell;
        for (int i = 0, length = dataArray.length; i < length; i++) {
            String name = dataArray[i];
            Row row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name);
        }
        Name namedCell = workbook.createName();
        namedCell.setNameName(sheetName);
        // 设置名称引用的公式
        namedCell.setRefersToFormula(sheetName + "!$A$1:$A$" + dataArray.length);
        //加载数据,将名称为hidden的sheet中的数据转换为List形式

        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

        //设置为隐藏

        workbook.setSheetHidden(workbook.getSheetIndex(sheetName), true);
        DataValidationHelper dataValidationHelper = writer.getSheet().getDataValidationHelper();
        DataValidationConstraint dataValidationConstraint = dataValidationHelper.createFormulaListConstraint(sheetName);
        // 将设置下拉选的位置和数据的对应关系 绑定到一起
        DataValidation createValidation = dataValidationHelper.createValidation(dataValidationConstraint, regions);

        //处理Excel兼容性问题
        if (createValidation instanceof XSSFDataValidation) {
            createValidation.setSuppressDropDownArrow(true);
            createValidation.setShowErrorBox(true);
        } else {
            createValidation.setSuppressDropDownArrow(false);
        }
        writer.getSheet().addValidationData(createValidation);
    }

    /**
     * 获取员工异动列表
     *
     * @param param 查询条件
     *
     * @return
     */
    public Pagination<StaffChangeVO> getStaffChange(StaffQueryParam param) {
        if (ROTATION.equals(param.getType())) {
            return Pagination.empty();
        } else {
            Page<StaffChangeVO> page = staffMapper.getStaffChange(PagingUtils.convert(param), param);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                return Pagination.empty();
            }
            Map<String, String> staffTypeMap = staffTypeCommandService.getStaffTypeByVOList(page.getRecords(), "type");
            page.getRecords().forEach(t -> {
                if (StringUtils.isNotBlank(t.getType())) {
                    t.setTypeName(staffTypeMap.get(t.getType()));
                }
            });
            return PagingUtils.convert(page, StaffChangeVO.class);
        }
    }

    /**
     * 员工异动统计
     *
     * @param param 查询参数
     *
     * @return
     */
    public Pagination<StaffChangeStatVO> getStaffChangeStat(StaffQueryParam param) {
        // 查询全量部门
        OrganizationQueryParam queryParam = new OrganizationQueryParam();
        queryParam.setEnabled(true);
        List<OrganizationDTO> organizations = organizationClient.getOrganizations(queryParam);
        Pagination<OrganizationDTO> pagination = PageResponse.doPage(organizations, param.getPageNumber(), param.getPageSize());
        return PagingUtils.convert(pagination, this.countChangeOrganizations(param, pagination.getRecords()));
    }

    /**
     * 导出人员异动统计
     *
     * @param param
     * @param request
     * @param response
     */
    public void getStaffChangeStatExport(StaffQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        // 查询全量部门
        OrganizationQueryParam queryParam = new OrganizationQueryParam();
        queryParam.setEnabled(true);
        List<OrganizationDTO> organizations = organizationClient.getOrganizations(queryParam);
        List<StaffChangeStatVO> staffChangeStatVOS = this.countChangeOrganizations(param, organizations);
        Assert.isNotFound(CollectionUtils.isEmpty(staffChangeStatVOS), "导出失败，未查询到数据");
        List<StaffChangeStatVO> excelVOS = ObjectUtils.copying(staffChangeStatVOS, StaffChangeStatVO.class);
        // 获取excel表格数据
        try {
            ExcelBeanUtil<StaffChangeStatVO> excelBeanUtil = new ExcelBeanUtil<>(StaffChangeStatVO.class);
            List<Map<String, String>> excelDatas = excelBeanUtil.getExcelDatas(excelVOS);
            ExcelWriter writer = ExcelUtils.getWriter(excelDatas, false, false, "");
            ExcelUtils.downloadExcel(request, response, "人员异动统计", writer);
        } catch (Exception e) {
            log.error("导出人员异动统计失败", e);
            throw new ServiceException("导出数据失败，请联系管理员");
        }
    }

    /**
     * 人员结构统计
     *
     * @return
     */
    public StaffStructureStatisticsVO getStrucStatistics() {
        StaffStructureStatisticsVO statisticsVO = new StaffStructureStatisticsVO();
        // 查询员工列表
        List<StaffPO> staffPOS = this.lambdaQuery()
                .ne(StaffPO::getStatus, StaffStatusEnum.LEAVE_OFFICE.name())
                .or()
                .isNull(StaffPO::getStatus)
                .list();
        if (CollectionUtils.isEmpty(staffPOS)) {
            return statisticsVO;
        }
        statisticsVO.setTotal(staffPOS.size());
        // 部门分布
        List<StaffStructureStatisticsVO.Statistics> deptDistr = new ArrayList<>();
        Set<Long> deptIds = staffPOS.parallelStream().map(StaffPO::getOrganizationId).collect(Collectors.toSet());
        OrganizationQueryParam queryParam = new OrganizationQueryParam();
        queryParam.setIds(deptIds);
        List<OrganizationDTO> organizations = organizationClient.getOrganizationsListByIds(queryParam);
        staffPOS.parallelStream().filter(s -> Objects.nonNull(s.getOrganizationId()))
                .collect(Collectors.groupingBy(StaffPO::getOrganizationId)).forEach((deptId, list) -> {
                    OrganizationDTO organizationDTO = organizations.stream().filter(o -> o.getId().equals(deptId)).findAny().orElse(null);
                    if (organizationDTO != null) {
                        StaffStructureStatisticsVO.Statistics statistics = new StaffStructureStatisticsVO.Statistics();
                        statistics.setName(organizationDTO.getName());
                        statistics.setNum(list.size());
                        deptDistr.add(statistics);
                    }
                });
        statisticsVO.setDeptDistr(deptDistr);
        // 性别分布
        List<StaffStructureStatisticsVO.Statistics> genderDistr = new ArrayList<>();
        staffPOS.parallelStream().filter(s -> Objects.nonNull(s.getGender())).collect(Collectors.groupingBy(StaffPO::getGender))
                .forEach((gender, list) -> {
                    StaffStructureStatisticsVO.Statistics statistics = new StaffStructureStatisticsVO.Statistics();
                    statistics.setName(GenderEnum.valueOf(gender).getValue());
                    statistics.setNum(list.size());
                    genderDistr.add(statistics);
                });
        statisticsVO.setGenderDistr(genderDistr);
        // 状态分布
        List<StaffStructureStatisticsVO.Statistics> statusDistr = new ArrayList<>();
        staffPOS.parallelStream().filter(s -> Objects.nonNull(s.getStatus())).collect(Collectors.groupingBy(StaffPO::getStatus))
                .forEach((status, list) -> {
                    StaffStructureStatisticsVO.Statistics statistics = new StaffStructureStatisticsVO.Statistics();
                    statistics.setName(StaffStatusEnum.valueOf(status).getValue());
                    statistics.setNum(list.size());
                    statusDistr.add(statistics);
                });
        statisticsVO.setStatusDistr(statusDistr);
        // 学历分布
        List<StaffStructureStatisticsVO.Statistics> eduDistr = new ArrayList<>();
        staffPOS.parallelStream().filter(s -> !Objects.isNull(s.getEducation())).collect(Collectors.groupingBy(StaffPO::getEducation))
                .forEach((edu, list) -> {
                    StaffStructureStatisticsVO.Statistics statistics = new StaffStructureStatisticsVO.Statistics();
                    statistics.setName(EducationEnum.valueOf(edu).getValue());
                    statistics.setNum(list.size());
                    eduDistr.add(statistics);
                });
        statisticsVO.setEduDistr(eduDistr);
        // 年龄分布
        List<StaffStructureStatisticsVO.Statistics> ageDistr = statisticsVO.initAgeDistr();
        staffPOS.parallelStream().filter(s -> !Objects.isNull(s.getDateOfBirth())).map(s -> DateUtils.ageOfNow(s.getDateOfBirth()))
                .collect(Collectors.groupingBy(Integer::intValue)).forEach((age, list) -> {
                    if (age >= 18 && age <= 22) {
                        StaffStructureStatisticsVO.Statistics age18 = ageDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.AGE18_22)).findAny().get();
                        age18.setNum(age18.getNum() + list.size());
                    } else if (age >= 23 && age <= 27) {
                        StaffStructureStatisticsVO.Statistics age22 = ageDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.AGE22_26)).findAny().get();
                        age22.setNum(age22.getNum() + list.size());
                    } else if (age >= 28 && age <= 32) {
                        StaffStructureStatisticsVO.Statistics age26 = ageDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.AGE26_30)).findAny().get();
                        age26.setNum(age26.getNum() + list.size());
                    } else if (age >= 33 && age <= 37) {
                        StaffStructureStatisticsVO.Statistics age30 = ageDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.AGE30_34)).findAny().get();
                        age30.setNum(age30.getNum() + list.size());
                    } else if (age >= 38 && age <= 42) {
                        StaffStructureStatisticsVO.Statistics age34 = ageDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.AGE34_38)).findAny().get();
                        age34.setNum(age34.getNum() + list.size());
                    } else if (age >= 43 && age <= 47) {
                        StaffStructureStatisticsVO.Statistics age38 = ageDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.AGE38_42)).findAny().get();
                        age38.setNum(age38.getNum() + list.size());
                    } else if (age >= 48) {
                        StaffStructureStatisticsVO.Statistics age42 = ageDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.AGE42)).findAny().get();
                        age42.setNum(age42.getNum() + list.size());
                    }
                });
        statisticsVO.setAgeDistr(ageDistr);
        // 司龄分布
        List<StaffStructureStatisticsVO.Statistics> seniorityDistr = statisticsVO.initInductionDateDistr();
        staffPOS.parallelStream().filter(s -> !Objects.isNull(s.getInductionDate())).map(s -> DateUtils.ageOfNow(s.getInductionDate()))
                .collect(Collectors.groupingBy(Integer::intValue)).forEach((age, list) -> {
                    if (age < 1) {
                        StaffStructureStatisticsVO.Statistics seniority1 = seniorityDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.SENIORITY1)).findAny().get();
                        seniority1.setNum(seniority1.getNum() + list.size());
                    } else if (age <= 3) {
                        StaffStructureStatisticsVO.Statistics seniority3 = seniorityDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.SENIORITY1_3)).findAny().get();
                        seniority3.setNum(seniority3.getNum() + list.size());
                    } else if (age <= 6) {
                        StaffStructureStatisticsVO.Statistics seniority5 = seniorityDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.SENIORITY3_5)).findAny().get();
                        seniority5.setNum(seniority5.getNum() + list.size());
                    } else if (age <= 9) {
                        StaffStructureStatisticsVO.Statistics seniority8 = seniorityDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.SENIORITY5_8)).findAny().get();
                        seniority8.setNum(seniority8.getNum() + list.size());
                    } else {
                        StaffStructureStatisticsVO.Statistics seniority8Up = seniorityDistr.stream()
                                .filter(d -> d.getName().equals(StaffStructureStatisticsVO.SENIORITY8)).findAny().get();
                        seniority8Up.setNum(seniority8Up.getNum() + list.size());
                    }
                });
        statisticsVO.setInductionDateDistr(seniorityDistr);
        return statisticsVO;
    }

    /**
     * 员工概览-变动统计
     *
     * @param param
     *
     * @return
     */
    public StaffExchangeStatisticsVO getExchangeStatistics(StaffQueryParam param) {
        StaffExchangeStatisticsVO statisticsVO = new StaffExchangeStatisticsVO();
        statisticsVO.setInductionNum(this.getStaffNum(param.getPeriod(), INDUCTION, null));
        statisticsVO.setRegularWorkerNum(this.getStaffNum(param.getPeriod(), REGULAR_WORKER, null));
        statisticsVO.setQuitNum(this.getStaffNum(param.getPeriod(), LEAVE_OFFICE, null));
        List<StaffExchangeStatisticsVO.Trend> initPeriodTrendList = statisticsVO.initTrend(param);
        // 查询一段时间的人员列表
        // 入职
        param.setType(INDUCTION);
        List<StaffExchangeStatisticsVO> inductionPeriods = staffMapper.getPeriodStaffNum(param);
        // 转正
        param.setType(REGULAR_WORKER);
        List<StaffExchangeStatisticsVO> regularWorkerPeriods = staffMapper.getPeriodStaffNum(param);
        // 离职
        param.setType(LEAVE_OFFICE);
        List<StaffExchangeStatisticsVO> leaveOfficePeriods = staffMapper.getPeriodStaffNum(param);
        initPeriodTrendList.forEach(trend -> {
            StaffExchangeStatisticsVO induction = inductionPeriods.stream()
                    .filter(ind -> trend.getCompTime().equals(ind.getInductionDate())).findAny().orElse(null);
            trend.setInductionNum(Objects.isNull(induction) ? 0 : induction.getTotal());
            StaffExchangeStatisticsVO regularWorker = regularWorkerPeriods.stream()
                    .filter(ind -> trend.getCompTime().equals(ind.getRegularWorkerDate())).findAny().orElse(null);
            trend.setRegularWorkerNum(Objects.isNull(regularWorker) ? 0 : regularWorker.getTotal());
            StaffExchangeStatisticsVO leaveOffice = leaveOfficePeriods.stream().filter(ind -> trend.getCompTime().equals(ind.getQuitDate()))
                    .findAny().orElse(null);
            trend.setQuitNum(Objects.isNull(leaveOffice) ? 0 : leaveOffice.getTotal());
        });
        statisticsVO.setExchangeTrend(initPeriodTrendList);
        return statisticsVO;
    }

    /**
     * 计算部门异动数据
     *
     * @param param
     * @param organizationDTOS
     *
     * @return
     */
    private List<StaffChangeStatVO> countChangeOrganizations(StaffQueryParam param, List<OrganizationDTO> organizationDTOS) {
        List<StaffChangeStatVO> staffChangeStatVOS = new ArrayList<>();
        List<ApprovalDataVO> statisticsRecruitGroupByDept = flowFormDataQueryService.getStatisticsRecruitGroupByDept(ObjectUtils.copying(param, FlowFormDataQueryParam.class));
        organizationDTOS.stream().forEach(o -> {
            StaffChangeStatVO statVO = new StaffChangeStatVO();
            statVO.setOrganizationName(o.getName());
            ApprovalDataVO approvalDataVO = statisticsRecruitGroupByDept.stream().filter(re -> String.valueOf(o.getId()).equals(re.getId()))
                    .findAny().orElse(null);
            statVO.setRecruitNum(approvalDataVO == null ? 0 : approvalDataVO.getNum());
            statVO.setInductionNum(this.getStaffNum(param.getPeriod(), INDUCTION, o.getId()));
            statVO.setRegularWorkerNum(this.getStaffNum(param.getPeriod(), REGULAR_WORKER, o.getId()));
            statVO.setQuitNum(this.getStaffNum(param.getPeriod(), LEAVE_OFFICE, o.getId()));
            statVO.setCurrentNum(this.getStaffNum(null, "", o.getId()));
            staffChangeStatVOS.add(statVO);
        });
        return staffChangeStatVOS;
    }

    /**
     * 查询员工数量
     *
     * @param period 时间段
     * @param type   入职、转正、离职 ；如果为空则查询除离职外的总人数
     * @param deptId 部门ID
     *
     * @return
     */
    private long getStaffNum(Period period, String type, Long deptId) {
        boolean periodFlag = false;
        LocalDateTime begin = null;
        LocalDateTime end = null;
        if (period != null) {
            begin = period.getBegin();
            end = period.getEnd();
            periodFlag = (begin != null && end != null);
        }
        return this.lambdaQuery()
                .between(INDUCTION.equals(type) && periodFlag, StaffPO::getInductionDate, begin, end)
                .between(REGULAR_WORKER.equals(type) && periodFlag, StaffPO::getRegularWorkerDate, begin, end)
                .between(LEAVE_OFFICE.equals(type) && periodFlag, StaffPO::getQuitDate, begin, end)
                .isNotNull(INDUCTION.equals(type) && !periodFlag, StaffPO::getInductionDate)
                .isNotNull(REGULAR_WORKER.equals(type) && !periodFlag, StaffPO::getRegularWorkerDate)
                .isNotNull(LEAVE_OFFICE.equals(type) && !periodFlag, StaffPO::getQuitDate)
                .eq(null != deptId, StaffPO::getOrganizationId, deptId)
                .and(StringUtils.isEmpty(type), q -> q
                        .ne(StaffPO::getStatus, StaffStatusEnum.LEAVE_OFFICE.name())
                        .or()
                        .isNull(StaffPO::getStatus)
                    )
                .count();
    }

    /**
     * 员工根据在岗状态分页查询
     *
     * @param param
     *
     * @return
     */
    public Pagination<StaffStateVO> getStatePage(StaffQueryParam param) {
        Page<StaffStateVO> page = staffMapper.getPageByState(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        Set<Long> userIds = page.getRecords().stream().map(StaffStateVO::getUserId).collect(Collectors.toSet());
        Map<Long, Boolean> sensitiveConfig = this.getSensitiveConfig(userIds);
        Map<String, List<StaffDetailVO.Position>> postitionMap = this.getPositions(page.getRecords().stream()
                                                                                           .filter(t -> StringUtils.isNotBlank(t.getPositionIds()))
                                                                                           .map(StaffStateVO::getPositionIds)
                                                                                           .collect(Collectors.toList()));
        page.getRecords().forEach(vo -> {
            if (!CollectionUtils.isEmpty(postitionMap.get(vo.getPositionIds()))) {
                vo.setPostNames(postitionMap.get(vo.getPositionIds()).stream().map(StaffDetailVO.Position::getName)
                                        .collect(Collectors.joining(",")));
            }
            vo.setMobile(this.getSensitive(vo.getMobile(), sensitiveConfig.get(vo.getId())));
        });
        return PagingUtils.convert(page);
    }

    /**
     * 适用于通讯录按照姓名的首字母排序
     *
     * @param param
     *
     * @return
     */
    public List<StaffStateVO> getListByABC(StaffQueryParam param) {
        List<StaffStateVO> list = staffMapper.getPageByABC(param, SessionContextHolder.getRequester().getTenant());
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        Set<Long> resultUserIds = list.stream().map(StaffStateVO::getUserId).collect(Collectors.toSet());
        //获取加密配置
        Map<Long, Boolean> sensitiveConfig = this.getSensitiveConfig(resultUserIds);
        list.forEach(t -> t.setMobile(getSensitive(t.getMobile(), sensitiveConfig.get(t.getId()))));
        return list;
    }

    /**
     * 查询在岗、离岗人数
     *
     * @return 在岗、离岗人数
     */
    public List<KeyValueVO> getStateTotal() {
        List<KeyValueVO> data = new ArrayList<>();
        long onDuty = staffMapper.selectCount(new LambdaQueryWrapper<StaffPO>()
                                                      .eq(StaffPO::getState, WorkingStatus.WORKING.name()));
        long total = staffMapper.selectCount(new LambdaQueryWrapper<StaffPO>());
        data.add(new KeyValueVO(WorkingStatus.WORKING.name(), onDuty));
        data.add(new KeyValueVO(WorkingStatus.LEAVE_POST.name(), total - onDuty));
        data.add(new KeyValueVO("TOTAL", total));

        return data;
    }

    /**
     * 查询员工信息中需要用到的枚举列表信息
     *
     * @param type
     *
     * @return
     */
    public List<Map<String, String>> getEnumList(String type) {
        switch (type) {
            case EDU:
                return EducationEnum.getList();
            case POLITICAL:
                return PoliticalStatusEnum.getList();
            case STAFF_TYPE:
                return staffTypeCommandService.getStaffTypeMap();
            case STAFF_STATUS:
                return StaffStatusEnum.getList();
            case PROBATION_PERIOD:
                return ProbationPeriodEnum.getList();
            case GENDER:
                return GenderEnum.getList();
            case MARITAL_STATUS:
                return MaritalStatusEnum.getList();
        }
        return null;
    }

    /**
     * 根据员工ID查询员工字段属性信息
     *
     * @param id   员工ID
     * @param attr 属性
     *
     * @return
     */
    public Map<String, Object> getInfoByStaffId(String id, String attr) {
        Map<String, Object> result = new HashMap<>();
        try {
            StaffDetailVO staffPO = this.get(Long.parseLong(id));
            Class<? extends StaffDetailVO> aClass = staffPO.getClass();
            Field field = aClass.getDeclaredField(attr);
            field.setAccessible(true);
            Object fieldVal = field.get(staffPO);
            result.put("name", fieldVal);
            // 处理日期格式
            Class<?> type = field.getType();
            String typeName = type.getName();
            if ("java.time.LocalDateTime".equals(typeName)) {
                var date = (LocalDateTime) fieldVal;
                result.put("name", DateUtils.format(date, "yyyy-MM-dd"));
            }
            // 部门和职位的时候需要ID
            if (ORGANIZATION_ID.equals(attr)) {
                result.put("id", fieldVal);
                result.put("name", staffPO.getOrganizationName());
            } else if (APPOINTMENT_ID.equals(attr)) {
                result.put("id", fieldVal);
                result.put("name", staffPO.getAppointmentName());
            } else if (POSITION_IDS.equals(attr)) {
                result.put("id", staffPO.getPositionIds());
                result.put("name", staffPO.getPostNames());
            } else {
                result.putAll(this.pareAttrEnum(attr, fieldVal));
            }
        } catch (Exception e) {
            log.error("获取员工字段信息失败，员工ID：" + id + "，属性：" + attr, e);
            result.clear();
        }
        return result;
    }

    /**
     * 解析属性为枚举的
     *
     * @param attr
     * @param fieldVal
     *
     * @return
     */
    private Map<String, Object> pareAttrEnum(String attr, Object fieldVal) {
        Map<String, Object> map = new HashMap<>();
        if (fieldVal == null) {
            return map;
        }
        switch (attr) {
            case EDUCATION:
                map.put("id", fieldVal);
                map.put("name", EducationEnum.valueOf(fieldVal.toString()).getValue());
                break;
            case POLITICAL_STATUS:
                map.put("id", fieldVal);
                map.put("name", PoliticalStatusEnum.valueOf(fieldVal.toString()).getValue());
                break;
            case TYPE:
                map.put("id", fieldVal);
                map.put("name", staffTypeCommandService.valueOfType(fieldVal.toString()));
                break;
            case STATUS:
                map.put("id", fieldVal);
                map.put("name", StaffStatusEnum.valueOf(fieldVal.toString()).getValue());
                break;
            case PROBATION_PERIOD:
                map.put("id", fieldVal);
                map.put("name", ProbationPeriodEnum.valueOf(fieldVal.toString()).getValue());
                break;
            case GENDER:
                map.put("id", fieldVal);
                map.put("name", GenderEnum.valueOf(fieldVal.toString()).getValue());
                break;
        }
        return map;
    }

    /**
     * 扩展员工信息 （部门、职位、岗位）
     *
     * @param detailVOList
     * @param phoneEncryption
     */
    private void extend(List<StaffDetailVO> detailVOList, Boolean phoneEncryption) {

        Set<Long> organizationIds = new HashSet<>();
        Set<String> positionIds = new HashSet<>();
        Set<String> appointIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        Set<Long> staffIdList = new HashSet<>();
        Set<String> areaIds = new HashSet<>();
        for (StaffDetailVO staffDetailVO : detailVOList) {
            staffIdList.add(staffDetailVO.getId());
            userIds.add(staffDetailVO.getUserId());
            if (Objects.nonNull(staffDetailVO.getOrganizationId())) {
                organizationIds.add(staffDetailVO.getOrganizationId());
            }
            if (StringUtils.isNotBlank(staffDetailVO.getPositionIds())) {
                positionIds.add(staffDetailVO.getPositionIds());
            }
            if (StringUtils.isNotBlank(staffDetailVO.getAppointmentId())) {
                appointIds.add(staffDetailVO.getAppointmentId());
            }
            if (StringUtils.isNotBlank(staffDetailVO.getAreaIds())) {
                areaIds.add(staffDetailVO.getAreaIds());
                staffDetailVO.setAreaIdList(Arrays.stream(staffDetailVO.getAreaIds()
                                                                  .split(",")).map(Long::valueOf).collect(Collectors.toList()));
            }
        }
        Map<Long, StaffExtendPO> staffExtendPOMap = Optional.ofNullable(staffExtendCommandService
                                                                                .lambdaQuery().in(StaffExtendPO::getId, staffIdList)
                                                                                .list()).orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(StaffExtendPO::getId, Function.identity()));
        Map<Long, Boolean> sensitiveConfig;
        if (phoneEncryption) {
            //获取加密配置
            sensitiveConfig = this.getSensitiveConfig(userIds);
        } else {
            sensitiveConfig = new HashMap<>();
        }
        Map<String, String> areaNameMap = this.getAreaName(areaIds);
        Map<Long, String> organizationNameMap = this.getOrganizationName(organizationIds);
        Map<String, List<StaffDetailVO.Position>> positionMap = this.getPositions(positionIds);
        Map<String, String> appointmentNameMap = this.getAppointmentName(appointIds);
        Map<String, String> staffTypeMap = staffTypeCommandService.getStaffTypeByVOList(detailVOList, "type");
        detailVOList.forEach(detailVO -> {
            detailVO.setTypeName(staffTypeMap.get(detailVO.getType()));
            // 扩展部门名称
            detailVO.setOrganizationName(organizationNameMap.get(detailVO.getOrganizationId()));
            // 扩展岗位
            detailVO.setPositions(positionMap.get(detailVO.getPositionIds()));
            // 扩展职位
            detailVO.setAppointmentName(appointmentNameMap.get(detailVO.getAppointmentId()));
            //扩展表信息
            detailVO.setCornet(staffExtendPOMap.getOrDefault(detailVO.getId(), new StaffExtendPO()).getCornet());
            detailVO.setAreaNameList(areaNameMap.get(detailVO.getAreaIds()));
            if (ObjectUtils.isNotEmpty(sensitiveConfig.get(detailVO.getId())) && sensitiveConfig.get(detailVO.getId()) &&
                ObjectUtils.isNotEmpty(detailVO.getMobile())) {
                try {
                    String mobile = DesensitizeUtils.desensitize(detailVO.getMobile(), DesensitizeUtils.DESENSITIZE_MODE_MOBILE);
                    detailVO.setMobile(mobile);
                } catch (Exception e) {
                    log.error("staff:员工脱敏失败", e);
                }
            }
        });
    }

    /**
     * 获取区域信息
     *
     * @param areaIds
     *
     * @return
     */
    private Map<String, String> getAreaName(Set<String> areaIds) {
        if (ObjectUtils.isEmpty(areaIds)) {
            return Collections.emptyMap();
        }
        List<Long> areaIdList = areaIds.stream().map(this::splitStringToIdList).flatMap(Collection::stream)
                .distinct().collect(Collectors.toList());
        List<AreaDTO> areaDTOList = areaClient.getByIds(areaIdList);
        if (CollectionUtils.isEmpty(areaDTOList)) {
            return Collections.emptyMap();
        }
        return areaIds.stream().collect(Collectors.toMap(k -> k, v -> {
            Set<Long> longs = splitStringToIdList(v);
            return areaDTOList.stream().filter(areaDTO -> longs.contains(areaDTO.getId()))
                    .map(AreaDTO::getName).collect(Collectors.joining("/"));
        }, (k1, k2) -> k1));
    }

    /**
     * 字符串拆分
     *
     * @param splitString
     *
     * @return
     */
    public Set<Long> splitStringToIdList(String splitString) {
        if (StringUtils.isNotBlank(splitString)) {
            String[] split = splitString.split(",");
            return Arrays.stream(split).map(Long::parseLong).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public Set<String> splitStringToStringList(String splitString) {
        if (StringUtils.isNotBlank(splitString)) {
            String[] split = splitString.split(",");
            return Arrays.stream(split).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * 批量获取部门信息
     *
     * @param idList
     *
     * @return
     */
    private Map<Long, String> getOrganizationName(Collection<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new HashMap<>();
        }
        List<OrganizationDTO> organizationsByIds = null;
        try {
            organizationsByIds = organizationClient.getOrganizationsByIds(new ArrayList<>(idList));
        } catch (Exception e) {
            log.error("扩展花名册员工部门失败，部门ID：" + idList);
        }
        return !CollectionUtils.isEmpty(organizationsByIds) ? organizationsByIds.stream()
                .collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName))
                                                            : new HashMap<>();
    }

    /**
     * 批量查询岗位信息
     *
     * @param idsList ids集合
     *
     * @return
     */
    private Map<String, String> getAppointmentName(Collection<String> idsList) {
        if (ObjectUtils.isEmpty(idsList)) {
            return Collections.emptyMap();
        }
        List<String> postIdList = idsList.stream().map(this::splitStringToStringList).flatMap(Collection::stream)
                .distinct().collect(Collectors.toList());
        List<AppointmentDetailDTO> appointmentList = appointmentClient.getAppointmentByIds(String.join(",", postIdList));
        if (CollectionUtils.isEmpty(appointmentList)) {
            return Collections.emptyMap();
        }
        return idsList.stream().collect(Collectors.toMap(k -> k, v -> {
            Set<Long> longs = splitStringToIdList(v);
            return appointmentList.stream().filter(appointment -> longs.contains(appointment.getId()))
                    .map(AppointmentDetailDTO::getName).collect(Collectors.joining("、"));
        }, (k1, k2) -> k1));
    }

    /**
     * 批量查询职位
     *
     * @param postIds 职位ids集合
     *
     * @return
     */
    private Map<String, List<StaffDetailVO.Position>> getPositions(Collection<String> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }
        List<Long> postIdList = postIds.stream().map(this::splitStringToIdList).flatMap(Collection::stream)
                .distinct().collect(Collectors.toList());
        List<PositionDTO> positions = positionClient.getPositions(postIdList);
        if (CollectionUtils.isEmpty(positions)) {
            return Collections.emptyMap();
        }

        return postIds.stream().collect(Collectors.toMap(k -> k, v -> {
            Set<Long> longs = splitStringToIdList(v);
            return positions.stream().filter(positionDTO -> longs.contains(positionDTO.getId()))
                    .map(k -> ObjectUtils.copying(k, StaffDetailVO.Position.class)).collect(Collectors.toList());
        }, (k1, k2) -> k1));

    }

    /**
     * 获取脱敏配置
     */
    public Map<Long, Boolean> getSensitiveConfig(Set<Long> userIds) {
        Boolean phoneEncryption = false;
        Map<Long, Boolean> configMap = new HashMap<>();
        try {
            String encryption = ServletUtils.getCurrentRequest().getHeader("encryption");
            if (ObjectUtils.isNotEmpty(encryption) && !Boolean.parseBoolean(encryption)) {
                return configMap;
            }
            DataDictionaryaramParam dictionaryaramParam = new DataDictionaryaramParam();
            dictionaryaramParam.setItemCode("SERVICE_CONFIGURATION");
            dictionaryaramParam.setCode("SERVICE_CONFIGURATION");
            List<DataDictionaryDTO> dictionarys = dataDictionaryAdapter.getDataDictionaryByItem(dictionaryaramParam);
            if (ObjectUtils.isNotEmpty(dictionarys)) {
                String variable = dictionarys.get(0).getVariable();
                JSONObject json = JSON.parseObject(variable);
                phoneEncryption = (Boolean) json.get("phone_encryption");
            }
            if (phoneEncryption) {
                for (Long userId : userIds) {
                    configMap.put(userId, true);
                }
            } else {
                UserDictionaryParam userDictionaryParam = new UserDictionaryParam();
                userDictionaryParam.setItemCode("SERVICE_CONFIGURATION");
                userDictionaryParam.setCode("SERVICE_CONFIGURATION");
                userDictionaryParam.setUserIds(userIds);
                List<UserDictionaryDTO> userDictionaryDTOList = userDictionaryAdapter.getUserDictionaryList(userDictionaryParam);
                for (UserDictionaryDTO userDictionaryDTO : userDictionaryDTOList) {
                    boolean flag = StringUtils.isNotEmpty(userDictionaryDTO.getName()) &&
                                   Boolean.TRUE.toString().equals(userDictionaryDTO.getName());
                    configMap.put(userDictionaryDTO.getUserId(), flag);
                }
            }
        } catch (Exception e) {
            log.error("staff:获取脱敏配置失败", e);
        }
        return configMap;
    }

    /**
     * 手机号脱敏
     */
    private String getSensitive(String phone, Boolean phoneEncryption) {
        if (ObjectUtils.isNotEmpty(phoneEncryption) && phoneEncryption && ObjectUtils.isNotEmpty(phone)) {
            try {
                return DesensitizeUtils.desensitize(phone, DesensitizeUtils.DESENSITIZE_MODE_MOBILE);
            } catch (Exception e) {
                log.error("staff:员工脱敏失败", e);
            }
        }
        return phone;
    }

    public HomePageStatisticsVO getHomePageStatistics(BaseDateStatisticsParam param) {
        HomePageStatisticsVO statisticsVO = new HomePageStatisticsVO();
        // 查询员工列表
        long total = this.lambdaQuery()
                .lt(StaffPO::getInductionDate, param.getPeriod().getEnd())
                .ne(StaffPO::getStatus, StaffStatusEnum.LEAVE_OFFICE.name())
                .or()
                .isNull(StaffPO::getStatus)
                .count();
        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getPeriod());
        long lastNum = this.lambdaQuery()
                .lt(StaffPO::getInductionDate, previousPeriod.getEnd())
                .ne(StaffPO::getStatus, StaffStatusEnum.LEAVE_OFFICE.name())
                .or()
                .isNull(StaffPO::getStatus)
                .count();
        statisticsVO.setTotal(total);
        statisticsVO.setChinRatio(MathUtils.calculateRingRatio(total, lastNum));
        long formalCount = this.lambdaQuery()
                .eq(StaffPO::getStatus, StaffStatusEnum.FORMAL.name())
                .count();
        statisticsVO.setFormalCount(formalCount);

        long trialCount = this.lambdaQuery()
                .eq(StaffPO::getStatus, StaffStatusEnum.ON_TRIAL.name())
                .count();
        statisticsVO.setTrialCount(trialCount);
        statisticsVO.setEntryCount(this.getStaffNum(param.getPeriod(), INDUCTION, null));
        statisticsVO.setFormalizedCount(this.getStaffNum(param.getPeriod(), REGULAR_WORKER, null));
        statisticsVO.setLeaveCount(this.getStaffNum(param.getPeriod(), LEAVE_OFFICE, null));
        return statisticsVO;
    }
}
