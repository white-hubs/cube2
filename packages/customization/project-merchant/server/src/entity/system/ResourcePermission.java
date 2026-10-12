package com.guhecloud.cube.deliver.project.merchant.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "resource_permission")
public class ResourcePermission {

	private String objectType;
	
    private String objectId;
    
    private String resourceId;

}
