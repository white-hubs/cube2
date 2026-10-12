package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyFundInoutRecordMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFundInoutRecord;
import com.guhecloud.cube.biz.emergency.server.param.CommonStatParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFundInoutRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFundInoutRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFundInoutRecordService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFundInoutRecordVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFundInoutStatVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 描述：应急资金收支记录服务层
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFundInoutRecordServiceImpl extends ServiceImpl<EmergencyFundInoutRecordMapper, EmergencyFundInoutRecord> implements EmergencyFundInoutRecordService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencyFundInoutRecordParam param) {
        EmergencyFundInoutRecord emergencyFundInoutRecord = ObjectUtils.copying(param, EmergencyFundInoutRecord.class);
        this.baseMapper.insert(emergencyFundInoutRecord);
        return emergencyFundInoutRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencyFundInoutRecordParam param) {
        EmergencyFundInoutRecord emergencyFundInoutRecord = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencyFundInoutRecord, "未查询到相关信息");
        ObjectUtils.copying(param, emergencyFundInoutRecord);
        this.baseMapper.updateById(emergencyFundInoutRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EmergencyFundInoutRecordVO getById(Long id) {
        EmergencyFundInoutRecord emergencyFundInoutRecord = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencyFundInoutRecord, EmergencyFundInoutRecordVO.class);
    }

    @Override
    public Pagination<EmergencyFundInoutRecordVO> getPage(EmergencyFundInoutRecordQueryParam param) {
        //请自行添加查询条件
        Period period = param.getPeriod();
        Page<EmergencyFundInoutRecord> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getInoutTag()), EmergencyFundInoutRecord::getInoutTag, param.getInoutTag())
                .eq(StringUtils.isNotBlank(param.getInoutType()), EmergencyFundInoutRecord::getInoutType, param.getInoutType())
                .between(period !=
                         null, EmergencyFundInoutRecord::getInoutTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EmergencyFundInoutRecordVO.class);
    }

    @Override
    public List<EmergencyFundInoutStatVO> fundInoutStatByDate(EmergencyFundInoutRecordQueryParam param) {
        return this.baseMapper.fundInoutStatByDate(PagingUtils.convert(param), param);
    }

    @Override
    public List<EmergencyFundInoutStatVO> fundInoutTrendStatByDate(CommonStatParam param) {
        this.initStatParam(param);
        List<EmergencyFundInoutStatVO> trendStatByDate = this.baseMapper.fundInoutTrendStatByDate(param);
        return this.checkTrendData(trendStatByDate, param.getType(), param.getPeriod());
    }

    /**
     * 初始化统计参数
     *
     * @param param
     */
    private void initStatParam(CommonStatParam param) {
        //根据统计类型，默认分组的时间格式
        if ("TODAY".equals(param.getType())) {
            param.setGroupFormat("%H");
        } else if ("THIS_WEEK".equals(param.getType()) || "THIS_MONTH".equals(param.getType())) {
            param.setGroupFormat("%Y-%m-%d");
        } else if ("THIS_YEAR".equals(param.getType())) {
            param.setGroupFormat("%Y-%m");
        } else {
            param.setGroupFormat("%Y-%m");
        }
        if (param.getPeriod() == null) {//默认本月
            LocalDateTime startDate = DateUtils.beginOfMonth(DateUtils.now());
            LocalDateTime endDate = DateUtils.now();
            param.setPeriod(Period.of(startDate, endDate));
        }
    }

    private List<EmergencyFundInoutStatVO> checkTrendData(List<EmergencyFundInoutStatVO> dataList, String statType, Period period) {
        LocalDateTime start = period.getBegin();
        LocalDateTime end = period.getEnd();
        if (statType.equals("THIS_WEEK") || statType.equals("THIS_MONTH")) {
            Stream.iterate(start, day -> day.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(start, end) + 1)
                    .forEach(date -> {
                        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        EmergencyFundInoutStatVO vo = dataList.stream().filter(d -> d.getDay().equals(dateStr)).findFirst().orElse(null);
                        if (vo == null) {
                            dataList.add(new EmergencyFundInoutStatVO(dateStr, BigDecimal.ZERO, BigDecimal.ZERO));
                        }
                    });
        } else if (statType.equals("TODAY")) {
            IntStream.range(0, 24).forEach(hour -> {
                EmergencyFundInoutStatVO vo = dataList.stream().filter(d -> d.getDay().equals(String.valueOf(hour))).findFirst()
                        .orElse(null);
                if (vo == null) {
                    dataList.add(new EmergencyFundInoutStatVO(String.format("%02d", hour), BigDecimal.ZERO, BigDecimal.ZERO));
                }
            });
        } else if (statType.equals("THIS_YEAR")) {
            Stream.iterate(start, day -> day.plusMonths(1))
                    .limit(ChronoUnit.MONTHS.between(start, end) + 1)
                    .forEach(date -> {
                        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                        EmergencyFundInoutStatVO vo = dataList.stream().filter(d -> d.getDay().equals(dateStr)).findFirst().orElse(null);
                        if (vo == null) {
                            dataList.add(new EmergencyFundInoutStatVO(dateStr, BigDecimal.ZERO, BigDecimal.ZERO));
                        }
                    });
        }
        dataList.sort(Comparator.comparing(EmergencyFundInoutStatVO::getDay));
        return dataList;
    }
}