package com.guhecloud.cube.deliver.project.merchant.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.system.ResourceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.system.ResourceSortingDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.system.Resource;

public interface ResourceService extends IService<Resource> {
	
	public static final String TYPE_MENU = "MENU";
	public static final String TYPE_FUNCTION = "FUNCTION";
	public static final String TYPE_SHORTCUT = "SHORTCUT";	//TODO 菜单类型：快捷方式。用于菜单下的功能（如： 诉求新增）。
															// 使用该功能需要前端重新新开发（诉求新增）页面。然后把路由配置到快捷方式中

	public static final String GROUP_WEB_HQ = "web-hq";
	public static final String GROUP_WEB_SITE = "web-site";
	public static final String GROUP_WEB_APP = "web-app";
	public static final String GROUP_WEB_STORE = "web-store";

	public static final String CODE_MENU_XMPT = "XMPT";
	public static final String CODE_MENU_XTPT = "XTPT";
	public static final String CODE_MENU_GRSZ_HQ = "GRSZ-HQ";
	public static final String CODE_MENU_ZBCDGN_HQ = "ZBCDGN";
	public static final String CODE_MENU_ZBSBLX_HQ = "ZBSBLX";
	public static final String CODE_MENU_XMCD_HQ = "XMCD";
	public static final String CODE_MENU_ZBSJZD_HQ = "ZBSJZD";

	public static final String CODE_MENU_JKPT = "JKPT";
	public static final String CODE_MENU_YWPT = "YWPT";
	public static final String CODE_MENU_GRSZ_SITE = "GRSZ-SITE";

	void save(ResourceDTO resourceDTO);

	void update(ResourceDTO resourceDTO);

	void remove(String id);
	
	void sort(ResourceSortingDTO resourceSortingDTO);

}
