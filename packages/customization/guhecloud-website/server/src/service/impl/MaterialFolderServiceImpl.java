package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.MaterialFolderConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.MaterialFolderMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.MaterialFolder;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialFolderBatchUpdateParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialFolderParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.MaterialFolderService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.MaterialFolderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 素材文件夹服务实现
 */
@Service
@RequiredArgsConstructor
public class MaterialFolderServiceImpl implements MaterialFolderService {

    private final MaterialFolderMapper materialFolderMapper;

    /**
     * 新增文件夹
     */
    @Override
    public void add(MaterialFolderParam param) {
        MaterialFolder entity = MaterialFolderConverts.toEntity(param);
        materialFolderMapper.insert(entity);
    }

    /**
     * 重命名文件夹名称
     */
    @Override
    public void update(MaterialFolderParam param) {
        MaterialFolder entity = materialFolderMapper.selectById(param.getId());
        entity.setName(param.getName());
        materialFolderMapper.updateById(entity);
    }

    /**
     * 删除文件夹
     */
    @Override
    public void delete(Long id) {
        materialFolderMapper.deleteById(id);
    }

    /**
     * 批量删除文件夹
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        materialFolderMapper.deleteBatchIds(ids);
    }

    /**
     * 批量修改文件夹
     */
    @Override
    public void updateBatch(MaterialFolderBatchUpdateParam param) {
        if (param.getIds() == null || param.getIds().isEmpty()) {
            return;
        }
        MaterialFolder update = new MaterialFolder();
        update.setPathId(param.getPathId());
        update.setName(param.getName());
        update.setSort(param.getSort());
        materialFolderMapper.update(update,
                new LambdaQueryWrapper<MaterialFolder>()
                        .in(MaterialFolder::getId, param.getIds()));
    }

    /**
     * 查询文件夹详情
     */
    @Override
    public MaterialFolderVO detail(Long id) {
        MaterialFolder entity = materialFolderMapper.selectById(id);
        return MaterialFolderConverts.toVO(entity);
    }

    /**
     * 查询文件夹
     */
    @Override
    public List<MaterialFolderVO> list(Long pathId) {
        if(pathId == null){
            pathId = 0L;
        }
        List<MaterialFolder> all = materialFolderMapper.selectList(
                new LambdaQueryWrapper<MaterialFolder>()
                        .eq(MaterialFolder::getPathId, pathId)
                        .orderByAsc(MaterialFolder::getSort)
                        .orderByDesc(MaterialFolder::getCreateTime)
        );
        List<MaterialFolderVO> voList = MaterialFolderConverts.toVOList(all);
        return voList;
    }


}
