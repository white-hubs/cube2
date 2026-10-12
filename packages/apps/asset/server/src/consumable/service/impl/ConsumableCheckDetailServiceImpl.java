package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtile;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableAddRecordMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCheckDetailMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCheckMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableReduceRecordMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStockDetailMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStoreStockMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheck;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheckDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableReduceRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.AppConsumablePageParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailAddParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableCheckDetailService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.AppConsumablePageVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckDetailVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckExcelVO;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：耗材盘点明细服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableCheckDetailServiceImpl extends ServiceImpl<ConsumableCheckDetailMapper, ConsumableCheckDetail> implements ConsumableCheckDetailService {
    private final ConsumableCheckMapper consumableCheckMapper;
    private final ConsumableStoreStockMapper consumableStoreStockMapper;
    private final FileClient fileClient;
    private final ConsumableAddRecordMapper consumableAddRecordMapper;
    private final ConsumableReduceRecordMapper consumableReduceRecordMapper;
    private final ConsumableStockDetailMapper consumableStockDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ConsumableCheckDetailAddParam param) {
        ConsumableCheckDetail detail = this.baseMapper.selectOne(new LambdaQueryWrapper<ConsumableCheckDetail>()
                                                                         .eq(ConsumableCheckDetail::getCheckId, param.getCheckId())
                                                                         .eq(ConsumableCheckDetail::getConsumableId, param.getConsumableId()));
        detail.setCheckerId(SecurityContextHolder.getPrincipal().getId());
        detail.setCheckerName(SecurityContextHolder.getPrincipal().getName());
        detail.setCheckTime(DateUtils.now());
        detail.setCheckNum(param.getCheckNum());
        detail.setStatus(param.getStatus());
        this.baseMapper.updateById(detail);
        return detail.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ConsumableCheckDetailParam param) {
        ConsumableCheckDetail consumableCheckDetail = this.baseMapper.selectById(id);
        Assert.parameterIsNull(consumableCheckDetail, PropertyStatus.CONSUMABLE_NO_DETAILS);
        ObjectUtils.copying(param, consumableCheckDetail);
        this.baseMapper.updateById(consumableCheckDetail);
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
    public ConsumableCheckDetailVO getById(Long id) {
        ConsumableCheckDetail consumableCheckDetail = this.baseMapper.selectById(id);
        return ObjectUtils.copying(consumableCheckDetail, ConsumableCheckDetailVO.class);
    }

    @Override
    public Pagination<ConsumableCheckDetailVO> getPage(ConsumableCheckDetailQueryParam param) {
        //请自行添加查询条件
        Page<ConsumableCheckDetail> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, ConsumableCheckDetailVO.class);
    }

    @Override
    public Pagination<AppConsumablePageVO> getAppPage(AppConsumablePageParam param) {
        Page<AppConsumablePageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.getAppPage(page, param);
        Pagination<AppConsumablePageVO> pagination = new Pagination<>(page.getTotal(), page.getPages(), ConvertUtils.toList(AppConsumablePageVO.class, page.getRecords()));
        pagination.getRecords().forEach(ex -> {
            // 获取文件信息
            if (ObjectUtils.isNotEmpty(ex.getFileIds())) {
                List<Long> fileIds = Arrays.stream(ex.getFileIds().split(","))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
                List<FileVO> file = fileClient.getByIds(fileIds);
                if (ObjectUtils.isNotEmpty(file)) {
                    ex.setFile(file);
                }
            }
        });
        return pagination;
    }

    @Override
    public void getExcel(HttpServletResponse response, DepositoryDetailedPageExcelParam param) {
        Map<String, Map<String, String>> title = new HashMap<>();
        Map<String, List<?>> data = new HashMap<>();
        String[] arry = param.getIds().split(",");
        for (String id : arry) {
            ConsumableCheck consumableCheck = consumableCheckMapper.selectById(Long.parseLong(id));
            Map<String, String> map = new HashMap<>();
            List<ConsumableCheckExcelVO> excelData = baseMapper.getExcel(id);
            for (ConsumableCheckExcelVO ex : excelData) {
                Integer addNum = baseMapper.getAddNum(ex.getId(), consumableCheck.getStartTime(), consumableCheck.getEndTime());
                Integer reduceNum = baseMapper.getReduceNum(ex.getId(), consumableCheck.getStartTime(), consumableCheck.getEndTime());
                ex.setAddNum(addNum);
                ex.setReduceNum(reduceNum);
                ex.setResidue(this.getBalance(consumableCheck.getStoreId(), consumableCheck.getStartTime(), consumableCheck.getEndTime()));
                ex.setUpperResidue(this.getBalance(consumableCheck.getStoreId(), consumableCheck.getPreviousCheckStartTime(), consumableCheck.getPreviousCheckEndTime()));
                ConsumableStoreStock stocks = consumableStoreStockMapper.selectOne(new LambdaQueryWrapper<ConsumableStoreStock>()
                                                                                           .eq(ConsumableStoreStock::getStoreId, consumableCheck.getStoreId())
                                                                                           .eq(ConsumableStoreStock::getConsumableId, ex.getId()));
                ex.setPrice(0 == stocks.getNum() ? BigDecimal.ZERO : stocks.getAmount()
                        .divide(BigDecimal.valueOf(stocks.getNum()), 2, RoundingMode.HALF_UP));
            }
            map.put("planName", consumableCheck.getName());
            title.put(id, map);
            data.put(id, excelData);
        }
        try {
            log.info("----------------------开始导出耗材盘点单----------------------");
            ExcelUtile.uploadExcelList(response, data, title, "物资盘点.xlsx");
        } catch (
                Exception exception) {
            log.error("耗材盘点单导出:{}", exception.getLocalizedMessage());
        }
    }

    /**
     * 获取某个时间段的耗材结余
     */
    private Integer getBalance(Long storeId, LocalDateTime startTime, LocalDateTime endTime) {
        ConsumableAddRecord addBalance;
        ConsumableReduceRecord reduceRecordBalance;
        if (ObjectUtils.isNotEmpty(startTime) && ObjectUtils.isNotEmpty(endTime)) {
            addBalance = consumableAddRecordMapper.selectOne(new LambdaQueryWrapper<ConsumableAddRecord>()
                                                                     .eq(ConsumableAddRecord::getStoreId, storeId)
                                                                     .eq(ConsumableAddRecord::getStatus, "PASS")
                                                                     .between(ConsumableAddRecord::getUpdateTime, startTime, DateUtils.endOfDay(endTime))
                                                                     .orderByDesc(ConsumableAddRecord::getUpdateTime)
                                                                     .last("limit 1"));
            reduceRecordBalance = consumableReduceRecordMapper.selectOne(new LambdaQueryWrapper<ConsumableReduceRecord>()
                                                                                 .eq(ConsumableReduceRecord::getStoreId, storeId)
                                                                                 .eq(ConsumableReduceRecord::getStatus, "PASS")
                                                                                 .between(ConsumableReduceRecord::getUpdateTime, startTime, DateUtils.endOfDay(endTime))
                                                                                 .orderByDesc(ConsumableReduceRecord::getUpdateTime)
                                                                                 .last("limit 1"));
        } else {
            addBalance = consumableAddRecordMapper.selectOne(new LambdaQueryWrapper<ConsumableAddRecord>()
                                                                     .eq(ConsumableAddRecord::getStoreId, storeId)
                                                                     .eq(ConsumableAddRecord::getStatus, "PASS")
                                                                     .orderByDesc(ConsumableAddRecord::getUpdateTime)
                                                                     .last("limit 1"));
            reduceRecordBalance = consumableReduceRecordMapper.selectOne(new LambdaQueryWrapper<ConsumableReduceRecord>()
                                                                                 .eq(ConsumableReduceRecord::getStoreId, storeId)
                                                                                 .eq(ConsumableReduceRecord::getStatus, "PASS")
                                                                                 .orderByDesc(ConsumableReduceRecord::getUpdateTime)
                                                                                 .last("limit 1"));
        }

        Long stockId = 0L;
        // 最后一次出入库是否为入库
        Boolean warehousing = false;
        if (ObjectUtils.isNotEmpty(addBalance) && ObjectUtils.isNotEmpty(reduceRecordBalance)) {
            stockId = DateUtils.compare(addBalance.getUpdateTime(), reduceRecordBalance.getUpdateTime()) > 0 ? addBalance.getId()
                                                                                                             : reduceRecordBalance.getId();
            warehousing = DateUtils.compare(addBalance.getUpdateTime(), reduceRecordBalance.getUpdateTime()) > 0;
        } else if (ObjectUtils.isNotEmpty(addBalance)) {
            warehousing = true;
            stockId = addBalance.getId();
        } else if (ObjectUtils.isNotEmpty(reduceRecordBalance)) {
            stockId = reduceRecordBalance.getId();
        }
        if (stockId > 1) {
            ConsumableStockDetail stockDetail = consumableStockDetailMapper.selectOne(new LambdaQueryWrapper<ConsumableStockDetail>()
                                                                                              .eq(ConsumableStockDetail::getStockId, stockId));
            if (ObjectUtils.isNotEmpty(stockDetail)) {
                return warehousing ? stockDetail.getInitNum() + stockDetail.getNum() : stockDetail.getInitNum() - stockDetail.getNum();
            }
        }
        return 0;
    }
}