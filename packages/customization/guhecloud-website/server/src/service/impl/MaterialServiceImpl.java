package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.MaterialConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.MaterialMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.Material;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.MaterialService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.MaterialVO;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 素材服务实现
 */
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialMapper materialMapper;
    private final FileClient fileClient;

    /**
     * 新增素材
     */
    @Override
    public void add(List<MaterialParam>  param) {
        List<Long> ids = param.stream().map(MaterialParam::getFileId).toList();
        List<FileVO> files = fileClient.getByIds(ids);
        List<Material> materials = files.stream().map(f -> {
            Material material = new Material();
            material.setPathId(param.get(0).getPathId());
            material.setName(f.getName());
            material.setExt(f.getSuffix());
            material.setSize(f.getSize());
            material.setFileId(f.getId());
            material.setMimeType(f.getType());
            return material;
        }).toList();
        materialMapper.insert(materials);
    }

    /**
     * 删除素材
     */
    @Override
    public void delete(Long id) {
        materialMapper.deleteById(id);
    }

    /**
     * 批量删除素材
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        materialMapper.deleteBatchIds(ids);
    }

    /**
     * 查询素材详情
     */
    @Override
    public MaterialVO detail(Long id) {
        Material entity = materialMapper.selectById(id);
        return MaterialConverts.toVO(entity);
    }

    /**
     * 查询素材
     */
    @Override
    public List<MaterialVO> list(MaterialQueryParam param) {
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Material::getPathId, param.getPathId())
                .like(null != param.getName(), Material::getName, param.getName())
                .eq(null != param.getExt(), Material::getExt, param.getExt())
                .orderByDesc(Material::getCreateTime);
        List<Material> list = materialMapper.selectList(wrapper);
        List<MaterialVO> vos = MaterialConverts.toVOList(list);
        List<Long> fileIds = vos.stream().map(MaterialVO::getFileId).toList();
        List<FileVO> files = fileClient.getByIds(fileIds);
        Map<Long, FileVO> idFileIdMap = files.stream().collect(Collectors.toMap(FileVO::getId, f -> f));
        vos.forEach(vo -> {
            FileVO fileVO = idFileIdMap.get(vo.getFileId());
            if(null != fileVO) {
                vo.setUrl(fileVO.getPath());
            }
        });
        return vos;
    }

    /**
     * 重命名素材
     */
    @Override
    public void rename(Long id, String name) {
        Material entity = materialMapper.selectById(id);
        if (entity == null) {
            throw new ServiceException("素材不存在，id=" + id);
        }
        entity.setName(name);
        materialMapper.updateById(entity);
    }

}
