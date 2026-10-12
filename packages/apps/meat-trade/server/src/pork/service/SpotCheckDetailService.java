package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckDetail;

import java.util.List;

public interface SpotCheckDetailService extends IService<SpotCheckDetail> {

    void addSpotCheckDetailBySpotCheck(Long spotCheck);

    List<SpotCheckDetail> getBySpotCheck(Long spotCheckId, Integer tenant);
}
