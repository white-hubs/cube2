package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PratiqueRegisterVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Repository("meattrade-pratiqueRegisterMapper")
public interface PratiqueRegisterMapper extends BaseMapper<PratiqueRegister> {

    List<PratiqueRegister> getFailedPratoqueRegister(@Param("porkReportIds") Collection<? extends Serializable> porkReportIds,
                                                     @Param("tenant") Integer tenant
                                                    );

    Page<PratiqueRegister> pageFailedPratoqueRegister(Page<?> page,
                                                      @Param("porkReportIds") Collection<? extends Serializable> porkReportIds,
                                                      @Param("tenant") Integer tenant
                                                     );

    Page<PratiqueRegister> pagePratiqueRegister(Page<?> page, @Param("param") PratiqueRegisterQueryParam queryParam);

    Page<PratiqueRegisterVO> selectPratiqueRegisterPage(Page<?> page, @Param("param") PratiqueRegisterQueryParam param);
}