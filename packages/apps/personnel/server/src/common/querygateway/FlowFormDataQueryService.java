package com.guhecloud.cube.srv.gen.personnel.management.server.common.querygateway;

import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.AppointmentClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.PositionClient;
import com.guhecloud.cube.biz.ouaa.client.dto.AppointmentDetailDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.PositionDTO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel.AssessExcelVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel.InductionExcelVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel.LeaveOfficeExcelVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel.RecruitExcelVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel.RegularWorkerExcelVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel.RotationPostExcelVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.RecruitPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.dao.FlowFormDataMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.interfaces.vo.ApprovalDataVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.po.FlowFormDataPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.querygateway.query.FlowFormDataQueryParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.ExcelBeanUtil;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.PageResponse;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.ExcelUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2023/02/17
 * @description 公共业务表查询
 */
@Service("personnelFlowFormDataQueryService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FlowFormDataQueryService extends ServiceImpl<FlowFormDataMapper, FlowFormDataPO> {

    private final FlowFormDataMapper flowFormDataMapper;
    private final PositionClient positionClient;
    private final OrganizationClient organizationClient;
    private final AppointmentClient appointmentClient;

    /**
     * 招聘统计分页
     *
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsRecruitPage(FlowFormDataQueryParam param) {
        List<FlowFormDataPO> flowFormDataPOS = flowFormDataMapper.getRecruitList(param);
        List<RecruitPO> recruitPOS = this.getRecruitByFlowDatas(flowFormDataPOS);
        List<ApprovalDataVO> dataVOS = this.getStatisticsRecruitList(recruitPOS);
        dataVOS.sort(Comparator.comparing(ApprovalDataVO::getNum).reversed());
        Pagination<ApprovalDataVO> pagination = PageResponse.doPage(dataVOS, param.getPageNumber(), param.getPageSize());
        this.buildNameInfo(pagination.getRecords());
        return pagination;
    }

    /**
     * 查询一段时间内每个部门的招聘人数
     *
     * @param param
     *
     * @return
     */
    public List<ApprovalDataVO> getStatisticsRecruitGroupByDept(FlowFormDataQueryParam param) {
        List<ApprovalDataVO> dataVOS = new ArrayList<>();
        List<FlowFormDataPO> flowFormDataPOS = flowFormDataMapper.getRecruitList(param);
        flowFormDataPOS.stream().filter(t -> Objects.nonNull(t.getApplicantOrganizationId()))
                .collect(Collectors.groupingBy(FlowFormDataPO::getApplicantOrganizationId)).forEach((organizationId, pos) -> {
                    ApprovalDataVO vo = new ApprovalDataVO();
                    vo.setId(String.valueOf(organizationId));
                    vo.setNum(this.getRecruitByFlowDatas(pos).stream().mapToInt(RecruitPO::getRecruitNum).sum());
                    dataVOS.add(vo);
                });
        return dataVOS;
    }

    /**
     * 招聘统计导出
     *
     * @param param
     * @param request
     * @param response
     */
    public void getStatisticsRecruitExport(FlowFormDataQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<FlowFormDataPO> flowFormDataPOS = flowFormDataMapper.getRecruitList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(flowFormDataPOS), "导出失败，未查询到统计数据");
        List<RecruitPO> recruitPOS = this.getRecruitByFlowDatas(flowFormDataPOS);
        List<ApprovalDataVO> approvalDataVOS = this.getStatisticsRecruitList(recruitPOS);
        approvalDataVOS.sort(Comparator.comparing(ApprovalDataVO::getNum).reversed());
        this.buildNameInfo(approvalDataVOS);
        List<RecruitExcelVO> excelVOS = ObjectUtils.copying(approvalDataVOS, RecruitExcelVO.class);
        // 获取excel表格数据
        try {
            ExcelBeanUtil<RecruitExcelVO> excelBeanUtil = new ExcelBeanUtil<>(RecruitExcelVO.class);
            List<Map<String, String>> excelDatas = excelBeanUtil.getExcelDatas(excelVOS);
            ExcelWriter writer = ExcelUtils.getWriter(excelDatas, false, false, "");
            ExcelUtils.downloadExcel(request, response, "招聘统计", writer);
        } catch (Exception e) {
            log.error("导出招聘统计失败", e);
            throw new ServiceException("导出数据失败，请联系管理员");
        }
    }

    /**
     * 招聘统计详情
     *
     * @param id
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsRecruitDetail(String id, FlowFormDataQueryParam param) {
        List<FlowFormDataPO> flowFormDataPOS = flowFormDataMapper.getRecruitList(param);
        List<ApprovalDataVO> dataVOS = this.getRecruitApprovalByFormData(flowFormDataPOS);
        List<ApprovalDataVO> approvalDataVOS = dataVOS.stream().filter(d -> id.equals(d.getId())).collect(Collectors.toList());
        Pagination<ApprovalDataVO> pagination = PageResponse.doPage(approvalDataVOS, param.getPageNumber(), param.getPageSize());
        pagination.getRecords().stream().forEach(this::extendPostName);
        return pagination;
    }

    /**
     * 入职统计分页
     *
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsInductionPage(FlowFormDataQueryParam param) {
        Page<ApprovalDataVO> statisticsInductionPage = flowFormDataMapper.getStatisticsInductionPage(PagingUtils.convert(param), param);
        // id ->> postId
        if (CollectionUtils.isEmpty(statisticsInductionPage.getRecords())) {
            return new Pagination<>();
        }
        List<ApprovalDataVO> records = statisticsInductionPage.getRecords();
        this.buildNameInfo(records);

        return PagingUtils.convert(statisticsInductionPage);
    }

    private void buildNameInfo(List<ApprovalDataVO> records) {
        Set<Long> ids = new HashSet<>();
        Set<Long> organizationIds = new HashSet<>();
        for (ApprovalDataVO record : records) {
            if (StringUtils.isNotBlank(record.getId())) {
                record.setId(record.getId().replace("\"", "").trim());
            }
            if (StringUtils.isNotBlank(record.getPostId())) {
                String postId = record.getPostId().replace("\"", "").trim();
                record.setPostId(postId);
                ids.addAll(Arrays.stream(postId.split(",")).map(Long::valueOf).distinct().collect(Collectors.toList()));
            }
            if (StringUtils.isNotBlank(record.getOrganizationId())) {
                String organizationId = record.getOrganizationId().replace("\"", "").trim();
                record.setOrganizationId(organizationId);
                organizationIds.add(Long.valueOf(record.getOrganizationId()));
            }
        }
        Map<Long, String> postionMap;
        if (!CollectionUtils.isEmpty(ids)) {
            postionMap = positionClient.getPositions(new ArrayList<>(ids))
                    .stream().collect(Collectors.toMap(PositionDTO::getId, PositionDTO::getName));
        } else {
            postionMap = new HashMap<>();
        }
        Map<String, String> organizationMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(organizationIds)) {
            organizationMap = organizationClient.getOrganizationsByIds(new ArrayList<>(organizationIds))
                    .stream().collect(Collectors.toMap(t -> t.getId().toString(), OrganizationDTO::getName));
        }
        for (ApprovalDataVO record : records) {
            if (StringUtils.isNotBlank(record.getPostId())) {
                List<Long> postionIds = Arrays.stream(record.getPostId().split(",")).map(Long::valueOf).distinct()
                        .collect(Collectors.toList());
                String postionName = postionIds.stream()
                        .filter(postionMap::containsKey)
                        .map(postionMap::get).collect(Collectors.joining(","));
                record.setPostName(postionName);
            }
            record.setOrganizationName(organizationMap.get(record.getOrganizationId()));
        }
    }

    /**
     * 入职统计详情
     *
     * @param id
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsInductionDetail(String id, FlowFormDataQueryParam param) {
        Page<ApprovalDataVO> statisticsInductionDetail = flowFormDataMapper.getStatisticsInductionDetail(PagingUtils.convert(param), id, param);
        if (CollectionUtils.isEmpty(statisticsInductionDetail.getRecords())) {
            return new Pagination<>();
        }
        this.buildNameInfo(statisticsInductionDetail.getRecords());
        return PagingUtils.convert(statisticsInductionDetail);
    }

    /**
     * 入职审批统计导出
     *
     * @param param
     * @param request
     * @param response
     */
    public void getStatisticsInductionExport(FlowFormDataQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ApprovalDataVO> approvalDataVOS = flowFormDataMapper.getStatisticsInductionList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(approvalDataVOS), "导出失败，未查询到统计数据");
        this.buildNameInfo(approvalDataVOS);
        List<InductionExcelVO> excelVOS = ObjectUtils.copying(approvalDataVOS, InductionExcelVO.class);
        // 获取excel表格数据
        try {
            ExcelBeanUtil<InductionExcelVO> excelBeanUtil = new ExcelBeanUtil<>(InductionExcelVO.class);
            List<Map<String, String>> excelDatas = excelBeanUtil.getExcelDatas(excelVOS);
            ExcelWriter writer = ExcelUtils.getWriter(excelDatas, false, false, "");
            ExcelUtils.downloadExcel(request, response, "入职统计", writer);
        } catch (Exception e) {
            log.error("导出入职统计失败", e);
            throw new ServiceException("导出数据失败，请联系管理员");
        }
    }

    /**
     * 考核审批统计分页
     *
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsAssessPage(FlowFormDataQueryParam param) {
        Page<ApprovalDataVO> statisticsAssessPage = flowFormDataMapper.getStatisticsAssessPage(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(statisticsAssessPage.getRecords())) {
            return new Pagination<>();
        }
        List<ApprovalDataVO> records = statisticsAssessPage.getRecords();
        this.buildAppointmentInfo(records);
        return PagingUtils.convert(statisticsAssessPage);
    }

    private void buildAppointmentInfo(List<ApprovalDataVO> records) {
        Set<String> appointmentIds = new HashSet<>();
        for (ApprovalDataVO record : records) {
            if (StringUtils.isNotBlank(record.getId())) {
                record.setId(record.getId().replace("\"", "").trim());
            }
            if (StringUtils.isNotBlank(record.getPostId())) {
                record.setPostId(record.getPostId().replace("\"", "").trim());
                appointmentIds.add(record.getPostId());
            }
        }
        if (CollectionUtils.isEmpty(appointmentIds)) {
            return;
        }
        List<AppointmentDetailDTO> appointmentByIds = appointmentClient.getAppointmentByIds(String.join(",", appointmentIds));
        Map<String, String> appointmentMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(appointmentByIds)) {
            appointmentMap = appointmentByIds.stream().collect(Collectors.toMap(t -> t.getId().toString(), AppointmentDetailDTO::getName));
        }
        for (ApprovalDataVO record : records) {
            record.setPostName(appointmentMap.get(record.getId()));
            ;
        }
    }

    /**
     * 考核审批统计详情
     *
     * @param id
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsAssessDetail(String id, FlowFormDataQueryParam param) {
        return PagingUtils.convert(flowFormDataMapper.getStatisticsAssessDetail(PagingUtils.convert(param), id, param));
    }

    /**
     * 考核审批统计导出
     *
     * @param param
     * @param request
     * @param response
     */
    public void getStatisticsAssessExport(FlowFormDataQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ApprovalDataVO> approvalDataVOS = flowFormDataMapper.getStatisticsAssessList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(approvalDataVOS), "导出失败，未查询到统计数据");
        this.buildAppointmentInfo(approvalDataVOS);
        List<AssessExcelVO> excelVOS = ObjectUtils.copying(approvalDataVOS, AssessExcelVO.class);
        // 获取excel表格数据
        try {
            ExcelBeanUtil<AssessExcelVO> excelBeanUtil = new ExcelBeanUtil<>(AssessExcelVO.class);
            List<Map<String, String>> excelDatas = excelBeanUtil.getExcelDatas(excelVOS);
            ExcelWriter writer = ExcelUtils.getWriter(excelDatas, false, false, "");
            ExcelUtils.downloadExcel(request, response, "考核统计", writer);
        } catch (Exception e) {
            log.error("导出考核统计失败", e);
            throw new ServiceException("导出数据失败，请联系管理员");
        }
    }

    /**
     * 转正申请统计分页
     *
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsRegularWorkerPage(FlowFormDataQueryParam param) {
        Pagination<ApprovalDataVO> pagination = PagingUtils.convert(flowFormDataMapper.getStatisticsRegularWorkerPage(PagingUtils.convert(param), param));
        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return new Pagination<>();
        }
        this.buildNameInfo(pagination.getRecords());
        return pagination;
    }

    /**
     * 转正申请统计详情
     *
     * @param id
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsRegularWorkerDetail(String id, FlowFormDataQueryParam param) {
        Pagination<ApprovalDataVO> workerDetail = PagingUtils.convert(flowFormDataMapper.getStatisticsRegularWorkerDetail(PagingUtils.convert(param), id, param));
        if (CollectionUtils.isEmpty(workerDetail.getRecords())) {
            return new Pagination<>();
        }
        this.buildNameInfo(workerDetail.getRecords());
        return workerDetail;
    }

    /**
     * 转正申请统计导出
     *
     * @param param
     * @param request
     * @param response
     */
    public void getStatisticsRegularWorkerExport(FlowFormDataQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ApprovalDataVO> approvalDataVOS = flowFormDataMapper.getStatisticsRegularWorkerList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(approvalDataVOS), "导出失败，未查询到统计数据");
        this.buildNameInfo(approvalDataVOS);
        List<RegularWorkerExcelVO> excelVOS = ObjectUtils.copying(approvalDataVOS, RegularWorkerExcelVO.class);
        // 获取excel表格数据
        try {
            ExcelBeanUtil<RegularWorkerExcelVO> excelBeanUtil = new ExcelBeanUtil<>(RegularWorkerExcelVO.class);
            List<Map<String, String>> excelDatas = excelBeanUtil.getExcelDatas(excelVOS);
            ExcelWriter writer = ExcelUtils.getWriter(excelDatas, false, false, "");
            ExcelUtils.downloadExcel(request, response, "转正统计", writer);
        } catch (Exception e) {
            log.error("导出转正统计失败", e);
            throw new ServiceException("导出数据失败，请联系管理员");
        }
    }

    /**
     * 离职审批统计分页
     *
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsLeaveOfficePage(FlowFormDataQueryParam param) {
        Page<ApprovalDataVO> statisticsLeaveOfficePage = flowFormDataMapper.getStatisticsLeaveOfficePage(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(statisticsLeaveOfficePage.getRecords())) {
            return new Pagination<>();
        }
        this.buildNameInfo(statisticsLeaveOfficePage.getRecords());
        return PagingUtils.convert(statisticsLeaveOfficePage);
    }

    /**
     * 离职审批统计详情
     *
     * @param id
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsLeaveOfficeDetail(String id, FlowFormDataQueryParam param) {
        Page<ApprovalDataVO> statisticsLeaveOfficeDetail = flowFormDataMapper.getStatisticsLeaveOfficeDetail(PagingUtils.convert(param), id, param);
        if (CollectionUtils.isEmpty(statisticsLeaveOfficeDetail.getRecords())) {
            return new Pagination<>();
        }
        this.buildNameInfo(statisticsLeaveOfficeDetail.getRecords());
        return PagingUtils.convert(statisticsLeaveOfficeDetail);
    }

    /**
     * 离职审批统计导出
     *
     * @param param
     * @param request
     * @param response
     */
    public void getStatisticsLeaveOfficeExport(FlowFormDataQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ApprovalDataVO> approvalDataVOS = flowFormDataMapper.getStatisticsLeaveOfficeList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(approvalDataVOS), "导出失败，未查询到统计数据");
        this.buildNameInfo(approvalDataVOS);
        List<LeaveOfficeExcelVO> excelVOS = ObjectUtils.copying(approvalDataVOS, LeaveOfficeExcelVO.class);
        // 获取excel表格数据
        try {
            ExcelBeanUtil<LeaveOfficeExcelVO> excelBeanUtil = new ExcelBeanUtil<>(LeaveOfficeExcelVO.class);
            List<Map<String, String>> excelDatas = excelBeanUtil.getExcelDatas(excelVOS);
            ExcelWriter writer = ExcelUtils.getWriter(excelDatas, false, false, "");
            ExcelUtils.downloadExcel(request, response, "离职统计", writer);
        } catch (Exception e) {
            log.error("导出离职统计失败", e);
            throw new ServiceException("导出数据失败，请联系管理员");
        }
    }

    /**
     * 轮岗申请统计分页
     *
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsRotationPostPage(FlowFormDataQueryParam param) {
        Pagination<ApprovalDataVO> pagination = PagingUtils.convert(flowFormDataMapper.getStatisticsRotationPostPage(PagingUtils.convert(param), param));
        pagination.getRecords().forEach(ex -> {
            OrganizationDTO organization = organizationClient.getOrganizationsById(Long.parseLong(ex.getId()));
            if (ObjectUtils.isNotEmpty(organization)) {
                ex.setRotationOrganizationName(organization.getName());
            }
        });
        return pagination;
    }

    /**
     * 轮岗申请统计详情
     *
     * @param id
     * @param param
     *
     * @return
     */
    public Pagination<ApprovalDataVO> getStatisticsRotationPostDetail(String id, FlowFormDataQueryParam param) {
        Pagination<ApprovalDataVO> pagination = PagingUtils.convert(flowFormDataMapper.getStatisticsRotationPostDetail(PagingUtils.convert(param), id, param));
        pagination.getRecords().stream().forEach(ex -> {
            OrganizationDTO organization;
            try {
                organization = organizationClient.getOrganizationsById(ex.getRotationOrganizationId());

            } catch (Exception exception) {
                log.error("获取轮岗组织失败", exception);
                organization = null;
            }
            if (organization != null) {
                ex.setRotationOrganizationName(organization.getName());
            }
            if (ObjectUtils.isNotEmpty(ex.getOrganizationId())) {
                try {
                    organization = organizationClient.getOrganizationsById(Long.valueOf(ex.getOrganizationId()));
                } catch (Exception exception) {
                    log.error("获取轮岗组织失败", exception);
                    organization = null;
                }
                if (ObjectUtils.isNotEmpty(organization)) {
                    if (organization != null) {
                        ex.setOrganizationName(organization.getName());
                    }
                }
            }
        });
        return pagination;
    }

    /**
     * 轮岗申请统计导出
     *
     * @param param
     * @param request
     * @param response
     */
    public void getStatisticsRotationPostExport(FlowFormDataQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ApprovalDataVO> approvalDataVOS = flowFormDataMapper.getStatisticsRotationPostList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(approvalDataVOS), "导出失败，未查询到统计数据");
        approvalDataVOS.forEach(ex -> {
            OrganizationDTO organization = organizationClient.getOrganizationsById(Long.parseLong(ex.getId()));
            if (ObjectUtils.isNotEmpty(organization)) {
                ex.setRotationOrganizationName(organization.getName());
            }
        });
        List<RotationPostExcelVO> excelVOS = ObjectUtils.copying(approvalDataVOS, RotationPostExcelVO.class);
        // 获取excel表格数据
        try {
            ExcelBeanUtil<RotationPostExcelVO> excelBeanUtil = new ExcelBeanUtil<>(RotationPostExcelVO.class);
            List<Map<String, String>> excelDatas = excelBeanUtil.getExcelDatas(excelVOS);
            ExcelWriter writer = ExcelUtils.getWriter(excelDatas, false, false, "");
            ExcelUtils.downloadExcel(request, response, "轮岗统计", writer);
        } catch (Exception e) {
            log.error("导出轮岗统计失败", e);
            throw new ServiceException("导出数据失败，请联系管理员");
        }
    }

    private List<ApprovalDataVO> getStatisticsRecruitList(List<RecruitPO> recruitPOS) {
        List<ApprovalDataVO> dataVOS = new ArrayList<>();
        recruitPOS.stream().collect(Collectors.groupingBy(RecruitPO::getPostId)).forEach((postId, pos) -> {
            ApprovalDataVO vo = new ApprovalDataVO();
            vo.setId(String.valueOf(postId));
            vo.setPostId(String.valueOf(postId));
            vo.setNum(pos.stream().mapToInt(RecruitPO::getRecruitNum).sum());
            dataVOS.add(vo);
        });
        return dataVOS;
    }

    private List<RecruitPO> getRecruitByFlowDatas(List<FlowFormDataPO> flowFormDataPOS) {
        List<RecruitPO> recruitPOS = new ArrayList<>();
        flowFormDataPOS.forEach(po -> {
            String jsonForm = po.getJsonForm();
            Object parseObject = JSONObject.parseObject(po.getJsonForm(), Object.class);
            if (parseObject instanceof JSONArray) {
                recruitPOS.addAll(JSON.parseArray(jsonForm, RecruitPO.class));
            } else {
                recruitPOS.add(JSONObject.parseObject(jsonForm, RecruitPO.class));
            }
        });
        return recruitPOS;
    }

    private List<ApprovalDataVO> getRecruitApprovalByFormData(List<FlowFormDataPO> flowFormDataPOS) {
        List<ApprovalDataVO> approvalDataVOS = new ArrayList<>();
        flowFormDataPOS.forEach(po -> {
            String jsonForm = po.getJsonForm();
            Object parseObject = JSONObject.parseObject(po.getJsonForm(), Object.class);
            if (parseObject instanceof JSONArray) {
                List<RecruitPO> recruitPOList = JSON.parseArray(jsonForm, RecruitPO.class);
                recruitPOList.forEach(recruitPO -> {
                    ApprovalDataVO dataVO = ObjectUtils.copying(po, ApprovalDataVO.class);
                    dataVO.setNum(recruitPO.getRecruitNum());
                    dataVO.setId(String.valueOf(recruitPO.getPostId()));
                    dataVO.setPostId(String.valueOf(recruitPO.getPostId()));
                    approvalDataVOS.add(dataVO);
                });
            } else {
                RecruitPO recruitPO = JSONObject.parseObject(jsonForm, RecruitPO.class);
                ApprovalDataVO dataVO = ObjectUtils.copying(po, ApprovalDataVO.class);
                dataVO.setNum(recruitPO.getRecruitNum());
                dataVO.setId(String.valueOf(recruitPO.getPostId()));
                dataVO.setPostId(String.valueOf(recruitPO.getPostId()));
                approvalDataVOS.add(dataVO);
            }
        });
        return approvalDataVOS;
    }

    private void extendPostName(ApprovalDataVO approvalDataVO) {
        if (StringUtils.isNotEmpty(approvalDataVO.getPostId())) {
            String[] split = approvalDataVO.getPostId().split(",");
            List<Long> postIds = Arrays.asList(split).parallelStream().map(Long::parseLong).collect(Collectors.toList());
            List<PositionDTO> positions = positionClient.getPositions(postIds);
            approvalDataVO.setPostName(positions.stream().map(PositionDTO::getName).collect(Collectors.joining(",")));
        }
    }

}
