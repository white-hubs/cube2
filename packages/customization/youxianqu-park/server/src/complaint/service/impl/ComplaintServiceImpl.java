package com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.youxianqu.park.management.server.common.utils.TrendTimeUtils;
import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.result.TrendResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.mapper.ComplaintMapper;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.param.ComplaintQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.po.ComplaintPO;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.result.ComplaintResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.service.ComplaintService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投诉服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, ComplaintPO> implements ComplaintService {

    private final ComplaintMapper complaintMapper;

    /**
     * 投诉分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<ComplaintResult> getPage(ComplaintQueryParam param) {
        Period period = param.getPeriod();
        boolean periodNotNullBool = period != null && period.getBegin() != null && period.getEnd() != null;
        Page<ComplaintPO> page = complaintMapper.selectPage(PagingUtils.convert(param), Wrappers.<ComplaintPO>lambdaQuery()
                                                                    .eq(StringUtils.isNotBlank(param.getStatus()), ComplaintPO::getStatus, param.getStatus())
                                                                    .between(periodNotNullBool, ComplaintPO::getCreateTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                    .orderByDesc(ComplaintPO::getCreateTime)
                                                           );
        return PagingUtils.convert(page, ComplaintResult.class);
    }

    /**
     * 获取投诉趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public List<TrendResult> getTrend(ComplaintQueryParam param) {
        TrendTimeUtils.setFormatAndPeriod(param);

        return complaintMapper.selectTrend(param.getFormat(), param.getPeriod());
    }
}
