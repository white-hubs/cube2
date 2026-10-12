package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.InspectionStatisticsMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.inspect.InspectBaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.InspectTaskVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.InspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.inspect.InspectCompleteStatVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.InspectionTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.safety.management.service.InspectionStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.util.CommonUtil;
import com.guhecloud.cube.srv.gen.safety.management.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.safety.management.util.StatisticsUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 巡检统计
 * @date 2023/9/8 10:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectionStatisticsServiceImpl extends ServiceImpl<InspectionStatisticsMapper, InspectTaskPO> implements InspectionStatisticsService {

    @Override
    public InspectionStatisticsVO getInspectionStatistics(BaseQuery query) {
        List<InspectTaskPO> taskPOS = this.baseMapper.getInspectionTaskList(query);
        return InspectionStatisticsVO.builder().inspectionTaskNum(taskPOS.size())
                .build();
    }

    @Override
    public Pagination<InspectTaskVO> getInspectionPage(InspectionListQuery param) {
        Page<InspectTaskPO> page = this.baseMapper.getInspectionTaskPage(PagingUtils.convert(param), param);
        List<InspectTaskVO> result = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(po -> {
                result.add(InspectTaskVO.builder()
                                   .marketTenant(po.getMarketTenant())
                                   .marketName(po.getMarketName())
                                   .name(po.getTaskName())
                                   .typeName(po.getTypeName())
                                   .taskType(po.getTaskType())
                                   .level(po.getLevelName())
                                   .status(po.getStatusName())
                                   .executor(po.getExecutor())
                                   .validEndTime(po.getValidEndTime())
                                   .targetNum(po.getTargetNum())
                                   .progressRate((po.getProgress() == null ? 0 : po.getProgress()) + "%")
                                   .build());
            });
        }
        return PagingUtils.convert(page, result);
    }

    @Override
    public List<String> getInspectionTypes() {
        BaseQuery query = new BaseQuery();
        return this.baseMapper.getInspectionTypes(query);
    }

    /**
     * @Author wql
     * @Description 计算巡检任务完成率
     * @Date 10:18 2023/9/14
     **/
    @Override
    public Double getTaskCompleteRatio(BaseQuery query) {
        InspectCompleteStatVO completeStat = getCompleteStat(query);
        return completeStat.getRatio();
    }

    @Override
    public InspectCompleteStatVO getCompleteStat(BaseQuery query) {
        List<InspectTaskPO> taskPOS = this.baseMapper.getInspectionTaskList(query);
        if (ObjectUtils.isNotEmpty(taskPOS)) {
            long count = taskPOS.stream().filter(po -> InspectionTaskStatusEnum.OVER.name().equals(po.getStatusCode())).count();
            return new InspectCompleteStatVO((int) count, CommonUtil.countProportion(BigDecimal.valueOf(taskPOS.size()), BigDecimal.valueOf(count)));
        }
        return new InspectCompleteStatVO(0, 0D);
    }

    /**
     * @Author wql
     * @Description 统计巡检任务数
     * @Date 10:52 2023/9/14
     **/
    @Override
    public Integer countTaskNum(CountTaskNumQuery query) {
        return this.baseMapper.countTaskNum(query);
    }

    @Override
    public Pagination<MarketStatisticsInfo> getInspectionMarketStatistics(MarketInspectionQuery query) {
        List<MarketStatisticsInfo> newPage = this.baseMapper.getInspectionMarketStatistics(query);
        Period lastPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(lastPeriod);
        List<MarketStatisticsInfo> oldPage = this.baseMapper.getInspectionMarketStatistics(query);

        return StatisticsUtils.marketStatisticsPageHandler(newPage, oldPage, query.getPageNumber(), query.getPageSize());

    }

    @Override
    public Integer count(InspectBaseQuery query) {
        CountTaskNumQuery countTaskNumQuery = new CountTaskNumQuery();
        countTaskNumQuery.setPeriod(query.getCreatedPeriod());
        if (CollectionUtils.isNotEmpty(query.getStatus())) {
            countTaskNumQuery.setStatus(query.getStatus().stream().map(InspectionTaskStatusEnum::name).collect(Collectors.toList()));

        }
        return this.baseMapper.countTaskNum(countTaskNumQuery);
    }

    @Override
    public List<GroupCountVO> countStatus(BaseStatisticsQuery query) {
        return this.baseMapper.countStatus(query);
    }

    /**
     * 市场违规排名
     *
     * @param query
     *
     * @return
     */
    @Override
    public Pagination<MarketStatisticsInfo> getInspectionMarketErrorRecordStatistics(MarketInspectionQuery query) {
        List<MarketStatisticsInfo> newPage = this.baseMapper.getInspectionMarketErrorRecordStatistics(query);
        Period lastPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(lastPeriod);
        List<MarketStatisticsInfo> oldPage = this.baseMapper.getInspectionMarketErrorRecordStatistics(query);
        return StatisticsUtils.marketStatisticsPageHandler(newPage, oldPage, query.getPageNumber(), query.getPageSize());
    }

}
