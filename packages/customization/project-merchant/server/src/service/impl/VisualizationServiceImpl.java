package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.DateUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.DeliverPeriod;
import com.guhecloud.cube.deliver.project.merchant.dto.KeyValueDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.MerchantCountDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.OperatingPriceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.stat.StallScoreStatOverviewDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecord;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyInsp;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.SpaceMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallEvaluationRecordMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallSafetyInspMapper;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.service.VisualizationService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyInspService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-15  11:41
 * @Description:
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisualizationServiceImpl implements VisualizationService {
    private final CompanyMapper companyMapper;
    private final SpaceMapper spaceMapper;
    private final StallSafetyInspService safetyInspService;
    private final StallSafetyInspMapper stallSafetyInspMapper;

    private final StallEvaluationRecordMapper stallEvaluationRecordMapper;
//    stall_evaluation_record

    @Override
    public MerchantCountDTO count() {
        MerchantCountDTO merchantCount = new MerchantCountDTO();
        merchantCount.setMerchantTotal(Optional.ofNullable(companyMapper.selectCount(new LambdaQueryWrapper<>())).orElse(0L).intValue());
        merchantCount.setGearTotal(Optional.ofNullable(spaceMapper.selectCount(new LambdaQueryWrapper<Space>().eq(Space::getDeleted, false))).orElse(0L).intValue());
        return merchantCount;
    }

    @Override
    public Pagination<KeyValueDTO> safeProductionByRanking(DeliverPeriod period) {
        return this.getTotalScoreTop(period);
    }

    @Override
    public List<KeyValueDTO> safeProductionByProportion(DeliverPeriod period) {
        List<KeyValueDTO> production = spaceMapper.safeProductionByProportion();
        List<KeyValueDTO> data = new ArrayList<>();
        AtomicInteger ordinary = new AtomicInteger(0);
        AtomicInteger good = new AtomicInteger(0);
        AtomicInteger excellent = new AtomicInteger(0);
        production.stream().forEach(ex -> {
            if (ex.getValue() < 61) {
                ordinary.set(ordinary.get() + ex.getValue());
            } else if (ex.getValue() < 81) {
                good.set(good.get() + ex.getValue());
            } else {
                excellent.set(excellent.get() + ex.getValue());
            }
        });
        KeyValueDTO keyValueDTO = new KeyValueDTO("0-60分", ordinary.get());
        data.add(keyValueDTO);
        keyValueDTO = new KeyValueDTO("60-80分", good.get());
        data.add(keyValueDTO);
        keyValueDTO = new KeyValueDTO("80分以上", excellent.get());
        data.add(keyValueDTO);
        return data;
    }

    @Override
    public List<KeyValueDTO> getQuestionnaire(DeliverPeriod period) {
        return spaceMapper.getQuestionnaire(period.getPeriod());
    }

    @Override
    public List<KeyValueDTO> getwholesalerAll(DeliverPeriod period) {
        return null;
    }

    @Override
    public List<KeyValueDTO> getOperatingProportion(DeliverPeriod period) {
        long total = stallEvaluationRecordMapper.selectCount(new LambdaQueryWrapper<StallEvaluationRecord>()
                .eq(StallEvaluationRecord::getExamineStatus, 1));
        List<KeyValueDTO> proportion = spaceMapper.getOperatingProportion(period.getCode());
        List<KeyValueDTO> data = new ArrayList<>();
        proportion.stream().forEach(ex -> {
            if (ObjectUtil.isNotEmpty(ex.getName())) {
                ex.setPercentage(BigDecimal.valueOf(total).divide(BigDecimal.valueOf(ex.getValue())).multiply(BigDecimal.valueOf(100)));
                if (ex.getValue() <= 1) {
                    ex.setName("一星");
                } else if (ex.getValue() <= 2) {
                    ex.setName("二星");
                } else if (ex.getValue() <= 3) {
                    ex.setName("三星");
                } else if (ex.getValue() <= 4) {
                    ex.setName("四星");
                } else {
                    ex.setName("五星");
                }
                data.add(ex);
            }
        });
        return data;
    }

    @Override
    public List<OperatingPriceDTO> getOperatingPrice(DeliverPeriod period) {

        return spaceMapper.getOperatingPrice();
    }

    @Override
    public List<KeyValueDTO> getContractCount(DeliverPeriod period) {
        return spaceMapper.getContractCountd(period.getPeriod());
    }

    public Pagination<KeyValueDTO> getTotalScoreTop(DeliverPeriod period) {
        List<KeyValueDTO> dtos = new ArrayList<>();
        Pagination<Space> spaces = this.querySpaceList(period);
        spaces.getRecords().forEach(ex -> {
            Integer scoreRecords = stallSafetyInspMapper.getScoreRecord(period.getPeriod(), ex.getId());
            Integer beforeRecords = stallSafetyInspMapper.getScoreRecord(period.getBeforePeriod(), ex.getId());
            KeyValueDTO dto = new KeyValueDTO();
            dto.setId(ex.getId());
            dto.setName(ex.getName());
            dto.setValue(StallSafetyMarkService.STORE_DEFAULT_MARK - scoreRecords);
            dto.setPercentage((new BigDecimal(StallSafetyMarkService.STORE_DEFAULT_MARK - scoreRecords).abs(new MathContext(100 - beforeRecords))).divide(BigDecimal.valueOf(100 - beforeRecords)).multiply(BigDecimal.valueOf(100)));
            dtos.add(dto);
        });
        return PagingUtils.convert(spaces, dtos);
    }

    private Pagination<Space> querySpaceList(DeliverPeriod period) {
        Page<Space> page = new Page<>(period.getPageNumber(), period.getPageSize());
       page= spaceMapper.selectPage(page, new LambdaQueryWrapper<Space>()
               .eq(Space::getDeleted,false));
        return PagingUtils.convert(page, page.getRecords());
    }
}
