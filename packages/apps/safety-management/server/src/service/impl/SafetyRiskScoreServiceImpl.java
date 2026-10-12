package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyRiskScoreMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.RiskScoreQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.RiskScoreVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyRiskScorePO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyRiskScoreService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author wql
 * @describe 分值区间
 * @date 2023/10/12 15:59
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyRiskScoreServiceImpl extends ServiceImpl<SafetyRiskScoreMapper, SafetyRiskScorePO> implements SafetyRiskScoreService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRiskScore(RiskScoreVO vo) {
        double slope = 0d;
        Integer scoreStart = vo.getScoreStart();
        Integer scoreEnd = vo.getScoreEnd();
        Double sectionStart = vo.getSectionStart();
        Double sectionEnd = vo.getSectionEnd();
        if (ObjectUtils.isNotEmpty(scoreEnd) && ObjectUtils.isNotEmpty(sectionEnd)) {
            BigDecimal score = new BigDecimal(scoreEnd - scoreStart);
            BigDecimal section = new BigDecimal(sectionEnd - sectionStart);
            slope = score.divide(section, 2, RoundingMode.HALF_UP).doubleValue();
        }
        vo.setSlope(slope);
        vo.setSectionEnd(sectionEnd == null ? 9999D : sectionEnd);
        SafetyRiskScorePO scorePO = ObjectUtils.copying(vo, SafetyRiskScorePO.class);
        this.saveOrUpdate(scorePO);
    }

    @Override
    public void deleteRiskScore(Long id) {
        this.removeById(id);
    }

    @Override
    public RiskScoreVO riskScoreDetail(Long id) {
        return ObjectUtils.copying(this.getById(id), RiskScoreVO.class);
    }

    @Override
    public Pagination<RiskScoreVO> riskScoreList(RiskScoreQuery query) {
        Page<SafetyRiskScorePO> page = this.lambdaQuery().page(PagingUtils.convert(query));
        return PagingUtils.convert(page, RiskScoreVO.class);
    }

    @Override
    public RiskScoreVO getRiskScoreConfig(double alarmNum, String riskIndexType) {
        List<SafetyRiskScorePO> list = this.lambdaQuery().le(SafetyRiskScorePO::getSectionStart, alarmNum)
                .gt(SafetyRiskScorePO::getSectionEnd, alarmNum)
                .eq(SafetyRiskScorePO::getIndexType, riskIndexType).list();
        return ObjectUtils.isNotEmpty(list) ? ObjectUtils.copying(list.getFirst(), RiskScoreVO.class) : null;
    }

}
