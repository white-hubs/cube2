package com.guhecloud.cube.biz.fusion.communicate.server.gps.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.param.UploadGpsParam;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.param.UserGpsHistoriesQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.po.UserGpsHistoryPO;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.vo.UserGpsHistoriesVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface UserGpsHistoryService extends IService<UserGpsHistoryPO> {

    Long uploadUserGps(UploadGpsParam param);

    UserGpsHistoriesVO getUserLastGpsHistory(Long userId);

    List<UserGpsHistoriesVO> getUserLastGpsHistories(List<Long> userIds);

    Pagination<UserGpsHistoriesVO> getGpsHistories(UserGpsHistoriesQueryParam param);
}
