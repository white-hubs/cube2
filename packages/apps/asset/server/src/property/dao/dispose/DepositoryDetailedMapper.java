package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.DepositoryDetailedPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.DepositoryDetailed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepositoryDetailedMapper extends BaseMapper<DepositoryDetailed> {
    DepositoryDetailedPageVO getDepositoryDetailed(@Param("id") Long id);

    Page<DepositoryDetailedPageVO> getDepositoryDetailedPage(@Param("page") Page page, @Param("param") DepositoryDetailedPageParam param);

    List<DepositoryDetailedExcelParam> getDepositoryDetailedExcel(@Param("id") String id);

}
