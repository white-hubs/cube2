package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceRoomType;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceBaseParam;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 10:19
 * @Description:
 */
public interface ConferenceRoomTypeService extends IService<ConferenceRoomType> {

    Boolean saveOrUpdate(ConferenceBaseParam param);
}
