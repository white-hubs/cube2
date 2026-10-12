package com.guhecloud.cube.biz.mdata.server.goods.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.server.goods.dao.GoodsCategoryMapper;
import com.guhecloud.cube.biz.mdata.server.goods.dto.GoodsCategorySyncDTO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryBatchRenewParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryCreateParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryIdsParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryQueryParam;
import com.guhecloud.cube.biz.mdata.server.goods.param.GoodsCategoryUpdateParam;
import com.guhecloud.cube.biz.mdata.server.goods.service.GoodsCategoryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {
    private final GoodsCategoryMapper goodsCategoryMapper;
    private final SupplierClient supplierClient;

    @Override
    public void save(@NonNull GoodsCategoryCreateParam param) {
        GoodsCategory goodsCategoryByName = this.getByName(param.getName());
        Assert.isConflict(Objects.nonNull(goodsCategoryByName), "存在商品分类信息");
        GoodsCategory goodsCategory = ObjectUtils.copying(param, GoodsCategory.class);
        this.save(goodsCategory);
    }

    @Override
    public void update(@NonNull GoodsCategoryUpdateParam param) {
        GoodsCategory goodsCategory = this.getById(param.getId());
        Assert.isNotFound(Objects.isNull(goodsCategory), "不存在商品分类");
        GoodsCategory goodsCategoryByName = this.getByName(param.getName());
        Assert.isConflict(
                Objects.nonNull(goodsCategoryByName) && !goodsCategoryByName.getId().equals(goodsCategory.getId()),
                "存在相同名称的商品分类"
                         );
        ObjectUtils.copying(param, goodsCategory);
        this.updateById(goodsCategory);
    }

    @Override
    public List<GoodsCategory> getByIds(@NonNull List<String> ids) {
        return this.lambdaQuery().in(GoodsCategory::getId, ids).list();
    }

    @Override
    public List<GoodsCategory> getList(GoodsCategoryQueryParam queryParam) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), GoodsCategory::getName, queryParam.getKeyword())
                .list();
    }

    @Override
    public List<GoodsCategory> getAppList(GoodsCategoryQueryParam queryParam) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), GoodsCategory::getName, queryParam.getKeyword())
                .list();
    }

    @Override
    public List<GoodsCategory> getBySequences(@NonNull List<String> sequences) {
        return this.baseMapper.getBySequences(sequences);
    }

    @Override
    public List<GoodsCategory> getThirdLevel(@NonNull List<String> sequences) {
        return this.baseMapper.getThirdLevel(sequences, 3);
    }

    @Override
    public List<GoodsCategory> getThirdLevelByIds(@NonNull List<Long> ids) {
        return this.baseMapper.getThirdLevelByIds(ids, 3);
    }

    @Override
    public List<GoodsCategory> getLastLevelByIds(@NonNull Collection<? extends Serializable> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(GoodsCategory::getId, ids).list();
    }

    @Override
    public void remove(@NonNull Long id) {
        List<GoodsCategory> goodsCategories = this.getByParentId(id);
        Assert.isNotAcceptable(CollectionUtils.isNotEmpty(goodsCategories), "存在下级分类");
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public GoodsCategory getByName(@NonNull String name) {
        return this.lambdaQuery().eq(GoodsCategory::getName, name).one();
    }

    @Override
    public GoodsCategory getByCode(@NonNull String code) {
        return this.lambdaQuery().eq(GoodsCategory::getCode, code).one();
    }

    @Override
    public List<GoodsCategory> getByParentId(Long parentId) {
        return this.lambdaQuery().eq(BasicEnhancedTreeEntity::getParentId, parentId).list();
    }

    @Override
    public List<String> getByIdAndLevel(GoodsCategoryIdsParam param) {
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> params = new ArrayList<>();
        switch (param.getLevel()) {
            case 1:
                ids.add(String.valueOf(param.getId()));
                params.add(String.valueOf(param.getId()));
                List<String> idsByParentIds = goodsCategoryMapper.getIdsByParentIds(params);
                ids.addAll(idsByParentIds);
                params.clear();
                params.addAll(idsByParentIds);
                List<String> idsByParentIds1 = goodsCategoryMapper.getIdsByParentIds(params);
                ids.addAll(idsByParentIds1);
                break;
            case 2:
                ids.add(String.valueOf(param.getId()));
                params.add(String.valueOf(param.getId()));
                List<String> idsByParentIds2 = goodsCategoryMapper.getIdsByParentIds(params);
                ids.addAll(idsByParentIds2);
                break;
            case 3:
                ids.add(String.valueOf(param.getId()));
                break;
        }
        return ids;
    }

    @Override
    public List<String> getNameByIds(List<String> ids) {
        SupplierDTO bySupplierTenant = supplierClient.getBySupplierTenant(SessionContextHolder.getRequester().getTenant());
        if (bySupplierTenant != null) {
            SessionContextHolder.requestTenant(bySupplierTenant.getTenant());
        }

        return goodsCategoryMapper.getNameByIds(ids);
    }

    /**
     * 批量更新商品品类(名称)
     * 编码相同就修改名称，不同就新增
     * 注意这里的品类的父级都是
     *
     * @param param 品类
     */
    @Override
    public void thirdCategoryBatchRenew(GoodsCategoryBatchRenewParam param) {
        if (param == null || param.getList() == null || param.getList().isEmpty()) {
            return;
        }
        // 检查对接一级品类
        GoodsCategory parent = goodsCategoryMapper.selectOne(Wrappers.<GoodsCategory>lambdaQuery()
                                                                     .eq(GoodsCategory::getCode, "thirdCategoryCode")
                                                                     .last(" limit 1 ")
                                                            );
        Long parentId;
        if (parent == null) {
            parent = new GoodsCategory();
            parentId = IdWorker.getId();
            parent.setId(parentId);
            parent.setCode("thirdCategoryCode");
            parent.setName("同步的品类");
            goodsCategoryMapper.insert(parent);
        } else {
            parentId = parent.getId();
        }

        Map<Long, GoodsCategory> goodsCategoryMap = new HashMap<>();
        List<GoodsCategory> list = this.goodsCategoryMapper.selectList(Wrappers.<GoodsCategory>lambdaQuery()
                                                                               .likeLeft(GoodsCategory::getSequence, parentId)
                                                                      );
        if (list != null && !list.isEmpty()) {
            for (GoodsCategory goodsCategory : list) {
                goodsCategoryMap.put(goodsCategory.getId(), goodsCategory);
            }
        }

        // 更新数据
        List<GoodsCategory> updateList = new ArrayList<>();
        List<GoodsCategory> insertList = new ArrayList<>();
        List<GoodsCategorySyncDTO> paramList = param.getList();
        for (GoodsCategorySyncDTO goodsCategorySyncDTO : paramList) {
            GoodsCategory category = goodsCategoryMap.get(goodsCategorySyncDTO.getId());
            if (category == null) {
                /* 新增 */
                category = new GoodsCategory();
                category.setId(goodsCategorySyncDTO.getId());
                category.setCode(goodsCategorySyncDTO.getCode());
                category.setName(goodsCategorySyncDTO.getName());
                category.setEnabled(true);
                category.setTenant(goodsCategorySyncDTO.getTenant());
                category.setParentId(goodsCategorySyncDTO.getParentId() == null ? parentId : goodsCategorySyncDTO.getParentId());
                category.setDeleted(false);
                EntityAuditor.populateCreateAudit(category, goodsCategorySyncDTO.getCreateBy(), StringUtils.EMPTY_STRING,
                                                  ActorTypeEnum.SYSTEM, goodsCategorySyncDTO.getCreateTime());
                EntityAuditor.populateUpdateAudit(category, goodsCategorySyncDTO.getUpdateBy(), StringUtils.EMPTY_STRING,
                                                  ActorTypeEnum.SYSTEM, goodsCategorySyncDTO.getUpdateTime());
                insertList.add(category);
            } else if (
                    (StringUtils.isNotEmpty(goodsCategorySyncDTO.getName()) && !goodsCategorySyncDTO.getName().equals(category.getName()))
                    ||
                    (StringUtils.isNotEmpty(goodsCategorySyncDTO.getCode()) && !goodsCategorySyncDTO.getCode().equals(category.getCode()))
            ) {
                /* 如果名称/code不同才修改，一样就不做处理 */
                category.setName(goodsCategorySyncDTO.getName());
                category.setUpdateBy(goodsCategorySyncDTO.getUpdateBy());
                category.setUpdateTime(goodsCategorySyncDTO.getUpdateTime());
                updateList.add(category);
            }
        }
        /* 树形结构暂时还不支持批量插入，所以只能单个插入(不过只有第一次同步会插入大量数据，之后只是更新的的才修改，所以还好) */
        for (GoodsCategory goodsCategory : insertList) {
            this.save(goodsCategory);
        }
        for (GoodsCategory goodsCategory : updateList) {
            this.updateById(goodsCategory);
        }

    }
}
