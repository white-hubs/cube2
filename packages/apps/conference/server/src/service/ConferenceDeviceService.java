package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceDevice;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceDeviceAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceDeviceQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceDeviceVo;
import com.guhecloud.platform.base.model.Pagination;

public interface ConferenceDeviceService extends IService<ConferenceDevice> {
    Pagination<ConferenceDeviceVo> getConferenceDeviceInfo(ConferenceDeviceQueryParam param);

    void insertConferenceDevice(ConferenceDeviceAddOrUpdateParam param);

    void updateConferenceDevice(ConferenceDeviceAddOrUpdateParam param);
}
