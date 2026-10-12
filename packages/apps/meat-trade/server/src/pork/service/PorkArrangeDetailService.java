package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PlaceStatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeCheatModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TallyDataModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TrackDetailQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkDeviceStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryVO;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface PorkArrangeDetailService extends IService<PorkArrangeDetail> {

    void addTallyData(TallyDataModifyParam param);

    void updatePorkArrangeDetailCheat(PorkArrangeCheatModifyParam param);

    PorkArrangeDetail getByCode(String code, Period period, Integer tenant);

    List<PorkArrangeDetail> getByPorkReportCode(Serializable porkReportCode, Integer tenant);

    List<PorkArrangeDetail> listByPorkReportCodes(Collection<? extends Serializable> porkReportCodes, Integer tenant);

    Page<PorkArrangeDetail> listByPorkReportCodes(Collection<? extends Serializable> porkReportCodes, String keyword, Integer tenant,
                                                  Paging paging
                                                 );

    Page<PorkArrangeDetail> pageByTrack(TrackDetailQueryParam queryParam);

    List<PorkArrangeDetail> listPorkArrangeDetails(String code, Integer tenant);

    List<PorkArrangeDetail> listCheatPorkArrangeDetails(Collection<? extends Serializable> porkReportCodes, Integer tenant);

    Page<PorkArrangeDetail> pageCheatPorkArrangeDetails(PorkArrangeQueryParam queryParam);

    /**
     * 通过与理货审核联查 查询理货信息
     *
     * @param queryParam
     *
     * @return
     */
    Page<PorkArrangeDetail> pageByComplex(PorkArrangeQueryParam queryParam);

    List<PorkArrangeDetail> listByIds(Collection<? extends Serializable> ids);

    PorkStatSummaryVO statSupplyMeatInfo(StatQueryParam param);

    PorkArrangeDetail getLastPorkArrangeDetailByDeviceId(String deviceCode);

    List<PorkDeviceStatisticsVO> getDevicePorkArrangeStatistics(PlaceStatQueryParam queryParam);

    Long getPorkArrangeSlicesStatistics(StatQueryParam queryParam);
}
