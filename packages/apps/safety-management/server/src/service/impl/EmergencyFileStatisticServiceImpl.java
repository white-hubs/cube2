package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.EmergencyFileStatisticMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.EmergencyFileQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.EmergencyFileClassifyVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.SafetyEmergencyFileVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.EmergencyFileClassify;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.EmergencyFileStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyEmergencyFilePO;
import com.guhecloud.cube.srv.gen.safety.management.service.EmergencyFileStatisticService;
import com.guhecloud.cube.srv.gen.safety.management.util.CommonUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wql
 * @describe 预案文件统计
 * @date 2023/10/8 17:45
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileStatisticServiceImpl extends ServiceImpl<EmergencyFileStatisticMapper, SafetyEmergencyFilePO> implements EmergencyFileStatisticService {

    @Override
    public EmergencyFileStatisticsVO getEmergencyFileStatistic(BaseQuery query) {
        EmergencyFileStatisticsVO vo = new EmergencyFileStatisticsVO();
        List<EmergencyFileClassify> classifies = this.baseMapper.getEmergencyFileStatistic(query);
        if (ObjectUtils.isNotNull(classifies)) {
            vo.setTotalNum(classifies.stream().mapToLong(EmergencyFileClassify::getClassifyNum).sum());
            long sum = 0L;
            for (int i = 0; i < classifies.size(); i++) {
                final EmergencyFileClassify classify = classifies.get(i);
                if (i > 2) {
                    final long otherNum = vo.getTotalNum() - sum;
                    classify.setClassifyRatio(CommonUtil.countProportion(new BigDecimal(vo.getTotalNum()), new BigDecimal(otherNum)));
                    classify.setClassifyCode("OTHER");
                    classify.setClassifyNum(otherNum);
                    classify.setClassifyName("其他");
                    classifies = classifies.subList(0, ++i);
                    break;
                }
                classify.setClassifyRatio(CommonUtil.countProportion(new BigDecimal(vo.getTotalNum()), new BigDecimal(classify.getClassifyNum())));
                sum += classify.getClassifyNum();
            }
            vo.setClassifyStatistics(classifies);
        }
        return vo;
    }

    @Override
    public Pagination<SafetyEmergencyFileVO> getEmergencyFileList(EmergencyFileQuery query) {
        Page<SafetyEmergencyFilePO> page = this.baseMapper.getSafetyEmergencyFilePage(PagingUtils.convert(query), query);
        return PagingUtils.convert(page, SafetyEmergencyFileVO.class);
    }

    @Override
    public List<EmergencyFileClassifyVO> getEmergencyFileClassify() {
        return this.baseMapper.getEmergencyFileClassify();
    }

    @Override
    public Integer countNum(BaseQuery cardQuery) {
        return this.baseMapper.countNum(cardQuery);
    }

    @Override
    public Integer count(BaseStatisticsQuery query) {
        return this.baseMapper.count(query);
    }
}
