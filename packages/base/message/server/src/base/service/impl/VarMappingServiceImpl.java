package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.VarMappingMapper;
import com.guhecloud.cube.base.message2.server.base.entity.VarMapping;
import com.guhecloud.cube.base.message2.server.base.service.VarMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VarMappingServiceImpl extends ServiceImpl<VarMappingMapper, VarMapping> implements VarMappingService {

    @Override
    public Optional<VarMapping> getVarMapping() {
        List<VarMapping> varMappings = this.baseMapper.selectList(null);
        if (CollectionUtils.isEmpty(varMappings)) {
            return Optional.empty();
        }
        return Optional.of(varMappings.get(0));
    }
}
