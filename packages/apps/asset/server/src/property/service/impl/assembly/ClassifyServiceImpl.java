package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.assembly;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.property.management.client.property.param.property.AssemblyClassifyPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.AssemblyClassifyParam;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyPageVO;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyTreeVO;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.ConsumableStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.assembly.AssemblyClassifyMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly.AssemblyClassify;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.assembly.AssemblyClassifyService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 通用分类;(assembly_classify)表服务实现类
 *
 * @author :lijie
 * @date : 2023-3-2
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassifyServiceImpl extends ServiceImpl<AssemblyClassifyMapper, AssemblyClassify> implements AssemblyClassifyService {
    private final AssemblyClassifyMapper assemblyClassifyMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    public AssemblyClassifyVO queryById(Long id) {
        AssemblyClassify assemblyClassify = assemblyClassifyMapper.selectById(id);
        return ConvertUtils.convert(AssemblyClassifyVO.class, assemblyClassify);
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页结果
     */
    public Pagination<AssemblyClassifyPageVO> getAssemblyClassifyPage(AssemblyClassifyPageParam param) {
        Page<AssemblyClassify> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<AssemblyClassify> pageData = assemblyClassifyMapper.selectPage(page,
                                                                            new LambdaQueryWrapper<AssemblyClassify>()
                                                                                    .eq(AssemblyClassify::getType, param.getType())
                                                                                    .eq(AssemblyClassify::getStatus, ConsumableStatusEnum.NO_DELETE.name())
                                                                                    .like(StringUtils.isNotEmpty(param.getName()), AssemblyClassify::getName, param.getName())
                                                                                    .orderByDesc(AssemblyClassify::getCreateTime));
        List<AssemblyClassifyPageVO> voList = ConvertUtils.toList(AssemblyClassifyPageVO.class, pageData.getRecords());
        return new Pagination<>(pageData.getTotal(), pageData.getPages(), voList);
    }

    /**
     * 查询树结构
     *
     * @param name 名称
     * @param type 类型
     *
     * @return 树
     */
    public List<AssemblyClassifyTreeVO> getAssemblyClassifyTree(String name, String type) {
        return getAssemblyClassifyTree(name, type, 0);
    }

    public List<AssemblyClassifyTreeVO> getAssemblyClassifyTree(String name, String type, Integer parentId) {
        List<AssemblyClassify> data = assemblyClassifyMapper.selectList(new LambdaQueryWrapper<AssemblyClassify>()
                                                                                .eq(AssemblyClassify::getType, type)
                                                                                .eq(parentId !=
                                                                                    null, AssemblyClassify::getParentId, parentId)
                                                                                .eq(AssemblyClassify::getStatus, ConsumableStatusEnum.NO_DELETE.name())
                                                                                .eq(StringUtils.isNotBlank(name), AssemblyClassify::getName, name)
                                                                                .orderByDesc(AssemblyClassify::getCreateTime));
        List<AssemblyClassifyTreeVO> treeData = ConvertUtils.toList(AssemblyClassifyTreeVO.class, data);
        treeData.forEach(ex -> {
            this.getChildren(ex, type);
        });
        return treeData;
    }

    public List<AssemblyClassify> getAssemblyClassify(String name, String type, Long classifyId) {
        List<AssemblyClassify> vos = assemblyClassifyMapper.selectList(new LambdaQueryWrapper<AssemblyClassify>()
                                                                               .eq(AssemblyClassify::getType, type)
                                                                               .eq(AssemblyClassify::getStatus, ConsumableStatusEnum.NO_DELETE.name())
                                                                               .eq(StringUtils.isNotBlank(name), AssemblyClassify::getName, name)
                                                                               .eq(classifyId != null, AssemblyClassify::getId, classifyId)
                                                                               .orderByDesc(AssemblyClassify::getCreateTime));
        vos.addAll(getSubAssemblyClassify(vos));
        return vos;
    }

    private List<AssemblyClassify> getSubAssemblyClassify(List<AssemblyClassify> vos) {
        List<AssemblyClassify> classifies = List.of();
        if (ObjectUtils.isNotEmpty(vos)) {
            vos.forEach(vo -> {
                List<AssemblyClassify> classifyList = assemblyClassifyMapper.selectList(new LambdaQueryWrapper<AssemblyClassify>()
                                                                                                .eq(AssemblyClassify::getParentId, vo.getId())
                                                                                                .eq(AssemblyClassify::getStatus, ConsumableStatusEnum.NO_DELETE.name())
                                                                                                .orderByDesc(AssemblyClassify::getCreateTime));
                if (ObjectUtils.isNotEmpty(classifyList)) {
                    classifies.addAll(classifyList);
                    classifies.addAll(getSubAssemblyClassify(classifyList));
                }
            });
        }
        return classifies;
    }

    @Override
    public AssemblyClassifyTreeVO getAssemblyClassifyTree(Long id) {
        AssemblyClassify tree = assemblyClassifyMapper.selectById(id);
        AssemblyClassifyTreeVO treeData = new AssemblyClassifyTreeVO();
        if (ObjectUtils.isNotEmpty(tree)) {
            treeData = ConvertUtils.convert(AssemblyClassifyTreeVO.class, tree);
            if (treeData.getParentId() > 0) {
                return this.getParent(treeData.getParentId(), treeData);
            }
        }
        return treeData;
    }

    /**
     * 新增数据
     *
     * @param param 实例对象
     *
     * @return 新增结果
     */
    public Boolean insert(AssemblyClassifyParam param) {
        AssemblyClassify classify = assemblyClassifyMapper.selectOne(new LambdaQueryWrapper<AssemblyClassify>()
                                                                             .eq(AssemblyClassify::getName, param.getName())
                                                                             .eq(AssemblyClassify::getStatus, ConsumableStatusEnum.NO_DELETE.name())
                                                                             .eq(AssemblyClassify::getType, param.getType()));
        Assert.isConflict(ObjectUtils.isNotEmpty(classify), "名称已存在");
        AssemblyClassify assemblyClassify = ConvertUtils.convert(AssemblyClassify.class, param);
        assemblyClassify.setParentId(Optional.ofNullable(param.getParentId()).orElse(0L));
        assemblyClassify.setStatus(ConsumableStatusEnum.NO_DELETE.name());
        return assemblyClassifyMapper.insert(assemblyClassify) > 0;
    }

    /**
     * 更新数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    public Boolean update(AssemblyClassifyParam param) {
        AssemblyClassify assemblyClassify = baseMapper.selectById(param.getId());
        AssemblyClassify classify = assemblyClassifyMapper.selectOne(new LambdaQueryWrapper<AssemblyClassify>()
                                                                             .eq(AssemblyClassify::getName, param.getName())
                                                                             .eq(AssemblyClassify::getStatus, ConsumableStatusEnum.NO_DELETE.name())
                                                                             .eq(AssemblyClassify::getType, param.getType())
                                                                             .last("limit 1"));
        Assert.isConflict(ObjectUtils.isNotEmpty(classify) && !classify.getId().equals(param.getId()), "名称已存在");
        assemblyClassify.setName(param.getName());
        assemblyClassify.setRemarks(param.getRemarks());
        assemblyClassify.setType(param.getType());
        return assemblyClassifyMapper.updateById(assemblyClassify) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    public Boolean deleteById(Long id) {
        Boolean bool = this.update().set("status", ConsumableStatusEnum.DELETE.name())
                .eq("id", id).update();
        if (bool) {
            List<AssemblyClassify> classifies = this.baseMapper.selectList(new LambdaQueryWrapper<AssemblyClassify>()
                                                                                   .eq(AssemblyClassify::getParentId, id));
            classifies.forEach(ex -> {
                ex.setStatus(ConsumableStatusEnum.DELETE.name());
                this.updateById(ex);
                List<AssemblyClassify> classifiesList = this.baseMapper.selectList(new LambdaQueryWrapper<AssemblyClassify>()
                                                                                           .eq(AssemblyClassify::getParentId, ex.getId()));
                classifiesList.forEach(exList -> {
                    exList.setStatus(ConsumableStatusEnum.DELETE.name());
                    this.updateById(exList);
                });
            });
        }
        return bool;
    }

    private void getChildren(AssemblyClassifyTreeVO tree, String type) {
        List<AssemblyClassify> data = assemblyClassifyMapper.selectList(new LambdaQueryWrapper<AssemblyClassify>()
                                                                                .eq(AssemblyClassify::getType, type)
                                                                                .eq(AssemblyClassify::getStatus, ConsumableStatusEnum.NO_DELETE.name())
                                                                                .eq(AssemblyClassify::getParentId, tree.getId())
                                                                                .orderByDesc(AssemblyClassify::getCreateTime));
        if (ObjectUtils.isNotEmpty(data)) {
            List<AssemblyClassifyTreeVO> treeData = ConvertUtils.toList(AssemblyClassifyTreeVO.class, data);
            treeData.forEach(ex -> {
                this.getChildren(ex, type);
            });
            tree.setChildren(treeData);
        }
    }

    private AssemblyClassifyTreeVO getParent(Long id, AssemblyClassifyTreeVO tree) {
        AssemblyClassify data = assemblyClassifyMapper.selectById(id);
        AssemblyClassifyTreeVO treeData = new AssemblyClassifyTreeVO();
        if (ObjectUtils.isNotEmpty(data)) {
            treeData = ConvertUtils.convert(AssemblyClassifyTreeVO.class, data);
            treeData.setChildren(Collections.singletonList(tree));
            if (treeData.getParentId() > 0) {
                tree.setChildren(Collections.singletonList(this.getParent(treeData.getParentId(), treeData)));
            }
            return treeData;
        }
        return tree;
    }
}
