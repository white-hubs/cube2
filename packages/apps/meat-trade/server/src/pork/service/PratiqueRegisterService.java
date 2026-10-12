package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PratiqueRegisterVO;
import com.guhecloud.platform.base.model.Paging;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface PratiqueRegisterService extends IService<PratiqueRegister> {

    Long addPratiqueRegister(PratiqueRegisterModifyParam param);

    void updatePratiqueRegister(Long id, PratiqueRegisterModifyParam param);

    PratiqueRegister getById(Serializable id);

    PratiqueRegister getByPorkReport(Long porkReportId, Integer tenant);

    List<PratiqueRegister> getFailedPratiqueRegister(Collection<? extends Serializable> porkReportIds, Integer tenant);

    Page<PratiqueRegister> pageFailedPratiqueRegister(Paging paging, Collection<? extends Serializable> porkReportIds, Integer tenant);

    Page<PratiqueRegisterVO> pagePratiqueRegister(PratiqueRegisterQueryParam queryParam);

    List<PratiqueRegister> listPratiqueRegister(PratiqueRegisterQueryParam queryParam);
}
