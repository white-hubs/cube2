package com.guhecloud.cube.base.dictionary.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.server.dao.CascadeTreeMapper;
import com.guhecloud.cube.base.dictionary.server.entity.CascadeTree;
import com.guhecloud.cube.base.dictionary.server.param.CascadeTreeParam;
import com.guhecloud.cube.base.dictionary.server.vo.CascadeTreeVO;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CascadeTreeServiceImpl extends ServiceImpl<CascadeTreeMapper, CascadeTree> {
    private static final Integer LEVEL = 1;

    public CascadeTreeVO getTreeByDictionaryId(Long dictionaryId) {
        CascadeTree cascadeTrees = baseMapper.selectOne(new LambdaQueryWrapper<CascadeTree>()
                                                                .eq(CascadeTree::getDictionaryId, dictionaryId));
        return ConvertUtils.convert(CascadeTreeVO.class, cascadeTrees);
    }

    public void addTree(CascadeTreeParam param) {
        CascadeTree tree = ConvertUtils.convert(CascadeTree.class, param);
        if (!LEVEL.equals(param.getLevel()) && (ObjectUtils.isNotEmpty(param.getSuperiorsIds()))) {
            CascadeTree treeSup = baseMapper.selectOne(new LambdaQueryWrapper<CascadeTree>()
                                                               .eq(CascadeTree::getDictionaryId, param.getSuperiorsIds()));
            treeSup.setNextIds(ObjectUtils.isNotEmpty(treeSup.getNextIds()) ? treeSup.getNextIds() + "," + tree.getDictionaryId()
                                                                            : tree.getDictionaryId().toString());
            baseMapper.updateById(treeSup);
            baseMapper.updateNextIds(treeSup.getDictionaryId(), tree.getDictionaryId().toString());
            StringBuilder buffer = new StringBuilder();
            if (ObjectUtils.isNotEmpty(treeSup.getSuperiorsIds())) {
                buffer.append(treeSup.getSuperiorsIds());
                buffer.append(",");
            }
            buffer.append(param.getSuperiorsIds());
            tree.setSuperiorsIds(buffer.toString());
        }
        this.baseMapper.insert(tree);
    }

    public void updateTree(CascadeTreeParam param) {
        CascadeTree cascadeTree = baseMapper.selectOne(new LambdaQueryWrapper<CascadeTree>()
                                                               .eq(CascadeTree::getDictionaryId, param.getDictionaryId()));
        if (cascadeTree.getLevel().equals(param.getLevel())) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        if (ObjectUtils.isNotEmpty(param.getSuperiorsIds())) {
            CascadeTree treeSup = baseMapper.selectOne(new LambdaQueryWrapper<CascadeTree>()
                                                               .eq(CascadeTree::getDictionaryId, param.getSuperiorsIds()));
            buffer.append(treeSup.getSuperiorsIds());
            buffer.append(",");
        }
        buffer.append(param.getSuperiorsIds());
        cascadeTree.setSuperiorsIds(buffer.toString());
        buffer.append(",");
        buffer.append(param.getSuperiorsIds());
        List<CascadeTree> cascadeTreesNextList = baseMapper.selectList(new LambdaQueryWrapper<CascadeTree>()
                                                                               .like(CascadeTree::getSuperiorsIds, param.getDictionaryId()));
        cascadeTreesNextList.forEach(ex -> ex.setSuperiorsIds(buffer.toString()));
        this.baseMapper.updateById(cascadeTree);
    }
}
