package com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.common.vo.RiskSourceTypeVO;
import com.guhecloud.cube.property.management.client.property.param.property.RiskSourceDeviceQueryParam;
import com.guhecloud.cube.property.management.client.property.vo.property.RiskSourceDeviceVO;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceTypeNumVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedger;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  17:54
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface RiskSourceLedgerMapper extends BaseMapper<RiskSourceLedger> {
    List<RiskSourceTypeVO> getAllGroupTypeCode(@Param("types") List<String> types);

    List<RiskSourceDeviceVO> selectRiskSourceDeviceNumByCode(@Param("param") RiskSourceDeviceQueryParam param);

    Page<RiskSourceLedgerVO> selectAllByPage(@Param("page") Page page, @Param("param") RiskSourceLedgerPageParam param);

    List<RiskSourceTypeNumVO> selectRiskSourceTypeNumLimitLocation(@Param("typeCodeList") List<String> typeCodeList);

    List<RiskSourceLedgerVO> selectRiskSourceLedgerWithLocation(@Param("typeNumMap") Map<String, Integer> typeNumMap);

    @InterceptorIgnore(tenantLine = "on")
    List<RiskSourceLedger> getAll(@Param("codes") List<String> codes, @Param("period") Period period);
}
