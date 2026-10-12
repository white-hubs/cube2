package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.FileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCategoryMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCheckMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStoreStockMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.Consumable;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheck;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.ConsumableStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.TaskStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableAddRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableCategoryService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableReduceRecordService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.template.MaterialTemplate;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCategoryVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableVO;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 描述：耗材管理服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableServiceImpl extends ServiceImpl<ConsumableMapper, Consumable> implements ConsumableService {
    private final ConsumableCategoryService consumableCategoryService;
    private final ConsumableAddRecordService consumableAddRecordService;
    private final ConsumableReduceRecordService consumableReduceRecordService;
    private final FileClient fileClient;
    private final ConsumableCheckMapper consumableCheckMapper;
    private final ConsumableStoreStockMapper consumableStoreStockMapper;
    private final ConsumableCategoryMapper consumableCategoryMapper;
    private final FileOperateClient fileOperateClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ConsumableParam param) {
        Consumable exist = this.lambdaQuery()
                .eq(Consumable::getCode, param.getCode())
                .last("LIMIT 1")
                .one();
        Assert.isConflict(Objects.nonNull(exist), PropertyStatus.CONSUMABLE_CODE_CHECK);
        Consumable consumable = ObjectUtils.copying(param, Consumable.class);
        consumable.setStock(0);
        consumable.setAmount(BigDecimal.ZERO);
        consumable.setStatus(ConsumableStatusEnum.NO_DELETE.name());
        consumable.setFileIds(ObjectUtils.isNotEmpty(param.getFile()) ? StringUtils.join(",", param.getFile()) : null);
        this.baseMapper.insert(consumable);
        return consumable.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ConsumableParam param) {
        Consumable consumable = this.baseMapper.selectById(id);
        Assert.parameterIsNull(consumable, PropertyStatus.CONSUMABLE_NO_DETAILS);
        //修改时如果编码不同判断编码是否重复
        if (!StringUtils.equals(param.getCode(), consumable.getCode())) {
            Consumable exist = this.lambdaQuery()
                    .eq(Consumable::getCode, param.getCode())
                    .last("LIMIT 1")
                    .one();
            Assert.isConflict(Objects.nonNull(exist), PropertyStatus.CONSUMABLE_CODE_CHECK);
        }
        ObjectUtils.copying(param, consumable);
        consumable.setFileIds(ObjectUtils.isNotEmpty(param.getFile()) ? StringUtils.join(",", param.getFile()) : "");
        consumable.setStatus(ConsumableStatusEnum.NO_DELETE.name());
        this.baseMapper.updateById(consumable);
        if (ObjectUtils.isNotEmpty(param.getFile())) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setSceneCode(FileSceneCodeEnum.PROPERTY_MANAGEMENT_CONSUMABLE.getCode());
            fileInduceParam.setFileIdList(param.getFile());
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.lambdaUpdate().eq(Consumable::getId, id)
                .set(Consumable::getStatus, ConsumableStatusEnum.DELETE.name())
                .update();
    }

    @Override
    public ConsumableVO getById(Long id) {
        Consumable consumable = this.baseMapper.selectById(id);
        ConsumableVO vo = ObjectUtils.copying(consumable, ConsumableVO.class);
        return this.extend(vo);
    }

    @Override
    public Pagination<ConsumableVO> getPage(ConsumableQueryParam param) {
        Page<Consumable> page = new Page<>(param.getPageNumber(), param.getPageSize());
        //请自行添加查询条件
        page = this.baseMapper.getPage(page, param);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            List<ConsumableVO> collect = page.getRecords().stream().map(d -> {
                ConsumableVO vo = ObjectUtils.copying(d, ConsumableVO.class);
                if (ObjectUtils.isNotEmpty(d.getFileIds())) {
                    List<Long> fileIdds = Arrays.stream(d.getFileIds().split(","))
                            .map(s -> Long.parseLong(s.trim()))
                            .collect(Collectors.toList());
                    List<FileVO> file = fileClient.getByIds(fileIdds);
                    if (ObjectUtils.isNotEmpty(file)) {
                        vo.setFile(file);
                    }
                }
                return this.extend(vo);
            }).collect(Collectors.toList());
            return PagingUtils.convert(page, collect);
        }
        return Pagination.empty();
    }

    /**
     * 扩展耗材信息
     *
     * @param consumableVO
     *
     * @return
     */
    private ConsumableVO extend(ConsumableVO consumableVO) {
        ConsumableCategoryVO category = consumableCategoryService.getById(consumableVO.getCategoryId());
        consumableVO.setCategoryName(Optional.ofNullable(category).map(ex -> ex.getName()).orElse(""));
        consumableVO.setAvgPrice(BigDecimal.valueOf(0.00));
        // 数量为空，则均价为0
        if (ObjectUtils.isEmpty(consumableVO.getStock()) || consumableVO.getStock().equals(0)) {
            consumableVO.setStock(0);
            consumableVO.setAmount(ObjectUtils.isEmpty(consumableVO.getAmount()) ? BigDecimal.valueOf(0.00) : consumableVO.getAmount());
            return consumableVO;
        }
        if (ObjectUtils.isEmpty(consumableVO.getAmount())) {
            consumableVO.setAmount(BigDecimal.ZERO);
        } else {
            //均价
            BigDecimal avgPrice = consumableVO.getAmount().divide(new BigDecimal(consumableVO.getStock()), 2, BigDecimal.ROUND_HALF_UP);
            consumableVO.setAvgPrice(avgPrice);
        }
        Integer num = consumableStoreStockMapper.getConsumableNumber(consumableVO.getId());
        consumableVO.setStock(ObjectUtils.isNotEmpty(num) ? num : 0);
        return consumableVO;
    }

    /**
     * 入库
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Long addStock(ConsumableAddStockParam param) {
        Boolean bool = consumableCheckMapper.selectCount(new LambdaQueryWrapper<ConsumableCheck>()
                                                                 .eq(ConsumableCheck::getStoreId, param.getStoreId())
                                                                 .eq(ConsumableCheck::getStatus, TaskStatusEnum.UNDONE.name())) > 0;
        Assert.isNotAcceptable(bool, PropertyStatus.CONSUMABLE_CHECK_RESTRICT);
        //添加入库记录并记录明细
        Long id = this.consumableAddRecordService.addWithDetail(param);
        return id;
    }

    /**
     * 出库
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Long reduceStock(ConsumableReduceStockParam param) {
        //添加出库记录并记录明细
        Long id = this.consumableReduceRecordService.reduceWithDetail(param);
        return id;
    }

    @Override
    public void importTemplate(MultipartFile file) {
        Assert.isParameterError(file.isEmpty(), "必须上传excel文件");
        ExcelReader writer = null;
        try {
            writer = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            throw new ServiceException("文件读取数据异常，导入失败");
        }
        writer.addHeaderAlias("*耗材分类", "categoryName");
        writer.addHeaderAlias("*耗材名称", "name");
        writer.addHeaderAlias("*耗材编码", "code");
        writer.addHeaderAlias("品牌", "brand");
        writer.addHeaderAlias("规格型号", "model");
        writer.addHeaderAlias("商品码", "goodsCode");
        writer.addHeaderAlias("*单位", "unit");
        writer.addHeaderAlias("质保期限", "qualityPeriod");
        writer.addHeaderAlias("安全上限", "stockMax");
        writer.addHeaderAlias("安全下限", "stockMin");
        writer.addHeaderAlias("限领数量(人/月)", "useLimit");

        List<ConsumableVO> datas = writer.readAll(ConsumableVO.class);
        Assert.parameterIsNull(datas, "无法添加空的excel");
        //数据校验，名称、分类、编码、单位必填
        datas.forEach(ex -> {
            Assert.isParameterError(StringUtils.isBlank(ex.getCategoryName()), "请填写耗材分类");
            Assert.isParameterError(StringUtils.isBlank(ex.getName()), "请填写耗材名称");
            Assert.isParameterError(StringUtils.isBlank(ex.getCode()), "请填写耗材编码");
            Assert.isParameterError(StringUtils.isBlank(ex.getUnit()), "请填写耗材单位");
        });
        List<ConsumableCategory> consumableCategoryList = consumableCategoryMapper.selectList(new LambdaQueryWrapper<>());
        Map<String, Long> consumableCategoryMap = consumableCategoryList.stream()
                .collect(Collectors.toMap(ConsumableCategory::getName, ConsumableCategory::getId));
        List<Consumable> consumableList = new ArrayList<>();
        for (ConsumableVO ex : datas) {
            ex.setStatus(ConsumableStatusEnum.NO_DELETE.name());
            if (ObjectUtils.isNotEmpty(ex.getCategoryName())) {
                ex.setCategoryId(consumableCategoryMap.get(ex.getCategoryName()));
            }
            consumableList.add(ConvertUtils.convert(Consumable.class, ex));
        }
        Boolean bool = super.saveBatch(consumableList);
        log.info("导入结果：{}", bool);
    }

    @Override
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 设置响应头
            String fileName = URLEncoder.encode("耗材导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ExcelTypeEnum.XLSX.getValue());

            // 创建空的模板数据
            List<MaterialTemplate> dataList = new ArrayList<>();

            // 设置表格样式（居中对齐）
            WriteCellStyle headStyle = new WriteCellStyle();
            headStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            headStyle.setWrapped(false);
            WriteCellStyle contentStyle = new WriteCellStyle();
            contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);
            // 2. 下拉列表处理器（正确写法）
            SheetWriteHandler dropdownHandler = new SheetWriteHandler() {
                @Override
                public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
                                             WriteSheetHolder writeSheetHolder
                                            ) {
                    Sheet sheet = writeSheetHolder.getSheet();
                    // 设置下拉选项（假设"耗材类型"是第3列，索引为2）
                    List<ConsumableCategory> consumableCategoryList = consumableCategoryMapper.selectList(new LambdaQueryWrapper<>());
                    List<String> categoryNames = CollectionUtils.isEmpty(consumableCategoryList) ? new ArrayList<>()
                                                                                                 : consumableCategoryList.stream()
                                                         .map(ConsumableCategory::getName).collect(Collectors.toList());
                    CellRangeAddressList rangeList = new CellRangeAddressList(
                            1, // 从第2行开始（跳过标题行）
                            65535, // 到最大行
                            4, 4   // 第3列（索引2）
                    );

                    DataValidationHelper helper = sheet.getDataValidationHelper();
                    DataValidationConstraint constraint = helper.createExplicitListConstraint(categoryNames.toArray(new String[0]));
                    DataValidation validation = helper.createValidation(constraint, rangeList);

                    // 可选：设置错误提示
                    // 代替原先的 setErrorBoxTitle 和 setError
                    validation.createErrorBox("输入错误", "请从下拉列表中选择有效选项");
                    validation.setShowErrorBox(true);
                    sheet.addValidationData(validation);
                }
            };

            // 写入Excel
            EasyExcel.write(response.getOutputStream(), MaterialTemplate.class)
                    .registerWriteHandler(styleStrategy)
                    .sheet("耗材导入模板")
                    .registerWriteHandler(dropdownHandler)
                    // 自适应列宽策略
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .doWrite(dataList);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel模板失败", e);
        }
    }
}