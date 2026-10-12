package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.PatrolStatisticsMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.PatrolListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.PatrolTaskVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.PatrolStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.patrol.PatrolCompleteStatVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.StatusNumStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.PatrolTaskStateEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.PatrolTaskPO;
import com.guhecloud.cube.srv.gen.safety.management.service.PatrolStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.util.CommonUtil;
import com.guhecloud.cube.srv.gen.safety.management.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.safety.management.util.StatisticsUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wql
 * @describe 电子巡更
 * @date 2023/9/8 11:04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolStatisticsServiceImpl extends ServiceImpl<PatrolStatisticsMapper, PatrolTaskPO> implements PatrolStatisticsService {

    /**
     * @Author wql
     * @Description 统计巡更任务数
     * @Date 10:55 2023/9/14
     **/
    @Override
    public Integer countTaskNum(CountTaskNumQuery query) {
        return this.baseMapper.countTaskNum(query);
    }

    @Override
    public PatrolStatisticsVO getPatrolStatistics(BaseQuery query) {
        // 默认返回对象
        int zero = BigDecimal.ZERO.intValue();
        AtomicInteger allNumber = new AtomicInteger(zero);
        PatrolStatisticsVO result = PatrolStatisticsVO.builder().errCount(zero).waitCount(zero).executionCount(zero).finishedCount(zero)
                .totalCount(zero).build();
        // 获取数据
        List<StatusNumStatisticsVO> list = this.baseMapper.getPatrolStatusStatistics(query);
        if (!ObjectUtils.isEmpty(list)) {
            // 组装各个状态数量
            list.forEach(po -> {
                allNumber.addAndGet(po.getNum());
                if (PatrolTaskStateEnum.TO_BE_RECEIVED.name().equals(po.getStatusCode())) {
                    result.setWaitCount(po.getNum());
                } else if (PatrolTaskStateEnum.IN_EXECUTION.name().equals(po.getStatusCode())) {
                    result.setExecutionCount(po.getNum());
                } else if (PatrolTaskStateEnum.FINISHED.name().equals(po.getStatusCode())) {
                    result.setFinishedCount(po.getNum());
                }
            });
        }

        // 获取错误数量
        int errorNumber = this.baseMapper.getPatrolErrorNum(query);
        result.setErrCount(errorNumber);
        // 设置总数量(总数是各个状态相加，错误数只是一个结果)
        result.setTotalCount(allNumber.get());
        // 返回
        return result;
    }

    private Integer countByStatus(BaseQuery query, String status) {
        List<PatrolTaskPO> pos = this.baseMapper.getPatrolTaskList(query, status);
        return ObjectUtils.isNull(pos) ? 0 : pos.size();
    }

    @Override
    public Pagination<PatrolTaskVO> getPatrolList(PatrolListQuery patrolListQuery) {
        Page<PatrolTaskPO> page = this.baseMapper.getPatrolTaskPage(PagingUtils.convert(patrolListQuery), patrolListQuery);
        List<PatrolTaskVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(po -> {
                vos.add(PatrolTaskVO.builder()
                                .marketTenant(po.getMarketTenant())
                                .marketName(po.getMarketName())
                                .taskName(po.getTaskName())
                                .executeData(po.getExecuteData())
                                .status(po.getStatusName())
                                .result(po.getResultName())
                                .routeName(po.getRouteName())
                                .locationNum(po.getLocationNum())
                                .userName(po.getUserName())
                                .build());
            });
        }
        return PagingUtils.convert(page, vos);
    }

    /**
     * @Author wql
     * @Description 巡更任务完成率
     * @Date 10:29 2023/9/14
     **/
    @Override
    public Double getTaskCompleteRatio(BaseQuery query) {
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setMarketTenant(query.getMarketTenant());
        baseQuery.setTaskCreatePeriod(query.getPeriod());
        List<PatrolTaskPO> taskPOS = this.baseMapper.getPatrolTaskList(baseQuery, null);
        if (ObjectUtils.isNotEmpty(taskPOS)) {
            long count = taskPOS.stream().filter(po -> PatrolTaskStateEnum.FINISHED.name().equals(po.getStatusCode())).count();
            return CommonUtil.countProportion(BigDecimal.valueOf(taskPOS.size()), BigDecimal.valueOf(count));
        }
        return 0D;
    }

    @Override
    public PatrolCompleteStatVO getTaskCompleteStat(BaseQuery query) {
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setMarketTenant(query.getMarketTenant());
        baseQuery.setTaskCreatePeriod(query.getPeriod());
        List<PatrolTaskPO> taskPOS = this.baseMapper.getPatrolTaskList(baseQuery, null);
        if (ObjectUtils.isNotEmpty(taskPOS)) {
            long count = taskPOS.stream().filter(po -> PatrolTaskStateEnum.FINISHED.name().equals(po.getStatusCode())).count();
            return new PatrolCompleteStatVO((int) count, CommonUtil.countProportion(BigDecimal.valueOf(taskPOS.size()), BigDecimal.valueOf(count)));
        }
        return new PatrolCompleteStatVO(0, 0D);
    }

    @Override
    public List<GroupCountVO> countStatus(BaseStatisticsQuery query) {
        return this.baseMapper.countStatus(query);
    }

    /**
     * 巡更排名
     *
     * @param query
     *
     * @return
     */
    @Override
    public Pagination<MarketStatisticsInfo> getPatrolMarketRankingStatistics(MarketInspectionQuery query) {
        List<MarketStatisticsInfo> newPage = this.baseMapper.getPatrolMarketStatistics(query);
        Period lastPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(lastPeriod);
        List<MarketStatisticsInfo> oldPage = this.baseMapper.getPatrolMarketStatistics(query);
        return StatisticsUtils.marketStatisticsPageHandler(newPage, oldPage, query.getPageNumber(), query.getPageSize());
    }
}
