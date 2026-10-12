package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.srv.gen.property.management.server.common.dao.StorehouseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.common.entity.Storehouse;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.impl.UserClientService;
import com.guhecloud.cube.srv.gen.property.management.server.common.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCategoryMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStockDetailMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableStoreStockMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.StockTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStockDetailService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStockDetailExtendVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStockDetailVO;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：出入库记录明细服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableStockDetailServiceImpl extends ServiceImpl<ConsumableStockDetailMapper, ConsumableStockDetail> implements ConsumableStockDetailService {
    private final ConsumableStoreStockMapper consumableStoreStockMapper;
    private final ConsumableCategoryMapper consumableCategoryMapper;
    private final StorehouseMapper storehouseMapper;
    private final FileClient fileClient;
    private final UserClientService userClientService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ConsumableStockDetailParam param) {
        ConsumableStockDetail consumableStockDetail = ObjectUtils.copying(param, ConsumableStockDetail.class);
        this.baseMapper.insert(consumableStockDetail);
        return consumableStockDetail.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ConsumableStockDetailParam param) {
        ConsumableStockDetail consumableStockDetail = this.baseMapper.selectById(id);
        Assert.parameterIsNull(consumableStockDetail, PropertyStatus.CONSUMABLE_NO_DETAILS);
        ObjectUtils.copying(param, consumableStockDetail);
        this.baseMapper.updateById(consumableStockDetail);
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
    public ConsumableStockDetailVO getById(Long id) {
        ConsumableStockDetail consumableStockDetail = this.baseMapper.selectById(id);
        return ObjectUtils.copying(consumableStockDetail, ConsumableStockDetailVO.class);
    }

    @Override
    public Pagination<ConsumableStockDetailVO> getPage(ConsumableStockDetailQueryParam param) {
        //请自行添加查询条件
        Page<ConsumableStockDetail> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, ConsumableStockDetailVO.class);
    }

    @Override
    public void extendInitStockInfo(Long storeId, ConsumableStockDetailParam param) {
        ConsumableStoreStock consumableStoreStock = consumableStoreStockMapper.selectOne(Wrappers.<ConsumableStoreStock>lambdaQuery()
                                                                                                 .eq(ConsumableStoreStock::getStoreId, storeId)
                                                                                                 .eq(ConsumableStoreStock::getConsumableId, param.getConsumableId()));
        if (consumableStoreStock != null) {
            param.setInitNum(consumableStoreStock.getNum());
            param.setInitAmount(consumableStoreStock.getAmount());
        } else {
            param.setInitNum(0);
            param.setInitAmount(BigDecimal.ZERO);
        }
    }

    @Override
    public Pagination<ConsumableStockDetailExtendVO> getStockRecordDetail(ConsumableStockDetailQueryParam param) {
        if (ObjectUtils.isNotEmpty(param.getPeriod()) && ObjectUtils.isNotEmpty(param.getPeriod().getBegin())) {
            param.setBegin(DateUtils.formatDate(param.getPeriod().getBegin()));
        }
        Page<ConsumableStockDetailExtendVO> page = this.baseMapper.getStockRecordDetail(PagingUtils.convert(param), param);

        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            //扩展价格和数量相关信息
            this.extend(page.getRecords());
            return PagingUtils.convert(page);
        }

        return Pagination.empty();
    }

    /**
     * 扩展数量和价格相关信息
     *
     * @param datas
     */
    private void extend(List<ConsumableStockDetailExtendVO> datas) {
        datas.stream().forEach(vo -> {
            //计算出入库均价
            vo.setInitPrice(vo.getInitNum().intValue() == 0 ? BigDecimal.ZERO
                                                            : vo.getInitAmount().divide(vo.getInitNum(), 2, RoundingMode.HALF_UP));
            if (StockTypeEnum.ADD.name().equals(vo.getStockFlag())) {
                //计算结余数量 金额 单价
                vo.setEndNum(vo.getInitNum().add(new BigDecimal(vo.getNum())));//数量
                vo.setEndAmount(vo.getInitAmount().add(vo.getStoreAmount()));//金额
                vo.setEndPrice(
                        vo.getEndNum().intValue() == 0 ? BigDecimal.ZERO : vo.getEndAmount().divide(vo.getEndNum(), 2, RoundingMode.UP));
            } else {
                //计算结余数量 金额 单价
                vo.setEndNum(vo.getInitNum().subtract(new BigDecimal(vo.getNum())));//数量
                vo.setEndAmount(vo.getInitAmount().subtract(vo.getStoreAmount()));//金额
                vo.setEndPrice(vo.getInitPrice());
            }
            //类型
            ConsumableCategory category = consumableCategoryMapper.selectById(vo.getCategoryId());
            vo.setCategoryName(category != null ? category.getName() : "");
            Storehouse storehouse = storehouseMapper.selectById(vo.getStoreId());
            vo.setStoreName(storehouse != null ? storehouse.getName() : "");
            vo.setCreateBy(userClientService.getCreateByName(vo.getCreateBy(), vo.getCreatorId(), vo.getCreatorType()));
            //查询耗材图片
            if (StringUtils.isNotBlank(vo.getFileIds())) {
                List<Long> fileIds = Arrays.stream(vo.getFileIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
                vo.setFiles(fileClient.getByIds(fileIds));
            }
            vo.setStockFlag(StockTypeEnum.valueOf(vo.getStockFlag()).getDesc());
        });
    }

    @Override
    public void exportStockRecordDetail(ConsumableStockDetailQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ConsumableStockDetailExtendVO> datas = this.baseMapper.getStockRecordDetail(param);
        //扩展耗材数量和价格信息
        this.extend(datas);
        if (ObjectUtils.isEmpty(datas)) {
            ConsumableStockDetailExtendVO extend = new ConsumableStockDetailExtendVO();
            datas.add(extend);
        }
        for (ConsumableStockDetailExtendVO ex : datas) {
            ex.setHandler(userClientService.getUser(ex.getHandler()));
        }
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("stockFlag", "出入库类型");
        writer.addHeaderAlias("name", "耗材名称");
        writer.addHeaderAlias("code", "耗材编码");
        writer.addHeaderAlias("categoryName", "分类名称");
        writer.addHeaderAlias("storeName", "所在仓库");
        writer.addHeaderAlias("brand", "品牌");
        writer.addHeaderAlias("unit", "单位");
        writer.addHeaderAlias("handleDate", "处理时间");
        writer.addHeaderAlias("handler", "处理人");
        writer.addHeaderAlias("initNum", "初始数量");
        writer.addHeaderAlias("initPrice", "初始价格");
        writer.addHeaderAlias("initAmount", "初始金额");
        writer.addHeaderAlias("num", "出入库数量");
        writer.addHeaderAlias("price", "出入库价格");
        writer.addHeaderAlias("storeAmount", "出入库金额");
        writer.addHeaderAlias("endNum", "结余数量");
        writer.addHeaderAlias("endPrice", "结余价格");
        writer.addHeaderAlias("endAmount", "结余金额");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        DataFormat dataFormat2 = writer.getWorkbook().createDataFormat();
        CellStyle cellStyle2 = writer.getStyleSet().getCellStyleForDate();
        short format2 = dataFormat2.getFormat("yyyy-MM-dd");
        cellStyle2.setDataFormat(format2);
        writer.setStyle(cellStyle2, 1, 8);
        writer.setColumnWidth(-1, 25);
        String fileName = "耗材出入库记录";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }
}