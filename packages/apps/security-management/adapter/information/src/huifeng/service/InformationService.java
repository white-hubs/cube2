package com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.service;

import com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.qo.InformationParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.PLayRecordVo;

import java.util.List;

public interface InformationService {
    List<PLayRecordVo> getPayLogs(InformationParam informationParam);
}
