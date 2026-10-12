package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultTypeCollectionQuery;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/3
 */
public interface FaultTypeQueryService extends IService<FaultType> {

    /**
     * 获取故障单类型列表
     *
     * @param query 查询参数
     *
     * @return 故障单类型信息
     */
    List<FaultType> listFaultType(FaultTypeCollectionQuery query);

}
