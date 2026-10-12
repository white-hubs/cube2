package com.guhecloud.cube.biz.mdata.server.supplier.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.supplier.dao.RiskLevelMapper;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.RiskLevel;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.RiskLevelEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.param.RiskLevelCreateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.RiskLevelUpdateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.service.RiskLevelService;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskLevelServiceImpl extends ServiceImpl<RiskLevelMapper, RiskLevel> implements RiskLevelService {

    private final SupplierService supplierService;

    @Override
    public void save(@NonNull List<RiskLevelCreateParam> params) {
        List<RiskLevel> riskLevels = ObjectUtils.copying(params, RiskLevel.class);
        // 校验参数
        this.check(riskLevels);
        this.saveBatch(riskLevels);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initRiskLevel(@NonNull Integer tenant) {
        RiskLevel low = new RiskLevel(RiskLevelEnum.LOW, "0");
        RiskLevel medium = new RiskLevel(RiskLevelEnum.MEDIUM, "2");
        RiskLevel high = new RiskLevel(RiskLevelEnum.HIGH, "2");
        List<RiskLevel> riskLevels = Stream.of(low, medium, high).collect(Collectors.toList());
        this.saveBatch(riskLevels);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull List<RiskLevelUpdateParam> params) {
        List<RiskLevel> riskLevels = ObjectUtils.copying(params, RiskLevel.class);
        // 校验参数
        this.check(riskLevels);
        List<Long> ids = riskLevels.stream().map(RiskLevel::getId).collect(Collectors.toList());
        List<RiskLevel> levels = this.getByIds(ids);
        Map<Long, List<RiskLevel>> riskLevelMap = riskLevels.stream().collect(Collectors.groupingBy(RiskLevel::getId));
        for (RiskLevel riskLevel : levels) {
            if (!MapUtils.isEmpty(riskLevelMap) && riskLevelMap.containsKey(riskLevel.getId())) {
                RiskLevel rk = riskLevelMap.get(riskLevel.getId()).get(0);
                riskLevel.setValue(rk.getValue());
            }
        }
        this.updateBatchById(levels);

        // 更新对应商户的风险等级
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<Supplier> suppliers = this.supplierService.listSupplier("", tenant);
        this.updateSupplierRiskLevel(suppliers, riskLevels);
    }

    @Override
    public RiskLevel getByValue(@NonNull Integer value, @NonNull List<RiskLevel> riskLevels) {
        Map<RiskLevelEnum, List<RiskLevel>> riskLevelMap = riskLevels.stream().collect(Collectors.groupingBy(RiskLevel::getType));
        if (!MapUtils.isEmpty(riskLevelMap)) {
            // 低风险
            if (Objects.nonNull(riskLevelMap.get(RiskLevelEnum.LOW).get(0))
                && Integer.parseInt(riskLevelMap.get(RiskLevelEnum.LOW).get(0).getValue()) >= value) {
                return riskLevelMap.get(RiskLevelEnum.LOW).get(0);
            }
            // 中风险
            if (Objects.nonNull(riskLevelMap.get(RiskLevelEnum.MEDIUM).get(0))
                && Integer.parseInt(riskLevelMap.get(RiskLevelEnum.MEDIUM).get(0).getValue()) >= value) {
                return riskLevelMap.get(RiskLevelEnum.MEDIUM).get(0);
            }
            // 高风险
            return riskLevelMap.get(RiskLevelEnum.HIGH).get(0);
        }
        return RiskLevel.builder().type(RiskLevelEnum.HIGH).build();
    }

    @Override
    public List<RiskLevel> getByIds(List<Long> ids) {
        return this.lambdaQuery().in(RiskLevel::getId, ids).list();
    }

    @Override
    public List<RiskLevel> listRiskLevels() {
        return this.lambdaQuery().list();
    }

    private void check(List<RiskLevel> riskLevels) {
        Map<RiskLevelEnum, List<RiskLevel>> riskLevelMap = riskLevels.stream().collect(Collectors.groupingBy(RiskLevel::getType));
        if (!riskLevelMap.containsKey(RiskLevelEnum.LOW)
            || !riskLevelMap.containsKey(RiskLevelEnum.MEDIUM)
            || !riskLevelMap.containsKey(RiskLevelEnum.HIGH)) {
            throw new ServiceException(String.valueOf(HttpStatus.PRECONDITION_FAILED.value()), "参数为空");
        }
        if (Integer.parseInt(riskLevelMap.get(RiskLevelEnum.MEDIUM).get(0).getValue()) <=
            Integer.parseInt(riskLevelMap.get(RiskLevelEnum.LOW).get(0).getValue())) {
            throw new ServiceException(String.valueOf(HttpStatus.PRECONDITION_FAILED.value()), "中风险数值不能小于低风险数值");
        }
        if (Integer.parseInt(riskLevelMap.get(RiskLevelEnum.HIGH).get(0).getValue()) !=
            Integer.parseInt(riskLevelMap.get(RiskLevelEnum.MEDIUM).get(0).getValue())) {
            throw new ServiceException(String.valueOf(HttpStatus.PRECONDITION_FAILED.value()), "高风险数值与中风险数值不相同");
        }
        if (Integer.parseInt(riskLevelMap.get(RiskLevelEnum.HIGH).get(0).getValue()) <=
            Integer.parseInt(riskLevelMap.get(RiskLevelEnum.LOW).get(0).getValue())) {
            throw new ServiceException(String.valueOf(HttpStatus.PRECONDITION_FAILED.value()), "高风险数值不能小于低风险数值");
        }
    }

    private void updateSupplierRiskLevel(List<Supplier> suppliers, List<RiskLevel> riskLevels) {
        suppliers.forEach(supplier -> {
            RiskLevel riskLevel = this.getByValue(supplier.getRiskAmount(), riskLevels);
            supplier.setRiskLevel(riskLevel.getType());
        });
        this.supplierService.updateBatchById(suppliers);
    }
}
