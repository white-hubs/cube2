package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.dao.StorehouseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.common.entity.Storehouse;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtile;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.convert.ConsumableAddRecordConverter;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.convert.ConsumableStockDetailConverter;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableAddRecordMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCategoryMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCheckMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStockDetailMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.Consumable;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheck;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.StockTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddRecordQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableAddRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStockDetailService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStoreStockService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.template.ConsumableAddRecordTemplate;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.template.DropdownWriteHandler;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.template.HideColumnWriteHandler;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddExcelVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableAddRecordVO;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.MessageModuleTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.FilesUtil;
import com.guhecloud.cube.srv.gen.property.management.server.property.util.MessageUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import instance.event.ProcessCompletedMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述：耗材入库服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableAddRecordServiceImpl extends ServiceImpl<ConsumableAddRecordMapper, ConsumableAddRecord> implements ConsumableAddRecordService {
    private final ConsumableStockDetailService consumableStockDetailService;
    private final ConsumableStockDetailMapper consumableStockDetailMapper;
    private final StorehouseMapper storehouseMapper;
    private final FileRefClient fileRefClient;
    private final UserClientService userClientService;
    private final ConsumableMapper consumableMapper;
    @Autowired
    @Lazy
    private ConsumableService consumableService;
    private final ConsumableCategoryMapper consumableCategoryMapper;
    private final ConsumableStoreStockService consumableStoreStockService;
    private final ConsumableCheckMapper consumableCheckMapper;
    private final MessageUtil messageUtil;
    private final ExcelUtils excelUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ConsumableAddRecordParam param) {
        ConsumableAddRecord consumableAddRecord = ObjectUtils.copying(param, ConsumableAddRecord.class);
        this.baseMapper.insert(consumableAddRecord);
        return consumableAddRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ConsumableAddRecordParam param) {
        ConsumableAddRecord consumableAddRecord = this.baseMapper.selectById(id);
        Assert.parameterIsNull(consumableAddRecord, PropertyStatus.CONSUMABLE_NO_DETAILS);
        ObjectUtils.copying(param, consumableAddRecord);
        this.baseMapper.updateById(consumableAddRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        Boolean bool = DatabaseContextHolder.softDelete(this, id);
        return bool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public ConsumableAddRecordVO getById(Long id) {
        ConsumableAddRecord consumableAddRecord = this.baseMapper.selectById(id);
        return ObjectUtils.copying(consumableAddRecord, ConsumableAddRecordVO.class);
    }

    @Override
    public Pagination<ConsumableAddPageVO> getPage(ConsumableAddRecordQueryParam param) {
        Page<ConsumableAddPageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.getPage(page, param);
        Pagination<ConsumableAddPageVO> pagination = new Pagination<>(page.getTotal(), page.getPages(), page.getRecords());
        for (ConsumableAddPageVO ex : pagination.getRecords()) {
            Storehouse storehouse = storehouseMapper.selectById(ex.getStoreId());
            ex.setStoreName(storehouse.getName());
            ex.setHandlerName(userClientService.getUser(ex.getHandler()));
        }
        return pagination;
    }

    @Override
    public Long addWithDetail(ConsumableAddStockParam param) {
        ConsumableAddRecord record = param.getConsumableAddRecord();
        //保存入库记录
        this.save(record);
        if (ObjectUtils.isNotEmpty(param.getFiles())) {
            // 处理附件
            List<FileRefCreateParam> files = FilesUtil.buildFiles(record.getId(), param.getFiles());
            fileRefClient.saveBatch(files);
        }
        //保存明细
        List<ConsumableStockDetailParam> details = param.getDetails();
        if (!CollectionUtils.isEmpty(details)) {
            details.stream().forEach(d -> {
                d.setStatus(FlowResult.UNDER.name());
                d.setStockId(record.getId());
                d.setStockFlag(StockTypeEnum.ADD.name());
                //查询初始的数量和金额
                consumableStockDetailService.extendInitStockInfo(param.getStoreId(), d);
                consumableStockDetailService.add(d);
            });
        }
        return record.getId();
    }

    @Override
    public void getExcel(HttpServletResponse response, DepositoryDetailedPageExcelParam param) {
        Map<String, Map<String, String>> title = new HashMap<>();
        Map<String, List<?>> data = new HashMap<>();
        String[] array = param.getIds().split(",");
        for (String id : array) {
            ConsumableAddRecord addRecord = baseMapper.selectById(Long.parseLong(id));
            Map<String, String> map = new HashMap<>();
            BigDecimal totalMoney = BigDecimal.ZERO;
            List<ConsumableAddExcelVO> addExcel = baseMapper.getExcel(id);
            for (ConsumableAddExcelVO ex : addExcel) {
                totalMoney = totalMoney.add(ex.getAmount());
            }
            map.put("totalMoney", totalMoney.toString());
            String date = DateUtils.formatDate(addRecord.getAddDate());
            map.put("addDate", date);
            title.put(id, map);
            data.put(id, addExcel);
        }
        try {
            log.info("----------------------开始导出物资入库单----------------------");
            ExcelUtile.uploadExcelList(response, data, title, "物资入库单.xlsx");
        } catch (Exception exception) {
            log.error("物资入库单导出--{}", exception.getLocalizedMessage());
        }
    }

    @Override
    public Boolean update(WorkflowRelevance param) {
        ConsumableAddRecord addRecord = baseMapper.selectById(param.getPropertyId());
        addRecord.setProcessInstanceId(param.getProcessInstanceId());
        Boolean bool = baseMapper.updateById(addRecord) > 0;
        return bool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean submit(ProcessCompletedMessage message) {
        log.info("耗材入库，审核结果：{}", message.getResult().name());
        ConsumableAddRecord addRecord = baseMapper.selectOne(new LambdaQueryWrapper<ConsumableAddRecord>()
                                                                     .eq(ConsumableAddRecord::getProcessInstanceId, message.getProcessInstanceId()));
        if (ObjectUtils.isEmpty(addRecord)) {
            log.info("耗材入库消费失败：未找到出库记录：{}", message.getProcessInstanceId());
            return false;
        } else if (!FlowResult.UNDER.name().equals(addRecord.getStatus())) {
            log.info("耗材入库消费失败：重复消费：{}；审核结果", message.getProcessInstanceId(), addRecord.getStatus());
            return false;
        }
        Boolean bool = consumableCheckMapper.selectCount(new LambdaQueryWrapper<ConsumableCheck>()
                                                                 .eq(ConsumableCheck::getStoreId, addRecord.getStoreId())
                                                                 .eq(ConsumableCheck::getStatus, TaskStatusEnum.UNDONE.name())) > 0;
        if (bool) {
            throw new ServiceException("该仓库盘点中，无法出入库");
        }
        addRecord.setStatus(message.getResult().name());
        bool = baseMapper.updateById(addRecord) > 0;
        log.info("业务更新结果：{},{}", bool, addRecord.getStatus());
        if (bool && FlowResult.PASS.name().equals(message.getResult().name())) {
            log.info("{}:审核通过，开始入库", addRecord.getId());
            //增加仓库库存
            consumableStoreStockService.insertOrUpdateStock(addRecord);
            //更新耗材的总库存、总金额
            List<ConsumableStockDetail> stockDetail = consumableStockDetailMapper.selectList(new LambdaQueryWrapper<ConsumableStockDetail>()
                                                                                                     .eq(ConsumableStockDetail::getStockId, addRecord.getId()));
            for (ConsumableStockDetail ex : stockDetail) {
                Consumable consumable = consumableMapper.selectById(ex.getConsumableId());
                consumable.setId(ex.getConsumableId());
                consumable.setStock(consumable.getStock() + ex.getNum());
                consumable.setAmount(consumable.getAmount().add(ex.getAmount()));
                consumableMapper.updateById(consumable);
            }
            try {
                this.sendMsg(stockDetail);
            } catch (Exception e) {
                log.error("耗材审批通过发送短信失败：{}", e.getMessage());
            }
        }
        return true;
    }

    @Override
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        var data = new ArrayList<ConsumableAddRecordTemplate>();
        var list = consumableStoreStockService.list();
        if (ListUtils.isNotEmpty(list)) {
            //获取耗材id
            var consumableIds = ListUtils.convertItem(list, ConsumableStoreStock::getConsumableId);
            //查询耗材信息
            var consumables = consumableMapper.selectByIds(consumableIds);
            var consumableMap = MapUtils.listToMap(consumables, Consumable::getId, consumable -> consumable);
            var storeIds = ListUtils.convertItem(list, ConsumableStoreStock::getStoreId);
            var stores = storehouseMapper.selectByIds(storeIds);
            var storeMap = MapUtils.listToMap(stores, Storehouse::getId, storehouse -> storehouse);
            var categoryIds = ListUtils.convertItem(consumables, Consumable::getCategoryId);
            var categories = consumableCategoryMapper.selectByIds(categoryIds);
            var categoryMap = MapUtils.listToMap(categories, ConsumableCategory::getId, category -> category);
            var datas = list.stream().map(consumableStoreStock -> {
                        var consumable = consumableMap.get(consumableStoreStock.getConsumableId());
                        if (ObjectUtils.isNull(consumable)) {
                            return null;
                        }
                        var store = storeMap.get(consumableStoreStock.getStoreId());
                        if (store == null) {
                            return null;
                        }
                        var consumableAddRecordTemplate = new ConsumableAddRecordTemplate();
                        consumableAddRecordTemplate.setName(consumable.getName());
                        consumableAddRecordTemplate.setCode(consumable.getCode());
                        consumableAddRecordTemplate.setModel(consumable.getModel());
                        consumableAddRecordTemplate.setBrand(consumable.getBrand());
                        consumableAddRecordTemplate.setQualityPeriod(consumable.getQualityPeriod());
                        consumableAddRecordTemplate.setStockMin(consumable.getStockMin());
                        consumableAddRecordTemplate.setStockMax(consumable.getStockMax());
                        consumableAddRecordTemplate.setUseLimit(consumable.getUseLimit());
                        var category = categoryMap.get(consumable.getCategoryId());
                        var categoryName = category == null ? "" : category.getName();
                        consumableAddRecordTemplate.setCategoryName(categoryName);
                        consumableAddRecordTemplate.setUnit(consumable.getUnit());
                        consumableAddRecordTemplate.setBeforeStock(
                                consumableStoreStock.getNum() - Objects.requireNonNullElse(consumableStoreStock.getOccupyNum(), 0));
                        consumableAddRecordTemplate.setCurrentStock(null);
                        consumableAddRecordTemplate.setAvgPrice(null);
                        consumableAddRecordTemplate.setStore(store.getName());
                        consumableAddRecordTemplate.setStoreId(String.valueOf(store.getId()));
                        consumableAddRecordTemplate.setConsumableId(String.valueOf(consumable.getId()));
                        return consumableAddRecordTemplate;
                    }).filter(ObjectUtils::isNotNull)
                    .sorted((o1, o2) -> (int) (Long.parseLong(o1.getStoreId()) - Long.parseLong(o2.getStoreId())))
                    .toList();
            data.addAll(datas);
        }
        //查询所有仓库
        var allStores = storehouseMapper.selectList(null);
        //查询所有耗材分类
        var allCategories = consumableCategoryMapper.selectList(null);
        var allStoreNames = allStores.stream().map(Storehouse::getName).toList();
        var allCategoryNames = allCategories.stream().map(ConsumableCategory::getName).toList();
        excelUtils.downloadExcel("耗材入库单"
                , data
                , ConsumableAddRecordTemplate.class
                , new DropdownWriteHandler(allStoreNames, 13, allCategoryNames, 4)
                , new HideColumnWriteHandler(14, 15));
    }

    @Override
    @Transactional
    public void importExcel(MultipartFile file) {
        try {
            var consumableAddRecordTemplates = excelUtils.readExcelFile(file, ConsumableAddRecordTemplate.class);
            Assert.isNotFound(consumableAddRecordTemplates, "未读取到导入的数据!");
            //处理新增的耗材档案
            handleAddConsumable(consumableAddRecordTemplates);
            consumableAddRecordTemplates = filterAddRecord(consumableAddRecordTemplates);
            var consumableIds = ListUtils.convertItem(consumableAddRecordTemplates, ConsumableAddRecordTemplate::getConsumableId);
            Assert.isNotFound(CollectionUtils.isEmpty(consumableIds), "未读取到导入的耗材档案!");
            var consumables = consumableMapper.selectList(new LambdaQueryWrapper<Consumable>().in(Consumable::getId, consumableIds));
            var consumableMap = MapUtils.listToMap(consumables, Consumable::getId, consumable -> consumable);
            var storeIdSet = consumableAddRecordTemplates.stream().map(ConsumableAddRecordTemplate::getStoreId).collect(Collectors.toSet());
            Assert.isNotFound(CollectionUtils.isEmpty(storeIdSet), "未读取到导入的仓库!");
            var stores = storehouseMapper.selectByIds(storeIdSet);
            var storeMap = MapUtils.listToMap(stores, Storehouse::getId, storehouse -> storehouse);
            checkStore(storeIdSet);
            var consumableStoreStocks = consumableStoreStockService.list();
            var consumableStoreStockMap = MapUtils.listToMap(consumableStoreStocks, consumableStoreStock ->
                    consumableStoreStock.getStoreId() + ":" +
                    consumableStoreStock.getConsumableId(), consumableStoreStock -> consumableStoreStock);
            var insertAddRecords = new ArrayList<ConsumableAddRecord>();
            var insertDetailRecords = new ArrayList<ConsumableStockDetail>();
            var updateStoreStockRecords = new ArrayList<ConsumableStoreStock>();
            var updateConsumables = new ArrayList<Consumable>();

            consumableAddRecordTemplates.forEach(consumableAddRecordTemplate -> {

                var store = storeMap.get(Long.parseLong(consumableAddRecordTemplate.getStoreId()));
                Assert.isNotFound(store, store.getName() + "，未找到这个仓库");
                var consumableAddRecord = ConsumableAddRecordConverter.convertToConsumableAddRecord(store);
                var consumable = consumableMap.get(Long.parseLong(consumableAddRecordTemplate.getConsumableId()));
                Assert.isNotFound(consumable, consumableAddRecordTemplate.getName() + ":未找到这个耗材");
                var consumableStoreStock = consumableStoreStockMap.get(store.getId() + ":" + consumable.getId());
                if (consumableStoreStock.getNum()
                    - Objects.requireNonNullElse(consumableStoreStock.getOccupyNum(), 0)
                    - consumableAddRecordTemplate.getCurrentStock()
                    == 0) {
                    log.info("新增库存数量为无变化，不做处理");
                    return;
                }
                //添加入库详情
                var consumableStockDetail = ConsumableStockDetailConverter.convertToConsumableStockDetail(consumableAddRecordTemplate, consumableAddRecord, consumable, consumableStoreStock);
                insertAddRecords.add(consumableAddRecord);
                insertDetailRecords.add(consumableStockDetail);
                Assert.isNotFound(consumableStoreStock, "未找到这个耗材的库存信息");
                //更新耗材存储信息
                consumableStoreStock.setAmount(consumableStoreStock.getAmount().add(consumableStockDetail.getAmount()));
                consumableStoreStock.setNum(consumableAddRecordTemplate.getCurrentStock() +
                                            Objects.requireNonNullElse(consumableStoreStock.getOccupyNum(), 0));
                consumableStoreStock.setAvgPrice(consumableStoreStock.getAmount()
                                                         .divide(new BigDecimal(consumableStoreStock.getNum()), 2, RoundingMode.HALF_UP));
                updateStoreStockRecords.add(consumableStoreStock);
                //更新耗材信息
                consumable.setStock(consumable.getStock() + consumableStoreStock.getNum());
                consumable.setAmount(consumable.getAmount().add(consumableStoreStock.getAmount()));
                updateConsumables.add(consumable);
            });
            saveBatch(insertAddRecords);
            consumableStockDetailService.saveBatch(insertDetailRecords);
            consumableStoreStockService.updateBatchById(updateStoreStockRecords);
            consumableService.updateBatchById(updateConsumables);
        } catch (IOException e) {
            log.error("读取Excel失败：{}", e.getMessage());
            throw new ServiceException("读取Excel失败");
        }
    }

    private void handleAddConsumable(List<ConsumableAddRecordTemplate> consumableAddRecordTemplates) {
        var insertStoreStockRecords = new ArrayList<ConsumableStoreStock>();

        //获取新增的耗材档案
        var addConsumables = consumableAddRecordTemplates.stream().filter(template -> Objects.isNull(template.getConsumableId()))
                .toList();
        if (CollectionUtils.isNotEmpty(addConsumables)) {

            //查询耗材分类信息
            var allCategories = consumableCategoryMapper.selectList(null);
            var categoryMap = MapUtils.listToMap(allCategories, ConsumableCategory::getName, category -> category);
            //查询所有的耗材信息
            var allConsumables = consumableMapper.selectList(new LambdaQueryWrapper<Consumable>().eq(Consumable::getStatus, "NO_DELETE"));
            var consumableCode = allConsumables.stream().map(Consumable::getCode).collect(Collectors.toSet());
            //新增耗材档案
            var consumableList = addConsumables.stream().map(template -> {
                var notFull = template.getCurrentStock() == null || template.getAvgPrice() == null || template.getCode() == null ||
                              template.getCategoryName() == null;
                //检查信息是否完整
                Assert.isNotFound(
                        notFull, template.getName() + ":无法新增该耗材，必填信息不完整");
                var category = categoryMap.get(template.getCategoryName());
                Assert.isNotFound(category, "未找到耗材分类:" + template.getCategoryName());
                var consumable = new Consumable();
                var code = template.getCode();
                Assert.isConflict(consumableCode.contains(code), "耗材编码重复:" + code);
                consumable.setName(template.getName());
                consumable.setCode(template.getCode());
                consumable.setModel(template.getModel());
                consumable.setBrand(template.getBrand());
                var qualityPeriod = template.getQualityPeriod();
                if (StringUtils.isNotBlank(qualityPeriod)) {
                    try {
                        Integer.parseInt(qualityPeriod);
                    } catch (Exception e) {
                        throw new ServiceException(consumable.getName() + "新增失败，请输入正确的保质期,仅使用数字");
                    }
                }
                consumable.setQualityPeriod(template.getQualityPeriod());
                consumable.setUnit(template.getUnit());
                consumable.setStock(0);
                consumable.setAmount(BigDecimal.ZERO);
                consumable.setCategoryId(category.getId());
                consumable.setStatus("NO_DELETE");
                consumable.setStockMin(template.getStockMin());
                consumable.setStockMax(template.getStockMax());
                consumable.setUseLimit(template.getUseLimit());
                return consumable;
            }).toList();
            consumableService.saveBatch(consumableList);
            //查询所有的仓库信息
            var allStores = storehouseMapper.selectList(null);
            var storeMap = MapUtils.listToMap(allStores, Storehouse::getName, store -> store);
            //设置模板的耗材id和仓库id和以前的库存数量，收集耗材档案的仓库存储信息
            for (int i = 0; i < addConsumables.size() && i < consumableList.size(); i++) {
                var template = addConsumables.get(i);
                template.setConsumableId(consumableList.get(i).getId().toString());
                var store = storeMap.get(template.getStore());
                Assert.isNotFound(store, "未找到仓库:" + template.getStore());
                template.setStoreId(store.getId().toString());
                template.setBeforeStock(0);
                var consumableStoreStock = new ConsumableStoreStock();
                consumableStoreStock.setConsumableId(Long.parseLong(template.getConsumableId()));
                consumableStoreStock.setConsumableName(template.getName());
                consumableStoreStock.setStoreId(Long.parseLong(template.getStoreId()));
                consumableStoreStock.setAmount(BigDecimal.ZERO);
                consumableStoreStock.setNum(0);
                consumableStoreStock.setAvgPrice(BigDecimal.ZERO);
                consumableStoreStock.setOccupyNum(0);
                insertStoreStockRecords.add(consumableStoreStock);
            }
            //保存耗材的仓库存储信息
            consumableStoreStockService.saveBatch(insertStoreStockRecords);
        }

    }

    private static List<ConsumableAddRecordTemplate> filterAddRecord(
            List<ConsumableAddRecordTemplate> consumableAddRecordTemplates
                                                                    ) {
        consumableAddRecordTemplates = consumableAddRecordTemplates
                .stream()
                .filter(consumableAddRecordTemplate ->
                                ObjectUtils.isNotNull(consumableAddRecordTemplate.getAvgPrice()) &&
                                ObjectUtils.isNotNull(consumableAddRecordTemplate.getCurrentStock())
                       )
                .collect(Collectors.toList());
        return consumableAddRecordTemplates;
    }

    private void checkStore(Set<String> storeIdSet) {
        if (CollectionUtils.isEmpty(storeIdSet)) {
            return;
        }
        //检查仓库是否盘点中
        boolean bool =
                consumableCheckMapper.selectCount(new LambdaQueryWrapper<ConsumableCheck>().in(ConsumableCheck::getStoreId, storeIdSet)
                                                          .eq(ConsumableCheck::getStatus, TaskStatusEnum.UNDONE.name())) > 0;
        Assert.isNotAcceptable(bool, PropertyStatus.CONSUMABLE_CHECK_RESTRICT);
    }

    /**
     * 发送短信
     */
    public Boolean sendMsg(List<ConsumableStockDetail> stockDetail) {
        List<MessageSendConfig> conditions = messageUtil.getSendMsgConfig(Arrays.asList(MessageModuleTypeEnum.CONSUMABLE_LOWER.name(), MessageModuleTypeEnum.CONSUMABLE_UPPER.name()));
        MessageSendConfig maxConditions = new MessageSendConfig();
        MessageSendConfig minConditions = new MessageSendConfig();
        for (MessageSendConfig ex : conditions) {
            if (ex.getSceneCode().equals(MessageModuleTypeEnum.CONSUMABLE_UPPER.name())) {
                maxConditions = ex;
            } else {
                minConditions = ex;
            }
        }
        if (ObjectUtils.isNotEmpty(maxConditions) || ObjectUtils.isNotEmpty(minConditions)) {
            StringBuffer maxConsumable = new StringBuffer();
            StringBuffer minConsumable = new StringBuffer();
            Integer consumableMaxTotal = 0;
            Integer consumableMinTotal = 0;
            for (ConsumableStockDetail ex : stockDetail) {
                Consumable consumable = consumableMapper.selectById(ex.getConsumableId());
                if (ObjectUtils.isNotEmpty(consumable.getStockMax()) && consumable.getStock() > consumable.getStockMax()) {
                    if (consumableMaxTotal < 2) {
                        if (ObjectUtils.isNotEmpty(maxConsumable)) {
                            maxConsumable.append("、");
                        }
                        maxConsumable.append(consumable.getName());
                    }
                    consumableMaxTotal += 1;
                    log.info(consumable.getName() + "超过上限");
                }
                if (ObjectUtils.isNotEmpty(consumable.getStockMin()) && consumable.getStock() < consumable.getStockMin()) {
                    log.info(consumable.getName() + "低于下限");
                    if (consumableMinTotal < 2) {
                        if (ObjectUtils.isNotEmpty(minConsumable)) {
                            maxConsumable.append("、");
                        }
                        minConsumable.append(consumable.getName());
                    }
                    consumableMinTotal += 1;
                }
            }

            if (ObjectUtils.isNotEmpty(consumableMaxTotal) && consumableMaxTotal > 0) {
                Map<String, String> map = new HashMap<>();
                map.put("consumableNmae", maxConsumable.toString());
                map.put("consumableMaxTotal", consumableMaxTotal.toString());
                messageUtil.messageSend(maxConditions, map);
            }
            if (ObjectUtils.isNotEmpty(consumableMinTotal) && consumableMinTotal > 0) {
                Map<String, String> map = new HashMap<>();
                map.put("consumableNmae", minConsumable.toString());
                map.put("consumableMinTotal", consumableMinTotal.toString());
                messageUtil.messageSend(maxConditions, map);
            }
        }
        return true;
    }
}