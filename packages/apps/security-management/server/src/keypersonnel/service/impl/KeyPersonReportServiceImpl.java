package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.dao.KeyPersonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.dao.KeyPersonSupervisionReportMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.entity.KeyPerson;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.entity.KeyPersonSupervisionReport;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade.FileFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonReportPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonReportSaveParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service.KeyPersonReportService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonReportVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyPersonReportServiceImpl extends ServiceImpl<KeyPersonSupervisionReportMapper, KeyPersonSupervisionReport> implements KeyPersonReportService {
    private final KeyPersonMapper keyPersonMapper;
    private final FileFacadeService fileFacadeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Long personId, KeyPersonReportSaveParam param) {
        KeyPerson person = keyPersonMapper.selectById(personId);
        Assert.isNotFound(person, "重点人员不存在");
        Assert.isParameterError(param == null || StringUtils.isBlank(param.getContent()), "上报内容不能为空");
        List<Long> attachmentFileIds = fileFacadeService.normalizeAndValidate(param.getReportAttachmentFileIds());

        KeyPersonSupervisionReport report = new KeyPersonSupervisionReport();
        report.setPersonId(personId);
        report.setContent(param.getContent().trim());
        report.setReportAttachmentFileIds(JsonUtils.serialize(attachmentFileIds));
        report.setReportTime(LocalDateTime.now());
        Principal principal = SecurityContextHolder.getPrincipal(true);
        if (principal != null) {
            report.setReporterUserId(principal.getId());
            report.setReporterName(StringUtils.isNotBlank(principal.getName()) ? principal.getName() : principal.getAccount());
        }
        this.save(report);
        fileFacadeService.induce(FileFacadeService.SCENE_KEY_PERSON_REPORT_ATTACHMENT, report.getId(), attachmentFileIds);
        return report.getId();
    }

    @Override
    public Pagination<KeyPersonReportVO> getReportPage(Long personId, KeyPersonReportPageParam param) {
        KeyPerson person = keyPersonMapper.selectById(personId);
        Assert.isNotFound(person, "重点人员不存在");
        KeyPersonReportPageParam pageParam = param == null ? new KeyPersonReportPageParam() : param;
        Period period = pageParam.getPeriod();
        Page<KeyPersonSupervisionReport> reportPage = this.page(PagingUtils.convert(pageParam), Wrappers.<KeyPersonSupervisionReport>lambdaQuery()
                .eq(KeyPersonSupervisionReport::getPersonId, personId)
                .ge(period != null && period.getBegin() != null, KeyPersonSupervisionReport::getReportTime, ObjectUtils.ifNull(period, Period::getBegin))
                .le(period != null && period.getEnd() != null, KeyPersonSupervisionReport::getReportTime, ObjectUtils.ifNull(period, Period::getEnd))
                .orderByDesc(KeyPersonSupervisionReport::getReportTime));
        Page<KeyPersonReportVO> voPage = new Page<>(reportPage.getCurrent(), reportPage.getSize(), reportPage.getTotal());
        voPage.setRecords(reportPage.getRecords().stream().map(this::toVO).toList());
        return PagingUtils.convert(voPage);
    }

    @Override
    public KeyPersonReportVO getReport(Long id) {
        KeyPersonSupervisionReport report = this.getById(id);
        Assert.isNotFound(report, "上报记录不存在");
        return toVO(report);
    }

    private KeyPersonReportVO toVO(KeyPersonSupervisionReport report) {
        KeyPersonReportVO vo = new KeyPersonReportVO();
        vo.setId(report.getId());
        vo.setPersonId(report.getPersonId());
        vo.setContent(report.getContent());
        vo.setReportTime(report.getReportTime());
        vo.setReporterUserId(report.getReporterUserId());
        vo.setReporterName(report.getReporterName());
        List<Long> fileIds = parseFileIds(report.getReportAttachmentFileIds());
        vo.setReportAttachmentFileIds(fileIds);
        vo.setReportAttachmentFiles(fileFacadeService.getFiles(fileIds));
        return vo;
    }

    private List<Long> parseFileIds(String json) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }
        List<Long> result = JsonUtils.deserialize(json, new TypeReference<List<Long>>() {});
        return result == null ? Collections.emptyList() : result;
    }
}
