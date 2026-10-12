package com.guhecloud.cube.deliver.project.merchant.service.impl.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.dto.system.ResourceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.system.ResourceSortingDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.system.Resource;
import com.guhecloud.cube.deliver.project.merchant.entity.system.ResourcePermission;
import com.guhecloud.cube.deliver.project.merchant.mapper.system.ResourceMapper;
import com.guhecloud.cube.deliver.project.merchant.service.ResourcePermissionService;
import com.guhecloud.cube.deliver.project.merchant.service.system.ResourceService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;
    private final ResourcePermissionService resourcePermissionService;

    @Override
    public void save(ResourceDTO resourceDTO) {
        Resource parent = null;
        if (StringUtils.isNotEmpty(resourceDTO.getPid())) {
            parent = resourceMapper.selectById(resourceDTO.getPid());
            if (parent == null) {
                throw new ServiceException("父级不存在");
            }
        }
        if (resourceMapper.selectOne(Wrappers.<Resource>lambdaQuery().eq(Resource::getGroup, resourceDTO.getGroup())
                                             .eq(Resource::getCode, resourceDTO.getCode())) != null) {
            throw new ServiceException("编码已经存在");
        }
        if (resourceMapper.selectOne(Wrappers.<Resource>lambdaQuery().eq(Resource::getPid, resourceDTO.getPid())
                                             .eq(Resource::getName, resourceDTO.getName())) != null) {
            throw new ServiceException("名称已经存在");
        }
        Resource resource = resourceDTO.convert();
        if (parent != null && StringUtils.isNotEmpty(parent.getGroup())) {
            resource.setGroup(parent.getGroup());
        }
        resourceMapper.insert(resource);
    }

    @Override
    public void update(ResourceDTO resourceDTO) {
        Resource parent = null;
        if (StringUtils.isNotEmpty(resourceDTO.getPid())) {
            parent = resourceMapper.selectById(resourceDTO.getPid());
            if (parent == null) {
                throw new ServiceException("父级不存在");
            }
        }
        Resource saved = resourceMapper.selectOne(Wrappers.<Resource>lambdaQuery().eq(Resource::getId, resourceDTO.getId()));
        if (saved == null) {
            throw new ServiceException("资源不存在");
        }
        Resource savedCode = resourceMapper.selectOne(Wrappers.<Resource>lambdaQuery().eq(Resource::getGroup, resourceDTO.getGroup())
                                                              .eq(Resource::getCode, resourceDTO.getCode()));
        if (savedCode != null && !savedCode.getId().equals(saved.getId())) {
            throw new ServiceException("编码已经存在");
        }
        Resource savedName = resourceMapper.selectOne(Wrappers.<Resource>lambdaQuery().eq(Resource::getPid, resourceDTO.getPid())
                                                              .eq(Resource::getName, resourceDTO.getName()));
        if (savedName != null && !savedName.getId().equals(saved.getId())) {
            throw new ServiceException("名称已经存在");
        }
        if (StringUtils.isEmpty(resourceDTO.getIcon())) {
            resourceDTO.setIcon("");
        }
        Resource resource = resourceDTO.convert();
        if (parent != null && StringUtils.isNotEmpty(parent.getGroup())) {
            resource.setGroup(parent.getGroup());
        }
        resourceMapper.updateById(resource);
    }

    @Override
    public void remove(String id) {
        if (resourceMapper.selectList(Wrappers.<Resource>lambdaQuery().eq(Resource::getPid, id)).size() > 0) {
            throw new ServiceException("存在子级，不能删除");
        }
        // 删除该资源相关的权限
        resourcePermissionService.remove(Wrappers.<ResourcePermission>lambdaQuery().eq(ResourcePermission::getResourceId, id));
        // 删除资源
        resourceMapper.deleteById(id);
    }

    @Override
    public void sort(ResourceSortingDTO resourceSortingDTO) {
        List<Resource> list = new ArrayList<>();
        String pid = null;
        for (String id : resourceSortingDTO.getIds()) {
            Resource resource = resourceMapper.selectById(id);
            if (resource != null) {
                if (pid == null) {
                    pid = resource.getPid();
                } else if (!pid.equals(resource.getPid())) {
                    throw new ServiceException("只能进行同级排序");
                }
                list.add(resource);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            Resource resource = list.get(i);
            resource.setSort(i);
            resourceMapper.updateById(resource);
        }
    }

}
