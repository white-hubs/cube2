package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.property.management.client.common.param.StatisticalChartParam;
import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.dao.StorehouseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.common.entity.Storehouse;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.OrganizationClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtile;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCategoryMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCheckMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableReduceRecordMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStockDetailMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStoreStockMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.VisualizationConsumableMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.Consumable;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheck;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableReduceRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.StockTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceRecordQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableAddRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableReduceRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStockDetailService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStoreStockService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceRecordExcelVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceRecordVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableReduceStockStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.FlowResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import instance.event.ProcessCompletedMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.guhecloud.platform.base.util.DateUtils.DEFAULT_YEAR_FORMATTER;

/**
 * 描述：耗材出库记录服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableReduceRecordServiceImpl extends ServiceImpl<ConsumableReduceRecordMapper, ConsumableReduceRecord> implements ConsumableReduceRecordService {
    private final ConsumableStockDetailService consumableStockDetailService;
    private final ConsumableStoreStockService consumableStoreStockService;
    private final OrganizationClient organizationClient;
    private final OrganizationClientService organizationClientService;
    private final ConsumableCategoryMapper consumableCategoryMapper;
    private final StorehouseMapper storehouseMapper;
    private final UserClientService userClientService;
    private final ConsumableMapper consumableMapper;
    private final ConsumableStockDetailMapper consumableStockDetailMapper;
    private final ConsumableCheckMapper consumableCheckMapper;
    private final ConsumableStoreStockMapper consumableStoreStockMapper;
    private final VisualizationConsumableMapper visualizationConsumableMapper;
    private final ConsumableAddRecordService consumableAddRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ConsumableReduceRecordParam param) {
        ConsumableReduceRecord consumableReduceRecord = ObjectUtils.copying(param, ConsumableReduceRecord.class);
        this.baseMapper.insert(consumableReduceRecord);
        return consumableReduceRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ConsumableReduceRecordParam param) {
        ConsumableReduceRecord consumableReduceRecord = this.baseMapper.selectById(id);
        Assert.isNotFound(consumableReduceRecord, "未查询到相关信息");
        ObjectUtils.copying(param, consumableReduceRecord);
        this.baseMapper.updateById(consumableReduceRecord);
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
    public ConsumableReduceRecordVO getById(Long id) {
        ConsumableReduceRecord consumableReduceRecord = this.baseMapper.selectById(id);
        return ObjectUtils.copying(consumableReduceRecord, ConsumableReduceRecordVO.class);
    }

    @Override
    public Pagination<ConsumableReduceRecordVO> getPage(ConsumableReduceRecordQueryParam param) {
        //请自行添加查询条件
        Page<ConsumableReduceRecordVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<ConsumableReduceRecordVO> pageData = this.baseMapper.getPage(page, param);
        Pagination<ConsumableReduceRecordVO> pagination = PagingUtils.convert(pageData, ConsumableReduceRecordVO.class);
        for (ConsumableReduceRecordVO record : pagination.getRecords()) {
            record.setClaimantName(userClientService.getUser(record.getClaimant()));
            record.setHandlerName(userClientService.getUser(record.getHandler()));
            OrganizationDTO org = organizationClient.getOrganizationsById(record.getDeptId());
            if (ObjectUtils.isNotEmpty(org)) {
                record.setDeptName(org.getName());
            }
            record.setStoreName(storehouseMapper.selectById(record.getStoreId()).getName());
        }
        return pagination;
    }

    @Override
    public Long reduceWithDetail(ConsumableReduceStockParam param) {
        if (StringUtils.isNotBlank(param.getProcessInstanceId())) {
            boolean exists = this.lambdaQuery().eq(ConsumableReduceRecord::getProcessInstanceId, param.getProcessInstanceId()).exists();
            if (exists) {
                return 0L;
            }
        }
        // 出库条件验证
        this.checking(null, param.getStoreId(), param.getDetails(), param.getReduceDate(), param.getClaimant());
        ConsumableReduceRecord record = param.getConsumableReduceRecord();
        //保存出库记录
        this.save(record);
        //保存明细
        List<ConsumableStockDetailParam> details = param.getDetails();

        if (!CollectionUtils.isEmpty(details)) {
            Map<Long, BigDecimal> priceMap = consumableStoreStockService.getConsumableStorePrice(record.getStoreId(), param.getDetails());
            details.stream().forEach(d -> {
                BigDecimal avgPrice = priceMap.get(d.getConsumableId());//获取均价
                avgPrice = avgPrice != null ? avgPrice : BigDecimal.ZERO;
                d.setStockId(record.getId());
                d.setPrice(avgPrice);
                d.setAmount(avgPrice.multiply(new BigDecimal(d.getNum())));
                d.setStockFlag(StockTypeEnum.REDUCE.name());
                //扩展初始库存信息
                consumableStockDetailService.extendInitStockInfo(record.getStoreId(), d);
                //更新占用库存
                consumableStoreStockService.updateOccupyStock(record.getStoreId(), d.getConsumableId(), d.getNum(), true);
                consumableStockDetailService.add(d);
            });
        }
        return record.getId();
    }

    @Override
    public List<ConsumableReduceStockStatVO> getReduceStockStat(ConsumableReduceRecordParam param) {
        List<ConsumableReduceStockStatVO> reduceStockStat = this.baseMapper.getReduceStockStat(param);
        if (!CollectionUtils.isEmpty(reduceStockStat)) {
            reduceStockStat.stream().forEach(d -> {
                OrganizationDTO dept = organizationClient.getOrganizationsById(d.getDeptId());
                d.setDeptName(dept != null ? dept.getName() : "");
            });
            return reduceStockStat;
        }
        return Collections.emptyList();
    }

    @Override
    public List<PieChartValueVO> getReduceStockStatByDep(String depId, String period) {
        StatisticalChartParam param = new StatisticalChartParam();
        param.setPeriod(period);
        List<PieChartValueVO> data = visualizationConsumableMapper.consumableByDep(param.getPeriodTiem());
        BigDecimal totalRatio = BigDecimal.ZERO;
        for (PieChartValueVO ex : data) {
            totalRatio = totalRatio.add(ex.getValue());
        }
        List<PieChartValueVO> list = new ArrayList<>();
        for (PieChartValueVO ex : data) {
            ex.setTotalRatio(MathUtils.calculateProportionPercentage(ex.getValue(), totalRatio));
            if (ObjectUtils.isNotEmpty(ex)) {
                Long orgId = Long.parseLong(ex.getName());
                if (ObjectUtils.isNotEmpty(orgId)) {
                    OrganizationDTO org = organizationClient.getOrganizationsById(orgId);
                    if (ObjectUtils.isNotEmpty(org)) {
                        list.add(new PieChartValueVO(org.getName(), ex.getValue(), null, ex.getTotalRatio()));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Pagination<ConsumableStoreStockExtendVO> getReduceDetail(ConsumableReduceRecordParam param) {
        Page<ConsumableStoreStockExtendVO> page = this.baseMapper.getReduceDetail(PagingUtils.convert(param), param);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().stream().forEach(d -> {
                //类型名称
                ConsumableCategory category = consumableCategoryMapper.selectById(d.getCategoryId());
                d.setCategoryName(category != null ? category.getName() : "");
                //仓库名称
                Storehouse storehouse = storehouseMapper.selectById(d.getStoreId());
                d.setStoreName(storehouse != null ? storehouse.getName() : "");
                //部门名称
                OrganizationDTO dept = organizationClient.getOrganizationsById(d.getDeptId());
                d.setDeptName(dept != null ? dept.getName() : "");
            });
        }
        return PagingUtils.convert(page);
    }

    @Override
    public void getExcel(HttpServletResponse response, DepositoryDetailedPageExcelParam param) {
        Map<String, Map<String, String>> title = new HashMap<>();
        Map<String, List<?>> data = new HashMap<>();
        String[] array = param.getIds().split(",");
        for (String id : array) {
            Map<String, String> map = new HashMap<>();
            List<ConsumableReduceRecordExcelVO> excelData = baseMapper.getExcel(id);
            for (ConsumableReduceRecordExcelVO ex : excelData) {
                ex.setDeptName(organizationClientService.getOrganizationsById(ex.getDeptName()));
                ex.setClaimant(userClientService.getUser(ex.getClaimant()));
            }
            title.put(id, map);
            data.put(id, excelData);
        }
        try {
            log.info("----------------------开始导出物资领用单----------------------");
            ExcelUtile.uploadExcelList(response, data, title, "物资领用单.xlsx");
        } catch (Exception exception) {
            log.error("耗材申请单导出--{}", exception.getLocalizedMessage());
        }
    }

    @Override
    public Boolean update(WorkflowRelevance param) {
        ConsumableReduceRecord reduceRecord = baseMapper.selectById(param.getPropertyId());
        reduceRecord.setProcessInstanceId(param.getProcessInstanceId());
        return baseMapper.updateById(reduceRecord) > 0;
    }

    @Override
    public Boolean submit(ProcessCompletedMessage message) {
        ConsumableReduceRecord reduceRecord = baseMapper.selectOne(new LambdaQueryWrapper<ConsumableReduceRecord>()
                                                                           .eq(ConsumableReduceRecord::getProcessInstanceId, message.getProcessInstanceId())
                                                                           .last("limit 1"));
        if (ObjectUtils.isEmpty(reduceRecord)) {
            log.info("耗材出库消费失败：未找到出库记录：{}", message.getProcessInstanceId());
            return false;
        } else if (!FlowResult.UNDER.name().equals(reduceRecord.getStatus())) {
            log.info("耗材出库消费失败：重复消费：{}；审核结果", message.getProcessInstanceId(), reduceRecord.getStatus());
            return false;
        }
        if (FlowResult.PASS.name().equals(message.getResult().name())) {
            List<ConsumableStockDetail> stockDetail = consumableStockDetailMapper.selectList(new LambdaQueryWrapper<ConsumableStockDetail>()
                                                                                                     .eq(ConsumableStockDetail::getStockId, reduceRecord.getId()));
            List<ConsumableStockDetailParam> detailParams = ConvertUtils.toList(ConsumableStockDetailParam.class, stockDetail);
            // 出库条件验证
            this.checking(FlowResult.PASS.name(), reduceRecord.getStoreId(), detailParams, reduceRecord.getReduceDate(), reduceRecord.getClaimant());
            // 更新仓库库存
            this.consumableStoreStockService.insertOrUpdateStock(reduceRecord, stockDetail);
            // 更新耗材的总库存、总金额
            for (ConsumableStockDetail ex : stockDetail) {
                Consumable consumable = consumableMapper.selectById(ex.getConsumableId());
                consumable.setId(ex.getConsumableId());
                Integer stock = consumable.getStock() - ex.getNum();
                BigDecimal amount = consumable.getAmount().subtract(ex.getAmount());
                consumable.setStock(stock < 0 ? 0 : stock);
                consumable.setAmount(stock < 0 ? BigDecimal.ZERO : amount);
                consumableMapper.updateById(consumable);
            }
            // 发送短信
            try {
                consumableAddRecordService.sendMsg(stockDetail);
            } catch (Exception e) {
                log.error("发送短信异常：{}", e);
            }
        } else {
            //更新占用库存
            List<ConsumableStockDetail> stockDetail = consumableStockDetailMapper.selectList(new LambdaQueryWrapper<ConsumableStockDetail>()
                                                                                                     .eq(ConsumableStockDetail::getStockId, reduceRecord.getId()));
            for (ConsumableStockDetail consumableStockDetail : stockDetail) {
                consumableStoreStockService.updateOccupyStock(reduceRecord.getStoreId(),
                                                              consumableStockDetail.getConsumableId(), consumableStockDetail.getNum(), false);
            }
        }
        reduceRecord.setStatus(message.getResult().name());
        baseMapper.updateById(reduceRecord);
        return true;
    }

    /**
     * 出库限制
     *
     * @param storeId    仓库id
     * @param details    出库历史
     * @param reduceDate 出库时间
     * @param claimant   领用人员id
     */
    @Override
    public void checking(String flowResult, Long storeId, List<ConsumableStockDetailParam> details,
                         LocalDateTime reduceDate, Long claimant
                        ) {
        log.info("出库条件判断");
        // 盘点该仓库是否在盘点中
        Boolean bool = consumableCheckMapper.selectCount(new LambdaQueryWrapper<ConsumableCheck>()
                                                                 .eq(ConsumableCheck::getStoreId, storeId)
                                                                 .eq(ConsumableCheck::getStatus, TaskStatusEnum.UNDONE.name())) > 0;
        Assert.parameterIsNull(bool, PropertyStatus.CONSUMABLE_CHECK_RESTRICT);
        // 判断库存是否充足
        details.forEach(ex -> {
            // 盘点仓库中库存是否充足
            ConsumableStoreStock storeStock = consumableStoreStockMapper.selectOne(new LambdaQueryWrapper<ConsumableStoreStock>()
                                                                                           .eq(ConsumableStoreStock::getStoreId, storeId)
                                                                                           .eq(ConsumableStoreStock::getConsumableId, ex.getConsumableId()));
            //审批通过时直接用实际库存判断不减去占用库存
            int occupyNum = 0;
            if (StringUtils.isBlank(flowResult)) {
                occupyNum = storeStock.getOccupyNum() == null || storeStock.getOccupyNum() < 0 ? 0 : storeStock.getOccupyNum();
            }
            Assert.parameterIsNull((storeStock.getNum() - occupyNum) < ex.getNum(), PropertyStatus.CONSUMABLE_REDUCE_STOCK);
            Consumable consumable = consumableMapper.selectById(ex.getConsumableId());
            if (ObjectUtils.isNotEmpty(consumable.getUseLimit())) {
                // 判断是否超过领用上限
                String date = DateUtils.format(reduceDate, DEFAULT_YEAR_FORMATTER);
                Integer num = consumableMapper.getUseLimit(claimant, consumable.getId(), date, flowResult);
                Assert.isNotAcceptable(consumable.getUseLimit() - Optional.ofNullable(num).orElse(0) <
                                       ex.getNum(), PropertyStatus.CONSUMABLE_REDUCE_RESTRICT);

            }
        });
    }
}