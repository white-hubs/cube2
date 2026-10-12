package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Track;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TrackQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("meattrade-trackMapper")
public interface TrackMapper extends BaseMapper<Track> {

    Page<Track> pageTrack(Page<?> page, @Param("param") TrackQueryParam queryParam);

}
