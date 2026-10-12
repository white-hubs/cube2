package com.guhecloud.cube.deliver.project.merchant.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "resouce")
public class Resource {

	private String id;

	private String pid;
	
	@TableField("`group`")
	private String group;

	private String type;

	private String code;

	private String name;

	private String icon;

	private String path;

	private String apis;

	private Integer sort;

	private Boolean enabled;
	private String identityType;

}
