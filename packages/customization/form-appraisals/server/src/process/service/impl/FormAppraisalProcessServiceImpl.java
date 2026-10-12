package com.guhecloud.cube.srv.gen.appraisals.server.process.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.dto.StartUserInfo;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.appraisals.server.process.dao.FormAppraisalProcessMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.process.entity.FormAppraisalProcess;
import com.guhecloud.cube.srv.gen.appraisals.server.process.service.FormAppraisalProcessService;
import com.guhecloud.cube.srv.gen.appraisals.server.process.vo.FormAppraisalProcessExportVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 表单考核流程服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalProcessServiceImpl extends ServiceImpl<FormAppraisalProcessMapper, FormAppraisalProcess> implements FormAppraisalProcessService {
    private static final String DATA_MODEL = "formAssessment:formAppraisalProcess";
    private static final int EXPORT_PAGE_SIZE = 1000;
    private static final String EXPORT_NAME = "流程审批记录";

    private final ProcessInstanceClient processInstanceClient;

    @Override
    @DataPermissionSwitch(dataModel = DATA_MODEL)
    public Pagination<ProcessInstanceDTO> allPageBatch(ProcessInstancePageQuery processInstancePageQuery) {
        ProcessInstancePageQuery query = this.buildPermissionQuery(processInstancePageQuery);
        if (query == null) {
            return Pagination.empty();
        }
        return processInstanceClient.allPageBatch(query);
    }

    @Override
    @DataPermissionSwitch(dataModel = DATA_MODEL)
    public void export(ProcessInstancePageQuery processInstancePageQuery, HttpServletResponse response) {
        ProcessInstancePageQuery query = this.buildPermissionQuery(processInstancePageQuery);
        Assert.isNotFound(query == null, "没有流程审批记录,无法导出");

        query.setPageNumber(1);
        query.setPageSize(EXPORT_PAGE_SIZE);
        Pagination<ProcessInstanceDTO> page = processInstanceClient.allPageBatch(query);
        Assert.isNotFound(page == null || CollectionUtils.isEmpty(page.getRecords()), "没有流程审批记录,无法导出");

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");

        ExcelWriter writer = null;
        try {
            String excelName = URLEncoder.encode(EXPORT_NAME, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            writer = EasyExcel.write(response.getOutputStream(), FormAppraisalProcessExportVO.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .build();
            WriteSheet sheet = EasyExcel.writerSheet(EXPORT_NAME).build();
            while (page != null && CollectionUtils.isNotEmpty(page.getRecords())) {
                writer.write(this.toExportRows(page.getRecords()), sheet);
                if (page.getRecords().size() < EXPORT_PAGE_SIZE) {
                    break;
                }
                query.setPageNumber(query.getPageNumber() + 1);
                page = processInstanceClient.allPageBatch(query);
            }
        } catch (IOException e) {
            log.error("表单考核流程审批记录导出失败", e);
            throw new ServiceException("文件下载失败");
        } finally {
            if (writer != null) {
                writer.finish();
            }
        }
    }

    private ProcessInstancePageQuery buildPermissionQuery(ProcessInstancePageQuery processInstancePageQuery) {
        if (processInstancePageQuery == null) {
            return null;
        }
        Set<String> processInstanceIds = this.listProcessInstanceIdsByPermission();
        if (CollectionUtils.isEmpty(processInstanceIds)) {
            return null;
        }
        Set<String> conditionProcessInstanceIds = this.extractProcessInstanceIdsFromQuery(processInstancePageQuery);
        if (CollectionUtils.isNotEmpty(conditionProcessInstanceIds)) {
            processInstanceIds = processInstanceIds.stream()
                    .filter(conditionProcessInstanceIds::contains)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (CollectionUtils.isEmpty(processInstanceIds)) {
                return null;
            }
        }
        ProcessInstancePageQuery query = new ProcessInstancePageQuery();
        BeanUtils.copyProperties(processInstancePageQuery, query);
        query.setProcessInstanceIds(null);
        query.setProcessInstanceIdsList(new ArrayList<>(processInstanceIds));
        return query;
    }

    private Set<String> listProcessInstanceIdsByPermission() {
        List<FormAppraisalProcess> processList = this.lambdaQuery()
                .select(FormAppraisalProcess::getProcessInstanceId)
                .list();
        if (CollectionUtils.isEmpty(processList)) {
            return new LinkedHashSet<>();
        }
        return processList.stream()
                .map(FormAppraisalProcess::getProcessInstanceId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<String> extractProcessInstanceIdsFromQuery(ProcessInstancePageQuery processInstancePageQuery) {
        Set<String> processInstanceIds = new LinkedHashSet<>();
        if (CollectionUtils.isNotEmpty(processInstancePageQuery.getProcessInstanceIdsList())) {
            processInstanceIds.addAll(processInstancePageQuery.getProcessInstanceIdsList().stream()
                                              .filter(Objects::nonNull)
                                              .map(String::trim)
                                              .filter(StringUtils::isNotBlank)
                                              .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        if (StringUtils.isNotBlank(processInstancePageQuery.getProcessInstanceIds())) {
            processInstanceIds.addAll(StringUtils.splitAndIgnoreBlank(processInstancePageQuery.getProcessInstanceIds())
                                              .stream()
                                              .map(String::trim)
                                              .filter(StringUtils::isNotBlank)
                                              .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return processInstanceIds;
    }

    private List<FormAppraisalProcessExportVO> toExportRows(List<ProcessInstanceDTO> records) {
        return records.stream()
                .filter(Objects::nonNull)
                .map(this::toExportRow)
                .toList();
    }

    private FormAppraisalProcessExportVO toExportRow(ProcessInstanceDTO processInstance) {
        String normalizedStatus = this.normalizeStatus(processInstance.getStatus());
        FormAppraisalProcessExportVO vo = new FormAppraisalProcessExportVO();
        vo.setFormNo(Objects.toString(processInstance.getFormNo(), ""));
        vo.setInstanceName(processInstance.getInstanceName());
        vo.setStartUserName(processInstance.getStartUserName());
        vo.setOrganizationName(this.resolveOrganizationName(processInstance));
        vo.setSubmitedTime(processInstance.getSubmitedTime());
        vo.setStatusDesc(this.toStatusDesc(normalizedStatus, processInstance.getStatus()));
        vo.setActiveNodeName(processInstance.getActiveNodeName());
        return vo;
    }

    private String resolveOrganizationName(ProcessInstanceDTO processInstance) {
        if (StringUtils.isNotBlank(processInstance.getOrganizationName())) {
            return processInstance.getOrganizationName();
        }
        StartUserInfo startUserInfo = processInstance.getStartUserInfo();
        return startUserInfo == null ? null : startUserInfo.getOrganizationName();
    }

    private String normalizeStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return FlowResult.ACTIVE.name();
        }
        return FlowResult.PASS.name().equals(status) ? FlowResult.FINISHED.name() : status;
    }

    private String toStatusDesc(String normalizedStatus, String originalStatus) {
        try {
            return FlowResult.valueOf(normalizedStatus).getDesc();
        } catch (IllegalArgumentException e) {
            log.warn("未知流程状态: {}", originalStatus);
            return StringUtils.isNotBlank(originalStatus) ? originalStatus : "";
        }
    }

}
