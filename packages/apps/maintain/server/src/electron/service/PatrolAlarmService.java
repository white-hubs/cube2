package com.guhecloud.cube.srv.gen.maintain.server.electron.service;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolAlarmParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolAlarmVO;
import com.guhecloud.platform.base.model.Pagination;

public interface PatrolAlarmService {

    Pagination<PatrolAlarmVO> getPage(PatrolAlarmParam param);
}
