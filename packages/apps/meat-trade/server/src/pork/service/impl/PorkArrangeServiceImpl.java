package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkReportMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TallyQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("meattrade-porkArrangeService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangeServiceImpl extends ServiceImpl<PorkArrangeMapper, PorkArrange> implements PorkArrangeService {
    private final PorkArrangeMapper porkArrangeMapper;
    private final PorkReportMapper porkReportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addPorkArrange(@NonNull PorkArrangeModifyParam param) {
        PorkReport porkReport = this.porkReportMapper.selectById(param.getPorkReportId());
        Assert.isNotFound(porkReport, "不存在报备信息!");
        PorkArrange porkArrange = this.getByPorkReport(param.getPorkReportId(), porkReport.getTenant());
        if (Objects.isNull(porkArrange)) {
            // 构建信息
            porkArrange = ObjectUtils.copying(param, PorkArrange.class);
            porkArrange.setPorkReportCode(porkReport.getCode());
            porkArrange.setStatus(PorkArrangeEnum.WAITED);
            porkArrange.setTenant(SessionContextHolder.getRequester().getTenant());
            this.baseMapper.insert(porkArrange);
        }
        return porkArrange.getId();
    }

    @Override
    public PorkArrange getByPorkReport(@NonNull Long porkReportId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<PorkArrange>lambdaQuery()
                                                 .eq(PorkArrange::getTenant, tenant)
                                                 .eq(PorkArrange::getPorkReportId, porkReportId));
    }

    @Override
    public List<PorkArrange> listByPorkReportIds(@NonNull Collection<? extends Serializable> porkReportIds, @NonNull Integer tenant) {
        return CollectionUtils.isEmpty(porkReportIds) ? Collections.emptyList() :
               this.baseMapper.selectList(Wrappers.<PorkArrange>lambdaQuery()
                                                  .in(PorkArrange::getPorkReportId, porkReportIds)
                                                  .eq(PorkArrange::getTenant, tenant));
    }

    @Override
    public List<PorkArrange> listByPorkReportCodes(@NonNull Collection<? extends Serializable> porkReportCodes, Integer tenant) {
        return CollectionUtils.isEmpty(porkReportCodes) ? Collections.emptyList() :
               this.baseMapper.selectList(Wrappers.<PorkArrange>lambdaQuery()
                                                  .eq(PorkArrange::getTenant, tenant)
                                                  .in(PorkArrange::getPorkReportCode, porkReportCodes));
    }

    @Override
    public List<PorkArrange> listByComplex(StatQueryParam queryParam) {
        Period period = queryParam.getPeriod();
        return porkArrangeMapper.listByComplex(Wrappers.query()
                                                       .eq("pa.audit_status", PorkArrangeAuditEnum.PASSED)
                                                       .eq("pa.tenant", queryParam.getTenant())
                                                       .between(Objects.nonNull(period), "pa.audit_time", ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd())));
    }

    @Override
    public List<PorkArrange> getPendingTallies(@NonNull TallyQueryParam queryParam,
                                               @NonNull Collection<? extends Serializable> porkReportIds
                                              ) {
        List<PorkArrangeEnum> enums =
                Stream.of(PorkArrangeEnum.WAITED, PorkArrangeEnum.PAUSED, PorkArrangeEnum.PROGRESSED).collect(Collectors.toList());
        return this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(queryParam.getDeviceId()), PorkArrange::getPlace, queryParam.getDeviceId())
                .in(PorkArrange::getStatus, enums)
                .in(!CollectionUtils.isEmpty(porkReportIds), PorkArrange::getPorkReportId, porkReportIds)
                .list();
    }

    @Override
    public List<PorkArrange> getTallies(Collection<? extends Serializable> porkReportIds) {
        return this.lambdaQuery()
                .in(!CollectionUtils.isEmpty(porkReportIds), PorkArrange::getPorkReportId, porkReportIds)
                .list();
    }

    @Override
    public Page<PorkArrange> statPage(@NonNull StatQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(PorkArrange::getStatus, PorkArrangeEnum.FINISHED)
                .eq(PorkArrange::getTenant, queryParam.getTenant())
                .in(!CollectionUtils.isEmpty(queryParam.getPorkReportIds()), PorkArrange::getPorkReportId, queryParam.getPorkReportIds())
                .between(Objects.nonNull(queryParam.getPeriod()), PorkArrange::getPorkArrangeTime, queryParam.getPeriod()
                        .getBegin(), queryParam.getPeriod().getEnd())
                .orderByDesc(PorkArrange::getPorkArrangeTime)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public Integer getCheatCount(StatQueryParam queryParam) {
        return this.baseMapper.selectCheatCount(queryParam);
    }

    @Override
    public Integer getArrangeCount(StatQueryParam queryParam) {
        return this.baseMapper.selectArrangeCount(queryParam);
    }

    @Override
    public Page<PorkArrange> pageMpPorkArrange(@NonNull PorkArrangeQueryParam queryParam) {
        List<PorkArrangeEnum> porkArrangeStatus = new ArrayList<>();
        if (Boolean.FALSE.equals(queryParam.getIsCompleted())) {
            porkArrangeStatus.add(PorkArrangeEnum.PAUSED);
            porkArrangeStatus.add(PorkArrangeEnum.WAITED);
            porkArrangeStatus.add(PorkArrangeEnum.PROGRESSED);
        }
        return this.lambdaQuery()
                .eq(Boolean.TRUE.equals(queryParam.getIsCompleted()), PorkArrange::getStatus, PorkArrangeEnum.FINISHED)
                .in(!CollectionUtils.isEmpty(porkArrangeStatus), PorkArrange::getStatus, porkArrangeStatus)
                .in(!CollectionUtils.isEmpty(queryParam.getPorkReportIds()), PorkArrange::getPorkReportId, queryParam.getPorkReportIds())
                .eq(PorkArrange::getTenant, queryParam.getTenant())
                .orderByDesc(Boolean.TRUE.equals(queryParam.getIsCompleted()), PorkArrange::getPorkArrangeTime)
                .orderByDesc(Boolean.TRUE.equals(queryParam.getIsCompleted()), BasicEntity::getCreateTime)
                .page(PagingUtils.convert(queryParam));
    }

    public Page<PorkArrange> pageBimPorkArrange(@NonNull PorkArrangeQueryParam queryParam) {
        return this.lambdaQuery()
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), PorkArrange::getPorkArrangeTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), PorkArrange::getPorkArrangeTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .eq(PorkArrange::getTenant, queryParam.getTenant())
                .isNotNull(PorkArrange::getSlices)
                .page(PagingUtils.convert(queryParam));
    }
}
