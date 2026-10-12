package com.guhecloud.cube.srv.gen.reportmanagement.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.reportmanagement.server.dao.ReportMapper;
import com.guhecloud.cube.srv.gen.reportmanagement.server.dao.ReportTypeMapper;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.Report;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.ReportType;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportTypeAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportTypeQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.ReportTypeService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportTypeVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportTypeServiceImpl extends ServiceImpl<ReportTypeMapper, ReportType> implements ReportTypeService {

    private final ReportTypeMapper reportTypeMapper;
    private final ReportMapper reportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReportType(ReportTypeAddOrUpdateParam param) {
        Assert.isParameterError(param == null, "汇报类型不能为空！");
        List<ReportType> reportTypes = reportTypeMapper.selectList(new QueryWrapper<>());
        if (!reportTypes.isEmpty()) {
            Set<String> reportTypeNames = reportTypes.stream().map(ReportType::getName).collect(Collectors.toSet());
            Assert.isConflict(reportTypeNames.contains(param.getName()), "汇报类型重复，请重新添加！");
        }
        long id = IdWorker.getId();
        ReportType copying = ObjectUtils.copying(param, ReportType.class);
        copying.setId(id);
        this.save(copying);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReportType(ReportTypeAddOrUpdateParam param) {
        ReportType reportType = reportTypeMapper.selectById(param.getId());
        ObjectUtils.copying(param, reportType);
        reportTypeMapper.updateById(reportType);
    }

    @Override
    public Pagination<ReportTypeVo> getReportTypeInfo(ReportTypeQueryParam param) {
        Page<ReportType> page = this.page(PagingUtils.convert(param), Wrappers.<ReportType>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), ReportType::getName, param.getKeyword())
                .orderByDesc(ReportType::getCreateTime));
        return PagingUtils.convert(page, ReportTypeVo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReportTypeStatus(Long id, Integer status) {
        ReportType reportType = reportTypeMapper.selectById(id);
        reportType.setStatus(status);
        reportTypeMapper.updateById(reportType);
    }

    @Override
    @Transactional
    public void deleteReportType(Long id) {
        List<Report> reportList = reportMapper.selectList(new QueryWrapper<Report>().eq("type_id", id));
        Assert.isNotAcceptable(!reportList.isEmpty(), "该类型下有汇报记录，不能删除！");
        DatabaseContextHolder.softDelete(this, id);
    }
}
