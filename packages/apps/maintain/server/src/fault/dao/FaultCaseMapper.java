package com.guhecloud.cube.srv.gen.maintain.server.fault.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCaseVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultCase;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultCaseCollectionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lzy
 * @date 2022/5/6
 */
@Mapper
public interface FaultCaseMapper extends BaseMapper<FaultCase> {

    Page<FaultCaseVO> pageFaultCase(Page<?> page, @Param("param") FaultCaseCollectionQuery query);

}
