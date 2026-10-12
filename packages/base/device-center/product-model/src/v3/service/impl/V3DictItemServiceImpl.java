package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.iot.product.model.v3.constant.ProductModel;
import com.guhecloud.cube.base.iot.product.model.v3.enums.DictEnum;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3DictItemMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3DictMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDict;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDictItem;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3DictItemService;
import com.guhecloud.cube.base.iot.product.model.v3.util.IdCodec;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemDetailWithPathVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemTreeVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.guhecloud.cube.base.iot.product.model.v3.constant.ProductModel.CATEGORY_CODE_SPLIT;
import static com.guhecloud.cube.base.iot.product.model.v3.constant.ProductModel.CATEGORY_NAME_SPLIT;

/**
 * 字典项服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3DictItemServiceImpl extends ServiceImpl<V3DictItemMapper, SysDictItem> implements V3DictItemService {

    private final V3DictItemMapper sysDictItemMapper;
    private final V3DictMapper sysDictMapper;

    @Override
    public Page<DictItemVO> page(DictItemPageParam param) {
        log.info("分页查询字典项, param={}", param);

        if (param.getDictId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dictId不能为空");
        }

        // 构建查询条件
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictItem::getDictId, param.getDictId());
        wrapper.eq(SysDictItem::getItemParentId, 0L);

        // 可选: 按父级ID过滤
        if (param.getParentId() != null) {
            wrapper.eq(SysDictItem::getItemParentId, param.getParentId());
        }

        // 可选: 按标签模糊查询
        if (StringUtils.hasText(param.getItemLabel())) {
            wrapper.like(SysDictItem::getItemLabel, param.getItemLabel());
        }

        // 可选: 按状态过滤
        if (param.getStatus() != null) {
            wrapper.eq(SysDictItem::getStatus, param.getStatus());
        }

        // 排序
        wrapper.orderByAsc(SysDictItem::getItemSort, SysDictItem::getId);

        // 分页查询
        Page<SysDictItem> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<SysDictItem> result = sysDictItemMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<DictItemVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<DictItemVO> voList = result.getRecords().stream()
                .map(V3DictItemService::toVO)
                .collect(Collectors.toList());

        // 可选: 加载子级
        if (Boolean.TRUE.equals(param.getWithChildren())) {
            for (DictItemVO vo : voList) {
                vo.setChildren(loadChildren(vo.getId()));
            }
        }

        voPage.setRecords(voList);
        log.info("分页查询字典项完成, total={}", result.getTotal());

        return voPage;
    }

    @Override
    public DictItemVO getById(Long id) {
        log.info("查询字典项详情, id={}", id);

        SysDictItem item = sysDictItemMapper.selectById(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "字典项不存在");
        }

        return V3DictItemService.toVO(item);
    }

    @Override
    public List<DictItemVO> listByIds(List<Long> ids) {
        log.info("批量查询字典项, ids={}", ids);

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        List<SysDictItem> items = sysDictItemMapper.selectBatchIds(ids);
        return items.stream()
                .map(V3DictItemService::toVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(DictItemModifyParam param) {
        log.info("新增字典项, param={}", param);

        SysDict dict = sysDictMapper.selectById(param.getDictId());
        if (dict == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "字典类型不存在");
        }

        checkParams(param);

        // 构建实体
        SysDictItem item = new SysDictItem();
        item.setDictId(param.getDictId());
        item.setItemCode(IdCodec.generateShortCode());  // 自动生成itemCode
        item.setItemParentId(param.getParentId() != null ? param.getParentId() : 0L);
        item.setItemSort(param.getSort() != null ? param.getSort() : 0);
        item.setItemLabel(param.getItemLabel());
        item.setItemValue(param.getItemValue());
        item.setStatus(param.getStatus() != null ? param.getStatus() : 0);
        item.setRemark(param.getRemark());

        // 计算级联字段
        item.setItemCode(item.getItemCode()); // itemCode已在上面生成
        item.setItemLevelCode(calculateLevelCode(item));
        item.setItemLevelLabel(calculateLevelLabel(item));

        // 保存
        sysDictItemMapper.insert(item);

        log.info("字典项新增成功, id={}, itemCode={}", item.getId(), item.getItemCode());
        return item.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, DictItemModifyParam param) {
        log.info("修改字典项, id={}, param={}", id, param);

        // 检查是否存在
        SysDictItem existing = sysDictItemMapper.selectById(id);
        if (existing == null) {
            throw new InvalidArgumentException("字典项不存在");
        }

        if (param.getDictId() != null) {
            SysDict dict = sysDictMapper.selectById(param.getDictId());
            if (dict == null) {
                throw new InvalidArgumentException( "字典类型不存在");
            }
        }

        checkParams(param);

        // 更新实体
        SysDictItem item = new SysDictItem();
        item.setId(id);
        item.setItemParentId(existing.getItemParentId());
        item.setItemSort(param.getSort() != null ? param.getSort() : existing.getItemSort());
        item.setItemLabel(param.getItemLabel());
        item.setItemValue(param.getItemValue());
        item.setStatus(param.getStatus() != null ? param.getStatus() : existing.getStatus());
        item.setRemark(param.getRemark());

        // 检查是否需要重新计算级联字段
        boolean needRecalculate = param.getParentId() != null && !param.getParentId().equals(existing.getItemParentId());

        // 父节点变化,需要重新计算
        if (param.getItemLabel() != null && !param.getItemLabel().equals(existing.getItemLabel())) {
            // 名称变化,需要重新计算label
            needRecalculate = true;
        }

        // 重新计算级联字段
        if (needRecalculate) {
            // 构建临时对象用于计算
            SysDictItem temp = new SysDictItem();
            temp.setId(id);
            temp.setItemParentId(param.getParentId() != null ? param.getParentId() : existing.getItemParentId());
            temp.setItemCode(existing.getItemCode());
            temp.setItemLabel(param.getItemLabel() != null ? param.getItemLabel() : existing.getItemLabel());

            item.setItemLevelCode(calculateLevelCode(temp));
            item.setItemLevelLabel(calculateLevelLabel(temp));
        }

        sysDictItemMapper.updateById(item);

        // 如果级联字段变化,需要更新所有子节点
        if (needRecalculate) {
            updateChildrenCascade(id);
        }

        log.info("字典项修改成功, id={}", id);
    }

    private void checkParams(DictItemModifyParam param) {
        if (!StringUtils.hasText(param.getItemLabel())) {
            throw new InvalidArgumentException( "字典项名称不能为空");
        }

        if (param.getItemLabel().contains(ProductModel.CATEGORY_NAME_SPLIT)) {
            throw new InvalidArgumentException( "字典项名称不能包含 \"" + ProductModel.CATEGORY_NAME_SPLIT+"\"");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("删除字典项, id={}", id);

        SysDictItem existing = sysDictItemMapper.selectById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "字典项不存在");
        }

        // 检查是否存在子级
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictItem::getItemParentId, id);
        Long childCount = sysDictItemMapper.selectCount(wrapper);

        if (childCount > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "存在子级字典项,无法删除");
        }

        // 软删除
        DatabaseContextHolder.softDelete(this, existing.getId());

        log.info("字典项删除成功, id={}", id);
    }

    @Override
    public List<DictItemTreeVO> listTree(Long dictId, Long dictItemId) {
        log.info("查询树形字典项, dictId={}, dictItemId={}", dictId, dictItemId);

        List<SysDictItem> items;

        if (dictItemId != null) {
            // 查询指定节点下的树
            items = loadSubTree(dictItemId);
        } else {
            // 查询整个字典的树
            LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysDictItem::getDictId, dictId);
            wrapper.orderByAsc(SysDictItem::getItemSort, SysDictItem::getId);
            items = sysDictItemMapper.selectList(wrapper);
        }

        // 构建树形结构
        List<DictItemTreeVO> tree = buildTree(items, dictItemId != null ? dictItemId : 0L);

        log.info("树形字典项查询完成, nodeCount={}", items.size());

        return tree;
    }

    @Override
    public List<DictItemVO> listAllByDictId(DictEnum dictEnum, Boolean ignoreTenant) {
        log.info("查询所有字典项列表, dictEnum={}, ignoreTenant={}", dictEnum, ignoreTenant);

        List<SysDictItem> items;

        // 根据 ignoreTenant 参数决定是否忽略租户过滤
        if (Boolean.TRUE.equals(ignoreTenant)) {
            // 使用无租户过滤的查询方法
            items = sysDictItemMapper.noTenantSelectByDictId(dictEnum.getId());
        } else {
            // 使用默认的租户过滤查询
            LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysDictItem::getDictId, dictEnum.getId());
            wrapper.orderByAsc(SysDictItem::getItemSort, SysDictItem::getId);
            items = sysDictItemMapper.selectList(wrapper);
        }

        return items.stream()
                .map(V3DictItemService::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public DictItemDetailWithPathVO getDetailWithPath(Long id, Boolean includeChildren) {
        log.info("查询字典项详情及路径, id={}, includeChildren={}", id, includeChildren);

        // 查询当前节点
        SysDictItem current = sysDictItemMapper.selectById(id);
        if (current == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "字典项不存在");
        }

        // 默认值处理
        boolean shouldIncludeChildren = includeChildren != null && includeChildren;

        // 构建从根节点到当前节点的路径树（根据参数决定是否包含当前节点的所有后代）
        DictItemVO pathTree = buildPathTree(current, shouldIncludeChildren);

        // 构建返回对象
        DictItemDetailWithPathVO result = new DictItemDetailWithPathVO();
        result.setPath(pathTree);
        result.setDetail(V3DictItemService.toVO(current));

        log.info("字典项详情及路径查询完成");
        return result;
    }

    /**
     * 构建从根节点到当前节点的路径树
     * 路径：根 -> 父 -> 当前 -> (可选)当前节点的所有后代
     * 不包含兄弟节点
     *
     * @param targetItem 目标节点（当前节点）
     * @param includeChildren 是否包含当前节点的子节点
     * @return 根节点（包含完整路径树）
     */
    private DictItemVO buildPathTree(SysDictItem targetItem, boolean includeChildren) {
        // 1. 向上查找，收集从根到当前节点的路径
        List<SysDictItem> pathList = new ArrayList<>();
        SysDictItem current = targetItem;
        while (current != null) {
            pathList.addFirst(current); // 添加到列表开头
            if (current.getItemParentId() == null || current.getItemParentId() == 0) {
                break; // 到达根节点
            }
            current = sysDictItemMapper.selectById(current.getItemParentId());
        }

        // 2. 从根节点开始构建树，每层只包含路径上的节点
        DictItemVO root = null;
        DictItemVO parent = null;

        for (int i = 0; i < pathList.size(); i++) {
            SysDictItem item = pathList.get(i);
            DictItemVO vo = V3DictItemService.toVO(item);

            if (i == 0) {
                // 根节点
                root = vo;
                parent = vo;
            } else {
                // 将当前节点作为父节点的唯一子节点
                List<DictItemVO> children = new ArrayList<>();
                children.add(vo);
                parent.setChildren(children);
                parent = vo;
            }

            // 如果是目标节点（最后一个节点）且需要包含子节点，加载其所有后代
            if (i == pathList.size() - 1 && includeChildren) {
                List<DictItemVO> descendants = loadChildren(item.getId());
                vo.setChildren(descendants);
            }
        }

        return root;
    }


    /**
     * 加载子级
     */
    private List<DictItemVO> loadChildren(Long parentId) {
        return buildTreeVo(loadSubTree(parentId), parentId);
    }

    /**
     * 加载子树(包括所有后代节点)
     */
    private List<SysDictItem> loadSubTree(Long parentId) {
        List<SysDictItem> result = new ArrayList<>();

        // 递归加载
        loadSubTreeRecursive(parentId, result);

        return result;
    }

    /**
     * 递归加载子树
     */
    private void loadSubTreeRecursive(Long parentId, List<SysDictItem> result) {
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictItem::getItemParentId, parentId);
        wrapper.orderByAsc(SysDictItem::getItemSort, SysDictItem::getId);

        List<SysDictItem> children = sysDictItemMapper.selectList(wrapper);
        result.addAll(children);

        for (SysDictItem child : children) {
            loadSubTreeRecursive(child.getId(), result);
        }
    }

    /**
     * 构建树形结构
     */
    private List<DictItemVO> buildTreeVo(List<SysDictItem> items, Long parentId) {
        // 转换为Map便于查找
        Map<Long, DictItemVO> map = items.stream()
                .map(V3DictItemService::toVO)
                .collect(Collectors.toMap(DictItemVO::getId, vo -> vo));

        // 构建树形结构
        List<DictItemVO> roots = new ArrayList<>();
        for (DictItemVO vo : map.values()) {
            if (vo.getItemParentId() == null || vo.getItemParentId().equals(parentId)) {
                roots.add(vo);
            } else {
                DictItemVO parent = map.get(vo.getItemParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(vo);
                }
            }
        }

        return roots;
    }

    /**
     * 构建树形结构
     */
    private List<DictItemTreeVO> buildTree(List<SysDictItem> items, Long parentId) {
        // 转换为Map便于查找
        Map<Long, DictItemTreeVO> map = items.stream()
                .map(this::toTreeVO)
                .collect(Collectors.toMap(DictItemTreeVO::getId, vo -> vo));

        // 构建树形结构
        List<DictItemTreeVO> roots = new ArrayList<>();
        for (DictItemTreeVO vo : map.values()) {
            if (vo.getItemParentId() == null || vo.getItemParentId().equals(parentId)) {
                roots.add(vo);
            } else {
                DictItemTreeVO parent = map.get(vo.getItemParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(vo);
                }
            }
        }

        return roots;
    }

    /**
     * 转换为TreeVO
     */
    private DictItemTreeVO toTreeVO(SysDictItem item) {
        DictItemTreeVO vo = new DictItemTreeVO();
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
        return vo;
    }

    /**
     * 计算字典项的级联code
     * <p>格式: root.parent.child, 用于快速判断层级关系</p>
     */
    private String calculateLevelCode(SysDictItem item) {
        if (item.getItemParentId() == null || item.getItemParentId() == 0) {
            return item.getItemCode();
        }

        SysDictItem parent = sysDictItemMapper.selectById(item.getItemParentId());
        if (parent == null) {
            return item.getItemCode();
        }

        String parentLevelCode = parent.getItemLevelCode();
        if (!StringUtils.hasText(parentLevelCode)) {
            parentLevelCode = calculateLevelCode(parent);
        }

        return parentLevelCode + CATEGORY_CODE_SPLIT + item.getItemCode();
    }

    /**
     * 计算字典项的级联label
     * <p>格式: 根节点/父节点/子节点, 用于前端面包屑导航展示</p>
     */
    private String calculateLevelLabel(SysDictItem item) {
        if (item.getItemParentId() == null || item.getItemParentId() == 0) {
            return item.getItemLabel();
        }

        SysDictItem parent = sysDictItemMapper.selectById(item.getItemParentId());
        if (parent == null) {
            return item.getItemLabel();
        }

        String parentLevelLabel = parent.getItemLevelLabel();
        if (!StringUtils.hasText(parentLevelLabel)) {
            parentLevelLabel = calculateLevelLabel(parent);
        }

        return parentLevelLabel + CATEGORY_NAME_SPLIT + item.getItemLabel();
    }

    /**
     * 级联更新子节点的级联字段
     * <p>当父节点的 code 或 label 变更时调用</p>
     */
    private void updateChildrenCascade(Long parentId) {
        // 查询所有直接子节点
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictItem::getItemParentId, parentId);
        List<SysDictItem> children = sysDictItemMapper.selectList(wrapper);

        for (SysDictItem child : children) {
            // 重新计算级联字段
            child.setItemLevelCode(calculateLevelCode(child));
            child.setItemLevelLabel(calculateLevelLabel(child));

            // 更新数据库
            sysDictItemMapper.updateById(child);

            // 递归更新子节点的子节点
            updateChildrenCascade(child.getId());
        }
    }
}
