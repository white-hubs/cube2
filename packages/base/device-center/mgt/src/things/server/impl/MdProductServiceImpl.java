package com.guhecloud.cube.iot.mgt.things.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.iot.mgt.base.mapper.IotSysUnitMapper;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.base.service.SysDictCacheService;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemRefService;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemService;
import com.guhecloud.cube.iot.mgt.base.service.impl.CommonDictItemService;
import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.base.vo.IotSysUnitVo;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.comm.util.SnowflakeBase62Util;
import com.guhecloud.cube.iot.mgt.core.data.lang.AccProtocolEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProduct;
import com.guhecloud.cube.iot.mgt.things.param.ProdDetailQueryParam;
import com.guhecloud.cube.iot.mgt.things.param.ProdFetchFullParam;
import com.guhecloud.cube.iot.mgt.things.param.ProductPageParam;
import com.guhecloud.cube.iot.mgt.things.server.MdProductEventService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductMethodService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductPropService;
import com.guhecloud.cube.iot.mgt.things.server.MdProductService;
import com.guhecloud.cube.iot.mgt.things.vo.ProdNameAndNodeTypeVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductChangeEvent;
import com.guhecloud.cube.iot.mgt.things.vo.ProductEventVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodMgtVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductPropVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.comm.translate.ThingsConvert.toNodeDictTypeId;
import static com.guhecloud.cube.iot.mgt.comm.translate.ThingsConvert.toNodeTypeName;
import static com.guhecloud.cube.iot.mgt.comm.translate.ThingsConvert.toStatusName;
import static com.guhecloud.cube.iot.mgt.things.ProductUtils.incrementProductVersion;

@Service
@Slf4j
public class MdProductServiceImpl extends ServiceImpl<MdProductMapper, MdProduct> implements MdProductService {
    @Resource
    private PlatformEvnCom platformEvnCom;
    @Resource
    private CommonDictItemService commonDictItemService;
    @Resource
    private SysDictCacheService sysDictCacheService;
    @Resource
    private SysDictItemService sysDictItemService;
    @Resource
    private SysDictItemRefService sysDictItemRefService;
    @Resource
    private MdProductPropService productPropService;
    @Resource
    private MdProductEventService productEventService;
    @Resource
    private MdProductMethodService productMethodService;
    @Resource
    private MdProductMapper mdProductMapper;
    @Resource
    private FileClient fileClient;
    @Resource
    private FileRefClient fileRefClient;
    @Resource
    private IotSysUnitMapper iotSysUnitMapper;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void saveProduct(ProductVo param) {
        var count = this.count(new LambdaQueryWrapper<MdProduct>()
                                       .eq(MdProduct::getProdModel, param.getProdModel())
                                       .or()
                                       .eq(MdProduct::getProdName, param.getProdName())
                              );
        Assert.isTrue(count == 0, "已存在相同的产品名称【" + param.getProdName() + "】或者产品型号");

        MdProduct product = toMdProduct(param);
        //默认为自定义
        if ((Objects.isNull(param.getIsCustom()))) {
            product.setIsCustom(1);
        } else {
            product.setIsCustom(param.getIsCustom());
        }

        //id可以动态指定
        if (Objects.isNull(param.getId())) {
            product.setId(platformEvnCom.nextSnowflakeId());
        }

        //状态默认启用
        product.setStatus(1);

        // 自动创建产品编号
        product.setProdNumber(SnowflakeBase62Util.shortId());

        Assert.notNull(product.getNodeType(), "节点类型不能为空");
        Assert.notNull(product.getProdCategoryId(), "产品品类不能为空");
        Assert.notNull(product.getProdBrandId(), "产品品牌不能为空");
        Assert.notNull(product.getProtocolId(), "接入协议不能为空");

        this.save(product);

        persistenceFile(product);

        if (platformEvnCom.isIotPlatform()) {
            incrementProductVersion(product.getId(), mdProductMapper);
        }
    }

    @Override
    public void updateProduct(ProductVo param) {
        Assert.notNull(param.getId(), "id can not be null");
        MdProduct product = this.getById(param.getId());
        Assert.notNull(product, "产品不存在");
        Assert.notNull(param.getProdCategoryId(), "产品品类不能为空");
        var count = this.count(new LambdaQueryWrapper<MdProduct>()
                                       .ne(MdProduct::getId, param.getId())
                                       .eq(MdProduct::getProdName, param.getProdName())
                              );
        Assert.isTrue(count == 0, "已存在相同的产品名称【" + param.getProdName() + "】或者产品型号");

        //暂时只能修改名字
        product.setProdName(param.getProdName());
        product.setRemark(param.getRemark());
        product.setProdIcon(param.getProdIcon());
        product.setProdPic(param.getProdPic());
        product.setProdBrandId(param.getProdBrandId());
        product.setProdModel(param.getProdModel());
        product.setProdCategoryId(param.getProdCategoryId());
        product.setExtendConfig(param.getExtendConfig());

        this.updateById(product);

        persistenceFile(product);

        incrementProductVersion(product.getId(), mdProductMapper);

        DictItemVo dictItemVo = commonDictItemService.getSingleById(product.getProdBrandId());

        //推送产品变更事件
        applicationEventPublisher.publishEvent(ProductChangeEvent.of(product.getId(), dictItemVo.getItemLabel()));
    }

    @Override
    public ProductVo productDetail(Long prodId) {
        Assert.notNull(prodId, "产品ID不能为空");
        MdProduct mdProduct = this.getById(prodId);

        Assert.notNull(mdProduct, "产品不存在");

        return convert(mdProduct);
    }

    /**
     * 将文件持久化<br/>
     * 由于文件服务不能友好区分业务关联的文件对象，因此数据库存储图片ID本身，并要求所有使用者使用FileVo对象
     *
     * @param product 产品
     */
    private void persistenceFile(MdProduct product) {
        if (Objects.isNull(product.getId())) {
            log.warn("产品ID不存在，使用临时存储方案！！");
            return;
        }

        Set<Long> fileIds = new HashSet<>(2);
        if (Objects.nonNull(product.getProdIcon())) {
            fileIds.add(product.getProdIcon());
        }
        if (Objects.nonNull(product.getProdPic())) {
            fileIds.add(product.getProdPic());
        }

        if (!fileIds.isEmpty()) {
            fileRefClient.ref(product.getId(), fileIds);
        }
    }

    @Override
    public Pagination<ProductVo> listByPage(ProductPageParam paging) {
        Page<MdProduct> page = getBaseMapper().selectPage(PagingUtils.convert(paging), new LambdaQueryWrapper<MdProduct>()
                .eq(Objects.nonNull(paging.getProdCategoryId()), MdProduct::getProdCategoryId, paging.getProdCategoryId())
                .like(StringUtils.isNotBlank(paging.getProdName()), MdProduct::getProdName, paging.getProdName())
                .orderByDesc(MdProduct::getCreateTime));

        if (page.getRecords().isEmpty()) {
            return Pagination.empty();
        }

        List<ProductVo> productVoList = page.getRecords()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());

        return PagingUtils.convert(page, productVoList);
    }

    @Override
    public void updateStatus(Long prodId, Integer status) {
        Assert.notNull(prodId, "prodId can not be null");
        Assert.isTrue((status == 0 || status == 1), "status must be 0 or 1");
        this.update(new LambdaUpdateWrapper<MdProduct>()
                            .set(MdProduct::getStatus, status)
                            .eq(MdProduct::getId, prodId));

        incrementProductVersion(prodId, mdProductMapper);
    }

    @Override
    public List<DictItemVo> mdProductService(Integer prodNodeType) {
        Long dictTypeId = toNodeDictTypeId(prodNodeType);

        List<SysDictItem> sysDictItemList = sysDictItemService.list(new LambdaQueryWrapper<SysDictItem>()
                                                                            .eq(SysDictItem::getDictId, dictTypeId));

        if (sysDictItemList.isEmpty()) {
            return Collections.emptyList();
        }

        return sysDictItemList.stream()
                .map(commonDictItemService::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<ProductVo> fetchFullProd(ProdFetchFullParam paging) {
        Page<MdProduct> page = getBaseMapper().selectPage(PagingUtils.convert(paging), new LambdaQueryWrapper<MdProduct>()
                .in(Objects.nonNull(paging.getProductIds()) && !paging.getProductIds().isEmpty(), MdProduct::getId, paging.getProductIds())
                .like(StringUtils.isNotBlank(paging.getProdName()), MdProduct::getProdName, paging.getProdName())
                .eq(Objects.nonNull(paging.getProdCategoryId()), MdProduct::getProdCategoryId, paging.getProdCategoryId())
                .eq(Objects.nonNull(paging.getNodeType()), MdProduct::getNodeType, paging.getNodeType())
                .eq(Objects.nonNull(paging.getProdBrandId()), MdProduct::getProdBrandId, paging.getProdBrandId())
                .eq(Objects.nonNull(paging.getProdNumberId()), MdProduct::getProdModel, paging.getProdNumberId())
                .eq(Objects.nonNull(paging.getAccessTypeId()), MdProduct::getAccessTypeId, paging.getAccessTypeId())
                //                .eq(MdProduct::getStatus, EnableStatus.ENABLE.code)
                .orderByDesc(MdProduct::getCreateTime));

        if (page.getRecords().isEmpty()) {
            return Pagination.empty();
        }

        List<ProductVo> productVoList = page.getRecords()
                .stream()
                .map(this::convert)
                .peek(this::fillProp)
                .peek(this::fillEvent)
                .peek(this::fillMethod)
                .map(productVo -> fillItemInfo(productVo, paging))
                .collect(Collectors.toList());

        return PagingUtils.convert(page, productVoList);
    }

    /**
     * 填充产品事件
     *
     * @param productVo 产品信息
     */
    private void fillEvent(ProductVo productVo) {
        List<ProductEventVo> eventVoList = productEventService.listProdEvent(productVo.getId());

        if (Objects.isNull(eventVoList) || eventVoList.isEmpty()) {
            return;
        }

        for (ProductEventVo productEventVo : eventVoList) {
            productEventVo.setCategoryItem(commonDictItemService.getSingleById(productEventVo.getCategoryId()));
        }

        productVo.setProductEventList(eventVoList);
    }

    /**
     * 填充产品方法
     *
     * @param productVo 产品信息
     */
    private void fillMethod(ProductVo productVo) {
        List<ProductMethodMgtVo> methodVoList = productMethodService.listProdMethod(productVo.getId());

        if (Objects.isNull(methodVoList) || methodVoList.isEmpty()) {
            return;
        }

        productVo.setProductMethodList(methodVoList);
    }

    /**
     * 填充产品属性
     *
     * @param productVo 产品信息
     */
    private void fillProp(ProductVo productVo) {
        List<ProductPropVo> propVoList = productPropService.listProdProp(productVo.getId());
        List<IotSysUnitVo> iotSysUnitVos = iotSysUnitMapper.queryAll();

        if (Objects.isNull(propVoList) || propVoList.isEmpty()) {
            return;
        }

        for (ProductPropVo productPropVo : propVoList) {
            IotSysUnitVo iotSysUnit = iotSysUnitVos.stream()
                    .filter(iotSysUnitVo -> iotSysUnitVo.getUnitCode().equals(productPropVo.getUnitCode()))
                    .findFirst().orElse(null);
            if (!ObjectUtils.isEmpty(iotSysUnit)) {
                DictItemVo vo = new DictItemVo();
                vo.setItemLabel(iotSysUnit.getUnitName());
                vo.setItemValue(iotSysUnit.getUnitCode());
                productPropVo.setUnitItem(vo);
            }
        }

        productVo.setProductPropList(propVoList);
    }

    /**
     * 填充字典信息<br/>
     * 字典项的所有级联
     *
     * @param productVo 产品信息
     *
     * @return 产品信息
     */
    private ProductVo fillItemInfo(ProductVo productVo, ProdFetchFullParam paging) {
        if (paging.isWithProdCategoryItem()) {
            DictItemVo dictItemVo = commonDictItemService.loadBottomItemTree(productVo.getProdCategoryId());
            productVo.setProdCategoryItem(dictItemVo);
        }

        if (paging.isWithProdBrandItem()) {
            DictItemVo dictItemVo = commonDictItemService.loadBottomItemTree(productVo.getProdBrandId());
            productVo.setProdBrandItem(dictItemVo);
        }

        if (paging.isWithAccessTypeItem()) {
            DictItemVo dictItemVo = commonDictItemService.loadBottomItemTree(productVo.getAccessTypeId());
            productVo.setAccessTypeItem(dictItemVo);
        }

        return productVo;
    }

    private ProductVo convert(MdProduct mdProduct) {
        ProductVo productVo = new ProductVo();
        productVo.setId(mdProduct.getId());
        productVo.setProdName(mdProduct.getProdName());
        // 产品编号， 兼容一下，如果为空，则用产品ID代替
        productVo.setProdNumber(StringUtils.isBlank(mdProduct.getProdNumber()) ? String.valueOf(mdProduct.getId()) : mdProduct.getProdNumber());
        productVo.setProdModel(mdProduct.getProdModel());
        productVo.setProdCategoryId(mdProduct.getProdCategoryId());
        productVo.setProdBrandId(mdProduct.getProdBrandId());
        productVo.setAccessTypeId(mdProduct.getAccessTypeId());
        productVo.setProtocolId(mdProduct.getProtocolId());
        productVo.setNodeType(mdProduct.getNodeType());
        productVo.setCreateTimeFormat(DateUtils.format(mdProduct.getCreateTime()));
        productVo.setUpdateTimeFormat(DateUtils.format(mdProduct.getUpdateTime()));
        productVo.setIsCustom(mdProduct.getIsCustom());
        productVo.setProdCategoryName(sysDictCacheService.dictItemLabelName(InnerDicEnum.prod_category.id, mdProduct.getProdCategoryId()));
        productVo.setProdCategoryItem(commonDictItemService.loadBottomItemTree(mdProduct.getProdCategoryId()));
        productVo.setProdBrandName(sysDictCacheService.dictItemLabelName(InnerDicEnum.prod_brand.id, mdProduct.getProdBrandId()));
        productVo.setAccessTypeName(commonDictItemService.dictItemLabelName(mdProduct.getAccessTypeId()));
        productVo.setProtocolName(AccProtocolEnum.idToName(mdProduct.getProtocolId()));
        productVo.setNodeTypeName(toNodeTypeName(mdProduct.getNodeType().intValue()));
        productVo.setStatus(mdProduct.getStatus());
        productVo.setStatusName(toStatusName(mdProduct.getStatus()));
        productVo.setProdCategoryNameWithCascade(commonDictItemService.loadBottomItemTreeLabelName(productVo.getProdCategoryId(), "-"));
        productVo.setRemark(mdProduct.getRemark());
        productVo.setExtendConfig(mdProduct.getExtendConfig());

        productVo.setDeviceCount(mdProductMapper.countDeviceByProdId(mdProduct.getId()));

        if (Objects.nonNull(mdProduct.getProdPic())) {
            productVo.setProdPicFile(fileClient.getById(mdProduct.getProdPic()));
        }
        if (Objects.nonNull(mdProduct.getProdIcon())) {
            productVo.setProdIconFile(fileClient.getById(mdProduct.getProdIcon()));
        }
        return productVo;
    }

    @Override
    public ProductVo productDetail(ProdDetailQueryParam param) {
        MdProduct product = this.getById(param.getProdId());
        if (Objects.isNull(product)) {
            return null;
        }

        ProductVo productVo = convert(product);
        if (param.isWithEvent()) {
            List<ProductEventVo> productEventList = productEventService.listProdEvent(param.getProdId());
            productVo.setProductEventList(productEventList);
        }

        if (param.isWithProp()) {
            List<ProductPropVo> productPropList = productPropService.listProdProp(param.getProdId());
            productVo.setProductPropList(productPropList);
        }

        return productVo;
    }

    @Override
    public void deleteProduct(Long prodId) {
        Assert.notNull(prodId, "prodId can not be null");
        MdProduct product = this.getById(prodId);
        Assert.notNull(product, "product can not be null");
        Assert.isTrue(product.getIsCustom() == 1, "无法删除产品库产品");

        Assert.isTrue(this.getBaseMapper().countDeviceByProdId(product.getId()) < 1,
                      "产品下拥有设备，无法删除");

        //删除产品
        DatabaseContextHolder.softDelete(this, prodId);
    }

    /**
     * 将ProductVo转换为MdProduct
     *
     * @param param productVo
     *
     * @return MdProduct
     */
    public MdProduct toMdProduct(ProductVo param) {
        Assert.notNull(param, "param can not be null");

        MdProduct mdProduct = new MdProduct();
        mdProduct.setId(param.getId());
        mdProduct.setProdName(param.getProdName());
        mdProduct.setIsCustom(param.getIsCustom());
        mdProduct.setProdCategoryId(param.getProdCategoryId());
        mdProduct.setNodeType(param.getNodeType());
        mdProduct.setProdBrandId(param.getProdBrandId());
        mdProduct.setProdModel(param.getProdModel());
        mdProduct.setAccessTypeId(param.getAccessTypeId());
        mdProduct.setProtocolId(param.getProtocolId());
        mdProduct.setRemark(param.getRemark());
        mdProduct.setExtendConfig(param.getExtendConfig());
        mdProduct.setAdminId(param.getAdminId());

        mdProduct.setProdIcon(param.getProdIcon());
        mdProduct.setProdPic(param.getProdPic());

        return mdProduct;
    }

    @Override
    public ProdNameAndNodeTypeVo queryProdNameAndNodeType(Long productId) {
        Assert.notNull(productId, "productId can not be null");

        return this.baseMapper.queryProdNameAndNodeType(productId);
    }

    /**
     * 获取所有产品<br/>
     * listAll 进提供id和名称,性能无法满足
     *
     * @return 所有产品
     */
    @Override
    public List<ProductVo> listAll() {
        List<MdProduct> list = getBaseMapper().selectList(new LambdaQueryWrapper<MdProduct>()
                                                                  .orderByDesc(MdProduct::getCreateTime));

        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        return list
                .stream()
                .map(mdProduct -> {
                    ProductVo productVo = new ProductVo();
                    productVo.setId(mdProduct.getId());
                    productVo.setProdName(mdProduct.getProdName());
                    productVo.setAccessTypeId(mdProduct.getAccessTypeId());
                    productVo.setNodeType(mdProduct.getNodeType());
                    productVo.setNodeTypeName(toNodeTypeName(mdProduct.getNodeType().intValue()));

                    return productVo;
                }).collect(Collectors.toList());
    }

    @Override
    public boolean isGateway(Long productId) {
        return mdProductMapper.isGateway(productId);
    }
}
