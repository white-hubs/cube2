package com.guhecloud.cube.iot.mgt.north.server.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthProductEventMapper;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthProductMapper;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthProductMethodMapper;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthProductPropMapper;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthSysDictItemMapper;
import com.guhecloud.cube.iot.mgt.north.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.north.param.ItemQueryParam;
import com.guhecloud.cube.iot.mgt.north.param.ProductCategoryParam;
import com.guhecloud.cube.iot.mgt.north.param.ProductPageParam;
import com.guhecloud.cube.iot.mgt.north.vo.ItemVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductCategoryVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductEventVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductMethodVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductPropVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductVo;

import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class NorthProductService {

    @Resource
    private NorthProductMapper northProductMapper;
    @Resource
    private NorthProductPropMapper northProductPropMapper;
    @Resource
    private NorthProductEventMapper northProductEventMapper;
    @Resource
    private NorthProductMethodMapper northProductMethodMapper;
    @Resource
    private NorthSysDictItemMapper northSysDictItemMapper;

    public Pagination<ProductVo> page(ProductPageParam param) {
        Page<ProductVo> page = northProductMapper.queryPage(PagingUtils.convert(param), param,
                                                            StringUtils.isEmpty(param.getProductId()) ? null
                                                                                                      : Long.parseLong(param.getProductId()));

        if (Objects.isNull(page) || page.getRecords().isEmpty()) {
            return Pagination.<ProductVo>builder()
                    .pages(0L)
                    .total(0L)
                    .records(null)
                    .build();
        }

        //格式化产品属性、事件、命令、方法
        for (ProductVo productVo : page.getRecords()) {
            if (param.isWithPropertySpec()) {
                productVo.setProductPropVoList(queryProductProp(productVo.getProdId()));
            }

            if (param.isWithEventSpec()) {
                productVo.setProductEventVoList(queryProductEvent(productVo.getProdId()));
            }

            if (param.isWithMethodSpec()) {
                productVo.setProductMethodVoList(queryProductMethod(productVo.getProdId()));
            }
        }

        return Pagination.<ProductVo>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

    /**
     * 查询产品属性
     *
     * @param prodId 产品ID
     *
     * @return 产品属性列表
     */
    public List<ProductPropVo> queryProductProp(String prodId) {
        List<ProductPropVo> productPropVos = northProductPropMapper.queryProductProp(Long.parseLong(prodId));

        if (Objects.isNull(productPropVos) || productPropVos.isEmpty()) {
            return Collections.emptyList();
        }

        for (ProductPropVo productPropVo : productPropVos) {
            JSONArray dc = JSON.parseArray(productPropVo.getDataConstraintStr());
            productPropVo.setDataConstraint(dc);
        }
        return productPropVos;
    }

    /**
     * 查询产品事件
     *
     * @param prodId 产品ID
     *
     * @return 产品事件列表
     */
    public List<ProductEventVo> queryProductEvent(String prodId) {
        List<ProductEventVo> productEventVos = northProductEventMapper.queryProductEvent(Long.parseLong(prodId));

        if (Objects.isNull(productEventVos) || productEventVos.isEmpty()) {
            return Collections.emptyList();
        }
        return productEventVos;
    }

    /**
     * 查询产品方法（仅返回已启用的方法）
     *
     * @param prodId 产品ID
     *
     * @return 产品方法列表
     */
    public List<ProductMethodVo> queryProductMethod(String prodId) {
        List<ProductMethodVo> productMethodVos = northProductMethodMapper.queryProductMethod(Long.parseLong(prodId));

        if (Objects.isNull(productMethodVos) || productMethodVos.isEmpty()) {
            return Collections.emptyList();
        }

        for (ProductMethodVo productMethodVo : productMethodVos) {
            // 解析输入参数JSON字符串为Map
            if (StringUtils.isNotEmpty(productMethodVo.getInputParamsStr())) {
                productMethodVo.setInputParams(JSON.parseObject(productMethodVo.getInputParamsStr()));
            }
            // 解析输出参数JSON字符串为Map
            if (StringUtils.isNotEmpty(productMethodVo.getOutputParamsStr())) {
                productMethodVo.setOutputParams(JSON.parseObject(productMethodVo.getOutputParamsStr()));
            }
        }
        return productMethodVos;
    }

    /**
     * 仅针对产品品类进行树级查询
     *
     * @param itemQueryParam itemQueryParam
     *
     * @return list
     */
    public List<ItemVo> category(ItemQueryParam itemQueryParam) {
        List<ItemVo> listItemVo = northProductMapper.listProductCategory(itemQueryParam, InnerDicEnum.prod_category.id);

        if (Objects.isNull(listItemVo) || listItemVo.isEmpty()) {
            return Collections.emptyList();
        }

        List<ItemVo> unknownParents = new LinkedList<>();
        List<ItemVo> rootItem = new ArrayList<>();

        //找到root元素，将所有子节点归类到unknown-list中
        for (ItemVo itemVo : listItemVo) {
            //归类root
            if (Objects.isNull(itemVo.getItemParentId()) || itemVo.getItemParentId() == 0) {
                rootItem.add(itemVo);
                continue;
            }

            unknownParents.add(itemVo);
        }

        //将剩余的子节点、孙子节点item项put到Root节点中
        putCascadeItem2Root(unknownParents, rootItem);

        return rootItem;
    }

    /**
     * 将未知的item项放到rootItem中
     *
     * @param unknownParents unknownParents
     * @param rootItem       rootItem
     */
    private void putCascadeItem2Root(List<ItemVo> unknownParents, List<ItemVo> rootItem) {
        int loopMax = 30;//避免因配置导致错误
        //遍历unknown-list不停尝试找到所有子节点的父类
        while (!unknownParents.isEmpty() && loopMax > 0) {
            Iterator<ItemVo> iterator = unknownParents.iterator();
            while (iterator.hasNext()) {
                ItemVo unknownParentItem = iterator.next();

                for (ItemVo itemVo : rootItem) {
                    //寻找父Item
                    ItemVo parentItem = findItemById(itemVo, unknownParentItem.getItemParentId());
                    if (Objects.isNull(parentItem)) {
                        continue;
                    }

                    if (parentItem.getChildrenList() == null) {
                        parentItem.setChildrenList(new ArrayList<>());
                    }

                    parentItem.getChildrenList().add(unknownParentItem);
                    iterator.remove();
                }
            }

            loopMax--;
        }
    }

    /**
     * 根据给定的ItemId找到item类<br/>
     * 查找按照深度优先进行遍历
     *
     * @param itemVo 带层级的itemVo树
     * @param itemId 待查找的itemId
     *
     * @return itemVo 如果为空返回null
     */
    private ItemVo findItemById(ItemVo itemVo, Long itemId) {
        if (itemVo.getItemId().equals(itemId)) {
            return itemVo;
        }

        List<ItemVo> childItem = itemVo.getChildrenList();
        if (Objects.isNull(childItem)) {
            return null;
        }

        for (ItemVo vo : childItem) {
            if (vo.getItemId().equals(itemId)) {
                return vo;
            }

            if (vo.getChildrenList() != null) {
                ItemVo findItem = findItemById(vo, itemId);

                if (Objects.nonNull(findItem)) {
                    return findItem;
                }
            }
        }

        return null;
    }

    public List<ProductVo> prodListDetail(Set<Long> prodIds) {
        List<ProductVo> productVos = northProductMapper.queryDetailByProdIds(prodIds);

        if (Objects.isNull(productVos) || productVos.isEmpty()) {
            return Collections.emptyList();
        }

        //格式化产品属性、事件、命令、方法
        for (ProductVo productVo : productVos) {
            productVo.setProductPropVoList(queryProductProp(productVo.getProdId()));

            productVo.setProductEventVoList(queryProductEvent(productVo.getProdId()));

            productVo.setProductMethodVoList(queryProductMethod(productVo.getProdId()));

            //not implements command
        }
        return productVos;
    }

    public List<ProductCategoryVo> getProductCategoryTree(ProductCategoryParam productCategoryParam) {

        return listDictItemTree(InnerDicEnum.prod_category.id);
    }

    private List<ProductCategoryVo> listDictItemTree(long dictId) {
        //加载并排序,生成一下顺序
        //A001
        //A001002
        //A002
        //A002002
        //A002002001
        //A003
        //A003001
        //A003002
        List<SysDictItem> sysDictItems = northSysDictItemMapper.selectList(Wrappers.<SysDictItem>lambdaQuery()
                                                                                   .eq(SysDictItem::getDictId, dictId)
                                                                                   .orderByAsc(SysDictItem::getItemLevel)//按照层级排序
                                                                          );

        if (Objects.isNull(sysDictItems) || sysDictItems.isEmpty()) {
            return Collections.emptyList();
        }

        //转换
        Map<String, ProductCategoryVo> tempCache = new HashMap<>();
        List<ProductCategoryVo> dictItemTreeVos = new ArrayList<>();
        int levelCharSize = 3;

        //处理第一个元素
        SysDictItem preSysDictItem = sysDictItems.get(0);
        ProductCategoryVo treeVo = toItemTree(preSysDictItem);
        treeVo.setCategoryCodeConcat(treeVo.getCategoryCode());
        tempCache.put(treeVo.getCategoryCode(), treeVo);
        dictItemTreeVos.add(treeVo);

        for (int i = 1; i < sysDictItems.size(); i++) {
            SysDictItem sysDictItem = sysDictItems.get(i);
            treeVo = toItemTree(sysDictItem);

            //层级code拼接
            tempCache.put(treeVo.getCategoryCode(), treeVo);

            String curtItemLevel = sysDictItem.getItemLevel();

            //顶级节点
            if (curtItemLevel.length() == (levelCharSize + 1)) {
                dictItemTreeVos.add(treeVo);
                treeVo.setCategoryCodeConcat(treeVo.getCategoryCode());

                continue;
            }

            //两个等级长度一致: A001, A002; 或则子节点: A001001, A001002
            ProductCategoryVo parent = tempCache.get(curtItemLevel.substring(0, curtItemLevel.length() - levelCharSize));

            //兼容老旧代码结构
            if (Objects.isNull(parent)) {
                log.error("parent is null, itemLevel:{}", curtItemLevel);
                continue;
            }

            parent.getChildren().add(treeVo);
            treeVo.setCategoryCodeConcat(parent.getCategoryCodeConcat() + "-" + treeVo.getCategoryCode());
        }

        return dictItemTreeVos;
    }

    private ProductCategoryVo toItemTree(SysDictItem sysDictItem) {
        ProductCategoryVo productCategoryVo = new ProductCategoryVo();
        productCategoryVo.setCategoryId(sysDictItem.getId());
        productCategoryVo.setCategoryName(sysDictItem.getItemLabel());
        productCategoryVo.setCategoryCode(sysDictItem.getItemLevel());
        productCategoryVo.setChildren(new ArrayList<>());

        return productCategoryVo;
    }
}
