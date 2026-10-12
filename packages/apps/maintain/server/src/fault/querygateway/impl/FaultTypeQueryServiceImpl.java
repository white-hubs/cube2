package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultTypeCollectionQuery;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/3
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultTypeQueryServiceImpl extends ServiceImpl<FaultTypeMapper, FaultType> implements FaultTypeQueryService {

    @Override
    public List<FaultType> listFaultType(@NonNull FaultTypeCollectionQuery query) {
        return this.lambdaQuery()
                .eq(FaultType::getApplicationCode, query.getApplicationCode())
                .like(StringUtils.isNotEmpty(query.getKeyword()), FaultType::getName, query.getKeyword())
                .list();
    }
}
