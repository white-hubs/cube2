package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.enumeration.PeriodStatTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.dao.TreatedWaterUsageHourMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.entity.TreatedWaterUsageHour;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageHourParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageHourQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param.TreatedWaterUsageStatQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.service.TreatedWaterUsageHourService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterStatVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterTypeTrendStatVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.vo.TreatedWaterUsageHourVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 描述：净化水使用量-小时表服务层
 *
 * @author Appet
 * @date 2023-10-30 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TreatedWaterUsageHourServiceImpl extends ServiceImpl<TreatedWaterUsageHourMapper, TreatedWaterUsageHour> implements TreatedWaterUsageHourService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(TreatedWaterUsageHourParam param) {
        TreatedWaterUsageHour treatedWaterUsageHour = ObjectUtils.copying(param, TreatedWaterUsageHour.class);
        this.baseMapper.insert(treatedWaterUsageHour);
        return treatedWaterUsageHour.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, TreatedWaterUsageHourParam param) {
        TreatedWaterUsageHour treatedWaterUsageHour = this.baseMapper.selectById(id);
        Assert.isNotFound(treatedWaterUsageHour, "未查询到相关信息");
        ObjectUtils.copying(param, treatedWaterUsageHour);
        this.baseMapper.updateById(treatedWaterUsageHour);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        ids.stream().forEach(id -> this.baseMapper.deleteById(id));
    }

    @Override
    public TreatedWaterUsageHourVO getById(String id) {
        TreatedWaterUsageHour treatedWaterUsageHour = this.baseMapper.selectById(id);
        return ObjectUtils.copying(treatedWaterUsageHour, TreatedWaterUsageHourVO.class);
    }

    @Override
    public Pagination<TreatedWaterUsageHourVO> getPage(TreatedWaterUsageHourQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        Page<TreatedWaterUsageHour> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), TreatedWaterUsageHour::getDeviceName, param.getKeyword())
                .between(period !=
                         null, TreatedWaterUsageHour::getDateHour, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, TreatedWaterUsageHourVO.class);
    }

    @Override
    public List<TreatedWaterStatVO> getUsageStatByType(TreatedWaterUsageStatQueryParam param) {
        return this.baseMapper.getUsageStatByType(param);
    }

    @Override
    public List<TreatedWaterTypeTrendStatVO> getUsageStatByTypeAndTime(TreatedWaterUsageStatQueryParam param) {
        List<TreatedWaterStatVO> dataList = this.baseMapper.getUsageStatByTypeAndTime(param);
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<TreatedWaterTypeTrendStatVO> finalList = new ArrayList<>();
            dataList.stream().collect(Collectors.groupingBy(TreatedWaterStatVO::getWaterTypeName))
                    .forEach((typeName, list) -> {
                        finalList.add(new TreatedWaterTypeTrendStatVO(typeName, extendTrend(param, list)));
                    });
            return finalList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<TreatedWaterStatVO> getUsageStatByArea(TreatedWaterUsageStatQueryParam param) {
        return this.baseMapper.getUsageStatByArea(param);
    }

    private List<TreatedWaterStatVO> extendTrend(TreatedWaterUsageStatQueryParam param, List<TreatedWaterStatVO> list) {
        String type = param.getType();
        if (StringUtils.isNotBlank(type) && CollectionUtils.isNotEmpty(list)) {
            Map<String, BigDecimal> dateUsageMap = list.stream()
                    .collect(Collectors.toMap(TreatedWaterStatVO::getDateHour, v -> v.getTotalUsage()));
            if (PeriodStatTypeEnum.TODAY.name().equals(type)) {
                String todayDate = DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd");
                IntStream.range(0, 24)
                        .forEach(hour -> {
                            //时间字符处理
                            String todayTime = todayDate + " " + String.format("%02d", hour);
                            if (dateUsageMap.get(todayTime) == null) {
                                list.add(new TreatedWaterStatVO(todayTime, BigDecimal.ZERO));
                            }
                        });
            } else if (PeriodStatTypeEnum.THIS_WEEK.name().equals(type) || PeriodStatTypeEnum.THIS_MONTH.name().equals(type)) {
                var begin = param.getPeriod().getBegin();
                var end = param.getPeriod().getEnd();
                while (begin.isBefore(end)) {
                    String date = DateUtils.format(begin, "yyyy-MM-dd");
                    if (dateUsageMap.get(date) == null) {
                        list.add(new TreatedWaterStatVO(date, BigDecimal.ZERO));
                    }
                    begin = DateUtils.offsetDay(begin, 1);
                }
            } else if (PeriodStatTypeEnum.THIS_YEAR.name().equals(type)) {
                String year = DateUtils.format(LocalDateTime.now(), "yyyy");
                IntStream.range(1, 13)
                        .forEach(month -> {
                            //时间字符处理
                            String curMonth = year + "-" + String.format("%02d", month);
                            if (dateUsageMap.get(curMonth) == null) {
                                list.add(new TreatedWaterStatVO(curMonth, BigDecimal.ZERO));
                            }
                        });
            }
        }
        // 按照dateHour字段升序排列
        Collections.sort(list, Comparator.comparing(TreatedWaterStatVO::getDateHour));
        return list;
    }
}