package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.property.management.client.common.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.dao.StorehouseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.common.entity.Storehouse;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCategoryMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCheckDetailMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCheckMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStoreStockMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheck;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheckDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.StockTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableAddRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableCheckService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckDetailStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckWithStatVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTask;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.CheckTaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.MessageModuleTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.MessageUtil;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.PropertyTaskCenterUtil;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.PropertyTaskTodoUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：耗材盘点服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableCheckServiceImpl extends ServiceImpl<ConsumableCheckMapper, ConsumableCheck> implements ConsumableCheckService {
    private final StorehouseMapper storehouseMapper;
    private final ConsumableCategoryMapper consumableCategoryMapper;
    private final ConsumableCheckDetailMapper consumableCheckDetailMapper;
    private final ConsumableCheckMapper consumableCheckMapper;

    private final ConsumableService consumableService;
    private final ConsumableStoreStockMapper consumableStoreStockMapper;
    private final UserClientService userClientService;
    private final PropertyTaskTodoUtil propertyTaskTodoUtil;
    private final PropertyTaskCenterUtil propertyTaskCenterUtil;
    private final ConsumableAddRecordService consumableAddRecordService;
    private final MessageUtil messageUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ConsumableCheckParam param) {
        // 验证仓库是否有库存
        List<ConsumableStoreStock> number = consumableStoreStockMapper.getConsumable(param.getStoreId());
        Assert.isNotAcceptable(ObjectUtils.isEmpty(number) || number.isEmpty(), PropertyStatus.CONSUMABLE_CHECK_TASK);
        ConsumableCheck consumableCheck = new ConsumableCheck();
        consumableCheck.setName(param.getName());
        consumableCheck.setCheckerId(param.getCheckerId());
        if (ObjectUtils.isNotEmpty(param.getPresentCheckDate())) {
            String[] presentCheckDate = param.getPresentCheckDate().split(",");
            consumableCheck.setStartTime(DateUtils.parseDate(presentCheckDate[0]));
            consumableCheck.setEndTime(DateUtils.parseDate(presentCheckDate[1]));
        }
        if (ObjectUtils.isNotEmpty(param.getPreviousCheckDate())) {
            String[] previousCheckDate = param.getPreviousCheckDate().split(",");
            consumableCheck.setPreviousCheckStartTime(DateUtils.parseDate(previousCheckDate[0]));
            consumableCheck.setPreviousCheckEndTime(DateUtils.parseDate(previousCheckDate[1]));
        }
        if (ObjectUtils.isNotEmpty(param.getPresentCheckDate()) && ObjectUtils.isNotEmpty(param.getPreviousCheckDate())) {
            Assert.isNotAcceptable(DateUtils.isIn(consumableCheck.getStartTime(), consumableCheck.getPreviousCheckStartTime(), consumableCheck.getPreviousCheckEndTime()), "上次盘点时间和本次盘点时间不能重复");
            Assert.isNotAcceptable(DateUtils.isIn(consumableCheck.getEndTime(), consumableCheck.getPreviousCheckStartTime(), consumableCheck.getPreviousCheckEndTime()), "上次盘点时间和本次盘点时间不能重复");
        }
        consumableCheck.setStoreId(param.getStoreId());
        consumableCheck.setStatus(TaskStatusEnum.UNDONE.name());
        consumableCheck.setChecker(userClientService.getUser(param.getCheckerId()));
        this.baseMapper.insert(consumableCheck);
        number.stream().forEach(ex -> {
            ConsumableCheckDetail detail = new ConsumableCheckDetail();
            detail.setCheckId(consumableCheck.getId());
            detail.setConsumableId(ex.getConsumableId());
            detail.setNum(ex.getNum());
            detail.setStatus(CheckTaskStatusEnum.INCOMPLETE.name());
            consumableCheckDetailMapper.insert(detail);
        });
        PropertyCheckTask task = new PropertyCheckTask();
        task.setId(consumableCheck.getId());
        task.setStatus(consumableCheck.getStatus());
        task.setPlanName(consumableCheck.getName());
        task.setCheckUserId(consumableCheck.getCheckerId());
        Map<String, String> map = new HashMap<>();
        map.put("taskName", task.getPlanName());
        messageUtil.messageSend(MessageModuleTypeEnum.INVENTORY_TASK.name(), map, task.getCheckUserId());
        propertyTaskTodoUtil.todoInsert(task, "CONSUMABLE_TASK", "耗材盘点");
        propertyTaskCenterUtil.workInsert(task, "CONSUMABLE_TASK", "耗材盘点");
        return consumableCheck.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ConsumableCheckParam param) {
        ConsumableCheck consumableCheck = this.baseMapper.selectById(id);
        Assert.isNotFound(consumableCheck, "未查询到相关信息");
        ObjectUtils.copying(param, consumableCheck);
        this.baseMapper.updateById(consumableCheck);
    }

    @Override
    public Boolean updateForSubmit(Long id, Boolean updateStock) {
        ConsumableCheck consumableCheck = this.baseMapper.selectById(id);
        Assert.parameterIsNull(consumableCheck, PropertyStatus.CONSUMABLE_NO_DETAILS);
        Long incomplete = consumableCheckDetailMapper.selectCount(new LambdaQueryWrapper<ConsumableCheckDetail>()
                                                                          .eq(ConsumableCheckDetail::getCheckId, consumableCheck.getId())
                                                                          .eq(ConsumableCheckDetail::getStatus, CheckTaskStatusEnum.INCOMPLETE.name()));
        Assert.isNotAcceptable(incomplete > 0, PropertyStatus.CONSUMABLE_CHECK_NO);
        consumableCheck.setStatus(TaskStatusEnum.FINISH.name().equals(consumableCheck.getStatus()) ? TaskStatusEnum.WEB_FINISH.name()
                                                                                                   : TaskStatusEnum.FINISH.name());
        consumableCheck.setRealityEndTime(LocalDateTime.now());
        this.baseMapper.updateById(consumableCheck);
        PropertyCheckTask task = new PropertyCheckTask();
        task.setId(consumableCheck.getId());
        task.setStatus(consumableCheck.getStatus());
        task.setPlanName(consumableCheck.getName());
        task.setCheckUserId(consumableCheck.getCheckerId());
        propertyTaskTodoUtil.todo(task, "CONSUMABLE_TASK");
        propertyTaskCenterUtil.work(task, "CONSUMABLE_TASK");
        return true;
    }

    /**
     * 盘点更新库存
     *
     * @param checkId 盘点任务id
     * @param storeId 仓库id
     */
    private void updateStock(Long checkId, Long storeId) {
        // 耗材盘点详情
        List<ConsumableCheckDetail> checkDetails = consumableCheckDetailMapper.selectList(Wrappers.<ConsumableCheckDetail>lambdaQuery()
                                                                                                  .eq(ConsumableCheckDetail::getCheckId, checkId));
        Assert.parameterIsNull(checkDetails, PropertyStatus.CONSUMABLE_NO_DETAILS);
        List<ConsumableStockDetailParam> addStockList = new ArrayList<>();
        List<ConsumableStockDetailParam> reduceStockList = new ArrayList<>();
        checkDetails.forEach(d -> {
            //判断盘盈还是盘亏
            if (d.getCheckNum() > d.getNum()) {
                //盘盈
                var addNum = d.getCheckNum() - d.getNum();
                ConsumableStoreStock storeStock = consumableStoreStockMapper.selectOne(Wrappers.<ConsumableStoreStock>lambdaQuery()
                                                                                               .eq(ConsumableStoreStock::getStoreId, storeId)
                                                                                               .eq(ConsumableStoreStock::getConsumableId, d.getConsumableId()));
                BigDecimal addAmount = storeStock.getAmount().divide(new BigDecimal(storeStock.getNum())).multiply(new BigDecimal(addNum))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                addStockList.add(ConsumableStockDetailParam.builder()
                                         .consumableId(d.getConsumableId())
                                         .num(addNum)
                                         .amount(addAmount)
                                         .status(CheckTaskStatusEnum.WIN.name())
                                         .stockFlag(StockTypeEnum.ADD.name())
                                         .build());
            } else if (d.getCheckNum().compareTo(d.getNum()) < 0) {
                //盘亏
                reduceStockList.add(ConsumableStockDetailParam.builder()
                                            .consumableId(d.getConsumableId())
                                            .num(d.getNum() - d.getCheckNum())
                                            .status(CheckTaskStatusEnum.LOSS.name())
                                            .stockFlag(StockTypeEnum.REDUCE.name())
                                            .build());
            }
        });
        //更新库存
        if (ObjectUtils.isNotEmpty(addStockList)) {
            List<Long> handler = Arrays.asList(SecurityContextHolder.getPrincipal().getId());
            ConsumableAddStockParam addStockParam = ConsumableAddStockParam.builder()
                    .storeId(storeId)
                    .addDate(DateUtils.now())
                    .handler(handler)
                    .remark("盘点增加库存")
                    .businessType(BusinessTypeEnum.CHECK.name())
                    .details(addStockList)
                    .build();
            consumableService.addStock(addStockParam);
        }
        if (ObjectUtils.isNotEmpty(reduceStockList)) {
            ConsumableReduceStockParam reduceStockParam = ConsumableReduceStockParam.builder()
                    .storeId(storeId)
                    .reduceDate(DateUtils.now())
                    .handler(SecurityContextHolder.getPrincipal().getId())
                    .remark("盘点减少库存")
                    .businessType(BusinessTypeEnum.CHECK.name())
                    .details(reduceStockList)
                    .build();
            consumableService.reduceStock(reduceStockParam);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public ConsumableCheckVO getById(Long id) {
        ConsumableCheck consumableCheck = this.baseMapper.selectById(id);
        Assert.isNotFound(consumableCheck, "未找到盘点信息");
        ConsumableCheckVO vo = ObjectUtils.copying(consumableCheck, ConsumableCheckVO.class);
        Storehouse storehouse = storehouseMapper.selectById(consumableCheck.getStoreId());
        vo.setStoreName(storehouse != null ? storehouse.getName() : "");
        return vo;
    }

    @Override
    public Pagination<ConsumableCheckVO> getPage(Long checkerId, ConsumableCheckQueryParam param) {
        ArrayList status = new ArrayList<>();
        if (TaskStatusEnum.UNDONE.name().equals(param.getStatus())) {
            status.add(TaskStatusEnum.UNDONE.name());
        } else {
            status.add(TaskStatusEnum.WEB_FINISH.name());
            status.add(TaskStatusEnum.FINISH.name());
        }
        //请自行添加查询条件
        Page<ConsumableCheck> page = this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(checkerId), ConsumableCheck::getCheckerId, checkerId)
                .eq(ObjectUtils.isNotEmpty(param.getId()), ConsumableCheck::getId, param.getId())
                .in(StringUtils.isNotEmpty(param.getStatus()), ConsumableCheck::getStatus, status)
                .like(StringUtils.isNotEmpty(param.getKeyword()), ConsumableCheck::getName, param.getKeyword())
                .like(StringUtils.isNotEmpty(param.getName()), ConsumableCheck::getName, param.getName())
                .orderByDesc(ConsumableCheck::getCreateTime)
                .page(PagingUtils.convert(param));
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<ConsumableCheckVO> collect = page.getRecords().stream().map(d -> {
                ConsumableCheckVO vo = ObjectUtils.copying(d, ConsumableCheckVO.class);
                Storehouse storehouse = storehouseMapper.selectById(d.getStoreId());
                vo.setStoreName(storehouse != null ? storehouse.getName() : "");
                //查询创建人
                vo.setCreateBy(userClientService.getCreateByName(d));
                return vo;
            }).collect(Collectors.toList());
            return PagingUtils.convert(page, collect);
        }
        return Pagination.empty();
    }

    @Override
    public Pagination<ConsumableCheckWithStatVO> getPageForApp(ConsumableCheckQueryParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Pagination<ConsumableCheckVO> page = this.getPage(userId, param);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<ConsumableCheckWithStatVO> collect = page.getRecords().stream().map(d -> {
                ConsumableCheckWithStatVO vo = ObjectUtils.copying(d, ConsumableCheckWithStatVO.class);
                //获取盘点统计信息
                ConsumableCheckDetailStatVO checkDetailStat = new ConsumableCheckDetailStatVO();
                List<KeyValueVO> value = this.baseMapper.getCheckDetailStat(d.getId());
                for (KeyValueVO ex : value) {
                    if (CheckTaskStatusEnum.INCOMPLETE.name().equals(ex.getName())) {
                        checkDetailStat.setCheckWaitNumber(ex.getValue());
                    }
                    if (CheckTaskStatusEnum.COMPLETED.name().equals(ex.getName())) {
                        checkDetailStat.setCheckedNum(ex.getValue());
                    }
                    if (CheckTaskStatusEnum.WIN.name().equals(ex.getName())) {
                        checkDetailStat.setAddNum(ex.getValue());
                    }
                    if (CheckTaskStatusEnum.LOSS.name().equals(ex.getName())) {
                        checkDetailStat.setReduceNum(ex.getValue());
                    }
                }
                vo.setStat(checkDetailStat);
                return vo;
            }).collect(Collectors.toList());
            return PagingUtils.convert(page, collect);
        }
        return Pagination.empty();
    }

    @Override
    public ConsumableCheckDetailStatVO getCheckStatById(Long checkId) {
        ConsumableCheck check = consumableCheckMapper.selectById(checkId);
        ConsumableCheckDetailStatVO checkDetailStat = new ConsumableCheckDetailStatVO();
        List<KeyValueVO> value = this.baseMapper.getCheckDetailStat(checkId);
        for (KeyValueVO ex : value) {
            if (CheckTaskStatusEnum.COMPLETED.name().equals(ex.getName())) {
                checkDetailStat.setCheckedNum(ex.getValue());
            } else if (CheckTaskStatusEnum.WIN.name().equals(ex.getName())) {
                checkDetailStat.setAddNum(ex.getValue());
            } else if (CheckTaskStatusEnum.LOSS.name().equals(ex.getName())) {
                checkDetailStat.setReduceNum(ex.getValue());
            }
        }
        Long total = consumableCheckDetailMapper.selectCount(new LambdaQueryWrapper<ConsumableCheckDetail>()
                                                                     .eq(ConsumableCheckDetail::getCheckId, checkId));
        checkDetailStat.setConsumableNum(total);
        checkDetailStat.setCheckWaitNumber(
                total - checkDetailStat.getCheckedNum() - checkDetailStat.getAddNum() - checkDetailStat.getReduceNum());
        return checkDetailStat;
    }

    @Override
    public Pagination<ConsumableStoreStockExtendVO> getCheckDetailPage(ConsumableCheckDetailParam param) {
        Page<ConsumableStoreStockExtendVO> page = this.baseMapper.getCheckDetail(PagingUtils.convert(param), param);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().stream().forEach(d -> {
                ConsumableCategory category = consumableCategoryMapper.selectById(d.getCategoryId());
                d.setCategoryName(category != null ? category.getName() : "");
                ConsumableCheckDetail checkDetail = consumableCheckDetailMapper.selectOne(new LambdaQueryWrapper<ConsumableCheckDetail>()
                                                                                                  .eq(ConsumableCheckDetail::getCheckId, param.getCheckId())
                                                                                                  .eq(ConsumableCheckDetail::getConsumableId, d.getId())
                                                                                                  .last("limit 1"));
                d.setCheckNum(ObjectUtils.isNotEmpty(checkDetail) ? checkDetail.getCheckNum() : 0);
                d.setStatus(ObjectUtils.isNotEmpty(checkDetail) ? checkDetail.getStatus() : CheckTaskStatusEnum.INCOMPLETE.name());
            });
        }
        return PagingUtils.convert(page);
    }

    @Override
    public Boolean submitWin(Long taskId) {
        List<ConsumableCheckDetail> details = consumableCheckDetailMapper.selectList(new LambdaQueryWrapper<ConsumableCheckDetail>()
                                                                                             .eq(ConsumableCheckDetail::getCheckId, taskId));
        for (ConsumableCheckDetail ex : details) {
            if (!ex.getNum().equals(ex.getCheckNum())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean addWin(Long taskId) {
        ConsumableCheck check = this.baseMapper.selectById(taskId);
        List<ConsumableCheckDetail> details = consumableCheckDetailMapper.selectList(new LambdaQueryWrapper<ConsumableCheckDetail>()
                                                                                             .eq(ConsumableCheckDetail::getCheckId, taskId));
        List<ConsumableStockDetail> stockDetail = new ArrayList<>();
        for (ConsumableCheckDetail ex : details) {
            if (!ex.getNum().equals(ex.getCheckNum())) {
                ConsumableStoreStock storeStock = consumableStoreStockMapper.selectOne(new LambdaQueryWrapper<ConsumableStoreStock>()
                                                                                               .eq(ConsumableStoreStock::getStoreId, check.getStoreId())
                                                                                               .eq(ConsumableStoreStock::getConsumableId, ex.getConsumableId()));
                storeStock.setNum(ex.getCheckNum());
                storeStock.setAmount(BigDecimal.valueOf(storeStock.getNum()).multiply(storeStock.getAvgPrice()));
                ConsumableStockDetail consumableStockDetail = new ConsumableStockDetail();
                consumableStockDetail.setConsumableId(ex.getConsumableId());
                stockDetail.add(consumableStockDetail);
                consumableStoreStockMapper.updateById(storeStock);
            }
        }
        if (ObjectUtils.isNotEmpty(stockDetail)) {
            try {
                consumableAddRecordService.sendMsg(stockDetail);
            } catch (Exception e) {
                log.error("同步清单发送短信失败");
            }
        }
        return true;
    }
}