package com.guhecloud.cube.biz.collection.analysis.gm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.collection.GmCollectionService;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryDayDetailsDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryWeekDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportWeekDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.ParkSettleCategoryDayDetailsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.ParkSettleCategoryDayMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.ParkSettleCategoryWeekMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.TransportDayMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.TransportWeekMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.ParkSettleCategoryDayDetailsPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.ParkSettleCategoryDayPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.ParkSettleCategoryWeekPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.TransportDayPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.TransportWeekPO;
import com.guhecloud.cube.biz.collection.analysis.gm.service.GmParkService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  11:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GmParkServiceImpl implements GmParkService {
    private final GmCollectionService gmCollectionService;
    private final ParkSettleCategoryDayMapper parkSettleCategoryDayMapper;
    private final ParkSettleCategoryDayDetailsMapper parkSettleCategoryDayDetailsMapper;
    private final ParkSettleCategoryWeekMapper parkSettleCategoryWeekMapper;
    private final TransportDayMapper transportDayMapper;
    private final TransportWeekMapper transportWeekMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void parkSettleCategoryDay(List<DataDictionaryDTO> dataDictionaryDTOList, Integer tenant) {
        String deliveryDate = DateUtils.format(DateUtils.offsetDay(LocalDateTime.now(), -1), "yyyy-MM-dd");
        ParkSettleCategoryDayDTO result = gmCollectionService.getParkSettleCategoryDay(dataDictionaryDTOList, deliveryDate);
        ParkSettleCategoryDayPO parkSettleCategoryDay = result.getParkSettleCategoryDayPO(deliveryDate);
        parkSettleCategoryDayMapper.delete(Wrappers.<ParkSettleCategoryDayPO>lambdaQuery()
                                                   .eq(ParkSettleCategoryDayPO::getDeliveryDate, parkSettleCategoryDay.getDeliveryDate()));
        int i = parkSettleCategoryDayMapper.insert(parkSettleCategoryDay);
        log.info("入库条数：{}", i);
        List<ParkSettleCategoryDayDetailsPO> list = ConvertUtils.toList(ParkSettleCategoryDayDetailsPO.class, result.getList());
        list.forEach(ex -> {
            ex.setCategoryId(parkSettleCategoryDay.getId());
            parkSettleCategoryDayDetailsMapper.insert(ex);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void parkSettleCategoryWeek(List<DataDictionaryDTO> dataDictionaryDTOList) {
        var nowTime = LocalDateTime.now();
        String bgnTime = DateUtils.format(DateUtils.offsetDay(nowTime, -7), "yyyy-MM-dd");
        String endTime = DateUtils.format(DateUtils.offsetDay(nowTime, -1), "yyyy-MM-dd");
        List<ParkSettleCategoryWeekDTO> result = gmCollectionService.parkSettleCategoryWeek(dataDictionaryDTOList, bgnTime, endTime);
        List<ParkSettleCategoryWeekPO> parkSettleCategoryDay = ConvertUtils.toList(ParkSettleCategoryWeekPO.class, result);
        List<LocalDateTime> deleteList = new ArrayList<>();
        parkSettleCategoryDay.forEach(ex -> {
            ex.setCreateTime(nowTime);
            parkSettleCategoryWeekMapper.insert(ex);
            deleteList.add(ex.getDays());
        });
        if (!CollectionUtils.isEmpty(deleteList)) {
            parkSettleCategoryWeekMapper.delete(Wrappers.<ParkSettleCategoryWeekPO>lambdaQuery()
                                                        .lt(ParkSettleCategoryWeekPO::getCreateTime, DateUtils.offset(nowTime,
                                                                                                                      ChronoUnit.SECONDS, -1))
                                                        .in(ParkSettleCategoryWeekPO::getDays, deleteList)
                                               );
        }
    }

    @Override
    public void transportDay(List<DataDictionaryDTO> param) {
        var date = DateUtils.offsetDay(LocalDateTime.now(), -1);
        TransportDayDTO result = gmCollectionService.transportDay(param, DateUtils.format(DateUtils.beginOfDay(date), "yyyy-MM-dd HH:ss:mm"), DateUtils.format(DateUtils.endOfDay(date), "yyyy-MM-dd HH:ss:mm"));
        TransportDayPO po = ConvertUtils.convert(TransportDayPO.class, result);
        po.setTransportDate(date);
        transportDayMapper.delete(Wrappers.<TransportDayPO>lambdaQuery()
                                          .eq(TransportDayPO::getTransportDate, DateUtils.beginOfDay(po.getTransportDate())));
        transportDayMapper.insert(po);
    }

    @Override
    public void transportWeek(List<DataDictionaryDTO> param) {
        var nowTime = LocalDateTime.now();
        String bgnTime = DateUtils.format(DateUtils.offsetDay(nowTime, -7), "yyyy-MM-dd");
        String endTime = DateUtils.format(DateUtils.offsetDay(nowTime, -1), "yyyy-MM-dd");
        List<TransportWeekDTO> result = gmCollectionService.transportWeek(param, bgnTime, endTime);
        List<LocalDateTime> deleteList = new ArrayList<>();
        List<TransportWeekPO> insertList = new ArrayList<>();
        result.forEach(ex -> {
            TransportWeekPO po = ConvertUtils.convert(TransportWeekPO.class, ex);
            insertList.add(po);
            deleteList.add(po.getEnterDate());
        });
        if (!CollectionUtils.isEmpty(deleteList)) {
            transportWeekMapper.delete(Wrappers.<TransportWeekPO>lambdaQuery()
                                               .in(TransportWeekPO::getEnterDate, deleteList)
                                      );
        }
        insertList.forEach(transportWeekMapper::insert);
    }

    @Override
    public ParkSettleCategoryDayDTO getGoodsYesterday(LocalDateTime deliveryDate) {
        ParkSettleCategoryDayPO categoryDayPO = parkSettleCategoryDayMapper.selectOne(new LambdaQueryWrapper<ParkSettleCategoryDayPO>()
                                                                                              .eq(ParkSettleCategoryDayPO::getDeliveryDate, deliveryDate));
        if (categoryDayPO == null) {
            return new ParkSettleCategoryDayDTO();
        }
        ParkSettleCategoryDayDTO data = ConvertUtils.convert(ParkSettleCategoryDayDTO.class, categoryDayPO);
        List<ParkSettleCategoryDayDetailsPO> list = parkSettleCategoryDayDetailsMapper.selectList(new LambdaQueryWrapper<ParkSettleCategoryDayDetailsPO>()
                                                                                                          .eq(ParkSettleCategoryDayDetailsPO::getCategoryId, categoryDayPO.getId()));
        data.setList(ConvertUtils.toList(ParkSettleCategoryDayDetailsDTO.class, list));
        return data;
    }

    @Override
    public List<ParkSettleCategoryWeekDTO> getGoodsWeek(Period period) {
        List<ParkSettleCategoryWeekPO> data = parkSettleCategoryWeekMapper.selectList(new LambdaQueryWrapper<ParkSettleCategoryWeekPO>()
                                                                                              .between(ParkSettleCategoryWeekPO::getDays, period.getBegin(), period.getEnd()));
        return ConvertUtils.toList(ParkSettleCategoryWeekDTO.class, data);
    }

    @Override
    public TransportDayDTO getTransportYesterday(LocalDateTime deliveryDate) {
        TransportDayPO transportDayDTO = transportDayMapper.selectOne(new LambdaQueryWrapper<TransportDayPO>()
                                                                              .eq(TransportDayPO::getTransportDate, deliveryDate)
                                                                              .last("limit 1"));
        return ConvertUtils.convert(TransportDayDTO.class, transportDayDTO);
    }

    @Override
    public List<TransportWeekDTO> getTransportWeek(Period period) {
        List<TransportWeekPO> list = transportWeekMapper.selectList(new LambdaQueryWrapper<TransportWeekPO>()
                                                                            .between(TransportWeekPO::getEnterDate, period.getBegin(), period.getEnd()));
        return ConvertUtils.toList(TransportWeekDTO.class, list);
    }
}
