package com.guhecloud.cube.deliver.project.merchant.service.impl.system;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.system.ResourcePermission;
import com.guhecloud.cube.deliver.project.merchant.mapper.system.ResourcePermissionMapper;
import com.guhecloud.cube.deliver.project.merchant.service.ResourcePermissionService;
import org.springframework.stereotype.Service;

@Service
public class ResourcePermissionServiceImpl extends ServiceImpl<ResourcePermissionMapper, ResourcePermission> implements ResourcePermissionService {
}
