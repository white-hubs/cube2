package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyCollectPeriodMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectPeriod;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodBatchParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyCollectPeriodService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectPeriodVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;

/**
 * 描述：能耗采集时段定义服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyCollectPeriodServiceImpl extends ServiceImpl<EnergyCollectPeriodMapper, EnergyCollectPeriod> implements EnergyCollectPeriodService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EnergyCollectPeriodParam param) {
        EnergyCollectPeriod energyCollectPeriod = ObjectUtils.copying(param, EnergyCollectPeriod.class);
        this.baseMapper.insert(energyCollectPeriod);
        return energyCollectPeriod.getId();
    }

    @Override
    public void addBatch(EnergyCollectPeriodBatchParam batchParam) {
        //删除之前的配置
        this.baseMapper.delete(Wrappers.<EnergyCollectPeriod>lambdaQuery()
                                       .eq(EnergyCollectPeriod::getInstrumentType, batchParam.getInstrumentType()));
        //保存现有的数据
        if (CollectionUtils.isNotEmpty(batchParam.getDataList())) {
            //检查时间段是否重复
            Assert.isConflict(checkPeriodOverlap(batchParam.getDataList()), "时间段重叠，请更正后再试");
            batchParam.getDataList().stream().forEach(data -> {
                data.setInstrumentType(batchParam.getInstrumentType());
                EnergyCollectPeriod energyCollectPeriod = ObjectUtils.copying(data, EnergyCollectPeriod.class);
                this.baseMapper.insert(energyCollectPeriod);
            });
        }
    }

    //检查时间段是否重叠
    private Boolean checkPeriodOverlap(List<EnergyCollectPeriodParam> periodDefines) {
        for (int i = 0; periodDefines != null && i < periodDefines.size(); i++) {
            EnergyCollectPeriodParam timePeriod1 = periodDefines.get(i);
            if (StringUtils.isNotBlank(timePeriod1.getStartTime()) && StringUtils.isNotBlank(timePeriod1.getEndTime())) {
                long startTime1 = DateUtils.toTimestamp(DateUtils.parse(formatTimeInterval(timePeriod1.getStartTime())));
                long endTime1 = DateUtils.toTimestamp(DateUtils.parse(formatTimeInterval(timePeriod1.getEndTime())));
                for (int j = i + 1; j < periodDefines.size(); j++) {
                    EnergyCollectPeriodParam timePeriod2 = periodDefines.get(j);
                    if (StringUtils.isNotBlank(timePeriod2.getStartTime()) && StringUtils.isNotBlank(timePeriod2.getEndTime())) {
                        long startTime2 = DateUtils.toTimestamp(DateUtils.parse(formatTimeInterval(timePeriod2.getStartTime())));
                        long endTime2 = DateUtils.toTimestamp(DateUtils.parse(formatTimeInterval(timePeriod2.getEndTime())));
                        if (!(endTime1 <= startTime2 || endTime2 <= startTime1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param time 07:00:00
     *
     * @return 1970-01-01 07:00:00
     */
    private String formatTimeInterval(String time) {
        return String.format("1970-01-01 %s", time);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EnergyCollectPeriodParam param) {
        EnergyCollectPeriod energyCollectPeriod = this.baseMapper.selectById(id);
        Assert.isNotFound(energyCollectPeriod, "未查询到相关信息");
        ObjectUtils.copying(param, energyCollectPeriod);
        this.baseMapper.updateById(energyCollectPeriod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EnergyCollectPeriodVO getById(Long id) {
        EnergyCollectPeriod energyCollectPeriod = this.baseMapper.selectById(id);
        return ObjectUtils.copying(energyCollectPeriod, EnergyCollectPeriodVO.class);
    }

    @Override
    public Pagination<EnergyCollectPeriodVO> getPage(EnergyCollectPeriodQueryParam param) {
        //请自行添加查询条件
        Page<EnergyCollectPeriod> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EnergyCollectPeriodVO.class);
    }

    @Override
    public String getPeriodFlagInFPG(String instrumentType, LocalDateTime dateTime) {
        String itype = StringUtils.isNotBlank(instrumentType) ? instrumentType : "default";
        List<EnergyCollectPeriod> periodList = this.baseMapper.selectList(Wrappers.<EnergyCollectPeriod>lambdaQuery()
                                                                                  .eq(EnergyCollectPeriod::getInstrumentType, itype)
                                                                                  .orderByAsc(EnergyCollectPeriod::getSort));
        if (CollectionUtils.isNotEmpty(periodList)) {
            EnergyCollectPeriod energyCollectPeriod = periodList.stream().filter(p -> {
                String currentDate = DateUtils.format(dateTime, DEFAULT_DATE_FORMAT);
                if (StringUtils.isNotBlank(p.getStartTime()) &&
                    StringUtils.isNotBlank(p.getEndTime())) {
                    LocalDateTime startTime = DateUtils.parse(currentDate + " " + p.getStartTime());
                    LocalDateTime endTime = DateUtils.parse(currentDate + " " + p.getEndTime());
                    if (DateUtils.toTimestamp(dateTime) >= DateUtils.toTimestamp(startTime) &&
                        DateUtils.toTimestamp(dateTime) < DateUtils.toTimestamp(endTime)) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }).findFirst().orElse(null);
            return energyCollectPeriod != null ? energyCollectPeriod.getCode() : null;
        }
        return null;

    }
}