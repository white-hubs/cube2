package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.property.management.client.property.vo.property.MoneyNumVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.dao.StorehouseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.common.entity.Storehouse;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStockDetailMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStoreStockMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableReduceRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableInStoreParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableCategoryService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStoreStockService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCategoryVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumablePriceVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockVO;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 描述：仓库耗材库存服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableStoreStockServiceImpl extends ServiceImpl<ConsumableStoreStockMapper, ConsumableStoreStock> implements ConsumableStoreStockService {
    private final ConsumableCategoryService consumableCategoryService;
    private final StorehouseMapper storehouseMapper;
    private final FileClient fileClient;
    private final ConsumableStockDetailMapper consumableStockDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.lambdaUpdate().set(ConsumableStoreStock::getDeleted, 1)
                .eq(ConsumableStoreStock::getConsumableId, id).update();
    }

    @Override
    public ConsumableStoreStockVO getById(Long id) {
        ConsumableStoreStock consumableStoreStock = this.baseMapper.selectById(id);
        return ObjectUtils.copying(consumableStoreStock, ConsumableStoreStockVO.class);
    }

    @Override
    public Pagination<ConsumableStoreStockExtendVO> getPage(ConsumableInStoreParam param) {
        //请自行添加查询条件
        Page<ConsumableStoreStockExtendVO> page = this.baseMapper.getStackInStore(PagingUtils.convert(param), param);
        //扩展耗材信息
        this.extend(page.getRecords(), param.getStoreId());

        return PagingUtils.convert(page);
    }

    /**
     * 统计即时库存总金额
     */
    @Override
    public MoneyNumVO getTotalMoney(String keyword, String type) {
        MoneyNumVO data = baseMapper.getTotalMoney(keyword, type);
        if (ObjectUtils.isEmpty(data)) {
            return new MoneyNumVO(BigDecimal.ZERO, 0);
        }
        if (ObjectUtils.isEmpty(data.getTotalMoney())) {
            data.setTotalMoney(BigDecimal.ZERO);
        }
        if (ObjectUtils.isEmpty(data.getTotalNum())) {
            data.setTotalNum(0);
        }
        return data;
    }

    //扩展耗材信息
    private void extend(List<ConsumableStoreStockExtendVO> datas, Long storeId) {
        if (!CollectionUtils.isEmpty(datas)) {
            datas.forEach(d -> {
                if (ObjectUtils.isNotEmpty(storeId)) {
                    d.setStock(d.getStoreNum());
                }
                ConsumableCategoryVO category = consumableCategoryService.getById(d.getCategoryId());
                d.setCategoryName(category != null ? category.getName() : "");
                Storehouse storehouse = storehouseMapper.selectById(d.getStoreId());
                d.setStoreName(storehouse != null ? storehouse.getName() : "");
                //查询附件
                if (!StringUtils.isEmpty(d.getFileIds())) {
                    List<Long> fileIds = Arrays.stream(d.getFileIds().split(",")).map(id -> Long.parseLong(id))
                            .collect(Collectors.toList());
                    d.setFiles(fileClient.getByIds(fileIds));
                }
            });
        }
    }

    @Override
    public void insertOrUpdateStock(ConsumableAddRecord addRecord) {
        // 入库的耗材信息
        List<ConsumableStockDetail> stockDetails = consumableStockDetailMapper.selectList(new LambdaQueryWrapper<ConsumableStockDetail>()
                                                                                                  .eq(ConsumableStockDetail::getStockId, addRecord.getId()));
        List<ConsumableStoreStock> addData = new ArrayList<>();
        // 处理新增的耗材
        for (ConsumableStockDetail ex : stockDetails) {
            ConsumableStoreStock data = baseMapper.selectOne(new LambdaQueryWrapper<ConsumableStoreStock>()
                                                                     .eq(ConsumableStoreStock::getConsumableId, ex.getConsumableId())
                                                                     .eq(ConsumableStoreStock::getStoreId, addRecord.getStoreId()));
            if (ObjectUtils.isNotEmpty(data)) {
                // 重新计算数量、单价、金额
                data.setNum(data.getNum() + ex.getNum());
                data.setAmount(data.getAmount().add(ex.getAmount()));
                data.setAvgPrice(data.getAmount().divide(new BigDecimal(data.getNum()), 2, BigDecimal.ROUND_HALF_UP));
                baseMapper.updateById(data);
            } else {
                ConsumableStoreStock add = ConsumableStoreStock.builder()
                        .id(IdWorker.getId())
                        .storeId(addRecord.getStoreId())
                        .consumableId(ex.getConsumableId())
                        .num(ex.getNum())
                        .avgPrice(ex.getAmount().divide(new BigDecimal(ex.getNum()), 2, BigDecimal.ROUND_HALF_UP))
                        .amount(ex.getAmount())
                        .occupyNum(0)
                        .tenant(addRecord.getTenant())
                        .createBy(addRecord.getCreateBy())
                        .creatorId(addRecord.getCreatorId())
                        .creatorType(addRecord.getCreatorType())
                        .createTime(LocalDateTime.now())
                        .updateBy(addRecord.getCreateBy())
                        .updaterId(addRecord.getCreatorId())
                        .updaterType(addRecord.getCreatorType())
                        .updateTime(LocalDateTime.now())
                        .deleted(false)
                        .build();
                addData.add(add);
            }
        }
        if (ObjectUtils.isNotEmpty(addData)) {
            boolean bool = this.baseMapper.insertStock(addData) < 1;
            Assert.isUnknownError(bool, PropertyStatus.CONSUMABLE_ADD);
        }
    }

    @Override
    public void insertOrUpdateStock(ConsumableReduceRecord reduceRecord, List<ConsumableStockDetail> stockDetail) {
        stockDetail.stream().forEach(d -> {
            ConsumableStoreStock storeStock = baseMapper.selectOne(new LambdaQueryWrapper<ConsumableStoreStock>()
                                                                           .eq(ConsumableStoreStock::getStoreId, reduceRecord.getStoreId())
                                                                           .eq(ConsumableStoreStock::getConsumableId, d.getConsumableId()));
            // 判断库存是否充足
            Assert.isNotAcceptable(d.getNum() > storeStock.getNum(), PropertyStatus.CONSUMABLE_LACK);
            // 计算出库后的金额、数量等
            storeStock.setNum(storeStock.getNum() - d.getNum());
            storeStock.setOccupyNum(storeStock.getOccupyNum() - d.getNum());
            storeStock.setAmount(
                    storeStock.getNum() < 1 ? BigDecimal.ZERO : storeStock.getAvgPrice().multiply(new BigDecimal(storeStock.getNum())));
            Boolean bool = this.baseMapper.updateById(storeStock) < 1;
            Assert.isUnknownError(bool, PropertyStatus.CONSUMABLE_RESTRICT_NO_DATA);
        });
    }

    @Override
    public Map<Long, BigDecimal> getConsumableStorePrice(Long storeId, List<ConsumableStockDetailParam> details) {
        List<Long> consumableIds = Optional.ofNullable(details).orElse(Collections.emptyList())
                .stream().map(d -> d.getConsumableId()).collect(Collectors.toList());
        List<ConsumableStoreStock> consumableStoreStocks = this.baseMapper.selectList(Wrappers.<ConsumableStoreStock>lambdaQuery()
                                                                                              .eq(ConsumableStoreStock::getStoreId, storeId)
                                                                                              .in(!CollectionUtils.isEmpty(consumableIds), ConsumableStoreStock::getConsumableId, consumableIds));
        if (!CollectionUtils.isEmpty(consumableStoreStocks)) {
            return consumableStoreStocks.stream().map(d -> {
                //计算均价
                BigDecimal avgPrice = d.getAmount().divide(new BigDecimal(d.getNum()), 2, BigDecimal.ROUND_HALF_UP);
                return ConsumablePriceVO.builder()
                        .id(d.getConsumableId())
                        .storeId(storeId)
                        .price(avgPrice).build();
            }).collect(Collectors.toMap(ConsumablePriceVO::getId, d -> d.getPrice()));
        }
        return Maps.newHashMapWithExpectedSize(0);
    }

    @Override
    public Integer getConsumableNumber() {
        Integer num = baseMapper.getConsumableNumber(null);
        return Optional.ofNullable(num).orElse(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOccupyStock(Long storeId, Long consumableId, Integer num, Boolean add) {
        ConsumableStoreStock storeStock = baseMapper.selectOne(new LambdaQueryWrapper<ConsumableStoreStock>()
                                                                       .eq(ConsumableStoreStock::getStoreId, storeId)
                                                                       .eq(ConsumableStoreStock::getConsumableId, consumableId));
        Integer occupyNum = storeStock.getOccupyNum() == null ? 0 : storeStock.getOccupyNum();
        int currentNum = add ? occupyNum + num : occupyNum - num;
        storeStock.setOccupyNum(currentNum);
        Boolean bool = this.baseMapper.updateById(storeStock) < 1;
        Assert.isUnknownError(bool, PropertyStatus.CONSUMABLE_RESTRICT_NO_DATA);
    }
}