package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCaseVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultCase;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultCaseCollectionQuery;

import java.util.List;
import java.util.Map;

/**
 * @author lzy
 * @date 2022/5/6
 */
public interface FaultCaseQueryService extends IService<FaultCase> {

    /**
     * 故障案例分页信息
     *
     * @param query 查询参数
     *
     * @return 故障案例信息
     */
    Page<FaultCaseVO> pageFaultCase(FaultCaseCollectionQuery query);

    /**
     * 故障案例列表信息
     *
     * @param query 查询参数
     *
     * @return 故障案例信息
     */
    List<FaultCase> listFaultCase(FaultCaseCollectionQuery query);

    /**
     * @Description案例故障频次近十二月趋势
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    Map<String, Object> getFaultTrend(Long id);

    /**
     * @Author wql
     * @Description 故障案例总数统计
     * @Date 17:48 2023/11/10
     **/
    Long countCase();

}
