package com.guhecloud.cube.iot.mgt.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.base.model.SysDict;

import java.util.List;

public interface SysDictService extends IService<SysDict>{



    List<SysDict> queryAllByTenantId(Long id);
}
