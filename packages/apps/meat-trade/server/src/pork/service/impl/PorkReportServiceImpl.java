package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.client.param.SupplierInsertParam;
import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import com.guhecloud.cube.biz.meattrade.server.base.dao.AbattoirMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.notify.MeattradeMsgNotifyUtils;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkReportMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Track;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeAuditModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkReportModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkReportQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SupplierTallyInsertParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PratiqueRegisterService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.TrackService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkReportStatAuditVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkSupplyStatisticsVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.guhecloud.platform.base.util.DateUtils.PURE_DATE_FORMAT;

/**
 * @author lzy
 * @date 2021/12/4
 */
@Slf4j
@Service("meattrade-porkReportService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkReportServiceImpl extends ServiceImpl<PorkReportMapper, PorkReport> implements PorkReportService {

    private final PratiqueRegisterService pratiqueRegisterService;
    private final SpotCheckService spotCheckService;
    private final PorkArrangeService porkArrangeService;
    private final PorkArrangeAuditService porkArrangeAuditService;
    private final TrackService trackService;

    private final SupplierClient supplierClient;
    private final FileRefClient fileRefClient;

    private final RedisTemplate redisTemplate;
    private final MeattradeMsgNotifyUtils meattradeMsgNotifyUtils;
    private final AbattoirMapper abattoirMapper;

    private final static String MEATTRADE_PROK_REPORT_ADOPT = "MEATTRADE_PROK_REPORT_ADOPT";
    private final static String MEATTRADE_PROK_REPORT_FAIL_ADOPT = "MEATTRADE_PROK_REPORT_FAIL_ADOPT";

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "新增来货报备", fail = "新增来货报备失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "PORK_REPORT", subTypeName = "来货报备",
               operationType = OperationType.ADD,
               extra = "{{#param}}",
               bizNo = ""
    )
    public PorkReport addPorkReport(@NonNull PorkReportModifyParam param) {
        // 获取批发商信息
        SupplierDTO supplier = null;
        if (Objects.isNull(param.getSupplierId())) {
            Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
            supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        } else {
            supplier = this.supplierClient.getSupplierById(param.getSupplierId());
        }
        Assert.isNotFound(supplier, "不存在批发商信息!");
        // 构建来货报备信息
        PorkReport porkReport = ObjectUtils.copying(param, PorkReport.class);
        porkReport.setSupplierId(supplier.getId());
        porkReport.setCode(this.getCode(LocalDateTime.now()));
        porkReport.setPratiqueCode(String.join(",", param.getPratiqueCodes()));
        porkReport.setStatus(PorkReportEnum.AUDIT);
        porkReport.setGoodsCategoryId(param.getGoodsCategoryIds().stream().collect(Collectors.joining("-")));
        porkReport.setTenant(supplier.getTenant());
        this.baseMapper.insert(porkReport);

        // 新增文件信息
        if (!CollectionUtils.isEmpty(param.getFileIds())) {
            List<FileRefCreateParam> saveFiles = param.getFileIds().stream().map(fileId -> {
                FileRefCreateParam save = new FileRefCreateParam();
                save.setFileId(fileId);
                save.setBusinessKey(porkReport.getId());
                return save;
            }).collect(Collectors.toList());
            this.fileRefClient.saveBatch(saveFiles);
        }
        // 新增供货商品信息
        this.addTrackInfo(porkReport.getId(), IdWorker.getId(), porkReport.getSupplierId(), porkReport.getTenant());
        return porkReport;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSupplierAndPorkArrange(SupplierTallyInsertParam param) {
        // 市场租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<SupplierDTO> suppliers = getSupplierDTOS(param);
        SupplierDTO supplierDTO = suppliers.get(0);
        if ((StringUtils.isNotEmpty(param.getMidName()) || StringUtils.isNotEmpty(param.getMidPhone())) &&
            supplierDTO.getCode().equals(supplierDTO.getCode())) {
            SupplierInsertParam supplierInsertParam = getSupplierInsertParam(param);
            this.supplierClient.updateSupplier(supplierDTO.getId(), supplierInsertParam);
        }
        Long abattoirId = null;
        if (StringUtils.isNotBlank(param.getMeatFarmName())) {
            List<Abattoir> abattoirList = this.abattoirMapper.selectList(Wrappers.<Abattoir>lambdaQuery()
                                                                                 .eq(Abattoir::getName, param.getMeatFarmName()));
            if (CollectionUtils.isEmpty(abattoirList)) {
                Abattoir abattoir = new Abattoir();
                abattoir.setName(param.getMeatFarmName());
                abattoir.setAbbr(param.getMeatFarmName());
                abattoir.setEnabled(Boolean.TRUE);
                abattoir.setCode(IdWorker.getIdStr());
                this.abattoirMapper.insert(abattoir);
                abattoirId = abattoir.getId();
            } else {
                abattoirId = abattoirList.get(0).getId();
            }
        }
        PorkReport porkReport = this.getByCode(param.getTallyId(), tenant);
        //批次号不存在则创建批次号
        if (Objects.isNull(porkReport)) {
            addPorkReport(abattoirId, param, supplierDTO, tenant);
        } else {
            //查询该报备是否理货完成
            PorkArrange porkArrange = this.porkArrangeService.getByPorkReport(porkReport.getId(), porkReport.getTenant());
            Assert.isNotAcceptable(PorkArrangeEnum.FINISHED == porkArrange.getStatus(), "该批次已理货完成,请勿重复操作");
        }
    }

    private void addPorkReport(Long abattoirId, SupplierTallyInsertParam param, SupplierDTO supplierDTO, Integer tenant) {
        RedissonRedLock lock = RedisUtils.getDistributedLock().tryLock(param.getTallyId(), Expiration.seconds(5));
        try {
            if (Objects.nonNull(lock)) {
                PorkReport porkReport = this.getByCode(param.getTallyId(), tenant);
                if (Objects.nonNull(porkReport)) {
                    return;
                }
                porkReport = buildPorkReport(abattoirId, param, supplierDTO, tenant);
                this.save(porkReport);
                // 构建来货报备信息
                // 新增理货数据
                PorkArrange porkArrange = new PorkArrange();
                //模拟生成一个报备id
                porkArrange.setPorkReportId(porkReport.getId());
                porkArrange.setPorkReportCode(porkReport.getCode());
                porkArrange.setPlace(param.getDeviceId());
                porkArrange.setStatus(PorkArrangeEnum.WAITED);
                porkArrange.setTenant(SessionContextHolder.getRequester().getTenant());
                this.porkArrangeService.save(porkArrange);
                // 新增理货审核数据
                PorkArrangeAudit porkArrangeAudit = buildPorkArrangeAudit(porkArrange);
                this.porkArrangeAuditService.save(porkArrangeAudit);
            }
        } catch (Exception e) {
            log.error("新增报备数据失败:{}", e.getMessage());
        } finally {
            if (Objects.nonNull(lock)) {
                lock.unlock();
            }
        }
    }

    private PorkReport buildPorkReport(Long abattoirId, SupplierTallyInsertParam param, SupplierDTO supplierDTO, Integer tenant) {
        return PorkReport.builder()
                .code(param.getTallyId())
                .abattoirId(abattoirId)
                .supplierId(supplierDTO.getId())
                .storeName(param.getStoreName())
                .reportSlices(Objects.isNull(param.getReportedCount()) ? 0 : param.getReportedCount())
                .reportWeight(Objects.isNull(param.getReportedWeight()) ? new BigDecimal(0) : param.getReportedWeight())
                .reportTime(LocalDateTime.now())
                .arrivedTime(LocalDateTime.now())
                .status(PorkReportEnum.PASSED)
                .goodsCategoryId(param.getGoodsCategoryId())
                .goodsCategoryName(param.getGoodsCategoryName())
                .freshLabel(param.getFreshLabel())
                .auditorId(1L)
                .auditTime(LocalDateTime.now())
                .tenant(tenant)
                .build();
    }

    private SupplierInsertParam getSupplierInsertParam(SupplierTallyInsertParam param) {
        SupplierInsertParam supplierInsertParam = new SupplierInsertParam();
        supplierInsertParam.setName(param.getMidName());
        supplierInsertParam.setAdminPhone(param.getMidCode());
        supplierInsertParam.setLiaison(param.getMidName());
        supplierInsertParam.setPhone(StringUtils.isNotBlank(param.getMidPhone()) ? param.getMidPhone() : param.getMidCode());
        supplierInsertParam.setAreaIds(Collections.singletonList("000000"));
        supplierInsertParam.setAddress("");
        supplierInsertParam.setRemark("");
        supplierInsertParam.setCategoryIds(Collections.singletonList("000000000000000"));
        supplierInsertParam.setRiskAmount(0);
        supplierInsertParam.setRiskLevel(RiskLevelEnum.LOW);
        return supplierInsertParam;
    }

    private List<SupplierDTO> getSupplierDTOS(SupplierTallyInsertParam param) {
        // 获取批发商信息
        List<SupplierDTO> suppliers = this.supplierClient.getByMidType("phone", param.getMidCode());
        //批发商不存在则创建
        if (CollectionUtils.isEmpty(suppliers)) {
            suppliers = Collections.singletonList(addSupplier(param));
        }
        return suppliers;
    }

    private SupplierDTO addSupplier(SupplierTallyInsertParam param) {
        RedissonRedLock lock = RedisUtils.getDistributedLock().tryLock(param.getMidCode(), Expiration.seconds(5));
        try {
            if (Objects.nonNull(lock)) {
                // 获取批发商信息
                List<SupplierDTO> suppliers = this.supplierClient.getByMidType("phone", param.getMidCode());
                if (!CollectionUtils.isEmpty(suppliers)) {
                    return suppliers.get(0);
                }
                SupplierInsertParam supplierInsertParam = SupplierInsertParam.builder()
                        .name(StringUtils.isNotEmpty(param.getMidName()) ? param.getMidName() : param.getMidCode())
                        .adminPhone(param.getMidCode())
                        .liaison(param.getMidName())
                        .phone(StringUtils.isNotBlank(param.getMidPhone()) ? param.getMidPhone() : param.getMidCode())
                        .areaIds(Collections.singletonList("000000"))
                        .address(param.getAreaName())
                        .remark("")
                        .categoryIds(Collections.singletonList("000000000000000"))
                        .riskAmount(0)
                        .riskLevel(RiskLevelEnum.LOW)
                        .build();
                Long supplierId = this.supplierClient.addSupplierAndAuthentication(supplierInsertParam);
                SupplierDTO supplierDTO = new SupplierDTO();
                supplierDTO.setId(supplierId);
                supplierDTO.setCode(param.getMidCode());
                supplierDTO.setName(supplierInsertParam.getName());
                return supplierDTO;
            }
        } catch (Exception e) {
            log.error("新增商户数据失败:{}", e.getMessage());
        } finally {
            if (Objects.nonNull(lock)) {
                lock.unlock();
            }
        }
        return null;
    }

    private PorkArrangeAudit buildPorkArrangeAudit(PorkArrange porkArrange) {
        PorkArrangeAudit porkArrangeAudit = new PorkArrangeAudit();
        porkArrangeAudit.setPorkReportId(porkArrange.getPorkReportId());
        porkArrangeAudit.setPorkReportCode(porkArrange.getPorkReportCode());
        porkArrangeAudit.setPratiqueRegisterId(IdWorker.getId());
        porkArrangeAudit.setSpotCheckId(IdWorker.getId());
        porkArrangeAudit.setPorkArrangeId(porkArrange.getId());
        porkArrangeAudit.setAuditTime(LocalDateTime.now());
        porkArrangeAudit.setAuditStatus(PorkArrangeAuditEnum.PASSED);
        porkArrangeAudit.setTenant(SessionContextHolder.getRequester().getTenant());
        return porkArrangeAudit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditPorkReport(@NonNull Long id, @NonNull PorkReportEnum status, String auditRemark) {
        PorkReport porkReport = this.baseMapper.selectById(id);
        Assert.isNotFound(porkReport, "不存在来货报备信息!");
        Assert.isNotAcceptable(porkReport.getStatus() != PorkReportEnum.AUDIT, "报备状态不是为待审核!");

        SupplierDTO supplier = this.supplierClient.getSupplierById(porkReport.getSupplierId());
        Assert.isNotFound(supplier, "不存在批发商信息!");

        if (PorkReportEnum.FAILED == status) {
            porkReport.setAuditRemark(auditRemark);
        }
        porkReport.setStatus(status);
        porkReport.setAuditorId(SecurityContextHolder.getPrincipal().getId());
        porkReport.setAuditTime(LocalDateTime.now());

        this.baseMapper.updateById(porkReport);

        // 发送消息
        this.sendMsg(porkReport, supplier);

        if (PorkReportEnum.PASSED == status) {
            try {
                // 新增登记检疫票、食安抽检、理货审核信息
                Long pratiqueRegisterId = this.addPratiqueRegister(porkReport.getId());
                Long spotCheckId = this.addSpotCheck(porkReport.getId());

                // 获取供货商品跟进信息
                Track track = this.trackService.getByPorkReport(porkReport.getId(), porkReport.getTenant());
                if (Objects.isNull(track)) {
                    // 如果不存在供货商品跟进信息，则新增一条对应的供货商品信息
                    Long porkArrangeAuditId = IdWorker.getId();
                    this.addPorkArrangeInfo(porkArrangeAuditId, porkReport.getId(), pratiqueRegisterId, spotCheckId);
                    this.addTrackInfo(porkReport.getId(), porkArrangeAuditId, porkReport.getSupplierId(), porkReport.getTenant());
                } else {
                    // 存在供货商品跟进信息，则沿用之前新增报备信息时产生的理货审核信息的主键
                    this.addPorkArrangeInfo(track.getPorkArrangeAuditId(), porkReport.getId(), pratiqueRegisterId, spotCheckId);
                }
            } catch (DuplicateKeyException e) {
                // 不做处理
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rescindPorkReport(@NonNull Long id) {
        PorkReport porkReport = this.baseMapper.selectById(id);
        Assert.isNotFound(porkReport, "不存在来货报备信息!");
        Assert.isNotAcceptable(PorkReportEnum.AUDIT != porkReport.getStatus(), "当前报备信息状态不是待审核,无法撤销!");

        porkReport.setStatus(PorkReportEnum.RESCINDED);
        this.baseMapper.updateById(porkReport);
    }

    @Override
    public PorkReport getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public PorkReport getByCode(@NonNull Serializable code, Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<PorkReport>lambdaQuery()
                                                 .eq(PorkReport::getCode, code)
                                                 .eq(Objects.nonNull(tenant), PorkReport::getTenant, tenant));
    }

    @Override
    public List<PorkReport> listByIds(@NonNull Collection<? extends Serializable> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.baseMapper.selectBatchIds(ids);
    }

    @Override
    public List<PorkReport> listByCodes(@NonNull Collection<? extends Serializable> codes, Integer tenant) {
        return CollectionUtils.isEmpty(codes) ? Collections.emptyList() :
               this.baseMapper.selectList(Wrappers.<PorkReport>lambdaQuery()
                                                  .eq(PorkReport::getTenant, tenant)
                                                  .in(PorkReport::getCode, codes));
    }

    @Override
    public List<PorkReport> listByAbattoir(@NonNull Serializable abattoirId, @NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<PorkReport>lambdaQuery()
                                                  .eq(PorkReport::getAbattoirId, abattoirId)
                                                  .eq(PorkReport::getTenant, tenant));
    }

    @Override
    public List<PorkReport> listPorkReportBySupplier(@NonNull Serializable supplierId, Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<PorkReport>lambdaQuery()
                                                  .eq(PorkReport::getSupplierId, supplierId)
                                                  .eq(Objects.nonNull(tenant), PorkReport::getTenant, tenant));
    }

    @Override
    public List<PorkReport> listPorkReportBySuppliers(@NonNull Collection<? extends Serializable> supplierIds, @NonNull Integer tenant) {
        return CollectionUtils.isEmpty(supplierIds) ? Collections.emptyList() :
               this.lambdaQuery().in(PorkReport::getSupplierId, supplierIds).eq(PorkReport::getTenant, tenant).list();
    }

    @Override
    public Integer getSupplySupplierCount(StatQueryParam queryParam) {
        return this.baseMapper.selectSupplySupplierCount(queryParam);
    }

    @Override
    public PorkSupplyStatisticsVO getSupplyStatistics(StatQueryParam queryParam) {
        return this.baseMapper.selectSupplyStatistics(queryParam);
    }

    @Override
    public List<PorkReport> getStatPorkReports(@NonNull StatQueryParam queryParam) {
        List<PorkReportEnum> enums = Stream.of(PorkReportEnum.PASSED, PorkReportEnum.FAILED).collect(Collectors.toList());
        return this.lambdaQuery()
                .in(PorkReport::getStatus, enums)
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), PorkReport::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), PorkReport::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .eq(PorkReport::getTenant, queryParam.getTenant())
                .list();
    }

    @Override
    public Page<PorkReport> pagePorkReport(@NonNull PorkReportQueryParam queryParam) {
        Period period = queryParam.getPeriod();
        List<PorkReportEnum> porkReportStatus = new ArrayList<>();
        if (Boolean.TRUE.equals(queryParam.getIsAudited())) {
            porkReportStatus.add(PorkReportEnum.PASSED);
            porkReportStatus.add(PorkReportEnum.FAILED);
        } else if (Boolean.FALSE.equals(queryParam.getIsAudited())) {
            porkReportStatus.add(PorkReportEnum.AUDIT);
        }
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()) && !CollectionUtils.isEmpty(queryParam.getSupplierIds()), q -> q
                        .like(PorkReport::getCode, queryParam.getKeyword())
                        .or()
                        .in(PorkReport::getSupplierId, queryParam.getSupplierIds())
                    )
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()) &&
                      CollectionUtils.isEmpty(queryParam.getSupplierIds()), PorkReport::getCode, queryParam.getKeyword())
                .in(!CollectionUtils.isEmpty(queryParam.getSupplierIds()) &&
                    StringUtils.isEmpty(queryParam.getKeyword()), PorkReport::getSupplierId, queryParam.getSupplierIds())
                .eq(Objects.nonNull(queryParam.getStatus()), PorkReport::getStatus, queryParam.getStatus())
                .in(!CollectionUtils.isEmpty(porkReportStatus), PorkReport::getStatus, porkReportStatus)
                .in(!CollectionUtils.isEmpty(queryParam.getIds()), PorkReport::getId, queryParam.getIds())
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), PorkReport::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), PorkReport::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .eq(PorkReport::getTenant, queryParam.getTenant())
                .orderByDesc(PorkReport::getCode)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public Page<PorkReport> pageMyPorkReport(@NonNull PorkReportQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(queryParam.getStatus()), PorkReport::getStatus, queryParam.getStatus())
                .eq(Objects.nonNull(queryParam.getTenant()), PorkReport::getTenant, queryParam.getTenant())
                .in(!CollectionUtils.isEmpty(queryParam.getSupplierIds()), PorkReport::getSupplierId, queryParam.getSupplierIds())
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), PorkReport::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), PorkReport::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .orderByDesc(BasicEntity::getCreateTime)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public PorkReportStatAuditVO getSupplierReportAudit(Long supplierId, Integer tenant) {
        return this.baseMapper.selectSupplierReportAudit(supplierId, tenant);
    }

    private Long addPorkArrangeInfo(Long id, Long porkReportId, Long pratiquerRegisterId, Long spotCheckId) {
        // 新增理货数据
        PorkArrangeModifyParam porkArrangeModifyParam = new PorkArrangeModifyParam();
        porkArrangeModifyParam.setPorkReportId(porkReportId);
        Long porkArrangeId = this.porkArrangeService.addPorkArrange(porkArrangeModifyParam);

        // 新增理货审核数据
        PorkArrangeAuditModifyParam porkArrangeAuditModifyParam = new PorkArrangeAuditModifyParam();
        porkArrangeAuditModifyParam.setPorkReportId(porkReportId);
        porkArrangeAuditModifyParam.setPratiqueRegisterId(pratiquerRegisterId);
        porkArrangeAuditModifyParam.setSpotCheckId(spotCheckId);
        porkArrangeAuditModifyParam.setPorkArrangeId(porkArrangeId);

        return this.porkArrangeAuditService.addPorkArrangeAudit(id, porkArrangeAuditModifyParam);
    }

    private Long addPratiqueRegister(Long porkReportId) {
        PratiqueRegisterModifyParam saveParam = new PratiqueRegisterModifyParam();
        saveParam.setPorkReportId(porkReportId);
        return this.pratiqueRegisterService.addPratiqueRegister(saveParam);
    }

    private Long addSpotCheck(Long porkReportId) {
        SpotCheckModifyParam param = new SpotCheckModifyParam();
        param.setPorkReportId(porkReportId);
        return this.spotCheckService.addSpotCheck(param);
    }

    private void addTrackInfo(Long porkReportId, Long porkArrangeAuditId, Long supplierId, Integer tenant) {
        Track track = new Track();
        track.setPorkReportId(porkReportId);
        track.setPorkArrangeAuditId(porkArrangeAuditId);
        track.setSupplierId(supplierId);
        track.setTenant(tenant);
        this.trackService.save(track);
    }

    private String getCode(LocalDateTime today) {
        String key = DateUtils.format(today, PURE_DATE_FORMAT);
        Long id = null;
        if (redisTemplate.getConnectionFactory() != null) {
            RedisAtomicLong atomicLong = new RedisAtomicLong("sat:" + key, redisTemplate.getConnectionFactory());
            id = atomicLong.getAndIncrement();
        }
        return key + String.format("%04d", id);
    }

    private void sendMsg(PorkReport porkReport, SupplierDTO supplier) {
        String sceneCode = "";
        try {
            if (porkReport.getStatus() == PorkReportEnum.PASSED) {
                sceneCode = MEATTRADE_PROK_REPORT_ADOPT;
            } else if (porkReport.getStatus() == PorkReportEnum.FAILED) {
                sceneCode = MEATTRADE_PROK_REPORT_FAIL_ADOPT;
            }
            Map<String, String> map = new HashMap<>();
            map.put("supplierName", supplier.getName());
            map.put("reportCode", porkReport.getCode());
            this.meattradeMsgNotifyUtils.supplierNotify(sceneCode, porkReport.getId().toString(), supplier, map);
        } catch (Exception e) {
            log.error("报备通知失败,场景编号为:{},失败原因:{}", sceneCode, e);
        }
    }
}
