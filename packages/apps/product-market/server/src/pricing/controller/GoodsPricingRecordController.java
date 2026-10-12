package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordThirdParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingRecordService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceTrendStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingRecordThirdVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Appet
 * @Description 采价记录
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingRecordController {
    private final GoodsPricingRecordService goodsPricingRecordService;

    /**
     * 功能描述： 采价记录添加
     */
    @PostMapping(value = "/goods-pricing-record")
    public Long add(@Valid @RequestBody GoodsPricingRecordParam param) {
        return goodsPricingRecordService.add(param);
    }

    /**
     * 功能描述： 采价记录修改
     */
    @PutMapping(value = "/goods-pricing-record/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPricingRecordParam param) {
        goodsPricingRecordService.update(id, param);
    }

    /**
     * 采价记录价格修改
     *
     * @param id
     * @param param
     */
    @PutMapping(value = "/goods-pricing-record/price/{id}")
    public void updatePrice(@PathVariable Long id, @Valid @RequestBody GoodsPricingRecordParam param) {
        goodsPricingRecordService.updatePrice(id, param);
    }

    /**
     * 功能描述： 采价记录删除
     */
    @DeleteMapping(value = "/goods-pricing-record/{id}")
    public void delete(@PathVariable Long id) {
        goodsPricingRecordService.delete(id);
    }

    /**
     * 功能描述： 采价记录批量删除
     */
    @DeleteMapping(value = "/goods-pricing-record/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPricingRecordService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价记录分页
     */
    @GetMapping(value = "/goods-pricing-record/page")
    public Pagination<GoodsPricingRecordVO> getPage(GoodsPricingRecordQueryParam param) {
        return goodsPricingRecordService.getPage(param);
    }

    /**
     * 采价记录导出
     *
     * @param param
     */
    @PostMapping(value = "/goods-pricing-record/export")
    public void export(@RequestBody GoodsPricingRecordQueryParam param,
                       HttpServletRequest request,
                       HttpServletResponse response
                      ) {
        List<GoodsPricingRecordVO> dataList = goodsPricingRecordService.getList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(dataList), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("createTime", "采价时间");
        writer.addHeaderAlias("goodsCategoryName", "商品名称");
        writer.addHeaderAlias("maxPrice", "最高价(元/公斤)");
        writer.addHeaderAlias("midPrice", "中间价(元/公斤)");
        writer.addHeaderAlias("minPrice", "最低价(元/公斤)");
        writer.addHeaderAlias("goodsFrom", "商品产地");
        writer.addHeaderAlias("stallCode", "采价来源");
        writer.addHeaderAlias("userName", "采价人");
        writer.addHeaderAlias("taskName", "采价任务");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(dataList, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "商品采价记录";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 功能描述： 采价记录详情
     */
    @GetMapping(value = "/goods-pricing-record/{id}")
    public GoodsPricingRecordVO getById(@PathVariable Long id) {
        return goodsPricingRecordService.getById(id);
    }

    /**
     * 功能描述： 根据采价情况，计算价格同比和环比值
     */
    @PostMapping(value = "/goods-pricing-record/compare/stat")
    public Pagination<GoodsPriceStatVO> getGoodsPriceStat(@Valid @RequestBody GoodsPriceStatParam param) {
        return goodsPricingRecordService.getGoodsPriceStat(param);
    }

    /**
     * 导出价格分析数据
     *
     * @param param
     *
     * @return
     */
    @PostMapping(value = "/goods-pricing-record/compare/stat/export")
    public void getGoodsPriceStatExport(@Valid @RequestBody GoodsPriceStatParam param,
                                        HttpServletRequest request,
                                        HttpServletResponse response
                                       ) {
        List<GoodsPriceStatVO> dataList = goodsPricingRecordService.getGoodsPriceStatForAll(param);
        Assert.isNotFound(CollectionUtils.isEmpty(dataList), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("goodsCategoryName", "品类/商品名称");
        writer.addHeaderAlias("incomingQuantity", "来货量(KG)");
        writer.addHeaderAlias("maxPriceAvg", "最高均价(元/公斤)");
        writer.addHeaderAlias("qoqMaxPricePert", "环比增长（高）");
        writer.addHeaderAlias("yoyMaxPricePert", "同比增长（高）");
        writer.addHeaderAlias("midPriceAvg", "中间均价(元/公斤)");
        writer.addHeaderAlias("qoqMidPricePert", "环比增长（中）");
        writer.addHeaderAlias("yoyMidPricePert", "同比增长（中）");
        writer.addHeaderAlias("minPriceAvg", "最低均价(元/公斤)");
        writer.addHeaderAlias("qoqMinPricePert", "环比增长（低）");
        writer.addHeaderAlias("yoyMinPricePert", "同比增长（低）");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(dataList, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "商品价格分析";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 功能描述： 根据采价情况，计算商品的价格趋势
     *
     * @param param
     *
     * @return
     */
    @PostMapping(value = "/goods-pricing-record/trend/stat")
    public List<List<GoodsPriceTrendStatVO>> getGoodsPriceTrendStat(@Valid @RequestBody GoodsPriceStatParam param) {
        return goodsPricingRecordService.getGoodsPriceTrendStat(param);
    }

    /**
     * 将价格趋势数据，行转列
     *
     * @param param
     *
     * @return
     */
    @PostMapping(value = "/goods-pricing-record/trend/stat/rtc")
    public List<Map<String, Object>> getGoodsPriceTrendStatForRtc(@Valid @RequestBody GoodsPriceStatParam param) {
        return goodsPricingRecordService.getGoodsPriceRToC(param);
    }

    /**
     * 第三方商品采价记录分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @PostMapping("/third/goods-pricing-record/page")
    public Pagination<GoodsPricingRecordThirdVO> getPricingRecordThirdPage(@RequestBody GoodsPricingRecordThirdParam param) {
        return goodsPricingRecordService.getPricingRecordThirdPage(param);
    }

    /**
     * 第三方商品采价记录列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @PostMapping("/third/goods-pricing-record/list")
    public List<GoodsPricingRecordThirdVO> getPricingRecordThirdList(@RequestBody GoodsPricingRecordThirdParam param) {
        return goodsPricingRecordService.getPricingRecordThirdList(param);
    }
}