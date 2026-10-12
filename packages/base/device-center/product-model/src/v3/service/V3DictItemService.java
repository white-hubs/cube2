package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.iot.product.model.v3.enums.DictEnum;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDictItem;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemDetailWithPathVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemTreeVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典项服务接口
 * 提供通用的字典项CRUD能力
 */
public interface V3DictItemService {

    /**
     * 分页查询字典项
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Page<DictItemVO> page(DictItemPageParam param);

    /**
     * 根据ID查询字典项详情
     *
     * @param id 字典项ID
     * @return 字典项VO
     */
    DictItemVO getById(Long id);

    /**
     * 批量查询字典项(用于ID转Name)
     *
     * @param ids 字典项ID列表
     * @return 字典项列表
     */
    List<DictItemVO> listByIds(List<Long> ids);

    /**
     * 新增字典项
     *
     * @param param 新增参数
     */
    Long save(DictItemModifyParam param);

    /**
     * 修改字典项
     *
     * @param id 字典项ID
     * @param param 修改参数
     */
    void update(Long id, DictItemModifyParam param);

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     */
    void delete(Long id);

    /**
     * 查询树形字典项列表
     *
     * @param dictId 字典ID
     * @param dictItemId 指定节点ID(可选),为null时查询整棵树
     * @return 树形字典项列表
     */
    List<DictItemTreeVO> listTree(Long dictId, Long dictItemId);

    /**
     * 批量查询字典项(用于ID转Name)
     *
     * @param dictEnum 字典枚举
     * @param ignoreTenant 是否忽略租户过滤，默认为false
     * @return 字典项列表
     */
    List<DictItemVO> listAllByDictId(DictEnum dictEnum, Boolean ignoreTenant) ;

    /**
     * 查询字典项详情及从根节点到当前节点的完整路径
     *
     * @param id 字典项ID
     * @param includeChildren 是否包含当前节点的子节点，默认false
     * @return 包含路径和详情的对象
     */
    DictItemDetailWithPathVO getDetailWithPath(Long id, Boolean includeChildren);

    /**
     * 转换为VO
     */
    static DictItemVO toVO(SysDictItem item) {
        DictItemVO vo = new DictItemVO();
        vo.setId(item.getId());
        vo.setDictId(item.getDictId());
        vo.setItemCode(item.getItemCode());
        vo.setItemParentId(item.getItemParentId());
        vo.setItemSort(item.getItemSort());
        vo.setItemLabel(item.getItemLabel());
        vo.setItemValue(item.getItemValue());
        vo.setStatus(item.getStatus());
        vo.setRemark(item.getRemark());
        vo.setCreateTime(item.getCreateTime());
        vo.setUpdateTime(item.getUpdateTime());
        return vo;
    }
}
