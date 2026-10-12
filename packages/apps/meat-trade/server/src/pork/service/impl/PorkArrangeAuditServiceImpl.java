package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mall.client.dto.MeatDTO;
import com.guhecloud.cube.biz.mall.client.rocketmq.message.AddMeatGoodsMessage;
import com.guhecloud.cube.biz.mdata.client.RiskLevelClient;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.RiskLevelDTO;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.client.param.SupplierInsertParam;
import com.guhecloud.cube.biz.meattrade.server.notify.MeattradeMsgNotifyUtils;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeAuditMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeDetailMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkReportMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAuditLog;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeAuditModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeAuditQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditLogService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeAuditVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatArrangeAuditVO;
import com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service("meattrade-porkArrangeAuditService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangeAuditServiceImpl extends ServiceImpl<PorkArrangeAuditMapper, PorkArrangeAudit> implements PorkArrangeAuditService {

    private final PorkArrangeAuditLogService porkArrangeAuditLogService;
    private final PorkArrangeDetailMapper porkArrangeDetailMapper;
    private final PorkReportMapper porkReportMapper;
    private final SupplierClient supplierClient;
    private final RiskLevelClient riskLevelClient;

    private final MeattradeMsgNotifyUtils meattradeMsgNotifyUtils;

    private final static String MEATTRADE_TALLY_FAIL_ADOPT = "MEATTRADE_TALLY_FAIL_ADOPT";
    private final static String MEATTRADE_TALLY_ADOPT = "MEATTRADE_TALLY_ADOPT";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addPorkArrangeAudit(@NonNull Long id, @NonNull PorkArrangeAuditModifyParam param) {
        PorkReport porkReport = this.porkReportMapper.selectById(param.getPorkReportId());
        Assert.isNotFound(porkReport, "不存在报备信息!");
        PorkArrangeAudit porkArrangeAudit = this.getByPorkReport(param.getPorkReportId(), porkReport.getTenant());
        if (Objects.isNull(porkArrangeAudit)) {
            porkArrangeAudit = ObjectUtils.copying(param, PorkArrangeAudit.class);
            porkArrangeAudit.setId(id);
            porkArrangeAudit.setPorkReportCode(porkReport.getCode());
            porkArrangeAudit.setAuditStatus(PorkArrangeAuditEnum.AUDIT);
            porkArrangeAudit.setTenant(SessionContextHolder.getRequester().getTenant());
            this.baseMapper.insert(porkArrangeAudit);
        }
        return porkArrangeAudit.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditPorkArrangeAudit(@NonNull PorkArrangeAudit porkArrangeAudit, @NonNull PorkArrangeAuditEnum status, String remark) {
        // 获取批发商信息
        PorkReport porkReport = this.porkReportMapper.selectById(porkArrangeAudit.getPorkReportId());
        Assert.isNotFound(porkReport, "不存在报备信息!");
        SupplierDTO supplier = this.supplierClient.getSupplierById(porkReport.getSupplierId());
        Assert.isNotFound(supplier, "不存在批发商信息!");

        if (PorkArrangeAuditEnum.FAILED == status) {
            porkArrangeAudit.setRemark(remark);
            supplier.setRiskAmount(supplier.getRiskAmount() + 1);
            // 获取对应风险级别
            RiskLevelDTO riskLevelDTO = this.riskLevelClient.getByValue(supplier.getRiskAmount());
            supplier.setRiskLevel(riskLevelDTO == null ? supplier.getRiskLevel() : riskLevelDTO.getType());
            // 更新批发商信息
            SupplierInsertParam supplierModifyParam = ObjectUtils.copying(supplier, SupplierInsertParam.class);
            this.supplierClient.updateSupplier(supplier.getId(), supplierModifyParam);
            //更改审核状态
            porkArrangeDetailMapper.updateArrangeDetailPassed(porkArrangeAudit.getPorkReportId(), false);
        } else if (PorkArrangeAuditEnum.PASSED == status) {
            //更新理货数据状态，passed = 1
            porkArrangeDetailMapper.updateArrangeDetailPassed(porkArrangeAudit.getPorkReportId(), true);
            //审核通过时 不需要填写理由；为了避免撤销或其他不通过时的理由一直存在，故在通过时将备注置为空
            porkArrangeAudit.setRemark(null);
        }
        porkArrangeAudit.setAuditStatus(status);
        porkArrangeAudit.setAuditTime(LocalDateTime.now());
        porkArrangeAudit.setAuditorId(SecurityContextHolder.getPrincipal().getId());
        this.baseMapper.updateById(porkArrangeAudit);

        //理货审核通过后 发送消息 通知商城添加白条猪
        this.notifyMallAddMeatGoods(porkArrangeAudit, supplier);

        // 发送信息 提醒商户理货完成
        this.sendMsg(porkArrangeAudit, supplier);

        // 新增审核日志
        this.addPorkArrangeLog(porkArrangeAudit, remark);
    }

    /**
     * 增加审核日志
     *
     * @param porkArrangeAudit
     * @param remark
     */
    private void addPorkArrangeLog(PorkArrangeAudit porkArrangeAudit, String remark) {
        PorkArrangeAuditLog porkArrangeAuditLog = PorkArrangeAuditLog.builder()
                .porkArrangeAuditId(porkArrangeAudit.getId())
                .auditorId(SecurityContextHolder.getPrincipal().getId())
                .remark(remark)
                .status(porkArrangeAudit.getAuditStatus())
                .time(LocalDateTime.now())
                .build();
        this.porkArrangeAuditLogService.save(porkArrangeAuditLog);
    }

    /**
     * 理货完成通知商城 添加白条商品
     */
    private void notifyMallAddMeatGoods(PorkArrangeAudit porkArrangeAudit, SupplierDTO supplier) {
        List<PorkArrangeDetail> porkArrangeDetails =
                this.porkArrangeDetailMapper.selectList(Wrappers.<PorkArrangeDetail>lambdaQuery()
                                                                .eq(PorkArrangeDetail::getPorkReportCode, porkArrangeAudit.getPorkReportCode())
                                                                .eq(PorkArrangeDetail::getTenant, porkArrangeAudit.getTenant()));
        if (!CollectionUtils.isEmpty(porkArrangeDetails)) {
            List<MeatDTO> meats = this.setMeatInfo(porkArrangeDetails, supplier);
            AddMeatGoodsMessage message = new AddMeatGoodsMessage();
            message.setMeats(meats);
            RocketMQContextHolder.send(AddMeatGoodsMessage.TOPIC, message);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "撤销理货审核信息", fail = "撤销理货审核信息失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "PORK_ARRANGE", subTypeName = "理货审核信息",
               operationType = OperationType.UPDATE,
               extra = "{{#porkArrangeAudit}}",
               bizNo = "{{#porkArrangeAudit.id}}"
    )
    public void rescindPorkArrangeAudit(@NonNull PorkArrangeAudit porkArrangeAudit) {
        porkArrangeAudit.setAuditStatus(PorkArrangeAuditEnum.AUDIT);
        this.baseMapper.updateById(porkArrangeAudit);
    }

    @Override
    public PorkArrangeAudit getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public PorkArrangeAudit getByPorkReport(@NonNull Long porkReportId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<PorkArrangeAudit>lambdaQuery()
                                                 .eq(PorkArrangeAudit::getPorkReportId, porkReportId)
                                                 .eq(PorkArrangeAudit::getTenant, tenant));
    }

    @Override
    public List<PorkArrangeAudit> listByPorkArranges(@NonNull Collection<? extends Serializable> ids, @NonNull Integer tenant) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(PorkArrangeAudit::getPorkArrangeId, ids).eq(PorkArrangeAudit::getTenant, tenant).list();
    }

    @Override
    public List<PorkArrangeAudit> listByPorkReportIds(@NonNull Collection<? extends Serializable> porkReportIds, @NonNull Integer tenant) {
        return CollectionUtils.isEmpty(porkReportIds) ? Collections.emptyList() :
               this.lambdaQuery().eq(PorkArrangeAudit::getTenant, tenant).in(PorkArrangeAudit::getPorkReportId, porkReportIds).list();
    }

    @Override
    public List<PorkArrangeAudit> listBySpotChecks(@NonNull Collection<? extends Serializable> spotCheckIds, Integer tenant) {
        return CollectionUtils.isEmpty(spotCheckIds) ? Collections.emptyList() :
               this.lambdaQuery().in(PorkArrangeAudit::getSpotCheckId, spotCheckIds).eq(PorkArrangeAudit::getTenant, tenant).list();
    }

    @Override
    public List<PorkArrangeAudit> listByPratiqueRgisters(@NonNull Collection<? extends Serializable> pratiqueRegisterIds, Integer tenant) {
        return CollectionUtils.isEmpty(pratiqueRegisterIds) ? Collections.emptyList() :
               this.lambdaQuery().in(PorkArrangeAudit::getPratiqueRegisterId, pratiqueRegisterIds).eq(PorkArrangeAudit::getTenant, tenant)
                       .list();
    }

    @Override
    public Page<PorkArrangeAuditVO> pagePorkArrangeAudit(@NonNull PorkArrangeAuditQueryParam queryParam) {
        if (Objects.nonNull(queryParam.getAuditPeriod())) {
            queryParam.setStatus(true);
            queryParam.setAuditPeriod(DateUtils.deriveNaturalDays(queryParam.getAuditPeriod()));
        }
        return this.baseMapper.selectPorkArrangeAuditPage(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public PorkStatArrangeAuditVO getSupplierArrangeAudit(@NonNull Long supplierId, @NonNull Integer tenant) {
        return this.baseMapper.selectSupplierArrangeAudit(supplierId, tenant);
    }

    private List<MeatDTO> setMeatInfo(List<PorkArrangeDetail> porkArrangeDetails, SupplierDTO supplier) {
        return porkArrangeDetails.stream().map(porkArrangeDetail -> {
            MeatDTO meat = new MeatDTO();
            meat.setTenant(supplier.getSupplierTenant());
            meat.setMarketTenant(supplier.getTenant());
            meat.setBatchNumber(porkArrangeDetail.getPorkReportCode());
            meat.setMerchantId(supplier.getId());
            meat.setCode(porkArrangeDetail.getLabel());
            meat.setWeight(porkArrangeDetail.getWeight());
            meat.setPorkArrangeDetailId(porkArrangeDetail.getId());
            return meat;
        }).collect(Collectors.toList());
    }

    private void sendMsg(PorkArrangeAudit porkArrangeAudit, SupplierDTO supplier) {
        String sceneCode = "";
        try {
            if (porkArrangeAudit.getAuditStatus() == PorkArrangeAuditEnum.PASSED) {
                sceneCode = MEATTRADE_TALLY_ADOPT;
            } else if (porkArrangeAudit.getAuditStatus() == PorkArrangeAuditEnum.FAILED) {
                sceneCode = MEATTRADE_TALLY_FAIL_ADOPT;
            }
            Map<String, String> map = new HashMap<>();
            map.put("supplierName", supplier.getName());
            map.put("reportCode", porkArrangeAudit.getPorkReportCode());
            this.meattradeMsgNotifyUtils.supplierNotify(sceneCode, porkArrangeAudit.getId().toString(), supplier, map);
        } catch (Exception e) {
            log.error("理货审核通知失败,场景编号为:{},失败原因:{}", sceneCode, e);
        }
    }

}
