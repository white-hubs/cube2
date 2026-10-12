package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.EmployeeBalanceDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.query.CollectionQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalancePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface HolidayBalanceMapper extends BaseMapper<HolidayBalancePO> {

    /**
     * 分页查询员工余额
     *
     * @param page 分页对象
     *
     * @return 分页结果
     */
    Page<EmployeeBalanceDTO> page(Page page, @Param("query") CollectionQuery collectionQuery, @Param("tenant") Integer tenant);

    /**
     * 列表查询
     *
     * @param collectionQuery 查询条件
     *
     * @return
     */
    List<EmployeeBalanceDTO> list(@Param("query") CollectionQuery collectionQuery, @Param("tenant") Integer tenant);

    List<EmployeeBalanceDTO> listByEmployee(@Param("employee") Set<Long> employee);
}
