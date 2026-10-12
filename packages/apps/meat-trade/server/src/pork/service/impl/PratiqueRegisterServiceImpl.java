package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkReportMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PratiqueRegisterMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PratiqueRegisterService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PratiqueRegisterVO;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.util.PagingUtils;
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
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("meattrade-pratiquerRegisterService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PratiqueRegisterServiceImpl extends ServiceImpl<PratiqueRegisterMapper, PratiqueRegister> implements PratiqueRegisterService {

    private final PorkReportMapper porkReportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addPratiqueRegister(@NonNull PratiqueRegisterModifyParam param) {
        PorkReport porkReport = this.porkReportMapper.selectById(param.getPorkReportId());
        Assert.isNotFound(porkReport, "不存在来货报备信息!");
        PratiqueRegister pratiqueRegister = this.getByPorkReport(porkReport.getId(), porkReport.getTenant());
        if (Objects.isNull(pratiqueRegister)) {
            // 构建检疫票信息并新增
            pratiqueRegister = ObjectUtils.copying(param, PratiqueRegister.class);
            pratiqueRegister.setPorkReportCode(porkReport.getCode());
            pratiqueRegister.setStatus(false);
            pratiqueRegister.setTenant(porkReport.getTenant());
            this.baseMapper.insert(pratiqueRegister);
        }
        return pratiqueRegister.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "修改检疫票", fail = "修改检疫票失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "PRATIQUE_REGISTER", subTypeName = "检疫票",
               operationType = OperationType.UPDATE,
               extra = "{{#param}}",
               bizNo = "{{#id}}"
    )
    public void updatePratiqueRegister(@NonNull Long id, @NonNull PratiqueRegisterModifyParam param) {
        PratiqueRegister pratiqueRegister = this.baseMapper.selectById(id);
        Assert.isNotFound(pratiqueRegister, "不存在登记检疫票信息!");
        // 构建信息
        ObjectUtils.copying(param, pratiqueRegister);
        pratiqueRegister.setRegistrantId(SecurityContextHolder.getPrincipal().getId());
        pratiqueRegister.setPratiqueCode(String.join(",", param.getPratiqueCodes()));
        pratiqueRegister.setStatus(true);
        pratiqueRegister.setRegisterTime(LocalDateTime.now());
        this.baseMapper.updateById(pratiqueRegister);
    }

    @Override
    public PratiqueRegister getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public PratiqueRegister getByPorkReport(@NonNull Long porkReportId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<PratiqueRegister>lambdaQuery()
                                                 .eq(PratiqueRegister::getTenant, tenant)
                                                 .eq(PratiqueRegister::getPorkReportId, porkReportId));
    }

    @Override
    public List<PratiqueRegister> getFailedPratiqueRegister(@NonNull Collection<? extends Serializable> porkReportIds,
                                                            @NonNull Integer tenant
                                                           ) {
        return CollectionUtils.isEmpty(porkReportIds) ? Collections.emptyList() :
               this.baseMapper.getFailedPratoqueRegister(porkReportIds, tenant);
    }

    @Override
    public Page<PratiqueRegister> pageFailedPratiqueRegister(@NonNull Paging paging,
                                                             @NonNull Collection<? extends Serializable> porkReportIds,
                                                             @NonNull Integer tenant
                                                            ) {
        return this.baseMapper.pageFailedPratoqueRegister(PagingUtils.convert(paging), porkReportIds, tenant);
    }

    @Override
    public Page<PratiqueRegisterVO> pagePratiqueRegister(@NonNull PratiqueRegisterQueryParam queryParam) {
        return this.baseMapper.selectPratiqueRegisterPage(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public List<PratiqueRegister> listPratiqueRegister(@NonNull PratiqueRegisterQueryParam queryParam) {
        return this.lambdaQuery()
                .in(!CollectionUtils.isEmpty(queryParam.getIds()), PratiqueRegister::getId, queryParam.getIds())
                .eq(PratiqueRegister::getTenant, queryParam.getTenant())
                .list();
    }
}
