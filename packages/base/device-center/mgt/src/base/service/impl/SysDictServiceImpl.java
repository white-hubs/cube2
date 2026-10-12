package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.base.mapper.SysDictMapper;
import com.guhecloud.cube.iot.mgt.base.model.SysDict;
import com.guhecloud.cube.iot.mgt.base.service.SysDictService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Override
    public List<SysDict> queryAllByTenantId(Long id) {
        return getBaseMapper().queryAllByTenantId(id);
    }
}
