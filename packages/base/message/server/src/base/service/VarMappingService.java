package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.VarMapping;

import java.util.Optional;

public interface VarMappingService extends IService<VarMapping> {

    Optional<VarMapping> getVarMapping();
}
