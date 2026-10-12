package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.iot.product.model.v3.constant.ModelConstants;
import com.guhecloud.cube.base.iot.product.model.v3.enums.DictEnum;import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductCategoryModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3DictItemService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductCategoryService;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemDetailWithPathVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemTreeVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品品类服务实现类
 * 提供品类管理能力,支持树形结构
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3ProductCategoryServiceImpl implements V3ProductCategoryService {

    private final V3DictItemService dictItemService;

    @Override
    public Page<DictItemVO> page(DictItemPageParam param) {
        log.info("分页查询品类, param={}", param);

        // 设置固定的dictId为品类字典ID
        param.setDictId(DictEnum.PRODUCT_CATEGORY.getId());

        // 调用通用字典服务
        Page<DictItemVO> result = dictItemService.page(param);

        log.info("品类分页查询完成, total={}", result.getTotal());
        return result;
    }

    @Override
    public DictItemVO getById(Long id) {
        log.info("查询品类详情, id={}", id);

        return dictItemService.getById(id);
    }

    @Override
    public List<DictItemTreeVO> listTree(Long dictItemId) {
        log.info("查询树形品类, dictItemId={}", dictItemId);

        // 调用通用字典服务的树形查询
        List<DictItemTreeVO> tree = dictItemService.listTree(
                DictEnum.PRODUCT_CATEGORY.getId(),
                dictItemId
        );

        log.info("树形品类查询完成, rootCount={}", tree.size());
        return tree;
    }

    @Override
    public void save(ProductCategoryModifyParam param) {
        log.info("新增品类, param={}", param);

        // 转换为通用字典修改参数(固定dictId为品类字典)
        DictItemModifyParam dictParam = param.toCommonDictItemModifyParam(
                DictEnum.PRODUCT_CATEGORY.getId()
        );

        // 调用通用字典服务
        dictItemService.save(dictParam);

        log.info("品类新增成功");
    }

    @Override
    public void update(Long id, ProductCategoryModifyParam param) {
        log.info("修改品类, id={}, param={}", id, param);

        // 转换为通用字典修改参数(固定dictId为品类字典)
        DictItemModifyParam dictParam = param.toCommonDictItemModifyParam(
                DictEnum.PRODUCT_CATEGORY.getId()
        );

        // 调用通用字典服务
        dictItemService.update(id, dictParam);

        log.info("品类修改成功, id={}", id);
    }

    @Override
    public void delete(Long id) {
        log.info("删除品类, id={}", id);

        // 调用通用字典服务
        dictItemService.delete(id);

        log.info("品类删除成功, id={}", id);
    }

    @Override
    public DictItemDetailWithPathVO getDetailWithPath(Long id, Boolean includeChildren) {
        log.info("查询品类详情及路径, id={}, includeChildren={}", id, includeChildren);

        // 调用通用字典服务
        DictItemDetailWithPathVO result = dictItemService.getDetailWithPath(id, includeChildren);

        log.info("品类详情及路径查询完成, id={}", id);
        return result;
    }
}
