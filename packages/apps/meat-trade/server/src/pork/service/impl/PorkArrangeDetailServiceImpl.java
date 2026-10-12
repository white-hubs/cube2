package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.FileGenerateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.GoodsWeightConfig;
import com.guhecloud.cube.biz.meattrade.server.base.service.GoodsWeightConfigService;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeDetailMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Tally;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PlaceStatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeCheatModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TallyDataModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TrackDetailQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.rocketmq.message.PorkArrangeDetailMessage;
import com.guhecloud.cube.biz.meattrade.server.pork.rocketmq.message.PorkArrangeMessage;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeCheatService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailLogService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.validation.PorkArrangePreValidation;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkDeviceStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.meattrade.server.constants.PorkFileSceneConstant.PORK_ARRANGE_IMAGE_CODE;

@Slf4j
@Service("meattrade-porkArrangeDetailService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangeDetailServiceImpl extends ServiceImpl<PorkArrangeDetailMapper, PorkArrangeDetail> implements PorkArrangeDetailService {
    private final PorkReportService porkReportService;
    private final PorkArrangeService porkArrangeService;
    private final PorkArrangeDetailMapper porkArrangeDetailMapper;
    private final SupplierClient supplierClient;
    private final GoodsWeightConfigService goodsWeightConfigService;
    private final PorkArrangeCheatService porkArrangeCheatService;
    private final PorkArrangeDetailLogService porkArrangeDetailLogService;
    private final FileClient fileClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTallyData(@NonNull TallyDataModifyParam param) {
        // 获取报备信息
        PorkReport porkReport = this.porkReportService.getByCode(param.getTallyId(), SessionContextHolder.getRequester().getTenant());
        Assert.isNotFound(porkReport, "不存在报备信息!");
        // 获取报备信息的批发商信息
        SupplierDTO supplier = this.supplierClient.getSupplierById(porkReport.getSupplierId());
        Assert.isNotFound(supplier, "不存在批发商信息!");
        // 判断当前批次状态
        PorkArrange porkArrange = this.porkArrangeService.getByPorkReport(porkReport.getId(), porkReport.getTenant());
        Assert.isNotFound(porkArrange, "不存在理货信息!");
        //占位一个接口，用于根据策略进行校验
        //批次理货已结束时不允许重新理货
        PorkArrangePreValidation porkArrangePreValidation = this.arrangeFinishValid(porkArrange);
        porkArrangePreValidation.preValidation();
        //获取商品重量信息
        GoodsWeightConfig goodWeightConfig = this.goodsWeightConfigService.getGoodWeightConfig(porkReport.getGoodsCategoryId(), porkReport.getFreshLabel());
        // 构建信息
        List<PorkArrangeDetail> porkArrangeDetails = param.getDatas().stream()
                .map(data -> buildPorkArrangeDetail(param, data, supplier, porkReport, goodWeightConfig)
                    ).collect(Collectors.toList());
        //修改理货状态
        updatePorkArrangeStatus(porkArrange, porkArrangeDetails, param.getDeviceId(), param.getTerminalNumber(), param.getEventType(),
                                param.getGuid(), param.getStartTime(), param.getEndTime());
        this.porkArrangeService.updateById(porkArrange);
    }

    private PorkArrangePreValidation arrangeFinishValid(PorkArrange porkArrange) {
        //        return () -> Assert.availableResource(PorkArrangeEnum.FINISHED != porkArrange.getStatus(), "该批次已理货完成,请勿重复操作");
        return () -> {};
    }

    private PorkArrangeDetail buildPorkArrangeDetail(TallyDataModifyParam param, Tally data, SupplierDTO supplier, PorkReport porkReport,
                                                     GoodsWeightConfig goodWeightConfig
                                                    ) {
        Long id = IdWorker.getId();
        List<FileVO> fileVOS = preConvertFile(data.getPicPaths());
        return PorkArrangeDetail.builder()
                .id(id)
                .supplierId(supplier.getId())
                .porkReportId(porkReport.getId())
                .porkReportCode(porkReport.getCode())
                .label(data.getRfid1())
                .footMarkCode(data.getRfid2())
                .code(data.getRfid2())
                .weight(data.getWeight())
                .standardWeight(Objects.nonNull(goodWeightConfig) ? BigDecimal.valueOf(goodWeightConfig.getStandardWeight()) : null)
                .porkArrangeTime(DateUtils.parse(data.getTime()))
                .goodsCategoryId(porkReport.getGoodsCategoryId())
                .goodsCategoryName(porkReport.getGoodsCategoryName())
                .freshLabel(porkReport.getFreshLabel())
                .weightCompliant(!Objects.nonNull(goodWeightConfig) ||
                                 BigDecimal.valueOf(goodWeightConfig.getStandardWeight()).compareTo(data.getWeight()) <= 0)
                .place(param.getDeviceId())
                .terminalNumber(param.getTerminalNumber())
                .passed(null)
                .cheat(data.getCheat())//理货嫌疑  正常为false 异常为true
                .cheatUpdated(false)
                .transferNumber(0)
                .transferFiles(fileVOS.stream().map(fileVO -> String.valueOf(fileVO.getId())).collect(Collectors.joining(",")))
                .transferPaths(fileVOS.stream().map(FileVO::getPath).collect(Collectors.joining(",")))
                .transferStatus(false)
                .picPaths(
                        !CollectionUtils.isEmpty(data.getPicPaths()) ? String.join(",", data.getPicPaths()) : null)
                .tenant(SessionContextHolder.getRequester().getTenant())
                .createBy("admin")
                .createTime(LocalDateTime.now())
                .build();
    }

    private List<FileVO> preConvertFile(List<String> picPaths) {
        List<FileVO> fileVOS = new ArrayList<>();
        FileGenerateParam fileGenerateParam = new FileGenerateParam();
        if (CollectionUtils.isEmpty(picPaths)) {
            return fileVOS;
        }
        fileGenerateParam.setSceneCode(PORK_ARRANGE_IMAGE_CODE);
        picPaths.stream().filter(StringUtils::isNotBlank).forEach(picPath -> {
            fileGenerateParam.setFileName(picPath.substring(picPath.lastIndexOf("/") + 1));
            FileVO file = this.fileClient.getUploadDetails(fileGenerateParam);
            fileVOS.add(file);
        });
        return fileVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePorkArrangeDetailCheat(@NonNull PorkArrangeCheatModifyParam param) {
        LambdaUpdateWrapper<PorkArrangeDetail> porkArrangeDetailLambdaUpdateWrapper = Wrappers.<PorkArrangeDetail>lambdaUpdate()
                .set(PorkArrangeDetail::getCheat, param.getCheat())
                .set(PorkArrangeDetail::getCheatUpdated, true)
                .eq(PorkArrangeDetail::getId, param.getPorkArrangeDetailId());
        this.update(porkArrangeDetailLambdaUpdateWrapper);
        this.porkArrangeCheatService.save(param);
    }

    private void updatePorkArrangeStatus(PorkArrange porkArrange,
                                         List<PorkArrangeDetail> porkArrangeDetails,
                                         String deviceId,
                                         String terminalNumber,
                                         Integer eventType,
                                         String guid,
                                         LocalDateTime startTime,
                                         LocalDateTime endTime
                                        ) {
        //理货数据入库
        if (!CollectionUtils.isEmpty(porkArrangeDetails)) {
            // 批量片数新增
            this.baseMapper.insertOrUpdateBatch(porkArrangeDetails);
        }
        //获取重量不达标的数据
        List<PorkArrangeDetail> nonWeightCompliantDetails = porkArrangeDetails.stream()
                .filter(porkArrangeDetail -> !porkArrangeDetail.getWeightCompliant()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(nonWeightCompliantDetails)) {
            this.porkArrangeDetailLogService.savePorkArrangeDetailLog(nonWeightCompliantDetails);
        }
        // 计算对应片数和重量,并更新理货数据
        List<PorkArrangeDetail> arrangeDetails = this.baseMapper.selectList(Wrappers.<PorkArrangeDetail>lambdaQuery()
                                                                                    .eq(PorkArrangeDetail::getPorkReportId, porkArrange.getPorkReportId()));
        porkArrange.setSlices(arrangeDetails.size());
        porkArrange.setWeight(arrangeDetails.stream().map(PorkArrangeDetail::getWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        porkArrange.setPlace(deviceId);
        porkArrange.setTerminalNumber(terminalNumber);

        PorkArrangeMessage porkArrangeMessage = ObjectUtils.copying(porkArrange, PorkArrangeMessage.class);
        //消息体
        porkArrangeMessage.setGuid(guid);
        porkArrangeMessage.setTallyMachineEventType(eventType);
        porkArrangeMessage.setStartTime(startTime);
        porkArrangeMessage.setEndTime(endTime);
        porkArrangeMessage.setPorkArrangeDetailMessages(ObjectUtils.copying(porkArrangeDetails, PorkArrangeDetailMessage.class));
        // 判断当前批次状态
        if (eventType == 0) {
            // 理货开始
            porkArrange.setStatus(PorkArrangeEnum.PROGRESSED);
            porkArrangeMessage.setStatus(PorkArrangeEnum.PROGRESSED);
        } else if (eventType == 2) {
            // 理货暂停
            porkArrange.setStatus(PorkArrangeEnum.PAUSED);
            porkArrangeMessage.setStatus(PorkArrangeEnum.PAUSED);
        } else if (eventType == 3) {
            // 理货结束
            porkArrange.setStatus(PorkArrangeEnum.FINISHED);
            porkArrange.setPorkArrangeTime(LocalDateTime.now());
            porkArrangeMessage.setPorkArrangeDetailMessages(ObjectUtils.copying(arrangeDetails, PorkArrangeDetailMessage.class));
            porkArrangeMessage.setPorkArrangeTime(porkArrange.getPorkArrangeTime());
            porkArrangeMessage.setStatus(PorkArrangeEnum.FINISHED);
        }
        //发送理货开始事件消息
        RocketMQContextHolder.send(PorkArrangeMessage.TOPIC, porkArrangeMessage);
    }

    @Override
    public PorkArrangeDetail getByCode(@NonNull String code, Period period, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<PorkArrangeDetail>lambdaQuery()
                                                 //                .between(Objects.nonNull(period), PorkArrangeDetail::getPorkArrangeTime, period.getBegin(), period.getEnd())
                                                 .eq(PorkArrangeDetail::getTenant, tenant)
                                                 .eq(PorkArrangeDetail::getFootMarkCode, code));
    }

    @Override
    public List<PorkArrangeDetail> getByPorkReportCode(@NonNull Serializable porkReportCode, @NonNull Integer
                                                               tenant
                                                      ) {
        return this.baseMapper.selectList(Wrappers.<PorkArrangeDetail>lambdaQuery()
                                                  .eq(PorkArrangeDetail::getPorkReportCode, porkReportCode)
                                                  .eq(PorkArrangeDetail::getTenant, tenant)
                                                  .orderByDesc(PorkArrangeDetail::getPorkArrangeTime));
    }

    @Override
    public List<PorkArrangeDetail> listByPorkReportCodes(@NonNull Collection<? extends
                                                                 Serializable> porkReportCodes, Integer tenant
                                                        ) {
        return CollectionUtils.isEmpty(porkReportCodes) ? Collections.emptyList() :
               this.lambdaQuery().in(PorkArrangeDetail::getPorkReportCode, porkReportCodes).eq(PorkArrangeDetail::getTenant, tenant).list();
    }

    @Override
    public Page<PorkArrangeDetail> listByPorkReportCodes(@NonNull Collection<? extends
                                                                 Serializable> porkReportCodes, String keyword, Integer tenant, Paging paging
                                                        ) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(keyword), q -> q
                        .like(PorkArrangeDetail::getLabel, keyword)
                        .or()
                        .like(PorkArrangeDetail::getWeight, keyword)
                    )
                .eq(Objects.nonNull(tenant), PorkArrangeDetail::getTenant, tenant)
                .isNull(PorkArrangeDetail::getPrice)
                .in(PorkArrangeDetail::getPorkReportCode, porkReportCodes)
                .page(PagingUtils.convert(paging));
    }

    @Override
    public Page<PorkArrangeDetail> pageByComplex(PorkArrangeQueryParam queryParam) {
        return porkArrangeDetailMapper.pageByComplex(PagingUtils.convert(queryParam), Wrappers.query()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like("pd.label", queryParam.getKeyword())
                        .or()
                        .like("pd.weight", queryParam.getKeyword())
                        .or()
                        .like("pd.goods_category_name", queryParam.getKeyword())
                    )
                .eq(Objects.nonNull(queryParam.getTenant()), "pd.tenant", queryParam.getTenant())
                .eq(Objects.nonNull(queryParam.getSupplierId()), "pd.supplier_id", queryParam.getSupplierId())
                .eq(queryParam.getAuditEnum() != null, "pa.audit_status", queryParam.getAuditEnum())
                .isNull("pd.price"));
    }

    @Override
    public Page<PorkArrangeDetail> pageByTrack(@NonNull TrackDetailQueryParam queryParam) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(PorkArrangeDetail::getLabel, queryParam.getKeyword())
                        .or()
                        .like(PorkArrangeDetail::getFootMarkCode, queryParam.getKeyword())
                    )
                .eq(PorkArrangeDetail::getPorkReportCode, queryParam.getCode())
                .eq(queryParam.getPassed() != null, PorkArrangeDetail::getPassed, queryParam.getPassed())
                .isNotNull(Objects.nonNull(queryParam.getSell()) && Boolean.TRUE.equals(queryParam.getSell()), PorkArrangeDetail::getPrice)
                .isNull(Objects.nonNull(queryParam.getSell()) && Boolean.FALSE.equals(queryParam.getSell()), PorkArrangeDetail::getPrice)
                .eq(Objects.nonNull(queryParam.getResult()), PorkArrangeDetail::getCheat, queryParam.getResult())
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public List<PorkArrangeDetail> listPorkArrangeDetails(@NonNull String code, @NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<PorkArrangeDetail>lambdaQuery()
                                                  .eq(PorkArrangeDetail::getPorkReportCode, code)
                                                  .eq(PorkArrangeDetail::getTenant, tenant));
    }

    @Override
    public List<PorkArrangeDetail> listCheatPorkArrangeDetails(Collection<? extends
                                                                       Serializable> porkReportCodes, Integer tenant
                                                              ) {
        return CollectionUtils.isEmpty(porkReportCodes) ? Collections.emptyList() :
               this.lambdaQuery().in(PorkArrangeDetail::getPorkReportCode, porkReportCodes)
                       .eq(PorkArrangeDetail::getTenant, tenant)
                       .eq(PorkArrangeDetail::getCheat, true)
                       .list();
    }

    @Override
    public Page<PorkArrangeDetail> pageCheatPorkArrangeDetails(@NonNull PorkArrangeQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(PorkArrangeDetail::getCheat, true)
                .eq(PorkArrangeDetail::getTenant, queryParam.getTenant())
                .eq(StringUtils.isNotEmpty(queryParam.getPorkReportCode()), PorkArrangeDetail::getPorkReportCode, queryParam.getPorkReportIds())
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public List<PorkArrangeDetail> listByIds(Collection<? extends Serializable> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.baseMapper.selectBatchIds(ids);
    }

    @Override
    public PorkStatSummaryVO statSupplyMeatInfo(StatQueryParam param) {
        Period period = param.getPeriod();
        return porkArrangeDetailMapper.statSupplyMeatInfo(Wrappers.query()
                                                                  .eq("pa.audit_status", PorkArrangeAuditEnum.PASSED)
                                                                  .eq("pa.tenant", param.getTenant())
                                                                  .eq("pd.tenant", param.getTenant())
                                                                  .between("pa.audit_time", period.getBegin(), period.getEnd()));
    }

    @Override
    public PorkArrangeDetail getLastPorkArrangeDetailByDeviceId(@NonNull String deviceCode) {
        Page<PorkArrangeDetail> porkArrangeDetailPage = this.baseMapper.selectPage(new Page<>(0, 1), Wrappers
                .<PorkArrangeDetail>lambdaQuery()
                .eq(PorkArrangeDetail::getPlace, deviceCode)
                .orderByDesc(PorkArrangeDetail::getCreateTime));
        if (CollectionUtils.isEmpty(porkArrangeDetailPage.getRecords())) {
            return null;
        }
        return porkArrangeDetailPage.getRecords().get(0);
    }

    @Override
    public List<PorkDeviceStatisticsVO> getDevicePorkArrangeStatistics(PlaceStatQueryParam queryParam) {
        return baseMapper.selectPlacePorkArrangeStatistics(queryParam);
    }

    @Override
    public Long getPorkArrangeSlicesStatistics(StatQueryParam queryParam) {
        return baseMapper.selectCount(Wrappers.<PorkArrangeDetail>lambdaQuery()
                                              .eq(Objects.nonNull(queryParam.getTenant()), PorkArrangeDetail::getTenant, queryParam.getTenant())
                                              .between(Objects.nonNull(queryParam.getPeriod()), PorkArrangeDetail::getPorkArrangeTime, queryParam
                                                      .getPeriod().getBegin(), queryParam.getPeriod().getEnd()));
    }
}
