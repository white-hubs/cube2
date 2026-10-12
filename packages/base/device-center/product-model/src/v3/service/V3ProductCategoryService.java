package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductCategoryModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemDetailWithPathVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemTreeVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;

import java.util.List;

/**
 * 产品品类服务接口
 * 提供品类管理能力,支持树形结构
 */
public interface V3ProductCategoryService {

    /**
     * 分页查询品类
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Page<DictItemVO> page(DictItemPageParam param);

    /**
     * 根据ID查询品类详情
     *
     * @param id 品类ID
     * @return 品类VO
     */
    DictItemVO getById(Long id);

    /**
     * 查询树形品类列表
     *
     * @param dictItemId 指定节点ID(可选),为null时查询整棵树
     * @return 树形品类列表
     */
    List<DictItemTreeVO> listTree(Long dictItemId);

    /**
     * 新增品类
     *
     * @param param 新增参数
     */
    void save(ProductCategoryModifyParam param);

    /**
     * 修改品类
     *
     * @param id 品类ID
     * @param param 修改参数
     */
    void update(Long id, ProductCategoryModifyParam param);

    /**
     * 删除品类
     *
     * @param id 品类ID
     */
    void delete(Long id);

    /**
     * 查询品类详情及从根节点到当前节点的完整路径
     *
     * @param id 品类ID
     * @param includeChildren 是否包含当前节点的子节点，默认false
     * @return 包含路径和详情的对象
     */
    DictItemDetailWithPathVO getDetailWithPath(Long id, Boolean includeChildren);
}
