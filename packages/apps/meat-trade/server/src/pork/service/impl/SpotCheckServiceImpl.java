package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Config;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.base.service.ConfigService;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkReportMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.SpotCheckMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.util.PagingUtils;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("meattrade-spotCheckService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpotCheckServiceImpl extends ServiceImpl<SpotCheckMapper, SpotCheck> implements SpotCheckService {

    private final PorkReportMapper porkReportMapper;
    private final SpotCheckDetailService spotCheckDetailService;

    private final ConfigService configService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addSpotCheck(@NonNull SpotCheckModifyParam param) {
        PorkReport porkReport = this.porkReportMapper.selectById(param.getPorkReportId());
        Assert.isNotFound(porkReport, "不存在来货报备信息!");
        SpotCheck spotCheck = this.getByPorkReport(porkReport.getId(), porkReport.getTenant());
        if (Objects.isNull(spotCheck)) {
            // 获取抽检比例
            Config config = this.configService.getByType(ConfigEnum.SPOT_CHECK);
            // 新增食安抽检信息
            spotCheck = ObjectUtils.copying(param, SpotCheck.class);
            spotCheck.setPorkReportCode(porkReport.getCode());
            spotCheck.setStatus(false);
            spotCheck.setTenant(porkReport.getTenant());
            spotCheck.setWatered(SpotCheckEnum.UNSELECTED);
            spotCheck.setClenbuterol(SpotCheckEnum.UNSELECTED);
            spotCheck.setRequiredCheckRatio(Objects.isNull(config) ? null : config.getValue());
            spotCheck.setRequiredCheckSlices(Objects.isNull(config) ? null : (int) Math.ceil(
                    porkReport.getReportSlices() * spotCheck.getRequiredCheckRatio() / 100));
            this.baseMapper.insert(spotCheck);

            // 新增食安抽检项
            this.spotCheckDetailService.addSpotCheckDetailBySpotCheck(spotCheck.getId());
        }
        return spotCheck.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "修改抽检结果", fail = "修改抽检结果失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "SPOT_CHECK", subTypeName = "食安抽检",
               operationType = OperationType.UPDATE,
               extra = "{{#param}}",
               bizNo = "{{#id}}"
    )
    public void updateSpotCheck(@NonNull Long id, @NonNull SpotCheckModifyParam param) {
        SpotCheck spotCheck = this.baseMapper.selectById(id);
        Assert.isNotFound(spotCheck, "不存在食安抽检信息!");
        // 更新食安抽检信息
        ObjectUtils.copying(param, spotCheck);
        spotCheck.setStatus(true);
        spotCheck.setSpotCheckTime(LocalDateTime.now());
        this.baseMapper.updateById(spotCheck);

        // 更新食安抽检项信息
        Set<Long> spotCheckDetailIds = param.getDetails().stream().map(SpotCheckDetail::getId).collect(Collectors.toSet());
        List<SpotCheckDetail> details = this.spotCheckDetailService.listByIds(spotCheckDetailIds);
        Map<Long, List<SpotCheckDetail>> detailMap = param.getDetails().stream().collect(Collectors.groupingBy(SpotCheckDetail::getId));
        details = details.stream().map(detail -> {
            if (detailMap.containsKey(detail.getId())) {
                SpotCheckDetail checkDetail = detailMap.get(detail.getId()).get(0);
                detail.setPassed(checkDetail.getPassed());
            }
            return detail;
        }).collect(Collectors.toList());
        this.spotCheckDetailService.updateBatchById(details);
    }

    @Override
    public SpotCheck getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public SpotCheck getByPorkReport(@NonNull Long porkReportId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<SpotCheck>lambdaQuery()
                                                 .eq(SpotCheck::getTenant, tenant)
                                                 .eq(SpotCheck::getPorkReportId, porkReportId));
    }

    @Override
    public Page<SpotCheck> pageSpotCheck(@NonNull SpotCheckQueryParam queryParam) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(SpotCheck::getPorkReportCode, queryParam.getKeyword())
                        .or()
                        .in(!CollectionUtils.isEmpty(queryParam.getPorkReportIds()), SpotCheck::getPorkReportId, queryParam.getPorkReportIds())
                    )
                .eq(Objects.nonNull(queryParam.getStatus()), SpotCheck::getStatus, queryParam.getStatus())
                .eq(Objects.nonNull(queryParam.getClenbuterol()), SpotCheck::getClenbuterol, queryParam.getClenbuterol())
                .eq(Objects.nonNull(queryParam.getWatered()), SpotCheck::getWatered, queryParam.getWatered())
                .eq(SpotCheck::getTenant, queryParam.getTenant())
                .in(!CollectionUtils.isEmpty(queryParam.getPorkReportIds()), SpotCheck::getPorkReportId, queryParam.getPorkReportIds())
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), SpotCheck::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), SpotCheck::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .orderByDesc(SpotCheck::getPorkReportCode)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public Page<SpotCheckVO> getSpotCheckPage(SpotCheckQueryParam queryParam) {
        return this.baseMapper.selectSpotCheck(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public List<SpotCheck> listSpotCheck(@NonNull SpotCheckQueryParam queryParam) {
        return this.lambdaQuery()
                .in(!CollectionUtils.isEmpty(queryParam.getIds()), SpotCheck::getId, queryParam.getIds())
                .eq(SpotCheck::getTenant, queryParam.getTenant())
                .list();
    }

    @Override
    public List<SpotCheck> listByInspectors(@NonNull Long inspectorId, @NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<SpotCheck>lambdaQuery()
                                                  .eq(SpotCheck::getInspectorId, inspectorId)
                                                  .eq(SpotCheck::getTenant, tenant));
    }

    @Override
    public List<SpotCheck> listByPorkReports(@NonNull Collection<? extends Serializable> porkReportIds, Integer tenant) {
        return CollectionUtils.isEmpty(porkReportIds) ? Collections.emptyList() :
               this.lambdaQuery().in(SpotCheck::getPorkReportId, porkReportIds)
                       .eq(SpotCheck::getTenant, tenant).list();
    }
}
