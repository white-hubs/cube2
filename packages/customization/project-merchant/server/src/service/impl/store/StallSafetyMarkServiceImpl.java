package com.guhecloud.cube.deliver.project.merchant.service.impl.store;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallSafetyMarkMapper;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkService;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.DateUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluation;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyMark;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantAreaService;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantCompanyService;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 档位安全生产值 服务实现类
 * </p>
 *
 * @author zwb
 * @since 2021-12-24
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSafetyMarkServiceImpl extends ServiceImpl<StallSafetyMarkMapper, StallSafetyMark> implements StallSafetyMarkService {

    private final StallSafetyMarkMapper mapper;
    private final MerchantCompanyService merchantCompanyService;
    private final StallEvaluationService evaluationService;
    private final MerchantAreaService merchantAreaService;

    @Override
    public synchronized void minusScore(String storeId, int mark, int size) {
        int year = DateUtils.getYear(LocalDateTime.now());
        StallSafetyMark safetyMark = mapper.selectOne(Wrappers.<StallSafetyMark>lambdaQuery()
                .eq(StallSafetyMark::getStallId, storeId)
                .eq(StallSafetyMark::getYear, year)
        );
        if (safetyMark == null) {
            //新增一条记录，记录当前年，当前档位的分值
            StallSafetyMark stallSafetyMark = new StallSafetyMark();
            stallSafetyMark.setStallId(storeId);
            stallSafetyMark.setScore(STORE_DEFAULT_MARK-mark);
            stallSafetyMark.setInspTotal(size);
            stallSafetyMark.setYear(year);
            mapper.insert(stallSafetyMark);
        } else {
            //扣除巡检分
            mapper.updateScoreAndInspTotal(safetyMark.getId(),mark,size);
        }
    }

    @Override
    public StallScoreDTO getStallScore(Space space) {
        StallScoreDTO dto = new StallScoreDTO();
        // 查询档位对应的信息
        // 查询档位所属人
        if (StringUtils.isNotEmpty(space.getCompanyId())) {
            Company company = merchantCompanyService.getById(space.getCompanyId());
            BeanUtils.copyProperties(space, dto);
            dto.setOwner(company == null ? "" : company.getOwner());
            dto.setMobile(company == null ? "" : company.getMobile());
            dto.setIntroduction(company == null ? "" : company.getIntroduction());
        }
        // 查询档安全生产总分
        StallSafetyMark safetyMark = mapper.selectOne(Wrappers.<StallSafetyMark>lambdaQuery()
                                                              .eq(StallSafetyMark::getStallId, space.getId())
                                                              .eq(StallSafetyMark::getYear, com.guhecloud.platform.base.util.DateUtils.now().getYear()));
        dto.setScore(safetyMark == null ? 100 : safetyMark.getScore());
        dto.setInspTotal(safetyMark == null ? 0 : safetyMark.getInspTotal());
        // 查询评价分数
        StallEvaluation stallEvaluation = evaluationService.lambdaQuery().eq(StallEvaluation::getStallId, space.getId()).one();
        if (stallEvaluation != null) {
            BeanUtils.copyProperties(stallEvaluation, dto, "id");
        }
        // 扩展区域信息
        dto.setAreaNames(merchantAreaService.parseNames(Arrays.asList(dto.getAreaId1(), dto.getAreaId2(), dto.getAreaId3())));
        return dto;
    }

    @Override
    public List<StallScoreDTO> getStallScoreAndEvaluation(StallScoreQueryDTO queryDTO) {
        return mapper.getStallScoreAndEvaluation(queryDTO);
    }
}
