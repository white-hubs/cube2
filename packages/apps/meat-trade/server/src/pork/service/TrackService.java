package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Track;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TrackQueryParam;

public interface TrackService extends IService<Track> {

    Track getByPorkReport(Long porkReportId, Integer tenant);

    Page<Track> pageTrack(TrackQueryParam queryParam);

}
