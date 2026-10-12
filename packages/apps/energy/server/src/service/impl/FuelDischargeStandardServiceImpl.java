package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.FuelDischargeStandardMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.FuelDischargeStandard;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.FuelDischargeStandardService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.FuelSelectListVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 燃料类型服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FuelDischargeStandardServiceImpl extends ServiceImpl<FuelDischargeStandardMapper, FuelDischargeStandard> implements FuelDischargeStandardService {

    /**
     * 获取燃料类型下拉列表
     *
     * @param keyword code或名称模糊查询
     *
     * @return 下拉列表
     */
    @Override
    public List<FuelSelectListVO> getSelectList(String keyword) {
        List<FuelDischargeStandard> fuelDischargeStandardList = this.list(Wrappers.<FuelDischargeStandard>lambdaQuery()
                                                                                  .and(StringUtils.isNotBlank(keyword),
                                                                                       i -> i.like(FuelDischargeStandard::getCode, keyword)
                                                                                               .or()
                                                                                               .like(FuelDischargeStandard::getFuelName, keyword)
                                                                                      )
                                                                         );
        if (fuelDischargeStandardList == null || fuelDischargeStandardList.isEmpty()) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(fuelDischargeStandardList, FuelSelectListVO.class);
    }

    /**
     * 插入初始数据
     */
    @Override
    public void insertDefaultFuel() {
        LocalDateTime now = LocalDateTime.now();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        this.remove(Wrappers.<FuelDischargeStandard>lambdaQuery().eq(FuelDischargeStandard::getTenant, tenant));
        List<FuelDischargeStandard> insertList = new ArrayList<>();
        insertList.add(FuelDischargeStandard.builder().code("YM").fuelName("原煤").unit("kg").carbonFactor(new BigDecimal("1.91200"))
                               .calculateProportion(new BigDecimal("0.71430")).build());
        insertList.add(FuelDischargeStandard.builder().code("XJM").fuelName("洗精煤").unit("kg").carbonFactor(new BigDecimal("2.38000"))
                               .calculateProportion(new BigDecimal("0.90000")).build());
        insertList.add(FuelDischargeStandard.builder().code("XZM").fuelName("洗中煤").unit("kg").carbonFactor(new BigDecimal("1.87000"))
                               .calculateProportion(new BigDecimal("0.28570")).build());
        insertList.add(FuelDischargeStandard.builder().code("MN").fuelName("煤泥").unit("kg").carbonFactor(new BigDecimal("1.24200"))
                               .calculateProportion(new BigDecimal("0.35715")).build());
        insertList.add(FuelDischargeStandard.builder().code("MGS").fuelName("煤矸石").unit("kg").carbonFactor(new BigDecimal("2.30100"))
                               .calculateProportion(new BigDecimal("0.28570")).build());
        insertList.add(FuelDischargeStandard.builder().code("JY").fuelName("焦炭").unit("kg").carbonFactor(new BigDecimal("2.41600"))
                               .calculateProportion(new BigDecimal("10.97140")).build());
        insertList.add(FuelDischargeStandard.builder().code("MJY").fuelName("煤焦油").unit("kg").carbonFactor(new BigDecimal("1.83100"))
                               .calculateProportion(new BigDecimal("11.14290")).build());
        insertList.add(FuelDischargeStandard.builder().code("YY").fuelName("原油").unit("kg").carbonFactor(new BigDecimal("2.87300"))
                               .calculateProportion(new BigDecimal("1.42860")).build());
        insertList.add(FuelDischargeStandard.builder().code("RLY").fuelName("燃料油").unit("kg").carbonFactor(new BigDecimal("0.77900"))
                               .calculateProportion(new BigDecimal("1.42860")).build());
        insertList.add(FuelDischargeStandard.builder().code("QY").fuelName("汽油").unit("m³").carbonFactor(new BigDecimal("0.77100"))
                               .calculateProportion(new BigDecimal("1.47140")).build());
        insertList.add(FuelDischargeStandard.builder().code("MY").fuelName("煤油").unit("M").carbonFactor(new BigDecimal("0.97700"))
                               .calculateProportion(new BigDecimal("1.47140")).build());
        insertList.add(FuelDischargeStandard.builder().code("CY").fuelName("柴油").unit("m³").carbonFactor(new BigDecimal("1.44500"))
                               .calculateProportion(new BigDecimal("1.45710")).build());
        insertList.add(FuelDischargeStandard.builder().code("TRR").fuelName("天然气").unit("m³").carbonFactor(new BigDecimal("0.23200"))
                               .calculateProportion(new BigDecimal("1.21500")).build());
        insertList.add(FuelDischargeStandard.builder().code("YHTRQ").fuelName("液化天然气").unit("kg")
                               .carbonFactor(new BigDecimal("3.03100")).calculateProportion(new BigDecimal("1.75720")).build());
        insertList.add(FuelDischargeStandard.builder().code("YHSYQ").fuelName("液化石油气").unit("kg")
                               .carbonFactor(new BigDecimal("2.93600")).calculateProportion(new BigDecimal("1.71430")).build());
        insertList.add(FuelDischargeStandard.builder().code("LCQQ").fuelName("炼厂千气").unit("kg").carbonFactor(new BigDecimal("3.04400"))
                               .calculateProportion(new BigDecimal("1.57140")).build());
        insertList.add(FuelDischargeStandard.builder().code("JLMQ").fuelName("焦炉煤气").unit("m³").carbonFactor(new BigDecimal("3.10700"))
                               .calculateProportion(new BigDecimal("0.59285")).build());
        insertList.add(FuelDischargeStandard.builder().code("GLMQ").fuelName("高炉煤气").unit("m³").carbonFactor(new BigDecimal("3.18100"))
                               .calculateProportion(new BigDecimal("0.12860")).build());
        insertList.add(FuelDischargeStandard.builder().code("FSLMQ").fuelName("发生炉煤气").unit("m³")
                               .carbonFactor(new BigDecimal("3.10400")).calculateProportion(new BigDecimal("0.17860")).build());
        insertList.add(FuelDischargeStandard.builder().code("ZYCHLJMQ").fuelName("重油催化裂解煤气").unit("m³")
                               .carbonFactor(new BigDecimal("3.01500")).calculateProportion(new BigDecimal("0.65710")).build());
        insertList.add(FuelDischargeStandard.builder().code("ZYRLJMQ").fuelName("重油热裂解煤气").unit("m³")
                               .carbonFactor(new BigDecimal("3.08700")).calculateProportion(new BigDecimal("1.21430")).build());
        insertList.add(FuelDischargeStandard.builder().code("JTZQ").fuelName("焦炭制气").unit("m³").carbonFactor(new BigDecimal("3.19200"))
                               .calculateProportion(new BigDecimal("0.55710")).build());
        insertList.add(FuelDischargeStandard.builder().code("YLQHMQ").fuelName("压力气化煤气").unit("m³")
                               .carbonFactor(new BigDecimal("2.16400")).calculateProportion(new BigDecimal("0.51430")).build());
        insertList.add(FuelDischargeStandard.builder().code("SMQ").fuelName("水煤气").unit("m³").carbonFactor(new BigDecimal("0.37480"))
                               .calculateProportion(new BigDecimal("0.35710")).build());
        insertList.add(FuelDischargeStandard.builder().code("JC").fuelName("甲醇（用作燃料）").unit("kg")
                               .carbonFactor(new BigDecimal("1.54000")).calculateProportion(new BigDecimal("0.67940")).build());
        insertList.add(FuelDischargeStandard.builder().code("WYM").fuelName("无烟煤").unit("kg").carbonFactor(new BigDecimal("2.38000"))
                               .calculateProportion(new BigDecimal("1.00000")).build());
        insertList.add(FuelDischargeStandard.builder().code("YBYM").fuelName("一般烟煤").unit("kg").carbonFactor(new BigDecimal("1.87000"))
                               .calculateProportion(new BigDecimal("1.00000")).build());
        insertList.add(FuelDischargeStandard.builder().code("HM").fuelName("褐煤").unit("kg").carbonFactor(new BigDecimal("1.24200"))
                               .calculateProportion(new BigDecimal("0.42860")).build());
        insertList.add(FuelDischargeStandard.builder().code("LJYM").fuelName("炼焦烟煤").unit("kg").carbonFactor(new BigDecimal("2.30100"))
                               .calculateProportion(new BigDecimal("0.90000")).build());
        insertList.add(FuelDischargeStandard.builder().code("QTXM").fuelName("其他洗煤").unit("kg").carbonFactor(new BigDecimal("1.83100"))
                               .calculateProportion(new BigDecimal("1.65800")).build());
        insertList.add(FuelDischargeStandard.builder().code("ZLMQ").fuelName("转炉煤气").unit("m³").carbonFactor(new BigDecimal("1.44500"))
                               .calculateProportion(new BigDecimal("0.20700")).build());
        insertList.add(FuelDischargeStandard.builder().code("QTMQ").fuelName("其他煤气").unit("m³").carbonFactor(new BigDecimal("0.23200"))
                               .calculateProportion(new BigDecimal("0.35700")).build());
        insertList.add(FuelDischargeStandard.builder().code("LQ").fuelName("沥青").unit("kg").carbonFactor(new BigDecimal("3.08700"))
                               .calculateProportion(new BigDecimal("1.33070")).build());
        insertList.add(FuelDischargeStandard.builder().code("DL").fuelName("电力").unit("kwh").carbonFactor(new BigDecimal("0.37480"))
                               .calculateProportion(new BigDecimal("0.12290")).build());
        insertList.add(FuelDischargeStandard.builder().code("S").fuelName("水").unit("m³").carbonFactor(new BigDecimal("0.00000"))
                               .calculateProportion(new BigDecimal("0.00000")).build());
        for (FuelDischargeStandard fuelDischargeStandard : insertList) {
            fuelDischargeStandard.setUpdateTime(now);
            fuelDischargeStandard.setTenant(tenant);
            EntityAuditor.populateInnerCreateAudit(fuelDischargeStandard);
        }
        this.saveBatch(insertList);
    }
}
