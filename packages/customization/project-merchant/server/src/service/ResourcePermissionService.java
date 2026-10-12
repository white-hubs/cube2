package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.system.ResourcePermission;

public interface ResourcePermissionService extends IService<ResourcePermission> {
	
	public static final String OBJECT_TYPE_ROLE = "ROLE";
	public static final String OBJECT_TYPE_USER = "USER";
	public static final String OBJECT_TYPE_PROJECT = "PROJECT";
	public static final String OBJECT_TYPE_STORE = "PROJECT";

}
