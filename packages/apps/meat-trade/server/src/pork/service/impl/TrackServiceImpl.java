package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.TrackMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Track;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TrackQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.TrackService;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("meattrade_trackService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrackServiceImpl extends ServiceImpl<TrackMapper, Track> implements TrackService {

    @Override
    public Track getByPorkReport(@NonNull Long porkReportId, @NonNull Integer tenant) {
        return this.baseMapper.selectOne(Wrappers.<Track>lambdaQuery()
                                                 .eq(Track::getPorkReportId, porkReportId)
                                                 .eq(Track::getTenant, tenant));
    }

    @Override
    public Page<Track> pageTrack(TrackQueryParam queryParam) {
        return this.baseMapper.pageTrack(PagingUtils.convert(queryParam), queryParam);
    }
}
