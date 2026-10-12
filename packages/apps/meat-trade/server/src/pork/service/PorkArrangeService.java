package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TallyQueryParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface PorkArrangeService extends IService<PorkArrange> {

    Long addPorkArrange(PorkArrangeModifyParam param);

    PorkArrange getByPorkReport(Long porkReportId, Integer tenant);

    List<PorkArrange> listByPorkReportIds(Collection<? extends Serializable> porkReportIds, Integer tenant);

    List<PorkArrange> listByPorkReportCodes(Collection<? extends Serializable> porkReportCodes, Integer tenant);

    /**
     * 与理货审核关联查询
     *
     * @param queryParam
     *
     * @return
     */
    List<PorkArrange> listByComplex(StatQueryParam queryParam);

    /**
     * 查询制定商户的待处理理货批次
     *
     * @param queryParam 查询参数
     *
     * @return 理货批次信息
     */
    List<PorkArrange> getPendingTallies(TallyQueryParam queryParam, Collection<? extends Serializable> porkReportIds);

    List<PorkArrange> getTallies(Collection<? extends Serializable> porkReportIds);

    Page<PorkArrange> statPage(StatQueryParam queryParam);

    Integer getCheatCount(StatQueryParam queryParam);

    Integer getArrangeCount(StatQueryParam queryParam);

    Page<PorkArrange> pageMpPorkArrange(PorkArrangeQueryParam queryParam);

    Page<PorkArrange> pageBimPorkArrange(PorkArrangeQueryParam queryParam);

}
