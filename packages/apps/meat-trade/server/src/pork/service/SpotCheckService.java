package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface SpotCheckService extends IService<SpotCheck> {

    Long addSpotCheck(SpotCheckModifyParam param);

    void updateSpotCheck(Long id, SpotCheckModifyParam param);

    SpotCheck getById(Serializable id);

    SpotCheck getByPorkReport(Long porkReportId, Integer tenant);

    Page<SpotCheck> pageSpotCheck(SpotCheckQueryParam queryParam);

    Page<SpotCheckVO> getSpotCheckPage(SpotCheckQueryParam queryParam);

    List<SpotCheck> listSpotCheck(SpotCheckQueryParam queryParam);

    List<SpotCheck> listByInspectors(Long inspectorId, Integer tenant);

    List<SpotCheck> listByPorkReports(Collection<? extends Serializable> porkReportIds, Integer tenant);
}
